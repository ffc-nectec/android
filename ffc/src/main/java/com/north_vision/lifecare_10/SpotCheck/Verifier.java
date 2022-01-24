//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.north_vision.lifecare_10.SpotCheck;

import java.util.Vector;

public class Verifier {
    private static byte a = -86;
    private static byte b = 85;
    private static byte[] c = new byte[]{0, 94, -68, -30, 97, 63, -35, -125, -62, -100, 126, 32, -93, -3, 31, 65, -99, -61, 33, 127, -4, -94, 64, 30, 95, 1, -29, -67, 62, 96, -126, -36, 35, 125, -97, -63, 66, 28, -2, -96, -31, -65, 93, 3, -128, -34, 60, 98, -66, -32, 2, 92, -33, -127, 99, 61, 124, 34, -64, -98, 29, 67, -95, -1, 70, 24, -6, -92, 39, 121, -101, -59, -124, -38, 56, 102, -27, -69, 89, 7, -37, -123, 103, 57, -70, -28, 6, 88, 25, 71, -91, -5, 120, 38, -60, -102, 101, 59, -39, -121, 4, 90, -72, -26, -89, -7, 27, 69, -58, -104, 122, 36, -8, -90, 68, 26, -103, -57, 37, 123, 58, 100, -122, -40, 91, 5, -25, -71, -116, -46, 48, 110, -19, -77, 81, 15, 78, 16, -14, -84, 47, 113, -109, -51, 17, 79, -83, -13, 112, 46, -52, -110, -45, -115, 111, 49, -78, -20, 14, 80, -81, -15, 19, 77, -50, -112, 114, 44, 109, 51, -47, -113, 12, 82, -80, -18, 50, 108, -114, -48, 83, 13, -17, -79, -16, -82, 76, 18, -111, -49, 45, 115, -54, -108, 118, 40, -85, -11, 23, 73, 8, 86, -76, -22, 105, 55, -43, -117, 87, 9, -21, -75, 54, 104, -118, -44, -107, -53, 41, 119, -12, -86, 72, 22, -23, -73, 85, 11, -120, -42, 52, 106, 43, 117, -105, -55, 74, 20, -10, -88, 116, 42, -56, -106, 21, 75, -87, -9, -74, -24, 10, 84, -41, -119, 107, 53};

    public Verifier() {
    }

    public static int a(Vector<Byte> var0) {
        int var1 = 0;
        if (var0.size() > 4) {
            int var2 = 0;
            int var3 = 0;
            boolean var4 = false;

            try {
                for(; var2 < var0.size() - 1; ++var2) {
                    boolean var5 = false;
                    if ((Byte)var0.get(var2) == a && (Byte)var0.get(var2 + 1) == b) {
                        var3 = var2 + 2;
                        var5 = true;
                    }

                    if (var3 >= var0.size() - 2) {
                        return var1;
                    }

                    if (var5) {
                        int var11 = (Byte)var0.get(var3 + 1) & 255;
                        boolean var6 = true;
                        int var12 = var3 + 2 + var11 - 1;
                        if (var0.size() <= var12) {
                            return var1;
                        }

                        try {
                            if (a(var0, var2, var12)) {
                                var2 = var12 + 1;
                                ++var1;
                            } else {
                                for(int var7 = var2; var7 < var12 + 1; ++var7) {
                                    var0.remove(var2);
                                }
                            }
                        } catch (Exception var9) {
                            var9.printStackTrace();

                            for(int var8 = var2; var8 < var12 + 1; ++var8) {
                                var0.remove(var2);
                            }
                        }
                    }
                }
            } catch (Exception var10) {
                var10.printStackTrace();
            }
        }

        return var1;
    }

    public static boolean a(Vector<Byte> var0, int var1, int var2) {
        boolean var3 = false;
        byte var4 = 0;

        for(int var5 = var1; var5 < var2; ++var5) {
            var4 = c[(var4 ^ (Byte)var0.get(var5)) & 255];
        }

        return var4 == (Byte)var0.get(var2);
    }
}
