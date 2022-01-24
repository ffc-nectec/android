package samplecode_north_vision_thermometer.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import ffc.app.R;
import samplecode_north_vision_thermometer.Service.BackgroundService;

@SuppressLint("SetTextI18n")
public class ThermometerActivity extends Activity {
//    private final String TAG = "MainActivity";

    // Intent request codes
    private static final int REQUEST_ENABLE_BT = 0;

    public static final String BLUETOOTH_DEVICE_ADDRESS = "44:EA:D8:3D:4A:E6";
//    public static final String BLUETOOTH_DEVICE_ADDRESS = "44:EA:D8:3D:44:12";

    private BluetoothAdapter mBluetoothAdapter;

    private TextView textView_ConnectionStatus;
    private TextView textView_TemperatureUnit;
    private TextView textView_Data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thermometer_main);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        textView_ConnectionStatus = findViewById(R.id.textView_conn);
        textView_TemperatureUnit = findViewById(R.id.textView_temperatureunit);
        textView_Data = findViewById(R.id.textView_data);

        Button button_connect = findViewById(R.id.button_connect);
        Button button_disconnect = findViewById(R.id.button_disconnect);
        Button btnClose = findViewById(R.id.btnClose);
        ImageButton homeAsUp = findViewById(R.id.homeAsUp);
        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, getString(R.string.bluetooth_is_not_available), Toast.LENGTH_LONG).show();
            this.finish();
        }

        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }

        Intent intent = new Intent(this, BackgroundService.class);
        startService(intent);
        BackgroundService.setHandler(myHandler);

        button_connect.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                BackgroundService.connect(BLUETOOTH_DEVICE_ADDRESS);
            }});

        button_disconnect.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                BackgroundService.disconnect();
            }});
        homeAsUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("TEMPInfo",textView_Data.getText());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
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
    private final Handler myHandler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(@NotNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BackgroundService.CONNECTIONSTATUSCHANGE:
                    String connString = (String) msg.obj;
                    textView_ConnectionStatus.setText(connString);
                    break;
                case BackgroundService.DATA:
                    int temperatureUnitFlag = msg.arg1;
                    float data = (float) msg.obj;

                    if(temperatureUnitFlag == 0)
                        textView_TemperatureUnit.setText("Celsius");
                    else
                        textView_TemperatureUnit.setText("Fahrenheit");

                    textView_Data.setText(Float.toString(data));
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == REQUEST_ENABLE_BT) {
            // the user has chosen not to enable Bluetooth
            if (resultCode != Activity.RESULT_OK) {
                finish();
            }
            if (!mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.enable();
            }
        }
    }
}


