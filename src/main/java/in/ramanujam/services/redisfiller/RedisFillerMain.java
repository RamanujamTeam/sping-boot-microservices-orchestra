package in.ramanujam.services.redisfiller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Roma
 */
@SpringBootApplication
@EnableScheduling
public class RedisFillerMain {
    public static void main(String[] args) {
        SpringApplication.run(RedisFillerMain.class, args);
        RedisFiller.writeIsFinished( false );
    }
}
