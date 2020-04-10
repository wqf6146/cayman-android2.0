package com.spark.szhb_master.entity;

/**
 * Created by Administrator on 2018/9/19 0019.
 */

public class MyDigging {


    /**
     * activityId : 1
     * activityCommodityId : 1
     * commodityName : ASUS 2018 电脑
     * cover : https://img.alicdn.com/i2/2/TB1ilbUHpXXXXb8XXXXSutbFXXX.jpg_200x200q100.jpg
     * imgs : http://img.alicdn.com/imgextra/i3/3307465094/O1CN011nV61AmiRtZJZne_!!3307465094.jpg_2200x2200Q50s50.jpg;https://img.alicdn.com/imgextra/i2/3307465094/TB2j92ZoRsmBKNjSZFsXXaXSVXa_!!3307465094.jpg_960x960Q50s50.jpg;https://img.alicdn.com/imgextra/i4/3307465094/TB2okfCoIj_B1NjSZFHXXaDWpXa_!!3307465094.jpg_960x960Q50s50.jpg
     * degree : 5000
     * completeAmount : 24
     * joinAmount : 9
     * voteAmount : null
     * joinVote : null
     * joinActivity : null
     * activityStatus : 1
     * luckUserId : null
     * luckUser : null
     */

    private int activityId;
    private int activityCommodityId;
    private String commodityName;
    private String cover;
    private String imgs;
    private int degree;
    private int completeAmount;
    private int joinAmount;
    private Object voteAmount;
    private Object joinVote;
    private Object joinActivity;
    private int activityStatus;
    private Object luckUserId;
    private Object luckUser;

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getActivityCommodityId() {
        return activityCommodityId;
    }

    public void setActivityCommodityId(int activityCommodityId) {
        this.activityCommodityId = activityCommodityId;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public int getCompleteAmount() {
        return completeAmount;
    }

    public void setCompleteAmount(int completeAmount) {
        this.completeAmount = completeAmount;
    }

    public int getJoinAmount() {
        return joinAmount;
    }

    public void setJoinAmount(int joinAmount) {
        this.joinAmount = joinAmount;
    }

    public Object getVoteAmount() {
        return voteAmount;
    }

    public void setVoteAmount(Object voteAmount) {
        this.voteAmount = voteAmount;
    }

    public Object getJoinVote() {
        return joinVote;
    }

    public void setJoinVote(Object joinVote) {
        this.joinVote = joinVote;
    }

    public Object getJoinActivity() {
        return joinActivity;
    }

    public void setJoinActivity(Object joinActivity) {
        this.joinActivity = joinActivity;
    }

    public int getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(int activityStatus) {
        this.activityStatus = activityStatus;
    }

    public Object getLuckUserId() {
        return luckUserId;
    }

    public void setLuckUserId(Object luckUserId) {
        this.luckUserId = luckUserId;
    }

    public Object getLuckUser() {
        return luckUser;
    }

    public void setLuckUser(Object luckUser) {
        this.luckUser = luckUser;
    }
}
