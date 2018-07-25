package cn.bme.hitsz.kevin.coolweather.db;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Town {


    @Id(autoincrement = true)
    private Long id;

    private String townName;

    private String weatherId;

    private String cityId;

    private int townCode;

    @Generated(hash = 197056992)
    public Town(Long id, String townName, String weatherId, String cityId,
            int townCode) {
        this.id = id;
        this.townName = townName;
        this.weatherId = weatherId;
        this.cityId = cityId;
        this.townCode = townCode;
    }

    @Generated(hash = 2030923556)
    public Town() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public int getTownCode() {
        return townCode;
    }

    public void setTownCode(int townCode) {
        this.townCode = townCode;
    }
}
