package in.ramanujam;

import in.ramanujam.properties.RedisProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import redis.clients.jedis.Jedis;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * @author Roma
 */
@Component
public class RedisFillerScheduledTask {

    @Value("redis-data.xml")
    private Resource redisDataFile;

    private int currentPosition = 1;
    // TODO: add StAX
    @Scheduled(fixedDelay = 1000) // TODO: add batching
    public void runWithDelay() throws ParserConfigurationException, IOException, SAXException {
        RedisProperties redisProperties = new RedisProperties();

        Jedis jedis = new Jedis( redisProperties.getRedisContainerHost(), redisProperties.getRedisContainerExternalPort());
        File fXmlFile = redisDataFile.getFile();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse( fXmlFile );

        NodeList records = doc.getDocumentElement().getChildNodes();

        int lastIndex = Math.min( currentPosition + 100, records.getLength() );
        while (currentPosition < lastIndex){ // TODO: replace with reading logic that does not rely on input file formatting
            String id = records.item(currentPosition).getChildNodes().item(0).getChildNodes().item(0).getNodeValue();
            String bitcoin = records.item(currentPosition).getChildNodes().item(1).getChildNodes().item(0).getNodeValue();
            currentPosition++;
            jedis.hset( new RedisProperties().getRedisHashsetName(), id, bitcoin );
        }
    }
}
