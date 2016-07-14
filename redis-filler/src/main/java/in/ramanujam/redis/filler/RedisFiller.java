package in.ramanujam.redis.filler;

import in.ramanujam.common.model.BitcoinRecord;
import in.ramanujam.common.properties.RedisProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

public class RedisFiller
{
  private static final Logger log = LoggerFactory.getLogger( RedisFiller.class );
  private static int count = 0;
  public static void addBitcoin( BitcoinRecord bitcoin )
  {
    Jedis jedis = new Jedis( RedisProperties.getInstance().getRedisContainerHost(),
                             RedisProperties.getInstance().getRedisContainerExternalPort());
    jedis.hset( RedisProperties.getInstance().getRedisHashsetName(), bitcoin.getId().toString(), bitcoin.getKey() );
    jedis.close();
    log.info( "RedisFiller :: Id = " + bitcoin.getId() + " count = " + ++count  );
  }

  public static void writeIsFinished( boolean isFinished )
  {
    Jedis jedis = new Jedis( RedisProperties.getInstance().getRedisContainerHost(),
                             RedisProperties.getInstance().getRedisContainerExternalPort());
    jedis.set( RedisProperties.getInstance().getRedisIsFinishedKey(), String.valueOf( isFinished ) );
    jedis.close();
  }
}
