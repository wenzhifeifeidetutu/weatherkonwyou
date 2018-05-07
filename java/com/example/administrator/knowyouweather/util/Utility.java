package com.example.administrator.knowyouweather.util;

import android.text.TextUtils;

import com.example.administrator.knowyouweather.db.City;
import com.example.administrator.knowyouweather.db.County;
import com.example.administrator.knowyouweather.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/5/8.
 */

public class Utility {

    //province
    public static boolean handleProvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allProvinces = new JSONArray(response);
                for (int i = 0; i <allProvinces.length(); i++) {
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceId(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            }catch (JSONException e) {
                e.printStackTrace();
            }


        }
        return false;

    }

    //city

    public static boolean handleCityResponse(String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allcities = new JSONArray(response);
                for (int i = 0; i < allcities.length(); i++) {
                   JSONObject cityObject = allcities.getJSONObject(i);
                    City city = new City();
                    city.setCityId(cityObject.getInt("id"));
                    city.setCityName(cityObject.getString("name"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    //county
    public static boolean handleCountyResponse(String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCounties = new JSONArray();
                for (int i = 0; i < allCounties.length(); i++ ) {
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCityId(countyObject.getInt("id"));
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherid(countyObject.getString("weather_id"));
                    county.save();
                }
                return true;
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}
