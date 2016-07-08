package in.ramanujam;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import in.ramanujam.model.MinerRecord;
import in.ramanujam.model.MongoDBRecord;
import in.ramanujam.model.BitcoinRecord;
import in.ramanujam.properties.MongoDBProperties;
import in.ramanujam.service.aggregator.MongoAggregator;
import in.ramanujam.service.MongoDBRecordsMerger;
import in.ramanujam.service.retriever.ElasticSearchRetriever;
import in.ramanujam.service.retriever.RedisRetriever;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

/**
 * @author Roma
 */
@Component
public class MongoAggregatorTask
{
    //@Scheduled(fixedDelay = 301000)
    @Scheduled(fixedDelay = 10000)
    public void runWithDelay() throws ParserConfigurationException, IOException, SAXException {

        List<MongoDBRecord> records = retrieveMongoDBRecords();
        MongoAggregator.getInstance().aggregate( records );
        MongoAggregator.getInstance().close();
    }

    private List<MongoDBRecord> retrieveMongoDBRecords()
    {
        List<BitcoinRecord> redisRecords = RedisRetriever.retrieveAllRecords();
        List<MinerRecord> elasticSearchRecords = ElasticSearchRetriever.retrieveAllRecords();
        return MongoDBRecordsMerger.merge( redisRecords, elasticSearchRecords );
    }
}
