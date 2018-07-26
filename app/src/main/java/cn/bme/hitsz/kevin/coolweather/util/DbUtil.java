package cn.bme.hitsz.kevin.coolweather.util;

import cn.bme.hitsz.kevin.coolweather.db.City;
import cn.bme.hitsz.kevin.coolweather.db.Country;
import cn.bme.hitsz.kevin.coolweather.db.MyObjectBox;
import cn.bme.hitsz.kevin.coolweather.db.Province;
import io.objectbox.Box;
import io.objectbox.BoxStore;

public class DbUtil {

    public static final String provinceDb = "table_provinces";
    public static final String cityDb = "table_cities";
    public static final String countryDb = "table_countries";

    private Box<Province> provinceBox;
    private Box<City> cityBox;
    private Box<Country> countryBox;

    public static DbUtil getInstance(){
        return DbManager.dbManager;
    }

    private static class DbManager {
        private final static DbUtil dbManager = new DbUtil();
    }

     private DbUtil (){
         BoxStore boxStore = MyObjectBox.builder().androidContext(MyApplication.getContext()).build();
         countryBox = boxStore.boxFor(Country.class);
         cityBox = boxStore.boxFor(City.class);
         provinceBox = boxStore.boxFor(Province.class);
     }

     public Box<Province> getProvinceBox() {
         return provinceBox;
     }

     public Box<City> getCityBox() {
         return cityBox;
     }

     public Box<Country> getCountryBox() {
         return countryBox;
     }



}
