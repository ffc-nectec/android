//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.north_vision.lifecare_10.Service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import java.util.UUID;

@SuppressLint({"NewApi"})
public class BluetoothLeService extends Service {
    private static final String d = BluetoothLeService.class.getSimpleName();
    private BluetoothGatt e;
    private int f = 0;
    public static final String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public static final String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public static final String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public static final String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public static final String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";
    public static final String ACTION_GATT_CONNECTING = "com.example.bluetooth.le.ACTION_GATT_CONNECTING";
    public static final UUID a = UUID.fromString("0000FFB0-0000-1000-8000-00805f9b34fb");
    public final UUID b = UUID.fromString("0000FFB2-0000-1000-8000-00805f9b34fb");
    public static final UUID c = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    private final BluetoothGattCallback g = new BluetoothGattCallback() {
        public void onConnectionStateChange(BluetoothGatt var1, int var2, int var3) {
            Log.i(BluetoothLeService.d, "newState = " + var3);
            String var4;
            if (var3 == 2) {
                var4 = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
                BluetoothLeService.this.f = 2;
                BluetoothLeService.this.a(var4);
                Log.i(BluetoothLeService.d, "Connected to GATT server.");
                Log.i(BluetoothLeService.d, "Attempting to start service discovery:" + BluetoothLeService.this.e.discoverServices());
            } else if (var3 == 0) {
                Log.i(BluetoothLeService.d, "Disconnected from GATT server.");
                Log.i(BluetoothLeService.d, "mConnectionState = " + BluetoothLeService.this.f);
                if (BluetoothLeService.this.f == 1) {
                    Log.i(BluetoothLeService.d, "Disconnected CONNECTING");
                    Log.i(BluetoothLeService.d, "mConnectionState = " + BluetoothLeService.this.f);
                    var4 = "com.example.bluetooth.le.ACTION_GATT_CONNECTING";
                    BluetoothLeService.this.a(var4);
                } else if (BluetoothLeService.this.f != 0) {
                    BluetoothLeService.this.f = 0;
                    Log.i(BluetoothLeService.d, "Disconnected broadcast");
                    Log.i(BluetoothLeService.d, "mConnectionState = " + BluetoothLeService.this.f);
                    var4 = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
                    BluetoothLeService.this.a(var4);
                } else {
                    Log.i(BluetoothLeService.d, "newState = " + var3);
                }
            }

        }

        public void onServicesDiscovered(BluetoothGatt var1, int var2) {
            if (var2 == 0) {
                BluetoothLeService.this.a("com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED");
            } else {
                Log.w(BluetoothLeService.d, "onServicesDiscovered received: " + var2);
            }

        }

        public void onCharacteristicRead(BluetoothGatt var1, BluetoothGattCharacteristic var2, int var3) {
            if (var3 == 0) {
                BluetoothLeService.this.a("com.example.bluetooth.le.ACTION_DATA_AVAILABLE", var2);
            }

        }

        public void onCharacteristicChanged(BluetoothGatt var1, BluetoothGattCharacteristic var2) {
            BluetoothLeService.this.a("com.example.bluetooth.le.ACTION_DATA_AVAILABLE", var2);
        }
    };
    private final IBinder h = new LocalBinder();

    public BluetoothLeService() {
    }

    private void a(String var1) {
        Intent var2 = new Intent(var1);
        this.sendBroadcast(var2);
    }

    private void a(String var1, BluetoothGattCharacteristic var2) {
        Intent var3 = new Intent(var1);
        byte[] var4;
        if (this.b.equals(var2.getUuid())) {
            var4 = var2.getValue();
            var3.putExtra("Data", var4);
        } else {
            var4 = var2.getValue();
            if (var4 != null && var4.length > 0) {
                StringBuilder var5 = new StringBuilder(var4.length);
                byte[] var6 = var4;
                int var7 = var4.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    byte var9 = var6[var8];
                    var5.append(String.format("%02X ", var9));
                }

                var3.putExtra("com.example.bluetooth.le.EXTRA_DATA", new String(var4) + "\n" + var5.toString());
            }
        }

        this.sendBroadcast(var3);
    }

    public IBinder onBind(Intent var1) {
        return this.h;
    }

    public boolean onUnbind(Intent var1) {
        this.a();
        return super.onUnbind(var1);
    }

    public void a() {
        if (this.e != null) {
            this.e.close();
            this.e = null;
        }
    }

    public class LocalBinder extends Binder {
        public LocalBinder() {
        }
    }
}
