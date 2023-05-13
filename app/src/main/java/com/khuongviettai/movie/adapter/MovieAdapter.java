package com.khuongviettai.movie.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khuongviettai.movie.R;
import com.khuongviettai.movie.model.Movie;
import com.khuongviettai.movie.utils.GlideUtils;
import com.khuongviettai.movie.utils.GlobalFuntion;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final List<Movie> listMovies;
    private final IClickItemListener iClickItemListener;

    public interface IClickItemListener {
        void onClickItem(Movie movie);
        void onClickFavorite(Movie movie, boolean favorite);
    }

    public MovieAdapter(List<Movie> listMovies, IClickItemListener listener) {
        this.listMovies = listMovies;
        this.iClickItemListener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = listMovies.get(position);
        if (movie == null) {
            return;
        }
        holder.tvTitleMovie.setText(movie.getTitle());
        if (movie.getImage() != null && !movie.getImage().equals("")) {
            GlideUtils.loadUrl(movie.getImage(), holder.imgMovie);
        } else {
            holder.imgMovie.setImageResource(R.drawable.ic_no_image);
        }

        boolean isFavorite = GlobalFuntion.isFavorite(movie);
        if (isFavorite) {
            holder.imgFavorite.setImageResource(R.drawable.icon_favorite_big_on);
        } else {
            holder.imgFavorite.setImageResource(R.drawable.icon_favorite_big_off);
        }

        holder.layoutItem.setOnClickListener(view -> iClickItemListener.onClickItem(movie));

        holder.imgFavorite.setOnClickListener(view
                -> iClickItemListener.onClickFavorite(movie, !isFavorite));
    }

    @Override
    public int getItemCount() {
        if (listMovies != null) {
            return listMovies.size();
        }
        return 0;
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imgMovie;
        private final ImageView imgFavorite;
        private final TextView tvTitleMovie;
        private final LinearLayout layoutItem;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMovie = itemView.findViewById(R.id.img_movie);
            imgFavorite = itemView.findViewById(R.id.img_favorite);
            tvTitleMovie = itemView.findViewById(R.id.tv_title_movie);
            layoutItem = itemView.findViewById(R.id.layout_item);
        }
    }
}
