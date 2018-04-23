package com.ngeartstudio.kamus.kamusku;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        DictionaryModel dictionaryModel = data.get(position);
        holder.wordText.setText(dictionaryModel.getKata());
        holder.wordText2.setText(dictionaryModel.getPengertian());
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
            final DatabaseHelper db = new DatabaseHelper(context);
            db.openDatabase();

            buttonFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                    alertDialog.setTitle("Pemberitahuan");
                    alertDialog.setMessage("Hapus Kata Favorit ?");
                    alertDialog.setCancelable(false);
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ya",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                    int position = getAdapterPosition();
                    DictionaryModel dictionaryModel = data.get(position);
                    db.RemoveFav(dictionaryModel.getId());
                    dictionaryModel.setFavorite("FALSE");
                    //Toast.makeText(view.getContext(), "Add to Favorite", Toast.LENGTH_SHORT).show();
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
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Tidak",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
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
}
