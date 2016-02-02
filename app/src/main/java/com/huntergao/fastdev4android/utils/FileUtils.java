package com.huntergao.fastdev4android.utils;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Description:文件相关的操作,如果需要在SD卡主目录下建立子目录,请在{@link ExternalStorageType}枚举下建立相同的变量,带上目录名字即可
 * <strong>image, voice, video目录下会有.nomedia文件来屏蔽系统扫描</strong>
 *
 * <ul>
 *     <li>{@link }</li>
 * </ul>
 * Created by HunterGao on 16/1/13.
 */
public class FileUtils {

    public static final String EXTERNAL_STORAGE_PATH = "fastdevelop";

    private static final float GB = 1024*1024*1024;
    private static final float MB = 1024*1024;
    private static final float KB = 1024;

    public enum ExternalStorageType{

    }

    /**
     * 检测并且创建文件
     */
    public static File checkAndCreateFile(String path){
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 检测并且创建文件夹
     */
    public static File checkAndCreateDirectory(String path){
        File dir = new File(path);
        if (!dir.exists()){
            dir.mkdir();
        }
        if(!dir.exists()){
            return null;
        }
        return dir;
    }

    /**
     * 外部{@link #EXTERNAL_STORAGE_PATH}目录或者/data/data/com.android.framework/files/目录（如果SD卡不可用）,
     * 末尾自带"/"符号
     */
    public static String getExternalStoragePath(){
        String path = null;
        //需要检测外部SD卡的挂载状态
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory().getPath() + EXTERNAL_STORAGE_PATH;
        }
        if (path == null){
            //如果外部SD卡不可用，使用"/data/data/com.android.framework/files/"目录
//            path = Application;
        }
        if (!path.subSequence(path.length()-1, path.length()).equals("/"))
            path += "/";
        return path;
    }

    /**
     * 获得文件的大小
     * @param file
     * @return
     */
    public static String getFileSize(File file){
        long size = 0;
        if (file.exists()){
            try {
                FileInputStream fis = new FileInputStream(file);
                size = fis.available()/8;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if(size/GB > 1){
            return size/GB + "G";
        }else if(size/MB > 1){
            return size/MB + "M";
        }else if(size/KB > 1){
            return size/KB +"K";
        }
        return size+"B";
    }

}
