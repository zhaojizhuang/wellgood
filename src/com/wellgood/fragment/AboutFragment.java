package com.wellgood.fragment;

import com.android.pc.ioc.inject.InjectBinder;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectView;
import com.android.pc.ioc.view.listener.OnClick;
import com.android.pc.util.Handler_Inject;
import com.wellgood.activity.R;
import com.wellgood.adapter.InterviewAdapter;
import com.wellgood.update.UpdateManager;

import android.os.Bundle;
import android.R.anim;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class AboutFragment extends Fragment {
	//注解
		@InjectView(binders = { @InjectBinder(method = "click", listeners = { OnClick.class }) })
		View checkupdate;				//我的行业 
	
	View rootView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//this.inflater = inflater;

		rootView = inflater.inflate(R.layout.fragment_about, container, false);
		Handler_Inject.injectOrther(this, rootView);
		return rootView;
	}

	@InjectInit
	private void init(){
		
	}
	
	public void click(View v) {
		Intent intent ;
		switch (v.getId()) {
		case R.id.checkupdate:
			UpdateManager manager = new UpdateManager(getActivity());
			
			manager.checkUpdate();
			break;
		default:
				break;
		}
		}
	
	
}
