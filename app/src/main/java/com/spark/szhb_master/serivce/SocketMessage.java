package com.spark.szhb_master.serivce;

import com.spark.szhb_master.factory.socket.ISocket;
import com.spark.szhb_master.factory.socket.NEWCMD;

/**
 * author: wuzongjie
 * time  : 2018/4/27 0027 11:13
 * desc  :
 */

public class SocketMessage {

    private int code; // 0 为行情的socket
    private NEWCMD cmd; // 传的指令
    private String body; // 参数


    public SocketMessage(NEWCMD cmd, String response) {
        this.cmd = cmd;

    }

    public SocketMessage(int code, NEWCMD cmd, String body) {
        this.code = code;
        this.cmd = cmd;
        this.body = body;
    }



    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public NEWCMD getCmd() {
        return cmd;
    }

    public void setCmd(NEWCMD cmd) {
        this.cmd = cmd;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
