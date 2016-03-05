package com.wellgood.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


/**
 * @author Administrator
 *
 */
public abstract class CommonAdapter extends BaseAdapter {

	public ArrayList<HashMap<String, Object>> data;
	public Context activity;

	/**
	 * 记得注销
	 */
/*	private int count = 10;
	public void setCount(int count) {
		this.count = count;
	}*/

	public CommonAdapter() {
    }
	
	public CommonAdapter(Activity activity, ArrayList<HashMap<String, Object>> data) {
	    this.activity = activity;
	    this.data = data;
    }
	
/*	@Override
	public int getCount() {
		return count;
	}*/

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return view(position, convertView, parent);
	}

	public abstract View view(int position, View convertView, ViewGroup parent);
}
