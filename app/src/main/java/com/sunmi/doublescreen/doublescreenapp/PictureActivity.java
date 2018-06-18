package com.sunmi.doublescreen.doublescreenapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;


public class PictureActivity extends AppCompatActivity {
    private final String TAG = PictureActivity.class.getSimpleName();
    private String path;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ------------>");
        setContentView(R.layout.activity_picture);
        initView();
        path = getIntent().getStringExtra("path");
        initData(new File(path));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent: ---------------->");
        path = intent.getStringExtra("path");
        initData(new File(path));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ------------->");
    }

    private void initView() {
        iv = (ImageView) findViewById(R.id.iv);
    }

    private void initData(File file) {
        if (file.exists()) {
            Glide.with(this).load(file).into(iv);
        } else {
            iv.setImageResource(R.drawable.img_03);
        }
    }
}
