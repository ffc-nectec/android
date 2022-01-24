package samplecode_north_vision_lifecare_10;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
//import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.north_vision.lifecare_10.Service.BackgroundService;

import java.util.Arrays;
import ffc.app.R;


public class LifeCareActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 0;

    //public static final String BLUETOOTH_DEVICE_ADDRESS = "88:1B:99:06:01:E0";
    //public static final String BLUETOOTH_DEVICE_ADDRESS = "88:1B:99:0C:48:99";
    public static final String BLUETOOTH_DEVICE_ADDRESS = "88:1B:99:0C:48:99";

    private TextView tv_connectionstatus;
    private TextView tv_deviceid;
    private TextView tv_hwversion;
    private TextView tv_swversion;
    private TextView tv_battery;
    private TextView tv_power;
    private TextView tv_nibpaction;
    private TextView tv_sys;
    private TextView tv_dia;
    private TextView tv_map;
    private TextView tv_pulse;
    private TextView tv_realtime;
    private TextView tv_error;
    private TextView tv_spo2;
    private TextView tv_pr;
    private TextView tv_pi;
    private TextView tv_wave;

    private BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifecare_main);

        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        tv_connectionstatus = findViewById(R.id.textView_connectionstatus);
        tv_deviceid = findViewById(R.id.textView_deviceid);
        tv_hwversion = findViewById(R.id.textView_hwversion);
        tv_swversion = findViewById(R.id.textView_swversion);
        tv_battery = findViewById(R.id.textView_battery);
        tv_power = findViewById(R.id.textView_power);
        tv_nibpaction = findViewById(R.id.textView_action);
        tv_sys = findViewById(R.id.textView_sys);
        tv_dia = findViewById(R.id.textView_dia);
        tv_map = findViewById(R.id.textView_map);
        tv_pulse = findViewById(R.id.textView_pulse);
        tv_realtime = findViewById(R.id.textView_realtime);
        tv_error = findViewById(R.id.textView_error);
        tv_spo2 = findViewById(R.id.textView_spo2);
        tv_pr = findViewById(R.id.textView_pr);
        tv_pi = findViewById(R.id.textView_pi);
        tv_wave = findViewById(R.id.textView_wave);
        Button btn_connect = findViewById(R.id.button_connect);
        Button btn_disconnect = findViewById(R.id.button_disconnect);
        Button btn_getdeviceid= findViewById(R.id.button_deviceid);
        Button btn_getinformation = findViewById(R.id.button_information);
        Button btn_start = findViewById(R.id.button_start);
        Button btn_stop = findViewById(R.id.button_stop);
        Button btnClose = findViewById(R.id.btnClose);
        ImageButton homeAsUp = findViewById(R.id.homeAsUp);
        btn_connect.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                BackgroundService.connect(BLUETOOTH_DEVICE_ADDRESS);
            }});

        btn_disconnect.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                BackgroundService.disconnect();
            }});

        btn_getdeviceid.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                BackgroundService.getDeviceID();
            }});

        btn_getinformation.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                BackgroundService.getDeviceInformation();
            }});

        btn_start.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                BackgroundService.NIBPStrat();
            }});

        btn_stop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                BackgroundService.NIBPStop();
            }});

        btnClose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("SYSInfo",tv_sys.getText());
                intent.putExtra("DIAInfo",tv_dia.getText());
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        Intent intent = new Intent(this, BackgroundService.class);
        startService(intent);

        BackgroundService.setHandler(myHandler);

        homeAsUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT){
            if (resultCode != RESULT_OK)
                Toast.makeText(this, "Bluetooth is disable", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, BackgroundService.class));
    }
    @SuppressLint("HandlerLeak")
    private Handler myHandler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BackgroundService.CONNECTIONSTATUSCHANGE:
                    String paramString = (String)msg.obj;
                    tv_connectionstatus.setText(paramString);
                    break;
                case BackgroundService.DEVICEID:
                    String sDeviceID = (String)msg.obj;
                    tv_deviceid.setText(sDeviceID);
                    break;
                case BackgroundService.DEVICEVER:
                    int nPower = msg.arg1;
                    int nBattery = msg.arg2;
                    String[] version = (String[])msg.obj;

                    tv_battery.setText(nBattery + ""); // 0 = no power input ; 1 = has power input
                    tv_power.setText(nPower + ""); // battery power level
                    tv_hwversion.setText(version[0]);
                    tv_swversion.setText(version[1]);
                    break;
                case BackgroundService.NIBPACTION:
                    int bStart = (int)msg.obj;
                    tv_nibpaction.setText(bStart + "");// 1 = start ; 2 = stop
                    break;
                case BackgroundService.NIBPREALTIME:
                    int nBldPrs = (int)msg.obj;
                    tv_realtime.setText(nBldPrs + "");
                    break;
                case BackgroundService.NIBPRESULT:
                    int[] nibp = (int[])msg.obj;
                    tv_pulse.setText(nibp[0] + "");
                    tv_sys.setText(nibp[1] + "");
                    tv_dia.setText(nibp[2] + "");
                    tv_map.setText(nibp[3] + "");
                    break;
                case BackgroundService.NIBPERROR:
                    int flag = msg.arg1;
                    int error = msg.arg2;
                    tv_error.setText(flag + " , " + error);
                    break;
                case BackgroundService.SPO2:
                    int nSpO2 = msg.arg1;
                    int nPR = msg.arg2;
                    float nPI = (float) msg.obj;
                    tv_spo2.setText(nSpO2 + "");
                    tv_pr.setText(nPR + "");
                    tv_pi.setText(nPI + "");
                    break;
                case BackgroundService.WAVE:
                    int[] wave = (int[])msg.obj;
                    tv_wave.setText(Arrays.toString(wave));
                    break;
                case BackgroundService.POWEROFF:
                    tv_connectionstatus.setText("Power off");
                    break;
            }
        }
    };
}
