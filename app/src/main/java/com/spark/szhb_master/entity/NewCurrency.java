package com.spark.szhb_master.entity;

import java.io.Serializable;

public class NewCurrency  implements Serializable {
    /**
     * Open : 6785.9
     * Close : 7051.02
     * Low : 6671.76
     * High : 7117.22
     * Amount : 63908.8607163508424923130459174124417128914
     * Count : 108750
     * Vol : 4395230
     * Symbol : BTC
     * Type : 60
     * Scale : 3.91
     * Convert : 7051.02
     */

    private String open;
    private String close;
    private String low;
    private String high;
    private String amount;
    private String count;
    private String vol;
    private String symbol;
    private String type;
    private String scale;
    private String convert;
    private int id; //时间戳

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    private Double baseUsdRate = 1.0;


    public Double getBaseUsdRate() {
        return baseUsdRate == 0 ? 1 : baseUsdRate;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String Open) {
        this.open = Open;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String Close) {
        this.close = Close;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String Low) {
        this.low = Low;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String High) {
        this.high = High;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String Amount) {
        this.amount = Amount;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String Count) {
        this.count = Count;
    }

    public String getVol() {
        return vol;
    }

    public void setVol(String Vol) {
        this.vol = Vol;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String Symbol) {
        this.symbol = Symbol;
    }

    public String getType() {
        return type;
    }

    public void setType(String Type) {
        this.type = Type;
    }



    public String getScale() {
        return scale;
    }

    public void setScale(String Scale) {
        this.scale = Scale;
    }

    public String getConvert() {
        return convert;
    }

    public void setConvert(String Convert) {
        this.convert = Convert;
    }

    public static NewCurrency shallowClone(NewCurrency origin, NewCurrency target) {
        origin.symbol = target.symbol;
        origin.open = target.open;
        origin.close = target.close;
        origin.low = target.low;
        origin.high = target.high;
        origin.amount = target.amount;
        origin.count = target.count;
        origin.vol = target.vol;
        origin.type = target.type;
        origin.scale = target.scale;
        origin.convert = target.convert;
        return origin;
    }

    public boolean equals(NewCurrency newCurrency) {
        return newCurrency.getSymbol().equals(this.getSymbol()) && newCurrency.getType().equals(this.getType());
    }
}
