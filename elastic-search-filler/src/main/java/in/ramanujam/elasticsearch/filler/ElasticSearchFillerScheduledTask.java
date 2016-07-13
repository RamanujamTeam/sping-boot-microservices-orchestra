package in.ramanujam.elasticsearch.filler;

import in.ramanujam.common.model.MinerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.stream.Stream;

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
        filler.fillItems( curPos, PAGE_SIZE );
        curPos += PAGE_SIZE;

        if( filler.elementsLeft( curPos ) <= 0 )
        {
            ElasticSearchFiller.writeIsFinished( true );
            System.out.println( "ElasticSearchFiller :: Successfully finished!");
            ElasticSearchFillerStarter.shutdown();
        }
    }
}
