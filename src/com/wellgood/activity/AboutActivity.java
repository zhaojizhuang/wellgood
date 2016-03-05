package com.wellgood.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.wellgood.application.APP;
import com.wellgood.fragment.AboutFragment;
/**
 * 
 * @author zhaojizhuang
 * @disccription 更多 界面
 *	@date  2015-10-29
 */
public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		APP.getIns().addActivity(this);
		//导航栏显示返回
		ActionBar actionBar = getActionBar();

		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		
		if(savedInstanceState==null){
			AboutFragment fragment = new AboutFragment();
            getFragmentManager()
            . beginTransaction()
            .add(R.id.about, fragment).commit();
            
           
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
