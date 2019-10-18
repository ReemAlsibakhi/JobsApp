package com.example.reeme.gazajob;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {
    private int SPLASH_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
          Thread thread=new Thread(){
   @Override
    public  void run(){
        try {
            sleep(SPLASH_TIME);
            Intent intent =new Intent(SplashScreen.this,WelcomeActivity.class);
            startActivity(intent);
            finish();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
   }
};
            thread.start();
    }
}