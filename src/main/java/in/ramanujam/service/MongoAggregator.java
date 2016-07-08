package in.ramanujam.service;

import com.mongodb.*;
import in.ramanujam.model.MongoDBRecord;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Denys Konakhevych
 * Date: 01.07.2016
 * Time: 18:46
 */
public class MongoAggregator
{
  public static void aggregate( DBCollection collection, List<MongoDBRecord> records )
  {
    records.stream().forEach( record -> aggregate( collection, record ) );
  }

  public static WriteResult aggregate( DBCollection collection, MongoDBRecord record )
  {
    BasicDBObject document = new BasicDBObject();

    BasicDBObject values = new BasicDBObject()
            .append( "bitcoin", record.getRedisRecord().getKey() )
            .append( "gender", record.getElasticSearchRecord().getGender() )
            .append( "first_name", record.getElasticSearchRecord().getFirstName() )
            .append( "last_name", record.getElasticSearchRecord().getLastName() )
            .append( "email", record.getElasticSearchRecord().getEmail() )
            .append( "ip_address", record.getElasticSearchRecord().getIpAddress() );

    document.append( "$set", values );

    BasicDBObject searchQuery = new BasicDBObject().append( "_id", record.getRedisRecord().getId() );

    return collection.update( searchQuery, document, true, false );
  }
}
