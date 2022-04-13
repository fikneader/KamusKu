package com.ngeartstudio.kamus.kamusku.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.ads.MobileAds;
import com.google.android.material.snackbar.Snackbar;
import com.ngeartstudio.kamus.kamusku.activity.DefinitionActivity;
import com.ngeartstudio.kamus.kamusku.utils.DatabaseHelper;
import com.ngeartstudio.kamus.kamusku.model.DictionaryModel;
import com.ngeartstudio.kamus.kamusku.R;
import com.ngeartstudio.kamus.kamusku.fragment.FavoriteFragment;
import com.ngeartstudio.kamus.kamusku.fragment.HomeFragment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;



public class WordAdapterFav extends RecyclerView.Adapter<WordAdapterFav.ViewHolder>{
    public List<DictionaryModel> data;
    public WordAdapterFav(){}
    public DatabaseHelper mDatabase;
    Context context;

    public void setData(List<DictionaryModel> data){
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        mDatabase =  new DatabaseHelper(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View wordView = inflater.inflate(R.layout.word_fav,parent,false);
        ViewHolder viewHolder = new ViewHolder(wordView,context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final DictionaryModel dictionaryModel = data.get(position);
        final DatabaseHelper db = new DatabaseHelper(context);
        db.openDatabase();
        holder.wordText.setText(dictionaryModel.getKata());
        holder.wordText2.setText(dictionaryModel.getPengertian());

        MobileAds.initialize(context, initializationStatus -> {});

        holder.buttonFav.setOnClickListener(view -> {
            AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
            alertDialog.setTitle("Pemberitahuan");
            alertDialog.setMessage("Hapus Kata Favorit ?");
            alertDialog.setCancelable(false);
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ya",
                    (dialog, which) -> {
                        try {
                            db.RemoveFav(dictionaryModel.getId());
                            dictionaryModel.setFavorite("FALSE");
                            Snackbar.make(view,"Hapus dari Kata Favorit",Snackbar.LENGTH_SHORT).show();
                            FavoriteFragment favoriteFragment = new FavoriteFragment();
                            HomeFragment homeFragment = new HomeFragment();
                            Bundle bundle = new Bundle();
                            if(bundle != null){
                                bundle.putString("FAV",dictionaryModel.getFavorite());
                                homeFragment.setArguments(bundle);
                            } else {
                                Toast.makeText(view.getContext(), dictionaryModel.getPengertian(), Toast.LENGTH_SHORT).show();
                            }
                            ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.content, favoriteFragment).commit();
                        } catch (Exception e){
                            Log.i("Kamusku",e.toString());
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Tidak",
                    (dialog, which) -> dialog.dismiss());
            alertDialog.show();
        });

        holder.itemView.setOnClickListener(view -> {
            Intent mIntent = new Intent(context, DefinitionActivity.class);
            mIntent.putExtra("KATA",dictionaryModel.getKata());
            mIntent.putExtra("PENGERTIAN",dictionaryModel.getPengertian());
            mIntent.putExtra("GAMBAR",dictionaryModel.getGambar());
            mIntent.putExtra("FAVORITE",dictionaryModel.getFavorite());
            mIntent.putExtra("ID",dictionaryModel.getId());
            Bundle bundle = new Bundle();
            if(bundle != null){
                bundle.putString("KATA",dictionaryModel.getKata());
                bundle.putString("PENGERTIAN",dictionaryModel.getPengertian());
                bundle.putString("GAMBAR",dictionaryModel.getGambar());
                bundle.putString("FAVORITE",dictionaryModel.getFavorite());
                bundle.putString("ID",dictionaryModel.getId());
            } else {
                Toast.makeText(view.getContext(), dictionaryModel.getPengertian(), Toast.LENGTH_SHORT).show();
            }
            context.startActivity(mIntent);

        }

        );
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public Context context;
        public TextView wordText;
        public TextView wordText2;
        public Button buttonFav;

        public ViewHolder(View itemView, final Context context) {
            super(itemView);
            this.context = context;
            wordText = itemView.findViewById(R.id.kataText);
            wordText2 = itemView.findViewById(R.id.pengertianText);
            buttonFav = itemView.findViewById(R.id.buttonFav);
        }
    }
}
