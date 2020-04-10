package com.spark.szhb_master.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/8.
 */

public class Address implements Serializable{
    private String remark;
    private String address;

    public String getAddress() {
        return address;
    }

    public String getRemark() {
        return remark;
    }

}
