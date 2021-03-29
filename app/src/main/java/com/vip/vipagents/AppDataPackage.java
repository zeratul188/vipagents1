package com.vip.vipagents;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class AppDataPackage {

    public String getVersionInfo(Context context) {
        String version = "알 수 없음";
        PackageInfo packageInfo;

        if (context == null) return version;
        try {
            packageInfo = context.getApplicationContext().getPackageManager().getPackageInfo(context.getApplicationContext().getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }
}
