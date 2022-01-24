//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.north_vision.lifecare_10.base;

import java.util.ArrayList;
import java.util.List;

public class BaseData {
    public static boolean isReadVer = false;

    public BaseData() {
    }

    public static class Wave {
        public int a;
        public int b;

        public Wave() {
        }

        public String toString() {
            return "Wave [data=" + this.a + ", flag=" + this.b + "]";
        }
    }

    public static class ECGData {
        public List<Wave> a = new ArrayList();

        public ECGData() {
        }
    }
}
