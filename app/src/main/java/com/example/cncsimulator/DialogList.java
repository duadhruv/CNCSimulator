package com.example.cncsimulator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class DialogList extends Activity {
    ListView lv;
    EditText search ;
    String[] machines;
    String[] temp;
    ArrayList<String> machine = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.dialoglist);
        Bundle extras = getIntent().getExtras();
       // machines = extras.getStringArray("data");
        temp = extras.getStringArray("data");
        machines = temp;
        lv = findViewById(R.id.list);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item,machines);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent resultIntent = new Intent();
                vibratebtn();
                resultIntent.putExtra("data", machines[position]);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });


        search = findViewById(R.id.searchtxt);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //adapter.getFilter().filter(s);
                machine.clear();
                for (int pos = 0; pos< temp.length; pos++)
                {
                    if(temp[pos].toLowerCase().contains(s))
                    {
                      machine.add(temp[pos]);
                        Log.w("list",temp[pos]);
                    }
                }
                machines = new String[machine.size()];
                machine.toArray(machines);
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item,machines);
                lv.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    public void vibratebtn()
    {
        //Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        //vibrator.vibrate(40);
    }


}
