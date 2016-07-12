package in.ramanujam.services.elasticsearch.filler.redis.filler;

import in.ramanujam.common.model.BitcoinRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

@Component
public class RedisFillerScheduledTask {

    @Value("redis-data.xml")
    private Resource redisDataFile;

    private int curPos = 1;
    // TODO: add StAX
    @Scheduled(fixedDelay = 100) // TODO: 30 secs
    public void runWithDelay() throws ParserConfigurationException, IOException, SAXException {
// TODO: add batching
        File fXmlFile = redisDataFile.getFile();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse( fXmlFile );

        NodeList records = doc.getDocumentElement().getChildNodes();

        int lastIndex = Math.min( curPos + 100, records.getLength() );
        while ( curPos < lastIndex){ // TODO: replace with reading logic that does not rely on input file formatting
            String id = records.item( curPos ).getChildNodes().item( 0).getChildNodes().item( 0).getNodeValue();
            String key = records.item( curPos ).getChildNodes().item( 1).getChildNodes().item( 0).getNodeValue();
            RedisFiller.addBitcoin( new BitcoinRecord( Integer.valueOf( id ), key ) );
            curPos++;
        }

        if( curPos >= records.getLength() )
        {
            RedisFiller.writeIsFinished( true );
            System.out.println( "RedisFiller :: Successfully finished!");
            RedisFillerStarter.shutdown();
        }
    }
}
