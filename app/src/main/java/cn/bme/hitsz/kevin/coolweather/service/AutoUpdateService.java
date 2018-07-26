package cn.bme.hitsz.kevin.coolweather.service;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import cn.bme.hitsz.kevin.coolweather.WeatherActivity;
import cn.bme.hitsz.kevin.coolweather.gson.Weather;
import cn.bme.hitsz.kevin.coolweather.util.HttpUtil;
import cn.bme.hitsz.kevin.coolweather.util.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {

    private static final String TAG = "AutoUpdateService";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
     启动时，更新天气，更新背景图，定闹钟
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBingPic();
        //声明一个定时闹钟
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 8*60*60*1000; // 8 hours
        Long triggeAtTime = SystemClock.elapsedRealtime() +anHour;
        Intent i = new Intent(this, AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggeAtTime, pi);

        return super.onStartCommand(intent, flags, startId);
    }

    private void updateBingPic() {
       String requestBindPic = "http://guolin.tech/api/bing_pic";

       HttpUtil.sendOkHttpRequest(requestBindPic, new Callback() {
           @Override
           public void onFailure(Call call, IOException e) {
               e.printStackTrace();
           }

           @Override
           public void onResponse(Call call, final Response response) throws IOException {
               final String bindPic = response.body().string();

               SharedPreferences.Editor editor = PreferenceManager
                       .getDefaultSharedPreferences(AutoUpdateService.this).edit();
               editor.putString("bind_pic", bindPic);
               editor.apply();
           }
       });
    }

    private void updateWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        if(weatherString != null) {
            //有缓存时读缓存
            Weather weather = Utility.handleWeatherResponse(weatherString);
            String weatherId = weather.basic.weatherId;
            String weatherUrl = "http://guolin.tech/api/weather?cityid="+
                    weatherId + "&key=122f18d473dd4cef9e119486c4bbeda0";
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    final String weatherInfo = response.body().string();
                    Log.e(TAG, weatherInfo);
                    final Weather weather = Utility.handleWeatherResponse(weatherInfo);

                    if(weather!=null&&"ok".equals(weather.status)){
                        SharedPreferences.Editor editor = PreferenceManager
                                .getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("weather", weatherInfo);
                        editor.apply();
                    }
                }
            });
        }

    }
}
