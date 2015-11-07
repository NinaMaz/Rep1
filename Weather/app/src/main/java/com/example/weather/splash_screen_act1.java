package com.example.weather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class splash_screen_act1 extends MainActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen1);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                    Intent mainIntent = new Intent(splash_screen_act1.this,MainActivity.class);
                    splash_screen_act1.this.startActivity(mainIntent);
                    splash_screen_act1.this.finish();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }


}

