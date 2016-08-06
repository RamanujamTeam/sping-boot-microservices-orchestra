package in.ramanujam.elasticsearch.aggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableAutoConfiguration
@ComponentScan({"in.ramanujam.elasticsearch.aggregator", "in.ramanujam.common.properties"})
@EnableScheduling
public class ElasticSearchToMongoStarter {
    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        context = SpringApplication.run(ElasticSearchToMongoStarter.class, args);
    }

    public static void shutdown() {
        context.close();
    }
}
