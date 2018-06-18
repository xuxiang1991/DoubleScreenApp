package com.sunmi.doublescreen.doublescreenapp.utils;

/**
 * Created by sunmi on 2017/2/21.
 */

import android.os.Environment;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileUtils {

    public static String SDPATH = Environment.getExternalStorageDirectory()
            + "/HCService/";//获取文件夹

    public static File createSDDir(String dirName) throws IOException {
        File dir = new File(SDPATH + dirName);
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {

            System.out.println("createSDDir:" + dir.getAbsolutePath());
            System.out.println("createSDDir:" + dir.mkdir());
        }
        return dir;
    }

    public static boolean isFileExist(String fileName) {
        File file = new File(SDPATH + fileName);
        file.isFile();
        return file.exists();
    }
    //删除文件
    public static void delFile(String fileName){
        File file = new File(SDPATH + fileName);
        if(file.isFile()){
            file.delete();
        }
        file.exists();
    }
    //删除文件夹和文件夹里面的文件
    public static void deleteDir(String path ) {
        File dir = new File(path);
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDir(file.getPath()); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }

    /**创建异常文件*/
    public static File createErrorFile(){
        File file = null;
        try {
            String DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Sunmi/UserCenter/log/";
            String NAME = DateUtils.getCurrentDateString() + ".txt";
            File dir = new File(DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            file = new File(dir, NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }


    public static String readFile(String file) {
        if (file == null) {
            return "";
        }
        String dataString = "";
        byte[] buff = new byte[200];
        FileInputStream fs = null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            fs = new FileInputStream(file);
            int len = -1;
            while ((len = fs.read(buff)) != -1) {
                os.write(buff, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fs != null) {
                try {
                    os.close();
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (os != null) {
            dataString = new String(os.toByteArray());
        }
        return dataString;
    }

    public static void initData(String txt) {
        String filePath = "/sdcard/Sunmi/DSD/log/";
        String fileName = "lxylxy.txt";
        writeTxtToFile(txt, filePath, fileName);
    }

    // 将字符串写入到文本文件中
    public static void writeTxtToFile(String strcontent, String filePath, String fileName) {
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName);

        String strFilePath = filePath+fileName;
        // 每次写入时，都换行写
        String strContent = strcontent + "\r\n";
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
        }
    }

    // 生成文件
    public static File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    // 生成文件夹
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
        }
    }

}