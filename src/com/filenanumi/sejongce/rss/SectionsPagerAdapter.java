package com.filenanumi.sejongce.rss;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.filenanumi.sejongce.rss.*;
import android.view.View.*;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
	Context mContext;

	public SectionsPagerAdapter(Context context, FragmentManager fm) {
		super(fm);
		mContext = context;
	}

	@Override
	public Fragment getItem(int position) {

		switch(position) {
		case 0:
			return new view_one(mContext);
		case 1:
			return new view_two(mContext);
		case 2:
			return new view_three(mContext);
		case 3:
			return new view_four(mContext);
		}
		return null;
	}

	@Override
	public int getCount() {
		// Show 3 total pages.
		return 4;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		switch (position) {
		case 0:
			return mContext.getString(R.string.title_section_i).toUpperCase();
		case 1:
			return mContext.getString(R.string.title_section_ii).toUpperCase();
		case 2:
			return mContext.getString(R.string.title_section_iii).toUpperCase();
		case 3:
			return mContext.getString(R.string.title_section_iv).toUpperCase();
		}
		return null;
	}

}
	

