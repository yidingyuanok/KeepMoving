package com.mycheering.vpf.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by zdyok on 2016/11/17.
 */

public  class NoDieVPAdapter extends FragmentPagerAdapter  {

    private ArrayList<TabInfo> tabs = new ArrayList<>();
    private ViewPager mViewPager;
    private Context mContext;

    public NoDieVPAdapter(FragmentManager fm, Context context, ViewPager viewPager) {
        super(fm);
        mContext = context;
        mViewPager = viewPager;

    }

    @Override
    public Fragment getItem(int position) {
        TabInfo tabInfo = tabs.get(position);
        if (tabInfo.fragment == null) {
            return Fragment.instantiate(mContext, tabInfo.clazz.getName());
        } else {
            return tabInfo.fragment;
        }
    }




    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object sObject = super.instantiateItem(container, position);
        TabInfo sTabInfo = tabs.get(position);
        sTabInfo.fragment = (Fragment) sObject;
        return sObject;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (object != null) {
            TabInfo sTabInfo = tabs.get(position);
            sTabInfo.fragment = null;
        }
    }

    public void addItem (Class<?> sClass, Bundle args ) {
        addItem(sClass,args,null);
    }

    public void addItem (Class<?> sClass, Bundle args ,String title) {
        TabInfo tabInfo = new TabInfo();
        tabInfo.clazz = sClass;
        tabInfo.args = args;
        tabInfo.title = title;
        tabs.add(tabInfo);
    }

    public void addItem (Class<?> sClass) {
        addItem(sClass,null);
    }

    @Override
    public int getCount() {
        return tabs == null ? 0 : tabs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position).title;

    }

    class TabInfo {
        public Bundle args;
        public Fragment fragment;
        public Class<?> clazz;
        public String title;


    }
}
