package in.ramanujam;

import in.ramanujam.common.model.MinerRecord;
import in.ramanujam.common.processing.JsonStreamDataSupplier;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

public class ElasticSearchFiller
{
  private void parseFile() throws IOException
  {
    InputStream is = new BufferedInputStream( getClass().getClassLoader().getResourceAsStream( "elastic-data.json" ) );

    JsonStreamDataSupplier<MinerRecord> it = JsonStreamDataSupplier.mapping( MinerRecord.class ).forStream( is ).build();
    it.stream()
            .skip( 80 )
            .limit( 10 )
            .forEach( System.out::println );

  }

  public static void main( String[] args ) throws IOException, ExecutionException, InterruptedException
  {
    InputStream is = new BufferedInputStream( ElasticSearchFiller.class.getClassLoader().getResourceAsStream( "elastic-data.json" ) );
    new ElasticSearchFiller().parseFile();


//
//
//    Client client = TransportClient.builder().build()
//        .addTransportAddress( new InetSocketTransportAddress( InetAddress.getByName( "localhost" ), 9300 ) );
////        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
//
//
//    ElasticSearchRecord elasticData1 = new ElasticSearchRecord()
//        .setId( 1 )
//        .setEmail( "com@com.com" )
//        .setFirstName( "Jack" )
//        .setLastName( "Monik" )
//        .setIpAddress( "127.0.0.1" )
//        .setGender( "Male" );
//
//    ElasticData elasticData2 = new ElasticData()
//        .setId( 2 )
//        .setEmail( "com2@com.com" )
//        .setFirstName( "Jack2" )
//        .setLastName( "Monik2" )
//        .setIpAddress( "127.0.0.2" )
//        .setGender( "Female" );
//
//    ObjectMapper mapper = new ObjectMapper(); // create once, reuse
//
//// generate json
//    byte[] json1 = mapper.writeValueAsBytes(elasticData1);
//    byte[] json2 = mapper.writeValueAsBytes(elasticData2);
//
//    IndexResponse responseIndex = client.prepareIndex( "javapro", "people", "1" )
//        .setSource( json1 )
//        .get();
//
//    System.out.println( responseIndex.toString() );
//
//    UpdateRequest updateRequest = new UpdateRequest();
//    updateRequest.index( "javapro" );
//    updateRequest.type( "people" );
//    updateRequest.id( "1" );
//    updateRequest.doc( json2 );
//
//    client.update( updateRequest ).get();
//
//    GetResponse responseUpdate = client.prepareGet( "javapro", "people", "1" )
//        .setOperationThreaded( false )
//        .get();
//    Map<String, Object> sourceAsMap = responseUpdate.getSourceAsMap();
//    System.out.println( sourceAsMap );
//
//
//    client.close();
  }
}