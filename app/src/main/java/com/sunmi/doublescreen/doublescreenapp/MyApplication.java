package com.sunmi.doublescreen.doublescreenapp;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.sunmi.doublescreen.doublescreenapp.utils.CrashHandler;

/**
 * Created by highsixty on 2017/11/16.
 * mail  gaolulin@sunmi.com
 */

public class MyApplication extends Application {
    public static final boolean isMain = Build.MODEL.equals("t1host") || Build.MODEL.equals("T1-G");

    public static int width = 0;
    public static int height = 0;

    private static MyApplication singleInstance;

    public static MyApplication getInstance() {
        return singleInstance;
    }





    @Override
    public void onCreate() {
        super.onCreate();

        getApplicationContext();
        singleInstance = this;
        super.onCreate();
        CrashHandler catchHandler = CrashHandler.getInstance();
        catchHandler.init(getApplicationContext());//用来获取全局的错误处理
//        if (isMain) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    initAssets();
//                }
//            }).start();
//        }
        initDeviceInfo();
    }


    public void initDeviceInfo() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return;
        }
        DisplayMetrics metric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels;
        height = metric.heightPixels;
    }

//    private void initAssets() {
//        AssetManager assetManager = getAssets();
//        InputStream inputStream = null;
//        FileOutputStream fos = null;
//        try {
//            String fileNames[] = assetManager.list("custom_resource");
//            String rootPath = Environment.getExternalStorageDirectory().getPath();
//            for (int i = 0; i < fileNames.length; i++) {
//                File file = new File(rootPath + "/" + fileNames[i]);
//                if (file.exists()) {
//                    Log.d("TAG", "initAssets: -------->文件存在");
//                    continue;
//                }
//                Log.d("TAG", "initAssets: -------->文件不存在");
//                inputStream = getClass().getClassLoader().getResourceAsStream("assets/custom_resource/" + fileNames[i]);
//                fos = new FileOutputStream(new File(rootPath + "/" + fileNames[i]));
//                int len = 0;
//                byte[] buffer = new byte[1024];
//                while ((len = inputStream.read(buffer)) != -1) {
//                    fos.write(buffer, 0, len);
//                    fos.flush();
//                }
//                inputStream.close();
//                fos.close();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
