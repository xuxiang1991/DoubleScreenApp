package com.sunmi.doublescreen.doublescreenapp.utils;

import java.math.BigDecimal;

/**
 * 类名称：
 * 类描述：
 * 创建人：
 * 修改人：
 */
public class DecimalMath {


    /**
     * 加
     *
     * @param mun1
     * @param mun2
     * @return
     */
    public static String add(String mun1, String mun2) {
        BigDecimal b1 = new BigDecimal(mun1);
        BigDecimal b2 = new BigDecimal(mun2);
        return b1.add(b2).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 乘
     *
     * @param mun1
     * @param mun2
     * @return
     */
    public static String plus(String mun1, String mun2) {
        BigDecimal b1 = new BigDecimal(mun1);
        BigDecimal b2 = new BigDecimal(mun2);
        return b1.multiply(b2).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }


    /**
     * 乘
     *
     * @param mun1
     * @param mun2
     * @return
     */
    public static String plus(int mun1, String mun2) {
        BigDecimal b1 = new BigDecimal(mun1);
        BigDecimal b2 = new BigDecimal(mun2);
        return b1.multiply(b2).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }


    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */

    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

}
