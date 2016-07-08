package in.ramanujam.service.retriever;

import in.ramanujam.model.BitcoinRecord;
import in.ramanujam.properties.RedisProperties;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: Denys Konakhevych
 * Date: 01.07.2016
 * Time: 19:56
 */
public class RedisRetriever
{
  public static List<BitcoinRecord> retrieveAllRecords()
  {
    RedisProperties redisProperties = new RedisProperties();
    Jedis jedis = new Jedis( redisProperties.getRedisContainerHost(), redisProperties.getRedisContainerExternalPort());

    Map<String, String> redisRecordMap = jedis.hgetAll( redisProperties.getRedisHashsetName() );
    return redisRecordMap.entrySet().stream()
            .map( entry -> new BitcoinRecord( Integer.parseInt( entry.getKey() ), entry.getValue() ) )
            .collect( Collectors.toList() );
  }
}
