package in.ramanujam.elasticsearch.filler;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@Configuration
@EnableAutoConfiguration
@ComponentScan({"in.ramanujam.elasticsearch.filler", "in.ramanujam.common.properties"})
public class ElasticSearchFillerStarter {
    private static ConfigurableApplicationContext context;

    @Autowired
    private void cleanIsFinishedKey(ElasticSearchFiller elasticFiller) throws JsonProcessingException {
        elasticFiller.writeIsFinished(false);
    }

    public static void main(String[] args) {
        context = SpringApplication.run(ElasticSearchFillerStarter.class, args);
    }

    public static void shutdown() {
        context.close();
    }
}