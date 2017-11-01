package com.huntergao.dailyweather.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

/**
 * Created by HG on 2017/10/31.
 */

public class AndroidUtils {

    public static String getVersionCode(Context context) {
        if (context == null) {
            return "v1.0";
        }

        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_ACTIVITIES);
            return "v"+String.valueOf(packageInfo.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "v1.0";
    }

    public static boolean hasPermission(Context context, String permission) {
        if (context == null || TextUtils.isEmpty(permission)) {
            return false;
        }

        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
}
