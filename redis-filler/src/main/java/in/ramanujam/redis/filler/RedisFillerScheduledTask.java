package in.ramanujam.redis.filler;

import in.ramanujam.common.model.BitcoinRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Collection;

@Component
public class RedisFillerScheduledTask {
    private static final Logger log = LoggerFactory.getLogger( RedisFillerScheduledTask.class );

    @Value("redis-data.xml")
    private Resource redisDataFile;

    @Autowired
    private BitcoinsParser parser;

    @Autowired
    RedisFiller filler;
    private int entriesParsed = 0;
    private int batchSize = 100;
    @Scheduled(fixedDelay = 100) // TODO: 30 secs
    public void runWithDelay() throws Exception
    {
        File xmlFile = redisDataFile.getFile();

        List<BitcoinRecord> bitcoins = parser.parseRecords(xmlFile, entriesParsed, batchSize);
        filler.addBitcoins(bitcoins);
        entriesParsed += bitcoins.size();

        if( bitcoins.isEmpty() )
        {
            filler.writeIsFinished( true );
            log.info("RedisFiller :: Successfully finished!");
            RedisFillerStarter.shutdown();
        }
    }
}
