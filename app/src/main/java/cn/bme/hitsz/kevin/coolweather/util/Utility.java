package cn.bme.hitsz.kevin.coolweather.util;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bme.hitsz.kevin.coolweather.db.City;
import cn.bme.hitsz.kevin.coolweather.db.Country;
import cn.bme.hitsz.kevin.coolweather.db.Province;

public class Utility {
    /**
     * 解析和处理服务器返回的省级数据
     */
    public static boolean handleProvincesResponce(String response){
        if( !TextUtils.isEmpty(response)){
            try{
                JSONArray allProvinces = new JSONArray(response);
                List<Province> allProvince = new ArrayList<>(36);
                for (int i = 0; i < allProvinces.length(); i++) {
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    // add to list
                    allProvince.add(province);
                }
                /* push to db */
                DbUtil.getInstance().getProvinceBox().put(allProvince);
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     */
    public static boolean handleCitiesResponse(String response, long provinceId){
        if( !TextUtils.isEmpty(response)){
            try{
                JSONArray allCities = new JSONArray(response);
                List<City> allCity = new ArrayList<>(16);
                for (int i = 0; i < allCities.length(); i++) {
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setPrivinceId(provinceId);
                    // add to list
                    allCity.add(city);
                }
                /* push to db */
                DbUtil.getInstance().getCityBox().put(allCity);
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的县级数据
     */
    public static boolean handleCountriesResponse(String response, long cityId){
        if( !TextUtils.isEmpty(response)){
            try{
                JSONArray allCountries = new JSONArray(response);
                List<Country> allCountry = new ArrayList<>(16);
                for (int i = 0; i < allCountries.length(); i++) {
                    JSONObject countryObject = allCountries.getJSONObject(i);
                    Country country = new Country();
                    country.setCountryName(countryObject.getString("name"));
                    country.setCountryCode(countryObject.getInt("id"));
                    country.setWeatherId(countryObject.getString("weather_id"));
                    country.setCityId(cityId);
                    // add to list
                    allCountry.add(country);
                }
                /* push to db */
                DbUtil.getInstance().getCountryBox().put(allCountry);
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }
}
