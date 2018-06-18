package com.sunmi.doublescreen.doublescreenapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import com.sunmi.doublescreen.doublescreenapp.player.IMPlayListener;
import com.sunmi.doublescreen.doublescreenapp.player.IMPlayer;
import com.sunmi.doublescreen.doublescreenapp.player.MPlayer;
import com.sunmi.doublescreen.doublescreenapp.player.MPlayerException;
import com.sunmi.doublescreen.doublescreenapp.player.MinimalDisplay;
import com.sunmi.doublescreen.doublescreenapp.utils.SharePreferenceUtil;

public class VideoActivity extends AppCompatActivity {

    private SurfaceView mPlayerView;
    private MPlayer player;
    private String path; //视频文件路劲
    private String fileId;//视频文件id
    private String data = "";//true 续播 false 从头播放
    private int position = 0;
    private final String TAG = "GLL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: --------------->");
        setContentView(R.layout.activity_video);
        mPlayerView = (SurfaceView) findViewById(R.id.mPlayerView);
        initPlayer();
        Intent intent = getIntent();
        path = intent.getStringExtra("path");

        if (intent.getLongExtra("FILEID", 0) > 0) {
            fileId = String.valueOf(intent.getLongExtra("FILEID", 0));
            position = (int) SharePreferenceUtil.getParam(this, fileId, 0);
        }
        data = intent.getStringExtra("DATA");
        if (TextUtils.isEmpty(data) || !data.equals("true")) {
            position = 0;
        }
        Log.d("FILE", "position-------->" + position);
        try {
            player.setSource(path, position);
        } catch (MPlayerException e) {
            e.printStackTrace();
        }
    }

    private void initPlayer() {
        player = new MPlayer();
        player.setDisplay(new MinimalDisplay(mPlayerView));
        player.setPlayListener(new IMPlayListener() {
            @Override
            public void onStart(IMPlayer player) {

            }

            @Override
            public void onPause(IMPlayer player) {

            }

            @Override
            public void onResume(IMPlayer player) {

            }

            @Override
            public void onComplete(IMPlayer player) {
                try {
                    player.setSource(path, 0);
                } catch (MPlayerException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ----------->");
        super.onResume();
        player.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: ------------>");
        super.onPause();
        if (getIntent().getLongExtra("FILEID", 0) > 0) {
            Log.d("FILE", "onPause: ---------------->" + player.getPosition());
            SharePreferenceUtil.setParam(this, fileId, player.getPosition());
        }
        player.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ------------>");
        super.onDestroy();
        player.onDestroy();
    }
}
