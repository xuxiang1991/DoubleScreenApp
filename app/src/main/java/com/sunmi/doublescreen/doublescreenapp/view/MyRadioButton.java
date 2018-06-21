package com.sunmi.doublescreen.doublescreenapp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;


/**
 * Created by highsixty on 2018/3/14.
 * mail  gaolulin@sunmi.com
 */

public class MyRadioButton extends AppCompatRadioButton {
    public MyRadioButton(Context context) {
        super(context);
    }

    public MyRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        if (null != drawables) {
            Drawable top = drawables[1]; // 上方图标
            int drawablepadding = getCompoundDrawablePadding();
            int drawableTop = top.getIntrinsicHeight();
            canvas.translate(0, (getHeight() - drawableTop - drawablepadding) / 2);
        }
        super.onDraw(canvas);
    }
}
