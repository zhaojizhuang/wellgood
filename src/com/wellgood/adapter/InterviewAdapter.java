package com.wellgood.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.ContentValues;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hikvision.vmsnetsdk.CameraInfo;
import com.wellgood.activity.R;
import com.wellgood.adapter.MyCameraAdapter.ViewHolder;

/*
 * Author: pan Email:gdpancheng@gmail.com
 * Created Date:2013-5-8
 * Copyright @ 2013 BU
 * Description: 类描�?
 *
 * History:
 */
public class InterviewAdapter extends CommonAdapter {
	  static class ViewHolder {
	        TextView msg_src;
	        TextView msg_sum;
	        TextView modifyDate;
	    }

	private LayoutInflater inflater;
	//private int count=10;
	private ArrayList<ContentValues> data;
/*	public InterviewAdapter(Activity context, ArrayList<HashMap<String, Object>> data) {
		inflater = LayoutInflater.from(context);
		this.activity = context;
		this.data = data;
	}*/
	public InterviewAdapter(Activity context, ArrayList<ContentValues> data) {
		inflater = LayoutInflater.from(context);
		this.activity = context;
		this.data = data;
	}
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public View view(int position, View convertView, ViewGroup parent) {
		
		ViewHolder mViewHolder;
		if (convertView == null) {
			mViewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.activity_interview_item, null);
			mViewHolder.msg_src=(TextView) convertView.findViewById(R.id.msg_src);
			mViewHolder.msg_sum=(TextView) convertView.findViewById(R.id.msg_sum);
			mViewHolder.modifyDate=(TextView) convertView.findViewById(R.id.modifyDate);
			convertView.setTag(mViewHolder);
			
		} else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}

		mViewHolder.msg_src.setText(data.get(position).getAsString("msg_type"));
		mViewHolder.msg_sum.setText(data.get(position).getAsString("msg_sum"));
		mViewHolder.modifyDate.setText(data.get(position).getAsString("modifyDate"));
			//mViewHolder.mImageView.setImageResource(R.drawable.friends_sends_pictures_no);
		//mViewHolder.mImageView.setImageResource(((GridItem)getItem(position)).getImageID());
		return convertView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return super.getView(position, convertView, parent);
	}


}