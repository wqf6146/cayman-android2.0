package com.spark.szhb_master.utils;

/**
 * Created by Administrator on 2017/8/30.
 */

public class WonderfulStringUtils {
    /**
     * 判断文本是否为空为null
     */
    public static boolean isEmpty(String... strs) {
        for (String str : strs) {
            if (str == null || "".equals(str) || "null".equals(str.toLowerCase())) return true;
        }
        return false;
    }

}
