package in.ramanujam.services.elasticsearchfiller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ElasticSearchFillerMain
{
    public static void main(String[] args) throws JsonProcessingException
    {
        SpringApplication.run( ElasticSearchFillerMain.class, args);
        ElasticSearchFiller.getInstance().writeIsFinished( false );
    }
}
