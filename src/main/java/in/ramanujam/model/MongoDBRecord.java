package in.ramanujam.model;

/**
 * Created with IntelliJ IDEA.
 * User: Denys Konakhevych
 * Date: 01.07.2016
 * Time: 18:28
 */
public class MongoDBRecord
{
  private RedisRecord redisRecord;
  private ElasticSearchRecord elasticSearchRecord;

  public MongoDBRecord()
  {
  }

  public MongoDBRecord( RedisRecord redisRecord, ElasticSearchRecord elasticSearchRecord )
  {
    this.redisRecord = redisRecord;
    this.elasticSearchRecord = elasticSearchRecord;
  }

  public RedisRecord getRedisRecord()
  {
    return redisRecord;
  }

  public void setRedisRecord( RedisRecord redisRecord )
  {
    this.redisRecord = redisRecord;
  }

  public ElasticSearchRecord getElasticSearchRecord()
  {
    return elasticSearchRecord;
  }

  public void setElasticSearchRecord( ElasticSearchRecord elasticSearchRecord )
  {
    this.elasticSearchRecord = elasticSearchRecord;
  }
}
