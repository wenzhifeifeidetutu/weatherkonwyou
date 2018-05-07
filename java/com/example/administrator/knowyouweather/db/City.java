package com.example.administrator.knowyouweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2018/5/8.
 */

public class City extends DataSupport {

    private  int id;

    private String cityName;

    private int cityId;

    private int provinceId;

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;

    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName =cityName;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
