package com.wellgood.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.pc.ioc.event.EventBus;
import com.android.pc.ioc.inject.InjectBinder;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectView;
import com.android.pc.util.Handler_Inject;
import com.wellgood.activity.R;
import com.wellgood.adapter.InterviewAdapter;
import com.wellgood.entity.FragmentEntity;

/*
 * Author: pan Email:gdpancheng@gmail.com
 * Created Date:2013-8-4
 * Copyright @ 2013 BU
 * Description: Á±ªÊèèËø?
 *
 * History:
 */
public class InterviewFragment extends BaseFragment {

	private static  String CLASS_NAME ;
	//@InjectView(binders = { @InjectBinder(method = "onItemClick", listeners = { OnItemClick.class }) })
	@InjectView
	public static  ListView interview_list;
	@InjectView
	private ImageView line;
	@InjectView
	private FrameLayout contain;
	private InterviewAdapter adapter;

	public InterviewFragment(){
		CLASS_NAME=getClass().getName();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.inflater = inflater;
		View rootView = inflater.inflate(R.layout.activity_interview, container, false);
		Handler_Inject.injectOrther(this, rootView);
		Log.d(CLASS_NAME, "createview");
		
		return rootView;
	}

	@InjectInit
	private void init(){
		adapter = new InterviewAdapter(activity, null);
		interview_list.setAdapter(adapter);
		setProgress(contain);
		startProgress();
		 interview_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {  
		        @Override  
		        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {  
		        	Log.d(CLASS_NAME, "setonitemclick"+position);
		            
		        }  
		    });  
		Log.d(CLASS_NAME, "setonitemclick");
		}
		


/*	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Log.d(CLASS_NAME, "on item click");
		InterviewInfoFragment fragment = new InterviewInfoFragment();
		EventBus eventBus = EventBus.getDefault();
		FragmentEntity fragmentEntity = new FragmentEntity();
		//SlidingEntity slidingEntity = new SlidingEntity();

		fragmentEntity.setFragment(fragment);
		eventBus.post(fragmentEntity);
		//eventBus.post(slidingEntity);
		Toast.makeText(getActivity(), "µ„ª˜¡Àitem",
			     Toast.LENGTH_SHORT).show();
	}
*/
/*	@InjectMethod(@InjectListener(ids = { R.id.lastest_interview, R.id.about_interview }, listeners = { OnClick.class }))
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.about_interview:
			line.setVisibility(View.GONE);
			adapter.setAbout(true);
			adapter.notifyDataSetChanged();
			break;
		default:
			line.setVisibility(View.VISIBLE);
			adapter.setAbout(false);
			adapter.notifyDataSetChanged();
			break;
		}
	}*/
}