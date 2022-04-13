package com.ngeartstudio.kamus.kamusku.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.ngeartstudio.kamus.kamusku.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mulaiAnimasiFadein();
        gotoMainActivity();
    }

    private void gotoMainActivity(){
        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(1000);
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }catch(InterruptedException e){
                    e.printStackTrace();
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
