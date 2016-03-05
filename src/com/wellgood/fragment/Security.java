package com.wellgood.fragment;

import info.hoang8f.android.segmented.SegmentedGroup;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.pc.util.Handler_Inject;
import com.wellgood.activity.R;
import com.wellgood.contract.AlarmInfo;

/**
 * 安防板块，包括 人 屋 利用 @
 * 
 * @author ZhaoJizhuang
 * @date 20150918
 */
public class Security extends BaseFragment implements RadioGroup.OnCheckedChangeListener{
	public static String CLASS_NAME="Security";
	public BroadcastReceiver receiver;
	//public static final String select = null;
	public static List<BaseFragment> fragments ;
	private Fragment people=new People();
	public static FragmentAdapter adapter=null ;
	Fragment room=new Room();
	  FragmentManager fm;
	  FragmentTransaction ft;
	  private ViewPager mViewPager;
	  SegmentedGroup segmented;
	//
	View view;// 缓存Fragment view

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_security, null);
		//注解
		Handler_Inject.injectOrther(this, view);
		//ViewUtils.inject(this, view);
		Log.d(CLASS_NAME, "onCreateView");
        initFragments();
		initViews();
		 
		    segmented = (SegmentedGroup) view.findViewById(R.id.segmented3);
	        
            segmented.setOnCheckedChangeListener(this);
            //默认选中
            segmented.check(R.id.button31);
            mViewPager.setAdapter(adapter);
            mViewPager.setOnPageChangeListener(new PageChangeListener());
            mViewPager.setOffscreenPageLimit(2);

		return view;
	}

	public void initFragments() {
		Log.d(CLASS_NAME, "initfragments");
		
		fragments=new ArrayList<BaseFragment>();
		BaseFragment people = new People();
		fragments.add(people);

		BaseFragment room = new Room();
		fragments.add(room);

	}
	public void initViews() {
		Log.d(CLASS_NAME, "initviews");
		//if (adapter==null) {
		mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
		//mIndicator = (IconTabPageIndicator) view.findViewById(R.id.indicator);
		adapter= new FragmentAdapter(fragments,
				this.getChildFragmentManager());
		mViewPager.setAdapter(adapter);
		mViewPager.setOnPageChangeListener(new PageChangeListener());
		//mIndicator.setViewPager(mViewPager);
			Log.d(CLASS_NAME, "initviewsend");
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub

		  switch (checkedId) {
		
          case R.id.button31:
             
              mViewPager.setCurrentItem(0);
              return;
          case R.id.button32:
            
        	  mViewPager.setCurrentItem(1);
              return;
  
          default:
              // Nothing to do
      }
		
	}

	/**
	 * receiver 接收service发过来的的消息
	 * 
	 * @author Windows 7
	 * @date 2015-10-30
	 */
	private class MyBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			AlarmInfo alarmInfo = (AlarmInfo) bundle
					.getSerializable("alarminfo");
			// Toast.makeText(MainActivity.this, intent.getStringExtra("back"),
			// Toast.LENGTH_SHORT).show();
			Toast.makeText(view.getContext(),
					",alarminfo:" + alarmInfo + "bundle" + bundle,
					Toast.LENGTH_SHORT).show();
		}
	}

	private class PageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int position) {
		switch (position) {
		case 0:
			segmented.check(R.id.button31);
		break;
		case 1:
			segmented.check(R.id.button32);
		break;

		}
		}
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		}

	class FragmentAdapter extends FragmentPagerAdapter  {
		private List<BaseFragment> mFragments;

		public FragmentAdapter(List<BaseFragment> fragments, FragmentManager fm) {
			super(fm);
			mFragments = fragments;
		}

		@Override
		public Fragment getItem(int i) {
			return mFragments.get(i);
		}


		@Override
		public int getCount() {
			return mFragments.size();
		}

	}

}
