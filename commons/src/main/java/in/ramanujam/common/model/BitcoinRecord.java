package in.ramanujam.common.model;

import java.util.Objects;

public class BitcoinRecord implements Comparable<BitcoinRecord>{
    private Integer id;
    private String key;

    public BitcoinRecord() {
    }

    public BitcoinRecord(int id, String key) {
        this.id = id;
        this.key = key;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BitcoinRecord that = (BitcoinRecord) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, key);
    }

    @Override
    public int compareTo(BitcoinRecord o) {
        if(this.getId() != o.getId())
            return this.getId().compareTo(o.getId());
        return this.getKey().compareTo(o.getKey());
    }
}
