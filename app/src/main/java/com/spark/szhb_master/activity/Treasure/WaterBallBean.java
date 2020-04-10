package com.spark.szhb_master.activity.Treasure;

/**
 * Created by Administrator on 2018/10/12 0012.
 */

public class WaterBallBean {

    /**
     * id : 7
     * createTime : 2018-10-12 09:30:02
     * memberId : 3449
     * completeTime : null
     * amount : 0.5
     * status : 0
     */

    private int id;
    private String createTime;
    private int memberId;
    private Object completeTime;
    private double amount;
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public Object getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Object completeTime) {
        this.completeTime = completeTime;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean	isDefault;

    public WaterBallBean(int id,  double amount,String createTime,boolean isDefault) {
        this.id = id;
        this.amount = amount;
        this.isDefault = isDefault;
        this.createTime = createTime;
    }
}