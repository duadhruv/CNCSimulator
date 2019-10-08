package com.example.cncsimulator;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigationView = findViewById(R.id.navi);
        navigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        Fragment selected = null;
                        switch (menuItem.getItemId())
                        {
                            case R.id.manual:
                                selected = new ManualFragment();
                                break;
                            case R.id.countinous:
                                selected=new CountinousFragment();
                                break;
                            case R.id.programmed:
                                selected=new ProgrammedFragment();
                                break;

                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame,selected).commit();
                        return true;
                    }
                }
        );
    }
}
