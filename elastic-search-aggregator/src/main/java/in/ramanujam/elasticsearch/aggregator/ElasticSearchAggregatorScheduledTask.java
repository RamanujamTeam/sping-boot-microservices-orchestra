package in.ramanujam.elasticsearch.aggregator;

import in.ramanujam.common.messaging.MessageBus;
import in.ramanujam.common.model.MinerRecord;
import in.ramanujam.common.properties.ElasticSearchProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class ElasticSearchAggregatorScheduledTask
{
    private static final Logger log = LoggerFactory.getLogger(ElasticSearchAggregatorScheduledTask.class);
    @Scheduled(fixedDelay = 100)
    public void runWithDelay() throws IOException
    {
        List<MinerRecord> records = ElasticSearchAggregator.getInstance().retrieveAllRecords();

        records.stream().filter( record -> !record.equals(new MinerRecord()) )
                .forEach(record -> ElasticSearchAggregator.getInstance().
                        moveRecordFromElasticSearchToMongo(record, MongoUtils.getCollection()));

        if( noMoreRecords( records ) )
        {
            MessageBus.getInstance().sendMessage( ElasticSearchProperties.getInstance().getElasticsearchToMongoIsFinishedKey() );
            log.info("ElasticSearchToMongo :: Successfully finished!");
            ElasticSearchToMongoStarter.shutdown();
        }
    }

    private boolean noMoreRecords( List records )
    {
        return ElasticSearchAggregator.getInstance().isElasticSearchFillerFinished()
                && records.size() == 1;
    }
}
