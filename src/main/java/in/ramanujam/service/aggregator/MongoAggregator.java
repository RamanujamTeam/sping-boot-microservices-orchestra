package in.ramanujam.service.aggregator;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import in.ramanujam.model.MongoDBRecord;
import in.ramanujam.properties.MongoDBProperties;
import org.bson.Document;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Denys Konakhevych
 * Date: 01.07.2016
 * Time: 18:46
 */
public class MongoAggregator
{
  private static MongoAggregator instance = new MongoAggregator();
  private final MongoClient mongo;
  private final MongoCollection<Document> collection;

  public static MongoAggregator getInstance()
  {
    return instance;
  }

  private MongoAggregator()
  {
    MongoDBProperties properties = new MongoDBProperties();
    mongo = new MongoClient( properties.getMongoHost(), properties.getMongoPort() );
    MongoDatabase database = mongo.getDatabase( properties.getMongoDb() );
    this.collection = database.getCollection( properties.getMongoCollection() );
  }

  public void close()
  {
    if( mongo != null )
      mongo.close();
  }

  public void aggregate( List<MongoDBRecord> records )
  {
    records.stream().forEach( this::aggregate );
  }

  public UpdateResult aggregate( MongoDBRecord record )
  {
    Document document = new Document();

    Document values = new Document( "bitcoin", record.getRedisRecord().getKey() )
            .append( "gender", record.getElasticSearchRecord().getGender() )
            .append( "first_name", record.getElasticSearchRecord().getFirstName() )
            .append( "last_name", record.getElasticSearchRecord().getLastName() )
            .append( "email", record.getElasticSearchRecord().getEmail() )
            .append( "ip_address", record.getElasticSearchRecord().getIpAddress() );

    document.append( "$set", values );

    Document searchQuery = new Document( "_id", record.getRedisRecord().getId() );

    return collection.updateOne( searchQuery, document, new UpdateOptions().upsert( true ) );
  }
}
