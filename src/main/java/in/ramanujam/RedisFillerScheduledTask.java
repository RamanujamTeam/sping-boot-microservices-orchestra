package in.ramanujam;

import in.ramanujam.properties.DockerProperties;
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
    @Scheduled(fixedDelay = 30000) // TODO: add batching
    public void runWithDelay() throws ParserConfigurationException, IOException, SAXException {
        RedisProperties redisProperties = new RedisProperties();
        DockerProperties dockerProperties = new DockerProperties();

        Jedis jedis = new Jedis(dockerProperties.getDockerHost(), redisProperties.getContainerRedisExternalPort());
        File fXmlFile = redisDataFile.getFile();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);

        NodeList records = doc.getDocumentElement().getChildNodes();

        int lastIndex = Math.min(currentPosition + 100, records.getLength());
        while (currentPosition < lastIndex){
            String id = records.item(currentPosition).getChildNodes().item(0).getChildNodes().item(0).getNodeValue();
            String bitcoin = records.item(currentPosition).getChildNodes().item(1).getChildNodes().item(0).getNodeValue();
            currentPosition++;
            jedis.set(id, bitcoin);
        }
        System.out.println( "Added " + lastIndex + " records" );
    }
}
