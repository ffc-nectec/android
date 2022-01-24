//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.north_vision.lifecare_10.Service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import com.north_vision.lifecare_10.SpotCheck.AnalyseData;
import com.north_vision.lifecare_10.SpotCheck.ISpotCheckCallBack;
import com.north_vision.lifecare_10.base.BaseData.Wave;
import com.north_vision.lifecare_10.bluetooth.BluetoothService;
import java.util.Iterator;
import java.util.List;

public class BackgroundService extends Service {
    public static final String BLUETOOTH_CONNECTFAIL = "ConnectFail";
    public static final String BLUETOOTH_CONNECTLOST = "ConnectLost";
    public static final String BLUETOOTH_CONNECTING = "Connecting";
    public static final String BLUETOOTH_CONNECTED = "Connected";
    public static final int CONNECTIONLOST = 0;
    public static final int CONNECTIONSTATUSCHANGE = 1;
    public static final int DEVICEID = 2;
    public static final int DEVICEVER = 3;
    public static final int NIBPACTION = 4;
    public static final int NIBPREALTIME = 5;
    public static final int NIBPRESULT = 6;
    public static final int NIBPERROR = 7;
    public static final int SPO2 = 8;
    public static final int WAVE = 9;
    public static final int POWEROFF = 10;
    private AnalyseData g;
    @SuppressLint({"StaticFieldLeak"})
    private static BluetoothService h = null;
    private static BluetoothAdapter i;
    static byte[] a = new byte[]{-86, 85, -1, 2, 1, 0};
    static byte[] b = new byte[]{-86, 85, 81, 2, 1, 0};
    static byte[] c = new byte[]{-86, 85, 64, 2, 1, 0};
    static byte[] d = new byte[]{-86, 85, 64, 2, 2, 0};
    static byte[] e = new byte[]{0, 94, -68, -30, 97, 63, -35, -125, -62, -100, 126, 32, -93, -3, 31, 65, -99, -61, 33, 127, -4, -94, 64, 30, 95, 1, -29, -67, 62, 96, -126, -36, 35, 125, -97, -63, 66, 28, -2, -96, -31, -65, 93, 3, -128, -34, 60, 98, -66, -32, 2, 92, -33, -127, 99, 61, 124, 34, -64, -98, 29, 67, -95, -1, 70, 24, -6, -92, 39, 121, -101, -59, -124, -38, 56, 102, -27, -69, 89, 7, -37, -123, 103, 57, -70, -28, 6, 88, 25, 71, -91, -5, 120, 38, -60, -102, 101, 59, -39, -121, 4, 90, -72, -26, -89, -7, 27, 69, -58, -104, 122, 36, -8, -90, 68, 26, -103, -57, 37, 123, 58, 100, -122, -40, 91, 5, -25, -71, -116, -46, 48, 110, -19, -77, 81, 15, 78, 16, -14, -84, 47, 113, -109, -51, 17, 79, -83, -13, 112, 46, -52, -110, -45, -115, 111, 49, -78, -20, 14, 80, -81, -15, 19, 77, -50, -112, 114, 44, 109, 51, -47, -113, 12, 82, -80, -18, 50, 108, -114, -48, 83, 13, -17, -79, -16, -82, 76, 18, -111, -49, 45, 115, -54, -108, 118, 40, -85, -11, 23, 73, 8, 86, -76, -22, 105, 55, -43, -117, 87, 9, -21, -75, 54, 104, -118, -44, -107, -53, 41, 119, -12, -86, 72, 22, -23, -73, 85, 11, -120, -42, 52, 106, 43, 117, -105, -55, 74, 20, -10, -88, 116, 42, -56, -106, 21, 75, -87, -9, -74, -24, 10, 84, -41, -119, 107, 53};
    public static Handler f;

    public BackgroundService() {
    }

    public IBinder onBind(Intent var1) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        Log.d("BackgroundService", "BackgroundService");
        i = BluetoothAdapter.getDefaultAdapter();
        if (!i.isEnabled()) {
            i.enable();
        }

        h = new BluetoothService(this, f);
        if (h.a() == 0) {
            h.b();
        }

        if (this.g != null) {
            this.g.interrupt();
            this.g.b();
            this.g = null;
        }

        this.g = new AnalyseData(new SpotCallBack());
        this.g.setName("analyse thread");
        this.g.start();
    }

    public static void setHandler(Handler var0) {
        f = var0;
    }

    public void onDestroy() {
        if (this.g != null) {
            this.g.b();
            this.g = null;
        }

        super.onDestroy();
    }

    public static void connect(String var0) {
        f.obtainMessage(1, "Connecting").sendToTarget();
        BluetoothDevice var1 = i.getRemoteDevice(var0);
        h.a(var1, true);
        Log.d("BackgroundService", "Start connect");
    }

    public static void disconnect() {
        h.c();
    }

    public static void getDeviceID() {
        byte[] var0 = a;
        a(var0, var0.length);
        h.a(var0);
    }

    public static void getDeviceInformation() {
        byte[] var0 = b;
        a(var0, var0.length);
        h.a(var0);
    }

    public static void NIBPStrat() {
        byte[] var0 = c;
        a(var0, var0.length);
        h.a(var0);
    }

    public static void NIBPStop() {
        byte[] var0 = d;
        a(var0, var0.length);
        h.a(var0);
    }

    public static void a(byte[] var0, int var1) {
        byte var2 = 0;

        int var4;
        for(var4 = 0; var4 < var1 - 1; ++var4) {
            byte var3 = e[(var2 ^ var0[var4]) & 255];
            var2 = var3;
        }

        var0[var4] = var2;
    }

    public class SpotCallBack implements ISpotCheckCallBack {
        public SpotCallBack() {
        }

        public void a() {
            BackgroundService.f.obtainMessage(0).sendToTarget();
        }

        public void a(String var1) {
            BackgroundService.f.obtainMessage(2, var1).sendToTarget();
        }

        public void a(int var1, int var2, int var3, int var4, int var5, int var6) {
            String var7 = var1 + "." + var2;
            String var8 = var3 + "." + var4;
            String[] var9 = new String[]{var7, var8};
            BackgroundService.f.obtainMessage(3, var5, var6, var9).sendToTarget();
        }

        public void a(int var1) {
            BackgroundService.f.obtainMessage(4, var1).sendToTarget();
        }

        public void b() {
            BackgroundService.f.obtainMessage(10).sendToTarget();
        }

        public void a(boolean var1, int var2) {
            BackgroundService.f.obtainMessage(5, var2).sendToTarget();
        }

        public void a(boolean var1, int var2, int var3, int var4, int var5, int var6) {
            int[] var7 = new int[]{var2, var4, var5, var3};
            BackgroundService.f.obtainMessage(6, var7).sendToTarget();
        }

        public void a(int var1, int var2) {
            BackgroundService.f.obtainMessage(7, var1, var2).sendToTarget();
        }

        public void a(int var1, int var2, float var3, boolean var4, int var5) {
            BackgroundService.f.obtainMessage(8, var1, var2, var3).sendToTarget();
        }

        public void a(List<Wave> var1) {
            int[] var2 = new int[var1.size()];
            Iterator var3 = var1.iterator();

            for(int var4 = 0; var3.hasNext(); ++var4) {
                Wave var5 = (Wave)var3.next();
                var2[var4] = var5.a;
            }

            BackgroundService.f.obtainMessage(9, var2).sendToTarget();
        }
    }
}
