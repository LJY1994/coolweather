package cn.bme.hitsz.kevin.coolweather.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bme.hitsz.kevin.coolweather.R;
import cn.bme.hitsz.kevin.coolweather.WeatherActivity;
import cn.bme.hitsz.kevin.coolweather.db.City;
import cn.bme.hitsz.kevin.coolweather.db.City_;
import cn.bme.hitsz.kevin.coolweather.db.Country;
import cn.bme.hitsz.kevin.coolweather.db.Country_;
import cn.bme.hitsz.kevin.coolweather.db.Province;
import cn.bme.hitsz.kevin.coolweather.util.DbUtil;
import cn.bme.hitsz.kevin.coolweather.util.HttpUtil;
import cn.bme.hitsz.kevin.coolweather.util.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChooseAreaFragment extends Fragment {

    private TextView tv_title;
    private Button btn_back;
    private ListView listView;

    private ArrayAdapter<String> arrayAdapter;
    private List<String> dataList = new ArrayList<>();
    /**
     * 省列表
     */
    private List<Province> provinces;
    /**
     * 市列表
     */
    private List<City> cities;
    /**
     * 县列表
     */
    private List<Country> countries;

    /**
     * 选中的地方
     */
    private Province provinceSelected;
    private City citySelected;
   // private Country countrySelected;

    /**
     * 当前选中级别
     */
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY     = 1;
    public static final int LEVEL_COUNTRY  = 2;
    private int currentLevel ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.choose_area, container, false);
        tv_title = view.findViewById(R.id.tv_title);
        btn_back = view.findViewById(R.id.btn_back);
        listView = view.findViewById(R.id.list_view);
        arrayAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1,
                dataList);
        listView.setAdapter(arrayAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if( currentLevel == LEVEL_PROVINCE ){
                    provinceSelected = provinces.get(i);
                    queryCities();
                }else if( currentLevel == LEVEL_CITY ){
                    citySelected = cities.get(i);
                    queryCountries();
                }else{
                    Intent intent = new Intent(getActivity(), WeatherActivity.class);
                    String weatherId = countries.get(i).getWeatherId();
                    intent.putExtra("weather_id", weatherId);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( currentLevel == LEVEL_COUNTRY ){
                    queryCities();
                }else if( currentLevel == LEVEL_CITY ){
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }

    /**
     * 查询全国所有的省，优先从数据库查询，没有再访问服务器
     */
    private void queryProvinces() {

        tv_title.setText("中国");
        btn_back.setVisibility(View.GONE);

        provinces = DbUtil.getInstance().getProvinceBox().getAll();
        if(provinces.size()>0){
            dataList.clear();
            for (Province p:provinces
                 ) {
                dataList.add(p.getProvinceName());
            }
            arrayAdapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        }else{
            String address = "http://guolin.tech/api/china";
            queryFromServer(address, "province");
        }
    }

    /**
     * 查询全国所有的市
     */
    private void queryCities() {
        Log.i("fragment", "queryCities");
        tv_title.setText(provinceSelected.getProvinceName());
        btn_back.setVisibility(View.VISIBLE);

        cities = DbUtil.getInstance().getCityBox().query()
                .equal(City_.privinceId, provinceSelected.getId())
                .build().find();
        if(cities.size()>0){
         //   Log.i("add city", cities.toString());

            dataList.clear();
            arrayAdapter.notifyDataSetChanged();
            for (City p:cities
                    ) {
                dataList.add(p.getCityName());

            }
            arrayAdapter.notifyDataSetChanged();

            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        }else{
            String address = "http://guolin.tech/api/china/"
                    +provinceSelected.getProvinceCode();
            queryFromServer(address, "city");
        }
    }

    /**
     * 查询全国所有的县
     */
    private void queryCountries() {
        Log.i("fragment", "queryCities");

        tv_title.setText(citySelected.getCityName());
        btn_back.setVisibility(View.VISIBLE);

        countries = DbUtil.getInstance().getCountryBox().query()
                .equal(Country_.cityId, citySelected.getId())
                .build().find();
        if(countries.size()>0){
            dataList.clear();
            for (Country p:countries
                    ) {
                dataList.add(p.getCountryName());

            }
            arrayAdapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTRY;
        }else{
            String address = "http://guolin.tech/api/china/"
                    + provinceSelected.getProvinceCode()
                    +"/"+citySelected.getCityCode();
            queryFromServer(address, "country");
        }
    }

    /**
     * 从服务器获取数据
     * @param address 网址
     * @param area    数据类型
     */
    private void queryFromServer(String address, final String area) {
        Log.i("fragment", "queryFromServer");
        //showProgressBar
        showProgressDialog();

        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //closeProgressBar
                        closeProgressDialog();
                        Toast.makeText(getActivity(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String rsp = response.body().string();
                if(rsp==null) return;
                boolean result = false;
                if("province".equals(area)){
                    result = Utility.handleProvincesResponce(rsp);
                }else if("city".equals(area)){
                    result = Utility.handleCitiesResponse(rsp, provinceSelected.getId());
                }else if ("country".equals(area)){
                    result = Utility.handleCountriesResponse(rsp, citySelected.getId());
                }
                if(result){
                    if(getActivity()!=null)
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //closeProgressBar
                            closeProgressDialog();
                            if("province".equals(area)){
                                queryProvinces();
                            }else if("city".equals(area)){
                                queryCities();
                            }else if ("country".equals(area)){
                                queryCountries();
                            }
                        }
                    });
                }
            }
        });
    }

    private ProgressDialog progressDialog;
    private void showProgressDialog(){
        if( progressDialog == null ){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog(){
        if( progressDialog != null ){
            progressDialog.dismiss();
        }
    }
}
