package com.ngeartstudio.kamus.kamusku;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.SearchManager;
import android.support.v7.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvWord;
    private WordAdapter wordAdapter;
    private List<DictionaryModel> dictionaryModelList;
    private DatabaseHelper mDBHelper;
    private Toolbar toolbar;

    private static final String SELECTED_ITEM = "arg_selected_item";
    private BottomNavigationView mBottomNav;
    private int mSelectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvWord = (RecyclerView) findViewById(R.id.hasilcari);
        rvWord.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mDBHelper = new DatabaseHelper(this);

        File database = getApplicationContext().getDatabasePath(DatabaseHelper.DATABASE_NAME);
/*        if (database.exists() == false){
            mDBHelper.getReadableDatabase();
                if (copyDatabase(this)){
                    Toast.makeText(getApplicationContext(),"Copy Success",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),"Copy Failed",Toast.LENGTH_LONG).show();
                    return;
                }
        }*/
        mDBHelper.getReadableDatabase();
        copyDatabase(this);
        //Toast.makeText(getApplicationContext(),"Copy Success",Toast.LENGTH_LONG).show();
        dictionaryModelList = mDBHelper.getListWord("");
        wordAdapter = new WordAdapter();
        wordAdapter.setData(dictionaryModelList);
        rvWord.setAdapter(wordAdapter);

        SearchView searchView = (SearchView) findViewById(R.id.carikata);
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

        toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        setupToolbar();
    }

    protected void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item_navigasi, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.w("ANDROID MENU TUTORIAL:", "onOptionsItemSelected(MenuItem item)");

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu1:
                Intent intent = new Intent(MainActivity.this, IntroActivity.class);
                startActivity(intent);
                //Toast.makeText(getApplicationContext(), "Menu 2", Toast.LENGTH_LONG).show();
                return true;
//            case R.id.menu2:
//                Intent intentAbout = new Intent(MainActivity.this, AboutActivity.class);
//                startActivity(intentAbout);
////                Toast.makeText(getApplicationContext(), "About", Toast.LENGTH_LONG).show();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

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
            Log.w("database","Copy Success");
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
}
