package in.ramanujam.redis.aggregator;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;
import in.ramanujam.common.model.BitcoinRecord;
import in.ramanujam.common.properties.RedisProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RedisAggregator {
    private static final Logger log = LoggerFactory.getLogger(RedisAggregator.class);
    private static int writeCount = 0;
    private static int removeCount = 0;
                            // TODO: remove all proprties files in the project
    @Autowired
    private RedisProperties redisProps;

    public List<BitcoinRecord> retrieveRecords(int size) {
        List<BitcoinRecord> bitcoins = new ArrayList<>();
        try (Jedis jedis = new Jedis(redisProps.getRedisContainerHost(),
                redisProps.getRedisContainerExternalPort())) {
            String cur = redis.clients.jedis.ScanParams.SCAN_POINTER_START;
            ScanParams params = new ScanParams().count(size);

            ScanResult<Map.Entry<String, String>> scanResult = jedis.hscan(redisProps.getRedisHashsetName(), cur, params);
            bitcoins = scanResult.getResult().stream()
                    .map(entry -> new BitcoinRecord(Integer.parseInt(entry.getKey()), entry.getValue()))
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
        }
        return bitcoins;
    }

    public void removeRecordFromRedis(BitcoinRecord redisRecord) {
        Jedis jedis = new Jedis(redisProps.getRedisContainerHost(),
                redisProps.getRedisContainerExternalPort());
        jedis.hdel(redisProps.getRedisHashsetName(), String.valueOf(redisRecord.getId()));
        jedis.close();
        log.info("RedisToMongo :: Removed from Redis :: Id = " + redisRecord.getId() + " count = " + ++removeCount);
    }

    public void moveRecordFromRedisToMongo(BitcoinRecord redisRecord, DBCollection collection) {
        try {
            writeRedisRecordToMongo(redisRecord, collection);
            removeRecordFromRedis(redisRecord);
        } catch (Exception e) {
        }
    }

    private WriteResult writeRedisRecordToMongo(BitcoinRecord redisRecord, DBCollection collection) {
        BasicDBObject document = new BasicDBObject();

        BasicDBObject values = new BasicDBObject()
                .append("bitcoin", redisRecord.getKey());

        document.append("$set", values);

        BasicDBObject searchQuery = new BasicDBObject().append("_id", redisRecord.getId());
        WriteResult result = collection.update(searchQuery, document, true, false);
        log.info("RedisToMongo :: Wrote to Mongo :: Id = " + redisRecord.getId() + " count = " + ++writeCount);
        return result;
    }

    public Boolean isRedisFillerFinished() {
        Jedis jedis = new Jedis(redisProps.getRedisContainerHost(),
                redisProps.getRedisContainerExternalPort());
        String isRedisFillerFinished = jedis.get(redisProps.getRedisIsFinishedKey());
        jedis.close();
        return Boolean.valueOf(isRedisFillerFinished);
    }
}