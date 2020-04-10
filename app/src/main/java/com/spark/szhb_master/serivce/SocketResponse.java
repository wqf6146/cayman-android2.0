package com.spark.szhb_master.serivce;

import com.spark.szhb_master.factory.socket.ISocket;
import com.spark.szhb_master.factory.socket.NEWCMD;

/**
 * author: wuzongjie
 * time  : 2018/5/4 0004 09:35
 * desc  :
 */

public class SocketResponse {

    private NEWCMD cmd; // 传的指令
    private String response; // 返回的参数
    private String remark;

    public SocketResponse(NEWCMD cmd, String response) {
        this.cmd = cmd;
        this.response = response;
    }

    public NEWCMD getCmd() {
        return cmd;
    }

    public void setCmd(NEWCMD cmd) {
        this.cmd = cmd;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public SocketResponse setRemark(String remark) {
        if (remark != null)
            this.remark = remark;
        return this;
    }

    public String getRemark() {
        return remark;
    }
}
