package com.wellgood.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.wellgood.activity.R;
import com.wellgood.adapter.MyFragmentPagerAdapter;
import com.wellgood.fragment.Message.ViewPagerAdapter;
import com.wellgood.frame.indicator.selectView.MSelect;
import com.wellgood.frame.indicator.selectView.SecuritySelect;
import com.wellgood.frame.widgets.IndicatorTabBar;
/**
 * 安防板块，包括 人 屋
 * 利用textview 添加onclicklistener 实现tab效果
 * @
 * @author ZhaoJizhuang
 *@date 20150918
 */
public class Security extends BaseFragment{
	 
	public static final String select = null;
	private IndicatorTabBar mIndicatorTabBar;
	private List<String> tabNames;
	private ViewPager mViewPager;
	private int maxColumn = 2;				//最多显示几个
	
    //
    View view;//缓存Fragment view
    
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		/* if(view==null){*/
	            view=inflater.inflate(R.layout.fragment_security, null);
	  /*      }
		 //缓存的View需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
	        ViewGroup parent = (ViewGroup) view.getParent();
	        if (parent != null) {
	            parent.removeView(view);
	        }*/ 
	      //注入view和事件
        ViewUtils.inject(this, view); 

        getActivity().setTitle("安防");
        
		tabNames = new ArrayList<String>();
		tabNames.add("人");
		tabNames.add("屋");
		
		
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
			return SecuritySelect.select(position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return tabNames.size();
		}

	}

	
	
}
