package in.ramanujam.service.retriever;

import in.ramanujam.model.MinerRecord;
import in.ramanujam.service.TestUtil;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Denys Konakhevych
 * Date: 01.07.2016
 * Time: 20:02
 */
public class ElasticSearchRetriever
{
  public static List<MinerRecord> retrieveAllRecords()
  {
    return TestUtil.generateElasticSearchRecords( 1, 1000 ); // TODO: replace stub
  }
}
