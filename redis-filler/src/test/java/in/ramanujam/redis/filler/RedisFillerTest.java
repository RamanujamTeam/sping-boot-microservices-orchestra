package in.ramanujam.redis.filler;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import in.ramanujam.common.docker.DockerClientFactory;
import in.ramanujam.common.docker.DockerUtils;
import in.ramanujam.common.model.BitcoinRecord;
import in.ramanujam.common.properties.RedisProperties;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RedisFillerStarter.class)
public class RedisFillerTest {

    @Autowired
    RedisFiller redisFiller;

    private static DockerClient dockerClient;
    private static CreateContainerResponse redisContainer;
    private static RedisProperties props = RedisProperties.getInstance();
    private static Jedis jedis;

    @BeforeClass
    public static void setup() {
        dockerClient = DockerClientFactory.getClient();
        redisContainer = DockerUtils.getRedisContainer(dockerClient); // TODO: replace CreateContainerResponse with container id
        DockerUtils.tryToStartContainer(dockerClient, redisContainer);
        jedis = new Jedis(props.getRedisContainerHost(), props.getRedisContainerExternalPort());
    }

    @Test
    public void testAddBitcoins() {
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

    @AfterClass
    public static void tearDown() {
        DockerUtils.tryToStopContainer(dockerClient, redisContainer);
        jedis.close();
    }
}
