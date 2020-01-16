package com.example.myapplication;

import android.app.Application;
import android.content.pm.PackageInfo;

public class AppData extends Application {

    static  PackageInfo packageInfo;

    public static PackageInfo getPackageInfo() {
        return packageInfo;
    }

    public void setPackageInfo(PackageInfo packageInfo) {

        this.packageInfo = packageInfo;
        System.out.println("app data   "  +packageInfo);
    }
}