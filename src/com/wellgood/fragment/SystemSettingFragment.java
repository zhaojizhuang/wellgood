package com.wellgood.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pc.ioc.inject.InjectBinder;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectView;
import com.android.pc.ioc.view.listener.OnClick;
import com.android.pc.util.Handler_Inject;
import com.util.DataCleanManager;
import com.wellgood.activity.LoginActivity;
import com.wellgood.activity.R;
import com.wellgood.application.APP;
import com.wellgood.contract.MyData;

public class SystemSettingFragment extends Fragment {

	public static String CLASS_NAME="SystemSettingFragment";
	//注解
	@InjectView(binders = { @InjectBinder(method = "OnClick", listeners = { OnClick.class }) })
	View	 logout,				//注销
			quit;				//退出
	
	View rootView;
	TextView cacheSize;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//this.inflater = inflater;

		rootView = inflater.inflate(R.layout.fragment_systemsetting, container, false);
		Handler_Inject.injectOrther(this, rootView);
		cacheSize=(TextView) rootView.findViewById(R.id.cache_file_size);
		
		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
		try {
			String sizeString=DataCleanManager.getCacheSize(APP.getIns().getCacheDir());
			String sizeString1=DataCleanManager.getCacheSize(APP.getIns().getExternalCacheDir());
			String sizeString2=DataCleanManager.getCacheSize(APP.getIns().getExternalCacheDir());
			Log.d(CLASS_NAME, "sizeString "+sizeString);
			Log.d(CLASS_NAME, "sizeString1 "+sizeString1);
			Log.d(CLASS_NAME, "sizeString2 "+sizeString2);
			cacheSize.setText(sizeString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		super.onResume();
	}

	@InjectInit
	private void init(){
		
	}
	
	 public void OnClick(View view){
	    	switch (view.getId()) {
	    	/**
	    	 * 布放按钮
	    	 */
			case R.id.logout:
				Toast.makeText(getActivity(), "正在退出", Toast.LENGTH_LONG).show();
				Log.d(CLASS_NAME, "点击了logout");
				//BackGroundService.Refresh_enable=false;
//				try {
//					Thread.sleep(1000);
//					
//				} catch (InterruptedException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
				MyData.saveRememberFlag(false);
//				getActivity().stopService(new Intent(getActivity(),BackGroundService.class));
				getActivity().finish();
				Intent intent=new Intent(getActivity(),LoginActivity.class);
				startActivity(intent);
				break;
			case R.id.quit:
				Log.d(CLASS_NAME, "点击了quit");
				DataCleanManager.cleanInternalCache(getActivity());
				DataCleanManager.cleanFiles(getActivity());
				DataCleanManager.cleanExternalCache(getActivity());
				
				
				try {
					String sizeString=DataCleanManager.getCacheSize(getActivity().getCacheDir());
					cacheSize.setText(sizeString);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//exitAll();
				break;
				default :
					break;
	    	}
	    }
	 
		/**
		 * 退出整个应用
		 */
		public void exitAll(){
//			getActivity().stopService(new Intent(getActivity(),BackGroundService.class));
		 System.exit(0);
	         
	     	
		}
}
