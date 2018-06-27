package com.sunmi.doublescreen.doublescreenapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gprinter.aidl.GpService;
import com.gprinter.command.EscCommand;
import com.gprinter.command.GpCom;
import com.gprinter.command.GpUtils;
import com.gprinter.command.LabelCommand;
import com.gprinter.io.GpDevice;
import com.gprinter.service.GpPrintService;
import com.sunmi.doublescreen.doublescreenapp.adapter.GvAdapter;
import com.sunmi.doublescreen.doublescreenapp.adapter.MenusAdapter;
import com.sunmi.doublescreen.doublescreenapp.bean.OrderForm;
import com.sunmi.doublescreen.doublescreenapp.bean.OrderResult;
import com.sunmi.doublescreen.doublescreenapp.bean.ProductList;
import com.sunmi.doublescreen.doublescreenapp.bean.RandomProduct;
import com.sunmi.doublescreen.doublescreenapp.boolkeyprint.CustomerDiaplayActivity;
import com.sunmi.doublescreen.doublescreenapp.boolkeyprint.PrinterConnectDialog;
import com.sunmi.doublescreen.doublescreenapp.data.UPacketFactory;
import com.sunmi.doublescreen.doublescreenapp.dialog.AddFruitDialogFragment;
import com.sunmi.doublescreen.doublescreenapp.dialog.ItemDeleteDialogFragment;
import com.sunmi.doublescreen.doublescreenapp.dialog.PayDialog;
import com.sunmi.doublescreen.doublescreenapp.dialog.PrintDialogFragment;
import com.sunmi.doublescreen.doublescreenapp.network.config.DailogUtil;
import com.sunmi.doublescreen.doublescreenapp.network.config.DomainUrl;
import com.sunmi.doublescreen.doublescreenapp.network.service.CommonApiProvider;
import com.sunmi.doublescreen.doublescreenapp.network.service.CommonRequest;
import com.sunmi.doublescreen.doublescreenapp.network.service.CommonResponse;
import com.sunmi.doublescreen.doublescreenapp.smprinter.AidlUtil;
import com.sunmi.doublescreen.doublescreenapp.smprinter.BluetoothUtil;
import com.sunmi.doublescreen.doublescreenapp.smprinter.ESCUtil;
import com.sunmi.doublescreen.doublescreenapp.toast.ToastManager;
import com.sunmi.doublescreen.doublescreenapp.utils.DateUtils;
import com.sunmi.doublescreen.doublescreenapp.utils.DecimalMath;
import com.sunmi.doublescreen.doublescreenapp.utils.SharePreferenceUtil;
import com.sunmi.doublescreen.doublescreenapp.view.MyGridView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import sunmi.ds.DSKernel;
import sunmi.ds.callback.IConnectionCallback;
import sunmi.ds.callback.IReceiveCallback;
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
    MyApplication baseApp;
    private Handler myHandler;
    private final String picturePath = Environment.getExternalStorageDirectory().getPath() + "/img_01.png";
    private ProgressDialog dialog;

//    private AlertDialog  dialog ;

    private MyGridView gvHot;
    private MyGridView gvDrind;
    private TextView mainBtnClear;
    private ListView lvMenus;
    private Button main_btn_more, main_btn_pay, bt_mang;
    private TextView main_tv_price;

    /**
     * 主打
     */
    private List<ProductList.ProductsBean> hotDrinks = new ArrayList<>();
    /**
     * 普通
     */
    private List<ProductList.ProductsBean> comDrinks = new ArrayList<>();
    /**
     * 购物车
     */
    private List<ProductList.ProductsBean> sallDrinks = new ArrayList<>();

    private GvAdapter hotAdapter;
    private GvAdapter comAdapter;
    private MenusAdapter menusAdapter;

    private Activity self;
    private AddFruitDialogFragment dialogFragment;
    private ItemDeleteDialogFragment deleteDialog;
    private PrintDialogFragment printDialog;

    private PayDialog payDialog;

    private OrderResult newOrder = null;

    /**
     * 订单编号
     */
    private int orderNo;

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
        self = this;
        baseApp = (MyApplication) getApplication();
        setContentView(R.layout.activity_kmain);
        initData();
        initSdk();
        initView();
        initBooltoothPrint();
        AidlUtil.getInstance().initPrinter();//初始化打印机

    }


    private void initView() {
        main_btn_more = (Button) findViewById(R.id.main_btn_more);
        gvHot = (MyGridView) findViewById(R.id.gv_hot);
        gvDrind = (MyGridView) findViewById(R.id.gv_drind);
        mainBtnClear = (TextView) findViewById(R.id.main_btn_clear);
        lvMenus = (ListView) findViewById(R.id.lv_menus);
        main_btn_pay = (Button) findViewById(R.id.main_btn_pay);
        main_tv_price = (TextView) findViewById(R.id.main_tv_price);
        findViewById(R.id.main_btn_clear).setOnClickListener(this);
        findViewById(R.id.bt_mang).setOnClickListener(this);
        main_btn_pay.setOnClickListener(this);
        main_btn_more.setOnClickListener(this);

        hotAdapter = new GvAdapter(self, hotDrinks, 0);
        comAdapter = new GvAdapter(self, comDrinks, 0);
        menusAdapter = new MenusAdapter(self, sallDrinks);

        gvHot.setAdapter(hotAdapter);
        gvDrind.setAdapter(comAdapter);
        lvMenus.setAdapter(menusAdapter);

        deleteDialog = new ItemDeleteDialogFragment();
        deleteDialog.setListener(new ItemDeleteDialogFragment.DeleteListener() {
            @Override
            public void onDelete(int index) {
                sallDrinks.remove(index);
                menusAdapter.update(sallDrinks);
                updateTotalMoney();
            }
        });

        printDialog = new PrintDialogFragment();
        printDialog.setListener(new PrintDialogFragment.ActionListener() {
            @Override
            public void onAction(int index) {
                switch (index) {
                    case R.id.tv_booltooth:
                        openPortDialogueClicked();
                    default:
                        break;
                }
            }
        });


        dialogFragment = new AddFruitDialogFragment();
        dialogFragment.setListener(new AddFruitDialogFragment.AddListener() {
            @Override
            public void onAddResult(ProductList.ProductsBean bean) {
                sallDrinks.add(bean);
                menusAdapter.update(sallDrinks);
                updateTotalMoney();
            }
        });
        lvMenus.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("item", position);
                deleteDialog.setArguments(bundle);
                deleteDialog.show(getSupportFragmentManager(), "");


                return false;
            }
        });
        gvDrind.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("item", comDrinks.get(position).getCopy());
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getSupportFragmentManager(), "");
            }
        });
        gvHot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("item", hotDrinks.get(position).getCopy());
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getSupportFragmentManager(), "");
            }
        });
    }

    private void initData() {
        orderNo = (int) SharePreferenceUtil.getParam(this, "orderNo", 1);

        myHandler = new MyHandler(this);

        dialog = new ProgressDialog(this);


//        for (int i = 0; i < 11; i++) {
//            ProductList.ProductsBean bean = new ProductList.ProductsBean();
//            bean.setUid("123456");
//            bean.setName("金桔柠檬茶" + 1);
//            bean.setSellPrice((8 + i) + "");
//            hotDrinks.add(bean);
//            comDrinks.add(bean);
//        }

        hotDrinks.addAll(Config.hotPorducts);
        comDrinks.addAll(Config.comPorducts);


        payDialog = new PayDialog();
        payDialog.setCompleteListener(new PayDialog.OnCompleteListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onSuccess() {

            }

            @Override
            public void onComplete() {
//                menus.clear();
//                tvCarMoeny.setText("");
//                tvCar.setText("");
//                tvCar.setVisibility(View.GONE);
//                ivCar.setImageResource(R.drawable.car_gray);
//                bottomSheetLayout.dismissSheet();
//                btnPay.setBackgroundColor(Color.parseColor("#999999"));
////                if (printerPresenter != null) {
////                    printerPresenter.print(goods_data, payMode);
////                }
            }
        });

    }


    private void initSdk() {
        mDSKernel = DSKernel.newInstance();
        mDSKernel.init(this, mIConnectionCallback);
        mDSKernel.addReceiveCallback(mIReceiveCallback);
    }

    /**
     * 订单号+1
     */
    private void updateOrderNO() {
        orderNo++;
        SharePreferenceUtil.setParam(this, "orderNo", orderNo);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_mang:
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

                                RandomProduct product = new Gson().fromJson(data.data, RandomProduct.class);
                                ProductList.ProductsBean pb = getSelectProduct(product);
                                if (pb == null) {
                                    Toast.makeText(KMainActvity.this, "获取数据有误" + data.data, Toast.LENGTH_LONG).show();
                                    return;
                                }
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("item", getSelectProduct(product));
                                dialogFragment.setArguments(bundle);
                                dialogFragment.show(getSupportFragmentManager(), "");
                                Toast.makeText(KMainActvity.this, "主屏4收到" + data.data, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                break;
            case R.id.main_btn_clear:
                sallDrinks.clear();
                menusAdapter.update(sallDrinks);
                updateTotalMoney();
                break;
            case R.id.main_btn_more:
                //TODO implement
                printDialog.setArguments(null);
                printDialog.show(getSupportFragmentManager(), "");
                break;
            case R.id.main_btn_pay:
//                if (sallDrinks.size() > 0) {
//                    Bundle bundle = new Bundle();
//                    bundle.putString("MONEY", "96.00");
//                    payDialog.setArguments(bundle);
//                    payDialog.show(getSupportFragmentManager(), "payDialog");
//                }

                printTestClicked();


                break;
            //播放多个视频文件
            case R.id.btn_play_videos:
//                showDialog(getString(R.string.sending_videos));
//                long videosTaskId = (long) SharePreferenceUtil.getParam(this, videosKey, 0L);
//                checkVideosFileExist(videosTaskId);

//                JSONObject jsonObjectapp2 = new JSONObject();
//                try {
//                    jsonObjectapp2.put("dataModel", "GETTEA");
//                    jsonObjectapp2.put("data", "随机数" + Math.random() * 100);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                DataPacket packetapp2 = new DataPacket.Builder(DSData.DataType.CMD).recPackName(getPackageName()).data(jsonObjectapp2.toString())
//                        .addCallback(null).build();
//
//
//                mDSKernel.sendQuery(packetapp2, new QueryCallback() {
//                    @Override
//                    public void onReceiveData(final DSData data) {
//                        Log.d("highsixty", "onReceiveData: ------------>" + data.data);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(KMainActvity.this, "主屏4收到" + data.data, Toast.LENGTH_LONG).show();
//                            }
//                        });
//                    }
//                });
                break;

            default:
                break;
        }
    }


    /**
     * 接收返回的产品
     *
     * @param product
     * @return
     */
    private ProductList.ProductsBean getSelectProduct(RandomProduct product) {
        if (comDrinks.size() > 0) {
            for (int i = 0; i < comDrinks.size(); i++) {
                ProductList.ProductsBean bean = comDrinks.get(i);
                if (product.getBarcode().equals(bean.getBarcode())) {
                    ProductList.ProductsBean newProduct = bean.getCopy();
                    newProduct.setHotType(product.getHotType());
                    newProduct.setUrl(product.getQrurl());
                    newProduct.setSign(product.getSign());
                    return newProduct;
                }

            }


        }

        return null;
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
     * 更新合计
     */
    private void updateTotalMoney() {
        String money = "0";

        for (int i = 0; i < sallDrinks.size(); i++) {
            ProductList.ProductsBean bean = sallDrinks.get(i);
            money = DecimalMath.add(money, DecimalMath.plus(bean.getCount(), bean.getSellPrice()));
        }
        main_tv_price.setText("￥" + money);
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


    /**
     * 新增订单
     */
    private void postForm() {
        if (sallDrinks.size() == 0) {
            ToastManager.show("购物车是空的，请先选择商品");
            return;
        }


        DailogUtil.showNetDialog(self);

        OrderForm orderForm = new OrderForm();
        try {
            orderForm.setPayMethod("Cash");//CustomerBalance 会员卡
            orderForm.setCustomerNumber("");
            orderForm.setOrderDateTime(DateUtils.getCurrentdata());
            orderForm.setContactAddress("");
            orderForm.setContactName("");
            orderForm.setContactTel("");

            List<OrderForm.ItemsBean> items = new ArrayList<>();
            for (int i = 0; i < sallDrinks.size(); i++) {
                ProductList.ProductsBean pbean = sallDrinks.get(i);
                OrderForm.ItemsBean ibean = new OrderForm.ItemsBean();
                ibean.setProductUid(pbean.getUid());
                ibean.setComment(pbean.getName());
                ibean.setManualSellPrice(pbean.getSellPrice());
                ibean.setQuantity(pbean.getCount() + "");
                items.add(ibean);
            }
            orderForm.setItems(items);
        } catch (Exception e) {
            e.printStackTrace();
        }

        CommonApiProvider.getNetPostCommon(DomainUrl.Create_Order, "", new Gson().toJson(orderForm), new CommonResponse<String>() {
            @Override
            public void onSuccess(CommonRequest request, String data) {
                super.onSuccess(request, data);
                Logger.e("xx_api", data + "");
                if (!TextUtils.isEmpty(data) && data.length() > 2) {
                    try {
                        JSONObject result = new JSONObject(data);
                        if ("success".equals(result.optString("status"))) {
                            OrderResult or = new Gson().fromJson(data, OrderResult.class);
                            newOrder = or;

                        } else {
                            ToastManager.show("订单生成失败");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

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


    /**
     * 完成订单
     */
    private void getCompleteOrder() {
        if (newOrder == null) {
            ToastManager.show("订单不存在");
            return;
        }
        DailogUtil.showNetDialog(self);
        JSONObject ob = new JSONObject();
        try {
            ob.put("orderNo", newOrder.getData().getOrderNo());
            ob.put("shouldAddTicket", true);

        } catch (Exception e) {
            e.printStackTrace();
        }

        CommonApiProvider.getNetGetCommon(DomainUrl.Complete_Order, new CommonResponse<String>() {
            @Override
            public void onSuccess(CommonRequest request, String data) {
                super.onSuccess(request, data);
                Logger.e("xx_api", data + "");
                if (!TextUtils.isEmpty(data) && data.length() > 2) {
                    try {
                        JSONObject result = new JSONObject(data);
                        if ("success".equals(result.optString("status"))) {
                            ToastManager.show("订单完成");
                        } else {
                            ToastManager.show("完成订单失败");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

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


    /**
     * 获取随机卡片数据
     */
    private void getCardsData() {

        DailogUtil.showNetDialog(self);
        CommonApiProvider.getNetGetCommon(DomainUrl.Choose_last_product, new CommonResponse<String>() {
            @Override
            public void onSuccess(CommonRequest request, String data) {
                super.onSuccess(request, data);
                Logger.e("xx_api", data + "");
                if (!TextUtils.isEmpty(data) && data.length() > 2) {
                    RandomProduct product = new Gson().fromJson(data, RandomProduct.class);
                }

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

    //=============================================热敏打印机=====================================================

    private GpService mGpService = null;
    public static final String CONNECT_STATUS = "connect.status";
    private static final String DEBUG_TAG = "MainActivity";
    private PrinterServiceConnection conn = null;
    private int mPrinterIndex = 0;
    private int mTotalCopies = 0;
    private static final int MAIN_QUERY_PRINTER_STATUS = 0xfe;
    private static final int REQUEST_PRINT_LABEL = 0xfd;
    private static final int REQUEST_PRINT_RECEIPT = 0xfc;

    class PrinterServiceConnection implements ServiceConnection {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("ServiceConnection", "onServiceDisconnected() called");
            mGpService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mGpService = GpService.Stub.asInterface(service);
        }
    }


    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("TAG", action);
            // GpCom.ACTION_DEVICE_REAL_STATUS 为广播的IntentFilter
            if (action.equals(GpCom.ACTION_DEVICE_REAL_STATUS)) {

                // 业务逻辑的请求码，对应哪里查询做什么操作
                int requestCode = intent.getIntExtra(GpCom.EXTRA_PRINTER_REQUEST_CODE, -1);
                // 判断请求码，是则进行业务操作
                if (requestCode == MAIN_QUERY_PRINTER_STATUS) {

                    int status = intent.getIntExtra(GpCom.EXTRA_PRINTER_REAL_STATUS, 16);
                    String str;
                    if (status == GpCom.STATE_NO_ERR) {
                        str = "打印机正常";
                    } else {
                        str = "打印机 ";
                        if ((byte) (status & GpCom.STATE_OFFLINE) > 0) {
                            str += "脱机";
                        }
                        if ((byte) (status & GpCom.STATE_PAPER_ERR) > 0) {
                            str += "缺纸";
                        }
                        if ((byte) (status & GpCom.STATE_COVER_OPEN) > 0) {
                            str += "打印机开盖";
                        }
                        if ((byte) (status & GpCom.STATE_ERR_OCCURS) > 0) {
                            str += "打印机出错";
                        }
                        if ((byte) (status & GpCom.STATE_TIMES_OUT) > 0) {
                            str += "查询超时";
                        }
                    }

                    Toast.makeText(getApplicationContext(), "打印机：" + mPrinterIndex + " 状态：" + str, Toast.LENGTH_SHORT)
                            .show();
                } else if (requestCode == REQUEST_PRINT_LABEL) {
                    int status = intent.getIntExtra(GpCom.EXTRA_PRINTER_REAL_STATUS, 16);
                    if (status == GpCom.STATE_NO_ERR) {
                        sendLabel();
                    } else {
                        Toast.makeText(self, "query printer status error", Toast.LENGTH_SHORT).show();
                    }
                } else if (requestCode == REQUEST_PRINT_RECEIPT) {
                    int status = intent.getIntExtra(GpCom.EXTRA_PRINTER_REAL_STATUS, 16);
                    if (status == GpCom.STATE_NO_ERR) {
                        sendReceipt();
                    } else {
                        Toast.makeText(self, "query printer status error", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (action.equals(GpCom.ACTION_RECEIPT_RESPONSE)) {
                if (--mTotalCopies > 0) {
                    sendReceiptWithResponse();
                }
            } else if (action.equals(GpCom.ACTION_LABEL_RESPONSE)) {
                byte[] data = intent.getByteArrayExtra(GpCom.EXTRA_PRINTER_LABEL_RESPONSE);
                int cnt = intent.getIntExtra(GpCom.EXTRA_PRINTER_LABEL_RESPONSE_CNT, 1);
                String d = new String(data, 0, cnt);
                /**
                 * 这里的d的内容根据RESPONSE_MODE去判断返回的内容去判断是否成功，具体可以查看标签编程手册SET
                 * RESPONSE指令
                 * 该sample中实现的是发一张就返回一次,这里返回的是{00,00001}。这里的对应{Status,######,ID}
                 * 所以我们需要取出STATUS
                 */
                Log.d("LABEL RESPONSE", d);

                if (--mTotalCopies > -1 && d.charAt(1) == 0x00) {
                    sendLabelWithResponse(mTotalCopies);
                }
            }
        }
    };


    /**
     * 初始化热敏打印机
     */
    private void initBooltoothPrint() {
        connection();

        // 注册实时状态查询广播
        registerReceiver(mBroadcastReceiver, new IntentFilter(GpCom.ACTION_DEVICE_REAL_STATUS));
        /**
         * 票据模式下，可注册该广播，在需要打印内容的最后加入addQueryPrinterStatus()，在打印完成后会接收到
         * action为GpCom.ACTION_DEVICE_STATUS的广播，特别用于连续打印，
         * 可参照该sample中的sendReceiptWithResponse方法与广播中的处理
         **/
        registerReceiver(mBroadcastReceiver, new IntentFilter(GpCom.ACTION_RECEIPT_RESPONSE));
        /**
         * 标签模式下，可注册该广播，在需要打印内容的最后加入addQueryPrinterStatus(RESPONSE_MODE mode)
         * ，在打印完成后会接收到，action为GpCom.ACTION_LABEL_RESPONSE的广播，特别用于连续打印，
         * 可参照该sample中的sendLabelWithResponse方法与广播中的处理
         **/
        registerReceiver(mBroadcastReceiver, new IntentFilter(GpCom.ACTION_LABEL_RESPONSE));
    }


    private void connection() {
        conn = new PrinterServiceConnection();
        Intent intent = new Intent(this, GpPrintService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE); // bindService
    }

    public boolean[] getConnectState() {
        boolean[] state = new boolean[GpPrintService.MAX_PRINTER_CNT];
        for (int i = 0; i < GpPrintService.MAX_PRINTER_CNT; i++) {
            state[i] = false;
        }
        for (int i = 0; i < GpPrintService.MAX_PRINTER_CNT; i++) {
            try {
                if (mGpService.getPrinterConnectStatus(i) == GpDevice.STATE_CONNECTED) {
                    state[i] = true;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return state;
    }

    public void openPortDialogueClicked() {
        if (mGpService == null) {
            Toast.makeText(this, "Print Service is not start, please check it", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(DEBUG_TAG, "openPortConfigurationDialog ");
        Intent intent = new Intent(this, PrinterConnectDialog.class);
        boolean[] state = getConnectState();
        intent.putExtra(CONNECT_STATUS, state);
        this.startActivity(intent);
    }

    public void printTestPageClicked(View view) {
        try {
            int rel = mGpService.printeTestPage(mPrinterIndex); //
            Log.i("ServiceConnection", "rel " + rel);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
    }

    public void getPrinterStatusClicked(View view) {
        try {
            mTotalCopies = 0;
            mGpService.queryPrinterStatus(mPrinterIndex, 500, MAIN_QUERY_PRINTER_STATUS);
        } catch (RemoteException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void getPrinterCommandTypeClicked(View view) {
        try {
            int type = mGpService.getPrinterCommandType(mPrinterIndex);
            if (type == GpCom.ESC_COMMAND) {
                Toast.makeText(getApplicationContext(), "打印机使用ESC命令", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "打印机使用TSC命令", Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void printArabicReceiptClicked(View view) {
        EscCommand esc = new EscCommand();
        // init printer
        esc.addInitializePrinter();
        // cancel Kanji
        esc.addCancelKanjiMode();
        // set paper width
        GpUtils.setPaperWidth(GpUtils.PAPER_58_WIDTH);
        // select codepage which is arabic
        esc.addSelectCodePage(EscCommand.CODEPAGE.ARABIC);
        esc.addArabicText("الهاتف المحمول معطوب و يحتاج إلى إصلاح");
        esc.addPrintAndFeedLines((byte) 5);
        Vector<Byte> datas = esc.getCommand(); // send data
        byte[] bytes = GpUtils.ByteTo_byte(datas);
        String sss = Base64.encodeToString(
                new byte[]{0x1f, 0x1b, 0x1f, (byte) 0xa5, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x33}, Base64.DEFAULT);
        int rs;
        try {
            rs = mGpService.sendEscCommand(mPrinterIndex, sss);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rs];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    void sendReceipt() {

        EscCommand esc = new EscCommand();
        esc.addInitializePrinter();
        esc.addPrintAndFeedLines((byte) 3);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印居中
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);// 设置为倍高倍宽
        esc.addText("Sample\n"); // 打印文字
        esc.addPrintAndLineFeed();

        /* 打印文字 */
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);// 取消倍高倍宽
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);// 设置打印左对齐
        esc.addText("Print text\n"); // 打印文字
        esc.addText("Welcome to use SMARNET printer!\n"); // 打印文字

        /* 打印繁体中文 需要打印机支持繁体字库 */
        String message = "佳博智匯票據打印機\n";
        // esc.addText(message,"BIG5");
        esc.addText(message, "GB2312");
        esc.addPrintAndLineFeed();

        /* 绝对位置 具体详细信息请查看GP58编程手册 */
        esc.addText("智汇");
        esc.addSetHorAndVerMotionUnits((byte) 7, (byte) 0);
        esc.addSetAbsolutePrintPosition((short) 6);
        esc.addText("网络");
        esc.addSetAbsolutePrintPosition((short) 10);
        esc.addText("设备");
        esc.addPrintAndLineFeed();

        /* 打印图片 */
        esc.addText("Print bitmap!\n"); // 打印文字
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.gprinter);
        esc.addRastBitImage(b, 384, 0); // 打印图片

        /* 打印一维条码 */
        esc.addText("Print code128\n"); // 打印文字
        esc.addSelectPrintingPositionForHRICharacters(EscCommand.HRI_POSITION.BELOW);//
        // 设置条码可识别字符位置在条码下方
        esc.addSetBarcodeHeight((byte) 60); // 设置条码高度为60点
        esc.addSetBarcodeWidth((byte) 1); // 设置条码单元宽度为1
        esc.addCODE128(esc.genCodeB("SMARNET")); // 打印Code128码
        esc.addPrintAndLineFeed();

        /*
         * QRCode命令打印 此命令只在支持QRCode命令打印的机型才能使用。 在不支持二维码指令打印的机型上，则需要发送二维条码图片
         */
        esc.addText("Print QRcode\n"); // 打印文字
        esc.addSelectErrorCorrectionLevelForQRCode((byte) 0x31); // 设置纠错等级
        esc.addSelectSizeOfModuleForQRCode((byte) 3);// 设置qrcode模块大小
        esc.addStoreQRCodeData("www.smarnet.cc");// 设置qrcode内容
        esc.addPrintQRCode();// 打印QRCode
        esc.addPrintAndLineFeed();

        /* 打印文字 */
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印左对齐
        esc.addText("Completed!\r\n"); // 打印结束
        // 开钱箱
        esc.addGeneratePlus(LabelCommand.FOOT.F5, (byte) 255, (byte) 255);
        esc.addPrintAndFeedLines((byte) 8);

        Vector<Byte> datas = esc.getCommand(); // 发送数据
        byte[] bytes = GpUtils.ByteTo_byte(datas);
        String sss = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rs;
        try {
            rs = mGpService.sendEscCommand(mPrinterIndex, sss);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rs];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    void sendReceiptWithResponse() {
        EscCommand esc = new EscCommand();
        esc.addInitializePrinter();
        esc.addPrintAndFeedLines((byte) 3);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印居中
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);// 设置为倍高倍宽
        esc.addText("Sample\n"); // 打印文字
        esc.addPrintAndLineFeed();

        /* 打印文字 */
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);// 取消倍高倍宽
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);// 设置打印左对齐
        esc.addText("Print text\n"); // 打印文字
        esc.addText("Welcome to use SMARNET printer!\n"); // 打印文字

        /* 打印繁体中文 需要打印机支持繁体字库 */
        String message = "佳博智匯票據打印機\n";
        // esc.addText(message,"BIG5");
        esc.addText(message, "GB2312");
        esc.addPrintAndLineFeed();

        /* 绝对位置 具体详细信息请查看GP58编程手册 */
        esc.addText("智汇");
        esc.addSetHorAndVerMotionUnits((byte) 7, (byte) 0);
        esc.addSetAbsolutePrintPosition((short) 6);
        esc.addText("网络");
        esc.addSetAbsolutePrintPosition((short) 10);
        esc.addText("设备");
        esc.addPrintAndLineFeed();

        /* 打印图片 */
        // esc.addText("Print bitmap!\n"); // 打印文字
        // Bitmap b = BitmapFactory.decodeResource(getResources(),
        // R.drawable.gprinter);
        // esc.addRastBitImage(b, 384, 0); // 打印图片

        /* 打印一维条码 */
        esc.addText("Print code128\n"); // 打印文字
        esc.addSelectPrintingPositionForHRICharacters(EscCommand.HRI_POSITION.BELOW);//
        // 设置条码可识别字符位置在条码下方
        esc.addSetBarcodeHeight((byte) 60); // 设置条码高度为60点
        esc.addSetBarcodeWidth((byte) 1); // 设置条码单元宽度为1
        esc.addCODE128(esc.genCodeB("SMARNET")); // 打印Code128码
        esc.addPrintAndLineFeed();

        /*
         * QRCode命令打印 此命令只在支持QRCode命令打印的机型才能使用。 在不支持二维码指令打印的机型上，则需要发送二维条码图片
         */
        esc.addText("Print QRcode\n"); // 打印文字
        esc.addSelectErrorCorrectionLevelForQRCode((byte) 0x31); // 设置纠错等级
        esc.addSelectSizeOfModuleForQRCode((byte) 3);// 设置qrcode模块大小
        esc.addStoreQRCodeData("www.smarnet.cc");// 设置qrcode内容
        esc.addPrintQRCode();// 打印QRCode
        esc.addPrintAndLineFeed();

        /* 打印文字 */
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印左对齐
        esc.addText("Completed!\r\n"); // 打印结束
        // 开钱箱
        esc.addGeneratePlus(LabelCommand.FOOT.F5, (byte) 255, (byte) 255);
        esc.addPrintAndFeedLines((byte) 8);

        // 加入查询打印机状态，打印完成后，此时会接收到GpCom.ACTION_DEVICE_STATUS广播
        esc.addQueryPrinterStatus();

        Vector<Byte> datas = esc.getCommand(); // 发送数据
        byte[] bytes = GpUtils.ByteTo_byte(datas);
        String sss = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rs;
        try {
            rs = mGpService.sendEscCommand(mPrinterIndex, sss);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rs];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    void sendLabel() {
        LabelCommand tsc = new LabelCommand();
        tsc.addSize(60, 60); // 设置标签尺寸，按照实际尺寸设置
        tsc.addGap(0); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addDirection(LabelCommand.DIRECTION.BACKWARD, LabelCommand.MIRROR.NORMAL);// 设置打印方向
        tsc.addReference(0, 0);// 设置原点坐标
        tsc.addTear(EscCommand.ENABLE.ON); // 撕纸模式开启
        tsc.addCls();// 清除打印缓冲区

        tsc.addText(20, 30, LabelCommand.FONTTYPE.KOREAN, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "조선말");
        tsc.addText(100, 30, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "简体字");
        tsc.addText(180, 30, LabelCommand.FONTTYPE.TRADITIONAL_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "繁體字");

        // 绘制图片
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.gprinter);
        tsc.addBitmap(20, 60, LabelCommand.BITMAP_MODE.OVERWRITE, b.getWidth(), b);
        //绘制二维码
        tsc.addQRCode(105, 75, LabelCommand.EEC.LEVEL_L, 5, LabelCommand.ROTATION.ROTATION_0, " www.smarnet.cc");
        // 绘制一维条码
        tsc.add1DBarcode(50, 350, LabelCommand.BARCODETYPE.CODE128, 100, LabelCommand.READABEL.EANBEL, LabelCommand.ROTATION.ROTATION_0, "SMARNET");
        tsc.addPrint(1, 1); // 打印标签
        tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
        Vector<Byte> datas = tsc.getCommand(); // 发送数据
        byte[] bytes = GpUtils.ByteTo_byte(datas);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rel;
        try {
            rel = mGpService.sendLabelCommand(mPrinterIndex, str);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    void sendLabelWithResponse(int index) {
        ProductList.ProductsBean bean = sallDrinks.get(index);

        LabelCommand tsc = new LabelCommand();
        tsc.addSize(40, 30); // 设置标签尺寸，按照实际尺寸设置
        tsc.addGap(5); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addDirection(LabelCommand.DIRECTION.FORWARD, LabelCommand.MIRROR.NORMAL);// 设置打印方向
        // 开启带Response的打印，用于连续打印
        tsc.addQueryPrinterStatus(LabelCommand.RESPONSE_MODE.ON);
        tsc.addReference(5, 5);// 设置原点坐标
        tsc.addTear(EscCommand.ENABLE.ON); // 撕纸模式开启
        tsc.addCls();// 清除打印缓冲区
        // 绘制简体中文
        tsc.addText(40, 10, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                orderNo + " " + bean.getName() + " 冰  中杯");
//        // 绘制图片
//        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.gprinter);
//        tsc.addBitmap(20, 50, BITMAP_MODE.OVERWRITE, b.getWidth(), b);

        tsc.addQRCode(50, 40, LabelCommand.EEC.LEVEL_L, 5, LabelCommand.ROTATION.ROTATION_0, bean.getUrl() + "www.baidu.com");
        // 绘制一维条码
//        tsc.add1DBarcode(20, 250, BARCODETYPE.CODE128, 100, READABEL.EANBEL, ROTATION.ROTATION_0, "SMARNET");
        tsc.addPrint(1, 1); // 打印标签
        tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);


        Vector<Byte> datas = tsc.getCommand(); // 发送数据
        byte[] bytes = GpUtils.ByteTo_byte(datas);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rel;
        try {
            rel = mGpService.sendLabelCommand(mPrinterIndex, str);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void printReceiptClicked(View view) {
        try {
            int type = mGpService.getPrinterCommandType(mPrinterIndex);
            if (type == GpCom.ESC_COMMAND) {
                mGpService.queryPrinterStatus(mPrinterIndex, 1000, REQUEST_PRINT_RECEIPT);
            } else {
                Toast.makeText(this, "Printer is not receipt mode", Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
    }

    public void printLabelClicked(View view) {
        try {
            int type = mGpService.getPrinterCommandType(mPrinterIndex);
            if (type == GpCom.LABEL_COMMAND) {
                mGpService.queryPrinterStatus(mPrinterIndex, 1000, REQUEST_PRINT_LABEL);
            } else {
                Toast.makeText(this, "Printer is not label mode", Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 打印热敏小票
     */
    public void printTestClicked() {
        try {

            if (sallDrinks.size() == 0) {
                return;
            }
//            int type = mGpService.getPrinterCommandType(mPrinterIndex);
//            if (type == GpCom.ESC_COMMAND) {
//                mTotalCopies = sallDrinks.size() - 1;
//                sendReceiptWithResponse();
//            } else if (type == GpCom.LABEL_COMMAND) {
            mTotalCopies = sallDrinks.size() - 1;
            sendLabelWithResponse(mTotalCopies);
//            } else {
//                Toast.makeText(this, "Printer is not receipt mode", Toast.LENGTH_SHORT).show();
//            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void customerDisplayerClicked(View view) {
        Intent intent = new Intent(this, CustomerDiaplayActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (conn != null) {
            unbindService(conn); // unBindService
        }
        unregisterReceiver(mBroadcastReceiver);
    }
//=============================================商米打印===============================


    /**
     * 打印订单信息
     *
     * @return
     */
    public String getPrintContent() {

        String contentStr = "订单号：" + orderNo + "\n" +
                "时间：" + DateUtils.getCurrentdata() + "\n" +
                "=================================\n" +
                "订单详情：\n";
        for (int i = 0; i < sallDrinks.size(); i++) {
            ProductList.ProductsBean bean = sallDrinks.get(i);
            contentStr = contentStr + bean.getName() + "   " + MenusAdapter.getSize(bean.getBoxType()) + "  " + MenusAdapter.getHot(bean.getHotType()) + "  X" + bean.getCount() + "\n";
        }

        contentStr = contentStr + "=================================";
        return contentStr;

    }


    public void smPrint(String content) {

        if (baseApp.isAidl()) {
            AidlUtil.getInstance().printText(content, 14, false, false);
        } else {
            printByBluTooth(content);
        }
    }

    private void printByBluTooth(String content) {
//        try {
////            if (isBold) {
////                BluetoothUtil.sendData(ESCUtil.boldOn());
////            } else {
//                BluetoothUtil.sendData(ESCUtil.boldOff());
////            }
//
////            if (isUnderLine) {
////                BluetoothUtil.sendData(ESCUtil.underlineWithOneDotWidthOn());
////            } else {
//                BluetoothUtil.sendData(ESCUtil.underlineOff());
////            }
//
//            if (record < 17) {
//                BluetoothUtil.sendData(ESCUtil.singleByte());
//                BluetoothUtil.sendData(ESCUtil.setCodeSystemSingle(codeParse(record)));
//            } else {
//                BluetoothUtil.sendData(ESCUtil.singleByteOff());
//                BluetoothUtil.sendData(ESCUtil.setCodeSystem(codeParse(record)));
//            }
//
//            BluetoothUtil.sendData(content.getBytes(mStrings[record]));
//            BluetoothUtil.sendData(ESCUtil.nextLine(3));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private byte codeParse(int value) {
        byte res = 0x00;
        switch (value) {
            case 0:
                res = 0x00;
                break;
            case 1:
            case 2:
            case 3:
            case 4:
                res = (byte) (value + 1);
                break;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
                res = (byte) (value + 8);
                break;
            case 12:
                res = 21;
                break;
            case 13:
                res = 33;
                break;
            case 14:
                res = 34;
                break;
            case 15:
                res = 36;
                break;
            case 16:
                res = 37;
                break;
            case 17:
            case 18:
            case 19:
                res = (byte) (value - 17);
                break;
            case 20:
                res = (byte) 0xff;
                break;
        }
        return (byte) res;
    }

}
