package in.ramanujam.services.elasticsearchfiller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.ramanujam.common.model.MinerRecord;
import in.ramanujam.common.properties.ElasticSearchProperties;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Denys Konakhevych
 * Date: 08.07.2016
 * Time: 18:26
 */
public class ElasticSearchFiller
{
  private static ElasticSearchFiller instance = new ElasticSearchFiller();
  private Client client;
  private final String index;
  private final String type;
  private static int count = 0;

  private ElasticSearchFiller()
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
    catch( UnknownHostException e )
    {
      throw new RuntimeException( e );
    }
  }

  public static ElasticSearchFiller getInstance()
  {
    return instance;
  }

  public void addMinerRecord( MinerRecord minerRecord ) throws JsonProcessingException
  {
    ObjectMapper mapper = new ObjectMapper();
    client.prepareIndex( index, type, minerRecord.getId().toString() )
            .setSource( mapper.writeValueAsBytes( minerRecord ) ).execute().actionGet();
    System.out.println( "ElasticSearchFiller :: Id = " + minerRecord.getId() + " count = " + ++count );
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
