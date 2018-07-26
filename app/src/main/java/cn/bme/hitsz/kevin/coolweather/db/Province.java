package cn.bme.hitsz.kevin.coolweather.db;


import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
/**
 * 省级表
 */
public class Province {
    @Id
    private Long id;

    private String provinceName;

    private int provinceCode;


    public Province() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
