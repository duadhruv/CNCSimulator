package com.example.cncsimulator;

import android.icu.util.ValueIterator;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.example.cncsimulator.R;

public class ManualFragment extends Fragment {

    Button jobstart,jobend,startcycle,stopcycle;
    TextView timetxt,count;
    View manualLayout,jobstartlayout;
    int cyclecount = 0;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manual,container,false);


        jobstart =  view.findViewById(R.id.jobstart);
        jobend = view.findViewById(R.id.jobend);
        timetxt=view.findViewById(R.id.time);
        manualLayout=view.findViewById(R.id.manuallayout);
        jobstartlayout=view.findViewById(R.id.jobstartlayout);
        jobstartlayout.setVisibility(View.GONE);
        TextView jobtime= view.findViewById(R.id.jobtime);
        TextView cycletime= view.findViewById(R.id.cycletime);

        startcycle=view.findViewById(R.id.startcycle);
        stopcycle=view.findViewById(R.id.stopcycle);
        count=view.findViewById(R.id.count);


        final Ticker jobticker = new Ticker(jobtime);
        final Ticker cycleticker = new Ticker(cycletime);
        count.setText(String.valueOf(cyclecount));

        jobstart.bringToFront();


        startcycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopcycle.setEnabled(true);
                startcycle.setEnabled(false);
                cycleticker.reset();
                cycleticker.start();

            }
        });


        stopcycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cyclecount++;
                count.setText(String.valueOf(cyclecount));
                stopcycle.setEnabled(false);
                startcycle.setEnabled(true);
                cycleticker.stop();



            }
        });








        jobstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jobstart.setEnabled(false);
                jobend.setEnabled(true);
                startcycle.setVisibility(View.VISIBLE);
                stopcycle.setVisibility(View.VISIBLE);
                startcycle.setEnabled(true);

                manualLayout.setBackgroundResource(R.color.startColor);
                Animation fadeout = AnimationUtils.loadAnimation(getContext(), R.anim.fadeout);
                Animation fadein = AnimationUtils.loadAnimation(getContext(), R.anim.fadein);

                fadeout.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        jobstart.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                jobstart.setAnimation(fadeout);

                fadein.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        jobstartlayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                jobstartlayout.setAnimation(fadein);
                jobticker.start();
            }
        });

        jobend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jobend.setEnabled(false);
                jobstart.setEnabled(true);
                startcycle.setEnabled(false);
                stopcycle.setEnabled(false);
                startcycle.setVisibility(View.GONE);
                stopcycle.setVisibility(View.GONE);
                manualLayout.setBackgroundResource(R.color.stopColor);
                Animation fadeout = AnimationUtils.loadAnimation(getContext(), R.anim.fadeout);
                Animation fadein = AnimationUtils.loadAnimation(getContext(), R.anim.fadein);
                fadein.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        jobstart.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });


                fadeout.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        jobstartlayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                //jobstartlayout.setVisibility(View.GONE);
                jobstartlayout.setAnimation(fadeout);
                jobstart.setAnimation(fadein);
                jobticker.stop();
                jobticker.reset();
            }
        });



        return view ;
    }




}



class Ticker{
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


