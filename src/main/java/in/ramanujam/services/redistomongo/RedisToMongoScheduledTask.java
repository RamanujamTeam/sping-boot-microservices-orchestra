package in.ramanujam.services.redistomongo;

import in.ramanujam.common.util.MongoUtils;
import in.ramanujam.common.messaging.MessageBus;
import in.ramanujam.common.model.BitcoinRecord;
import in.ramanujam.common.properties.RedisProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class RedisToMongoScheduledTask
{
    // TODO: how can we stop it from running after all records are persisted?
    @Scheduled(fixedDelay = 100)
    public void runWithDelay() throws IOException
    {
        List<BitcoinRecord> records = RedisToMongoService.retrieveAllRecords();
        for( BitcoinRecord bitcoinRecord : records )
        {
            RedisToMongoService.moveRecordFromRedisToMongo( bitcoinRecord, MongoUtils.getCollection() );
        }

        if( records.size() == 0 && RedisToMongoService.isRedisFillerFinished() )
        {
            MessageBus.getInstance().sendMessage( RedisProperties.getInstance().getRedisToMongoIsFinishedKey() );
            System.out.println( "RedisToMongo :: Successfully finished!");
            RedisToMongoStarter.shutdown();
        }
    }
}
