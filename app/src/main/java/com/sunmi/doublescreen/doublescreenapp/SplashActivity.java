package com.sunmi.doublescreen.doublescreenapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sunmi.doublescreen.doublescreenapp.bean.ProductList;
import com.sunmi.doublescreen.doublescreenapp.network.config.DailogUtil;
import com.sunmi.doublescreen.doublescreenapp.network.config.DomainUrl;
import com.sunmi.doublescreen.doublescreenapp.network.service.CommonApiProvider;
import com.sunmi.doublescreen.doublescreenapp.network.service.CommonRequest;
import com.sunmi.doublescreen.doublescreenapp.network.service.CommonResponse;
import com.sunmi.doublescreen.doublescreenapp.utils.DateUtils;
import com.sunmi.doublescreen.doublescreenapp.utils.SharePreferenceUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    public static final boolean isMain = Build.MODEL.equals("t1host") || Build.MODEL.equals("T1-G");
    private ImageView iv;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private final int permissionRequestCode = 0x1;
    private AlertDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        iv = (ImageView) findViewById(R.id.iv);
        if (isMain) {
            iv.setImageResource(R.drawable.img_05);
        } else {
            iv.setImageResource(R.drawable.img_03);
        }

        Object today=SharePreferenceUtil.getParam(this,"today", DateUtils.getCurrentDay());
        if (today==null)
        {
            SharePreferenceUtil.setParam(this,"today",DateUtils.getCurrentDay());
            SharePreferenceUtil.setParam(this,"orderNo",1);
        }else {
            String todayStr=today.toString();
            if (!todayStr.equals(DateUtils.getCurrentDay()))
            {
                SharePreferenceUtil.setParam(this,"today",DateUtils.getCurrentDay());
                SharePreferenceUtil.setParam(this,"orderNo",1);
            }
        }


        /**
         * 版本判断，大于23的时候才需要动态申请权限
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            /**
             * 判断该权限是否已经获取
             */
            int i = ContextCompat.checkSelfPermission(this, permissions[0]);
            /**
             * PERMISSION_GRANTED  已经获取  PERMISSION_DENIED 拒绝
             */
            if (i != PackageManager.PERMISSION_GRANTED) {
                /**
                 * 如果为获取权限，弹框提示用户当前应用需要该权限的意图
                 */
                showDialogTipUserRequestPermission();
            } else {
                transfer();
            }
        }
    }

    private void transfer() {
        getProducts();

    }


    /**
     * 获取产品列表
     */
    private void getProducts() {
        CommonApiProvider.getNetGetCommon(DomainUrl.Product_List, new CommonResponse<String>() {
            @Override
            public void onSuccess(CommonRequest request, String data) {
                super.onSuccess(request, data);
                if (!TextUtils.isEmpty(data) && data.length() > 2) {
                    ProductList productList = new Gson().fromJson(data, ProductList.class);
                    ruleProducts(productList);
                }
            }

            @Override
            public void onFail(int errorCode, String errorMsg) {
                super.onFail(errorCode, errorMsg);
            }

            @Override
            public void onComplete() {
                super.onComplete();
            }
        });
    }


    /**
     * 整理热门产品和普通产品品
     */
    private void ruleProducts(ProductList productList) {
        Config.hotPorducts.clear();
        Config.comPorducts.clear();
        if (productList == null || productList.getCategorys() == null || productList.getProducts() == null) {
            return;
        }
        List<String> hotCategory = new ArrayList<>();
        List<String> comCategory = new ArrayList<>();

        for (int i = 0; i < productList.getCategorys().size(); i++) {
            ProductList.CategorysBean categorysBean = productList.getCategorys().get(i);
            if (!TextUtils.isEmpty(categorysBean.getName()) && categorysBean.getName().contains("主打")) {
                hotCategory.add(categorysBean.getUid());
            } else {
                comCategory.add(categorysBean.getUid());
            }
        }


        for (int j = 0; j < productList.getProducts().size(); j++) {
            ProductList.ProductsBean productsBean = productList.getProducts().get(j);
            if (hotCategory.contains(productsBean.getCategoryUid())) {
                Config.hotPorducts.add(productsBean);
            } else {
                Config.comPorducts.add(productsBean);
            }
        }


        goToNext();


    }


    private void goToNext() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (isMain) {
                    intent = new Intent(SplashActivity.this, KMainActvity.class);
                } else {
                    intent = new Intent(SplashActivity.this, TaroActivity.class);
                }

                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    // 提示用户该请求权限的弹出框
    private void showDialogTipUserRequestPermission() {

        new AlertDialog.Builder(this)
                .setTitle(R.string.permission_dialog_tip)
                .setMessage(R.string.permission_dialog_message)
                .setPositiveButton(R.string.granted, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startRequestPermission();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.denied, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).setCancelable(false).show();
    }

    /**
     * 开始请求权限
     */
    private void startRequestPermission() {
        ActivityCompat.requestPermissions(this, permissions, permissionRequestCode);
    }

    /**
     * 用户权限申请回调方法
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == permissionRequestCode) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    /**
                     * 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                     */
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!b) {
                        // 用户还是想用我的 APP 的
                        // 提示用户去应用设置界面手动开启权限
                        showDialogTipUserGoToAppSettting();
                    } else {
                        finish();
                    }
                } else {
                    Toast.makeText(this, R.string.acquire_permission_success, Toast.LENGTH_SHORT).show();
                    if (isMain) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                initAssets();
                            }
                        }).start();
                    }
                    transfer();
                }
            }
        }
    }
    // 提示用户去应用设置界面手动开启权限

    private void showDialogTipUserGoToAppSettting() {
        dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.permission_denied)
                .setMessage(R.string.goto_setting)
                .setPositiveButton(R.string.allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 跳转到应用设置界面
                        goToAppSetting();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();
    }

    // 跳转到当前应用的设置界面
    private void goToAppSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 123);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 检查该权限是否已经获取
                int i = ContextCompat.checkSelfPermission(this, permissions[0]);
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (i != PackageManager.PERMISSION_GRANTED) {
                    // 提示用户应该去应用设置界面手动开启权限
                    showDialogTipUserGoToAppSettting();
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Toast.makeText(this, getString(R.string.acquire_permission_success), Toast.LENGTH_SHORT).show();
                    if (isMain) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                initAssets();
                            }
                        }).start();
                    }
                    transfer();

                }
            }
        }
    }


    private void initAssets() {
        AssetManager assetManager = getAssets();
        InputStream inputStream = null;
        FileOutputStream fos = null;
        try {
            String fileNames[] = assetManager.list("custom_resource");
            String rootPath = Environment.getExternalStorageDirectory().getPath();
            for (int i = 0; i < fileNames.length; i++) {
                File file = new File(rootPath + "/" + fileNames[i]);
                if (file.exists()) {
                    Log.d("TAG", "initAssets: -------->文件存在");
                    continue;
                }
                Log.d("TAG", "initAssets: -------->文件不存在");
                inputStream = getClass().getClassLoader().getResourceAsStream("assets/custom_resource/" + fileNames[i]);
                fos = new FileOutputStream(new File(rootPath + "/" + fileNames[i]));
                int len = 0;
                byte[] buffer = new byte[1024];
                while ((len = inputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    fos.flush();
                }
                inputStream.close();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
