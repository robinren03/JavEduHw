package com.example.renyanyu;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {


    private List<Fragment>fragmentList;
    private String[] titles;


    public MyFragmentPagerAdapter(FragmentManager fragmentManager, List<Fragment> fragmentList, String[] titles){
        super(fragmentManager);
        this.fragmentList = fragmentList;
        this.titles = titles;

    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    public Fragment changeto(){return fragmentList.get(1);}

}
