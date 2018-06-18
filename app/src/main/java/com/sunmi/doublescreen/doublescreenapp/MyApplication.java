package com.sunmi.doublescreen.doublescreenapp;

import android.app.Application;
import android.os.Build;

import com.sunmi.doublescreen.doublescreenapp.utils.CrashHandler;

/**
 * Created by highsixty on 2017/11/16.
 * mail  gaolulin@sunmi.com
 */

public class MyApplication extends Application {
    public static final boolean isMain = Build.MODEL.equals("t1host") || Build.MODEL.equals("T1-G");

    @Override
    public void onCreate() {
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
