package com.sunmi.doublescreen.doublescreenapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sunmi.doublescreen.doublescreenapp.adapter.GvAdapter;
import com.sunmi.doublescreen.doublescreenapp.adapter.MenusAdapter;
import com.sunmi.doublescreen.doublescreenapp.bean.OrderForm;
import com.sunmi.doublescreen.doublescreenapp.bean.OrderResult;
import com.sunmi.doublescreen.doublescreenapp.bean.ProductList;
import com.sunmi.doublescreen.doublescreenapp.bean.RandomProduct;
import com.sunmi.doublescreen.doublescreenapp.data.DataModel;
import com.sunmi.doublescreen.doublescreenapp.data.UPacketFactory;
import com.sunmi.doublescreen.doublescreenapp.dialog.AddFruitDialogFragment;
import com.sunmi.doublescreen.doublescreenapp.dialog.ItemDeleteDialogFragment;
import com.sunmi.doublescreen.doublescreenapp.dialog.PayDialog;
import com.sunmi.doublescreen.doublescreenapp.network.config.DailogUtil;
import com.sunmi.doublescreen.doublescreenapp.network.config.DomainUrl;
import com.sunmi.doublescreen.doublescreenapp.network.service.CommonApiProvider;
import com.sunmi.doublescreen.doublescreenapp.network.service.CommonRequest;
import com.sunmi.doublescreen.doublescreenapp.network.service.CommonResponse;
import com.sunmi.doublescreen.doublescreenapp.toast.ToastManager;
import com.sunmi.doublescreen.doublescreenapp.utils.DateUtils;
import com.sunmi.doublescreen.doublescreenapp.utils.DecimalMath;
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

    private PayDialog payDialog;

    private OrderResult newOrder = null;

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
                break;
            case R.id.main_btn_pay:
                if (sallDrinks.size() > 0) {
                    Bundle bundle = new Bundle();
                    bundle.putString("MONEY", "96.00");
                    payDialog.setArguments(bundle);
                    payDialog.show(getSupportFragmentManager(), "payDialog");
                }
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
}
