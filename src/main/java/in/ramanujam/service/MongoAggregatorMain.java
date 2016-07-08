package in.ramanujam.service;

import in.ramanujam.model.MongoDBRecord;
import in.ramanujam.service.aggregator.MongoAggregator;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Denys Konakhevych
 * Date: 01.07.2016
 * Time: 18:46
 */
public class MongoAggregatorMain
{
  public static void main( String[] args )
  {
    List<MongoDBRecord> records = TestUtil.generateMongoRecords();
    MongoAggregator.getInstance().aggregate( records );
    MongoAggregator.getInstance().close();
  }


}
