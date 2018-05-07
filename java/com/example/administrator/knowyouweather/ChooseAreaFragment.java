package com.example.administrator.knowyouweather;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.knowyouweather.db.City;
import com.example.administrator.knowyouweather.db.County;
import com.example.administrator.knowyouweather.db.Province;
import com.example.administrator.knowyouweather.util.HttpUtil;
import com.example.administrator.knowyouweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/5/8.
 */

public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVINCE = 0;

    public static final int LEVEL_CITY = 1;

    public static final  int LEVEL_COUNTY = 2;

    private ProgressDialog progressDialog;

    private TextView titleText;

    private Button backButton;

    private ListView listView;

    private ArrayAdapter<String> adapter;

    private List<String> dataList = new ArrayList<>();

    //provinceList
    private List<Province> provinceList;

    //cityList
    private List<City> cityList;

    //countyList

    private List<County> countyList;


    //select province
    private Province selectedProvince;

    private City selectedCity;

    private int currentLevel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        titleText =(TextView)view.findViewById(R.id.title_text);
        backButton = (Button)view.findViewById(R.id.back_button);
        listView =(ListView)view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, dataList);

        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provinceList.get(i);
                    queryCities();
                }else if (currentLevel == LEVEL_CITY) {
                    selectedCity = cityList.get(i);
                    queryCounties();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentLevel == LEVEL_COUNTY) {
                    queryCities();
                }else if (currentLevel == LEVEL_CITY) {
                    queryProvinces();
                }
            }
        });
    }

    private void queryProvinces() {
        titleText.setText("China");
        backButton.setVisibility(View.GONE);
        provinceList =DataSupport.findAll(Province.class);
        if (provinceList.size() >0 ) {
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());

            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;

        }else {
            String address = "http://guolin.tech/api/china";
            queryFromServer(address, "province");

        }

    }

    private void queryCities() {
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("povinceid = ?", String.valueOf(selectedProvince.getId())).find(City.class);

        if (cityList.size() > 0) {
            dataList.clear();
            for (City city: cityList) {
                dataList.add(city.getCityName());
            }

            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel  = LEVEL_CITY;

        }else {
            int provinceId = selectedProvince.getProvinceId();
            String address = "http://guolin.tech/api/china" + provinceId;
            queryFromServer(address, "city");

        }
    }


    private void queryCounties() {
        titleText.setText("China");
        backButton.setVisibility(View.VISIBLE);
        countyList =DataSupport.where("cityid = ?", String.valueOf(selectedCity.getCityId())).find(County.class);
        if (cityList.size() >0 ) {
            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getCountyName());

            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;

        }else {
            int provinceCode = selectedProvince.getProvinceId();
            int cityCode = selectedCity.getCityId();
            String address = "http://guolin.tech/api/china" + provinceCode + "/"+cityCode;
            queryFromServer(address, "county");

        }

    }


    private void queryFromServer(String address, final  String type) {
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(), "file", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if ("province".equals(type)) {
                    result = Utility.handleProvinceResponse(responseText);

                }else if ("city".equals(type)) {
                    result = Utility.handleCityResponse(responseText, selectedProvince.getId());

                }else if ("county".equals(type)) {
                    result = Utility.handleCountyResponse(responseText, selectedCity.getCityId());

                }
                if (result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)) {
                                queryProvinces();
                            }else if ("city".equals(type)) {
                                queryCities();
                            }else if ("county".equals(type)) {
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });
    }

    //dialog
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("please wait!");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }


    //close
    private void  closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }



}
