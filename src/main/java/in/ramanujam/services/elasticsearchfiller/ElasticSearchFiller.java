package in.ramanujam.services.elasticsearchfiller;

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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ElasticSearchFiller
{
  private static final Logger log = LoggerFactory.getLogger( ElasticSearchFiller.class );
  private final Client client;
  private final String index;
  private final String type;

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
      log.trace( "ElasticSearchFiller :: Id = " + minerRecord.getId() + " count = " + ++count );
    }
    catch ( JsonProcessingException e )
    {
      throw new RuntimeException( e );
    }
  }

  public void fillItems( int offset, int limit )
  {
    try
    {
      InputStream file = elasticSearchFile.getInputStream();
      JsonStreamDataSupplier<MinerRecord> streamDataSupplier = JsonStreamDataSupplier.mapping( MinerRecord.class )
          .forStream( file )
          .build();

      streamDataSupplier.stream()
          .skip( offset )
          .limit( limit )
          .forEach( this::addMinerRecord );
      log.info( offset + " " + limit);
    }
    catch ( IOException e )
    {
      throw new RuntimeException( e );
    }
  }

  public void writeIsFinished( boolean isFinished ) throws JsonProcessingException
  {
    Map isFinishedMap = new HashMap<>();
    isFinishedMap.put( ElasticSearchProperties.getInstance().getElasticsearchIsFinishedKey(), isFinished );
    ObjectMapper mapper = new ObjectMapper();
    client.prepareIndex( index, ElasticSearchProperties.getInstance().getElasticsearchIsFinishedKey(), ElasticSearchProperties.getInstance().getElasticsearchIsFinishedKey() )
            .setSource( mapper.writeValueAsBytes( isFinishedMap ) ).get();
  }
}
