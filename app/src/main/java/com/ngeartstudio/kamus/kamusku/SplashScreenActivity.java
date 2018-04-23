package com.ngeartstudio.kamus.kamusku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mulaiAnimasiFadein();
        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{

                }
            }
        };
        timerThread.start();
    }

    //memanggil method untuk menjalankan animasi pada button onClick mulaiAnimasiFadein
    public void mulaiAnimasiFadein() {
        ImageView imageView = findViewById(R.id.imageView);
        TextView textView = findViewById(R.id.textView);
        //menjalankanya pada imageview
        Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
        //memanggil resource animasi di folder res anim fade in
        imageView.startAnimation(startAnimation);
        //mulai animasi
    }
}
