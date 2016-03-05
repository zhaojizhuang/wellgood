package com.wellgood.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.hikvision.vmsnetsdk.LineInfo;
import com.hikvision.vmsnetsdk.ServInfo;
import com.wellgood.application.APP;
import com.wellgood.camera.CameraFragment;
import com.wellgood.camera.CameraFragment11;
import com.wellgood.camera.CameraUtils;
import com.wellgood.fragment.BusinessAlliance;
import com.wellgood.fragment.Message;
import com.wellgood.fragment.Public;
import com.wellgood.fragment.Security;
import com.wellgood.fragment.Settings;

/**
 * @author zhaojizhuang 
 * @discription 功能描述：主程序入口 maintab 通过加载5个fragment 利用fragmenttabhost进行切换
 */
public class MainActivity extends FragmentActivity {
	
	public static String CLASS_NAME;
	/** 百度定位相关的对象 **/

	// 定位精度 高精度
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	// 定位坐标系
	private String tempcoor = "gcj02";
	// 定位client
	private LocationClient mLocationClient;

	// 定义FragmentTabHost对象，并绑定
	//@InjectView(android.R.id.tabhost)
	private FragmentTabHost mTabHost;

	// 定义一个布局
	private LayoutInflater layoutInflater;
	// 定义接收断网的recevier
	private BroadcastReceiver netBroadcastReceiver = new MyReceiver();
	// 定义数组来存放Fragment界面
	private Class fragmentArray[] = {

			Public.class, 					// 公共板块
			BusinessAlliance.class, 		// 商盟板块
			Security.class, 				// 安防板块
			Message.class, 					// 消息板块
			Settings.class 					// 设置板块
	};

	// 定义数组来存放tab按钮图片
	private int mImageViewArray[] = {
			R.drawable.tab_public_btn,		//公共
			R.drawable.tab_business_btn,	//商盟
			R.drawable.tab_security_btn,	//安防
			R.drawable.tab_message_btn, 	//消息
			R.drawable.tab_settings_btn };	//设置

	// Tab选项卡的文字
	private String mTextviewArray[] = { "公共", "商盟", "安防", "消息", "我的" };

	// 构造函数得到类名
	public MainActivity() {
		CLASS_NAME = getClass().getName();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 取消状态栏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_tab_layout);
		APP.getIns().addActivity(this);
		// 初始化tabhost
		initView();
		// 初始化baidu地图定位客户端
		mLocationClient = ((APP) getApplication()).mLocationClient;
		initLocation();

		mLocationClient.start();// 定位SDK
								// start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
		mLocationClient.requestLocation();
//登录视频服务器
		LoginCamera();

	}

	/**
	 * 返回键不退出
	 */

	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {

				// forceStopAPK("com.wellgood.activity");
				exitAll();

			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 退出整个应用,除了service
	 */
	public void exitAllWithoutService() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	/**
	 * 退出整个应用
	 */
	public void exitAll() {
		//stopService(new Intent(MainActivity.this, BackGroundService.class));
		APP.getIns().exit();

	}

	private void initView() {
		// 实例化布局对象
		layoutInflater = LayoutInflater.from(this);
		mTabHost=(FragmentTabHost) findViewById(android.R.id.tabhost);
		// 实例化TabHost对象，得到TabHost
		// mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		// setup在加载了view之后调用
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

		// 得到fragment的个数
		int count = fragmentArray.length;

		for (int i = 0; i < count; i++) {
			// 为每一个Tab按钮设置图标、文字和内容
			TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i])
					.setIndicator(getTabItemView(i));
			// 将Tab按钮添加进Tab选项卡中 绑定fragment
			mTabHost.addTab(tabSpec, fragmentArray[i], null);
			// 设置Tab按钮的背景
			mTabHost.getTabWidget().getChildAt(i)
					.setBackgroundResource(R.drawable.selector_tab_background);

		}
		// 设置默认的首先显示tab
		mTabHost.setCurrentTab(2);

	}

	/**
	 * 给Tab按钮设置图标和文字
	 */
	private View getTabItemView(int index) {
		View view = layoutInflater.inflate(R.layout.tab_item_view, null);

		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
		imageView.setImageResource(mImageViewArray[index]);

		TextView textView = (TextView) view.findViewById(R.id.textview);
		textView.setText(mTextviewArray[index]);
		return view;
	}

	/**
	 * 监听网络状态的receiver 的action
	 */
	public static final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

	/**
	 * 注册监听网络的receiver
	 */
	private void registerDateTransReceiver() {
		Log.d(CLASS_NAME, "register receiver " + CONNECTIVITY_CHANGE_ACTION);
		IntentFilter filter = new IntentFilter();
		filter.addAction(CONNECTIVITY_CHANGE_ACTION);
		// 跟apppstart中的recevier区别开
		filter.setPriority(1002);
		registerReceiver(netBroadcastReceiver, filter);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		//Intent intent = new Intent(MainActivity.this, BackGroundService.class);
		//stopService(intent);
		super.onDestroy();
		// unregisterReceiver(netBroadcastReceiver);
	}

	/**
	 * 监听网络状态的receiver，网络状态一发生变化就会接收
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			Log.d(CLASS_NAME, "PfDataTransReceiver receive action " + action);
			if (TextUtils.equals(action, CONNECTIVITY_CHANGE_ACTION)) {// 网络变化的时候会发送通知
				Log.d(CLASS_NAME, "网络变化了");
				ConnectivityManager mConnMgr = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo aActiveInfo = mConnMgr.getActiveNetworkInfo(); // 获取活动网络连接信息
				Log.d(CLASS_NAME, "aActiveInfo" + aActiveInfo);
				// 断网了
				if (aActiveInfo == null) {
					Log.e(CLASS_NAME, "断网了");
					dialog();
					// dialog(context);
				}
				// 连上网了
				else {
					Log.e(CLASS_NAME, "联网了");

				}
				return;
			}
		}

	}

	// 成对出现
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		registerDateTransReceiver();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregisterReceiver(netBroadcastReceiver);
	}

	/** 确认dialog **/
	private void dialog() {
		AlertDialog.Builder builder = new Builder(MainActivity.this);
		builder.setMessage("处于断网状态，请检查网络设置");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 跳转到网络设置
				Intent wifiSettingsIntent = new Intent(
						"android.settings.WIFI_SETTINGS");
				startActivity(wifiSettingsIntent);

			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

			}
		});

		builder.create().show();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub

		mLocationClient.stop();
		super.onStop();
	}
	/*
	 * 初始化location 的设置
	 */
	private void initLocation() {
		Log.d(CLASS_NAME, "initLocation()");
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType(tempcoor);// 可选，默认gcj02，设置返回的定位结果坐标系，
		int span = 1000;
		option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIgnoreKillProcess(true);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

		mLocationClient.setLocOption(option);
		Log.d(CLASS_NAME, "initLocation");
	}
	public void LoginCamera(){
		
		  new Thread() {
      public void run() {
      	
      	try {
					
      		List<LineInfo> linelist=new ArrayList<LineInfo>();
      		linelist=CameraUtils.fetchLine(CameraFragment11.servAddr);
      		ServInfo servInfo=CameraUtils.Login(CameraFragment.servAddr, linelist.get(1).lineID, "dbwl", "12345");


		        
      	} catch (Exception e) {
					// TODO: handle exception
      		e.printStackTrace();
				}
      	
		        
		        
	         };
}.start();
}
}
