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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ApkListActivity extends Activity
        implements OnItemClickListener {

    PackageManager packageManager;
    ListView apkList;
    List<PackageInfo> packageList1;
    List<String> packageNameList;
    private static Socket s;
    private static ServerSocket ss;
    private static InputStreamReader isr;
    private static BufferedReader br;
    private static PrintWriter pw;
    private static ObjectOutputStream os;
    private static String ip="192.168.0.107";
    Button send;


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

//        send=findViewById(R.id.send);
//        send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                sendPackageNames(packageList1);
//            }
//        });


    }


    public void sendPackageNames(List<PackageInfo> pkg)
    {
        myTask mt=new myTask();
        mt.execute();
    }


    class myTask extends AsyncTask<Void,Void,Void>
    {
        protected Void doInBackground(Void... params)
        {
            try
            {
               // String mess="helllooo";

                s=new Socket(ip,5005);
                //pw=new PrintWriter(s.getOutputStream());
                os=new ObjectOutputStream(s.getOutputStream());
                os.writeObject(packageNameList);
                os.flush();
                os.close();
//                for(String i:packageNameList)
//                {
//                    pw.write(i);
//                    pw.flush();
//                }



                //pw.close();
                s.close();




            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return null;
        }
    }
    /**
     * Return whether the given PackgeInfo represents a system package or not.
     * User-installed packages (Market or otherwise) should not be denoted as
     * system packages.
     *
     * @param pkgInfo
     * @return boolean
     */



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