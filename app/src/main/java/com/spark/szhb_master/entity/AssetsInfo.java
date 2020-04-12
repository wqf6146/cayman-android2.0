package com.spark.szhb_master.entity;

public class AssetsInfo {

    /**
     * frost : 0
     * frost_convert : 0
     * sum : 100
     * sum_convert : 100
     * usable : 100
     * usable_convert : 100
     */

    private float frost; //冻结(全合约账户)
    private float frost_convert;
    private float sum;  //总额（总资产合计）
    private float sum_convert;
    private float usable; //余额(资金账户)
    private float usable_convert;

    public float getFrost() {
        return frost;
    }

    public void setFrost(float frost) {
        this.frost = frost;
    }

    public float getFrost_convert() {
        return frost_convert;
    }

    public void setFrost_convert(float frost_convert) {
        this.frost_convert = frost_convert;
    }

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }

    public float getSum_convert() {
        return sum_convert;
    }

    public void setSum_convert(float sum_convert) {
        this.sum_convert = sum_convert;
    }

    public float getUsable() {
        return usable;
    }

    public void setUsable(float usable) {
        this.usable = usable;
    }

    public float getUsable_convert() {
        return usable_convert;
    }

    public void setUsable_convert(float usable_convert) {
        this.usable_convert = usable_convert;
    }
}
