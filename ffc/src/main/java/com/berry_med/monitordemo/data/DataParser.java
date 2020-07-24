package com.berry_med.monitordemo.data;


import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by ZXX on 2016/1/8.
 */
public class DataParser {

    //Const
    public String TAG = this.getClass().getSimpleName();

    //Buffer queue
    private LinkedBlockingQueue<Integer> bufferQueue   = new LinkedBlockingQueue<Integer>(256);
    private int[]         PACKAGE_HEAD        = new int[]{0x55,0xaa};
    private final int     PKG_ECG_WAVE        = 0x01;
    private final int     PKG_ECG_PARAMS      = 0x02;
    private final int     PKG_NIBP            = 0x03;
    private final int     PKG_SPO2_PARAMS     = 0x04;
    private final int     PKG_TEMP            = 0x05;
    private final int     PKG_SW_VER          = 0xfc;
    private final int     PKG_HW_VER          = 0xfd;
    private final int     PKG_SPO2_WAVE       = 0xfe;
    public static byte[]  CMD_START_NIBP = new byte[]{0x55, (byte) 0xaa, 0x04, 0x02, 0x01, (byte) 0xf8};
    public static byte[]  CMD_STOP_NIBP  = new byte[]{0x55, (byte) 0xaa, 0x04, 0x02, 0x00, (byte) 0xf9};
    public static byte[]  CMD_FW_VERSION = new byte[]{0x55, (byte) 0xaa, 0x04, (byte) 0xfc, 0x00, (byte) 0xff};
    public static byte[]  CMD_HW_VERSION = new byte[]{0x55, (byte) 0xaa, 0x04, (byte) 0xfd, 0x00, (byte) 0xfe};

    //Parse Runnable
    private ParseRunnable mParseRunnable;
    private boolean       isStop = true;

    private onPackageReceivedListener mListener;

    /**
     * interface for parameters changed.
     */
    public interface onPackageReceivedListener
    {
        void onSpO2WaveReceived(int dat);
        void onSpO2Received(SpO2 spo2);

        void onECGWaveReceived(int dat);
        void onECGReceived(ECG ecg);

        void onTempReceived(Temp temp);

        void onNIBPReceived(NIBP nibp);

        void onFirmwareReceived(String str);
        void onHardwareReceived(String str);
    }

    //Constructor
    public DataParser(onPackageReceivedListener listener) {
        this.mListener = listener;
    }

    public void start()
    {
        mParseRunnable = new ParseRunnable();
        new Thread(mParseRunnable).start();
    }

    public void stop()
    {
        isStop = true;
    }

    /**
     * ParseRunnable
     */
    class ParseRunnable implements Runnable {
        int dat;
        int[] packageData;
        @Override
        public void run() {
            while (isStop) {
                dat = getData();
                if(dat == PACKAGE_HEAD[0]){
                    dat = getData();
                    if(dat == PACKAGE_HEAD[1]) {
                        int packageLen = getData();
                        packageData = new int[packageLen + PACKAGE_HEAD.length];

                        packageData[0] = PACKAGE_HEAD[0];
                        packageData[1] = PACKAGE_HEAD[1];
                        packageData[2] = packageLen;

                        for (int i = 3; i < packageLen + PACKAGE_HEAD.length; i++) {
                            packageData[i] = getData();
                        }

                        if(CheckSum(packageData)){
                            ParsePackage(packageData);
                        }
                    }
                }
            }
        }
    }

    private void ParsePackage(int[] pkgData) {
        // TODO Auto-generated method stub
        int pkgType = pkgData[3];
        int[] tempBuffer = new int[5];

        switch (pkgType) {
            case PKG_ECG_WAVE:
                mListener.onECGWaveReceived(pkgData[4]);
                break;
            case PKG_SPO2_WAVE:
                mListener.onSpO2WaveReceived(pkgData[4]);
                break;
            case PKG_ECG_PARAMS:
                int heartRate = pkgData[5];
                ECG params = new ECG(heartRate,pkgData[6],pkgData[4]);
                mListener.onECGReceived(params);
                break;
            case PKG_NIBP:
                //Log.i(TAG, "pkg_nibp");
                NIBP params2 = new NIBP(pkgData[6],pkgData[7],pkgData[8],pkgData[5]*2,pkgData[4]);
                mListener.onNIBPReceived(params2);
                break;
            case PKG_SPO2_PARAMS:
                SpO2 params3 = new SpO2(pkgData[5],pkgData[6],pkgData[4]);
                mListener.onSpO2Received(params3);
                break;
            case PKG_TEMP:
                Temp params4 = new Temp((pkgData[5]*10 + pkgData[6])/10.0,pkgData[4]);
                mListener.onTempReceived(params4);
                break;
            case PKG_SW_VER:
                StringBuilder sb = new StringBuilder();
                for (int i = 4; i < pkgData.length-1; i++){
                    sb.append((char)(pkgData[i]&0xff));
                }
                mListener.onFirmwareReceived(sb.toString());
                break;
            case PKG_HW_VER:
                StringBuilder sb1 = new StringBuilder();
                for (int i = 4; i < pkgData.length-1; i++){
                    sb1.append((char)(pkgData[i]&0xff));
                }
                mListener.onHardwareReceived(sb1.toString());
                break;
            default:
                break;
        }

    }

    /**
     * Add the data received from USB or Bluetooth
     * @param dat
     */
    public void add(byte[] dat)
    {
        for(byte b : dat)
        {
            try {
                bufferQueue.put(toUnsignedInt(b));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //Log.i(TAG, "add: "+ bufferQueue.size());
    }

    /**
     * Get Dat from Queue
     * @return
     */
    private int getData()
    {
        int dat = 0;
        try {
            dat = bufferQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return dat;
    }
    private boolean CheckSum(int[] packageData) {
        // TODO Auto-generated method stub
        int sum = 0;
        for(int i = 2; i < packageData.length-1; i++)
        {
            sum+=(packageData[i]);
        }

        if(((~sum)&0xff) == (packageData[packageData.length-1]&0xff))
        {
            return true;
        }

        return false;
    }


    private int toUnsignedInt(byte x) {
        return ((int) x) & 0xff;
    }
}
