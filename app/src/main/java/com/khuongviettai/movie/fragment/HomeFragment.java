package com.khuongviettai.movie.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.khuongviettai.movie.MyApplication;
import com.khuongviettai.movie.R;
import com.khuongviettai.movie.adapter.BannerMovieAdapter;
import com.khuongviettai.movie.adapter.MovieAdapter;
import com.khuongviettai.movie.model.Movie;
import com.khuongviettai.movie.utils.GlobalFuntion;
import com.khuongviettai.movie.utils.StringUtil;
import com.khuongviettai.movie.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class HomeFragment extends Fragment {

    private View mView;
    private KProgressHUD progressHUD;
    private EditText edtSearchName;
    private ImageView imgSearch;
    private ViewPager2 viewPager2;
    private CircleIndicator3 circleIndicator;
    private RecyclerView rcvData;
    private LinearLayout layoutContent;

    private List<Movie> listMovies;
    private List<Movie> listMovieBanner;

    private final Handler mHandlerBanner = new Handler();
    private final Runnable mRunnableBanner = new Runnable() {
        @Override
        public void run() {
            if (listMovieBanner == null || listMovieBanner.isEmpty()) {
                return;
            }
            if (viewPager2.getCurrentItem() == listMovieBanner.size() - 1) {
                viewPager2.setCurrentItem(0);
                return;
            }
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);

        initUi();
        initListener();
        getListMovies("");

        return mView;
    }

    private void initUi() {
        if (getActivity() != null) {
            progressHUD = KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait...")
                    .setCancellable(false)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f);
        }
        layoutContent = mView.findViewById(R.id.layout_content);
        edtSearchName = mView.findViewById(R.id.edt_search_name);
        imgSearch = mView.findViewById(R.id.img_search);

        rcvData = mView.findViewById(R.id.rcv_data);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        rcvData.setLayoutManager(gridLayoutManager);

        viewPager2 = mView.findViewById(R.id.view_pager_2);
        circleIndicator = mView.findViewById(R.id.indicator_3);
    }

    private void initListener() {
        imgSearch.setOnClickListener(view1 -> searchMovie());

        edtSearchName.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchMovie();
                return true;
            }
            return false;
        });

        edtSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String strKey = s.toString().trim();
                if (strKey.equals("") || strKey.length() == 0) {
                    if (listMovies != null) listMovies.clear();
                    getListMovies("");
                }
            }
        });
    }

    private void searchMovie() {
        String strKey = edtSearchName.getText().toString().trim();
        listMovies.clear();
        getListMovies(strKey);
        Utils.hideSoftKeyboard(getActivity());
    }

    private void getListMovies(String key) {
        if (getActivity() == null) {
            return;
        }
        progressHUD.show();
        MyApplication.get(getActivity()).getMovieDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressHUD.dismiss();
                layoutContent.setVisibility(View.VISIBLE);
                listMovies = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Movie movie = dataSnapshot.getValue(Movie.class);
                    if (movie == null) {
                        return;
                    }

                    if (StringUtil.isEmpty(key)) {
                        listMovies.add(0, movie);
                    } else {
                        if (Utils.getTextSearch(movie.getTitle()).toLowerCase().trim()
                                .contains(Utils.getTextSearch(key).toLowerCase().trim())) {
                            listMovies.add(0, movie);
                        }
                    }
                }
                displayListBannerMovies();
                displayListAllMovies();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), getString(R.string.msg_get_date_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayListBannerMovies() {
        BannerMovieAdapter bannerMovieAdapter = new BannerMovieAdapter(getListBannerMovies(),
                movie -> GlobalFuntion.onClickItemMovie(getActivity(), movie));
        viewPager2.setAdapter(bannerMovieAdapter);
        circleIndicator.setViewPager(viewPager2);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mHandlerBanner.removeCallbacks(mRunnableBanner);
                mHandlerBanner.postDelayed(mRunnableBanner, 3000);
            }
        });
    }

    private List<Movie> getListBannerMovies() {
        if (listMovieBanner != null) {
            listMovieBanner.clear();
        } else {
            listMovieBanner = new ArrayList<>();
        }
        if (listMovies == null || listMovies.isEmpty()) {
            return listMovieBanner;
        }
        for (Movie movie : listMovies) {
            if (movie.isFeatured()) {
                listMovieBanner.add(movie);
            }
        }
        return listMovieBanner;
    }

    private void displayListAllMovies() {
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
        rcvData.setAdapter(movieAdapter);
    }
}
