package com.sunmi.doublescreen.doublescreenapp.bean;

import java.io.Serializable;

/**
 * 类名称：卡片动画位置
 * 类描述：
 * 创建人：
 * 修改人：
 */
public class CardMove implements Serializable{

    private float X;
    private float Y;
    
    public CardMove(float x,float y)
    {
        this.X=x;
        this.Y=y;
    }

    public float getX() {
        return X;
    }

    public void setX(float x) {
        X = x;
    }

    public float getY() {
        return Y;
    }

    public void setY(float y) {
        Y = y;
    }
}
