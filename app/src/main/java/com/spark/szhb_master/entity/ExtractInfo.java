package com.spark.szhb_master.entity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/3/8.
 */

public class ExtractInfo {

    private String unit;
    private double threshold;
    private double minAmount;
    private double maxAmount;
    private double minTxFee;
    private double maxTxFee;
    private String nameCn;
    private String name;
    private double canAutoWithdraw;
    private double balance;
    private ArrayList<Address> addresses;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getMinAmount() {
        return minAmount;
    }

    public double getMinTxFee() {
        return minTxFee;
    }

    public double getMaxTxFee() {
        return maxTxFee;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public ArrayList<Address> getAddresses() {
        return addresses;
    }

}
