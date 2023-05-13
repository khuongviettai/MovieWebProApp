package com.khuongviettai.movie.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.khuongviettai.movie.MyApplication;
import com.khuongviettai.movie.activity.PlayMovieActivity;
import com.khuongviettai.movie.model.Movie;
import com.khuongviettai.movie.model.UserInfor;
import com.khuongviettai.movie.prefs.DataStoreManager;

import java.util.ArrayList;
import java.util.List;

public class GlobalFuntion {

    public static void startActivity(Context context, Class<?> clz) {
        Intent intent = new Intent(context, clz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(context, clz);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void onClickItemMovie(Context context, Movie movie) {
        if (context == null) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("movie_id", movie.getId());
        startActivity(context, PlayMovieActivity.class, bundle);
    }

    public static boolean isFavorite(Movie movie) {
        if (movie.getFavorite() == null || movie.getFavorite().isEmpty()) {
            return false;
        }
        List<UserInfor> listFavorite = new ArrayList<>(movie.getFavorite().values());
        if (listFavorite.isEmpty()) {
            return false;
        }
        for (UserInfor userInfor : listFavorite) {
            if (DataStoreManager.getUser().getEmail().equals(userInfor.getEmailUser())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isHistory(Movie movie) {
        if (movie.getHistory() == null || movie.getHistory().isEmpty()) {
            return false;
        }
        List<UserInfor> listHistory = new ArrayList<>(movie.getHistory().values());
        if (listHistory.isEmpty()) {
            return false;
        }
        for (UserInfor userInfor : listHistory) {
            if (DataStoreManager.getUser().getEmail().equals(userInfor.getEmailUser())) {
                return true;
            }
        }
        return false;
    }

    public static UserInfor getUserInfor(Movie movie) {
        UserInfor userInfor = null;
        if (movie.getFavorite() == null || movie.getFavorite().isEmpty()) {
            return null;
        }
        List<UserInfor> listFavorite = new ArrayList<>(movie.getFavorite().values());
        if (listFavorite.isEmpty()) {
            return null;
        }
        for (UserInfor userInforObject : listFavorite) {
            if (DataStoreManager.getUser().getEmail().equals(userInforObject.getEmailUser())) {
                userInfor = userInforObject;
                break;
            }
        }
        return userInfor;
    }

    public static void onClickFavoriteMovie(Context context, Movie movie, boolean isFavorite) {
        if (context == null) {
            return;
        }
        if (isFavorite) {
            String userEmail = DataStoreManager.getUser().getEmail();
            UserInfor userInfor = new UserInfor(System.currentTimeMillis(), userEmail);
            MyApplication.get(context).getMovieDatabaseReference()
                    .child(String.valueOf(movie.getId()))
                    .child("favorite")
                    .child(String.valueOf(userInfor.getId()))
                    .setValue(userInfor);
            return;
        }
        UserInfor userInforFavorite = getUserInfor(movie);
        if (userInforFavorite != null) {
            MyApplication.get(context).getMovieDatabaseReference()
                    .child(String.valueOf(movie.getId()))
                    .child("favorite")
                    .child(String.valueOf(userInforFavorite.getId()))
                    .removeValue();
        }
    }
}
