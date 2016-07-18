package in.ramanujam.redis.aggregator;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import in.ramanujam.common.properties.MongoDBProperties;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;

@Component
public class MongoUtils
{
  private static DBCollection collection;

  private static DBCollection setupCollection() throws UnknownHostException
  {
    MongoDBProperties properties = new MongoDBProperties();
    MongoClient mongo = new MongoClient( properties.getMongoHost(), properties.getMongoPort() );
    mongo.setWriteConcern( WriteConcern.SAFE );
    DB db = mongo.getDB( properties.getMongoDb() );

    return db.getCollection( properties.getMongoCollection() );
  }

  public DBCollection getCollection() throws UnknownHostException
  {
    if( collection != null )
      return collection;
    else return collection = setupCollection();
  }
}
