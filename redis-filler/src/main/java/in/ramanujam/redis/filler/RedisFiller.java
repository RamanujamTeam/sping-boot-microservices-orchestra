package in.ramanujam.redis.filler;

import in.ramanujam.common.model.BitcoinRecord;
import in.ramanujam.common.properties.RedisProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.List;

@Component
public class RedisFiller {
    private static final Logger log = LoggerFactory.getLogger(RedisFiller.class);

    private static int count = 0;

    @Autowired
    RedisProperties redisProps;

    public void addBitcoins(List<BitcoinRecord> bitcoins) {
        try (Jedis jedis = new Jedis(redisProps.getRedisContainerHost(),
                redisProps.getRedisContainerExternalPort())) {
            Pipeline pipeline = jedis.pipelined();
            bitcoins.forEach(
                    b -> {
                        pipeline.hset(redisProps.getRedisHashsetName(), b.getId().toString(), b.getKey());
                        log.info("RedisFiller :: Id = " + b.getId() + " count = " + ++count);
                    }
            );
            pipeline.sync();
            log.info("Bitcoin batch: " + bitcoins.size());
        }
    }

    public void writeIsFinished(boolean isFinished) {
        Jedis jedis = new Jedis(redisProps.getRedisContainerHost(),
                redisProps.getRedisContainerExternalPort());
        jedis.set(redisProps.getRedisIsFinishedKey(), String.valueOf(isFinished));
        jedis.close();
    }
}
