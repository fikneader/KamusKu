package com.ngeartstudio.kamus.kamusku;

import android.content.Context;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


/**
 * Created by fikneader on 1/30/2018.
 */

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder>{
    public List<DictionaryModel> data;
    public WordAdapter(){}
    public DatabaseHelper mDatabase;
    private int lastPosition = -1;
    Context context;
    public void setData(List<DictionaryModel> data){
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        mDatabase =  new DatabaseHelper(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View wordView = inflater.inflate(R.layout.word_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(wordView,context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DictionaryModel dictionaryModel = data.get(position);
        holder.wordText.setText(dictionaryModel.getKata());
        setAnimation(holder.wordText, position);
        if (dictionaryModel.getFavorite().equals("TRUE")) {
            holder.buttonFav.setBackgroundResource(R.drawable.notebookred);
        } else {
            holder.buttonFav.setBackgroundResource(R.drawable.notebook);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public Context context;
        public TextView wordText;
        public Button buttonFav;
        public ViewHolder(View itemView, final Context context) {
            super(itemView);
            this.context = context;
            wordText = itemView.findViewById(R.id.kataText);
            buttonFav = itemView.findViewById(R.id.buttonFav);
            final DatabaseHelper db = new DatabaseHelper(context);
            db.openDatabase();

            buttonFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    DictionaryModel dictionaryModel = data.get(position);

                    if (dictionaryModel.getFavorite().equals("FALSE")){
                        buttonFav.setBackgroundResource(R.drawable.notebookred);
                        dictionaryModel.setFavorite("TRUE");
                        db.updateFav(dictionaryModel.getId());
                        //Toast.makeText(view.getContext(), "Add to Favorite", Toast.LENGTH_SHORT).show();
                        Snackbar.make(view,"Tambah ke Kata Favorit",Snackbar.LENGTH_SHORT).show();
                    } else if (dictionaryModel.getFavorite().equals("TRUE")){
                        buttonFav.setBackgroundResource(R.drawable.notebook);
                        dictionaryModel.setFavorite("FALSE");
                        db.RemoveFav(dictionaryModel.getId());
                        //Toast.makeText(view.getContext(), "Add to Favorite", Toast.LENGTH_SHORT).show();
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
                  //  ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.content, homeFragment).commit();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    DictionaryModel dictionaryModel = data.get(position);

                    DefinitionFragment definitionFragment = new DefinitionFragment();
                    Bundle bundle = new Bundle();
                    if(bundle != null){
                        bundle.putString("KATA",dictionaryModel.getKata());
                        bundle.putString("PENGERTIAN",dictionaryModel.getPengertian());
                        bundle.putString("GAMBAR",dictionaryModel.getGambar());
                        bundle.putString("FAVORITE",dictionaryModel.getFavorite());
                        bundle.putString("ID",dictionaryModel.getId());
                        definitionFragment.setArguments(bundle);
                    } else {
                        Toast.makeText(view.getContext(), dictionaryModel.getPengertian(), Toast.LENGTH_SHORT).show();
                    }
                    ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.content, definitionFragment).commit();

                }
            }

            );
        }

    }
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.item_animation_from_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
