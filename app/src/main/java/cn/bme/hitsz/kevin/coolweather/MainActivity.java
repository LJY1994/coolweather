package cn.bme.hitsz.kevin.coolweather;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import cn.bme.hitsz.kevin.coolweather.fragments.ChooseAreaFragment;


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
    }

    @Override
    protected void onResume() {
        Log.e(TAG, "onResume");
        super.onResume();

    }


}
