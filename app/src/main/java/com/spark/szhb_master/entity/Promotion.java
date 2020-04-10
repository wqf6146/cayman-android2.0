package com.spark.szhb_master.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/9/3 0003.
 */

public class Promotion {


    /**
     * data : {"reward":{"GalaxyChain":4},"number":1,"size":10,"content":[{"symbol":"GCC","remark":"推广注册","amount":1,"createTime":"2018-05-09 14:20:28"},{"symbol":"GCC","remark":"推广注册","amount":1,"createTime":"2018-03-28 14:47:50"},{"symbol":"GCC","remark":"推广注册","amount":1,"createTime":"2018-03-28 09:58:40"},{"symbol":"GCC","remark":"推广注册","amount":1,"createTime":"2018-03-28 09:40:55"}],"totalElements":4}
     * code : 0
     * message : SUCCESS
     */

    private DataBean data;
    private int code;
    private String message;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        /**
         * reward : {"GalaxyChain":4}
         * number : 1
         * size : 10
         * content : [{"symbol":"GCC","remark":"推广注册","amount":1,"createTime":"2018-05-09 14:20:28"},{"symbol":"GCC","remark":"推广注册","amount":1,"createTime":"2018-03-28 14:47:50"},{"symbol":"GCC","remark":"推广注册","amount":1,"createTime":"2018-03-28 09:58:40"},{"symbol":"GCC","remark":"推广注册","amount":1,"createTime":"2018-03-28 09:40:55"}]
         * totalElements : 4
         */

        private RewardBean reward;
        private int number;
        private int size;
        private int totalElements;
        private List<ContentBean> content;

        public RewardBean getReward() {
            return reward;
        }

        public void setReward(RewardBean reward) {
            this.reward = reward;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getTotalElements() {
            return totalElements;
        }

        public void setTotalElements(int totalElements) {
            this.totalElements = totalElements;
        }

        public List<ContentBean> getContent() {
            return content;
        }

        public void setContent(List<ContentBean> content) {
            this.content = content;
        }

        public static class RewardBean {
            /**
             * GalaxyChain : 4.0
             */

            private double GalaxyChain;

            public double getGalaxyChain() {
                return GalaxyChain;
            }

            public void setGalaxyChain(double GalaxyChain) {
                this.GalaxyChain = GalaxyChain;
            }
        }

        public static class ContentBean {
            /**
             * symbol : GCC
             * remark : 推广注册
             * amount : 1.0
             * createTime : 2018-05-09 14:20:28
             */

            private String symbol;
            private String remark;
            private double amount;
            private String createTime;

            public String getSymbol() {
                return symbol;
            }

            public void setSymbol(String symbol) {
                this.symbol = symbol;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
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
        }
    }
}
