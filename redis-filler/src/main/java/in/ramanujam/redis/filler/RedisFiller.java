package in.ramanujam.redis.filler;

import java.util.List;
import in.ramanujam.common.model.BitcoinRecord;
import in.ramanujam.common.properties.RedisProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

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
    System.out.println( "RedisFiller :: Id = " + bitcoin.getId() + " count = " + ++count  );
  }

  public static void addBitcoins(List<BitcoinRecord> bitcoins )
  {
    try ( Jedis jedis = new Jedis( RedisProperties.getInstance().getRedisContainerHost(),
        RedisProperties.getInstance().getRedisContainerExternalPort())) {
      Pipeline pipeline = jedis.pipelined();
      bitcoins.forEach(
          b -> {
            pipeline.hset(RedisProperties.getInstance().getRedisHashsetName(), b.getId().toString(), b.getKey() );
            LOGGER.info( "RedisFiller :: Id = " + b.getId() + " count = " + ++count   );
          }
      );
      pipeline.sync();
      LOGGER.info( "Bitcoin batch: " + bitcoins.size() );
    }

  }

  public static void writeIsFinished( boolean isFinished )
  {
    Jedis jedis = new Jedis( RedisProperties.getInstance().getRedisContainerHost(),
                             RedisProperties.getInstance().getRedisContainerExternalPort());
    jedis.set( RedisProperties.getInstance().getRedisIsFinishedKey(), String.valueOf( isFinished ) );
    jedis.close();
  }
}
