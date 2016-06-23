package in.ramanujam;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import redis.clients.jedis.Jedis;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class RedisFiller {

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

        Jedis jedis = new Jedis("localhost", 7777);
        File fXmlFile = new File("/home/anatolii/translator-service/resources/redis-data.xml"); // TODO unhardcode path
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);

        NodeList records = doc.getDocumentElement().getChildNodes();

        for (int i = 1; i < records.getLength(); i++){ // TODO: add scheduling
            String id = records.item(i).getChildNodes().item(0).getChildNodes().item(0).getNodeValue();
            String bitcoin = records.item(i).getChildNodes().item(1).getChildNodes().item(0).getNodeValue();
            jedis.set(id, bitcoin);
        }
    }

}
