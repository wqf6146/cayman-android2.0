package com.spark.szhb_master.entity;

import com.spark.szhb_master.factory.socket.ISocket;

/**
 * Created by Administrator on 2018/4/20 0020.
 */

public class ChatEvent {
    private String resonpce;
    private String type;
    private ISocket.CMD cmd;

    public ISocket.CMD getCmd() {
        return cmd;
    }

    public void setCmd(ISocket.CMD cmd) {
        this.cmd = cmd;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResonpce() {
        return resonpce;
    }

    public void setResonpce(String resonpce) {
        this.resonpce = resonpce;
    }
}