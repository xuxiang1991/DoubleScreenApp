package com.sunmi.doublescreen.doublescreenapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sunmi.doublescreen.doublescreenapp.data.Data;
import com.sunmi.doublescreen.doublescreenapp.utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import sunmi.ds.DSKernel;
import sunmi.ds.FilesManager;
import sunmi.ds.callback.IConnectionCallback;
import sunmi.ds.callback.IReceiveCallback;
import sunmi.ds.data.DSData;
import sunmi.ds.data.DSFile;
import sunmi.ds.data.DSFiles;

public class ViceActivity extends AppCompatActivity {
    private final String TAG = ViceActivity.class.getSimpleName();
    //双屏通讯帮助类
    private DSKernel mDSKernel = null;
    //文件管理帮助类
    private FilesManager mFilesManager;

    private Handler myHandler;
    private Gson gson = new Gson();
    private Intent intent = new Intent();
    private long taskid=0;
    /**
     * 发送端app包名
     */
    public String sender;

    private IConnectionCallback mIConnectionCallback = new IConnectionCallback() {
        @Override
        public void onDisConnect() {
            Message message = new Message();
            message.what = 1;
            message.obj = getString(R.string.unconnect_main_service);
            myHandler.sendMessage(message);
        }

        @Override
        public void onConnected(ConnState state) {
            Message message = new Message();
            message.what = 1;
            switch (state) {
                case AIDL_CONN:
                    message.obj = getString(R.string.connect_main_service);
                    break;
                case VICE_SERVICE_CONN:
                    message.obj = getString(R.string.connect_vice_service);
                    break;
                case VICE_APP_CONN:
                    message.obj = getString(R.string.connect_vice_dsd);
                    break;
                default:
                    break;
            }
            myHandler.sendMessage(message);
        }
    };

    /**
     * 双屏通讯消息回调
     */
    private IReceiveCallback mIReceiveCallback = new IReceiveCallback() {
        @Override
        public void onReceiveData(DSData data) {
            Log.d(TAG, "onReceiveData: ---------->" + data.data);
        }

        @Override
        public void onReceiveFile(DSFile file) {

        }

        @Override
        public void onReceiveFiles(DSFiles files) {

        }

        @Override
        public void onReceiveCMD(DSData cmd) {
            Log.d(TAG, "onReceiveCMD: ------------------->" + cmd.data);
            mFilesManager = FilesManager.getInstance();
            Log.d(TAG, "onReceiveCMD: ------------>1111111111111");
            Data data = gson.fromJson(cmd.data, Data.class);
            Log.d(TAG, "onReceiveCMD: ------------------->" + data.dataModel);
            taskid=cmd.taskId;
            sender=cmd.sender;
            switch (data.dataModel) {
                //副屏显示单张图片
                case SHOW_IMG_WELCOME:
                    intent.setClass(ViceActivity.this, PictureActivity.class);
                    intent.putExtra("path", data.data);
                    startActivity(intent);
                    break;
                //播放单个视频
                case VIDEO:
                    String path = mFilesManager.getFile(cmd.fileId).path;
                    intent.setClass(ViceActivity.this, VideoActivity.class);
                    intent.putExtra("path", path);
                    intent.putExtra("FILEID", cmd.fileId);
                    intent.putExtra("DATA", data.data);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    break;
                //播放多个视频文件
                case VIDEOS:
                    DSFiles videosFile = mFilesManager.getFiles(cmd.fileId);
                    ArrayList<String> videoFiles = new ArrayList<>();
                    for (DSFile dsFile : videosFile.files) {
                        videoFiles.add(dsFile.path);
                    }
                    intent.setClass(ViceActivity.this, VideosActivity.class);
                    intent.putStringArrayListExtra("paths", videoFiles);
                    intent.putExtra("FILEID", cmd.fileId);
                    intent.putExtra("DATA", data.data);
                    startActivity(intent);
                    break;
                //显示轮播图
                case IMAGES:
                    DSFiles dsFiles1 = mFilesManager.getFiles(cmd.fileId);
                    String msg1 = dsFiles1.filesDescribe.msg;
                    List<DSFile> paths = dsFiles1.files;
                    List<String> imgPaths = new ArrayList<>();
                    for (DSFile dsFile : paths) {
                        imgPaths.add(dsFile.path);
                    }
                    intent.setClass(ViceActivity.this, ImgsActivity.class);
                    intent.putExtra("json", msg1);
                    intent.putStringArrayListExtra("paths", (ArrayList<String>) imgPaths);
                    startActivity(intent);
                    break;
                case CLEAN_FILES:
                    Log.d(TAG, "delete file is ----->" + data.data);
                    FileUtils.deleteDir(data.data);
                    break;
                case GETVICECACHEFILESIZE://获取副屏缓存文件大小
                    Log.d(TAG, "获取副屏缓存文件大小----->" + data.data);
//                    long size = 0L;
//                    File file = new File(data.data);
//                    if (file.exists()) {
//                        size = getFilesSize(file);
//                    }
                    Toast.makeText(ViceActivity.this, "" + data.data, Toast.LENGTH_LONG).show();
                    mDSKernel.sendResult(cmd.sender, data.data+"", cmd.taskId, null);
                    break;
                case OPEN_APP:
                    Log.e(TAG,"打开了app");
//                    Toast.makeText(ViceActivity.this, "副屏收到"+String.valueOf(data.data),Toast.LENGTH_LONG).show();
                    mDSKernel.sendResult(cmd.sender, data.data+"", cmd.taskId, null);
                    break;
                case GETTEA:
                    Log.e(TAG,"打开了app");
//                    Toast.makeText(ViceActivity.this, "副屏收到"+String.valueOf(data.data),Toast.LENGTH_LONG).show();
//                    mDSKernel.sendResult(cmd.sender, data.data+"", cmd.taskId, null);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vice2);
        initSdk();
        initData();
        findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDSKernel.sendResult(sender, "dove", taskid, null);

            }
        });
    }

    private void initSdk() {
        mDSKernel = DSKernel.newInstance();
        mDSKernel.init(this, mIConnectionCallback);
        mDSKernel.addReceiveCallback(mIReceiveCallback);
    }

    private void initData() {

        myHandler = new MyHandler(this);
    }

    private static class MyHandler extends Handler {
        private WeakReference<Activity> mActivity;

        public MyHandler(Activity activity) {
            mActivity = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() != null && !mActivity.get().isFinishing()) {
                switch (msg.what) {
                    case 1://消息提示用途
                        Toast.makeText(mActivity.get(), msg.obj + "", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 获取文件夹大小
     *
     * @param file
     * @return
     */
    private long getFilesSize(File file) {
        long size = 0L;
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            Log.d(TAG, "filename----->" + files[i]);
            if (files[i].isDirectory()) {
                size = size + getFilesSize(files[i]);
            } else {
                size = size + getFileSize(files[i]);
            }
        }
        return size;
    }

    /**
     * @param file
     */
    private long getFileSize(File file) {
        long size = 0L;
        if (file.exists()) {
            FileInputStream fileInputStream;
            try {
                fileInputStream = new FileInputStream(file);
                size = fileInputStream.available();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, file.getName() + "大小----》" + size);
        return size;
    }
}
