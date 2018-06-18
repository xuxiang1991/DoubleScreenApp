package com.sunmi.doublescreen.doublescreenapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.List;

/**
 * 项目名称：DSD
 * 类描述：显示多张图片
 * 创建人：Abtswiath丶lxy
 * 创建时间：2016/10/9 14:53
 * 修改人：longx
 * 修改时间：2016/10/9 14:53
 * 修改备注：
 */
public class ImgsActivity extends AppCompatActivity {

    private NoScrollViewPager viewPager;
    private TextView txt;
    private List<String> mPaths;
    private String jsonStr;
    private int i = 0;
    private int duration = 1000;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ++i;
            if (i > mPaths.size() - 1) {
                i = 0;
            }
            viewPager.setCurrentItem(i, false);
            mHandler.sendMessageDelayed(mHandler.obtainMessage(1), duration);
        }
    };
    private LruCache<String, Bitmap> mMemoryCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imgs);
        Intent intent = getIntent();
        if (intent != null) {
            mPaths = getIntent().getStringArrayListExtra("paths");
            jsonStr = getIntent().getStringExtra("json");
        }
        viewPager = (NoScrollViewPager) findViewById(R.id.viewpager);
        txt = (TextView) findViewById(R.id.txt);
        txt.setVisibility(View.GONE);
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            duration = jsonObject.getInt("rotation_time");
        } catch (Exception e) {
            e.printStackTrace();
        }
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = wm.getDefaultDisplay().getWidth();
        int screenHeight = wm.getDefaultDisplay().getHeight();
        viewPager.setAdapter(new GalleryAdapter(mPaths, this, screenWidth, screenHeight));
        viewPager.setNoScroll(true);
        viewPager.setCurrentItem(0);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(1), duration);
    }

    /**
     * 隐藏导航栏
     */
    public void hideNavigationBar() {
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | 0x00002000);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideNavigationBar();
            }
        }, 100);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        mHandler.removeMessages(1);
        super.onPause();
    }


}

