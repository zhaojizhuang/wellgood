package com.wellgood.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.wellgood.activity.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/*
 * Author: pan Email:gdpancheng@gmail.com
 * Created Date:2013-5-8
 * Copyright @ 2013 BU
 * Description: 类描�?
 *
 * History:
 */
public class InterviewAdapter extends CommonAdapter {

	private LayoutInflater inflater;
	private boolean isAbout;

	public InterviewAdapter(Activity context, ArrayList<HashMap<String, Object>> data) {
		inflater = LayoutInflater.from(context);
		this.activity = context;
		this.data = data;
	}

	@Override
	public View view(int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.activity_interview_item, null);
		if (isAbout) {
		/*	LinearLayout last_time = (LinearLayout) convertView.findViewById(R.id.last_time);
			last_time.setVisibility(View.GONE);*/
		}
		//ImageView interview_image = (ImageView) convertView.findViewById(R.id.interview_image);
		//interview_image.setVisibility(View.GONE);
		//if (position == 1) {
			//interview_image.setVisibility(View.VISIBLE);
		//}
		return convertView;
	}

	public boolean isAbout() {
		return isAbout;
	}

	public void setAbout(boolean isAbout) {
		this.isAbout = isAbout;
	}

}