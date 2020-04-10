package com.spark.szhb_master.entity;

/**
 * Created by Administrator on 2018/9/18 0018.
 */
public class CallHistory {


    /**
     * username : 刘小东
     * commodityName : 手机4
     * amount : 1
     * createTime : 2018-09-18 09:34:52
     */


    private String username;
    private String activityTitle;
    private String commodityName;

    private int amount;
    private String createTime;

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
