package in.ramanujam.model;

/**
 * Created with IntelliJ IDEA.
 * User: Denys Konakhevych
 * Date: 01.07.2016
 * Time: 17:30
 */
public class RedisRecord
{
  private int id;
  private String bitcoin;

  public RedisRecord()
  {
  }

  public RedisRecord( int id, String bitcoin )
  {
    this.id = id;
    this.bitcoin = bitcoin;
  }

  public int getId()
  {
    return id;
  }

  public void setId( int id )
  {
    this.id = id;
  }

  public String getBitcoin()
  {
    return bitcoin;
  }

  public void setBitcoin( String bitcoin )
  {
    this.bitcoin = bitcoin;
  }
}
