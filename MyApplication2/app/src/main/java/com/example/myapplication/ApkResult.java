package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ApkResult  extends Activity {

    TextView r1c1, r1c2,r1c3,r2c1,r2c2,r2c3,r3c1,r3c2,r3c3,r4c1,r4c2,r4c3,r5c1,r5c2,r5c3;
    Button uninstall;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample);

        uninstall = (Button) findViewById(R.id.uninstall);



        r1c1 = (TextView) findViewById(R.id.r1c1);
        r1c2 = (TextView) findViewById(R.id.r1c2);
        r1c3 = (TextView) findViewById(R.id.r1c3);

        r2c1 = (TextView) findViewById(R.id.r2c1);
        r2c2 = (TextView) findViewById(R.id.r2c2);
        r2c3 = (TextView) findViewById(R.id.r2c3);

        r3c1 = (TextView) findViewById(R.id.r3c1);
        r3c2 = (TextView) findViewById(R.id.r3c2);
        r3c3 = (TextView) findViewById(R.id.r3c3);

        r4c1 = (TextView) findViewById(R.id.r4c1);
        r4c2 = (TextView) findViewById(R.id.r4c2);
        r4c3 = (TextView) findViewById(R.id.r4c3);


        r5c1 = (TextView) findViewById(R.id.r5c1);
        r5c2 = (TextView) findViewById(R.id.r5c2);
        r5c3 = (TextView) findViewById(R.id.r5c3);

        System.out.println("result");
        Intent intent = getIntent();
        String message = intent.getStringExtra("result");
        final String packName = intent.getStringExtra("pack");
        System.out.println("msg" +message);

        uninstall.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(ApkResult.this, "Button Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_DELETE);
                intent.setData(Uri.parse("package:"+packName));
                startActivity(intent);
                Toast.makeText(ApkResult.this,"App Removed",Toast.LENGTH_SHORT).show();

            }
        });

        r1c1.setText("Insecured Storage - Shared Preferences");
        r2c1.setText("Local Database Storgae");
        r3c1.setText("Data Leakage through Log");
        r4c1.setText("Clipboard data");
        r5c1.setText("Intent Spoofing");

        String vul[]  = message.split("\n");
        if(vul[0].equalsIgnoreCase("Yes"))
            r1c2.setText("Vulnerable");

        else
            r1c2.setText("Not Vulnerable");

        if(vul[1].equalsIgnoreCase("Yes"))
            r2c2.setText("Vulnerable");

        else
            r2c2.setText("Not Vulnerable");

        if(vul[2].equalsIgnoreCase("Yes"))
            r3c2.setText("Vulnerable");

        else
            r3c2.setText("Not Vulnerable");

        if(vul[3].equalsIgnoreCase("Yes"))
            r4c2.setText("Vulnerable");

        else
            r4c2.setText("Not Vulnerable");


        if(vul[4].equalsIgnoreCase("Yes"))
            r5c2.setText("Vulnerable");

        else
            r5c2.setText("Not Vulnerable");



        if(vul[0].equalsIgnoreCase("Yes"))
            r1c3.setText("Data is leaked through Shared Preferences object. Use different method to store data");
         else
             r1c3.setText("No data leakage");

        if(vul[1].equalsIgnoreCase("Yes"))
            r2c3.setText("Data is locally stored and not encrypted. Either encrypt the database, or use external storage");
        else
            r2c3.setText("No data leakage");

        if(vul[2].equalsIgnoreCase("Yes"))
            r3c3.setText("Data is leaked through Log. Ensure that sensitive data is not stored in log");
        else
            r3c3.setText("No data leakage");

        if(vul[3].equalsIgnoreCase("Yes"))
            r4c3.setText("Application is reading clipbaord and sensitive data could be compromised. Disable the clipboard");
        else
            r4c3.setText("No data leakage");

        if(vul[4].equalsIgnoreCase("Yes"))
            r5c3.setText("Activities are exported and this could leak user's private data");
        else
            r5c3.setText("No data leakage");
    }
}

