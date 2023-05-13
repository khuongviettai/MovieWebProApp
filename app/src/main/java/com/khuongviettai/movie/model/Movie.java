package com.khuongviettai.movie.model;

import java.io.Serializable;
import java.util.HashMap;

public class Movie implements Serializable {

    private int id;
    private String title;
    private String image;
    private String url;
    private boolean featured;
    private String banner;

    private HashMap<String, UserInfor> favorite;
    private HashMap<String, UserInfor> history;

    public Movie() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HashMap<String, UserInfor> getFavorite() {
        return favorite;
    }

    public void setFavorite(HashMap<String, UserInfor> favorite) {
        this.favorite = favorite;
    }

    public HashMap<String, UserInfor> getHistory() {
        return history;
    }

    public void setHistory(HashMap<String, UserInfor> history) {
        this.history = history;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }
}
