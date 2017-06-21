package com.techsofficial.prithivi.nofication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private String s;
    SessionManagement session;
    String token;
    EditText number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session = new SessionManagement(getApplicationContext());




         number = (EditText)findViewById(R.id.number);



        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Check type of intent filte        r
                if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)){
                    //Registration success
                 token = intent.getStringExtra("token");
                /*    Toast.makeText(getApplicationContext(), "GCM token:" + token, Toast.LENGTH_LONG).show();*/
                } else if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)){
                    //Registration error
                  /*  Toast.makeText(getApplicationContext(), "GCM registration error!!!", Toast.LENGTH_LONG).show();*/
                } else {
                    //Tobe define
                }
            }
        };
   /*     Toast.makeText(getApplicationContext(), ""+token+"\n"+""+s, Toast.LENGTH_LONG).show();*/

        //Check status of Google play service in device
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if(ConnectionResult.SUCCESS != resultCode) {
            //Check type of error
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                //So notification
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());
            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }
        } else {
            //Start service
            Intent itent = new Intent(this, GCMRegistrationIntentService.class);
            startService(itent);
        }




        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.login);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s =  number.getText().toString();
                session.createLoginSession(""+token, ""+s);
                Toast.makeText(getApplicationContext(), "" +token+"\n"+s, Toast.LENGTH_LONG).show();



                            HashMap postData2 = new HashMap();
                Date now = new Date();
                DateFormat formatter = DateFormat.getInstance();
                String dateStr = formatter.format(now);
                System.out.println(dateStr);
                            postData2.put("data", dateStr);
                            postData2.put("number",""+s);
                            postData2.put("token", ""+token);


                            PostResponseAsyncTask taskInsert = new PostResponseAsyncTask(MainActivity.this,
                                    postData2, new AsyncResponse()
                            {
                                @Override
                                public void processFinish(String s) {
                                    Log.d(TAG, s);
                                    if (s.contains("success")) {
                                        Toast.makeText(MainActivity.this, " Successfully", Toast.LENGTH_LONG).show();


                                    } else {
                                        Toast.makeText(getApplicationContext(), "Error while uploading.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                taskInsert.execute("http://hateyou.esy.es/quiz/people/numbercheck.php");



                // Staring MainActivity
                Intent i = new Intent(getApplicationContext(), recycle.class);
                startActivity(i);
                finish();

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w("userlistview", "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Log.w("userlistview", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }
}
