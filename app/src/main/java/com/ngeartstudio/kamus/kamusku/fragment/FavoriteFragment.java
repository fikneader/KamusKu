package com.ngeartstudio.kamus.kamusku.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.ngeartstudio.kamus.kamusku.utils.DatabaseHelper;
import com.ngeartstudio.kamus.kamusku.model.DictionaryModel;
import com.ngeartstudio.kamus.kamusku.utils.MyDividerItemDecoration;
import com.ngeartstudio.kamus.kamusku.R;
import com.ngeartstudio.kamus.kamusku.adapter.WordAdapterFav;

import java.io.File;
import java.util.Arrays;
import java.util.List;


public class FavoriteFragment extends Fragment {
    private AdView mAdView;
    private RecyclerView rvWord;
    private WordAdapterFav wordAdapterFav;
    private List<DictionaryModel> dictionaryModelList;
    private DatabaseHelper mDBHelper;
    TextView txt_nodata;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        txt_nodata = view.findViewById(R.id.txt_nodata);
        rvWord = view.findViewById(R.id.hasilfavorite);
        rvWord.setLayoutManager(new LinearLayoutManager(getContext()));

        Toolbar toolbar = view.findViewById(R.id.toolbarfav);
        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), resId);
        rvWord.setLayoutAnimation(animation);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        mDBHelper = new DatabaseHelper(getContext());

        File database = getContext().getDatabasePath(DatabaseHelper.DATABASE_NAME);

        mDBHelper.getReadableDatabase();

        rvWord.setHasFixedSize(true);
        rvWord.addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL,10));
        rvWord.setItemAnimator(new DefaultItemAnimator());
        dictionaryModelList = mDBHelper.getListWord2("");
        wordAdapterFav = new WordAdapterFav();
        wordAdapterFav.setData(dictionaryModelList);
        rvWord.setAdapter(wordAdapterFav);

        if (dictionaryModelList.isEmpty()){
            txt_nodata.setVisibility(View.VISIBLE);
        } else {
            txt_nodata.setVisibility(View.GONE);
        }

        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = view.findViewById(R.id.adViewFavourite);
//        List<String> testDeviceIds = Arrays.asList("A71F65B1BBB834E07F92CF70EA696C84");
//        RequestConfiguration configuration =
//                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
//        MobileAds.setRequestConfiguration(configuration);
//        new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("A71F65B1BBB834E07F92CF70EA696C84")).build();

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        return view;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.item_navigasi, menu);  // Use filter.xml from step 1
    }

}
