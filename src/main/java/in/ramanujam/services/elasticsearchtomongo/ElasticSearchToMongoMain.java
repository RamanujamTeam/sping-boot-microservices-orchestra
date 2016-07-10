package in.ramanujam.services.elasticsearchtomongo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ElasticSearchToMongoMain
{
    public static void main(String[] args) {
        SpringApplication.run( ElasticSearchToMongoMain.class, args);
    }
}
