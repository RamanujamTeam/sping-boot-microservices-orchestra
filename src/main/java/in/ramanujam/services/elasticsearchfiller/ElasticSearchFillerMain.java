package in.ramanujam.services.elasticsearchfiller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ElasticSearchFillerMain //TODO: set spring boot to search only inside this package
{
    public static void main(String[] args) {
        SpringApplication.run( ElasticSearchFillerMain.class, args);
    }
}
