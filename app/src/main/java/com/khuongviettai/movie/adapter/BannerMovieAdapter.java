package com.khuongviettai.movie.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khuongviettai.movie.R;
import com.khuongviettai.movie.model.Movie;
import com.khuongviettai.movie.utils.GlideUtils;

import java.util.List;

public class BannerMovieAdapter extends RecyclerView.Adapter<BannerMovieAdapter.BannerMovieViewHolder> {

    private final List<Movie> mListMovies;
    public final IClickItemListener iClickItemListener;

    public interface IClickItemListener {
        void onClickItem(Movie movie);
    }

    public BannerMovieAdapter(List<Movie> mListMovies, IClickItemListener iClickItemListener) {
        this.mListMovies = mListMovies;
        this.iClickItemListener = iClickItemListener;
    }

    @NonNull
    @Override
    public BannerMovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner_movie, parent, false);
        return new BannerMovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerMovieViewHolder holder, int position) {
        Movie movie = mListMovies.get(position);
        if (movie == null) {
            return;
        }
        GlideUtils.loadUrlBanner(movie.getBanner(), holder.imgBanner);
        holder.tvTitle.setText(movie.getTitle());

        holder.layoutItem.setOnClickListener(view -> iClickItemListener.onClickItem(movie));
    }

    @Override
    public int getItemCount() {
        if (mListMovies != null) {
            return mListMovies.size();
        }
        return 0;
    }

    public static class BannerMovieViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imgBanner;
        private final TextView tvTitle;
        private final RelativeLayout layoutItem;

        public BannerMovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBanner = itemView.findViewById(R.id.img_banner);
            tvTitle = itemView.findViewById(R.id.tv_title);
            layoutItem = itemView.findViewById(R.id.layout_item);
        }
    }
}
