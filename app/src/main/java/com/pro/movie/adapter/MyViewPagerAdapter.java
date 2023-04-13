package com.pro.movie.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.pro.movie.fragment.AccountFragment;
import com.pro.movie.fragment.FavoriteFragment;
import com.pro.movie.fragment.HistoryFragment;
import com.pro.movie.fragment.HomeFragment;

public class MyViewPagerAdapter extends FragmentStateAdapter {

    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new FavoriteFragment();

            case 2:
                return new HistoryFragment();

            case 3:
                return new AccountFragment();

            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
