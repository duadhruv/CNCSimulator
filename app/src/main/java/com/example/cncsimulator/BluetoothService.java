package com.example.cncsimulator;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class BluetoothService extends Service {
    private  final IBinder btbinder = new LocalBinder();
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    String MY_PREFS_NAME = "mypref";
    InputStream inputStream;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    String btadd;
    String data;
    boolean registered = false;
    public BluetoothService() {
        Log.w("btservice","service started");

    }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                //Do something if connected

            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                //Do something if disconnected
                isBtConnected = false;

            }

            //else if...
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return btbinder;
    }
    public void refresh()
    {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        btadd = prefs.getString("btadd", "00:21:13:00:E8:4F");//"No name defined" is the default value.
        Log.w("btservice","refresh");
    }
/////////////////////////////////////////////////////////////////////////

    public void connectBT()
    {

                        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                        btadd = prefs.getString("btadd", "null");//"No name defined" is the default value.
                        Log.w("thread",btadd);
                        if(!btadd.equals("null")) {
                            try {

                                myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                                BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(btadd);//connects to the device's address and checks if it's available
                                btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                                btSocket.connect();//start connection
                                isBtConnected = true;

                                Log.w("btservice", "connected");


                            } catch (IOException e) {
                                Log.w("btservice", "not connected");
                                isBtConnected = false;//if the try failed, you can check the exception here
                            }
                        }
                    }


/////////////////////////////////////////////////////////////////////////





    public boolean isConnected()
    {

        return isBtConnected;
    }

    public BluetoothSocket getSocket()
    {
        if(isBtConnected) {
            return btSocket;
        }
        else return null;
    }
    public void  unregister(){
        if(registered) {
            this.unregisterReceiver(mReceiver);
        }
    }
    public boolean isbtOn()
    {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return mBluetoothAdapter.isEnabled();
    }
    public  void register()
    {
        IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        IntentFilter filter3 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        //this.registerReceiver(mReceiver, filter1);
        //this.registerReceiver(mReceiver, filter2);
        this.registerReceiver(mReceiver, filter3);
        registered = true;

    }




    public class LocalBinder extends Binder
    {
        BluetoothService getService(){
            return BluetoothService.this;
        }
    }
}
