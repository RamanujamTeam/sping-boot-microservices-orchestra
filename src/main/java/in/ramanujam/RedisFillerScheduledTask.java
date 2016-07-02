package in.ramanujam;

import in.ramanujam.properties.DockerProperties;
import in.ramanujam.properties.RedisProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import redis.clients.jedis.Jedis;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
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
    public void runWithDelay() throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        RedisProperties redisProperties = new RedisProperties();
        DockerProperties dockerProperties = new DockerProperties();

        Jedis jedis = new Jedis(dockerProperties.getDockerHost(), redisProperties.getContainerRedisExternalPort());
        File fXmlFile = redisDataFile.getFile();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse( fXmlFile );


        NodeList records = trimSpacesFromXml(doc); //todo fixme

        records = doc.getDocumentElement().getChildNodes(); //todo fixme

        int lastIndex = Math.min( currentPosition + 100, records.getLength() );
        String hashSetName = "bitcoins";
        while (currentPosition < lastIndex){ // TODO: replace with reading logic that does not rely on input file formatting
            String id = records.item(currentPosition).getChildNodes().item(0).getChildNodes().item(0).getNodeValue();
            String bitcoin = records.item(currentPosition).getChildNodes().item(1).getChildNodes().item(0).getNodeValue();
            currentPosition++;
            jedis.hset( hashSetName, id, bitcoin );
        }
        System.out.println( "Added " + lastIndex + " records" );
    }

    private NodeList trimSpacesFromXml(Document doc) throws XPathExpressionException {
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPathExpression xpathExp = xpathFactory.newXPath().compile(
            "//text()[normalize-space(.) = '']"
        );
        NodeList records = (NodeList) xpathExp.evaluate(doc, XPathConstants.NODESET);

        for (int i = 0; i < records.getLength(); i++) {
            Node emptyTextNode = records.item(i);
            emptyTextNode.getParentNode().removeChild(emptyTextNode);
        }
        return records;
    }
}
