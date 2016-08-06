package in.ramanujam.redis.aggregator;

import in.ramanujam.common.messaging.MessageBus;
import in.ramanujam.common.model.BitcoinRecord;
import in.ramanujam.common.properties.RedisProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class RedisAggregatorScheduledTask {
    private static final Logger log = LoggerFactory.getLogger(RedisAggregatorScheduledTask.class);
    @Autowired
    private RedisAggregator aggregator;
    @Autowired
    private MongoUtils mongoUtils;
    @Autowired
    private RedisProperties redisProps;
    @Autowired
    private MessageBus messageBus;

    @Scheduled(fixedDelay = 30_000)
    public void runWithDelay() throws IOException {
        List<BitcoinRecord> records = aggregator.retrieveRecords(100);
        for (BitcoinRecord bitcoinRecord : records) {
            aggregator.moveRecordFromRedisToMongo(bitcoinRecord, mongoUtils.getCollection());
        }

        if (records.size() == 0 && aggregator.isRedisFillerFinished()) {
            messageBus.sendMessage(redisProps.getRedisToMongoIsFinishedKey());
            log.info("RedisToMongo :: Successfully finished!");
            RedisAggregatorStarter.shutdown();
        }
    }
}
