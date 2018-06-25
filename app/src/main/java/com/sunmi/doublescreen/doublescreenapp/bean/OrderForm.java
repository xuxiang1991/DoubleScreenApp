package com.sunmi.doublescreen.doublescreenapp.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 类名称：订单新增
 * 类描述：
 * 创建人：
 * 修改人：
 */
public class OrderForm implements Serializable{


    /**
     * appId : abcdefghijklmn
     * payMethod : Cash
     * customerNumber : 001
     * shippingFee : 15.0
     * orderRemark : addOnLineOrder
     * orderDateTime : 2015-12-04 10:05:01
     * contactAddress : 测试测试。。。。
     * contactName : 张三
     * contactTel : 1360097865
     * items : [{"productUid":102066793346170331,"comment":"测试添加","quantity":1.2,"manualSellPrice":30.2}]
     */

    private String appId;
    private String payMethod;
    private String customerNumber;
    private double shippingFee;
    private String orderRemark;
    private String orderDateTime;
    private String contactAddress;
    private String contactName;
    private String contactTel;
    private List<ItemsBean> items;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }

    public String getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(String orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean implements Serializable{
        /**
         * productUid : 102066793346170331
         * comment : 测试添加
         * quantity : 1.2
         * manualSellPrice : 30.2
         */

        private String productUid;
        private String comment;
        private String quantity;
        private String manualSellPrice;

        public String getProductUid() {
            return productUid;
        }

        public void setProductUid(String productUid) {
            this.productUid = productUid;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getManualSellPrice() {
            return manualSellPrice;
        }

        public void setManualSellPrice(String manualSellPrice) {
            this.manualSellPrice = manualSellPrice;
        }
    }
}
