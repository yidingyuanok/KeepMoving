package com.yuenan.shame.util;

import java.math.BigDecimal;

/**
 * 小数点格式化
 * Created by liuhuacheng
 * Created on 18/6/5
 */

public class DecimalUtil {

    public static String format(long value) {
        if (value == 0) {
            return "0.00";
        }
        float f = (float) (value * 0.01);//转换成元
        BigDecimal num = new BigDecimal(f).setScale(2, BigDecimal.ROUND_HALF_UP);
        String result = num.toString();
        return result;
    }

}
