package in.ramanujam.model;

/**
 * Created with IntelliJ IDEA.
 * User: Denys Konakhevych
 * Date: 01.07.2016
 * Time: 18:28
 */
public class MongoDBRecord
{
  private BitcoinRecord redisRecord;
  private MinerRecord elasticSearchRecord;

  public MongoDBRecord()
  {
  }

  public MongoDBRecord( BitcoinRecord redisRecord, MinerRecord elasticSearchRecord )
  {
    this.redisRecord = redisRecord;
    this.elasticSearchRecord = elasticSearchRecord;
  }

  public BitcoinRecord getRedisRecord()
  {
    return redisRecord;
  }

  public void setRedisRecord( BitcoinRecord redisRecord )
  {
    this.redisRecord = redisRecord;
  }

  public MinerRecord getElasticSearchRecord()
  {
    return elasticSearchRecord;
  }

  public void setElasticSearchRecord( MinerRecord elasticSearchRecord )
  {
    this.elasticSearchRecord = elasticSearchRecord;
  }
}
