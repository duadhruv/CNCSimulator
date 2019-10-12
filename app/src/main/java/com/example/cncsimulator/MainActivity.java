package com.example.cncsimulator;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    int oldposn = 0;
    int newposn = 0;

    private BluetoothAdapter myBluetooth = null;
    BluetoothService btservice;
    boolean servicebinded = false;
    ImageView btimg;
    AnimationDrawable frameAnimation;
    boolean activtypaused=false;
    int REQUEST_BT_DEVICE = 161;
    Set<BluetoothDevice> pairedDevices ;
    String MY_PREFS_NAME = "mypref";
    String btselectedadd;
    String btselectedname;
    byte[] buffer = new byte[256];
    int bytes;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("CNC Simulator");
        //toolbar.setBackgroundResource(R.drawable.dbnotconnectedcolor);


        Intent o = new Intent(MainActivity.this,BluetoothService.class);
        bindService(o,btconnection, Context.BIND_AUTO_CREATE);


        btimg = findViewById(R.id.btimg);
        btimg.setBackgroundResource(R.drawable.spin_animation);
        frameAnimation = (AnimationDrawable) btimg.getBackground();
        frameAnimation.start();


        btimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!btservice.isbtOn()) {
                    Turnbton();
                }
            }
        });


        btimg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                myBluetooth = BluetoothAdapter.getDefaultAdapter();
                pairedDevices = myBluetooth.getBondedDevices();
                ArrayList bt_name_add = new ArrayList();
                if (pairedDevices.size() > 0)
                {
                    for (BluetoothDevice device :pairedDevices)
                    {
                        bt_name_add.add(device.getName() + "\n" + device.getAddress());
                    }
                    toolListDialog(bt_name_add,REQUEST_BT_DEVICE);
                }
                else {
                    Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });


        getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame, new ManualFragment()).commit();

        BottomNavigationView navigationView = findViewById(R.id.navi);
        navigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        Fragment selected = null;
                        oldposn=newposn;
                        switch (menuItem.getItemId())
                        {
                            case R.id.manual:
                                selected = new ManualFragment();
                                newposn=0;
                                break;
                            case R.id.programmed:
                                selected=new ProgrammedFragment();
                                newposn=1;
                                break;
                            case R.id.countinous:
                                selected=new CountinousFragment();
                                newposn=2;
                                break;

                        }
                        //getSupportFragmentManager().beginTransaction().replace(R.id.frame,selected).commit();
                        return loadFragment(selected,newposn);
                    }
                }
        );
    }


    public void Turnbton()
    {
        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        if (myBluetooth == null)
        {
            Toast.makeText(getApplicationContext(),"bluetooth not available",Toast.LENGTH_LONG).show();
            finish();
        }
        else if (!myBluetooth.isEnabled())
        {
            Intent turnbt = new Intent(myBluetooth.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnbt,1);
        }

    }



    public void toolListDialog(ArrayList<String> data , int request)
    {
        Intent intent=new Intent(MainActivity.this , DialogList.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        String[] data1 = new String[data.size()];
        data.toArray(data1);
        intent.putExtra("data",data1);
        startActivityForResult(intent,request);
    }





    private boolean loadFragment(Fragment fragment, int newPosition) {
        if(fragment != null) {
//            if(newPosition == 0) {
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.frame, fragment).commit();
//
//            }
            if(oldposn > newPosition) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right )
                        .replace(R.id.frame, fragment).commit();

            }
            if(oldposn < newPosition) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.frame, fragment).commit();

            }
            if(oldposn==0&& newposn==1)
            {
                sendString("end$;");
            }
            oldposn = newPosition;

            return true;
        }

        return false;
    }

    public void startjob()
    {
        Toast.makeText(getApplicationContext(),"job started",Toast.LENGTH_SHORT).show();
        sendString("js$;");
    }

    public void endjob()
    {
        sendString("je$;");
        Toast.makeText(getApplicationContext(),"job ended",Toast.LENGTH_SHORT).show();
    }
    public void startcycle()
    {
        sendString("cs$;");
        Toast.makeText(getApplicationContext(),"cycle started",Toast.LENGTH_SHORT).show();
    }
    public void endcycle()
    {
        sendString("ce$;");
        Toast.makeText(getApplicationContext(),"cycle ended",Toast.LENGTH_SHORT).show();
    }




    private ServiceConnection btconnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            BluetoothService.LocalBinder binder = (BluetoothService.LocalBinder) iBinder;
            btservice = binder.getService();
            servicebinded = true;
            btservice.register();
            if(!btservice.isConnected()){
                new ConnectBT().execute();
            }


        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };


////////////////////////////////////////////////////////////////////////////////////////////////////

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected


        @Override
        protected void onPreExecute()
        {

        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {

            if(!activtypaused) {
                //btservice.unregister();
                btservice.connectBT();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);
            if (!activtypaused) {
                if (btservice.isConnected()) {
                    //frameAnimation.stop();
                    btimg.setBackgroundResource(R.drawable.bluetooth_blue);
                    btconnectioncheck();
                    //readInput();
                    lineRead();
                    //beginRead();
                    //incomingdata();
                } else {

                    if (btservice.isbtOn()) {
                        new ConnectBT().execute();

                    } else {
                        btimg.setBackgroundResource(R.drawable.bluetooth_black);
                    }


                }


            }
            else {new ConnectBT().execute();}
        }

    }
    ///////////////////////////////////////////////////////////////////////////

    public void btconnectioncheck()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (!activtypaused) {
                        if (!btservice.isConnected()) {
                            new ConnectBT().execute();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btimg.setBackgroundResource(R.drawable.spin_animation);
                                    frameAnimation = (AnimationDrawable) btimg.getBackground();
                                    frameAnimation.start();
                                }
                            });

                            break;
                        }
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            if(requestCode == REQUEST_BT_DEVICE)
            {
                String info = data.getStringExtra("data");
                btselectedadd = info.substring(info.length() - 17);
                btselectedname = info.substring(0, info.length() - 18);
                //bttxt.setText(btselectedname);
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("btadd", btselectedadd);
                editor.apply();
                btservice.refresh();
            }
        }
    }

    public void sendString(final String string) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    btservice.getSocket().getOutputStream().write(string.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    void readInput()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        bytes = btservice.getSocket().getInputStream().read(buffer);            //read bytes from input buffer
                        String readMessage = new String(buffer, 0, bytes);
                        // Send the obtained bytes to the UI Activity via handler
                        Log.i("logging", readMessage + "");
                    } catch (IOException e) {
                        Log.e("logging", e + "");
                        //break;
                    }
                }
            }
        }).start();
    }



    public void incomingdata()
    {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true)
                {
                    InputStream inputStream = null;
                    if(btservice.isConnected()&& !activtypaused)
                    {

                        try {
                            BluetoothSocket btserviceSocket = btservice.getSocket();
                            if(btserviceSocket != null)
                            {
                                Log.w("thread","incoming data thread");
                                inputStream = btserviceSocket.getInputStream();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        finally {
                            if(inputStream != null) {
                                final String data = readUntilChar(inputStream, '\n');
                                Log.d("data", data);

                            }


                        }

                    }
                    else {break;}
                }

            }
        }).start();
    }


    public static String readUntilChar(InputStream stream, char target) {
        StringBuilder sb = new StringBuilder();

        try {
            BufferedReader buffer=new BufferedReader(new InputStreamReader(stream));

            int r;
            while ((r = buffer.read()) != -1) {
                char c = (char) r;

                if (c == target)
                    break;

                sb.append(c);
            }

            System.out.println(sb.toString());
        } catch(IOException e) {
            // Error handling
        }

        return sb.toString();
    }


    public void lineRead()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true)
                {
                    BufferedReader r = null;
                    try {
                        r = new BufferedReader(new InputStreamReader(btservice.getSocket().getInputStream()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String x = "";
                    try {
                        x = r.readLine();
                        Log.e("data", x);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(btconnection);
        servicebinded = false;
    }
    @Override
    public void onResume() {
        //btservice.register();
        super.onResume();
        activtypaused = false;
        if(servicebinded) {
            // new ConnectBT().execute();
        }
    }

    @Override
    public void onPause() {
        //btservice.unregister();
        activtypaused = true;
        super.onPause();
    }

    @Override
    protected void onStop() {
        try {
            btservice.unregister();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        super.onStop();
    }

}




