package com.wellgood.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;
import com.wellgood.activity.R;
import com.wellgood.frame.indicator.selectView.MSelect;
import com.wellgood.frame.widgets.IndicatorTabBar;

/**
 * 消息板块 资讯，安防警示，资费提醒
 * @author Windows 7
 *
 */
public class Message extends BaseFragment{
	
	public static final String select = null;
	private IndicatorTabBar mIndicatorTabBar;
	private List<String> tabNames;
	private ViewPager mViewPager;
	private int maxColumn = 3;				//最多显示几个
	
	View view;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {	
		 //ViewUtils.inject(this, view); //注入view和事件
		
		
	            view=inflater.inflate(R.layout.fragment_message, null);
	     
	      //注入view和事件
	        ViewUtils.inject(this, view); 
	        
	        getActivity().setTitle("消息");
	        
			tabNames = new ArrayList<String>();
			tabNames.add("资讯");
			tabNames.add("安防警示");
			tabNames.add("资费提醒");
			
			
			//绑定viewpaber
			mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
			//设置适配器
			mViewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));

			mIndicatorTabBar = (IndicatorTabBar) view.findViewById(R.id.tab_indicator);
			mIndicatorTabBar.setMaxColumn(maxColumn);
			
			mIndicatorTabBar.setViewPager(mViewPager);
			mIndicatorTabBar.initView(tabNames);
	      
	        
	        return view;	
	}	
	

	class ViewPagerAdapter extends FragmentPagerAdapter {

		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			return MSelect.select(position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return tabNames.size();
		}

	}

}