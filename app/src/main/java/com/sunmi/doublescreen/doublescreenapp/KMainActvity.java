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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sunmi.doublescreen.doublescreenapp.data.DataModel;
import com.sunmi.doublescreen.doublescreenapp.data.UPacketFactory;
import com.sunmi.doublescreen.doublescreenapp.utils.SharePreferenceUtil;
import com.sunmi.doublescreen.doublescreenapp.view.MyGridView;

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
public class KMainActvity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = KMainActvity.class.getSimpleName();
    //双屏通讯帮助类
    private DSKernel mDSKernel = null;

    private Handler myHandler;
    private final String picturePath = Environment.getExternalStorageDirectory().getPath() + "/img_01.png";
    private ProgressDialog dialog;

//    private AlertDialog  dialog ;

    private MyGridView gvHot;
    private MyGridView gvDrind;
    private TextView mainBtnClear;
    private ListView lvMenus;
    private Button main_btn_more, main_btn_pay;


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
        setContentView(R.layout.activity_kmain);
        initData();
        initSdk();
        initView();

    }


    private void initView() {
        main_btn_more = (Button) findViewById(R.id.main_btn_more);
        gvHot = (MyGridView) findViewById(R.id.gv_hot);
        gvDrind = (MyGridView) findViewById(R.id.gv_drind);
        mainBtnClear = (TextView) findViewById(R.id.main_btn_clear);
        lvMenus = (ListView) findViewById(R.id.lv_menus);
        main_btn_pay = (Button) findViewById(R.id.main_btn_pay);
        main_btn_pay.setOnClickListener(this);
        main_btn_more.setOnClickListener(this);
    }

    private void initData() {


        myHandler = new MyHandler(this);

        dialog = new ProgressDialog(this);
    }


    private void initSdk() {
        mDSKernel = DSKernel.newInstance();
        mDSKernel.init(this, mIConnectionCallback);
        mDSKernel.addReceiveCallback(mIReceiveCallback);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_btn_more:
                //TODO implement
                break;
            case R.id.main_btn_pay:
                //TODO implement
                break;

            //播放多个视频文件
            case R.id.btn_play_videos:
//                showDialog(getString(R.string.sending_videos));
//                long videosTaskId = (long) SharePreferenceUtil.getParam(this, videosKey, 0L);
//                checkVideosFileExist(videosTaskId);

                JSONObject jsonObjectapp2 = new JSONObject();
                try {
                    jsonObjectapp2.put("dataModel", "GETTEA");
                    jsonObjectapp2.put("data", "随机数" + Math.random() * 100);
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
                                Toast.makeText(KMainActvity.this, "主屏4收到" + data.data, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                break;

            default:
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
