package in.ramanujam.services.redistomongo;

import in.ramanujam.common.MongoUtils;
import in.ramanujam.common.model.BitcoinRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RedisToMongoScheduledTask
{
    // TODO: how can we stop it from running after all records are persisted?
    @Scheduled(fixedDelay = 1000)
    public void runWithDelay() throws IOException
    {
        for( BitcoinRecord bitcoinRecord : RedisToMongoService.retrieveAllRecords() )
        {
            RedisToMongoService.moveRecordFromRedisToMongo( bitcoinRecord, MongoUtils.getCollection() );
        }
    }
}
