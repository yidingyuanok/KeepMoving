package com.mycheering.vpf.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class FragmentVPAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragments;

	public FragmentVPAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragments.get(arg0);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	/**
	 * 下面的 instantiteItem和destroyItem两个方法 创建不需要修改
	 */

//	@Override
//	public Object instantiateItem(ViewGroup container, int position) {
//		return super.instantiateItem(container, position);
//	}
//
//	@Override
//	public void destroyItem(ViewGroup container, int position, Object object) {
//		super.destroyItem(container, position, object);
//	}

}
