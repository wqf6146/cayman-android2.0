package com.spark.szhb_master.entity;

public class VolumeBean {

    /**
     * amount : 292
     * ts : 1585667670361
     * id : 562522896740000
     * price : 6484.42
     * direction : sell
     */

    private int amount;
    private long ts;
    private long id;
    private double price;
    private String direction;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
