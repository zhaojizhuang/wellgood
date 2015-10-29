package com.wellgood.activity;

import com.wellgood.fragment.CountCenterFragment;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

public class CountCenterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_countcenter);
		//µº∫Ω¿∏œ‘ æ∑µªÿ
		ActionBar actionBar = getActionBar();

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
