package com.spark.szhb_master.entity;

/**
 * Created by Administrator on 2018/3/21.
 */

public class BannerEntity {
    private String serialNumber;
    private String name;
    private int sysAdvertiseLocation;
    private String startTime;
    private String endTime;
    private String url;
    private String linkUrl;
    private String remark;
    private int status;
    private String createTime;

    public String getLinkUrl() {
        return linkUrl;
    }


    public String getName() {
        return name;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
