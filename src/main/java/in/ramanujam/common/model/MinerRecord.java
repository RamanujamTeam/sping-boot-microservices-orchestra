package in.ramanujam.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created with IntelliJ IDEA.
 * User: Denys Konakhevych
 * Date: 01.07.2016
 * Time: 17:30
 */
public class MinerRecord
{
  @JsonProperty
  private Integer id;
  @JsonProperty
  private String gender;
  @JsonProperty("first_name")
  private String firstName;
  @JsonProperty("last_name")
  private String lastName;
  @JsonProperty
  private String email;
  @JsonProperty("ip_address")
  private String ipAddress;

  public MinerRecord()
  {
  }

  public MinerRecord( Integer id, String gender, String firstName, String lastName, String email,
                      String ipAddress )
  {
    this.id = id;
    this.gender = gender;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.ipAddress = ipAddress;
  }

  public Integer getId()
  {
    return id;
  }

  public void setId( Integer id )
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
