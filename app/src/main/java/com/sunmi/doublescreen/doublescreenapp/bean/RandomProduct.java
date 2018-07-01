package com.sunmi.doublescreen.doublescreenapp.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 类名称：随机产品
 * 类描述：
 * 创建人：xuxiang
 * 修改人：
 */
public class RandomProduct implements Serializable {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * sign : 87
         * barcode : 1806261529101
         * name : 珍珠奶茶
         * qrurl : http://www.5dsq.com/tea/admin/detail?id=10
         */

        private String sign;
        private String barcode;
        private String name;
        private String qrurl;
        private int hotType;


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

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getQrurl() {
            return qrurl;
        }

        public void setQrurl(String qrurl) {
            this.qrurl = qrurl;
        }
    }
}
