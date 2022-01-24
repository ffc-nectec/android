//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.north_vision.lifecare_10.SpotCheck;

import com.north_vision.lifecare_10.base.BaseThread;
import com.north_vision.lifecare_10.base.Ireader;
import java.util.Vector;

public class Receive {
    private Ireader b;
    public static boolean BLUETOOTH_STATE = true;
    private ISpotCheckCallBack c;
    public static Vector<Byte> a = new Vector();

    protected class RecvThread extends BaseThread {
        private byte[] d;
        private Receive a;

        public void run() {
            super.run();
            this.setName(this.getClass().toString());
            Receive.BLUETOOTH_STATE = true;

            label91: {
                try {
                    synchronized(this) {
                        do {
                            if (this.c) {
                                this.wait();
                            }

                            int var2 = this.a.b.a(this.d);

                            for(int var3 = 0; var3 < var2; ++var3) {
                                Receive.a.add(this.d[var3]);
                            }
                        } while(!this.b && this.a.b != null);
                        break label91;
                    }
                } catch (Exception var10) {
                    this.a.c.a();
                    var10.printStackTrace();
                    Receive.BLUETOOTH_STATE = false;
                } finally {
                    Receive.BLUETOOTH_STATE = false;
                }

                return;
            }

            Receive.BLUETOOTH_STATE = false;
        }
    }
}
