package com.wellgood.activity;

import com.android.pc.ioc.inject.InjectLayer;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.wellgood.fragment.WallView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
/**
 * 开始界面
 * @author ZhaoJizhuang
 *
 */
@InjectLayer(R.layout.appstart)
public class APPStart extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	
		  //ViewUtils.inject(this);
		  
	new Handler().postDelayed(new Runnable(){
		@Override
		public void run(){
		//Intent intent = new Intent (APPStart.this,MainActivity.class);
			Intent intent = new Intent (APPStart.this,LoginActivity.class);
			
			//Intent intent = new Intent (APPStart.this,ListActivity.class);
			//Intent intent = new Intent (APPStart.this,WallView.class);			
			startActivity(intent);			
			APPStart.this.finish();
		}
	}, 1000);

	}
}
