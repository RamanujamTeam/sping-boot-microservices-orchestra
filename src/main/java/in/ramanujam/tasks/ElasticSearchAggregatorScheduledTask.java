package in.ramanujam.tasks;

import in.ramanujam.common.util.MongoUtils;
import in.ramanujam.common.messaging.MessageBus;
import in.ramanujam.common.model.MinerRecord;
import in.ramanujam.common.properties.ElasticSearchProperties;
import in.ramanujam.services.aggregators.ElasticSearchAggregator;
import in.ramanujam.starters.ElasticSearchToMongoStarter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class ElasticSearchAggregatorScheduledTask
{
    // TODO: how can we stop it from running after all records are persisted?
    @Scheduled(fixedDelay = 100)
    public void runWithDelay() throws IOException
    {
        List<MinerRecord> records = ElasticSearchAggregator.getInstance().retrieveAllRecords();
        for( MinerRecord esRecord : records )
        {
            ElasticSearchAggregator.getInstance().
                    moveRecordFromElasticSearchToMongo( esRecord, MongoUtils.getCollection() );
        }
            // TODO: replace these conditions:
        if( records.size() == 1 && records.get( 0 ).equals( new MinerRecord(  ) )
                && ElasticSearchAggregator.getInstance().isElasticSearchFillerFinished() ) // TODO: это фикс для бага - при записи в ES создается еще одна запись со всеми параметрами null
        {
            MessageBus.getInstance().sendMessage( ElasticSearchProperties.getInstance().getElasticsearchToMongoIsFinishedKey() );
            System.out.println( "ElasticSearchToMongo :: Successfully finished!");
            ElasticSearchToMongoStarter.shutdown();
        }
    }
}
