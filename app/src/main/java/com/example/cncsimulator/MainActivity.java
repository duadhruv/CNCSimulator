package com.example.cncsimulator;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    int oldposn = 0;
    int newposn = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            oldposn = newPosition;
            return true;
        }

        return false;
    }

    public void startjob()
    {
        Toast.makeText(getApplicationContext(),"job started",Toast.LENGTH_SHORT).show();
    }

    public void endjob()
    {
        Toast.makeText(getApplicationContext(),"job ended",Toast.LENGTH_SHORT).show();
    }
    public void startcycle()
    {
        Toast.makeText(getApplicationContext(),"cycle started",Toast.LENGTH_SHORT).show();
    }
    public void endcycle()
    {
        Toast.makeText(getApplicationContext(),"cycle ended",Toast.LENGTH_SHORT).show();
    }
}
