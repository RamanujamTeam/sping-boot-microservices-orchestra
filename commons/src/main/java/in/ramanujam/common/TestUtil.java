package in.ramanujam.common;

import in.ramanujam.common.model.MinerRecord;
import in.ramanujam.common.model.MongoDBRecord;
import in.ramanujam.common.model.BitcoinRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: Denys Konakhevych
 * Date: 01.07.2016
 * Time: 19:57
 */
public class TestUtil
{
  public static MongoDBRecord generateMongoRecord()
  {
    BitcoinRecord redisRecord = new BitcoinRecord( 1, "1PtEHHN1SLUACkRhyBADURZWQuTpX56HRo" );
    MinerRecord elasticSearchRecord = new MinerRecord( 1, "Female", "Louise", "Matthews", "lmatthews0@ning.com", "73.145.90.254" );
    return new MongoDBRecord( redisRecord, elasticSearchRecord );
  }

  public static List<MongoDBRecord> generateMongoRecords()
  {
    List<MongoDBRecord> records = new ArrayList<>( );
    for( int id = 1; id <= 1000; id++ )
    {
      BitcoinRecord redisRecord = new BitcoinRecord( id, randomString() );
      MinerRecord elasticSearchRecord = new MinerRecord( id, randomString(), randomString(), randomString(), randomEmail(), randomIP() );
      MongoDBRecord mongoDBRecord = new MongoDBRecord( redisRecord, elasticSearchRecord );
      records.add( mongoDBRecord );
    }
    return records;
  }

  public static BitcoinRecord generateRedisRecord( int id )
  {
    return new BitcoinRecord( id, randomString() );
  }

  public static List<BitcoinRecord> generateRedisRecords( int fromId, int toId )
  {
    List<BitcoinRecord> records = new ArrayList<>( );
    for( int id = fromId; id <= toId; id++ )
      records.add( generateRedisRecord( id ) );
    return records;
  }

  public static MinerRecord generateElasticSearchRecord( int id )
  {
    return new MinerRecord( id, randomString(), randomString(), randomString(), randomEmail(), randomIP() );
  }

  public static List<MinerRecord> generateElasticSearchRecords( int fromId, int toId )
  {
    List<MinerRecord> records = new ArrayList<>( );
    for( int id = fromId; id <= toId; id++ )
      records.add( generateElasticSearchRecord( id ) );
    return records;
  }

  private static String randomString()
  {
    return UUID.randomUUID().toString().replaceAll( "-", "" );
  }

  private static String randomEmail()
  {
    return UUID.randomUUID().toString().replaceAll( "-", "" ) + "@ema.il";
  }

  private static String randomIP()
  {
    Random random = new Random();
    return randInt(random, 0, 255) + "." + randInt(random, 0, 255) + "." + randInt(random, 0, 255) + "." + randInt(random, 0, 255);
  }

  public static int randInt(Random rand, int min, int max) {
    return rand.nextInt((max - min) + 1) + min;
  }
}
