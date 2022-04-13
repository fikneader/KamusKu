package com.ngeartstudio.kamus.kamusku.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.ngeartstudio.kamus.kamusku.utils.DatabaseHelper;
import com.ngeartstudio.kamus.kamusku.model.DictionaryModel;
import com.ngeartstudio.kamus.kamusku.utils.MyDividerItemDecoration;
import com.ngeartstudio.kamus.kamusku.R;
import com.ngeartstudio.kamus.kamusku.adapter.WordAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private RecyclerView rvWord;
    private WordAdapter wordAdapter;
    private List<DictionaryModel> dictionaryModelList;
    private DatabaseHelper mDBHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        rvWord = view.findViewById(R.id.hasilcari);
        rvWord.setLayoutManager(new LinearLayoutManager(getContext()));

        Toolbar toolbar = view.findViewById(R.id.toolbarhome2);

        setOverflowButtonColor(Objects.requireNonNull(getActivity()), getResources().getColor(R.color.white));
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        mDBHelper = new DatabaseHelper(getActivity());

        File database = Objects.requireNonNull(getContext()).getDatabasePath(DatabaseHelper.DATABASE_NAME);
        if(database.exists() == false){
            mDBHelper.getReadableDatabase();
            mDBHelper.close();
            if (copyDatabase(getContext())){
//                Toast.makeText(getContext(),"Copy Success",Toast.LENGTH_LONG).show();
            } else {
//                Toast.makeText(getContext(),"Copy Failed",Toast.LENGTH_LONG).show();
            }
        } else {
//            Toast.makeText(getContext(),"Database exits",Toast.LENGTH_LONG).show();
        }

        rvWord.setHasFixedSize(true);
        rvWord.addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL,10));
        rvWord.setItemAnimator(new DefaultItemAnimator());
        dictionaryModelList = mDBHelper.getListWord("");
        wordAdapter = new WordAdapter();
        wordAdapter.setData(dictionaryModelList);
        rvWord.setAdapter(wordAdapter);

        SearchView searchView = view.findViewById(R.id.carikata);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchWord(newText);
                return false;
            }
        });

        return view;
    }

    private void searchWord(String wordSearch){
        dictionaryModelList.clear();
        dictionaryModelList = mDBHelper.getListWord(wordSearch);
        wordAdapter.setData(dictionaryModelList);
        rvWord.setAdapter(wordAdapter);

    }

    private boolean copyDatabase(Context context){
        try {
            InputStream inputStream = context.getAssets().open(DatabaseHelper.DATABASE_NAME);
            String outFileName = DatabaseHelper.DBLOCATION + DatabaseHelper.DATABASE_NAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte [] buff = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buff)) > 0){
                outputStream.write(buff,0,length);
            }
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.item_navigasi, menu);  // Use filter.xml from step 1
    }

    public static void setOverflowButtonColor(final Activity activity, final int color) {
        final String overflowDescription = activity.getString(R.string.abc_action_menu_overflow_description);
        final ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        final ViewTreeObserver viewTreeObserver = decorView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final ArrayList<View> outViews = new ArrayList<View>();
                decorView.findViewsWithText(outViews, overflowDescription, View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                if (outViews.isEmpty()) {
                    return;
                }
                AppCompatImageView overflow = (AppCompatImageView) outViews.get(0);
                overflow.setColorFilter(color);
            }
        });
    }

}

