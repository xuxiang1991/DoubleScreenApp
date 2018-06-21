package com.sunmi.doublescreen.doublescreenapp.bean;

import java.io.Serializable;

/**
 * 类名称：饮料
 * 类描述：
 * 创建人：xuxiang
 * 修改人：
 */
public class DrinkBean implements Serializable {


    private boolean canCold;
    private boolean canHot;
    private boolean canMin;
    private boolean canMiddle;
    private boolean canBig;

    private long ID;
    private String Name;
    private String price;
    private int count;

    private String description;

    private int hotType;//0 热饮  1冷饮 2 常温
    private int boxType;//0 小  1中 2 大


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }



    public DrinkBean getCopy()
    {
        DrinkBean bean=new DrinkBean();
        bean.setID(this.getID());
        bean.setBoxType(this.getBoxType());
        bean.setHotType(this.getHotType());
        bean.setPrice(this.getPrice());
        bean.setName(this.getName());
        bean.setCanMin(this.isCanMin());
        bean.setCanMiddle(this.isCanMiddle());
        bean.setCanHot(this.isCanHot());
        bean.setCanCold(this.isCanCold());
        bean.setCanBig(this.isCanBig());
        bean.setCount(this.getCount());
        bean.setDescription(this.getDescription());
        return bean;
    }
}
