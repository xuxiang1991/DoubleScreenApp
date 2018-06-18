package com.sunmi.doublescreen.doublescreenapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.sunmi.doublescreen.doublescreenapp.utils.SharePreferenceUtil;

import java.io.IOException;

/**
 * 播放视频
 * Created by TAG on 2017/8/7.
 */

public class PlayVideoActivity extends AppCompatActivity {
    private SurfaceView surfaceView;
    private MediaPlayer player;
    private SurfaceHolder holder;
    private String path; //视频文件路劲
    private String fileId;//视频文件id
    private String data = "";//true 续播 false 从头播放
    private final String TAG = PlayVideoActivity.class.getSimpleName();
    private int position = 0;
    //***********************
    private int mVideoWidth;
    private int mVideoHeight;
    //    private boolean mIsVideoSizeKnown = false;
//    private boolean mIsVideoReadyToBePlayed = false;
    private boolean isPrepared = false;
    //**********************


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playvideo);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceView.getHolder().setKeepScreenOn(true);
        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        Log.d("TAG", "PlayVideoActivity FILEID------->" + intent.getLongExtra("FILEID", 0));
        if (intent.getLongExtra("FILEID", 0) > 0) {
            fileId = String.valueOf(intent.getLongExtra("FILEID", 0));
            position = (int) SharePreferenceUtil.getParam(this, fileId, 0);
        }
        data = intent.getStringExtra("DATA");
        if (TextUtils.isEmpty(data) || !data.equals("true")) {
            position = 0;
        }
        Log.d("TAG", "position-------->" + position);

        holder = surfaceView.getHolder();
        holder.addCallback(new MyCallBack());
        player = new MediaPlayer();

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(TAG, "onCompletion:-----------> ");
            }
        });

        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.d(TAG, "onPrepared: -------------->" + position);
                if (null != player) {
                    isPrepared = true;
                    player.seekTo(position);
                }
            }
        });


        player.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                Log.d(TAG, "onSeekComplete: ------------>" + mp.getCurrentPosition());
                startVideoPlayback();
            }
        });

        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(PlayVideoActivity.this, R.string.video_play_fail, Toast.LENGTH_LONG).show();
                return false;
            }
        });

        player.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                Log.d("TAG", "onVideoSizeChanged------>" + width + "    " + height);
//                if (width == 0 || height == 0) {
//                    Log.d(TAG, "invalid video width(" + width + ") or height(" + height + ")");
//                    return;
//                }
//                mIsVideoSizeKnown = true;
//                mVideoWidth = width;
//                mVideoHeight = height;
//                if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
//                    startVideoPlayback();
//                }
            }
        });

        isPrepared = false;


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ----------->" + isPrepared);
        if (player != null && isPrepared) {
            player.start();
        }
    }

    private void startVideoPlayback() {
        Log.d(TAG, "startVideoPlayback: --------->");
        if (player != null && !player.isPlaying()) {
//            holder.setFixedSize(mVideoWidth, mVideoHeight);
            player.start();
            player.setLooping(true);
        }
    }


    private class MyCallBack implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (player != null) {
                player.setDisplay(holder);
                try {
                    player.setDataSource(PlayVideoActivity.this, Uri.parse(path));
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(PlayVideoActivity.this, "播放视频文件失败" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                player.prepareAsync();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.d(TAG, "surfaceChanged: ----------------------->");
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.d(TAG, "surfaceDestroyed: ---------------->");

        }

    }

    @Override
    protected void onPause() {
        if (player != null) {
            if (player.isPlaying()) {
                player.pause();
            }
            Log.d("TAG", "onpause position------------>" + player.getCurrentPosition());
            if (getIntent().getLongExtra("FILEID", 0) > 0) {
                SharePreferenceUtil.setParam(this, fileId, player.getCurrentPosition());
            }
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player = null;
        }
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
}
