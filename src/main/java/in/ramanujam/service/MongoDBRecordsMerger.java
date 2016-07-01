package in.ramanujam.service;

import in.ramanujam.model.ElasticSearchRecord;
import in.ramanujam.model.MongoDBRecord;
import in.ramanujam.model.RedisRecord;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: Denys Konakhevych
 * Date: 01.07.2016
 * Time: 20:04
 */
public class MongoDBRecordsMerger
{
  public List<MongoDBRecord> merge( List<RedisRecord> redisRecords, List<ElasticSearchRecord> elasticSearchRecords )
  {
    Map<Integer, RedisRecord> idToRedisRecord = redisRecords.stream()
            .collect( Collectors.toMap( RedisRecord::getId, Function.identity() ) );
    Map<Integer, ElasticSearchRecord> idToElasticSearchRecord = elasticSearchRecords.stream()
            .collect( Collectors.toMap( ElasticSearchRecord::getId, Function.identity() ) );

//    elasticSearchRecords.stream().map( elasticSearchRecord -> elasticSearchRecord.getId() )
    return null;
  }

//  private static
}
