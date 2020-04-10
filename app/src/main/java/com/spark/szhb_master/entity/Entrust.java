package com.spark.szhb_master.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/30.
 */

public class Entrust implements Serializable {

    private String orderId;
    private int memberId;
    private String type; // MARKET_PRICE LIMIT_PRICE
    private double amount;
    private String symbol;
    private double tradedAmount;
    private double turnover;
    private String coinSymbol;
    private String baseSymbol;
    private String status;
    private String direction;
    private double price;
    private long time;
    private String completedTime;
    private String canceledTime;

    public String getOrderId() {
        return orderId;
    }


    public String getType() {
        return type;
    }


    public double getAmount() {
        return amount;
    }


    public String getSymbol() {
        return symbol;
    }


    public double getTradedAmount() {
        return tradedAmount;
    }


    public double getTurnover() {
        return turnover;
    }


    public String getCoinSymbol() {
        return coinSymbol;
    }


    public String getBaseSymbol() {
        return baseSymbol;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }


}
