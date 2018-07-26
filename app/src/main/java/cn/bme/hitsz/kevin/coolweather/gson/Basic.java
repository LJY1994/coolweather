package cn.bme.hitsz.kevin.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 用于解析天气的JSON信息
 */
public class Basic {

    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update {
        @SerializedName("loc")
        public String updateTime;
    }

}
