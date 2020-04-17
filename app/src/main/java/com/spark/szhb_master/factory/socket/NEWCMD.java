package com.spark.szhb_master.factory.socket;

public enum NEWCMD {


    SUBSCRIBE_SYMBOL_DETAIL("detail"), //此接口为获取实时成交订单推送
    SUBSCRIBE_SYMBOL_TRADEDETAIL("trade.detail"), //此接口为获取实时成交订单推送
    SUBSCRIBE_SYMBOL_DEPTH("depth.step"), //此接口为获取交易对推送
    SUBSCRIBE_SYMBOL_KLIST("klist"),//此接口为获取k线数据

    //获取侧边币种信息推送
    SUBSCRIBE_SIDE_TRADE("market.overview"),

    SUBSCRIBE_HOME_TRADE("market.overview3");

    private String typeName;
    NEWCMD(String typeName) {
        this.typeName = typeName;
    }
    /**
     * 根据类型的名称，返回类型的枚举实例。
     *
     * @param typeName 类型名称
     */
    public static NEWCMD fromTypeName(String typeName) {
        for (NEWCMD type : NEWCMD.values()) {
            if (type.getTypeName().equals(typeName)) {
                return type;
            }
        }
        return null;
    }

    public String getTypeName() {
        return this.typeName;
    }
}
