package com.example.cncsimulator;

import android.app.Activity;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class ProgrammedFragment extends Fragment {

    int noofcycles = 0;
    TextView cycleno,cycletime,jobtime ,cttxt,lutxt;
    Button addcycle,subcycle,jobstart,luadd,lusub,ctadd,ctsub;
    Boolean irregular=false;
    CheckBox irregularbox;
    int add[] = {0,5,7,0,5,7,0,5,7,0,5,7,0,5,7,0,5,7,0,5,7,0,5,7,0,5,7,0,5,7,0,5,7,0,5,7};
    int cycletimecount=0;
    int lutime = 0;
    String MY_PREFS_NAME = "mypref";


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
        ctsub=view.findViewById(R.id.ctsub);
        cttxt=view.findViewById(R.id.cttxt);
        Ticker jobticker = new Ticker(jobtime);
        Ticker cycleticker = new Ticker(cycletime);
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        noofcycles = prefs.getInt("cyclecount", 0);
        cycletimecount=prefs.getInt("cycletimecount", 0);
        lutime=prefs.getInt("lutime", 0);
        cttxt.setText(String.valueOf(cycletimecount));
        lutxt.setText(String.valueOf(lutime));
        cycleno.setText(String.valueOf(noofcycles));


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

        final Activity main = ((MainActivity)getActivity());
        jobstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(((MainActivity) main).isBtConneced())
                {
                    String command = cycleno.getText().toString()+","+cttxt.getText().toString()+","+lutxt.getText().toString()+",1;";
                    ((MainActivity) main).sendString(command);
                    Log.w("command",command);
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putInt("cyclecount",noofcycles);
                    editor.putInt("cycletimecount",cycletimecount);
                    editor.putInt("lutime",lutime);

                    editor.apply();
                }
                else {
                    Toast.makeText(getContext(),"Not Connected",Toast.LENGTH_SHORT).show();
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
