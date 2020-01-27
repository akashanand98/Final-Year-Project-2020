package com.example.myapplication;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.example.myapplication.AppData;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ApkInfo extends Activity {
    PackageManager packageManager;
    TextView appLabel, packageName, version, features,result;
    TextView permissions, andVersion, installed, lastModify, path;
    PackageInfo packageInfo;
    Button uninstall,sp,enc,leak;
    String apName, packName, ver, andVer, ins, lmod, pth;

    //socket kaga
    private static Socket s;
    private static ServerSocket ss;
    private static InputStreamReader isr;
    private static BufferedReader br;
    private static PrintWriter pw;
    private static ObjectOutputStream os;
    private static String ip="192.168.0.105";
    private static String inet="";
    String mess;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apkinfo);


        packageInfo = AppData.getPackageInfo();
        apName = getPackageManager().getApplicationLabel(packageInfo.applicationInfo).toString();
        packName = packageInfo.packageName;
        ver = packageInfo.versionName;
        andVer = Integer.toString(packageInfo.applicationInfo.targetSdkVersion);
        pth = packageInfo.applicationInfo.sourceDir;
        result=findViewById(R.id.result);
        try {
            inet=getLocalIpAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        Log.v("inet",inet);

        //ACTIVITIES INFO
        try {
            ActivityInfo[] list = getPackageManager().getPackageInfo(packName,PackageManager.GET_ACTIVITIES).activities;
           for(ActivityInfo a:list) {
               System.out.println(a.toString());
               System.out.println(a.permission);
               System.out.println(a.exported);

           }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        //SERVICES INFO
        try {
            ServiceInfo[] services = getPackageManager().getPackageInfo(packName, PackageManager.GET_SERVICES).services;
            if(services!=null) {
                Log.v("serv", services.toString());
                for (ServiceInfo s : services) {
                    System.out.println(s.toString());
                    System.out.println(s.permission);
                    System.out.println(s.exported);

                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }



         //CONTENT PROVIDERS
        for (PackageInfo pack : getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS)) {
            if(pack.packageName.equals(packName) )
            {ProviderInfo[] providers = pack.providers;
            if (providers != null ) {
                for (ProviderInfo provider : providers) {
                    System.out.println("Example" + "provider: " + provider.authority);
                }
            }   }
        }





        //BROADCAST RECEVIERS
        try {
            final ActivityInfo[] receivers = getPackageManager().getPackageInfo(packName, PackageManager.GET_RECEIVERS).receivers;
        if(receivers!=null) {
            for (ActivityInfo r : receivers) {
                System.out.println(r.toString());
                System.out.println(r.permission);
                System.out.println(r.exported);

            }
        }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }



        ins = setDateFormat(packageInfo.firstInstallTime);
        lmod = setDateFormat(packageInfo.lastUpdateTime);




        uninstall = (Button) findViewById(R.id.uninstall);
        uninstall.setVisibility(View.INVISIBLE);

        sp=findViewById(R.id.sp);
        enc=findViewById(R.id.enc);
        leak=findViewById(R.id.leak);

        uninstall.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(ApkInfo.this, "Button Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_DELETE);
                intent.setData(Uri.parse("package:"+packName));
                startActivity(intent);
                Toast.makeText(ApkInfo.this,"App Removed",Toast.LENGTH_SHORT).show();

            }
        });

        sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendPackageNames();
            }

        });

        enc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPackageNames();
            }
        });

        leak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPackageNames();
            }
        });




        findViewsById();
        setValues();

    }

    public void sendPackageNames()
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

                s=new Socket(ip,5008);
                os=new ObjectOutputStream(s.getOutputStream());
                os.writeObject(packName);
                os.flush();

                Thread.sleep(2000);

                ObjectInputStream is = new ObjectInputStream(s.getInputStream());;
                mess=(String) is.readObject();
                Log.v("mess",mess);



            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            result.setText(mess);
            if(mess.equals("This App is Vulnerable due to exposed Shared Preferences File"))
            {
                uninstall.setVisibility(View.VISIBLE);
            }

        }


    }


    private String getLocalIpAddress() throws UnknownHostException {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        assert wifiManager != null;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipInt = wifiInfo.getIpAddress();
        return InetAddress.getByAddress(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(ipInt).array()).getHostAddress();
    }


        private void findViewsById() {

        appLabel = (TextView) findViewById(R.id.applabel);
        packageName = (TextView) findViewById(R.id.package_name);
        version = (TextView) findViewById(R.id.version_name);
        features = (TextView) findViewById(R.id.req_feature);
        permissions = (TextView) findViewById(R.id.req_permission);
        andVersion = (TextView) findViewById(R.id.andversion);
        path = (TextView) findViewById(R.id.path);
        installed = (TextView) findViewById(R.id.insdate);
        lastModify = (TextView) findViewById(R.id.last_modify);
    }

    private void setValues() {
        packageInfo = AppData.getPackageInfo();
        // APP name
        appLabel.setText(apName);

        // package name
        packageName.setText(packName);

        // version name
        version.setText(ver);

        // target version
        andVersion.setText(andVer);

        //path
        path.setText(pth);

        // first installation
        installed.setText(ins);

        // last modified
        lastModify.setText(lmod);

        // features
        if (packageInfo.reqFeatures != null)
            features.setText(getFeatures(packageInfo.reqFeatures));
        else
            features.setText("-");

        // uses-permission
        if (packageInfo.requestedPermissions != null)
            permissions
                    .setText(getPermissions(packageInfo.requestedPermissions));
        else
            permissions.setText("-");
    }

    @SuppressLint("SimpleDateFormat")
    private String setDateFormat(long time) {
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String strDate = formatter.format(date);
        return strDate;
    }

    // Convert string array to comma separated string
    private String getPermissions(String[] requestedPermissions) {
        String permission = "";
        for (int i = 0; i < requestedPermissions.length; i++) {
            permission = permission + requestedPermissions[i] + ",\n";
        }
        return permission;
    }

    // Convert string array to comma separated string
    private String getFeatures(FeatureInfo[] reqFeatures) {
        String features = "";
        for (int i = 0; i < reqFeatures.length; i++) {
            features = features + reqFeatures[i] + ",\n";
        }
        return features;
    }



}
