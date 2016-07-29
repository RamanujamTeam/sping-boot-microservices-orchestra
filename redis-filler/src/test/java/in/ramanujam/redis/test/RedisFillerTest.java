package in.ramanujam.redis.test;

import in.ramanujam.common.model.BitcoinRecord;
import in.ramanujam.redis.filler.RedisFiller;
import in.ramanujam.redis.filler.RedisFillerTestConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RedisFillerTestConfig.class)
public class RedisFillerTest extends AbstractRedisFillerTest{

    @Autowired
    RedisFiller redisFiller;

    @Test
    public void testAddBitcoins() {
        jedis.flushAll();
        // Prepare Bitcoins
        List<BitcoinRecord> bitcoins = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            bitcoins.add(new BitcoinRecord(i, "key" + i));

        redisFiller.addBitcoins(bitcoins);

        // test for Bitcoins in Redis
        Map<String, String> result = jedis.hgetAll(props.getRedisHashsetName());
        List<BitcoinRecord> bitcoinsFromRedis = result.entrySet().stream().map(entry -> new BitcoinRecord(Integer.valueOf(entry.getKey()), entry.getValue())).collect(Collectors.toList());

        Collections.sort(bitcoins);
        Collections.sort(bitcoinsFromRedis);
        Assert.assertEquals(bitcoins, bitcoinsFromRedis);
    }

    @Test
    public void testWriteIsFinished() {
        redisFiller.writeIsFinished(true);
        String finishedTrueString = jedis.get(props.getRedisIsFinishedKey());
        Assert.assertTrue(Boolean.valueOf(finishedTrueString));

        redisFiller.writeIsFinished(false);
        String finishedFalseString = jedis.get(props.getRedisIsFinishedKey());
        Assert.assertFalse(Boolean.valueOf(finishedFalseString));
    }
}
