package com.spark.szhb_master.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/2/5.
 */

public class Ads implements Serializable {
    private int id;//
    private int advertiseType;// 0 BUY 1  SELL
    private double minLimit;//
    private double maxLimit;//
    private int status;//0 上架 1 下架 2 关闭
    private double remainAmount;
    private String coinUnit;//
    private String createTime;
    private int coinId;
    private String coinName;
    private String coinNameCn;
    private Country country;
    private int priceType; // 0  固定 1 变动
    private double price;
    private String remark;
    private int timeLimit;
    private double premiseRate;
    private String payMode;
    private double number;
    private double marketPrice;
    private int auto;
    private String autoword;
    private Coin coin;

    public Coin getCoin() {
        return coin;
    }

    public int getId() {
        return id;
    }

    public int getAdvertiseType() {
        return advertiseType;
    }

    public double getMinLimit() {
        return minLimit;
    }

    public double getMaxLimit() {
        return maxLimit;
    }

    public int getStatus() {
        return status;
    }

    public double getRemainAmount() {
        return remainAmount;
    }

    public String getCoinUnit() {
        return coinUnit;
    }

    public int getCoinId() {
        return coinId;
    }

    public Country getCountry() {
        return country;
    }

    public int getPriceType() {
        return priceType;
    }

    public double getPrice() {
        return price;
    }

    public String getRemark() {
        return remark;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public double getPremiseRate() {
        return premiseRate;
    }

    public String getPayMode() {
        return payMode;
    }

    public double getNumber() {
        return number;
    }

    public double getMarketPrice() {
        return marketPrice;
    }

    public int getAuto() {
        return auto;
    }

    public String getAutoword() {
        return autoword;
    }

    public class Coin implements Serializable {
        private String unit;

        public String getUnit() {
            return unit;
        }

    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
