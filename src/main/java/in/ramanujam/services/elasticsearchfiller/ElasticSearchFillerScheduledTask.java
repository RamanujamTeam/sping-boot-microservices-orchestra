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

    @Scheduled(fixedDelay = 1000)
    public void runWithDelay() throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        MinerRecord[] esRecords = mapper.readValue( elasticSearchFile.getFile(), MinerRecord[].class );
        List<MinerRecord> minerRecords = new ArrayList<>( Arrays.asList( esRecords ) );
        int lastIndex = Math.min( curPos + 100, 1000 );
        while( curPos < lastIndex )
        {
            ElasticSearchFiller.getInstance().addMinerRecord( minerRecords.get( curPos++ ) );
        }
    }
}
