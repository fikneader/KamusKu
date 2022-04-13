package com.ngeartstudio.kamus.kamusku.model;

/**
 * Created by fikneader on 1/30/2018.
 */

public class DictionaryModel {
    private String id;
    private String kata;
    private String pengertian;
    private String favorite;
    private String gambar;

    public DictionaryModel(String id, String kata, String pengertian,String gambar, String favorite){
        this.id = id;
        this.kata = kata;
        this.pengertian = pengertian;
        this.gambar = gambar;
        this.favorite = favorite;
    }

    public String getId(){
        return id;
    }

    public String getKata(){
        return kata;
    }

    public String getPengertian(){
        return pengertian;
    }

    public String getFavorite(){
        return favorite;
    }

    public String getGambar(){
        return gambar;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setKata(String kata){
        this.kata = kata;
    }

    public void setPengertian(String pengertian){
        this.pengertian = pengertian;
    }

    public void setFavorite(String favorite){
        this.favorite = favorite;
    }

    public void setGambar(String gambar){
        this.gambar = gambar;
    }
}
