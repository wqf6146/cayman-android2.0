package com.spark.szhb_master.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/6/4 0004.
 */

public class DepthChart {
    /**
     * ask : {"minAmount":10,"highestPrice":1,"symbol":"GCX/USDT","lowestPrice":0.08,"maxAmount":955,"items":[{"price":0.08,"amount":955,"priceStr":"0.08","amountStr":"955.0000"},{"price":0.09,"amount":130,"priceStr":"0.09","amountStr":"130.0000"},{"price":0.1,"amount":150,"priceStr":"0.10","amountStr":"150.0000"},{"price":0.12,"amount":10,"priceStr":"0.12","amountStr":"10.0000"},{"price":0.15,"amount":80,"priceStr":"0.15","amountStr":"80.0000"},{"price":0.5,"amount":50,"priceStr":"0.50","amountStr":"50.0000"},{"price":1,"amount":50,"priceStr":"1.00","amountStr":"50.0000"}],"direction":"SELL"}
     * bid : {"minAmount":861,"highestPrice":0.07,"symbol":"GCX/USDT","lowestPrice":0.06,"maxAmount":891.05,"items":[{"price":0.07,"amount":891.05,"priceStr":"0.07","amountStr":"891.0500"},{"price":0.06,"amount":861,"priceStr":"0.06","amountStr":"861.0000"}],"direction":"BUY"}
     */

    private AskBean ask;
    private BidBean bid;

    public AskBean getAsk() {
        return ask;
    }

    public void setAsk(AskBean ask) {
        this.ask = ask;
    }

    public BidBean getBid() {
        return bid;
    }

    public void setBid(BidBean bid) {
        this.bid = bid;
    }

    public static class AskBean {
        /**
         * minAmount : 10.0
         * highestPrice : 1.0
         * symbol : GCX/USDT
         * lowestPrice : 0.08
         * maxAmount : 955.0
         * items : [{"price":0.08,"amount":955,"priceStr":"0.08","amountStr":"955.0000"},{"price":0.09,"amount":130,"priceStr":"0.09","amountStr":"130.0000"},{"price":0.1,"amount":150,"priceStr":"0.10","amountStr":"150.0000"},{"price":0.12,"amount":10,"priceStr":"0.12","amountStr":"10.0000"},{"price":0.15,"amount":80,"priceStr":"0.15","amountStr":"80.0000"},{"price":0.5,"amount":50,"priceStr":"0.50","amountStr":"50.0000"},{"price":1,"amount":50,"priceStr":"1.00","amountStr":"50.0000"}]
         * direction : SELL
         */

        private double minAmount;
        private double highestPrice;
        private String symbol;
        private double lowestPrice;
        private double maxAmount;
        private String direction;
        private List<ItemsBean> items;

        public double getMinAmount() {
            return minAmount;
        }

        public void setMinAmount(double minAmount) {
            this.minAmount = minAmount;
        }

        public double getHighestPrice() {
            return highestPrice;
        }

        public void setHighestPrice(double highestPrice) {
            this.highestPrice = highestPrice;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public double getLowestPrice() {
            return lowestPrice;
        }

        public void setLowestPrice(double lowestPrice) {
            this.lowestPrice = lowestPrice;
        }

        public double getMaxAmount() {
            return maxAmount;
        }

        public void setMaxAmount(double maxAmount) {
            this.maxAmount = maxAmount;
        }

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        public List<ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<ItemsBean> items) {
            this.items = items;
        }

        public static class ItemsBean {
            /**
             * price : 0.08
             * amount : 955.0
             * priceStr : 0.08
             * amountStr : 955.0000
             */

            private double price;
            private double amount;
            private String priceStr;
            private String amountStr;

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public double getAmount() {
                return amount;
            }

            public void setAmount(double amount) {
                this.amount = amount;
            }

            public String getPriceStr() {
                return priceStr;
            }

            public void setPriceStr(String priceStr) {
                this.priceStr = priceStr;
            }

            public String getAmountStr() {
                return amountStr;
            }

            public void setAmountStr(String amountStr) {
                this.amountStr = amountStr;
            }
        }
    }

    public static class BidBean {
        /**
         * minAmount : 861.0
         * highestPrice : 0.07
         * symbol : GCX/USDT
         * lowestPrice : 0.06
         * maxAmount : 891.05
         * items : [{"price":0.07,"amount":891.05,"priceStr":"0.07","amountStr":"891.0500"},{"price":0.06,"amount":861,"priceStr":"0.06","amountStr":"861.0000"}]
         * direction : BUY
         */

        private double minAmount;
        private double highestPrice;
        private String symbol;
        private double lowestPrice;
        private double maxAmount;
        private String direction;
        private List<ItemsBeanX> items;

        public double getMinAmount() {
            return minAmount;
        }

        public void setMinAmount(double minAmount) {
            this.minAmount = minAmount;
        }

        public double getHighestPrice() {
            return highestPrice;
        }

        public void setHighestPrice(double highestPrice) {
            this.highestPrice = highestPrice;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public double getLowestPrice() {
            return lowestPrice;
        }

        public void setLowestPrice(double lowestPrice) {
            this.lowestPrice = lowestPrice;
        }

        public double getMaxAmount() {
            return maxAmount;
        }

        public void setMaxAmount(double maxAmount) {
            this.maxAmount = maxAmount;
        }

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        public List<ItemsBeanX> getItems() {
            return items;
        }

        public void setItems(List<ItemsBeanX> items) {
            this.items = items;
        }

        public static class ItemsBeanX {
            /**
             * price : 0.07
             * amount : 891.05
             * priceStr : 0.07
             * amountStr : 891.0500
             */

            private double price;
            private double amount;
            private String priceStr;
            private String amountStr;

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public double getAmount() {
                return amount;
            }

            public void setAmount(double amount) {
                this.amount = amount;
            }

            public String getPriceStr() {
                return priceStr;
            }

            public void setPriceStr(String priceStr) {
                this.priceStr = priceStr;
            }

            public String getAmountStr() {
                return amountStr;
            }

            public void setAmountStr(String amountStr) {
                this.amountStr = amountStr;
            }
        }
    }
}
