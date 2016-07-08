package in.ramanujam;

import com.mongodb.*;
import in.ramanujam.model.MinerRecord;
import in.ramanujam.model.BitcoinRecord;
import in.ramanujam.properties.MongoDBProperties;
import in.ramanujam.properties.RedisProperties;
import in.ramanujam.service.TestUtil;
import redis.clients.jedis.Jedis;

import java.net.UnknownHostException;

/**
 * Created by anatolii on 7/2/16.
 */
public class MongoAggregationTest
{

  public static void main( String[] args ) throws UnknownHostException
  {
    DBCollection collection = setupMongoCollection();

    BitcoinRecord redisRecord = TestUtil.generateRedisRecord( 1002 );
    MinerRecord esRecord = TestUtil.generateElasticSearchRecord( 1002 );

    try
    {
      writeRedisRecordToMongo( redisRecord, collection );
      removeRecordFromRedis( redisRecord );
    }
    catch( Exception e ){}

    try
    {
      writeESRecordToMongo( esRecord, collection );
      removeRecordFromES( esRecord );
    }
    catch( Exception e ){}
  }

  private static DBCollection setupMongoCollection() throws UnknownHostException
  {
    MongoDBProperties properties = new MongoDBProperties();
    MongoClient mongo = new MongoClient( properties.getMongoHost(), properties.getMongoPort() );
    mongo.setWriteConcern( WriteConcern.SAFE );
    DB db = mongo.getDB( properties.getMongoDb() );

    return db.getCollection( properties.getMongoCollection() );
  }

  private static WriteResult writeRedisRecordToMongo( BitcoinRecord redisRecord, DBCollection collection )
  {
    BasicDBObject document = new BasicDBObject();

    BasicDBObject values = new BasicDBObject()
            .append( "bitcoin", redisRecord.getKey() );

    document.append( "$set", values );

    BasicDBObject searchQuery = new BasicDBObject().append( "_id", redisRecord.getId() );

    return collection.update( searchQuery, document, true, false );
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

    return collection.update( searchQuery, document, true, false );
  }

  private static Long removeRecordFromRedis( BitcoinRecord redisRecord )
  {
    Jedis jedis = new Jedis( new RedisProperties().getRedisContainerHost(),
                             new RedisProperties().getRedisContainerPort());
    return jedis.hdel( "bitcoins", String.valueOf( redisRecord.getId() ) );
  }

  private static Long removeRecordFromES( MinerRecord esRecord )
  {
    return 42L; // TODO: replace stub
  }
}
