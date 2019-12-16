package com.filenanumi.sejongce.rss;

import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v4.view.*;
import android.view.*;
import android.view.WindowManager.*;
import android.widget.*;
import com.filenanumi.sejongce.rss.Service.*;
import android.view.View.*;
import com.roger.match.library.*;

public class MainActivity extends FragmentActivity
{

	private SectionsPagerAdapter mSectionsPagerAdapter;

	private ViewPager mViewPager;

	private LinearLayout lay_main,lay_pref;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		mSectionsPagerAdapter = new SectionsPagerAdapter(getApplicationContext(), getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setOffscreenPageLimit(3);
        mViewPager.setCurrentItem(0);
		
		lay_pref = (LinearLayout) findViewById(R.id.lay_pref);
		lay_main = (LinearLayout) findViewById(R.id.lay_main);
		
		final SharedPreferences pref = getSharedPreferences("usr_pref",MODE_PRIVATE);
		final SharedPreferences.Editor editor = pref.edit();
		
		if (pref.getInt("firstload",0) == 0){
			setWeight(lay_main,0);
			setWeight(lay_pref,1);
		}
		
		final TextView tv_noti_i = (TextView) findViewById(R.id.tv_noti_i);
		final TextView tv_noti_ii = (TextView) findViewById(R.id.tv_noti_ii);
		
		Switch switch_i = (Switch) findViewById(R.id.pref_switch_i);
		switch_i.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton cb, boolean isChecked) {
					if(isChecked){
						editor.putInt("service_i",1);
						editor.commit();
						startService(new Intent(MainActivity.this, NotiChecker.class));
					}
					else {
						editor.putInt("service_i",0);
						editor.commit();
						stopService(new Intent(MainActivity.this, NotiChecker.class));
					}
				}
			});
		Switch switch_ii = (Switch) findViewById(R.id.pref_switch_ii);
		switch_ii.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton cb, boolean isChecked) {
					if(isChecked){
						editor.putInt("service_ii",1);
						editor.commit();
						startService(new Intent(MainActivity.this, ServerChecker.class));
					}
					else {
						editor.putInt("service_ii",0);
						editor.commit();
						stopService(new Intent(MainActivity.this, ServerChecker.class));
					}
				}
			});
		Switch switch_iii = (Switch) findViewById(R.id.pref_switch_iii);
		switch_iii.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton cb, boolean isChecked) {
					if(isChecked){
						editor.putInt("service_iii",1);
						editor.commit();
						startService(new Intent(MainActivity.this, FreeChecker.class));
					}
					else {
						editor.putInt("service_iii",0);
						editor.commit();
						stopService(new Intent(MainActivity.this, FreeChecker.class));
					}
				}
			});
		Switch switch_iv = (Switch) findViewById(R.id.pref_switch_iv);
		switch_iv.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton cb, boolean isChecked) {
					if(isChecked){
						editor.putInt("service_iv",1);
						editor.commit();
						startService(new Intent(MainActivity.this, ItChecker.class));
					}
					else {
						editor.putInt("service_iv",0);
						editor.commit();
						stopService(new Intent(MainActivity.this, ItChecker.class));
					}
				}
			});
		Switch switch_noti_i = (Switch) findViewById(R.id.switch_noti_i);
		switch_noti_i.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton cb, boolean isChecked) {
					if(isChecked){
						editor.putInt("noti_wifi",1);
						editor.commit();
						tv_noti_i.setText("모두 사용");
					}
					else {
						editor.putInt("noti_wifi",0);
						editor.commit();
						tv_noti_i.setText("WI-FI만 사용");
					}
				}
			});
		Switch switch_noti_ii = (Switch) findViewById(R.id.switch_noti_ii);
		switch_noti_ii.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton cb, boolean isChecked) {
					if(isChecked){
						editor.putInt("noti_vibe",1);
						editor.commit();
						tv_noti_ii.setText("알림시 진동 울림");
					}
					else {
						editor.putInt("noti_vibe",0);
						editor.commit();
						tv_noti_ii.setText("알림시 진동 없음");
					}
				}
			});
			
		Button btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				setWeight(lay_main,1);
				setWeight(lay_pref,0);
			}
		});
			
			
		if (pref.getInt("service_i",0) == 1){
			switch_i.setChecked(true);
			startService(new Intent(this, NotiChecker.class));
		} else {
			switch_i.setChecked(false);
		}
		if (pref.getInt("service_ii",0) == 1){
			switch_ii.setChecked(true);
			startService(new Intent(this, ServerChecker.class));
		} else {
			switch_ii.setChecked(false);
		}
		if (pref.getInt("service_iii",0) == 1){
			switch_iii.setChecked(true);
			startService(new Intent(this, FreeChecker.class));
		} else {
			switch_iii.setChecked(false);
		}
		if (pref.getInt("service_iv",0) == 1){
			switch_iv.setChecked(true);
			startService(new Intent(this, ItChecker.class));
		} else {
			switch_iv.setChecked(false);
		}
		
		if (pref.getInt("noti_wifi",0) == 1){
			switch_noti_i.setChecked(true);
			tv_noti_i.setText("모두 사용");
		} else {
			switch_noti_i.setChecked(false);
			tv_noti_i.setText("WI-FI만 사용");
		}
		if (pref.getInt("noti_vibe",0) == 1){
			switch_noti_ii.setChecked(true);
			tv_noti_ii.setText("알림시 진동 울림");
		} else {
			switch_noti_ii.setChecked(false);
			tv_noti_ii.setText("알림시 진동 없음");
		}
		
		editor.putInt("firstload",1);
		editor.commit();
		
    }
	
	public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
	}
	
	@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
    }
	
	public void setWeight(LinearLayout tlay,int weight){
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
			LayoutParams.MATCH_PARENT, 0);
		params.weight = weight;
		tlay.setLayoutParams(params);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) 
	{ 

		switch (item.getItemId()) 
		{ 
			case R.id.menu_Pref:
				setWeight(lay_main,0);
				setWeight(lay_pref,1);
		}
		return true;
	}
	
}
