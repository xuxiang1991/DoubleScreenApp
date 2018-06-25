package com.sunmi.doublescreen.doublescreenapp.bean;

import java.io.Serializable;

/**
 * 类名称：随机产品
 * 类描述：
 * 创建人：xuxiang
 * 修改人：
 */
public class RandomProduct implements Serializable {


    /**
     * sign : 07
     * qrurl :
     * barcode : 1806231415458
     */

    private String sign;
    private String qrurl;
    private String barcode;
    private String uid;
    private int hotType;//0 热 1 冷 2 常温


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getHotType() {
        return hotType;
    }

    public void setHotType(int hotType) {
        this.hotType = hotType;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getQrurl() {
        return qrurl;
    }

    public void setQrurl(String qrurl) {
        this.qrurl = qrurl;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
