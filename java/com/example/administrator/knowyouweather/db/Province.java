package com.example.administrator.knowyouweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2018/5/8.
 */

public class Province extends DataSupport {
    private int id;

    private String provinceName;

    private int provinceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceId() {
        return this.provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }


}
