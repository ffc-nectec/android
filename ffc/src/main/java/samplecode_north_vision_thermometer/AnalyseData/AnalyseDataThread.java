package samplecode_north_vision_thermometer.AnalyseData;


import samplecode_north_vision_thermometer.Service.BackgroundService;

public class AnalyseDataThread extends Thread {

    private static final String TAG = "AnalyseDataThread";

    private IDataCallBack dataCallBack;

    public void setDataCallBack(IDataCallBack _dataCallBack) {
        this.dataCallBack = _dataCallBack;
    }

    @Override
    public void run() {
        super.run();
        while(BackgroundService.status == 2){
            synchronized (this)
            {
                if(BackgroundService.originalData.size() > 12) {
                    Analyse();
                }
            }
        }
    }

    private void Analyse()
    {
        try {
            byte byte0 = BackgroundService.originalData.remove(0);
            int temperatureUnitFlag = byte0 & 0x01;
            float temp;
            int temp_L = BackgroundService.originalData.remove(0) & 0xff;
            int temp_M = (BackgroundService.originalData.remove(0) << 8) & 0xff00;
            int temp_H = (BackgroundService.originalData.remove(0) << 16) & 0xff0000;
            int n = BackgroundService.originalData.remove(0);
            temp = (float) ((temp_L + temp_M + temp_H) * Math.pow(10.0, n));
            dataCallBack.OnGetData(temperatureUnitFlag, temp);

            int timeStamp = (byte0 >> 1) & 0x01;
            if(timeStamp == 1)
            {
                BackgroundService.originalData.remove(0);
                BackgroundService.originalData.remove(0);
                BackgroundService.originalData.remove(0);
                BackgroundService.originalData.remove(0);
                BackgroundService.originalData.remove(0);
                BackgroundService.originalData.remove(0);
                BackgroundService.originalData.remove(0);
                BackgroundService.originalData.remove(0);
            }

            int temperatureType = (byte0 >> 2) & 0x01;

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

