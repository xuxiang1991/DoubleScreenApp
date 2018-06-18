package com.sunmi.doublescreen.doublescreenapp;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * 项目名称：DSD
 * 类描述：
 * 创建人：Abtswiath丶lxy
 * 创建时间：2016/10/10 16:18
 * 修改人：longx
 * 修改时间：2016/10/10 16:18
 * 修改备注：
 */
public class NoScrollViewPager extends ViewPager {
    private boolean noScroll = false;
    private MScroller scroller = null;

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        /* return false;//super.onTouchEvent(arg0); */
        if (noScroll)
            return false;
        else
            return super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (noScroll)
            return false;
        else
            return super.onInterceptTouchEvent(arg0);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        if (Math.abs(getCurrentItem() - item) > 1) {
            scroller.setNoDuration(true);
            super.setCurrentItem(item, smoothScroll);
            scroller.setNoDuration(false);
        } else {
            scroller.setNoDuration(false);
            super.setCurrentItem(item, smoothScroll);
        }
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }


    private void init(Context context) {
        scroller = new MScroller(context);
        Class<ViewPager> cl = ViewPager.class;
        try {
            Field field = cl.getDeclaredField("mScroller");
            field.setAccessible(true); //利用反射设置mScroller域为自己定义的MScroller field.set(viewPager,scroller); } catch (NoSuchFieldException e) { e.printStackTrace(); }catch (IllegalAccessException e){ e.printStackTrace(); } }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}

class MScroller extends Scroller {
    private static final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };
    public boolean noDuration;

    public void setNoDuration(boolean noDuration) {
        this.noDuration = noDuration;
    }

    public MScroller(Context context) {
        this(context, sInterpolator);
    }

    public MScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        if (noDuration) //界面滑动不需要时间间隔
            super.startScroll(startX, startY, dx, dy, 0);
        else super.startScroll(startX, startY, dx, dy, duration);
    }
}






