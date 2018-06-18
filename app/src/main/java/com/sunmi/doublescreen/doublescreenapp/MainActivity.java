package com.sunmi.doublescreen.doublescreenapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sunmi.doublescreen.doublescreenapp.data.DataModel;
import com.sunmi.doublescreen.doublescreenapp.data.UPacketFactory;
import com.sunmi.doublescreen.doublescreenapp.utils.SharePreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import sunmi.ds.DSKernel;
import sunmi.ds.SF;
import sunmi.ds.callback.ICheckFileCallback;
import sunmi.ds.callback.IConnectionCallback;
import sunmi.ds.callback.IReceiveCallback;
import sunmi.ds.callback.ISendCallback;
import sunmi.ds.callback.ISendFilesCallback;
import sunmi.ds.callback.QueryCallback;
import sunmi.ds.data.DSData;
import sunmi.ds.data.DSFile;
import sunmi.ds.data.DSFiles;
import sunmi.ds.data.DataPacket;

/**
 * 双屏应用主屏界面
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = MainActivity.class.getSimpleName();
    //双屏通讯帮助类
    private DSKernel mDSKernel = null;
    //副屏显示单张图片
    private Button btnShowPicture;
    //打开自定义副屏应用
    private Button btnOPenApp;
    //播放单个视频
    private Button btnPlayVideo;
    //播放多个视频文件
    private Button btnPlayVideos;
    //展示轮播图片
    private Button btnShowImgs;
    //清除缓存文件
    private Button btnCleanFile;
    //获取副屏缓存文件大小
    private Button btnGetfilecachesize;
    //发送文本
    private Button btnShowText ;
    //轮播图集合
    List<String> imgs = new ArrayList<>();
    //视频文件在本地缓存的key
    private final String videoKey = "DOUBLESCREENVIDEO";
    //多视频文件在本地缓存key
    private final String videosKey = "DOUBLESCREENVIDEOS";
    //轮播图文件在本地缓存key
    private final String imgsKey = "DOUBLESCREENIMGS";
    //单张图文件在本地缓存key
    private final String imgKey = "DOUBLESCREENIMG";
    private Handler myHandler;
    private final String picturePath = Environment.getExternalStorageDirectory().getPath() + "/img_01.png";
    private ProgressDialog dialog;

//    private AlertDialog  dialog ;


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
                    DataPacket dsPacket = UPacketFactory.buildOpenApp(getPackageName(), null);
                    mDSKernel.sendCMD(dsPacket);
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

        }

        @Override
        public void onReceiveFile(DSFile file) {

        }

        @Override
        public void onReceiveFiles(DSFiles files) {

        }

        @Override
        public void onReceiveCMD(DSData cmd) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initSdk();
        initView();
        initAction();
    }


    private void initView() {
        btnOPenApp = (Button) findViewById(R.id.btn_open_vice_app);
        btnShowPicture = (Button) findViewById(R.id.btn_show_picture);
        btnPlayVideo = (Button) findViewById(R.id.btn_play_video);
        btnPlayVideos = (Button) findViewById(R.id.btn_play_videos);
        btnShowImgs = (Button) findViewById(R.id.btn_show_imgs);
        btnCleanFile = (Button) findViewById(R.id.btn_clean_cache);
        btnGetfilecachesize = (Button) findViewById(R.id.btn_getfilecachesize);
        btnShowText = (Button)findViewById(R.id.btn_show_text);
    }

    private void initData() {
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_01.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_02.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_03.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_04.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_05.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_06.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_01.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_02.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_03.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_04.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_05.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_06.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_01.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_02.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_03.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_04.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_05.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_06.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_01.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_02.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_03.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_04.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_05.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_06.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_01.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_02.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_03.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_04.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_05.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_06.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_01.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_02.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_03.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_04.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_05.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_06.png");

        myHandler = new MyHandler(this);

        dialog = new ProgressDialog(this);
    }

    private void initAction() {
        btnShowPicture.setOnClickListener(this);
        btnOPenApp.setOnClickListener(this);
        btnPlayVideo.setOnClickListener(this);
        btnPlayVideos.setOnClickListener(this);
        btnShowImgs.setOnClickListener(this);
        btnCleanFile.setOnClickListener(this);
        btnGetfilecachesize.setOnClickListener(this);
        btnShowText.setOnClickListener(this);
    }


    private void initSdk() {
        mDSKernel = DSKernel.newInstance();
        mDSKernel.init(this, mIConnectionCallback);
        mDSKernel.addReceiveCallback(mIReceiveCallback);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //显示单张图片
            case R.id.btn_show_picture:
//                showDialog(getString(R.string.sending_picture));
//                long imgTaskId = (long) SharePreferenceUtil.getParam(this, imgKey, 0L);
//                checkImgFileExist(imgTaskId);




                JSONObject jsonObjectimg = new JSONObject();
                try {
                    jsonObjectimg.put("dataModel", "OPEN_APP");
                    jsonObjectimg.put("data", "随机数"+Math.random()*100);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                DataPacket packetimg = new DataPacket.Builder(DSData.DataType.CMD).recPackName(SF.SUNMI_DSD_PACKNAME).data(jsonObjectimg.toString())
                        .addCallback(null).build();


                mDSKernel.sendQuery(packetimg, new QueryCallback() {
                    @Override
                    public void onReceiveData(final DSData data) {
                        Log.d("highsixty", "onReceiveData: ------------>" + data.data);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "主屏1收到"+data.data,Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });

                break;
            //打开指定副屏应用
            case R.id.btn_open_vice_app:
//                DataPacket dsPacket = UPacketFactory.buildOpenApp(getPackageName(), null);
//                mDSKernel.sendCMD(dsPacket);




                JSONObject jsonObjectapp = new JSONObject();
                try {
                    jsonObjectapp.put("dataModel", "OPEN_APP");
                    jsonObjectapp.put("data", "随机数"+Math.random()*100);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                DataPacket packetapp = new DataPacket.Builder(DSData.DataType.CMD).recPackName(getPackageName()).data(jsonObjectapp.toString())
                        .addCallback(null).build();


                mDSKernel.sendQuery(packetapp, new QueryCallback() {
                    @Override
                    public void onReceiveData(final DSData data) {
                        Log.d("highsixty", "onReceiveData: ------------>" + data.data);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "主屏2收到"+data.data,Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                break;
            //播放单个视频
            case R.id.btn_play_video:
//                showDialog(getString(R.string.sending_video));
//                long videoTaskId = (long) SharePreferenceUtil.getParam(this, videoKey, 0L);
//                checkVideoFileExist(videoTaskId);
                JSONObject jsonObjectapp1 = new JSONObject();
                try {
                    jsonObjectapp1.put("dataModel", "GETTEA");
                    jsonObjectapp1.put("data", "随机数"+Math.random()*100);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                DataPacket packetapp1 = new DataPacket.Builder(DSData.DataType.CMD).recPackName(SF.SUNMI_DSD_PACKNAME).data(jsonObjectapp1.toString())
                        .addCallback(null).build();


                mDSKernel.sendQuery(packetapp1, new QueryCallback() {
                    @Override
                    public void onReceiveData(final DSData data) {
                        Log.d("highsixty", "onReceiveData: ------------>" + data.data);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "主屏3收到"+data.data,Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                break;
            //播放多个视频文件
            case R.id.btn_play_videos:
//                showDialog(getString(R.string.sending_videos));
//                long videosTaskId = (long) SharePreferenceUtil.getParam(this, videosKey, 0L);
//                checkVideosFileExist(videosTaskId);

                JSONObject jsonObjectapp2 = new JSONObject();
                try {
                    jsonObjectapp2.put("dataModel", "GETTEA");
                    jsonObjectapp2.put("data", "随机数"+Math.random()*100);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                DataPacket packetapp2 = new DataPacket.Builder(DSData.DataType.CMD).recPackName(getPackageName()).data(jsonObjectapp2.toString())
                        .addCallback(null).build();


                mDSKernel.sendQuery(packetapp2, new QueryCallback() {
                    @Override
                    public void onReceiveData(final DSData data) {
                        Log.d("highsixty", "onReceiveData: ------------>" + data.data);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "主屏4收到"+data.data,Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                break;
            //展示轮播图
            case R.id.btn_show_imgs:
                showDialog(getString(R.string.sending_pictures));
                long imgsTaskId = (long) SharePreferenceUtil.getParam(this, imgsKey, 0L);
                checkImgsFileExist(imgsTaskId);
                break;
            //清除副屏缓存
            case R.id.btn_clean_cache:
                DataPacket packet2 = UPacketFactory.remove_folders(getPackageName(), Environment.getExternalStorageDirectory().getAbsolutePath() + "/HCService/" + getPackageName().replace(".", "_"), new ISendCallback() {
                    @Override
                    public void onSendSuccess(long taskId) {
                        showToast("清除缓存文件成功");
                    }

                    @Override
                    public void onSendFail(int errorId, String errorInfo) {
                        showToast("清除缓存文件失败");
                    }

                    @Override
                    public void onSendProcess(long totle, long sended) {

                    }
                });
                mDSKernel.sendCMD(packet2);
                break;
            //获取副屏缓存文件大小
            case R.id.btn_getfilecachesize:
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("dataModel", "GETVICECACHEFILESIZE");
                    jsonObject.put("data", "{ name : dove}");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                DataPacket packet = new DataPacket.Builder(DSData.DataType.CMD).recPackName(getPackageName()).data(jsonObject.toString())
                        .addCallback(null).build();
                mDSKernel.sendQuery(packet, new QueryCallback() {
                    @Override
                    public void onReceiveData(final DSData data) {
                        Log.d("highsixty", "onReceiveData: ------------>" + data.data);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "" + data.data, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                break;
            //展示文本
            case  R.id.btn_show_text:
                DataPacket packet1 = UPacketFactory.buildShowText(getPackageName(),"hello world",null);
                mDSKernel.sendData(packet1);
                break;
            default:
                break;
        }
    }


    /**
     * 检查单张图图缓存问价是否存在
     *
     * @param taskID
     */
    private void checkImgFileExist(final long taskID) {
        Log.d(TAG, "checkImgFileExist: ---------->" + taskID);
        if (taskID < 0) {
            sendPicture();
            return;
        }
        checkFileExist(taskID, new ICheckFileCallback() {
            @Override
            public void onCheckFail() {
                //检查缓存文件失败
                Log.d(TAG, "onCheckFail: ------------>file not exist");
                sendPicture();
            }

            @Override
            public void onResult(boolean exist) {
                if (exist) {
                    //缓存文件存在
                    Log.d(TAG, "onResult: --------->file is exist");
                    dismissDialog();
                    showPicture(taskID);
                } else {
                    //缓存文件不存在
                    Log.d(TAG, "onResult: --------->file is not exist");
                    sendPicture();
                }
            }
        });
    }

    /**
     * 检查轮播图缓存问价是否存在
     *
     * @param taskID
     */
    private void checkImgsFileExist(final long taskID) {
        if (taskID < 0) {
            sendImgs();
            return;
        }
        checkFileExist(taskID, new ICheckFileCallback() {
            @Override
            public void onCheckFail() {
                //检查缓存文件失败
                Log.d(TAG, "onCheckFail: ------------>file not exist");
                sendImgs();
            }

            @Override
            public void onResult(boolean exist) {
                if (exist) {
                    //缓存文件存在
                    Log.d(TAG, "onResult: --------->file is exist");
                    dismissDialog();
                    showImgs(taskID);
                } else {
                    //缓存文件不存在
                    sendImgs();
                }
            }
        });
    }

    /**
     * 发送轮播图片
     */
    private void sendImgs() {
        JSONObject json = new JSONObject();
        try {
            //轮播图切换时间
            json.put("rotation_time", 2000);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mDSKernel.sendFiles(getPackageName(), json.toString(), imgs, new ISendFilesCallback() {
            @Override
            public void onAllSendSuccess(long l) {
                Log.d("TAG", "onAllSendSuccess: ---------->");
                dismissDialog();
                showImgs(l);
                SharePreferenceUtil.setParam(MainActivity.this, imgsKey, l);
            }

            @Override
            public void onSendSuccess(final String path, final long fileId) {
                Log.d("TAG", "onSendSuccess: ----------->");
            }

            @Override
            public void onSendFaile(final int i, final String s) {
                Log.d("TAG", "onSendFaile: ------------->" + s);
                dismissDialog();
                showToast(getString(R.string.fail_send_picture) + s);
            }

            @Override
            public void onSendFileFaile(String s, int i, String s1) {
                Log.d(TAG, "onSendFileFaile: --------------->" + s + "  " + s1);
                dismissDialog();
                showToast(getString(R.string.fail_send_picture) + s + "  " + s1);
            }

            @Override
            public void onSendProcess(final String s, final long l, final long l1) {
            }
        });
    }

    /**
     * 展示轮播图片
     */
    private void showImgs(long taskId) {
        String json = UPacketFactory.createJson(DataModel.IMAGES, "");
        mDSKernel.sendCMD(getPackageName(), json, taskId, null);
    }

    /**
     * 检查视频文件在副屏是否存在
     *
     * @param taskID
     */
    private void checkVideoFileExist(final long taskID) {
        Log.d(TAG, "checkVideoFileExist: ---------->" + taskID);
        if (taskID <= 0L) {
            sendVideo();
            return;
        }
        checkFileExist(taskID, new ICheckFileCallback() {
            @Override
            public void onCheckFail() {
                //检查缓存文件失败
                Log.d(TAG, "onCheckFail: ------------>file not exist");
                sendVideo();
            }

            @Override
            public void onResult(boolean exist) {
                if (exist) {
                    //缓存文件存在
                    dismissDialog();
                    Log.d(TAG, "onResult: --------->file is exist");
                    playvideo(taskID, "true");
                } else {
                    //缓存文件不存在
                    Log.d(TAG, "onResult: --------->file is not exist");
                    sendVideo();
                }
            }
        });
    }

    /**
     * 发送单视频文件到副屏
     */
    private void sendVideo() {
        mDSKernel.sendFile(getPackageName(), Environment.getExternalStorageDirectory().getPath() + "/video_01.mp4", new sunmi.ds.callback.ISendCallback() {
            @Override
            public void onSendSuccess(long l) {
                Log.d(TAG, "onSendSuccess: ----------->" + l);
                dismissDialog();
                SharePreferenceUtil.setParam(MainActivity.this, videoKey, l);
                playvideo(l, "true");
            }

            @Override
            public void onSendFail(int i, String s) {
                dismissDialog();
                showToast(getString(R.string.fail_send_sigle_video) + s);
            }

            @Override
            public void onSendProcess(final long l, final long l1) {
            }
        });
    }

    /**
     * 播放单个视频文件
     *
     * @param filedID      发送视频文件返回的id
     * @param continueplay 是否续播，"true" 续播 "false"  从头开始
     */
    private void playvideo(long filedID, String continueplay) {
        Log.d("TAG", "playvideo: ------------>");
        String json = UPacketFactory.createJson(DataModel.VIDEO, continueplay);
        mDSKernel.sendCMD(getPackageName(), json, filedID, null);
    }

    /**
     * 检查多视频文件
     *
     * @param taskID
     */
    private void checkVideosFileExist(final long taskID) {
        Log.d(TAG, "checkVideosFileExist: ---------->" + taskID);
        if (taskID <= 0L) {
            sendVideos();
            return;
        }
        checkFileExist(taskID, new ICheckFileCallback() {
            @Override
            public void onCheckFail() {
                Log.d(TAG, "onCheckFail: ------------>file not exist");
                sendVideos();
            }

            @Override
            public void onResult(boolean exist) {
                if (exist) {
                    Log.d(TAG, "onResult: --------->file is exist");
                    dismissDialog();
                    playvideos(taskID, "true");
                } else {
                    Log.d(TAG, "onResult: ---------->file is not exist");
                    sendVideos();
                }
            }
        });
    }

    /**
     * 发送多视频
     */
    private void sendVideos() {
        //请对文件是否存在做判断
        List<String> files = new ArrayList<>();
        files.add(Environment.getExternalStorageDirectory().getPath() + "/video_01.mp4");
        files.add(Environment.getExternalStorageDirectory().getPath() + "/video_02.mp4");
        files.add(Environment.getExternalStorageDirectory().getPath() + "/video_03.mp4");
        mDSKernel.sendFiles(getPackageName(), "", files, new ISendFilesCallback() {
            @Override
            public void onAllSendSuccess(long fileid) {
                Log.d(TAG, "onAllSendSuccess: ----------->" + fileid);
                dismissDialog();
                SharePreferenceUtil.setParam(MainActivity.this, videosKey, fileid);
                playvideos(fileid, "true");
            }

            @Override
            public void onSendSuccess(String path, long taskId) {
                Log.d(TAG, "onSendSuccess: --------------->");
            }

            @Override
            public void onSendFaile(int errorId, String errorInfo) {
                Log.d(TAG, "onSendFaile: --------------->");
                dismissDialog();
                showToast(getString(R.string.fail_send_more_videos) + errorInfo);
            }

            @Override
            public void onSendFileFaile(String path, int errorId, String errorInfo) {
                Log.d(TAG, "onSendFileFaile: -------------->");
                dismissDialog();
                showToast(getString(R.string.fail_send_more_videos) + path + errorInfo);
            }

            @Override
            public void onSendProcess(String path, long totle, long sended) {

            }
        });
    }

    /**
     * 播放多视频文件
     *
     * @param taskID
     */
    private void playvideos(long taskID, String continueplay) {
        Log.d(TAG, "playvideos: ------------>" + taskID);
        String json = UPacketFactory.createJson(DataModel.VIDEOS, continueplay);
        mDSKernel.sendCMD(getPackageName(), json, taskID, null);
    }

    private void sendPicture() {

        mDSKernel.sendFile(getPackageName(), picturePath, new ISendCallback() {
            @Override
            public void onSendSuccess(long taskId) {
                dismissDialog();
                showToast(getString(R.string.single_picture_sendsuccess));
                SharePreferenceUtil.setParam(MainActivity.this, imgKey, taskId);
                showPicture(taskId);
            }

            @Override
            public void onSendFail(int errorId, String errorInfo) {
                Log.d("TAG", "onSendFail: -------------------->" + errorId + "  " + errorInfo);
                dismissDialog();
                showToast(getString(R.string.single_picture_sendfail) + errorInfo);
            }

            @Override
            public void onSendProcess(long totle, long sended) {

            }
        });
    }

    /**
     * 展示单张图片
     *
     * @param taskId
     */
    private void showPicture(long taskId) {
        //显示图片
        try {
            JSONObject json = new JSONObject();
            json.put("dataModel", "SHOW_IMG_WELCOME");
            json.put("data", "default");
            mDSKernel.sendCMD(getPackageName(), json.toString(), taskId, null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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


    public void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void checkFileExist(long fileId, final ICheckFileCallback mICheckFileCallback) {
        DataPacket packet = new DataPacket.Builder(DSData.DataType.CHECK_FILE).data("def").
                recPackName(getPackageName()).addCallback(new ISendCallback() {
            @Override
            public void onSendSuccess(long taskId) {

            }

            @Override
            public void onSendFail(int errorId, String errorInfo) {
                if (mICheckFileCallback != null) {
                    mICheckFileCallback.onCheckFail();
                }
            }

            @Override
            public void onSendProcess(long totle, long sended) {

            }
        }).isReport(true).build();
        packet.getData().fileId = fileId;
        mDSKernel.sendQuery(packet, new QueryCallback() {
            @Override
            public void onReceiveData(DSData data) {
                boolean exist = TextUtils.equals("true", data.data);
                if (mICheckFileCallback != null) {
                    mICheckFileCallback.onResult(exist);
                }
            }
        });
    }

    private synchronized void showDialog(String title) {
        Log.d(TAG, "showDialog: ----------------->");
        if (dialog != null && !dialog.isShowing()) {
            dialog.setTitle(title);
            dialog.show();
        }
    }

    private synchronized void dismissDialog() {
        Log.d(TAG, "dismissDialog: ------------->");
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
