package com.example.lazyyeah.closewifi;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        final WifiManager wifiManger = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (wifiManger.isWifiEnabled()){
                wifiManger.setWifiEnabled(false);
            }
            else
            {
                wifiManger.setWifiEnabled(true);
            }
        }});


    }
}