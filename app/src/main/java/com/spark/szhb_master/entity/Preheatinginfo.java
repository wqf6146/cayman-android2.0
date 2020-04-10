package com.spark.szhb_master.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/9/17 0017.
 */

public class Preheatinginfo {


    /**
     * activityId : 1
     * title : 迎中秋第一期
     * startTime : 2018-09-16 10:00:00
     * endTime : 2018-09-17 16:00:00
     * activityCommodityInfoList : [{"activityCommodityId":1,"commodityName":"ASUS 2018 电脑","cover":"","degree":0,"completeAmount":null,"joinAmount":0,"voteAmount":0,"joinVote":1,"activityStatus":0,"luckUser":null}]
     */

    private int activityId;
    private String title;
    private String startTime;
    private String endTime;
    private List<ActivityCommodityInfoListBean> activityCommodityInfoList;

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<ActivityCommodityInfoListBean> getActivityCommodityInfoList() {
        return activityCommodityInfoList;
    }

    public void setActivityCommodityInfoList(List<ActivityCommodityInfoListBean> activityCommodityInfoList) {
        this.activityCommodityInfoList = activityCommodityInfoList;
    }

    public static class ActivityCommodityInfoListBean {
        /**
         * activityCommodityId : 1
         * commodityName : ASUS 2018 电脑
         * cover :
         * degree : 0
         * completeAmount : null
         * joinAmount : 0
         * voteAmount : 0
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
        private int activityStatus;
        private Object luckUser;

        public String getImgs() {
            return imgs;
        }

        public void setImgs(String imgs) {
            this.imgs = imgs;
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

        public Object getLuckUser() {
            return luckUser;
        }

        public void setLuckUser(Object luckUser) {
            this.luckUser = luckUser;
        }
    }
}
