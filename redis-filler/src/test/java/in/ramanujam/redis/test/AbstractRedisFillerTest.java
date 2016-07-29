package in.ramanujam.redis.test;

import com.github.dockerjava.api.DockerClient;
import in.ramanujam.common.docker.DockerClientFactory;
import in.ramanujam.common.docker.DockerUtils;
import in.ramanujam.common.properties.RedisProperties;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import redis.clients.jedis.Jedis;

public class AbstractRedisFillerTest {

    private static DockerClient dockerClient;
    private static String redisContainerId;
    protected static RedisProperties props = RedisProperties.getInstance();
    protected static Jedis jedis;

    @BeforeClass
    public static void setup() {
        dockerClient = DockerClientFactory.getClient();
        redisContainerId = DockerUtils.getRedisContainerId(dockerClient);
        DockerUtils.tryToStartContainer(dockerClient, redisContainerId);
        jedis = new Jedis(props.getRedisContainerHost(), props.getRedisContainerExternalPort());
        jedis.flushAll();
    }

    @AfterClass
    public static void tearDown() {
        DockerUtils.tryToStopContainer(dockerClient, redisContainerId);
        jedis.flushAll();
        jedis.close();
    }
}
