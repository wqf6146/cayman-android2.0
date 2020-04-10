package com.spark.szhb_master.entity;

/**
 * Created by Administrator on 2018/4/12.
 */

public class ChatEntity {

    private String content;
    private String messageType;
    private String nameFrom;
    private String nameTo;
    private String orderId;
    private long sendTime;
    private String sendTimeStr;
    private String uidFrom;
    private String uidTo;
    private Type type;
    private String fromAvatar;


    public String getFromAvatar() {
        return fromAvatar;
    }

    public String getContent() {
        return content;
    }

    public String getNameFrom() {
        return nameFrom;
    }

    public String getNameTo() {
        return nameTo;
    }

    public String getOrderId() {
        return orderId;
    }

    public long getSendTime() {
        return sendTime;
    }

    public String getSendTimeStr() {
        return sendTimeStr;
    }

    public String getUidFrom() {
        return uidFrom;
    }

    public String getUidTo() {
        return uidTo;
    }

    public void setType(Type type) {
        this.type = type;
    }

   public enum Type {
        LEFT, RIGHT;
    }
}
