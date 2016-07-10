package in.ramanujam.services.redisfiller;

import in.ramanujam.common.model.BitcoinRecord;
import in.ramanujam.common.properties.RedisProperties;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Denys Konakhevych
 * Date: 08.07.2016
 * Time: 18:08
 */
public class RedisFiller
{
  private static int count = 0;
  public static void addBitcoin( BitcoinRecord bitcoin )
  {
    Jedis jedis = new Jedis( RedisProperties.getInstance().getRedisContainerHost(),
                             RedisProperties.getInstance().getRedisContainerExternalPort());
    jedis.hset( RedisProperties.getInstance().getRedisHashsetName(), bitcoin.getId().toString(), bitcoin.getKey() );
    jedis.close();
    System.out.println( "RedisFiller :: Id = " + bitcoin.getId() + " count = " + ++count  );
  }
}
