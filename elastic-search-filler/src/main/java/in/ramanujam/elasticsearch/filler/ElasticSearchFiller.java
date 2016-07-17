package in.ramanujam.elasticsearch.filler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.ramanujam.common.model.MinerRecord;
import in.ramanujam.common.processing.JsonStreamDataSupplier;
import in.ramanujam.common.properties.ElasticSearchProperties;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ElasticSearchFiller
{
  private static final Logger log = LoggerFactory.getLogger( ElasticSearchFiller.class );
  private static Client client = null;
  private static String index = null;
  private static String type = null;

  private static int count = 0;

  @Value("elastic-data.json")
  private Resource elasticSearchFile;

  private ObjectMapper mapper;

  public ElasticSearchFiller()
  {
    ElasticSearchProperties elasticSearchProperties = new ElasticSearchProperties();
    index = elasticSearchProperties.getElasticsearchIndexName();
    type = elasticSearchProperties.getElasticsearchTypeName();
    mapper = new ObjectMapper();
    try
    {
      client = TransportClient.builder().build()
          .addTransportAddress( new InetSocketTransportAddress(
              InetAddress.getByName( elasticSearchProperties.getElasticsearchContainerHost() ),
              elasticSearchProperties.getElasticsearchContainerExternalPort() ) );
    }
    catch ( UnknownHostException e )
    {
      throw new RuntimeException( e );
    }
  }

  public void addMinerRecord( MinerRecord minerRecord )
  {
    try
    {
      client.prepareIndex( index, type, minerRecord.getId().toString() )
              .setSource( mapper.writeValueAsBytes( minerRecord ) ).execute().actionGet();
      log.info( "ElasticSearchFiller :: Id = " + minerRecord.getId() + " count = " + ++count );
    }
    catch ( JsonProcessingException e )
    {
      throw new RuntimeException( e );
    }
  }

  public int fillItems( int offset, int limit )
  {
    try
    {
      InputStream file = elasticSearchFile.getInputStream();
      JsonStreamDataSupplier<MinerRecord> streamDataSupplier = JsonStreamDataSupplier.mapping( MinerRecord.class )
          .forStream( file )
          .build();

      List<MinerRecord> minerRecords = streamDataSupplier.stream()
          .skip( offset )
          .limit( limit )
          .collect( Collectors.toList() );

      for( MinerRecord record : minerRecords )
        addMinerRecord( record );

      log.info( "Offset: " + offset + ", limit: " + limit);
      return minerRecords.size();
    }
    catch ( IOException e )
    {
      throw new RuntimeException( e );
    }
  }

  public static void writeIsFinished( boolean isFinished ) throws JsonProcessingException
  {
    Map isFinishedMap = new HashMap<>();
    isFinishedMap.put( ElasticSearchProperties.getInstance().getElasticsearchIsFinishedKey(), isFinished );
    ObjectMapper mapper = new ObjectMapper();
    client.prepareIndex( index, ElasticSearchProperties.getInstance().getElasticsearchIsFinishedKey(), ElasticSearchProperties.getInstance().getElasticsearchIsFinishedKey() )
            .setSource( mapper.writeValueAsBytes( isFinishedMap ) ).get();
  }
}
