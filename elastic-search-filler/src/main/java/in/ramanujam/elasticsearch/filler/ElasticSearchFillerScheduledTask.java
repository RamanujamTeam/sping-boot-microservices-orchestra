package in.ramanujam.elasticsearch.filler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ElasticSearchFillerScheduledTask
{
    private static final Logger log = LoggerFactory.getLogger(ElasticSearchFillerScheduledTask.class);
    @Autowired
    ElasticSearchFiller filler;

    public static final int PAGE_SIZE = 100;
    private int curPos = 0;

    @Scheduled(fixedDelay = 1000) // TODO: 30 secs
    public void runWithDelay() throws IOException
    {
        filler.fillItems( curPos, PAGE_SIZE );
        curPos += PAGE_SIZE;

        if( curPos >= 1000 )
        {
            ElasticSearchFiller.writeIsFinished( true );
            log.info( "ElasticSearchFiller :: Successfully finished!");
            ElasticSearchFillerStarter.shutdown();
        }
    }
}
