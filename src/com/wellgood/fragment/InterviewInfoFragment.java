package com.wellgood.fragment;

import com.wellgood.activity.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*
 * Author: pan Email:gdpancheng@gmail.com
 * Created Date:2013-8-4
 * Copyright @ 2013 BU
 * Description: 类描�?
 *
 * History:
 */
public class InterviewInfoFragment extends BaseFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.inflater = inflater;
		return inflater.inflate(R.layout.activity_interview_info, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
}