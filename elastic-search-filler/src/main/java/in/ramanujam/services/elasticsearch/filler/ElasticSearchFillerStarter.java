package in.ramanujam.services.elasticsearch.filler;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan
public class ElasticSearchFillerStarter
{
    private static ConfigurableApplicationContext context;

    public static void main(String[] args) throws JsonProcessingException
    {
        context = SpringApplication.run(ElasticSearchFillerStarter.class, args);
//        ElasticSearchFiller.getInstance().writeIsFinished( false );
    }

    public static void shutdown()
    {
        context.close();
    }
}
