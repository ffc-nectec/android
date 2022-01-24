//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.north_vision.lifecare_10.SpotCheck;

import com.north_vision.lifecare_10.base.BYTEO;
import com.north_vision.lifecare_10.base.BaseThread;
import com.north_vision.lifecare_10.base.Ianalyse;
import com.north_vision.lifecare_10.base.BaseData.Wave;
import java.util.ArrayList;

public class AnalyseData extends BaseThread implements Ianalyse {
    private ISpotCheckCallBack a;
    private long d = System.currentTimeMillis();
    private static int[][] e = new int[][]{{1, 2, 3, 4, 5, 6}, {2, 2, 3, 4, 5, 6}, {3, 3, 3, 4, 5, 6}, {4, 4, 4, 4, 5, 6}, {5, 5, 5, 5, 5, 6}, {7, 6, 6, 6, 6, 6}};

    public AnalyseData(ISpotCheckCallBack var1) {
        this.a = var1;
    }

    public void run() {
        super.run();

        try {
            while(!this.b) {
                synchronized(this) {
                    if (this.c) {
                        try {
                            this.wait();
                        } catch (InterruptedException var4) {
                            var4.printStackTrace();
                        }
                    }

                    if (Receive.a.size() > 0) {
                        this.a();
                    } else {
                        Thread.sleep(50L);
                    }
                }
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }

    public void a() throws Exception {
        int var1 = Verifier.a(Receive.a);

        try {
            for(int var5 = 0; var5 < var1; ++var5) {
                Receive.a.remove(0);
                Receive.a.remove(0);
                byte var2 = (Byte)Receive.a.remove(0);
                byte var3 = (Byte)Receive.a.remove(0);
                byte var4 = (Byte)Receive.a.remove(0);
                int var13;
                int var14;
                int var15;
                int var16;
                int var17;
                int var18;
                int var19;
                int var20;
                int var21;
                label91:
                switch(var2) {
                    case -48:
                        this.a.b();
                        break;
                    case -1:
                        switch(var4) {
                            case 1:
                                byte[] var26 = new byte[var3 - 2];

                                for(var13 = 0; var13 < var3 - 2; ++var13) {
                                    var26[var13] = (Byte)Receive.a.remove(0);
                                }

                                this.a.a(new String(var26));
                                break label91;
                            case 2:
                                byte var27 = (Byte)Receive.a.remove(0);
                                var14 = BYTEO.a((byte)var27);
                                var15 = BYTEO.b((byte)var27);
                                byte var30 = (Byte)Receive.a.remove(0);
                                var17 = BYTEO.a((byte)var30);
                                var18 = BYTEO.b((byte)var30);
                                var13 = (Byte)Receive.a.remove(0) & 255;
                                this.a.a(var17, var18, var14, var15, var13, 0);
                            case 3:
                            default:
                                break label91;
                        }
                    case 64:
                        this.a.a(var4);
                        break;
                    case 66:
                        int var12 = (Byte)Receive.a.remove(0) & 255;
                        var13 = (Byte)Receive.a.remove(0) & 255;
                        var14 = (var12 & 15) << 8;
                        var14 += var13;
                        var13 = var12 >>> 4 & 1;
                        this.a.a(var13 == 1, var14);
                        break;
                    case 67:
                        if (var3 == 7) {
                            var15 = (Byte)Receive.a.remove(0) & 255;
                            var16 = (Byte)Receive.a.remove(0) & 255;
                            var17 = var15 >>> 7 & 1;
                            var18 = ((var15 & 127) << 8) + var16;
                            var19 = (Byte)Receive.a.remove(0) & 255;
                            byte var29 = (Byte)Receive.a.remove(0);
                            var20 = var29 & 255;
                            var29 = (Byte)Receive.a.remove(0);
                            var21 = var29 & 255;
                            this.a.a(var17 == 1, var21, var19, var18, var20, a(var18, var20));
                        } else if (var3 == 3) {
                            var15 = (Byte)Receive.a.remove(0) & 255;
                            var16 = var15 >>> 7 & 255;
                            var17 = var15 & 127;
                            if (var16 == 0) {
                                this.a.a(0, var17);
                            } else {
                                this.a.a(1, var17);
                            }
                        }
                        break;
                    case 80:
                        if (var3 == 2) {
                            this.a.a(0, 0, 0.0F, false, 0);
                        } else if (var3 == 3) {
                            Receive.a.remove(0);
                        }
                        break;
                    case 81:
                        byte var6 = (Byte)Receive.a.remove(0);
                        int var7 = BYTEO.a((byte)var6);
                        int var8 = BYTEO.b((byte)var6);
                        byte var9 = (Byte)Receive.a.remove(0);
                        int var10 = BYTEO.a((byte)var9);
                        int var11 = BYTEO.b((byte)var9);
                        int var24 = (Byte)Receive.a.remove(0) & 255;
                        int var25 = (var24 & 240) >>> 4;
                        var24 &= 15;
                        if (var24 == 4) {
                            var24 = 3;
                        }

                        this.a.a(var10, var11, var7, var8, var24, var25);
                        break;
                    case 82:
                        if (var3 != 4) {
                            break;
                        }

                        ArrayList var28 = new ArrayList();

                        for(var16 = 0; var16 < 2; ++var16) {
                            Wave var31 = new Wave();
                            var18 = (Byte)Receive.a.remove(0) & 255;
                            var31.a = var18 & 127;
                            if (var18 > 127) {
                                var31.b = 1;
                            }

                            var28.add(var31);
                        }

                        this.a.a(var28);
                        break;
                    case 83:
                        var15 = (Byte)Receive.a.remove(0) & 255;
                        var16 = (Byte)Receive.a.remove(0) & 255;
                        var17 = (Byte)Receive.a.remove(0) & 255;
                        var18 = var16 + var17;
                        var19 = (Byte)Receive.a.remove(0) & 255;
                        var17 = (Byte)Receive.a.remove(0) & 255;
                        var20 = (var17 & 2) >>> 1;
                        var21 = (var17 & 192) >>> 6;
                        this.a.a(var15, var18, (float)var19 / 10.0F, var20 == 0, var21);
                        break;
                    default:
                        for(int var22 = 0; var22 < var3 - 2; ++var22) {
                            Receive.a.remove(0);
                        }
                }

                Receive.a.remove(0);
            }
        } catch (Exception var23) {
            var23.printStackTrace();
        }

    }

    public static int a(int var0) {
        byte var1 = 0;
        if (var0 < 120) {
            var1 = 0;
        } else if (var0 >= 120 && var0 < 130) {
            var1 = 1;
        } else if (var0 >= 130 && var0 < 139) {
            var1 = 2;
        } else if (var0 >= 140 && var0 < 159) {
            var1 = 3;
        } else if (var0 >= 160 && var0 < 179) {
            var1 = 4;
        } else if (var0 >= 180) {
            var1 = 5;
        }

        return var1;
    }

    public static int b(int var0) {
        byte var1 = 0;
        if (var0 < 80) {
            var1 = 0;
        } else if (var0 >= 80 && var0 <= 84) {
            var1 = 1;
        } else if (var0 >= 85 && var0 <= 89) {
            var1 = 2;
        } else if (var0 >= 90 && var0 <= 99) {
            var1 = 3;
        } else if (var0 >= 100 && var0 <= 109) {
            var1 = 4;
        } else if (var0 >= 110) {
            var1 = 5;
        }

        return var1;
    }

    public static int a(int var0, int var1) {
        boolean var2 = false;
        int var3 = a(var0);
        int var4 = b(var1);
        int var5 = e[var3][var4];
        return var5;
    }
}
