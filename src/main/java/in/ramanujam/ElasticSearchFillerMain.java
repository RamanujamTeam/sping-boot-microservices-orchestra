package in.ramanujam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ElasticSearchFillerMain
{
    public static void main(String[] args) {
        SpringApplication.run( ElasticSearchFillerMain.class, args);
    }
}
