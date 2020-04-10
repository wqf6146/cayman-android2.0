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

    private String Open;
    private String Close;
    private String Low;
    private String High;
    private String Amount;
    private String Count;
    private String Vol;
    private String Symbol;
    private String Type;
    private String Scale;
    private String Convert;

    private Double baseUsdRate = 1.0;


    public Double getBaseUsdRate() {
        return baseUsdRate == 0 ? 1 : baseUsdRate;
    }

    public String getOpen() {
        return Open;
    }

    public void setOpen(String Open) {
        this.Open = Open;
    }

    public String getClose() {
        return Close;
    }

    public void setClose(String Close) {
        this.Close = Close;
    }

    public String getLow() {
        return Low;
    }

    public void setLow(String Low) {
        this.Low = Low;
    }

    public String getHigh() {
        return High;
    }

    public void setHigh(String High) {
        this.High = High;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String Amount) {
        this.Amount = Amount;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String Count) {
        this.Count = Count;
    }

    public String getVol() {
        return Vol;
    }

    public void setVol(String Vol) {
        this.Vol = Vol;
    }

    public String getSymbol() {
        return Symbol;
    }

    public void setSymbol(String Symbol) {
        this.Symbol = Symbol;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }



    public String getScale() {
        return Scale;
    }

    public void setScale(String Scale) {
        this.Scale = Scale;
    }

    public String getConvert() {
        return Convert;
    }

    public void setConvert(String Convert) {
        this.Convert = Convert;
    }

    public static NewCurrency shallowClone(NewCurrency origin, NewCurrency target) {
        origin.Symbol = target.Symbol;
        origin.Open = target.Open;
        origin.Close = target.Close;
        origin.Low = target.Low;
        origin.High = target.High;
        origin.Amount = target.Amount;
        origin.Count = target.Count;
        origin.Vol = target.Vol;
        origin.Type = target.Type;
        origin.Scale = target.Scale;
        origin.Convert = target.Convert;
        return origin;
    }

    public boolean equals(NewCurrency newCurrency) {
        return newCurrency.getSymbol().equals(this.getSymbol()) && newCurrency.getType().equals(this.getType());
    }
}
