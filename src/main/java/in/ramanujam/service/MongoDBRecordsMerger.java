package in.ramanujam.service;

import in.ramanujam.model.MinerRecord;
import in.ramanujam.model.MongoDBRecord;
import in.ramanujam.model.BitcoinRecord;

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
  public static List<MongoDBRecord> merge( List<BitcoinRecord> redisRecords, List<MinerRecord> elasticSearchRecords )
  {
    Map<Integer, MinerRecord> idToElasticSearchRecord = elasticSearchRecords.stream()
            .collect( Collectors.toMap( MinerRecord::getId, Function.identity() ) );

    return redisRecords.stream()
            .map( redisRecord -> merge( redisRecord, idToElasticSearchRecord.get( redisRecord.getId() ) ) )
            .collect( Collectors.toList() );
  }

  private static MongoDBRecord merge( BitcoinRecord redisRecord, MinerRecord elasticSearchRecord )
  {
    return new MongoDBRecord( redisRecord, elasticSearchRecord );
  }
}
