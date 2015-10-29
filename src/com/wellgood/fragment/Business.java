package com.wellgood.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;
import com.wellgood.activity.R;
/**
 * 商盟板块
 * @author Windows 7
 *
 */
public class Business extends BaseFragment{
	View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {	
		
		 //ViewUtils.inject(this, view); //注入view和事件
		
		 if(view==null){
	            view=inflater.inflate(R.layout.fragment_business, null);
	        }
		 //缓存的View需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
	        ViewGroup parent = (ViewGroup) view.getParent();
	        if (parent != null) {
	            parent.removeView(view);
	        } 
		
	      //注入view和事件
  ViewUtils.inject(this, view); 
  return view;
	}	
}