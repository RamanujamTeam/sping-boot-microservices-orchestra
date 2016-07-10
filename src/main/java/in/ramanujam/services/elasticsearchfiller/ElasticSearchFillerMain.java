package in.ramanujam.services.elasticsearchfiller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan
public class ElasticSearchFillerMain
{
    public static void main(String[] args) {
        SpringApplication.run( ElasticSearchFillerMain.class, args);
    }
}
