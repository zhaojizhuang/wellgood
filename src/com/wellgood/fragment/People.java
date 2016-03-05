package com.wellgood.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.pc.ioc.inject.InjectBinder;
import com.android.pc.ioc.inject.InjectView;
import com.android.pc.ioc.view.listener.OnClick;
import com.android.pc.ioc.view.listener.OnItemClick;
import com.android.pc.ioc.view.listener.OnItemLongClick;
import com.android.pc.util.Handler_Inject;
import com.wellgood.activity.AskHelpActivity;
import com.wellgood.activity.MainActivity;
import com.wellgood.activity.R;
import com.wellgood.adapter.PublicGridAdapter;
import com.wellgood.adapter.PublicGridItem;
import com.wellgood.fenleicamera.ShangmengActivity;
/**
 * 
 * 商盟板块
 * @author Administrator
 *
 */
public class People extends BaseFragment{
	public static String CLASS_NAME="People";


	//注解
	@InjectView(binders = { @InjectBinder(method = "onClick", listeners = { OnClick.class }) })
	View cashhelp,onekey_askhelp,keyhelp;
	  
	View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {	
		
		view = inflater.inflate(R.layout.fragment_people, container, false);
		Handler_Inject.injectOrther(this, view);
       
      
    	
    	Log.d(CLASS_NAME, "create");
    	
    	
     return view;
	}
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cashhelp:
			Toast.makeText(getActivity(), "警情期待！", Toast.LENGTH_LONG).show();

			break;
		case R.id.onekey_askhelp:
			Intent intent=new Intent(getActivity(),AskHelpActivity.class);
			startActivity(intent);
			Toast.makeText(getActivity(), "警情期待！", Toast.LENGTH_LONG).show();

			break;
		case R.id.keyhelp:
			
			Toast.makeText(getActivity(), "警情期待！", Toast.LENGTH_LONG).show();

			break;
			}
		}
	
	
	

}