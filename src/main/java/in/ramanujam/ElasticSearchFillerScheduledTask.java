package in.ramanujam;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.ramanujam.model.MinerRecord;
import in.ramanujam.properties.ElasticSearchProperties;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ElasticSearchFillerScheduledTask
{
    private static Client client;
    static
    {
        try {
            client = TransportClient.builder().build()
                    .addTransportAddress(new InetSocketTransportAddress( InetAddress.getByName(
                            new ElasticSearchProperties().getElasticsearchContainerHost() ),
                            new ElasticSearchProperties().getElasticsearchContainerExternalPort()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

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
            curPos++;
            client.prepareIndex( new ElasticSearchProperties().getElasticsearchIndexName(),
                                 new ElasticSearchProperties().getElasticsearchTypeName(), String.valueOf( minerRecords.get( curPos-1 ).getId() ))
                    .setSource( mapper.writeValueAsBytes( minerRecords.get( curPos - 1 ) ) ).get();

        }
    }
}
