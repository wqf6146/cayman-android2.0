package com.spark.szhb_master.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/9/14 0014.
 */

public class Treasure {
    /**
     * data : {"items":[{"time":"2018:9:14  10:30","content":[{"memberName":"Despacito","avatar":null,"advertiseId":152,"transactions":1637,"price":6.3,"minLimit":500,"maxLimit":1000,"remainAmount":670,"createTime":"2018-06-20 15:22:54","payMode":"支付宝","coinId":2,"unit":"USDT","coinName":"USDT","coinNameCn":"泰达币","level":2,"advertiseType":0,"advType":0,"premiseRate":null,"completeRate":72,"realVerified":1,"emailVerified":1,"phoneVerified":1},{"memberName":"星语星愿","avatar":"http://caymanex.oss-cn-hangzhou.aliyuncs.com/2018/06/12/deab3613-6de2-4a5c-8c7e-e18e1e0703ec.jpg","advertiseId":237,"transactions":167,"price":6.2,"minLimit":100,"maxLimit":1000,"remainAmount":244.63908681,"createTime":"2018-08-05 10:17:01","payMode":"支付宝","coinId":2,"unit":"USDT","coinName":"USDT","coinNameCn":"泰达币","level":2,"advertiseType":0,"advType":0,"premiseRate":null,"completeRate":73,"realVerified":1,"emailVerified":1,"phoneVerified":1},{"memberName":"sunquanling0219","avatar":null,"advertiseId":247,"transactions":80,"price":5.9,"minLimit":100,"maxLimit":1000,"remainAmount":395.52,"createTime":"2018-08-27 07:39:34","payMode":"支付宝,微信","coinId":2,"unit":"USDT","coinName":"USDT","coinNameCn":"泰达币","level":2,"advertiseType":0,"advType":0,"premiseRate":null,"completeRate":94,"realVerified":1,"emailVerified":1,"phoneVerified":1}]},{"time":"2018:9:15  8:30","content":[{"memberName":"杨洋0208","avatar":"http://caymanex.oss-cn-hangzhou.aliyuncs.com/2018/06/07/3554b855-a6f1-437e-8bb1-7cd2fcb1e842.jpg","advertiseId":177,"transactions":1909,"price":5.9,"minLimit":100,"maxLimit":500,"remainAmount":738.69,"createTime":"2018-07-05 15:56:12","payMode":"支付宝,微信,银联","coinId":2,"unit":"USDT","coinName":"USDT","coinNameCn":"泰达币","level":2,"advertiseType":0,"advType":0,"premiseRate":null,"completeRate":98,"realVerified":1,"emailVerified":1,"phoneVerified":1},{"memberName":"旺仔哦","avatar":"http://caymanex.oss-cn-hangzhou.aliyuncs.com/2018/07/23/694b0807-70ec-4bd4-b1ea-14f8f8d06b4b.jpg","advertiseId":223,"transactions":97,"price":5.9,"minLimit":100,"maxLimit":500,"remainAmount":914,"createTime":"2018-07-20 16:01:08","payMode":"微信,支付宝,银联","coinId":2,"unit":"USDT","coinName":"USDT","coinNameCn":"泰达币","level":2,"advertiseType":0,"advType":0,"premiseRate":null,"completeRate":95,"realVerified":1,"emailVerified":1,"phoneVerified":1},{"memberName":"杨洋0208","avatar":"http://caymanex.oss-cn-hangzhou.aliyuncs.com/2018/06/07/3554b855-a6f1-437e-8bb1-7cd2fcb1e842.jpg","advertiseId":239,"transactions":1909,"price":5.81,"minLimit":100,"maxLimit":500,"remainAmount":888.09,"createTime":"2018-08-17 19:42:45","payMode":"支付宝,微信,银联","coinId":2,"unit":"USDT","coinName":"USDT","coinNameCn":"泰达币","level":2,"advertiseType":0,"advType":0,"premiseRate":null,"completeRate":98,"realVerified":1,"emailVerified":1,"phoneVerified":1}]}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<ItemsBean> items;

        public List<ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<ItemsBean> items) {
            this.items = items;
        }

        public static class ItemsBean {
            /**
             * time : 2018:9:14  10:30
             * content : [{"memberName":"Despacito","avatar":null,"advertiseId":152,"transactions":1637,"price":6.3,"minLimit":500,"maxLimit":1000,"remainAmount":670,"createTime":"2018-06-20 15:22:54","payMode":"支付宝","coinId":2,"unit":"USDT","coinName":"USDT","coinNameCn":"泰达币","level":2,"advertiseType":0,"advType":0,"premiseRate":null,"completeRate":72,"realVerified":1,"emailVerified":1,"phoneVerified":1},{"memberName":"星语星愿","avatar":"http://caymanex.oss-cn-hangzhou.aliyuncs.com/2018/06/12/deab3613-6de2-4a5c-8c7e-e18e1e0703ec.jpg","advertiseId":237,"transactions":167,"price":6.2,"minLimit":100,"maxLimit":1000,"remainAmount":244.63908681,"createTime":"2018-08-05 10:17:01","payMode":"支付宝","coinId":2,"unit":"USDT","coinName":"USDT","coinNameCn":"泰达币","level":2,"advertiseType":0,"advType":0,"premiseRate":null,"completeRate":73,"realVerified":1,"emailVerified":1,"phoneVerified":1},{"memberName":"sunquanling0219","avatar":null,"advertiseId":247,"transactions":80,"price":5.9,"minLimit":100,"maxLimit":1000,"remainAmount":395.52,"createTime":"2018-08-27 07:39:34","payMode":"支付宝,微信","coinId":2,"unit":"USDT","coinName":"USDT","coinNameCn":"泰达币","level":2,"advertiseType":0,"advType":0,"premiseRate":null,"completeRate":94,"realVerified":1,"emailVerified":1,"phoneVerified":1}]
             */

            private String time;
            private List<ContentBean> content;

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public List<ContentBean> getContent() {
                return content;
            }

            public void setContent(List<ContentBean> content) {
                this.content = content;
            }

            public static class ContentBean {
                /**
                 * memberName : Despacito
                 * avatar : null
                 * advertiseId : 152
                 * transactions : 1637
                 * price : 6.3
                 * minLimit : 500
                 * maxLimit : 1000
                 * remainAmount : 670
                 * createTime : 2018-06-20 15:22:54
                 * payMode : 支付宝
                 * coinId : 2
                 * unit : USDT
                 * coinName : USDT
                 * coinNameCn : 泰达币
                 * level : 2
                 * advertiseType : 0
                 * advType : 0
                 * premiseRate : null
                 * completeRate : 72
                 * realVerified : 1
                 * emailVerified : 1
                 * phoneVerified : 1
                 */

                private String memberName;
                private Object avatar;
                private int advertiseId;
                private int transactions;
                private double price;
                private int minLimit;
                private int maxLimit;
                private int remainAmount;
                private String createTime;
                private String payMode;
                private int coinId;
                private String unit;
                private String coinName;
                private String coinNameCn;
                private int level;
                private int advertiseType;
                private int advType;
                private Object premiseRate;
                private int completeRate;
                private int realVerified;
                private int emailVerified;
                private int phoneVerified;

                public String getMemberName() {
                    return memberName;
                }

                public void setMemberName(String memberName) {
                    this.memberName = memberName;
                }

                public Object getAvatar() {
                    return avatar;
                }

                public void setAvatar(Object avatar) {
                    this.avatar = avatar;
                }

                public int getAdvertiseId() {
                    return advertiseId;
                }

                public void setAdvertiseId(int advertiseId) {
                    this.advertiseId = advertiseId;
                }

                public int getTransactions() {
                    return transactions;
                }

                public void setTransactions(int transactions) {
                    this.transactions = transactions;
                }

                public double getPrice() {
                    return price;
                }

                public void setPrice(double price) {
                    this.price = price;
                }

                public int getMinLimit() {
                    return minLimit;
                }

                public void setMinLimit(int minLimit) {
                    this.minLimit = minLimit;
                }

                public int getMaxLimit() {
                    return maxLimit;
                }

                public void setMaxLimit(int maxLimit) {
                    this.maxLimit = maxLimit;
                }

                public int getRemainAmount() {
                    return remainAmount;
                }

                public void setRemainAmount(int remainAmount) {
                    this.remainAmount = remainAmount;
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

                public void setPayMode(String payMode) {
                    this.payMode = payMode;
                }

                public int getCoinId() {
                    return coinId;
                }

                public void setCoinId(int coinId) {
                    this.coinId = coinId;
                }

                public String getUnit() {
                    return unit;
                }

                public void setUnit(String unit) {
                    this.unit = unit;
                }

                public String getCoinName() {
                    return coinName;
                }

                public void setCoinName(String coinName) {
                    this.coinName = coinName;
                }

                public String getCoinNameCn() {
                    return coinNameCn;
                }

                public void setCoinNameCn(String coinNameCn) {
                    this.coinNameCn = coinNameCn;
                }

                public int getLevel() {
                    return level;
                }

                public void setLevel(int level) {
                    this.level = level;
                }

                public int getAdvertiseType() {
                    return advertiseType;
                }

                public void setAdvertiseType(int advertiseType) {
                    this.advertiseType = advertiseType;
                }

                public int getAdvType() {
                    return advType;
                }

                public void setAdvType(int advType) {
                    this.advType = advType;
                }

                public Object getPremiseRate() {
                    return premiseRate;
                }

                public void setPremiseRate(Object premiseRate) {
                    this.premiseRate = premiseRate;
                }

                public int getCompleteRate() {
                    return completeRate;
                }

                public void setCompleteRate(int completeRate) {
                    this.completeRate = completeRate;
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
            }
        }
    }
}
