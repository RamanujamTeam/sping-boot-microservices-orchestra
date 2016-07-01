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
public class MongoAggregatorTask
{

    @Value("redis-data.xml")
    private Resource redisDataFile;

    private int currentPosition = 1;

    @Scheduled(fixedDelay = 301000) // TODO: add batching
    public void runWithDelay() throws ParserConfigurationException, IOException, SAXException {

    }
}
