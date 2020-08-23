package com.example.graduate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.Totheking;
import com.example.activity.TTKMainActivity;

public class SplashActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Totheking ttk = (Totheking) getApplication();
        ttk.load(this);

        try{
            Thread.sleep(2000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        startActivity(new Intent(this, TTKMainActivity.class));

        finish();
    }
}
