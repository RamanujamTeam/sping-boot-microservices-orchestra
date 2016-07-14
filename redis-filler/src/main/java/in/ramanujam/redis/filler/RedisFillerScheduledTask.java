package in.ramanujam.redis.filler;

import in.ramanujam.common.model.BitcoinRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Component
public class RedisFillerScheduledTask {

    @Value("redis-data.xml")
    private Resource redisDataFile;

    private int curPos = 0;
    // TODO: add StAX
    @Scheduled(fixedDelay = 100) // TODO: 30 secs
    public void runWithDelay() throws ParserConfigurationException, IOException, SAXException {
        File fXmlFile = redisDataFile.getFile();
        String trimmedXML = String.join("", Files.readAllLines(fXmlFile.toPath(), StandardCharsets.UTF_8))
            .replaceAll(">\\s+<", "><").trim();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse( new InputSource(new ByteArrayInputStream(trimmedXML.getBytes("utf-8"))) );

        NodeList records = doc.getDocumentElement().getChildNodes();

        int lastIndex = Math.min( curPos + 100, records.getLength() );
        List<BitcoinRecord> bitcoinRecords = new ArrayList<>();
        while ( curPos < lastIndex){
            String id = records.item( curPos ).getChildNodes().item( 0).getChildNodes().item( 0).getNodeValue();
            String key = records.item( curPos ).getChildNodes().item( 1).getChildNodes().item( 0).getNodeValue();
            bitcoinRecords.add(new BitcoinRecord( Integer.valueOf( id ), key ));
            curPos++;
        }
        RedisFiller.addBitcoins( bitcoinRecords );

        if( curPos >= records.getLength() )
        {
            RedisFiller.writeIsFinished( true );
            System.out.println( "RedisFiller :: Successfully finished!");
            RedisFillerStarter.shutdown();
        }
    }
}
