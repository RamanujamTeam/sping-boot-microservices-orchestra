package in.ramanujam.service.retriever;

import in.ramanujam.model.ElasticSearchRecord;
import in.ramanujam.model.RedisRecord;
import in.ramanujam.service.TestUtil;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Denys Konakhevych
 * Date: 01.07.2016
 * Time: 19:56
 */
public class RedisRetriever
{
  public static List<RedisRecord> retrieveAllRecords()
  {
    return TestUtil.generateRedisRecords();
  }
}
