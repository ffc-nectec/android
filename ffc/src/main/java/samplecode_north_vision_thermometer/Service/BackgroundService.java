package samplecode_north_vision_thermometer.Service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import samplecode_north_vision_thermometer.AnalyseData.AnalyseDataThread;
import samplecode_north_vision_thermometer.AnalyseData.IDataCallBack;
import samplecode_north_vision_thermometer.bluetooth.BluetoothLeService;
import samplecode_north_vision_thermometer.utils.Constants;

public class BackgroundService extends Service {

	private static final String TAG = "BackgroundService";

	private static BluetoothLeService mBluetoothLeService;

	public static int status = 0; // 0 = disconnected; 1 = connecting; 2 = connected

	public static final String BLUETOOTH_DISCONNECT = "Disconnect";
	public static final String BLUETOOTH_CONNECTING = "Connecting";
	public static final String BLUETOOTH_CONNECTED = "Connected";

	public static final int CONNECTIONSTATUSCHANGE = 1;
	public static final int DATA = 2;

	public static Handler mHandler;

	public static IDataCallBack dataCallBack;
	public static final Vector<Byte> originalData = new Vector<>();
	private AnalyseDataThread analyseDataThread;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "BackgroundService");

		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		if (!mBluetoothAdapter.isEnabled()) {
			mBluetoothAdapter.enable();
		}

		IntentFilter filter = new IntentFilter(BluetoothLeService.ACTION_GATT_CONNECTED);
		filter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
		filter.addAction(BluetoothLeService.ACTION_GATT_CONNECTING);
		filter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
		filter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
		filter.addAction(Constants.INTENTFILTER_DISCONNECT);
		registerReceiver(receiver, filter);

		Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
		bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

		dataCallBack = new DataCallBack();
	}

	// Code to manage Service lifecycle.
	private final ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName componentName, IBinder service) {
			mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
			if (!mBluetoothLeService.initialize()) {
				Log.e(TAG, "Unable to initialize Bluetooth");
			}
			else {
//				mBluetoothLeService.setCallBack(dataCallBack);
				Log.d(TAG, "Set callback");
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			mBluetoothLeService = null;
		}
	};

	public static void setHandler(Handler _handler)
	{
		mHandler = _handler;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		try {
			if (mBluetoothLeService != null) {
				mBluetoothLeService.disconnect();
			}
		}catch (Exception ex)
		{
			ex.printStackTrace();
		}

		mBluetoothLeService = null;
		unregisterReceiver(receiver);
		unbindService(mServiceConnection);
		Log.d(TAG, "BackgroundService onDestroy");
	}

	public static void connect(String address)
	{
		dataCallBack.OnConnectionStatusChange(BLUETOOTH_CONNECTING);
		mBluetoothLeService.connect(address);
		Log.d(TAG, "Start connect");
	}

	public static void disconnect()
	{
		status = 0;
		mBluetoothLeService.disconnect();
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@SuppressLint("NewApi")
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			String address = intent.getStringExtra(Constants.BROADCAST_ADDRESS);
			assert action != null;
			switch (action) {
				case BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED:
					Log.d(TAG, "Connected");

					List<BluetoothGattService> gattServices = mBluetoothLeService.getSupportedGattServices();
					Log.d(TAG, "gattServices");

					if(gattServices == null || gattServices.size() == 0)
					{
						mBluetoothLeService.disconnect();
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						mBluetoothLeService.connect(address);
						break;
					}
					else if(status != 2)
					{
						Log.d(TAG, "gattServices != null  size = " + gattServices.size());
						for (int i = 0 ; i < gattServices.size() ; i++) {
							BluetoothGattService gattService = gattServices.get(i);
							Log.d(TAG, gattService.getUuid().toString());
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							if (gattService.getUuid().equals(BluetoothLeService.DATA_SERVICE_UUID)) {

								List<BluetoothGattCharacteristic> gattCharacteristics =
										gattService.getCharacteristics();

								for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
									if (gattCharacteristic.getUuid().toString()
											.equals(mBluetoothLeService.DATA_LINE_UUID.toString())) {
										Log.d(TAG, "DATA_LINE_UUID = " + gattCharacteristic.getUuid().toString());

										final int charaProp = gattCharacteristic.getProperties();
										if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {

											Log.d(TAG, gattCharacteristic.getUuid().toString());

											mBluetoothLeService.setCharacteristicNotification(gattCharacteristic, true);

											Log.d(TAG, "Connect Successed");

											dataCallBack.OnConnectionStatusChange(BLUETOOTH_CONNECTED);
											status = 2;

											//originalData.clear();
											if(analyseDataThread != null)
											{
												try{
													analyseDataThread.interrupt();
													analyseDataThread = null;
												}catch(Exception e)
												{
													e.printStackTrace();
												}
												analyseDataThread = null;
											}
											analyseDataThread = new AnalyseDataThread();
											analyseDataThread.setDataCallBack(dataCallBack);
											analyseDataThread.start();
										}
										break;
									}
								}
							}
						}
					}
					break;
				case BluetoothLeService.ACTION_GATT_DISCONNECTED:
					Log.d(TAG, "Disconnected " + address);
					status = 0;
					dataCallBack.OnConnectionStatusChange(BLUETOOTH_DISCONNECT);
					mBluetoothLeService.disconnect();

					if(analyseDataThread != null)
					{
						try{
							analyseDataThread.interrupt();
							analyseDataThread = null;
						}catch(Exception e)
						{
							e.printStackTrace();
						}
						analyseDataThread = null;
					}
					break;
				case BluetoothLeService.ACTION_GATT_CONNECTING:
					status = 1;
					dataCallBack.OnConnectionStatusChange(BLUETOOTH_CONNECTING);
					break;
				case BluetoothLeService.ACTION_DATA_AVAILABLE:
					byte[] data = intent.getByteArrayExtra("Data");
					String log_msg = Arrays.toString(data);
					synchronized (this)
					{
						assert data != null;
						for (byte aBuffer : data) originalData.add((byte) (aBuffer & 0xFF));
					}
					break;
			}
		}
	};

	public static class DataCallBack implements IDataCallBack {

		@Override
		public void OnConnectionStatusChange(String status) {
			mHandler.obtainMessage(CONNECTIONSTATUSCHANGE, status).sendToTarget();
		}

		@Override
		public void OnGetData(int temperatureUnitFlag, float data) {
			mHandler.obtainMessage(DATA, temperatureUnitFlag, 0, data).sendToTarget();
		}
	}
}
