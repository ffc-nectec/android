package samplecode_north_vision_thermometer.AnalyseData;

public abstract interface IDataCallBack {

    public abstract void OnConnectionStatusChange(String status);
    public abstract void OnGetData(int temperatureUnitFlag, float data);
}
