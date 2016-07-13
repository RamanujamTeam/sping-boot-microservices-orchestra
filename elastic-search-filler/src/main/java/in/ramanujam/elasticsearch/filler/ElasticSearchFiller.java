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
import java.util.stream.Stream;

@Service
public class ElasticSearchFiller
{
  private static final Logger log = LoggerFactory.getLogger( ElasticSearchFiller.class );
  private static Client client = null;
  private static String index = null;
  private static String type = null;

  //debug
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

  public void fillItems( int offset, int limit )
  {
    getStreamDataSupplier().stream()
          .skip(offset)
          .limit( limit )
          .forEach( this::addMinerRecord );
  }

  public static void writeIsFinished( boolean isFinished ) throws JsonProcessingException
  {
    Map isFinishedMap = new HashMap<>();
    isFinishedMap.put( ElasticSearchProperties.getInstance().getElasticsearchIsFinishedKey(), isFinished );
    ObjectMapper mapper = new ObjectMapper();
    client.prepareIndex( index, ElasticSearchProperties.getInstance().getElasticsearchIsFinishedKey(), ElasticSearchProperties.getInstance().getElasticsearchIsFinishedKey() )
            .setSource( mapper.writeValueAsBytes( isFinishedMap ) ).get();
  }

  private JsonStreamDataSupplier<MinerRecord> getStreamDataSupplier()
  {
    InputStream inputStream = null;
    try
    {
      inputStream = elasticSearchFile.getInputStream();
    }
    catch( IOException e )
    {
      e.printStackTrace();
    }

    return JsonStreamDataSupplier.mapping( MinerRecord.class )
              .forStream( inputStream )
              .build();
  }

  public long elementsLeft( int fromNumber )
  {
    return getStreamDataSupplier().stream().count() - fromNumber;
  }
}
