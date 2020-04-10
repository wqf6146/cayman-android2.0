package com.spark.szhb_master.factory.socket;

/**
 * Created by Administrator on 2018/4/12.
 */

public interface ISocket extends Runnable {
    // TODO: 2018/4/18 0018  30s一次心跳发送   11004
    int MARKET = 1;
    int C2C = 2;
    int GETCHAT = 3;
    int GROUP = 3;
    int HEART = 4;

    void sendRequest(CMD cmd, byte[] body, TCPCallback callback);

    interface TCPCallback {
        void dataSuccess(CMD cmd, String response);

        void dataFail(int code, CMD cmd, String errorInfo);

    }

    enum CMD {
        COMMANDS_VERSION((short) 1),
        HEART_BEAT((short) 11004),
        SUBSCRIBE_SYMBOL_THUMB((short) 20001), UNSUBSCRIBE_SYMBOL_THUMB((short) 20002),
        PUSH_SYMBOL_THUMB((short) 20003),
        SUBSCRIBE_SYMBOL_KLINE((short) 20011), UNSUBSCRIBE_SYMBOL_KLINE((short) 20012),
        PUSH_SYMBOL_KLINE((short) 20013),

        //SUBSCRIBE_HOME_TRADE((String) "s"),//获取侧边币种信息推送
        SUBSCRIBE_SIDE_TRADE((short) 20020),//获取侧边币种信息推送

        SUBSCRIBE_EXCHANGE_TRADE((short) 20021), // 盘口信息
        UNSUBSCRIBE_EXCHANGE_TRADE((short) 20022), // 取消盘口信息
        PUSH_EXCHANGE_TRADE((short) 20023), // 成交记录(用于深度图)
        PUSH_EXCHANGE_PLATE((short) 20024), // 盘口返回
        SUBSCRIBE_CHAT((short) 20031), UNSUBSCRIBE_CHAT((short) 20032),
        PUSH_CHAT((short) 20033),
        SEND_CHAT((short) 20034),
        PUSH_EXCHANGE_ORDER_COMPLETED((short) 20026),
        PUSH_EXCHANGE_ORDER_CANCELED((short) 20027),
        PUSH_EXCHANGE_ORDER_TRADE((short) 20028),
        SUBSCRIBE_GROUP_CHAT((short) 20035), UNSUBSCRIBE_GROUP_CHAT((short) 20036),
        PUSH_GROUP_CHAT((short) 20039), PUSH_EXCHANGE_DEPTH((short) 20029);

        private short code;

        CMD(short code) {
            this.code = code;
        }

        public short getCode() {
            return code;
        }

        public static CMD findObjByCode(short code) {
            for (CMD cmd : CMD.values()) {
                if (cmd.getCode() == code) return cmd;
            }
            return null;
        }

    }

}
