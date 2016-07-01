package in.ramanujam.service;

import com.mongodb.*;
import in.ramanujam.model.MongoDBRecord;

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
      mongo = new MongoClient( "192.168.99.100", 32771 );

      DB db = mongo.getDB( "mydb" );

      DBCollection collection = db.getCollection( "dummyColl" );

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
