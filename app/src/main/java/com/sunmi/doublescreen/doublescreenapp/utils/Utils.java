package com.sunmi.doublescreen.doublescreenapp.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static int screenWidth = 0;
    private static int screenHeight = 0;

    public static int getScreenHeight(Context c) {
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }

        return screenHeight;
    }

    public static int getScreenWidth(Context c) {
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }

        return screenWidth;
    }


    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float sp2px(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public static int px2dp(Resources resources, float dipValue) {
        final float scale = resources.getDisplayMetrics().density;
        return (int) (dipValue / scale + 0.5f);
    }

    public static boolean isAppInstalled(Context context, String packagename) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            //System.out.println("没有安装");
            return false;
        } else {
            //System.out.println("已经安装");
            return true;
        }
    }

    /**
     * 获取应用包名
     */
    public static String getPackageName(Context context) {
        try {
            String pkName = context.getPackageName();
            return pkName;
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 获取系统版本号
     */
    public static String getDeviceOSVersion() {
        // 系统版本号
        String deviceOSVersion = Build.VERSION.RELEASE;
        return deviceOSVersion;
    }

    /**
     * 获取设备型号
     */
    public static String getMODEL() {
        // 系统版本号
        String model = Build.MODEL;
        return model;
    }

    /**
     * 获取设备名称
     */
    public static String getDeviceName() {
        // 系统版本号
        String hardware = Build.HARDWARE;
        return hardware;
    }




    /**
     * 获取版本名字
     *
     * @param context
     * @return version name
     */
    public static int getClientVersionCode(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            int appVersionCode = info.versionCode; // 版本名

            return appVersionCode; // 版本号
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * 获取版本名字
     *
     * @param context
     * @return version name
     */
    public static String getClientVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            String appVersion = info.versionName; // 版本名
            return appVersion; // 版本号
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }


//    /**
//     * textview显示不同颜色2
//     *
//     * @param context
//     * @return
//     */
//    public static SpannableString setDifTvColor(Context context,String showText,int start,int end) {
//        SpannableString msp = null;
//        msp = new SpannableString(showText);
//        msp.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.App_more_text_3)),0,start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        msp.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.App_back_orange)),start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        msp.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.App_more_text_3)),end,showText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        return msp;
//    }

    /**
     * 判断是否是手机网络连接
     *
     * @param context
     * @return
     */
    public static boolean isPhoneNetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null
                && networkINfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }
        return false;
    }
//
//    /**
//     * 网络IP地址获得
//     *
//     * @return
//     */
//    public static String getLocalIpAddress() {
//        String ipaddress = "0.0.0.0";
//        try {
//            Enumeration<NetworkInterface> en = NetworkInterface
//                    .getNetworkInterfaces();
//            // 遍历所用的网络接口
//            while (en.hasMoreElements()) {
//                NetworkInterface nif = en.nextElement();// 得到每一个网络接口绑定的所有ip
//                Enumeration<InetAddress> inet = nif.getInetAddresses();
//                // 遍历每一个接口绑定的所有ip
//                while (inet.hasMoreElements()) {
//                    InetAddress ip = inet.nextElement();
//                    if (!ip.isLoopbackAddress()
//                            && InetAddressUtils.isIPv4Address(ip
//                            .getHostAddress())) {
//                        return ip.getHostAddress().toString();
//                    }
//                }
//            }
//        } catch (SocketException e) {
//            return ipaddress;
//        }
//        return ipaddress;
//    }

    /**
     * 判断sdcard是否存在
     *
     * @return true存在可读写 false不存在不可读写
     */
    public static boolean sdacrdExist() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            // Log.d(TAG, "sdcard is exist,read and write ok");
            return true;
        }
        // Log.d(TAG, "sdcard is not exist or can not read/write");
        return false;
    }

    public static final String ACTION = "com.ufo.service.RemindService";


    /**
     * 过滤城市中的“市”，“区”，“县”
     *
     * @return 县级城市
     */
    public static String CityTirm(String city) {

        city = city.substring(0, city.length() - 1);

        return city;
    }


    public static String formatLongToTimeStr(Long l) {
        int hour = 0;
        int minute = 0;
        int second = 0;

        second = l.intValue() / 1000;

        if (second >= 60) {
            minute = second / 60;
            second = second % 60;
        }
        if (minute >= 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        return (getTwoLength(hour) + ":" + getTwoLength(minute) + ":" + getTwoLength(second));
    }

    private static String getTwoLength(final int data) {
        if (data < 10) {
            return "0" + data;
        } else {
            return "" + data;
        }
    }

    public static boolean isHuaweiPhone(Context context) {
        String packageName = "com.huawei.systemmanager";
        boolean isHuaweiPhone = false;
        try {
            PackageManager pm = context.getPackageManager();
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            isHuaweiPhone = true;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
        }

        return isHuaweiPhone;
    }

    public static void openHuaweiProcessProtectActivity(Context context) {
        String packageName = "com.huawei.systemmanager";
        PackageManager pm = context.getPackageManager(); //获得PackageManager对象
        //Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        Intent mainIntent = new Intent();
        mainIntent.setPackage(packageName);
        //mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        // 通过查询，获得所有ResolveInfo对象.
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo reInfo : resolveInfos) {
            String activityName = reInfo.activityInfo.name; // 获得该应用程序的启动Activity的name
            String pkgName = reInfo.activityInfo.packageName; // 获得应用程序的包名

            // 为应用程序的启动Activity 准备Intent
            Intent launchIntent = new Intent(Intent.ACTION_VIEW);
            launchIntent.setComponent(new ComponentName(pkgName, activityName));
            if (activityName.contains("ProtectActivity")) {
                context.startActivity(launchIntent);
                break;
            }
        }
    }




    /**
     * 序列化对象
     *
     * @param object
     * @return
     * @throws IOException
     */
    public static String serialize(Object object) throws IOException {
        long startTime = System.currentTimeMillis();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        String serStr = byteArrayOutputStream.toString("ISO-8859-1");
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
        objectOutputStream.close();
        byteArrayOutputStream.close();
        Log.d("serial", "serialize str =" + serStr);
        long endTime = System.currentTimeMillis();
        Log.d("serial", "序列化耗时为:" + (endTime - startTime));
        return serStr;
    }

    /**
     * 反序列化对象
     *
     * @param str
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object deSerialization(String str) throws IOException,
            ClassNotFoundException {
        long startTime = System.currentTimeMillis();
        String redStr = java.net.URLDecoder.decode(str, "UTF-8");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                redStr.getBytes("ISO-8859-1"));
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        Object object = (Object) objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        long endTime = System.currentTimeMillis();
        Log.d("serial", "反序列化耗时为:" + (endTime - startTime));
        return object;
    }


    public static double doubleFor2(double f) {

        BigDecimal b = new BigDecimal(f);
        double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }


//    /**
//     * 服务地址
//     *
//     * @param places
//     * @return
//     */
//    public static String getSerplace(List<DistrectsInfo.District> places) {
//        StringBuffer str = new StringBuffer("服务地区：");
//
//
//        for (DistrectsInfo.District item : places) {
//            str.append(item.getName());
//            str.append("  ");
//        }
//        return str.toString();
//
//    }



    //判断手机格式是否正确
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((\\d{3}-\\d{8}|\\d{4}-\\d{7,8})|(1[3|5|7|8][0-9]{9}))$");
        Matcher m = p.matcher(mobiles);

        return m.matches();
    }

    //判断email格式是否正确
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    //判断是否全是数字
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }


    public static String getOrderNo() {
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = df.format(new Date());
        String rd = String.valueOf((int) (Math.random() * 10 + 1));

        return time + rd;

    }

    //dp转化为sp
    public int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    //dp转化为sp
    public int Px2Dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }


    /**
     * This method converts device specific pixels to device independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     *           <p>
     *           Context to get resources and device specific display metrics
     * @return A float value to represent db equivalent to px value
     */
    public float convertPixelsToDp(Context ctx, float px) {
        DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;

    }

    public static int convertDpToPixelInt(Context context, float dp) {

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int px = (int) (dp * (metrics.densityDpi / 160f));
        return px;
    }

    public static float convertDpToPixel(Context context, float dp) {

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float px = (float) (dp * (metrics.densityDpi / 160f));
        return px;
    }

    /**
     * 关键字高亮显示
     *
     * @param target 需要高亮的关键字
     * @param text   需要显示的文字
     * @return spannable 处理完后的结果，记得不要toString()，否则没有效果
     */
    public static SpannableStringBuilder highlight(String text, String target) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);
        CharacterStyle span = null;

        Pattern p = Pattern.compile(target);
        Matcher m = p.matcher(text);
        while (m.find()) {
            span = new ForegroundColorSpan(Color.RED);// 需要重复！
            spannable.setSpan(span, m.start(), m.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }





    // a integer to xx:xx:xx
    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }


    /**
     * 时间转换
     *
     * @param strTime
     * @param formatType
     * @return
     * @throws ParseException
     */
    public static long stringToLong(String strTime, String formatType)
            throws ParseException {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    /**
     * 时间转换
     *
     * @param strTime
     * @param formatType
     * @return
     * @throws ParseException
     */
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    /**
     * 时间转换
     *
     * @param date
     * @return
     */
    public static long dateToLong(Date date) {
        return date.getTime();
    }
}
