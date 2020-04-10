package com.spark.szhb_master.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/2/28.
 */

public class C2C {
    private int currentPage;
    private int totalPage;
    private int pageNumber;
    private int totalElement;

    public int getTotalElement() {
        return totalElement;
    }

    public void setTotalElement(int totalElement) {
        this.totalElement = totalElement;
    }

    private List<C2CBean> context;

    public List<C2CBean> getContext() {
        return context;
    }


    public static class C2CBean implements Serializable {
        private String advertiseType;
        private String memberName;
        private String avatar;
        private int advertiseId;
        private int transactions;
        private double price;
        private double minLimit;
        private double maxLimit;
        private double remainAmount;
        private String createTime;
        private String payMode;
        private int coinId;
        private String unit;
        private String coinName;
        private String coinNameCn;
        private String completeRate;
        private int realVerified;
        private int emailVerified;
        private int phoneVerified;



        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getAdvertiseType() {
            return advertiseType;
        }


        public String getMemberName() {
            return memberName;
        }


        public int getAdvertiseId() {
            return advertiseId;
        }


        public int getTransactions() {
            return transactions;
        }


        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getMinLimit() {
            return minLimit;
        }


        public double getMaxLimit() {
            return maxLimit;
        }


        public double getRemainAmount() {
            return remainAmount;
        }


        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getPayMode() {
            return payMode;
        }


        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public int getRealVerified() {
            return realVerified;
        }

        public void setRealVerified(int realVerified) {
            this.realVerified = realVerified;
        }

        public int getEmailVerified() {
            return emailVerified;
        }

        public void setEmailVerified(int emailVerified) {
            this.emailVerified = emailVerified;
        }

        public int getPhoneVerified() {
            return phoneVerified;
        }

        public void setPhoneVerified(int phoneVerified) {
            this.phoneVerified = phoneVerified;
        }

        public String getCompleteRate() {
            return completeRate;
        }

        public void setCompleteRate(String completeRate) {
            this.completeRate = completeRate;
        }
    }
}
