package com.spark.szhb_master.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/9/15 0015.
 */

public class TreasureInfo {


    /**
     * availablePower : 0
     * gcxAmount : 0.99
     * unReceiveRewardAmount : 0
     * rewardInfoList : []
     * activityInfoList : []
     */

    private double availablePower;
    private double gcxAmount;
    private int unReceiveRewardAmount;
    private List<?> rewardInfoList;
    private List<?> activityInfoList;

    public double getAvailablePower() {
        return availablePower;
    }

    public void setAvailablePower(double availablePower) {
        this.availablePower = availablePower;
    }

    public double getGcxAmount() {
        return gcxAmount;
    }

    public void setGcxAmount(double gcxAmount) {
        this.gcxAmount = gcxAmount;
    }

    public int getUnReceiveRewardAmount() {
        return unReceiveRewardAmount;
    }

    public void setUnReceiveRewardAmount(int unReceiveRewardAmount) {
        this.unReceiveRewardAmount = unReceiveRewardAmount;
    }

    public List<?> getRewardInfoList() {
        return rewardInfoList;
    }

    public void setRewardInfoList(List<?> rewardInfoList) {
        this.rewardInfoList = rewardInfoList;
    }

    public List<?> getActivityInfoList() {
        return activityInfoList;
    }

    public void setActivityInfoList(List<?> activityInfoList) {
        this.activityInfoList = activityInfoList;
    }
}
