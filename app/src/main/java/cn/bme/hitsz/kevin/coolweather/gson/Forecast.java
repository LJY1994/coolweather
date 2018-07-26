package cn.bme.hitsz.kevin.coolweather.gson;

import android.widget.TextView;

import com.google.gson.annotations.SerializedName;

public class Forecast {

    public String date;

    @SerializedName("tmp")
    public Temperature temperature;

    public class Temperature {

        public String max;

        public String min;
    }

    @SerializedName("cond")
    public More more;

    public class More {

        @SerializedName("txt_d")
        public String info;
    }
}
