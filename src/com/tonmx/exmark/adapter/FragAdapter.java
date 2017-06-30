package com.tonmx.exmark.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragAdapter extends FragmentPagerAdapter{
    private List<Fragment> mFragments;
	public FragAdapter(FragmentManager fm,List<Fragment> fragments) {
		super(fm);
		// TODO Auto-generated constructor stub
		mFragments = fragments;
	}

	@Override
	public Fragment getItem(int postion) {
		// TODO Auto-generated method stub
		return mFragments.get(postion);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mFragments.size();
	}

}
