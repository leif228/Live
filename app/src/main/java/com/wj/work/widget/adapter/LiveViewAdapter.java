package com.wj.work.widget.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * LiveViewAdapter
 * 2020/4/22 10:38
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class LiveViewAdapter extends FragmentPagerAdapter {

    private Fragment[] mFragments;

    public LiveViewAdapter(@NonNull FragmentManager fm,Fragment[] fragments) {
        super(fm);
        this.mFragments=fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return mFragments.length;
    }
}
