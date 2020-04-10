package com.spark.szhb_master.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by Administrator on 2017/8/30.
 */

public class MathUtils {
    public static String getRundNumber(double number, int n, String pattern) {
        if (StringUtils.isEmpty(pattern)) pattern = "########0.";
        String str = "";
        for (int j = 0; j < n; j++) str += "0";
        pattern += str;
        int m = (int) Math.pow(10, n);
        number = (Math.round(number * m)) / (m * 1.0);
        return new DecimalFormat(pattern).format(number);
    }

    public static double getDoubleTransferString(String strDouble) {
        double transferNumber = 0.0;
        if (StringUtils.isNotEmpty(strDouble)) {
            BigDecimal bigDecimal = new BigDecimal(strDouble);
            transferNumber = bigDecimal.doubleValue();
        }
        return transferNumber;
    }

    public static double mul(double num1, double num2) {
        BigDecimal b1 = new BigDecimal(num1);
        BigDecimal b2 = new BigDecimal(num2);
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 当浮点型数据位数超过10位之后，数据变成科学计数法显示。用此方法可以使其正常显示。
     *
     * @return d
     */
    public static float transform(double d) {
        BigDecimal bigDecimal = new BigDecimal(d);//不以科学计数法显示，正常显示保留两位小数
        float f = Float.parseFloat(bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        LogUtils.i("ff===" + f + ",,,ss==" + getRundNumber(d, 5, null));
        return f;
    }
}
