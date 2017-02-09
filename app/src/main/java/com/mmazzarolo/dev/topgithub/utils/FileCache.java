package com.mmazzarolo.dev.topgithub.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.mmazzarolo.dev.topgithub.MainApplication;

import java.io.File;

/**
 * Created by Arison on 2017/2/9.
 */
public class FileCache {

    private static final String TAG = "FileCache";
    private static String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";
    private static FileCache instance;
    private static String cachePath = Environment.getExternalStorageDirectory() + "/GitHub/repo/";
    private File cacheDir;
    public final String cacheDirPath = "/repo/";

    private FileCache() {
        if (hasSDCard() && hasExternalStoragePermission(MainApplication.getAppContext())) {
            cacheDir = createFilePath(cachePath);
        } else {
            cacheDir = createFilePath(MainApplication.getAppContext().getCacheDir() + cacheDirPath);
        }
        LogUtil.d(TAG,cacheDir.getAbsolutePath());
    }

    public static FileCache getInstance() {
        if (null == instance)
            instance = new FileCache();
        return instance;
    }

    private File createFilePath(String filePath) {
        return createFilePath(new File(filePath));
    }

    private File createFilePath(File file) {
        if (!file.exists()) {
            file.mkdirs();// 按照文件夹路径创建文件夹
        }
        return file;
    }
     
    public boolean hasSDCard() {
        return Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    /**
      * @desc:检查权限
      * @author：Arison on 2017/2/9
      */
    public static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
        return perm == PackageManager.PERMISSION_GRANTED;
    }

    public File getCacheDir() {
        return cacheDir;
    }

    public String getRepoAbsolutePath(String repoName) {
        return getCacheDir().getPath() + File.separator + repoName;
    }
}
