package cn.bme.hitsz.kevin.coolweather.db;


import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class City {
    @Id
    private Long id;

    private String cityName;

    private long privinceId;

    private int cityCode;

    public City() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public long getPrivinceId() {
        return privinceId;
    }

    public void setPrivinceId(long privinceId) {
        this.privinceId = privinceId;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    @Override
    public String toString() {
        return this.getCityName()+" "+this.getCityCode()+" "+this.getPrivinceId();
    }
}
