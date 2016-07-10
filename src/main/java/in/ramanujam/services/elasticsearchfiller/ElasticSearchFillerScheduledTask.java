package in.ramanujam.services.elasticsearchfiller;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.ramanujam.common.model.MinerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ElasticSearchFillerScheduledTask
{

    @Autowired
    ElasticSearchFiller filler;

    public static final int PAGE_SIZE = 10;
    private int curPos = 0;

    // TODO: how can we stop it from running after all records are persisted?
    @Scheduled(fixedDelay = 1000) // TODO: 30 secs
    public void runWithDelay() throws IOException
    {
        filler.fillItems( curPos, PAGE_SIZE );
        curPos += PAGE_SIZE;
    }
}
