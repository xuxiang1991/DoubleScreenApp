package com.sunmi.doublescreen.doublescreenapp.network.config;

/**
 * Created by hua on 2017/1/10.
 */

public class DomainUrl {

    public static final String BASE_URL = "http://47.100.113.233/";//"http://114.55.5.20:8080"


    /**
     * 获取产品列表
     */
    public static final String Product_List = BASE_URL + "tea/admin/products";//线上地址


    /**
     * 生成订单
     * <p>
     * <p>
     * http://47.100.113.233/tea/admin/order
     */
    public static final String Create_Order = BASE_URL + "tea/admin/order";
    /**
     * 完成订单
     * <p>
     * <p>
     * http://47.100.113.233/tea/admin/complete
     */
    public static final String Complete_Order = BASE_URL + "tea/admin/complete";
    /**
     * 获取随意产品
     * <p>
     * <p>
     * http://47.100.113.233/tea/admin/choose
     */
    public static final String Choose_last_product = BASE_URL + "tea/admin/choose";
}
