package cn.bme.hitsz.kevin.coolweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e(TAG, "onCreate");
        /*
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        ChooseAreaFragment chooseAreaFragment = new ChooseAreaFragment();
        // transaction.replace(R.id.frg_choose_area, chooseAreaFragment);

         transaction.add(R.id.frg_choose_area, chooseAreaFragment);
        transaction.commit();
        */
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                this);
        if(prefs.getString("weather", null)!=null){
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    protected void onResume() {
        Log.e(TAG, "onResume");
        super.onResume();

    }


}
