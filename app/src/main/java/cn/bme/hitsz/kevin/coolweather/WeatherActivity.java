package cn.bme.hitsz.kevin.coolweather;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;

import cn.bme.hitsz.kevin.coolweather.gson.Forecast;
import cn.bme.hitsz.kevin.coolweather.gson.Weather;
import cn.bme.hitsz.kevin.coolweather.util.HttpUtil;
import cn.bme.hitsz.kevin.coolweather.util.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private static final String TAG = "WeatherActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);

        initView();

        initData();
    }

    private void initData() {
        
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        if(weatherString != null){
            //有缓存时读缓存
            Weather weather = Utility.handleWeatherResponse(weatherString);

            showWeatherInfo(weather);
        }else{
            //无缓存时向服务器咨询
            String weatherId = getIntent().getStringExtra("weather_id");
            scrollView.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }

        String binPic = prefs.getString("bind_pic", null);
        if(binPic!=null){
            Glide.with(this).load(binPic).into(bingPicImg);
        }else {
            loadBindPic();
        }
    }


    /**
     * 加载每日一图
     */
    private void loadBindPic() {
        String requestBindPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBindPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bindPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager
                        .getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bind_pic", bindPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bindPic).into(bingPicImg);
                    }
                });
            }
        });
    }

    /**
     * 根据天气id请求城市天气
     */
    private void requestWeather(String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid="+
                weatherId + "&key=122f18d473dd4cef9e119486c4bbeda0";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败",
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String weatherInfo = response.body().string();
                Log.e(TAG, weatherInfo);
                final Weather weather = Utility.handleWeatherResponse(weatherInfo);
                Log.e(TAG, weather.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather!=null){
                            SharedPreferences.Editor editor = PreferenceManager
                                    .getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", weatherInfo);
                            editor.apply();
                            showWeatherInfo(weather);
                        }else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败",
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
            }
        });
        loadBindPic();
    }

    /**
     * 处理并展示天气信息
     * @param weather
     */
    private void showWeatherInfo(Weather weather) {

        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature+"℃";
        String weatherInfo = weather.now.more.info;

        titleCity.setText(cityName);
        degreeText.setText(degree);
        titleUpdateTime.setText(updateTime);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for(Forecast forecast : weather.forecastList){
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,
                    forecastLayout, false);
            TextView dataText = view.findViewById(R.id.tv_date);
            dataText.setText(forecast.date);
            TextView infoText = view.findViewById(R.id.tv_info);
            infoText.setText(forecast.more.info);
            TextView maxText = view.findViewById(R.id.tv_max_tmp);
            maxText.setText(forecast.temperature.max);
            TextView minText = view.findViewById(R.id.tv_min_tmp);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }
        if (weather.aqi!=null){
            apiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }
        String comfort = "舒适度："+weather.suggestion.comfort.info;
        String carWash = "洗车指数："+weather.suggestion.carWash.info;
        String sport = "运动建议："+weather.suggestion.sport.info;

        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
    }

    private ImageView bingPicImg;

    private ScrollView scrollView;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView apiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;

    private void initView() {
        scrollView = findViewById(R.id.layout_weather);
        titleCity = findViewById(R.id.tv_city_title);
        titleUpdateTime = findViewById(R.id.tv_update_time);
        degreeText = findViewById(R.id.tv_degree);
        weatherInfoText = findViewById(R.id.tv_weather_info);
        forecastLayout = findViewById(R.id.layout_forecast);
        apiText =  findViewById(R.id.tv_aqi);
        pm25Text =  findViewById(R.id.tv_pm25);
        comfortText =  findViewById(R.id.tv_comft);
        carWashText =  findViewById(R.id.tv_car_wash);
        sportText =  findViewById(R.id.tv_sport);
        
        bingPicImg = findViewById(R.id.bing_pic_img);
    }
}
