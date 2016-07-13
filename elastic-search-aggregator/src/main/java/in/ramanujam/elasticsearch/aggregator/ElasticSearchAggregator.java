package in.ramanujam.elasticsearch.aggregator;

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

public class ElasticSearchAggregator
{
  private static ElasticSearchAggregator instance = new ElasticSearchAggregator();
  private Client client;
  private String index;
  private String type;
  private static int writeCount = 0;
  private static int removeCount = 0;


  private ElasticSearchAggregator()
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

  public static ElasticSearchAggregator getInstance()
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
    System.out.println( "ElasticSearchToMongo :: Removed from ElasticSearch :: Id = " + record.getId() + " count = " + ++removeCount );
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

  public Boolean isElasticSearchFillerFinished()
  {
    try
    {
      SearchResponse response = client.prepareSearch( index )
              .setTypes( ElasticSearchProperties.getInstance().getElasticsearchIsFinishedKey() ).setFrom(0).setSize(1)
              .execute().actionGet();
      return (Boolean) response.getHits().getAt( 0 ).getSource().get( ElasticSearchProperties.getInstance().getElasticsearchIsFinishedKey() ); //TODO: there is an ArrayIndexOutOfBoundsException in the beginning. Stacktrace bellow
    }
    catch( IndexNotFoundException e )
    {
      return false;
    }
  }
}
/*
TODO: stacktrace:
java.lang.ArrayIndexOutOfBoundsException: 0
        at org.elasticsearch.search.internal.InternalSearchHits.getAt(InternalSearchHits.java:149) ~[elasticsearch-2.3.3.jar:2.3.3]
        at in.ramanujam.services.elasticsearchtomongo.ElasticSearchToMongoService.isElasticSearchFillerFinished(ElasticSearchToMongoService.java:130) ~[main/:na]
        at in.ramanujam.services.elasticsearchtomongo.ElasticSearchToMongoScheduledTask.runWithDelay(ElasticSearchToMongoScheduledTask.java:27) ~[main/:na]
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:1.8.0_77]
        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:1.8.0_77]
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:1.8.0_77]
        at java.lang.reflect.Method.invoke(Method.java:498) ~[na:1.8.0_77]
        at org.springframework.scheduling.support.ScheduledMethodRunnable.run(ScheduledMethodRunnable.java:65) ~[spring-context-4.2.6.RELEASE.jar:4.2.6.RELEASE]
        at org.springframework.scheduling.support.DelegatingErrorHandlingRunnable.run(DelegatingErrorHandlingRunnable.java:54) ~[spring-context-4.2.6.RELEASE.jar:4.2.6.RELEASE]
        at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:511) [na:1.8.0_77]
        at java.util.concurrent.FutureTask.runAndReset(FutureTask.java:308) [na:1.8.0_77]
        at java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.access$301(ScheduledThreadPoolExecutor.java:180) [na:1.8.0_77]
        at java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:294) [na:1.8.0_77]
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142) [na:1.8.0_77]
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617) [na:1.8.0_77]
        at java.lang.Thread.run(Thread.java:745) [na:1.8.0_77]*/
