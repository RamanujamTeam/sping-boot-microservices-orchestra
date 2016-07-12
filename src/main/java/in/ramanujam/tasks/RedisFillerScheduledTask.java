package in.ramanujam.tasks;

import in.ramanujam.common.model.BitcoinRecord;
import in.ramanujam.parsers.BitcoinsParser;
import in.ramanujam.services.fillers.RedisFiller;
import in.ramanujam.starters.RedisFillerStarter;
import in.ramanujam.services.fillers.RedisFiller;
import in.ramanujam.starters.RedisFillerStarter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.io.File;
import java.util.List;

/**
 * @author Roma
 */
@Component
public class RedisFillerScheduledTask {

    @Value("redis-data.xml")
    private Resource redisDataFile;

    private int processedBitcoins = 0;
    // TODO: add batching
    @Scheduled(fixedDelay = 100) // TODO: 30 secs
    public void runWithDelay() throws Exception
    {
        File xmlFile = redisDataFile.getFile();
        BitcoinsParser bitcoinsParser = new BitcoinsParser();
        List<BitcoinRecord> bitcoins = bitcoinsParser.parseRecords( xmlFile, processedBitcoins, 100 );
        bitcoins.forEach( RedisFiller::addBitcoin );
        if( bitcoins.isEmpty() )
        {
            RedisFiller.writeIsFinished( true );
            System.out.println("RedisFiller :: Successfully finished!");
            RedisFillerStarter.shutdown();
        }
    }
}
