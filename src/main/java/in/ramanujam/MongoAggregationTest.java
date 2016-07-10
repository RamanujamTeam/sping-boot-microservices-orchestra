//package in.ramanujam;
//
//import com.mongodb.*;
//import in.ramanujam.common.MongoUtils;
//import in.ramanujam.common.model.MinerRecord;
//import in.ramanujam.common.model.BitcoinRecord;
//import in.ramanujam.services.elasticsearchtomongo.ElasticSearchToMongoService;
//import in.ramanujam.services.redistomongo.RedisToMongoService;
//
//import java.net.UnknownHostException;
//
///**
// * Created by anatolii on 7/2/16.
// */
//public class MongoAggregationTest // TODO: create 2 separate services from this task
//{
//
//  public static void main( String[] args ) throws UnknownHostException
//  {
//    for( BitcoinRecord bitcoinRecord : RedisToMongoService.retrieveAllRecords() )
//    {
//      RedisToMongoService.moveRecordFromRedisToMongo( bitcoinRecord, MongoUtils.getCollection() );
//    }
//
//    for( MinerRecord esRecord : ElasticSearchToMongoService.getInstance().retrieveAllRecords() )
//    {
//      moveRecordFromElasticSearchToMongo( esRecord, MongoUtils.getCollection() );
//    }
//  }
//
//  private static WriteResult writeESRecordToMongo( MinerRecord esRecord, DBCollection collection  )
//  {
//    BasicDBObject document = new BasicDBObject();
//
//    BasicDBObject values = new BasicDBObject()
//            .append( "gender", esRecord.getGender() )
//            .append( "first_name", esRecord.getFirstName() )
//            .append( "last_name", esRecord.getLastName() )
//            .append( "email", esRecord.getEmail() )
//            .append( "ip_address", esRecord.getIpAddress() );
//
//    document.append( "$set", values );
//
//    BasicDBObject searchQuery = new BasicDBObject().append( "_id", esRecord.getId() );
//
//    return collection.update( searchQuery, document, true, false );
//  }
//
//  private static void moveRecordFromElasticSearchToMongo( MinerRecord minerRecord, DBCollection collection )
//  {
//    try
//    {
//      writeESRecordToMongo( minerRecord, collection );
//      ElasticSearchToMongoService.getInstance().removeRecord( minerRecord );
//    }
//    catch( Exception e ){}
//  }
//
//}
