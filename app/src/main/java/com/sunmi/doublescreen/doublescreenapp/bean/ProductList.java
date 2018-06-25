package com.sunmi.doublescreen.doublescreenapp.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 类名称：产品列表
 * 类描述：
 * 创建人：xuxiang
 * 修改人：
 */
public class ProductList implements Serializable {


    private List<CategorysBean> categorys;
    private List<ProductsBean> products;

    public List<CategorysBean> getCategorys() {
        return categorys;
    }

    public void setCategorys(List<CategorysBean> categorys) {
        this.categorys = categorys;
    }

    public List<ProductsBean> getProducts() {
        return products;
    }

    public void setProducts(List<ProductsBean> products) {
        this.products = products;
    }

    public static class CategorysBean implements Serializable {
        /**
         * uid : 1529566310401559600
         * parentUid : 0
         * name : 主打+热
         */

        private String uid;
        private int parentUid;
        private String name;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public int getParentUid() {
            return parentUid;
        }

        public void setParentUid(int parentUid) {
            this.parentUid = parentUid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class ProductsBean implements Serializable {
        /**
         * uid : 596836068685260026
         * sellPrice : 20.00
         * categoryUid : 1529723223995346110
         * isCustomerDiscount : 1
         * barcode : 1806211602441
         * attribute4 : null
         * stock : 0
         * description :
         * name : 一杯奶绿
         * customerPrice : 20.00
         * buyPrice : 10.00
         * attribute1 : n
         * attribute2 :
         * pinyin : ybnl
         * attribute3 :
         */

        private String uid;
        private String sellPrice;
        private String categoryUid;
        private String isCustomerDiscount;
        private String barcode;
        private Object attribute4;
        private int stock;
        private String description;
        private String name;
        private String customerPrice;
        private String buyPrice;
        private String attribute1;
        private String attribute2;
        private String pinyin;
        private String attribute3;
        private int Count;
        private int hotType;
        private int boxType;
        private String url;//随机网页
        private String sign;//签名

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getHotType() {
            return hotType;
        }

        public void setHotType(int hotType) {
            this.hotType = hotType;
        }

        public int getBoxType() {
            return boxType;
        }

        public void setBoxType(int boxType) {
            this.boxType = boxType;
        }

        public int getCount() {
            return Count;
        }

        public void setCount(int count) {
            Count = count;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getSellPrice() {
            return sellPrice;
        }

        public void setSellPrice(String sellPrice) {
            this.sellPrice = sellPrice;
        }

        public String getCategoryUid() {
            return categoryUid;
        }

        public void setCategoryUid(String categoryUid) {
            this.categoryUid = categoryUid;
        }

        public String getIsCustomerDiscount() {
            return isCustomerDiscount;
        }

        public void setIsCustomerDiscount(String isCustomerDiscount) {
            this.isCustomerDiscount = isCustomerDiscount;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public Object getAttribute4() {
            return attribute4;
        }

        public void setAttribute4(Object attribute4) {
            this.attribute4 = attribute4;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCustomerPrice() {
            return customerPrice;
        }

        public void setCustomerPrice(String customerPrice) {
            this.customerPrice = customerPrice;
        }

        public String getBuyPrice() {
            return buyPrice;
        }

        public void setBuyPrice(String buyPrice) {
            this.buyPrice = buyPrice;
        }

        public String getAttribute1() {
            return attribute1;
        }

        public void setAttribute1(String attribute1) {
            this.attribute1 = attribute1;
        }

        public String getAttribute2() {
            return attribute2;
        }

        public void setAttribute2(String attribute2) {
            this.attribute2 = attribute2;
        }

        public String getPinyin() {
            return pinyin;
        }

        public void setPinyin(String pinyin) {
            this.pinyin = pinyin;
        }

        public String getAttribute3() {
            return attribute3;
        }

        public void setAttribute3(String attribute3) {
            this.attribute3 = attribute3;
        }


        public ProductsBean getCopy() {
            ProductsBean bean = new ProductsBean();

            bean.setSellPrice(this.getSellPrice());
            bean.setUid(this.getUid());
            bean.setAttribute1(this.getAttribute1());
            bean.setAttribute2(this.getAttribute2());
            bean.setAttribute3(this.getAttribute3());
            bean.setAttribute4(this.getAttribute4());
            bean.setBarcode(this.getBarcode());
            bean.setBoxType(this.getBoxType());
            bean.setBuyPrice(this.getBuyPrice());
            bean.setCategoryUid(this.getCategoryUid());
            bean.setCount(this.getCount());
            bean.setCustomerPrice(this.getCustomerPrice());
            bean.setDescription(this.getDescription());
            bean.setHotType(this.getHotType());
            bean.setIsCustomerDiscount(this.getIsCustomerDiscount());
            bean.setName(this.getName());
            bean.setPinyin(this.pinyin);
            bean.setStock(this.stock);
            return bean;
        }
    }
}
