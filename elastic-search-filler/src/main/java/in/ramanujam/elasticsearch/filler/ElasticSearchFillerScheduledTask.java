package in.ramanujam.elasticsearch.filler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ElasticSearchFillerScheduledTask
{

    @Autowired
    ElasticSearchFiller filler;

    public static final int PAGE_SIZE = 100;
    private int curPos = 0;

    // TODO: how can we stop it from running after all records are persisted?
    @Scheduled(fixedDelay = 1000) // TODO: 30 secs
    public void runWithDelay() throws IOException
    {
        if( filler.fillItems( curPos, PAGE_SIZE ) == 0 )
        {
            ElasticSearchFiller.writeIsFinished( true );
            System.out.println( "ElasticSearchFiller :: Successfully finished!");
            ElasticSearchFillerStarter.shutdown();
        }
        curPos += PAGE_SIZE;
    }
}
