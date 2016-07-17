package in.ramanujam.redis.filler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan
public class RedisFillerStarter {

    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        context = SpringApplication.run(RedisFillerStarter.class, args);
        new RedisFiller().writeIsFinished( false );
    }

    public static void shutdown()
    {
        context.close();
    }
}
