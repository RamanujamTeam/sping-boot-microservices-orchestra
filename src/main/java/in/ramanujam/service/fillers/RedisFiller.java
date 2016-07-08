package in.ramanujam.service.fillers;

import in.ramanujam.model.BitcoinRecord;
import in.ramanujam.properties.RedisProperties;
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
  private static RedisFiller instance = new RedisFiller();
  private Jedis jedis;
  private String bitcoinCollectionName;

  public static RedisFiller getInstance()
  {
    return instance;
  }

  private RedisFiller()
  {
    RedisProperties redisProperties = new RedisProperties();
    bitcoinCollectionName = redisProperties.getRedisHashsetName();
    jedis = new Jedis( redisProperties.getRedisContainerHost(), redisProperties.getRedisContainerExternalPort());
  }

  public void addBatch( List<BitcoinRecord> bitcoins )
  {
    bitcoins.stream().forEach( this::addBitcoin );
  }

  public void addBitcoin( BitcoinRecord bitcoin )
  {
    jedis.hset( bitcoinCollectionName, bitcoin.getId().toString(), bitcoin.getKey() );
  }
}
