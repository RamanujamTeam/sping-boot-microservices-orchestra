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

  public MinerRecord setId( int id )
  {
    this.id = id;
    return this;
  }

  public String getGender()
  {
    return gender;
  }

  public MinerRecord setGender( String gender )
  {
    this.gender = gender;
    return this;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public MinerRecord setFirstName( String firstName )
  {
    this.firstName = firstName;
    return this;
  }

  public String getLastName()
  {
    return lastName;
  }

  public MinerRecord setLastName( String lastName )
  {
    this.lastName = lastName;
    return this;
  }

  public String getEmail()
  {
    return email;
  }

  public MinerRecord setEmail( String email )
  {
    this.email = email;
    return this;
  }

  public String getIpAddress()
  {
    return ipAddress;
  }

  public MinerRecord setIpAddress( String ipAddress )
  {
    this.ipAddress = ipAddress;
    return this;
  }

  @Override
  public String toString()
  {
    return "MinerRecord{" +
        "id=" + id +
        ", gender='" + gender + '\'' +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", email='" + email + '\'' +
        ", ipAddress='" + ipAddress + '\'' +
        '}';
  }
}
