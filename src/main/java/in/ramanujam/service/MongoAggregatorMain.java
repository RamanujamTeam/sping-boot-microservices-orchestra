package in.ramanujam.service;

import com.mongodb.*;
import in.ramanujam.model.MongoDBRecord;
import in.ramanujam.properties.MongoDBProperties;

import java.net.UnknownHostException;
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
    MongoClient mongo = null;
    try
    {
      MongoDBProperties properties = new MongoDBProperties();
      mongo = new MongoClient( properties.getMongoHost(), properties.getMongoPort() );
      DB db = mongo.getDB( properties.getMongoDb() );

      DBCollection collection = db.getCollection( properties.getMongoCollection() );

      List<MongoDBRecord> records = TestUtil.generateMongoRecords();
      MongoAggregator.aggregate( collection, records );
      MongoDBRecord record = TestUtil.generateMongoRecord();
      MongoAggregator.aggregate( collection, record );
    }
    catch( UnknownHostException e )
    {
      throw new RuntimeException( e );
    }
    finally
    {
      if( mongo != null )
        mongo.close();
    }
  }


}
