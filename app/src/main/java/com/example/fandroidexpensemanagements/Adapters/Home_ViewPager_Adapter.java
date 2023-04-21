package com.example.fandroidexpensemanagements.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class Home_ViewPager_Adapter extends FragmentStateAdapter {
    private ArrayList<Fragment> fragments;

    public Home_ViewPager_Adapter(@NonNull FragmentActivity activity, ArrayList<Fragment> fragments) {
        super(activity);
        this.fragments = fragments;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
