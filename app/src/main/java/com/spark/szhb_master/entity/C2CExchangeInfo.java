package com.spark.szhb_master.entity;

/**
 * Created by Administrator on 2018/3/7.
 */

public class C2CExchangeInfo {
    private String username;
    private int emailVerified;
    private int phoneVerified;
    private int idCardVerified;
    private int transactions;
    private int otcCoinId;
    private String unit;
    private double price;
    private double number;
    private String payMode;
    private double minLimit;
    private double maxLimit;
    private double timeLimit;
    private double remainAmount;
    private Double maxTradableAmount;

    public Double getMaxTradableAmount() {
        return maxTradableAmount;
    }

    private String country;
    private String remark;
    private int advertiseType;

    public double getNumber() {
        return number;
    }

    public void setNumber(double number) {
        this.number = number;
    }

    public String getUsername() {
        return username;
    }

    public int getTransactions() {
        return transactions;
    }

    public int getOtcCoinId() {
        return otcCoinId;
    }

    public String getUnit() {
        return unit;
    }

    public double getPrice() {
        return price;
    }

    public String getPayMode() {
        return payMode;
    }

    public double getMinLimit() {
        return minLimit;
    }

    public double getMaxLimit() {
        return maxLimit;
    }

    public String getRemark() {
        return remark;
    }


    public int getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(int emailVerified) {
        this.emailVerified = emailVerified;
    }

    public int getPhoneVerified() {
        return phoneVerified;
    }

    public void setPhoneVerified(int phoneVerified) {
        this.phoneVerified = phoneVerified;
    }

    public int getIdCardVerified() {
        return idCardVerified;
    }

    public void setIdCardVerified(int idCardVerified) {
        this.idCardVerified = idCardVerified;
    }
}
