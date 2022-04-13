package com.ngeartstudio.kamus.kamusku.activity;

import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ngeartstudio.kamus.kamusku.R;
import com.ngeartstudio.kamus.kamusku.fragment.AboutFragment;
import com.ngeartstudio.kamus.kamusku.fragment.FavoriteFragment;
import com.ngeartstudio.kamus.kamusku.fragment.HomeFragment;
import com.ngeartstudio.kamus.kamusku.utils.GooglePlayStoreAppVersionNameLoader;
import com.ngeartstudio.kamus.kamusku.utils.WSCallerVersionListener;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements WSCallerVersionListener {
    public Toolbar toolbar;
    public BottomNavigationView navigationView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    transaction.replace(R.id.content, new HomeFragment()).commit();
                    return true;
                case R.id.navigation_favorite:
                    transaction.replace(R.id.content, new FavoriteFragment()).commit();
                    return true;
                case R.id.navigation_about:
                    transaction.replace(R.id.content, new AboutFragment()).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new GooglePlayStoreAppVersionNameLoader(getApplicationContext(), MainActivity.this).execute();
        navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content, new HomeFragment()).commit();

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu1:
                Intent intent = new Intent(MainActivity.this, IntroActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private static long back_pressed;
    @Override
    public void onBackPressed() {
        if (navigationView.getSelectedItemId() == R.id.navigation_home) {
            //super.onBackPressed();
            if (back_pressed + 2500 > System.currentTimeMillis()) super.onBackPressed();
            //else Toast.makeText(getBaseContext(), "Tekan kembali sekali lagi untuk keluar!", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
            super.onBackPressed();
        }
        else {
            navigationView.setSelectedItemId(R.id.navigation_home);
        }
    }

    @Override
    public void onGetResponse(boolean isUpdateAvailable) {
        if (isUpdateAvailable) {
            showUpdateDialog();
        }
    }

    public void showUpdateDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this,R.style.Theme_AppCompat_Light_Dialog_Alert);
        alertDialogBuilder.setTitle("Info Aplikasi");
        alertDialogBuilder.setMessage("Versi Terbaru Aplikasi Kamus Istilah Komputer dan Jaringan Sudah Tersedia di Playstore");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Update Sekarang", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                dialog.cancel();
            }
        });
        alertDialogBuilder.show();
    }
}
