package in.ramanujam.redis.aggregator;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;
import in.ramanujam.common.model.BitcoinRecord;
import in.ramanujam.common.properties.RedisProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RedisAggregator
{
  private static int writeCount = 0;
  private static int removeCount = 0;
  private static final Logger log = LoggerFactory.getLogger( RedisAggregator.class );
  public static List<BitcoinRecord> retrieveAllRecords() // TODO: rename method
  {
    List<BitcoinRecord> bitcoins = new ArrayList<>();
    try( Jedis jedis = new Jedis( RedisProperties.getInstance().getRedisContainerHost(),
                                  RedisProperties.getInstance().getRedisContainerExternalPort()) )
    {
      Map<String, String> redisRecordMap = jedis.hgetAll( RedisProperties.getInstance().getRedisHashsetName() );//TODO: retrieve only 100 records, not all of them
      bitcoins = redisRecordMap.entrySet().stream()
              .map( entry -> new BitcoinRecord( Integer.parseInt( entry.getKey() ), entry.getValue() ) )
              .collect( Collectors.toList() );
    }
    catch( RuntimeException e ){ }
    return bitcoins;
  }

  public static void removeRecordFromRedis( BitcoinRecord redisRecord )
  {
    Jedis jedis = new Jedis( RedisProperties.getInstance().getRedisContainerHost(),
                             RedisProperties.getInstance().getRedisContainerExternalPort());
    jedis.hdel( "bitcoins", String.valueOf( redisRecord.getId() ) ); // TODO: extract to properties
    jedis.close();
    log.info( "RedisToMongo :: Removed from Redis :: Id = " + redisRecord.getId() + " count = " + ++removeCount );
  }

  public static void moveRecordFromRedisToMongo( BitcoinRecord redisRecord, DBCollection collection )
  {
    try
    {
      writeRedisRecordToMongo( redisRecord, collection );
      removeRecordFromRedis( redisRecord );
    }
    catch( Exception e ){}
  }

  private static WriteResult writeRedisRecordToMongo( BitcoinRecord redisRecord, DBCollection collection )
  {
    BasicDBObject document = new BasicDBObject();

    BasicDBObject values = new BasicDBObject()
            .append( "bitcoin", redisRecord.getKey() ); // TODO: extract to properties

    document.append( "$set", values );

    BasicDBObject searchQuery = new BasicDBObject().append( "_id", redisRecord.getId() );
    WriteResult result = collection.update( searchQuery, document, true, false );
    log.info( "RedisToMongo :: Wrote to Mongo :: Id = " + redisRecord.getId() + " count = " + ++writeCount );
    return result;
  }

  public static Boolean isRedisFillerFinished()
  {
    Jedis jedis = new Jedis( RedisProperties.getInstance().getRedisContainerHost(),
                             RedisProperties.getInstance().getRedisContainerExternalPort() );
    String isRedisFillerFinished = jedis.get( RedisProperties.getInstance().getRedisIsFinishedKey() );
    jedis.close();
    return Boolean.valueOf( isRedisFillerFinished );
  }
}
