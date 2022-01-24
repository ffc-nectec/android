//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.north_vision.lifecare_10.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.north_vision.lifecare_10.SpotCheck.Receive;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Vector;

public class BluetoothService {
    private static final UUID b = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final UUID c = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private final BluetoothAdapter d;
    private BluetoothService.a e;
    private BluetoothService.b f;
    private int g;
    public static final int STATE_NONE = 0;
    public static final int STATE_LISTEN = 1;
    private ArrayList<byte[]> h = new ArrayList();
    private Vector<Byte> i = new Vector();
    private OutputStream j;
    private InputStream k;
    private BluetoothSocket l;
    private Context m;
    public static Handler a;

    public BluetoothService(Context var1, Handler var2) {
        this.m = var1;
        a = var2;
        this.d = BluetoothAdapter.getDefaultAdapter();
        this.g = 0;
    }

    private synchronized void a(int var1) {
        Log.d("BluetoothService", "setState() " + this.g + " -> " + var1);
        this.g = var1;
    }

    public synchronized int a() {
        return this.g;
    }

    public synchronized void b() {
        Log.d("BluetoothService", "start");
        if (this.e != null) {
            this.e.a();
            this.e = null;
        }

        if (this.f != null) {
            this.f.a();
            this.f = null;
        }

        this.h.clear();
        this.i.clear();
        this.a(0);
    }

    public synchronized void a(BluetoothDevice var1, boolean var2) {
        Log.d("BluetoothService", "connect to: " + var1);
        if (this.g == 2 && this.e != null) {
            this.e.a();
            this.e = null;
        }

        if (this.f != null) {
            this.f.a();
            this.f = null;
        }

        this.e = new a(var1, var2);
        this.e.start();
        this.a(2);
    }

    public void c() {
        if (this.k != null) {
            try {
                this.k.close();
            } catch (Exception var4) {
                var4.printStackTrace();
            }

            this.k = null;
        }

        if (this.j != null) {
            try {
                this.j.close();
            } catch (Exception var3) {
                var3.printStackTrace();
            }

            this.j = null;
        }

        if (this.l != null) {
            try {
                this.l.close();
            } catch (Exception var2) {
                var2.printStackTrace();
            }

            this.l = null;
        }

    }

    private synchronized void a(BluetoothSocket var1, BluetoothDevice var2, String var3) {
        Log.d("BluetoothService", "connected, Socket Type:" + var3);
        if (this.e != null) {
            this.e.a();
            this.e = null;
        }

        if (this.f != null) {
            this.f.a();
            this.f = null;
        }

        this.f = new b(var1, var3);
        this.f.start();
        a.obtainMessage(1, "Connected").sendToTarget();
        this.a(3);
    }

    public void a(byte[] var1) {
        BluetoothService.b var2;
        synchronized(this) {
            if (this.g != 3) {
                return;
            }

            var2 = this.f;
        }

        var2.a(var1);
    }

    private void f() {
        a.obtainMessage(1, "ConnectFail").sendToTarget();
        this.b();
    }

    private void g() {
        a.obtainMessage(1, "ConnectLost").sendToTarget();
        this.b();
    }

    private class b extends Thread {
        private final BluetoothSocket b;
        private final DataInputStream c;

        public b(BluetoothSocket var2, String var3) {
            Log.d("BluetoothService", "create ConnectedThread: " + var3);
            this.b = var2;
            InputStream var4 = null;
            OutputStream var5 = null;

            try {
                var4 = var2.getInputStream();
                var5 = var2.getOutputStream();
            } catch (IOException var7) {
                Log.e("BluetoothService", "temp sockets not created", var7);
            }

            BluetoothService.this.k = var4;
            BluetoothService.this.j = var5;
            this.c = new DataInputStream(BluetoothService.this.k);
        }

        public void run() {
            Log.i("BluetoothService", "BEGIN mConnectedThread");
            byte var1 = 8;
            byte[] var2 = new byte[var1];

            while(true) {
                try {
                    int var3 = BluetoothService.this.k.read(var2);
                    synchronized(BluetoothService.this.i) {
                        for(int var5 = 0; var5 < var3; ++var5) {
                            Receive.a.add((byte)(var2[var5] & 255));
                        }
                    }
                } catch (IOException var8) {
                    BluetoothService.this.g();
                    BluetoothService.this.b();
                    Log.d("BluetoothService", "hello thread terminate.");
                    return;
                }
            }
        }

        public void a(byte[] var1) {
            try {
                DataOutputStream var2 = new DataOutputStream(BluetoothService.this.j);
                if (var1.length > 0) {
                    Log.d("var1-->",var1.toString());
                    var2.write(var1, 0, var1.length);
                }

                var2.flush();
            } catch (IOException var3) {
                Log.e("BluetoothService", "Exception during write", var3);
            }

        }

        public void a() {
            try {
                this.b.close();
            } catch (IOException var2) {
                Log.e("BluetoothService", "close() of connect socket failed", var2);
            }

        }
    }

    private class a extends Thread {
        private final BluetoothDevice b;
        private String c;

        @SuppressLint({"NewApi"})
        public a(BluetoothDevice var2, boolean var3) {
            this.b = var2;
            BluetoothSocket var4 = null;
            this.c = var3 ? "Secure" : "Insecure";

            try {
                if (var3) {
                    var4 = var2.createRfcommSocketToServiceRecord(BluetoothService.b);
                } else {
                    var4 = var2.createInsecureRfcommSocketToServiceRecord(BluetoothService.c);
                }
            } catch (IOException var6) {
                Log.e("BluetoothService", "Socket Type: " + this.c + "create() failed", var6);
            }

            BluetoothService.this.l = var4;
        }

        public void run() {
            Log.i("BluetoothService", "BEGIN mConnectThread SocketType:" + this.c);
            this.setName("ConnectThread" + this.c);
            BluetoothService.this.d.cancelDiscovery();

            try {
                BluetoothService.this.l.connect();
            } catch (IOException var6) {
                try {
                    BluetoothService.this.l.close();
                } catch (IOException var4) {
                    Log.e("BluetoothService", "unable to close() " + this.c + " socket during connection failure", var4);
                }

                BluetoothService.this.f();
                return;
            }

            synchronized(BluetoothService.this) {
                BluetoothService.this.e = null;
            }

            BluetoothService.this.a(BluetoothService.this.l, this.b, this.c);
        }

        public void a() {
            try {
                BluetoothService.this.l.close();
            } catch (IOException var2) {
                Log.e("BluetoothService", "close() of connect " + this.c + " socket failed", var2);
            }

        }
    }
}
