package in.ramanujam.elasticsearch.aggregator;

import in.ramanujam.common.messaging.MessageBus;
import in.ramanujam.common.model.MinerRecord;
import in.ramanujam.common.properties.ElasticSearchProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class ElasticSearchAggregatorScheduledTask
{
    @Scheduled(fixedDelay = 100)
    public void runWithDelay() throws IOException
    {
        List<MinerRecord> records = ElasticSearchAggregator.getInstance().retrieveAllRecords();
        for( MinerRecord esRecord : records )
        {
            if( !esRecord.equals( new MinerRecord()) )
            {
                ElasticSearchAggregator.getInstance().
                        moveRecordFromElasticSearchToMongo( esRecord, MongoUtils.getCollection() );
            }
        }

        if( ElasticSearchAggregator.getInstance().isElasticSearchFillerFinished() && records.size() == 1 )
        {
            MessageBus.getInstance().sendMessage( ElasticSearchProperties.getInstance().getElasticsearchToMongoIsFinishedKey() );
            System.out.println( "ElasticSearchToMongo :: Successfully finished!");
            ElasticSearchToMongoStarter.shutdown();
        }
    }
}
