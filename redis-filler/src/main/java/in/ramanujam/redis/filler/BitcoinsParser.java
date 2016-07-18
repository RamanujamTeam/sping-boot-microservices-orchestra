package in.ramanujam.redis.filler;

import in.ramanujam.common.model.BitcoinRecord;
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class BitcoinsParser {
    public List<BitcoinRecord> parseRecords(File xmlFile, int skip, int limit) throws Exception {
        XMLStreamReader reader = null;
        try (FileReader fileReader = new FileReader(xmlFile)) {
            reader = XMLInputFactory.newInstance().createXMLStreamReader(fileReader);

            BitcoinRecord currBitcoin = new BitcoinRecord();
            List<BitcoinRecord> bitcoins = new ArrayList<>();
            String tagContent = "";
            int entitiesParsed = 0;

            while (reader.hasNext()) {
                int event = reader.next();

                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        if ("record".equals(reader.getLocalName()))
                            currBitcoin = new BitcoinRecord();
                        break;

                    case XMLStreamConstants.CHARACTERS:
                        tagContent = reader.getText().trim();
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        switch (reader.getLocalName()) {
                            case "record":
                                if (++entitiesParsed > skip)
                                    bitcoins.add(currBitcoin);
                                if (entitiesParsed - (skip + limit) >= 0)
                                    return bitcoins;
                                break;
                            case "id":
                                currBitcoin.setId(Integer.parseInt(tagContent));
                                break;
                            case "bitcoin":
                                currBitcoin.setKey(tagContent);
                                break;
                        }
                        break;
                }
            }
            return bitcoins;
        } finally {
            if (reader != null)
                reader.close();
        }
    }
}
