package in.ramanujam.services.elasticsearchfiller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.ramanujam.common.model.MinerRecord;
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

  public ElasticSearchFiller()
  {
    ElasticSearchProperties elasticSearchProperties = new ElasticSearchProperties();
    index = elasticSearchProperties.getElasticsearchIndexName();
    type = elasticSearchProperties.getElasticsearchTypeName();
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

  public void addMinerRecord( MinerRecord minerRecord ) throws JsonProcessingException
  {
    ObjectMapper mapper = new ObjectMapper();
    client.prepareIndex( index, type, minerRecord.getId().toString() )
        .setSource( mapper.writeValueAsBytes( minerRecord ) ).get();
    log.trace( "ElasticSearchFiller :: Id = " + minerRecord.getId() + " count = " + ++count );
  }

  public void fillItems( int offset, int limit )
  {
    //stream should be here
    try
    {
      InputStream file = elasticSearchFile.getInputStream();
      //stream should be here
      //...
      log.info( offset + " " + limit);
    }
    catch ( IOException e )
    {
      throw new RuntimeException( e );
    }
  }
}
