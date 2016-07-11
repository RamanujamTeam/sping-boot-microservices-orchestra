package in.ramanujam.services.redistomongo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RedisToMongoStarter
{

    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        context = SpringApplication.run( RedisToMongoStarter.class, args);
    }
    public static void shutdown()
    {
        context.close();
    }
}
