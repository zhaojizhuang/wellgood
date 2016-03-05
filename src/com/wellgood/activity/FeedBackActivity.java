package com.wellgood.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.wellgood.application.APP;
import com.wellgood.fragment.FeedbackFragment;

public class FeedBackActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		APP.getIns().addActivity(this);
		//µº∫Ω¿∏œ‘ æ∑µªÿ
		ActionBar actionBar = getActionBar();

		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		
		if(savedInstanceState==null){
			FeedbackFragment fragment = new FeedbackFragment();
            getFragmentManager()
            . beginTransaction()
            .add(R.id.feedback, fragment).commit();
            
           
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
