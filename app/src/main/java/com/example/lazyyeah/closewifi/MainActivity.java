package com.example.lazyyeah.closewifi;

import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import java.lang.reflect.Method;
import java.util.Observer;


public class MainActivity extends AppCompatActivity {

    private Button button;
    private ConnectivityManager mCM;
    private WifiManager wifiManger;
    boolean gpsEnabled;
    private Button buttonGps;
    private Context context;
    private BluetoothAdapter mAdapter;


    public MainActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);

        wifiManger = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);


        gpsEnabled = Settings.Secure.isLocationProviderEnabled(getContentResolver(), LocationManager.GPS_PROVIDER);
        Log.i("test", "GPS的状态是。。。" + gpsEnabled);
        buttonGps = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (wifiManger.isWifiEnabled()) {
                    wifiManger.setWifiEnabled(false);
                    System.out.println("wifi关上了");
                } else {
                    wifiManger.setWifiEnabled(true);
                    System.out.println("wifi打开了");
                }
                gprsInit();

/*启动gps界面
                Intent intent = new Intent;
                intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
*/
                BluetoothInit();


            }

        });
    }

    private void gprsInit() {
        mCM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        gprsSetter();
        finish();
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
        } else {
            setGprsEnabled("setMobileDataEnabled", true);
            System.out.println("GPRS开启");
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
            System.out.println("蓝牙是关闭的");
            mAdapter.enable();
            System.out.println("蓝牙打开了");
        } else {
            System.out.println("蓝牙是打开的");
            mAdapter.disable();
            System.out.println("蓝牙关闭了");
        }
    }
}


