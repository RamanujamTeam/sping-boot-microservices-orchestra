package in.ramanujam.redis.filler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan({"in.ramanujam.redis.filler", "in.ramanujam.common.properties"})
public class RedisFillerStarter {

    private static ConfigurableApplicationContext context;

    @Autowired
    private void cleanIsFinishedKey(RedisFiller redisFiller)
    {
        redisFiller.writeIsFinished(false);
    }

    public static void main(String[] args) {
        context = SpringApplication.run(RedisFillerStarter.class, args);
    }

    public static void shutdown() {
        context.close();
    }
}
