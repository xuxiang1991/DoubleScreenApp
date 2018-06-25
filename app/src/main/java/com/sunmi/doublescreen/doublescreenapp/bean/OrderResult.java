package com.sunmi.doublescreen.doublescreenapp.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 类名称：订单新增返回
 * 类描述：
 * 创建人：
 * 修改人：
 */
public class OrderResult implements Serializable {


    /**
     * status : success
     * messages : []
     * data : {"orderNo":"20151207112323296104","orderCreateDateTime":"2015-12-04 10:05:01","customerNumber":"001"}
     */

    private String status;
    private DataBean data;
    private List<String> messages;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public List<?> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public static class DataBean {
        /**
         * orderNo : 20151207112323296104
         * orderCreateDateTime : 2015-12-04 10:05:01
         * customerNumber : 001
         */

        private String orderNo;
        private String orderCreateDateTime;
        private String customerNumber;

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getOrderCreateDateTime() {
            return orderCreateDateTime;
        }

        public void setOrderCreateDateTime(String orderCreateDateTime) {
            this.orderCreateDateTime = orderCreateDateTime;
        }

        public String getCustomerNumber() {
            return customerNumber;
        }

        public void setCustomerNumber(String customerNumber) {
            this.customerNumber = customerNumber;
        }
    }
}
