package in.ramanujam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Roma
 */
@SpringBootApplication
@EnableScheduling
public class ScheduledMain {
    public static void main(String[] args) {
        SpringApplication.run(ScheduledMain.class, args);
    }
}
