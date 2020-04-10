package com.spark.szhb_master.entity;

/**
 * Created by Administrator on 2018/9/18 0018.
 */
public class PowerDetail {


    /**
     * id : 54
     * memberId : 3442
     * amount : 0.74
     * reduceAmount : 0.26
     * type : 0
     * createTime : 2018-09-20 22:00:00
     * activityId : null
     * activityCommodityId : null
     */

    private int id;
    private int memberId;
    private double amount;
    private double reduceAmount;
    private int type;
    private String createTime;
    private Object activityId;
    private Object activityCommodityId;

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

    public double getReduceAmount() {
        return reduceAmount;
    }

    public void setReduceAmount(double reduceAmount) {
        this.reduceAmount = reduceAmount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Object getActivityId() {
        return activityId;
    }

    public void setActivityId(Object activityId) {
        this.activityId = activityId;
    }

    public Object getActivityCommodityId() {
        return activityCommodityId;
    }

    public void setActivityCommodityId(Object activityCommodityId) {
        this.activityCommodityId = activityCommodityId;
    }
}
