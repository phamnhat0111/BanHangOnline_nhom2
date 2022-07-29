package com.example.manager.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.manager.R;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Thread thread = new Thread(){
          public void run(){
              try {
                  sleep(5000);
              }catch (Exception ex){

              }finally {
                  Intent intent = new Intent(getApplicationContext(), DangNhapActivity.class);
                  startActivity(intent);
                  finish();
              }
          }
        };
        thread.start();
    }
}