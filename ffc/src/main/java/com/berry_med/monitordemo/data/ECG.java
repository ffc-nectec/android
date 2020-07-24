package com.berry_med.monitordemo.data;

/**
 * Created by ZXX on 2016/8/3.
 */

public class ECG {
    public int HEART_RATE_INVALID = 0;
    public int RESP_RATE_INVALID  = 0;

    private int heartRate;
    private int restRate;
    private int status;

    public ECG(int heartRate, int restRate, int status) {
        this.heartRate = heartRate;
        this.restRate = restRate;
        this.status = status;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public int getRestRate() {
        return restRate;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Heart Rate:" + (heartRate!=HEART_RATE_INVALID ? heartRate: "- -") +
                "  Resp Rate:"+(restRate!=RESP_RATE_INVALID ? restRate: "- -");
    }
}
