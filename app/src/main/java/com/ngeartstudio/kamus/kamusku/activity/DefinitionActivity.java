package com.ngeartstudio.kamus.kamusku.activity;

import android.annotation.SuppressLint;
import android.content.Intent;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.ngeartstudio.kamus.kamusku.R;
import com.ngeartstudio.kamus.kamusku.model.DictionaryModel;
import com.ngeartstudio.kamus.kamusku.utils.DatabaseHelper;
import com.ngeartstudio.kamus.kamusku.utils.Helper;

import java.util.List;

public class DefinitionActivity extends AppCompatActivity {
    private InterstitialAd mInterstitialAd;
    private AdView mAdView;
    private Boolean isFabOpen = false;
    private FloatingActionButton fabshare, fabadd;
    private FloatingActionMenu fabmenu;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    public List<DictionaryModel> data;
    public DatabaseHelper db;
    String id,kata,sinonim,antonim,status,sinonim_dan_antonim;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definition);
        db = new DatabaseHelper(getApplicationContext());
        db.openDatabase();
        try {
            id = getIntent().getStringExtra("ID");
            kata = getIntent().getStringExtra("KATA");
            sinonim = getIntent().getStringExtra("SINONIM");
            antonim = getIntent().getStringExtra("ANTONIM");
            status = getIntent().getStringExtra("STATUS");
        } catch (Exception e){
            Helper.Log(e.toString());
        }

        MobileAds.initialize(this, initializationStatus -> {
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this,this.getResources().getString(R.string.id_adds_interstisial_definition), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                    }
                });

        fabmenu = findViewById(R.id.fab_menu);
        fabadd = findViewById(R.id.fabadd);
        fabshare = findViewById(R.id.fabshare);
        mAdView = findViewById(R.id.adViewDefinition);
        TextView txt_word = findViewById(R.id.kataText);
        TextView txt_desc = findViewById(R.id.pengertianText);
        Toolbar toolbar = findViewById(R.id.toolbardefinisi);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_foward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);

        mAdView = new AdView(this);

        mAdView.setAdSize(AdSize.BANNER);

        mAdView.setAdUnitId(this.getResources().getString(R.string.id_adds_interstisial_definition));
        mAdView.loadAd(adRequest);


        toolbar.setNavigationIcon(R.drawable.leftarrow);

        toolbar.setNavigationOnClickListener(view -> {
            try {
                if (mInterstitialAd != null) {
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent();
                            finish();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            super.onAdFailedToShowFullScreenContent(adError);
                            finish();
                        }
                    });
                    mInterstitialAd.show(DefinitionActivity.this);
                } else {
                    finish();
                }
            } catch (Exception e){
                Helper.Log(e.toString());
            }
        });

        fabmenu.setOnClickListener(view -> {
            try {
                if (mInterstitialAd != null) {
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent();
                            if (fabmenu.isOpened()) {
                                fabmenu.close(true);
                            }
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            super.onAdFailedToShowFullScreenContent(adError);
                            if (fabmenu.isOpened()) {
                                fabmenu.close(true);
                            }
                        }
                    });
                    mInterstitialAd.show(DefinitionActivity.this);
                } else {
                    if (fabmenu.isOpened()) {
                        fabmenu.close(true);
                    }
                }
            } catch (Exception e){
                Helper.Log(e.toString());
            }
        });

        txt_word.setText(kata);
        if (sinonim==null || sinonim.equals("")){
            sinonim = "Sinonim : - ";
        } else {
            sinonim = "Sinonim : " + sinonim;
        }

        if (antonim==null || antonim.equals("")){
            antonim = "Antonim : - ";
        } else {
            antonim = "Antonim : " + antonim;
        }

        sinonim_dan_antonim = sinonim + "\n \n" + antonim;

        txt_desc.setText(sinonim_dan_antonim);

        fabshare.setOnClickListener(view -> {
            String share = "Kata : " + kata + " \n \n" + sinonim_dan_antonim + " \n \n (Kamus Kosakata - INFINITEUNY) ";
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, share);
            sendIntent.setType("text/plain");
            Intent.createChooser(sendIntent, "Share via");
            startActivity(sendIntent);
        });

        fabadd.setOnClickListener(view -> {
            db.updateFav(id);
            Snackbar.make(view, "Menambahkan ke Kata Kesukaan", Snackbar.LENGTH_SHORT).show();

        });
    }

    @Override
    public void onBackPressed() {
        try {
            if (mInterstitialAd != null) {
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        finish();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        super.onAdFailedToShowFullScreenContent(adError);
                        finish();
                    }
                });
                mInterstitialAd.show(DefinitionActivity.this);
            } else {
                finish();
            }
        } catch (Exception e){
            Helper.Log(e.toString());
        }
    }
}