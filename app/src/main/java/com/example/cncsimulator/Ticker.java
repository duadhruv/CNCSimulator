package com.example.cncsimulator;

import android.os.Handler;
import android.os.SystemClock;
import android.widget.TextView;

public  class Ticker{
    TextView timetxt;
    long MillisecondTime , StartTime, TimeBuff, UpdateTime = 0L ;
    Handler handler;
    int Seconds, Minutes, MilliSeconds ;
    Ticker(TextView timetxt)
    {
        this.timetxt = timetxt;
        handler = new Handler();
    }

    void start()
    {
        StartTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 0);
        //reset.setEnabled(false);
    }
    void stop()
    {
        TimeBuff += MillisecondTime;
        handler.removeCallbacks(runnable);
        //reset.setEnabled(true);

    }

    void reset()
    {
        MillisecondTime = 0L ;
        StartTime = 0L ;
        TimeBuff = 0L ;
        UpdateTime = 0L ;
        Seconds = 0 ;
        Minutes = 0 ;
        MilliSeconds = 0 ;

        timetxt.setText("00:00");
    }


    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;

            Seconds = (int) (UpdateTime / 1000);

            Minutes = Seconds / 60;

            Seconds = Seconds % 60;

            MilliSeconds = (int) (UpdateTime % 1000);

            timetxt.setText("" + String.format("%02d", Minutes) + ":"
                    + String.format("%02d", Seconds));

            handler.postDelayed(this, 0);
        }

    };
}

