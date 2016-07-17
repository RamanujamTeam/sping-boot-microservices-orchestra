package in.ramanujam.common.model;

public class BitcoinRecord
{
  private Integer id;
  private String key;

  public BitcoinRecord()
  {
  }

  public BitcoinRecord( int id, String key )
  {
    this.id = id;
    this.key = key;
  }

  public Integer getId()
  {
    return id;
  }

  public void setId( Integer id )
  {
    this.id = id;
  }

  public String getKey()
  {
    return key;
  }

  public void setKey( String key )
  {
    this.key = key;
  }
}
