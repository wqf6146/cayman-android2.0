package com.spark.szhb_master.entity;

/**
 * Created by Administrator on 2018/9/18 0018.
 */
public class MessageEvent {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageEvent(String message) {
        this.message = message;
    }

    public MessageEvent() {

    }
}
