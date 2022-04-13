
package com.ngeartstudio.kamus.kamusku.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ngeartstudio.kamus.kamusku.model.DictionaryModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static Context mContext;
    public static final String DATABASE_NAME = "kamus_jaringan_feb_2020.sqlite";
    public static final int DBVERSION = 3;
    public static final String DBLOCATION = "/data/data/com.ngeartstudio.kamus.kamusku"+"/databases/";
    public static final String QUERY = "Select * From kamus_tabel Where KATA Like ? ORDER BY KATA ASC";
    public static final String QUERY2 = "Select * From kamus_tabel Where KATA LIKE ? and FAVORITE LIKE 'TRUE%' ORDER BY KATA ASC";
    private SQLiteDatabase mDatabase;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DBVERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    public void openDatabase() {
        try {
            String dbPath = mContext.getDatabasePath(DATABASE_NAME).getPath();
            if (mDatabase != null && mDatabase.isOpen()) {
                return;
            }
            mDatabase = SQLiteDatabase.openDatabase(dbPath,null,SQLiteDatabase.OPEN_READWRITE);
        } catch (Exception e){
            Log.i("Kamusku", e.toString());
        }
    }

    public void closeDatabase(){
        if (mDatabase != null){
            mDatabase.close();
        }
    }

    public List<DictionaryModel> getListWord(String wordSearch){
        DictionaryModel dictionaryModel = null;
        List<DictionaryModel> dictionaryModelList = new ArrayList<>();
        openDatabase();
        String[] args = {"%" + wordSearch +"%"};

        Cursor cursor = mDatabase.rawQuery(QUERY,args);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            dictionaryModel = new DictionaryModel(cursor.getString(0),cursor.getString(1),cursor.getString(2), cursor.getString(3), cursor.getString(4));
            dictionaryModelList.add(dictionaryModel);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return dictionaryModelList;
    }

    public List<DictionaryModel> getListWord2(String wordSearch){
        DictionaryModel dictionaryModel = null;
        List<DictionaryModel> dictionaryModelList = new ArrayList<>();
        openDatabase();
        String[] args = {"%" + wordSearch +"%"};

        Cursor cursor = mDatabase.rawQuery(QUERY2,args);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            dictionaryModel = new DictionaryModel(cursor.getString(0),cursor.getString(1),cursor.getString(2), cursor.getString(3), cursor.getString(4));
            dictionaryModelList.add(dictionaryModel);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return dictionaryModelList;
    }



    public void updateFav(String id) {
        mDatabase = this.getWritableDatabase();
        String QUERY2 = "Update kamus_tabel set favorite = 'TRUE' where id =" + id;
        mDatabase.execSQL(QUERY2);
    }

    public void RemoveFav(String id) {
        mDatabase = this.getWritableDatabase();
        String QUERY2 = "Update kamus_tabel set favorite = 'FALSE' where id =" + id;
        mDatabase.execSQL(QUERY2);
    }

}
