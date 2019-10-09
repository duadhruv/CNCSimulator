package com.example.cncsimulator;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class ProgrammedFragment extends Fragment {

    int noofcycles = 0;
    TextView cycleno,cycletime,jobtime ,cttxt,lutxt;
    Button addcycle,subcycle,jobstart,luadd,lusub,ctadd,ctsub;
    Boolean irregular=false;
    CheckBox irregularbox;
    int add[] = {0,5,7,0,5,7,0,5,7,0,5,7,0,5,7,0,5,7,0,5,7,0,5,7,0,5,7,0,5,7,0,5,7,0,5,7};
    int cycletimecount=0;
    int lutime = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_programmed,container,false);
        cycleno = view.findViewById(R.id.cycleno);
        addcycle = view.findViewById(R.id.cycleadd);
        subcycle = view.findViewById(R.id.cyclesub);
        jobstart=view.findViewById(R.id.jobstart);
        cycleno.setText(String.valueOf(noofcycles));
        irregularbox = view.findViewById(R.id.checkBox);
        cycletime = view.findViewById(R.id.cycletime);
        jobtime=view.findViewById(R.id.jobtime);
        luadd=view.findViewById(R.id.luadd);
        lusub = view.findViewById(R.id.lusub);
        lutxt=view.findViewById(R.id.lutxt);
        ctadd= view.findViewById(R.id.ctadd);
        ctsub=view.findViewById(R.id.lusub);
        cttxt=view.findViewById(R.id.cttxt);
        Ticker jobticker = new Ticker(jobtime);
        Ticker cycleticker = new Ticker(cycletime);

        ctadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cycletimecount++;
                cttxt.setText(String.valueOf(cycletimecount));
            }
        });

        ctsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cycletimecount!=0)
                {
                    cycletimecount--;
                }
                cttxt.setText(String.valueOf(cycletimecount));

            }
        });


        luadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lutime++;
                lutxt.setText(String.valueOf(lutime));

            }
        });

        lusub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lutime!=0)
                {
                    lutime--;
                }
                lutxt.setText(String.valueOf(lutime));
            }
        });


        irregularbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                irregular = irregularbox.isChecked();
            }
        });
        addcycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noofcycles++;
                cycleno.setText(String.valueOf(noofcycles));
            }
        });

        subcycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(noofcycles!=0)
                {
                    noofcycles--;
                }
                else
                {
                    noofcycles = 0;
                }
                cycleno.setText(String.valueOf(noofcycles));


            }
        });


        jobstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(true)
                {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<Integer> randoms = new ArrayList<>();
                            randoms = genrateRandoms(noofcycles);
                        }
                    }).start();


                }
            }
        });



        return view;
    }


    ArrayList<Integer> genrateRandoms(int range)
    {
       ArrayList<Integer> no = new ArrayList<>();
       Random r = new Random();
       for(int i = 0;i<range/4;i++)
       {
           int rno = r.nextInt(range);
           no.add(rno);
           Log.w("random",String.valueOf(rno));
       }
       return no;
    }
}
