package com.wellgood.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.android.pc.ioc.event.EventBus;
import com.android.pc.util.Handler_Inject;
import com.wellgood.activity.R;

/*
 * Author: pan Email:gdpancheng@gmail.com
 * Created Date:2013-8-10
 * Copyright @ 2013 BU
 * Description: 绫绘弿杩?
 *
 * History:
 */
public class BaseFragment extends Fragment {

	protected LayoutInflater inflater;
	protected Activity activity;
	protected View progress;
	protected LinearLayout progress_container;

/*	public View.OnTouchListener on = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			EventBus eventBus = EventBus.getDefault();
			SlidingEntity slidingEntity = new SlidingEntity();
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				slidingEntity.setSlidingEnable(false);
				eventBus.post(slidingEntity);
				break;
			case MotionEvent.ACTION_UP:
				slidingEntity.setSlidingEnable(true);
				slidingEntity.setViewPage(true);
				eventBus.post(slidingEntity);
				break;
			}
			return false;
		}
	};*/

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}
	//页面加载的过程
	protected void setProgress(View view) {
		if (progress != null) {
			return;
		}
		LayoutParams lp = view.getLayoutParams();
		ViewParent parent = view.getParent();
		FrameLayout container = new FrameLayout(activity);
		ViewGroup group = (ViewGroup) parent;
		int index = group.indexOfChild(view);
		group.removeView(view);
		group.addView(container, index, lp);
		container.addView(view);
		if (inflater != null) {
			progress = inflater.inflate(R.layout.fragment_progress, null);
			progress_container = (LinearLayout) progress.findViewById(R.id.progress_container);
			container.addView(progress);
			progress_container.setTag(view);
			view.setVisibility(View.GONE);
		}
		group.invalidate();
	}
	//开始progress
	protected void startProgress() {
		if (progress_container != null) {
			progress_container.setVisibility(View.VISIBLE);
		}
		hideProgress();
	}

	protected void endProgress() {
		if (progress_container != null) {
			((View) progress_container.getTag()).setVisibility(View.VISIBLE);
			progress_container.setVisibility(View.GONE);
		}
	}

	Handler handler;

	protected void hideProgress() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				endProgress();
			}
		};
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				handler.sendEmptyMessage(0);
			}
		}).start();
	}
}
