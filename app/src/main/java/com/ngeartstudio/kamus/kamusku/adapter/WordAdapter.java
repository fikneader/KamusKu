package com.ngeartstudio.kamus.kamusku.adapter;

import android.content.Context;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import com.ngeartstudio.kamus.kamusku.utils.DatabaseHelper;
import com.ngeartstudio.kamus.kamusku.model.DictionaryModel;
import com.ngeartstudio.kamus.kamusku.R;
import com.ngeartstudio.kamus.kamusku.activity.DefinitionActivity;
import com.ngeartstudio.kamus.kamusku.fragment.HomeFragment;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder>{
    public List<DictionaryModel> data;
    public WordAdapter(){}
    public DatabaseHelper mDatabase;
    private int lastPosition = -1;
    Context context;
    public void setData(List<DictionaryModel> data){
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        mDatabase =  new DatabaseHelper(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View wordView = inflater.inflate(R.layout.word_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(wordView, context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final DictionaryModel dictionaryModel = data.get(position);
        final DatabaseHelper db = new DatabaseHelper(context);
        db.openDatabase();

        holder.wordText.setText(dictionaryModel.getKata());
        setAnimation(holder.wordText, position);

        if (dictionaryModel.getFavorite().equals("TRUE")) {
            holder.buttonFav.setBackgroundResource(R.drawable.notebookred);
        } else {
            holder.buttonFav.setBackgroundResource(R.drawable.notebook);
        }

        holder.buttonFav.setOnClickListener(view -> {
            try {
                    if (dictionaryModel.getFavorite().equals("FALSE")){
                        holder.buttonFav.setBackgroundResource(R.drawable.notebookred);
                        dictionaryModel.setFavorite("TRUE");
                        db.updateFav(dictionaryModel.getId());
                        Snackbar.make(view,"Tambah ke Kata Favorit",Snackbar.LENGTH_SHORT).show();
                    } else if (dictionaryModel.getFavorite().equals("TRUE")){
                        holder.buttonFav.setBackgroundResource(R.drawable.notebook);
                        dictionaryModel.setFavorite("FALSE");
                        db.RemoveFav(dictionaryModel.getId());
                        Snackbar.make(view,"Hapus dari Kata Favorit",Snackbar.LENGTH_SHORT).show();
                    }
                    HomeFragment homeFragment = new HomeFragment();
                    Bundle bundle = new Bundle();
                    if(bundle != null){
                        bundle.putString("FAV",dictionaryModel.getFavorite());
                        homeFragment.setArguments(bundle);
                    } else {
                        Toast.makeText(view.getContext(), dictionaryModel.getPengertian(), Toast.LENGTH_SHORT).show();
                    }
            } catch (Exception e){
                Log.i("Kamusku",e.toString());
            }
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
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public Context context;
        public TextView wordText;
        public Button buttonFav;
        public ViewHolder(View itemView, final Context context) {
            super(itemView);
            this.context = context;
            wordText = itemView.findViewById(R.id.kataText);
            buttonFav = itemView.findViewById(R.id.buttonFav);
        }
    }
    private void setAnimation(View viewToAnimate, int position)
    {
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.item_animation_from_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
