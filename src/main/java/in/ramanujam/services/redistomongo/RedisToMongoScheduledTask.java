package in.ramanujam.services.redistomongo;

import in.ramanujam.common.MongoUtils;
import in.ramanujam.common.messaging.MessageBus;
import in.ramanujam.common.model.BitcoinRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class RedisToMongoScheduledTask
{
    // TODO: how can we stop it from running after all records are persisted?
    @Scheduled(fixedDelay = 1000)
    public void runWithDelay() throws IOException
    {
        List<BitcoinRecord> records = RedisToMongoService.retrieveAllRecords();
        for( BitcoinRecord bitcoinRecord : records )
        {
            RedisToMongoService.moveRecordFromRedisToMongo( bitcoinRecord, MongoUtils.getCollection() );
        }

        if( records.size() == 0 && RedisToMongoService.isRedisFillerFinished() )
            MessageBus.getInstance().sendMessage( "RedisToMongoFinished" );
    }
}
