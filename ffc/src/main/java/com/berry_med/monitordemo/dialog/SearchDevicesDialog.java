package com.berry_med.monitordemo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import ffc.app.R;

/**
 * Created by ZXX on 2017/4/28.
 */

public abstract class SearchDevicesDialog extends Dialog {


    public ListView lvBluetoothDevices;
    private ProgressBar pbSearchDevices;
    private Button btnSearchDevices;



    public SearchDevicesDialog(Context context, BluetoothDeviceAdapter adapter) {
        super(context);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.devices_dialog);

        lvBluetoothDevices = (ListView) findViewById(R.id.lvBluetoothDevices);
        lvBluetoothDevices.setAdapter(adapter);
        lvBluetoothDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClickDeviceItem(position);
            }
        });

        pbSearchDevices = (ProgressBar) findViewById(R.id.pbSearchDevices);
        btnSearchDevices = (Button) findViewById(R.id.btnSearchDevices);
        btnSearchDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearch();
            }
        });
    }
    public void stopSearch(){
        pbSearchDevices.setVisibility(View.GONE);
        btnSearchDevices.setVisibility(View.VISIBLE);
    }

    public void startSearch(){
        onStartSearch();
        pbSearchDevices.setVisibility(View.VISIBLE);
        btnSearchDevices.setVisibility(View.GONE);
    }

    public abstract void onStartSearch();
    public abstract void onClickDeviceItem(int pos);


}
