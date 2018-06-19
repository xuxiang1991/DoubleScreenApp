package com.sunmi.doublescreen.doublescreenapp.bean;

import java.io.Serializable;

/**
 * 类名称：饮料
 * 类描述：
 * 创建人：xuxiang
 * 修改人：
 */
public class DrinkBean implements Serializable{


    private boolean canCold;
    private boolean canHot;
    private boolean canMin;
    private boolean canMiddle;
    private boolean canBig;

    private long ID;
    private String Name;
    private String price;
    private long count;


    public boolean isCanCold() {
        return canCold;
    }

    public void setCanCold(boolean canCold) {
        this.canCold = canCold;
    }

    public boolean isCanHot() {
        return canHot;
    }

    public void setCanHot(boolean canHot) {
        this.canHot = canHot;
    }

    public boolean isCanMin() {
        return canMin;
    }

    public void setCanMin(boolean canMin) {
        this.canMin = canMin;
    }

    public boolean isCanMiddle() {
        return canMiddle;
    }

    public void setCanMiddle(boolean canMiddle) {
        this.canMiddle = canMiddle;
    }

    public boolean isCanBig() {
        return canBig;
    }

    public void setCanBig(boolean canBig) {
        this.canBig = canBig;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
