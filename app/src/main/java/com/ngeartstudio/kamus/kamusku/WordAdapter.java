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
    public void setData(List<DictionaryModel> data){
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
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
            wordText = (TextView) itemView.findViewById(R.id.kataText);
            buttonFav = (Button) itemView.findViewById(R.id.buttonFav);
            final DatabaseHelper db = new DatabaseHelper(context);
            db.openDatabase();
            //int i = 0;


            buttonFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    DictionaryModel dictionaryModel = data.get(position);
                    db.updateFav(dictionaryModel.getId());
                    dictionaryModel.setFavorite("TRUE");
                    //Toast.makeText(view.getContext(), "Add to Favorite", Toast.LENGTH_SHORT).show();
                    Snackbar.make(view,"Add to Favorite",Snackbar.LENGTH_SHORT).show();

                    if (dictionaryModel.getFavorite().equals("TRUE")){
                        buttonFav.setBackgroundResource(R.drawable.bookmarkred);
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
                        definitionFragment.setArguments(bundle);
                    } else {
                        Toast.makeText(view.getContext(), dictionaryModel.getPengertian(), Toast.LENGTH_SHORT).show();
                    }
                    ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.content, definitionFragment).commit();

                }
            }

            );
            for (int i=0; i <= getAdapterPosition(); i++) {
                //int position = 0;
                DictionaryModel dictionaryModel = data.get(i);
                if (dictionaryModel.getFavorite().equals("TRUE")){
                    buttonFav.setBackgroundResource(R.drawable.bookmarkred);
                } else {
                    buttonFav.setBackgroundResource(R.drawable.bookmarkdedblack);
                }
            }
        }
    }
}
