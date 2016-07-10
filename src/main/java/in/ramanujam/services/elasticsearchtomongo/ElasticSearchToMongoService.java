package in.ramanujam.services.elasticsearchtomongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;
import in.ramanujam.common.model.MinerRecord;
import in.ramanujam.common.properties.ElasticSearchProperties;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.IndexNotFoundException;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Denys Konakhevych
 * Date: 01.07.2016
 * Time: 20:02
 */
public class ElasticSearchToMongoService
{
  private static ElasticSearchToMongoService instance = new ElasticSearchToMongoService();
  private Client client;
  private String index;
  private String type;
  private static int writeCount = 0;
  private static int removeCount = 0;


  private ElasticSearchToMongoService()
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

  public static ElasticSearchToMongoService getInstance()
  {
    return instance;
  }

  public List<MinerRecord> retrieveAllRecords() // TODO: rename method
  {
    QueryBuilder qb = QueryBuilders.matchAllQuery();
    SearchResponse response;
    List<MinerRecord> minerRecords = new ArrayList<>();
    try
    {
      response = client.prepareSearch( index )
                .setQuery(qb)
                .setSize( 100 )
                .execute().actionGet();

    }
    catch( IndexNotFoundException e ){ return new ArrayList<>(); }
    for( SearchHit hit : response.getHits().hits() )
    {
      minerRecords.add( new MinerRecord( hit.getSource() ) );
    }
    return minerRecords;
  }

  public void removeRecord( MinerRecord record )
  {
    try
    {
      client.prepareDelete( index, type, String.valueOf( record.getId() ) )
              .execute()
              .actionGet();
    }
    catch( IndexNotFoundException e ){return;}
    System.out.println( "ElasticSearchToMongo :: Removed from Redis :: Id = " + record.getId() + " count = " + ++removeCount );
  }

  public void moveRecordFromElasticSearchToMongo( MinerRecord minerRecord, DBCollection collection )
  {
    try
    {
      writeESRecordToMongo( minerRecord, collection );
      removeRecord( minerRecord );
    }
    catch( Exception e ){}
  }

  private static WriteResult writeESRecordToMongo( MinerRecord esRecord, DBCollection collection  )
  {
    BasicDBObject document = new BasicDBObject();

    BasicDBObject values = new BasicDBObject()
            .append( "gender", esRecord.getGender() )
            .append( "first_name", esRecord.getFirstName() )
            .append( "last_name", esRecord.getLastName() )
            .append( "email", esRecord.getEmail() )
            .append( "ip_address", esRecord.getIpAddress() );

    document.append( "$set", values );

    BasicDBObject searchQuery = new BasicDBObject().append( "_id", esRecord.getId() );
    WriteResult result = collection.update( searchQuery, document, true, false );
    System.out.println( "ElasticSearchToMongo :: Wrote to Mongo :: Id = " + esRecord.getId() + " count = " + ++writeCount ); // TODO: can we replace it with Spring AOP?
    return result;
  }
}
