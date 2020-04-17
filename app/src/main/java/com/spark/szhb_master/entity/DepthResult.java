package com.spark.szhb_master.entity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/6/4 0004.
 */

public class DepthResult {
    private DepthList ask;
    private DepthList bid;

    public DepthList getAsk() {
        return ask;
    }

    public DepthList getBid() {
        return bid;
    }

    public class DepthList {
        private double minAmount;
        private double highestPrice;
        private double lowestPrice;
        private double maxAmount;
        private String direction;
        private String symbol;
        private ArrayList<DepthListInfo> items;

        public String getSymbol() {
            return symbol;
        }

        public double getMinAmount() {
            return minAmount;
        }

        public double getHighestPrice() {
            return highestPrice;
        }

        public double getLowestPrice() {
            return lowestPrice;
        }

        public double getMaxAmount() {
            return maxAmount;
        }

        public ArrayList<DepthListInfo> getItems() {
            return items;
        }

        public String getDirection() {
            return direction;
        }
    }




}
