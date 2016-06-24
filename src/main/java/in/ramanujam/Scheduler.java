package in.ramanujam;

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
public class Scheduler {

    @Value("redis-data.xml")
    private Resource redis;

    @Scheduled(fixedDelay = 1000)
    public void runWithDelay() throws ParserConfigurationException, IOException, SAXException {
        System.out.println("Hello!!!");

        Jedis jedis = new Jedis("localhost", 7777);
        File fXmlFile = redis.getFile();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);

        NodeList records = doc.getDocumentElement().getChildNodes();

        for (int i = 1; i < records.getLength(); i++){
            String id = records.item(i).getChildNodes().item(0).getChildNodes().item(0).getNodeValue();
            String bitcoin = records.item(i).getChildNodes().item(1).getChildNodes().item(0).getNodeValue();
            System.out.println(id);
            System.out.println(bitcoin);
            //jedis.set(id, bitcoin);
        }
    }
}
