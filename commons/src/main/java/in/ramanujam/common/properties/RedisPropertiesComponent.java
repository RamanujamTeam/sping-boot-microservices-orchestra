package in.ramanujam.common.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RedisPropertiesComponent {

    @Value("${redis.container.name}")
    private String redisContainerName;

    @Value("${redis.container.host}")
    private String redisContainerHost;

    @Value("${redis.container.port}")
    private int redisContainerPort;

    @Value("${redis.container.externalPort}")
    private int redisContainerExternalPort;

    @Value("${redis.hashset.name}")
    private String redisHashSetName;

    @Value("${redis.isfinished.key}")
    private String redisIsFinishedKey;

    @Value("${redis.tomongo.isfinished.key}")
    private String redisToMongoIsFinishedKey;

    public String getRedisContainerName() {
        return redisContainerName;
    }

    public String getRedisContainerHost() {
        return redisContainerHost;
    }

    public int getRedisContainerPort() {
        return redisContainerPort;
    }

    public int getRedisContainerExternalPort() {
        return redisContainerExternalPort;
    }

    public String getRedisHashsetName() {
        return redisHashSetName;
    }

    public String getRedisIsFinishedKey() {
        return redisIsFinishedKey;
    }

    public String getRedisToMongoIsFinishedKey() {
        return redisToMongoIsFinishedKey;
    }
}