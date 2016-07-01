package in.ramanujam.model;

/**
 * Created with IntelliJ IDEA.
 * User: Denys Konakhevych
 * Date: 01.07.2016
 * Time: 17:30
 */
public class ElasticSearchRecord
{
  private int id;
  private String gender;
  private String firstName;
  private String lastName;
  private String email;
  private String ipAddress;

  public ElasticSearchRecord()
  {
  }

  public ElasticSearchRecord( int id, String gender, String firstName, String lastName, String email,
                              String ipAddress )
  {
    this.id = id;
    this.gender = gender;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.ipAddress = ipAddress;
  }

  public int getId()
  {
    return id;
  }

  public void setId( int id )
  {
    this.id = id;
  }

  public String getGender()
  {
    return gender;
  }

  public void setGender( String gender )
  {
    this.gender = gender;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  public String getEmail()
  {
    return email;
  }

  public void setEmail( String email )
  {
    this.email = email;
  }

  public String getIpAddress()
  {
    return ipAddress;
  }

  public void setIpAddress( String ipAddress )
  {
    this.ipAddress = ipAddress;
  }
}
