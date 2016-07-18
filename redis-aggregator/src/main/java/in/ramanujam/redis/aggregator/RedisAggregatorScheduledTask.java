package in.ramanujam.redis.aggregator;

import in.ramanujam.common.messaging.MessageBus;
import in.ramanujam.common.model.BitcoinRecord;
import in.ramanujam.common.properties.RedisProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class RedisAggregatorScheduledTask
{
    private static final Logger log = LoggerFactory.getLogger( RedisAggregatorScheduledTask.class );
    @Scheduled(fixedDelay = 100)
    public void runWithDelay() throws IOException
    {
        List<BitcoinRecord> records = RedisAggregator.retrieveRecords( 100 );
        for( BitcoinRecord bitcoinRecord : records )
        {
            RedisAggregator.moveRecordFromRedisToMongo( bitcoinRecord, MongoUtils.getCollection() );
        }

        if( records.size() == 0 && RedisAggregator.isRedisFillerFinished() )
        {
            MessageBus.getInstance().sendMessage( RedisProperties.getInstance().getRedisToMongoIsFinishedKey() );
            log.info( "RedisToMongo :: Successfully finished!");
            RedisAggregatorStarter.shutdown();
        }
    }
}
