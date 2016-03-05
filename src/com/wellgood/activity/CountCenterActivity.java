package com.wellgood.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.wellgood.application.APP;
import com.wellgood.fragment.CountCenterFragment;

public class CountCenterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_countcenter);
		APP.getIns().addActivity(this);
		//导航栏显示返回
		ActionBar actionBar = getActionBar();
		setTitle("账户中心");
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		
		if(savedInstanceState==null){
			CountCenterFragment fragment = new CountCenterFragment();
            getFragmentManager()
            . beginTransaction()
            .add(R.id.countcontainer, fragment).commit();
            
           
		}
		
		
	}

	@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case android.R.id.home:
				finish();
			default:
				return super.onOptionsItemSelected(item);
	
			}
	}
}
