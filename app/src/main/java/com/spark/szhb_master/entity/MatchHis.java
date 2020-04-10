package com.spark.szhb_master.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/7/11 0011.
 */

public class MatchHis implements Serializable {


    /**
     * id : 410686
     * memberId : 50
     * amount : -1.0E-4
     * createTime : 2018-07-03 21:14:02
     * type : 11
     * symbol : GCB
     * address : null
     * fee : 0
     * flag : 0
     */

    private int id;
    private int memberId;
    private double amount;
    private String createTime;
    private int type;
    private String symbol;
    private Object address;
    private int fee;
    private int flag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Object getAddress() {
        return address;
    }

    public void setAddress(Object address) {
        this.address = address;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
