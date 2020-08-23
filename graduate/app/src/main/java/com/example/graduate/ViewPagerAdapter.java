package com.example.graduate;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.activity.MainActivity;
import com.example.module.CategoryID;
import com.example.part.PartListFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    public static Fragment f;
    @Override
    public Fragment getItem(int position) {     //0번째 CPU 1번째 메인보드 3번째 RAM...
        MainActivity.tab = position;
        PartListFragment f = new PartListFragment();
        f.setCategory(CategoryID.values()[position]);
        return f;
    }

    @Override
    public int getCount() {
        return CategoryID.values().length;                           //카운트 늘려야 POWER, CASE, COOLER 추가됨
        //return 7;
    }
}