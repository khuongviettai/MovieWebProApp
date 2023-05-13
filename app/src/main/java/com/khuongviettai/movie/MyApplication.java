package com.khuongviettai.movie;

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.khuongviettai.movie.prefs.DataStoreManager;

public class MyApplication extends Application {

    private FirebaseDatabase mFirebaseDatabase;

    public static MyApplication get(Context context) {
        return (MyApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        DataStoreManager.init(getApplicationContext());
    }

    public DatabaseReference getMovieDatabaseReference() {
        return mFirebaseDatabase.getReference("movie");
    }

    public DatabaseReference getMovieDetailDatabaseReference(int id) {
        return mFirebaseDatabase.getReference("movie/" + id);
    }
}
