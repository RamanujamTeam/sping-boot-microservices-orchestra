package in.ramanujam.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Objects;

public class MinerRecord {
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

    public MinerRecord() {
    }

    public MinerRecord(Map map) {
        this.id = (Integer) map.get("id");
        this.gender = (String) map.get("gender");
        this.firstName = (String) map.get("first_name");
        this.lastName = (String) map.get("last_name");
        this.email = (String) map.get("email");
        this.ipAddress = (String) map.get("ip_address");
    }

    public MinerRecord(Integer id, String gender, String firstName, String lastName, String email,
                       String ipAddress) {
        this.id = id;
        this.gender = gender;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.ipAddress = ipAddress;
    }

    public Integer getId() {
        return id;
    }

    public MinerRecord setId(int id) {
        this.id = id;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public MinerRecord setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public MinerRecord setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public MinerRecord setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public MinerRecord setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public MinerRecord setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }

    @Override
    public String toString() {
        return "MinerRecord{" +
                "id=" + id +
                ", gender='" + gender + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MinerRecord that = (MinerRecord) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(gender, that.gender) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(email, that.email) &&
                Objects.equals(ipAddress, that.ipAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, gender, firstName, lastName, email, ipAddress);
    }
}
