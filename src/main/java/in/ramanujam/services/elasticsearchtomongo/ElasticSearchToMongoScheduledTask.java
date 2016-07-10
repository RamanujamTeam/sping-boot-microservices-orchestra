package in.ramanujam.services.elasticsearchtomongo;

import in.ramanujam.common.MongoUtils;
import in.ramanujam.common.model.MinerRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ElasticSearchToMongoScheduledTask
{
    // TODO: how can we stop it from running after all records are persisted?
    @Scheduled(fixedDelay = 1000)
    public void runWithDelay() throws IOException
    {
        for( MinerRecord esRecord : ElasticSearchToMongoService.getInstance().retrieveAllRecords() )
        {
            ElasticSearchToMongoService.getInstance().moveRecordFromElasticSearchToMongo( esRecord, MongoUtils.getCollection() );
        }
    }
}
