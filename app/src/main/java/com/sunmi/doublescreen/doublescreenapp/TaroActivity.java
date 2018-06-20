package com.sunmi.doublescreen.doublescreenapp;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sunmi.doublescreen.doublescreenapp.bean.CardMove;
import com.sunmi.doublescreen.doublescreenapp.data.Data;
import com.sunmi.doublescreen.doublescreenapp.network.config.DailogUtil;
import com.sunmi.doublescreen.doublescreenapp.network.config.DomainUrl;
import com.sunmi.doublescreen.doublescreenapp.network.service.CommonApiProvider;
import com.sunmi.doublescreen.doublescreenapp.network.service.CommonRequest;
import com.sunmi.doublescreen.doublescreenapp.network.service.CommonResponse;
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

public class TaroActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = TaroActivity.class.getSimpleName();
    //双屏通讯帮助类
    private DSKernel mDSKernel = null;
    //文件管理帮助类
    private FilesManager mFilesManager;

    private Handler myHandler;
    private Gson gson = new Gson();
    private Intent intent = new Intent();
    private long taskid = 0;
    /**
     * 发送端app包名
     */
    public String sender;


    private ImageView ivLogo;
    private LinearLayout llHot;
    private ImageView imgHot;
    private ImageView imgIce;
    private LinearLayout llBusness;
    private ImageView imgLove;
    private ImageView imgBusiness;
    private RelativeLayout rl_drink, rl_select;
    private TextView tv_drink_id;
    private TextView tv_drink_name;
    //    private LinearLayout llDrinkTwo;
    private ImageView ivSelect;
    private ImageView ivBack;

    private int hotType = 0;// 0 热饮  1冷饮

    private Activity self;

    /**
     * 保存动画位置
     */
    private List<CardMove> cardMoves = new ArrayList<>();

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
            taskid = cmd.taskId;
            sender = cmd.sender;
            switch (data.dataModel) {
                //副屏显示单张图片
                case SHOW_IMG_WELCOME:
                    intent.setClass(TaroActivity.this, PictureActivity.class);
                    intent.putExtra("path", data.data);
                    startActivity(intent);
                    break;
                //播放单个视频
                case VIDEO:
                    String path = mFilesManager.getFile(cmd.fileId).path;
                    intent.setClass(TaroActivity.this, VideoActivity.class);
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
                    intent.setClass(TaroActivity.this, VideosActivity.class);
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
                    intent.setClass(TaroActivity.this, ImgsActivity.class);
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
                    Toast.makeText(TaroActivity.this, "" + data.data, Toast.LENGTH_LONG).show();
                    mDSKernel.sendResult(cmd.sender, data.data + "", cmd.taskId, null);
                    break;
                case OPEN_APP:
                    Log.e(TAG, "打开了app");
//                    Toast.makeText(TaroActivity.this, "副屏收到"+String.valueOf(data.data),Toast.LENGTH_LONG).show();
                    mDSKernel.sendResult(cmd.sender, data.data + "", cmd.taskId, null);
                    break;
                case GETTEA:
                    Log.e(TAG, "打开了app");
//                    Toast.makeText(TaroActivity.this, "副屏收到"+String.valueOf(data.data),Toast.LENGTH_LONG).show();
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
        self = this;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_taro);
        initView();
        initSdk();
        initData();
        showBusiness();

//        findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mDSKernel.sendResult(sender, "dove", taskid, null);
//
//            }
//        });
    }

    private void initSdk() {
        mDSKernel = DSKernel.newInstance();
        mDSKernel.init(this, mIConnectionCallback);
        mDSKernel.addReceiveCallback(mIReceiveCallback);
    }

    private void initView() {
        ivLogo = (ImageView) findViewById(R.id.iv_logo);
        llHot = (LinearLayout) findViewById(R.id.ll_hot);
        imgHot = (ImageView) findViewById(R.id.img_hot);
        imgIce = (ImageView) findViewById(R.id.img_ice);
        llBusness = (LinearLayout) findViewById(R.id.ll_busness);
        imgLove = (ImageView) findViewById(R.id.img_love);
        imgBusiness = (ImageView) findViewById(R.id.img_business);
        rl_drink = (RelativeLayout) findViewById(R.id.rl_drink);
        rl_select = (RelativeLayout) findViewById(R.id.rl_select);
        ivSelect = (ImageView) findViewById(R.id.iv_select);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tv_drink_id = (TextView) findViewById(R.id.tv_drink_id);
        tv_drink_name = (TextView) findViewById(R.id.tv_drink_name);

        imgLove.setOnClickListener(this);
        imgBusiness.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        imgHot.setOnClickListener(this);
        imgIce.setOnClickListener(this);
    }


    private void initData() {

        myHandler = new MyHandler(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_love:
            case R.id.img_business:
                showHot();
                break;
            case R.id.img_hot:
                hotType = 0;
                showDrinks();
                break;
            case R.id.img_ice:
                hotType = 1;
                showDrinks();
                break;
            case R.id.iv_back:
                showBusiness();
                break;
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

    private int alphaCount = 0;


    /**
     * 显示卡
     */
    private void goToAlpha() {

        for (int i = 0; i < rl_drink.getChildCount(); i++) {
            final ImageView v = (ImageView) rl_drink.getChildAt(i);
            rl_drink.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AnimationWithAlpa(v);
                }
            }, 300 * i);

        }
    }


    /**
     * 显示盲选
     */
    private void showDrinks() {

        llBusness.setVisibility(View.GONE);
        llHot.setVisibility(View.GONE);
        rl_drink.setVisibility(View.VISIBLE);
        rl_select.setVisibility(View.GONE);
        ivBack.setVisibility(View.GONE);

        rl_drink.postDelayed(new Runnable() {
            @Override
            public void run() {
                goTocenter();
            }
        }, 1);
    }

    /**
     * 聚合动画
     */
    private void goTocenter() {

        goToAlpha();
        rl_drink.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 3; i++) {
                    rl_drink.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < rl_drink.getChildCount(); i++) {
                                ImageView v = (ImageView) rl_drink.getChildAt(i);
                                AnimationWithItem(v);
                            }
                            rl_drink.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    for (int i = 0; i < rl_drink.getChildCount(); i++) {
                                        ImageView v = (ImageView) rl_drink.getChildAt(i);
                                        AnimationWithItemOff(v, i);
                                    }

                                }
                            }, 500);
                        }
                    }, 1000 * i);
                }

            }
        }, 4800);


        for (int i = 0; i < rl_drink.getChildCount(); i++) {
            ImageView v = (ImageView) rl_drink.getChildAt(i);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLastTea();
                }
            });
        }
        ivBack.postDelayed(new Runnable() {
            @Override
            public void run() {
                ivBack.setVisibility(View.VISIBLE);
            }
        }, 7800);
    }

    private void AnimationWithAlpa(ImageView iv) {
        iv.setVisibility(View.VISIBLE);
        AnimatorSet animatorSet = new AnimatorSet();//组合动画
        ObjectAnimator alpha = ObjectAnimator.ofFloat(iv, "alpha", 0f, 1f);


        animatorSet.setDuration(300);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.play(alpha);
        animatorSet.start();
    }

    /**
     * 聚合动画
     */
    private void AnimationWithItem(ImageView iv) {
        iv.setVisibility(View.VISIBLE);
        AnimatorSet animatorSet = new AnimatorSet();//组合动画
        ObjectAnimator transX1 = ObjectAnimator.ofFloat(iv, "X", iv.getX(), MyApplication.width / 2);
        ObjectAnimator transY1 = ObjectAnimator.ofFloat(iv, "Y", iv.getY(), MyApplication.height / 2);
        ObjectAnimator animator = ObjectAnimator.ofFloat(iv, "rotation", 0, 1080);
        CardMove cm = new CardMove(iv.getX(), iv.getY());
        cardMoves.add(cm);
        animatorSet.setDuration(500);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.play(transX1).with(transY1).with(animator);
        animatorSet.start();
    }

    /**
     * 分散动画
     */
    private void AnimationWithItemOff(ImageView iv, int i) {
        iv.setVisibility(View.VISIBLE);
        AnimatorSet animatorSet = new AnimatorSet();//组合动画
        ObjectAnimator transX1 = ObjectAnimator.ofFloat(iv, "X", MyApplication.width / 2, cardMoves.get(i).getX());
        ObjectAnimator transY1 = ObjectAnimator.ofFloat(iv, "Y", MyApplication.height / 2, cardMoves.get(i).getY());
        ObjectAnimator animator = ObjectAnimator.ofFloat(iv, "rotation", 0, 1080);

        animatorSet.setDuration(500);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.play(transX1).with(transY1).with(animator);
        animatorSet.start();
    }


    /**
     * 获取选中的一杯
     */
    private void showLastTea() {
        llBusness.setVisibility(View.GONE);
        llHot.setVisibility(View.GONE);
        ivBack.setVisibility(View.GONE);
        rl_drink.setVisibility(View.GONE);
        rl_select.setVisibility(View.VISIBLE);
        AnimatorSet animatorSet = new AnimatorSet();//组合动画
        ObjectAnimator animator = ObjectAnimator.ofFloat(ivSelect, "rotation", 0, 1080);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(ivSelect, "rotationX", 0, 720);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(ivSelect, "rotationY", 0, 720);

        animatorSet.setDuration(800);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.play(animator).with(animator1).with(animator2);
        animatorSet.start();

        rl_drink.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_drink_id.setVisibility(View.VISIBLE);
                tv_drink_name.setVisibility(View.VISIBLE);
            }
        },800);

    }


    /**
     * 显示事业爱情
     */
    private void showBusiness() {
        llBusness.setVisibility(View.VISIBLE);
        llHot.setVisibility(View.GONE);
        rl_drink.setVisibility(View.GONE);
        rl_select.setVisibility(View.GONE);
        ivBack.setVisibility(View.GONE);
        tv_drink_name.setVisibility(View.GONE);
        tv_drink_id.setVisibility(View.GONE);


    }


    /**
     * 选择冷饮热映
     */
    private void showHot() {
        llBusness.setVisibility(View.GONE);
        llHot.setVisibility(View.VISIBLE);
        rl_drink.setVisibility(View.GONE);
        rl_select.setVisibility(View.GONE);
        ivBack.setVisibility(View.GONE);
    }


    /**
     * 获取一杯奶茶数据
     */
    private void getlastCardData() {

        DailogUtil.showNetDialog(self);
        CommonApiProvider.getNetGetCommon(DomainUrl.UPLOAD_DATA, new CommonResponse<String>() {
            @Override
            public void onSuccess(CommonRequest request, String data) {
                super.onSuccess(request, data);
                Logger.e("xx_api", data + "");
            }

            @Override
            public void onFail(int errorCode, String errorMsg) {
                super.onFail(errorCode, errorMsg);
            }

            @Override
            public void onComplete() {
                super.onComplete();
                DailogUtil.closeNetDialog();
            }
        });
    }

}
