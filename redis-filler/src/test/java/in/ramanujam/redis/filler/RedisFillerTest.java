package in.ramanujam.redis.filler;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import in.ramanujam.common.docker.DockerClientFactory;
import in.ramanujam.common.docker.DockerUtils;
import in.ramanujam.common.model.BitcoinRecord;
import in.ramanujam.common.properties.RedisProperties;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RedisFillerTest {

    @Autowired
    RedisFiller redisFiller;

    private DockerClient dockerClient;
    private CreateContainerResponse redisContainer;

    @BeforeClass
    public void startRedisContainer() {
        dockerClient = DockerClientFactory.getClient();
        redisContainer = DockerUtils.getRedisContainer(dockerClient); // TODO: replace CreateContainerResponse with container id
        DockerUtils.tryToStartContainer(dockerClient, redisContainer);
    }

    @Test
    public void testAddBitcoins() {
        // Prepare Bitcoins
        List<BitcoinRecord> bitcoins = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            bitcoins.add(new BitcoinRecord(i, "key" + i));

        redisFiller.addBitcoins(bitcoins);

        // test for Bitcoins in Redis
        Jedis jedis = new Jedis(RedisProperties.getInstance().getRedisContainerHost(),
                RedisProperties.getInstance().getRedisContainerExternalPort());
        Map<String, String> result = jedis.hgetAll(RedisProperties.getInstance().getRedisHashsetName());
        System.out.println("Good?");
    }


    @AfterClass
    public void stopRedisContainer() {
        DockerUtils.tryToStopContainer(dockerClient, redisContainer);
    }
}
