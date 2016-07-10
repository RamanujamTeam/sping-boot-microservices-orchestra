package in.ramanujam.services.elasticsearchfiller;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.ramanujam.common.model.MinerRecord;
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
    @Value("elastic-data.json")
    private Resource elasticSearchFile;

    private int curPos = 1;
    // TODO: how can we stop it from running after all records are persisted?
    @Scheduled(fixedDelay = 1000) // TODO: 30 secs
    public void runWithDelay() throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        MinerRecord[] esRecords = mapper.readValue( elasticSearchFile.getFile(), MinerRecord[].class ); // TODO: can we improve this logic?
        List<MinerRecord> minerRecords = new ArrayList<>( Arrays.asList( esRecords ) );
        int lastIndex = Math.min( curPos + 100, minerRecords.size() + 1 );
        while( curPos < lastIndex )
        {
            ElasticSearchFiller.getInstance().addMinerRecord( minerRecords.get( curPos++ - 1 ) );
        }
    }
}
