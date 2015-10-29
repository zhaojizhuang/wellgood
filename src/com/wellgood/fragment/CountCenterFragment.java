package com.wellgood.fragment;

import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.util.Handler_Inject;
import com.wellgood.activity.R;
import com.wellgood.adapter.InterviewAdapter;

import android.os.Bundle;
import android.R.anim;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class CountCenterFragment extends Fragment {

	
	View rootView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//this.inflater = inflater;

		rootView = inflater.inflate(R.layout.fragment_count_center, container, false);
		Handler_Inject.injectOrther(this, rootView);
		return rootView;
	}

	@InjectInit
	private void init(){
		
	}
}
