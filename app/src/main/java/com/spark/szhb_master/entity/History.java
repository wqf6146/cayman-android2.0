package com.spark.szhb_master.entity;

/**
 * Created by Administrator on 2018/9/18 0018.
 */
public class History {


    /**
     * activityCommodityId : 11
     * commodityName : 手机4
     * cover : https://img.alicdn.com/imgextra/i4/612748136/TB2o_yiaVkkyKJjSszfXXbdiFXa_!!612748136.jpg_160x160q80.jpg
     * imgs :
     * degree : 7500
     * completeAmount : null
     * joinAmount : 6
     * voteAmount : 17
     * joinVote : 1
     * activityStatus : 0
     * luckUser : null
     */

    private int activityCommodityId;
    private String commodityName;
    private String cover;
    private String imgs;
    private int degree;
    private int completeAmount;
    private int joinAmount;
    private int voteAmount;
    private int joinVote;
    private int joinActivity;
    private int activityStatus;
    private String luckUser;
    private int luckUserId;

    public int getLuckUserId() {
        return luckUserId;
    }

    public void setLuckUserId(int luckUserId) {
        this.luckUserId = luckUserId;
    }

    public int getActivityCommodityId() {
        return activityCommodityId;
    }

    public void setActivityCommodityId(int activityCommodityId) {
        this.activityCommodityId = activityCommodityId;
    }

    public int getJoinActivity() {
        return joinActivity;
    }

    public void setJoinActivity(int joinActivity) {
        this.joinActivity = joinActivity;
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

    public int getVoteAmount() {
        return voteAmount;
    }

    public void setVoteAmount(int voteAmount) {
        this.voteAmount = voteAmount;
    }

    public int getJoinVote() {
        return joinVote;
    }

    public void setJoinVote(int joinVote) {
        this.joinVote = joinVote;
    }

    public int getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(int activityStatus) {
        this.activityStatus = activityStatus;
    }

    public String getLuckUser() {
        return luckUser;
    }

    public void setLuckUser(String luckUser) {
        this.luckUser = luckUser;
    }
}
