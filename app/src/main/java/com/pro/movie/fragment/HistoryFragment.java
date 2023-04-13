package com.pro.movie.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pro.movie.MyApplication;
import com.pro.movie.R;
import com.pro.movie.adapter.MovieAdapter;
import com.pro.movie.model.Movie;
import com.pro.movie.utils.GlobalFuntion;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private View mView;
    private RecyclerView rcvHistory;

    private List<Movie> listMovies;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_history, container, false);

        initUi();
        getListHistories();

        return mView;
    }

    private void initUi() {
        rcvHistory = mView.findViewById(R.id.rcv_history);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        rcvHistory.setLayoutManager(gridLayoutManager);
    }

    private void getListHistories() {
        if (getActivity() == null) {
            return;
        }
        MyApplication.get(getActivity()).getMovieDatabaseReference()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (listMovies != null) {
                            listMovies.clear();
                        } else {
                            listMovies = new ArrayList<>();
                        }
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Movie movie = dataSnapshot.getValue(Movie.class);
                            if (movie != null && GlobalFuntion.isHistory(movie)) {
                                listMovies.add(0, movie);
                            }
                        }
                        displayListHistories();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), getString(R.string.msg_get_date_error), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayListHistories() {
        MovieAdapter movieAdapter = new MovieAdapter(listMovies, new MovieAdapter.IClickItemListener() {
            @Override
            public void onClickItem(Movie movie) {
                GlobalFuntion.onClickItemMovie(getActivity(), movie);
            }

            @Override
            public void onClickFavorite(Movie movie, boolean favorite) {
                GlobalFuntion.onClickFavoriteMovie(getActivity(), movie, favorite);
            }
        });
        rcvHistory.setAdapter(movieAdapter);
    }
}
