package com.wellgood.fragment;

import com.android.pc.ioc.inject.InjectView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.wellgood.activity.R;
import com.wellgood.adapter.InterviewAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * {@link Security}子版块 -----room
 * @author Windows 7
 *
 */
public class Room extends BaseFragment{
	///@InjectView(binders = { @InjectBinder(method = "onItemClick", listeners = { OnItemClick.class }) })
	@ViewInject(R.id.interview_list)
	private ListView interview_list;
	//@InjectView
	@ViewInject(R.id.line)
	private ImageView line;
	@InjectView
	private FrameLayout contain;
	private InterviewAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_room, null);
		 //ViewUtils.inject(this, view); //注入view和事件
		return view;
	}

}
