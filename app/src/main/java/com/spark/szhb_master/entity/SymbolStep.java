package com.spark.szhb_master.entity;

import java.util.List;

public class SymbolStep {

    private List<List<Double>> bids;
    private List<List<Double>> asks;

    public List<List<Double>> getBids() {
        return bids;
    }

    public void setBids(List<List<Double>> bids) {
        this.bids = bids;
    }

    public List<List<Double>> getAsks() {
        return asks;
    }

    public void setAsks(List<List<Double>> asks) {
        this.asks = asks;
    }
}
