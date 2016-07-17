package in.ramanujam.common.model;

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
