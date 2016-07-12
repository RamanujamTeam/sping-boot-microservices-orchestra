package in.ramanujam.services.elasticsearch.filler.redis.filler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RedisFillerStarter {

    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        context = SpringApplication.run(RedisFillerStarter.class, args);
        RedisFiller.writeIsFinished( false );
    }

    public static void shutdown()
    {
        context.close();
    }
}
