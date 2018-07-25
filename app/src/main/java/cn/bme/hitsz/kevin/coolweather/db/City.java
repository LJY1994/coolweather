package cn.bme.hitsz.kevin.coolweather.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class City {
    @Id(autoincrement = true)
    private Long id;

    private String cityName;

    private int privinceId;

    private int cityCode;

    @Generated(hash = 899981801)
    public City(Long id, String cityName, int privinceId, int cityCode) {
        this.id = id;
        this.cityName = cityName;
        this.privinceId = privinceId;
        this.cityCode = cityCode;
    }

    @Generated(hash = 750791287)
    public City() {
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getPrivinceId() {
        return privinceId;
    }

    public void setPrivinceId(int privinceId) {
        this.privinceId = privinceId;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
