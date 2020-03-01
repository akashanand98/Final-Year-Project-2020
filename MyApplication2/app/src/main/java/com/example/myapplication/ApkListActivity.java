package com.example.myapplication;
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.net.http.SslCertificate;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;

public class ApkListActivity extends Activity
        implements OnItemClickListener {

    PackageManager packageManager;
    ListView apkList;
    List<PackageInfo> packageList1;
    List<String> packageNameList;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        packageManager = getPackageManager();

        List<PackageInfo> packageList = packageManager
                .getInstalledPackages(PackageManager.GET_PERMISSIONS);

        packageList1 = new ArrayList<PackageInfo>();
        packageNameList=new ArrayList<String>();


        /*To filter out System apps*/
        for(PackageInfo pi : packageList) {
            Log.v("pkginfo",pi.applicationInfo.packageName);
            boolean b = isSystemPackage(pi);
//            if(pi.applicationInfo.packageName=="com.ist.challenge3")
//            {
//                packageList1.add(pi);
//            }
            if(!b) {
                packageList1.add(pi);
                packageNameList.add(pi.applicationInfo.packageName);
            }
        }
        Log.v("list",packageList1.toString());
        apkList = (ListView) findViewById(R.id.applist);
        apkList.setAdapter(new ApkAdapter(this, packageList1, packageManager));

        apkList.setOnItemClickListener(this);




    }



    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true
                : false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long row) {
        System.out.println("clicked");


        PackageInfo packageInfo = (PackageInfo) parent.getItemAtPosition(position);
        System.out.println(packageInfo);
        System.out.println(position);
        AppData appData = new AppData();
        appData.setPackageInfo(packageInfo);
        System.out.println(appData);
        Intent appInfo = new Intent(getApplicationContext(), ApkInfo.class);
        startActivity(appInfo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_apk_list, menu);
        return true;
    }
}