package  com.berry_med.monitordemo.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.berry_med.monitordemo.dialog.BluetoothDeviceAdapter;

/**
 * Created by ZXX on 2017/4/28.
 */

public class BTController {
    //TAG
    private final String TAG = this.getClass().getName();

    private BTController mBtController = null;
    private BluetoothAdapter mBtAdapter    = null;
    private boolean isBTConnected             = false;
    private boolean isRegistered = false;

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public  Listener                     mListener;
    private BluetoothChatService         mBluetoothChatService;
    private BluetoothDeviceAdapter       mBluetoothDeviceAdapter;
    public BTController(Listener listener){
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        mListener         = listener;
    }

    /**
     * Get a Controller
     * @return
     */
    public BTController getDefaultBTController(Listener listener) {
        if (mBtController == null) {
            mBtController = new BTController(listener);
        }
        return mBtController;
    }
    public void setBluetoothDeviceAdapter(BluetoothDeviceAdapter deviceAdapter){
        mBluetoothDeviceAdapter = deviceAdapter;
    }

    /**
     * enable bluetooth adapter
     */
    public void enableBtAdpter(){
        if (!mBtAdapter.isEnabled()) {
            mBtAdapter.enable();
        }
    }

    /**
     * disable bluetooth adapter
     */
    public void disableBtAdpter(){
        if (mBtAdapter.isEnabled()) {
            mBtAdapter.disable();
        }
    }


    public boolean isBTConnected() {
        return isBTConnected;
    }

    /**
     * Scan bluetooth devices
     * @param b
     */
    public void startScan(boolean b){
        if(b){
            mBtAdapter.startDiscovery();
        }
        else {
            mBtAdapter.cancelDiscovery();
        }
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        return intentFilter;
    }

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.

    private BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                mListener.onFoundDevice(device);
                //mBluetoothDeviceAdapter.notifyDataSetChanged();
                Log.i(TAG, "<<<Bluetooth Devices>>>  On Found Device : "+ device.getName() + "  MAC:"+device.getAddress());
            }
            else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                Log.i(TAG, "<<<Bluetooth Devices>>>  Stop Scan.......");
                mListener.onStopScan();
            }
            else if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                Log.i(TAG, "<<<Bluetooth Devices>>>  Start Scan.......");
                mListener.onStartScan();
            }

        }
    };

    //GATT
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case Const.MESSAGE_BLUETOOTH_STATE_CHANGE:
                {
                    switch(msg.arg1)
                    {
                        case BluetoothChatService.STATE_CONNECTING:
                            Log.i(TAG, "<<<Bluetooth Devices>>>  Connecting");
                            break;
                        case BluetoothChatService.STATE_CONNECTED:
                            Log.i(TAG, "<<<Bluetooth Devices>>>  connected");
                            mListener.onConnected();
                            isBTConnected = true;
                            break;
                        case BluetoothChatService.STATE_NONE:
                            Log.i(TAG, "<<<Bluetooth Devices>>>  disconnected");
                            mListener.onDisconnected();
                            isBTConnected = false;
                            break;
                        default:
                            break;
                    }
                }break;
                case Const.MESSAGE_BLUETOOTH_DATA:
                    mListener.onReceiveData((byte[])msg.obj);
                    break;

            }
        }
    };

    /**
     * connect the bluetooth device
     * @param context
     * @param device
     */
    public void connect(Context context, final BluetoothDevice device) {

        mBluetoothChatService = new BluetoothChatService(context,mHandler);
        mBluetoothChatService.connect(device, true);
    }

    /**
     * Disconnect the bluetooth
     */
    public void disconnect(){
        mBluetoothChatService.stop();
    }

    /**
     * Send data to the monitor
     * @param dat
     */
    public void write(byte[] dat){
        if(mBluetoothChatService != null){
            mBluetoothChatService.write(dat);
        }
    }

    public void registerBroadcastReceiver(Context context)
    {
        context.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        this.isRegistered =true;
    }

    public void unregisterBroadcastReceiver(Context context)
    {
        context.unregisterReceiver(mGattUpdateReceiver);
        this.isRegistered =false;
    }

    /**
     * BTController interfaces
     */
    public interface Listener
    {
        void onFoundDevice(BluetoothDevice device);
        void onStopScan();
        void onStartScan();

        void onConnected();
        void onDisconnected();
        void onReceiveData(byte[] dat);
    }
}
