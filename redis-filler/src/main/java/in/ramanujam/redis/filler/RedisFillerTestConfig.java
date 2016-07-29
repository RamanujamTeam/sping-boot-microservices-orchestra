package in.ramanujam.redis.filler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class RedisFillerTestConfig {
    public static void main(String[] args) {
        SpringApplication.run(RedisFillerTestConfig.class, args);
        new RedisFiller().writeIsFinished(false);
    }
}
