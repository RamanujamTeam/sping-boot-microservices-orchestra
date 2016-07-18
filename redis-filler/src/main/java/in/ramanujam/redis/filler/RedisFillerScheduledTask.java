package in.ramanujam.redis.filler;

import in.ramanujam.common.model.BitcoinRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class RedisFillerScheduledTask {
    private static final Logger log = LoggerFactory.getLogger(RedisFillerScheduledTask.class);
    @Autowired
    RedisFiller filler;
    @Value("redis-data.xml")
    private Resource redisDataFile;
    @Autowired
    private BitcoinsParser parser;
    private int entriesParsed = 0;
    private int batchSize = 100;

    @Scheduled(fixedDelay = 100) // TODO: 30 secs
    public void runWithDelay() throws Exception {
        File xmlFile = redisDataFile.getFile();

        List<BitcoinRecord> bitcoins = parser.parseRecords(xmlFile, entriesParsed, batchSize);
        filler.addBitcoins(bitcoins);
        entriesParsed += bitcoins.size();

        if (bitcoins.isEmpty()) {
            filler.writeIsFinished(true);
            log.info("RedisFiller :: Successfully finished!");
            RedisFillerStarter.shutdown();
        }
    }
}
