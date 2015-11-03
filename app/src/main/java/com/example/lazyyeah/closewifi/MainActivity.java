package com.example.lazyyeah.closewifi;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import java.lang.reflect.Method;


public class MainActivity extends AppCompatActivity {

    private ConnectivityManager mCM;
    private WifiManager wifiManger;
    boolean gpsEnabled;
    private Button buttonwifi;
    private Button buttonGps;
    private Button buttongprs;
    private Button buttonblueteeth;
    private Context context;
    private BluetoothAdapter mAdapter;


    public MainActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        wifiManger = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        gpsEnabled = Settings.Secure.isLocationProviderEnabled(getContentResolver(), LocationManager.GPS_PROVIDER);
        Log.i("test", "GPS的状态是。。。" + gpsEnabled);


        buttonwifi = (Button) findViewById(R.id.wifibutton);
        buttonwifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               wifiInit();
            }
        });


        buttongprs = (Button) findViewById(R.id.gprsbutton);
        buttongprs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gprsInit();
            }
        });


        buttonblueteeth = (Button) findViewById(R.id.bluebutton);
        buttonblueteeth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothInit();
            }
        });

        buttonGps = (Button)findViewById(R.id.gpsbutton);
        buttonGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //启动gps界面
                GPSInit();
            }
        });


    }
    private void wifiInit(){
        if (wifiManger.isWifiEnabled()) {
            wifiManger.setWifiEnabled(false);
            System.out.println("wifi关上了");
            Toast.makeText(getApplicationContext(), "wifi关上了",
                    Toast.LENGTH_SHORT).show();

        } else {
            wifiManger.setWifiEnabled(true);
            System.out.println("wifi打开了");
            Toast.makeText(getApplicationContext(), "wifi打开了",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void gprsInit() {
        mCM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        gprsSetter();
    }


    //判断gprs是否开启
    private boolean gprsIsOpenMethod(String methodName) {
        Class cmClass = mCM.getClass();
        Class[] argClasses = null;
        Object[] argObject = null;

        Boolean isOpen = false;
        try {
            Method method = cmClass.getMethod(methodName, argClasses);

            isOpen = (Boolean) method.invoke(mCM, argObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isOpen;
    }

    private boolean gprsSetter() {
        Object[] argObjects = null;

        boolean gprsisOpen = this.gprsIsOpenMethod("getMobileDataEnabled");
        if (gprsisOpen) {
            setGprsEnabled("setMobileDataEnabled", false);
            System.out.println("GPRS关闭");
            Toast.makeText(getApplicationContext(), "GPRS关闭",
                    Toast.LENGTH_SHORT).show();
        } else {
            setGprsEnabled("setMobileDataEnabled", true);
            System.out.println("GPRS开启");
            Toast.makeText(getApplicationContext(), "GPRS开启",
                    Toast.LENGTH_SHORT).show();
        }
        return gprsisOpen;
    }

    //开启/关闭GPRS
    private void setGprsEnabled(String methodName, boolean isEnable) {
        Class cmClass = mCM.getClass();
        Class[] argClasses = new Class[1];
        argClasses[0] = boolean.class;

        try {
            Method method = cmClass.getMethod(methodName, argClasses);
            method.invoke(mCM, isEnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/*没用，应该只支持android2.2以下
    private void toggleGPS() {
        Intent gpsIntent = new Intent();
        gpsIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
        gpsIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(MainActivity.this, 0, gpsIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
            finish();
        }
    }
*/

    private void BluetoothInit() {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mAdapter.isEnabled() == false) {
            mAdapter.enable();
            System.out.println("蓝牙打开了");
            Toast.makeText(getApplicationContext(), "蓝牙打开了",
                    Toast.LENGTH_SHORT).show();

        } else {
            mAdapter.disable();
            System.out.println("蓝牙关闭了");
            Toast.makeText(getApplicationContext(), "蓝牙关闭了",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void GPSInit(){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "请用户手动关闭",
                Toast.LENGTH_SHORT).show();
    }
}


