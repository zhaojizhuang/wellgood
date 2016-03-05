package com.wellgood.application;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Service;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;

import com.android.pc.ioc.app.Ioc;
import com.android.pc.util.Handler_SharedPreferences;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.hik.mcrsdk.MCRSDK;
import com.hik.mcrsdk.rtsp.RtspClient;
import com.hikvision.vmsnetsdk.CameraInfo;
import com.tencent.android.tpush.XGNotifaction;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.XGPushNotifactionCallback;
import com.wellgood.adapter.GridItem;
import com.wellgood.contract.MyCameraInfo;
/**
 * 存放全局变量
 * {@link SharedPreferences} 保存
 * @author ZhaoJizhuang
 *
 */
public class APP extends Application {
	public static String CLASS_NAME="APP";
	
	//activity列表，用于退出整个程序
	private List<Activity> mList = new LinkedList<Activity>();
	public static APP app;
	//百度定位client
	public  LocationClient mLocationClient;
	//位置监听listener
	public MyLocationListener mMyLocationListener;
	//震动
    public Vibrator mVibrator;
    //mac 地址
	public static String MacAddress;
	//公共摄像头列表
	public static List<GridItem> zhsqListdata=new ArrayList<GridItem>();
	//公共摄像头列表
	public static List<GridItem> zhxyListdata=new ArrayList<GridItem>();
	//公共摄像头列表
	public static List<GridItem> zhjtListdata=new ArrayList<GridItem>();
	//公共摄像头列表
	public static List<GridItem> zhlyListdata=new ArrayList<GridItem>();
	
	
	//我的摄像头列表
	public static List<CameraInfo> MyCameraList=new ArrayList<CameraInfo>();
	//社区
	public static List<MyCameraInfo> shequList=new ArrayList<MyCameraInfo>();
		//交通
	public static List<CameraInfo> jiaotongList=new ArrayList<CameraInfo>();
		//旅游
	public static List<CameraInfo> xiaoyuanList=new ArrayList<CameraInfo>();
	//旅游
public static List<CameraInfo> lvyouList=new ArrayList<CameraInfo>();
	// 消息列表
	public static ArrayList<ContentValues> messagesList=new ArrayList<ContentValues>();
	// 求助信息列表
	public static ArrayList<ContentValues> helpmessagesList=new ArrayList<ContentValues>();
	// 网路状态，为空的话表示断网
	public static  NetworkInfo netInfo;
	// 联网和断网
	public static Boolean NetAvalible;
	//网络状态监听器
    BroadcastReceiver netBroadcastReceiver=new MyReceiver();
    // 主机状态
    public static Boolean host_status=null;
    //反控状态
    public static Boolean RC_status=null;
    // 地址详情
    public static String addr_info=null;
    //经度
    public static String Longitude=null;
    //纬度
    public static String Latitude=null;
    
    
	@Override
	public void onCreate() {
		
		  mLocationClient = new LocationClient(this.getApplicationContext());
	        mMyLocationListener = new MyLocationListener();
	        //注册位置监听器
	        mLocationClient.registerLocationListener(mMyLocationListener);
	        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
		
		
		 app = this;
		 MacAddress=getMac();
		 //初始化ioc框架
		Ioc.getIoc().init(this);
	    super.onCreate();
	    /** 初始化视频框架 **/
	    MCRSDK.init();
        RtspClient.initLib();
        MCRSDK.setPrint(1, null);
        registerDateTransReceiver();
        netInfo=getNetStatus();
        if (netInfo!=null) {
			NetAvalible=true;
		}

		// 在主进程设置信鸽相关的内容
		if (isMainProcess()) {
			// 为保证弹出通知前一定调用本方法，需要在application的onCreate注册
			// 收到通知时，会调用本回调函数。
			// 相当于这个回调会拦截在信鸽的弹出通知之前被截取
			// 一般上针对需要获取通知内容、标题，设置通知点击的跳转逻辑等等
			XGPushManager
					.setNotifactionCallback(new XGPushNotifactionCallback() {

						@Override
						public void handleNotify(XGNotifaction xGNotifaction) {
							Log.i("test", "处理信鸽通知：" + xGNotifaction);
							// 获取标签、内容、自定义内容
							String title = xGNotifaction.getTitle();
							String content = xGNotifaction.getContent();
							String customContent = xGNotifaction
									.getCustomContent();
							// 其它的处理
							// 如果还要弹出通知，可直接调用以下代码或自己创建Notifaction，否则，本通知将不会弹出在通知栏中。
							xGNotifaction.doNotify();
						}
					});
		}
      
	}
	
	public boolean isMainProcess() {
		ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
		List<RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
		String mainProcessName = getPackageName();
		int myPid = android.os.Process.myPid();
		for (RunningAppProcessInfo info : processInfos) {
			if (info.pid == myPid && mainProcessName.equals(info.processName)) {
				return true;
			}
		}
		return false;
	}
    /**
     * 获取登录设备mac地址
     * 
     * @return
     */
    public  String getMac() {
        WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        String mac = wm.getConnectionInfo().getMacAddress();
        return mac == null ? "" : mac;
    }
	/**
	 * 数据存储到本地数据库
	 * 利用了loonAndroid的工具框架  android.pc.utils里的
	 * @author Windows 7
	 * @param key
	 * @param value
	 * @return void
	 */
	public void setData(String key, String value) {
		Handler_SharedPreferences.WriteSharedPreferences(getApplicationContext(), "Cache", key, value);
	}

	/**
	 * 取出本地数据
	 * 利用了loonAndroid的工具框架  android.pc.utils里的
	 * @author Windows 7
	 * @param key
	 * @return
	 * @return String
	 */
	public String getData(String key) {
		return Handler_SharedPreferences.getValueByName(getApplicationContext(), "Cache", key, Handler_SharedPreferences.STRING).toString();
	}

	/*
	 * 得到app实例
	 */
	public static APP getIns() {
		return app;
	}
	/**
	 * 每个activityoncreate的时候调用
	 * @param activity
	 */
	public void addActivity(Activity activity) {
	      mList.add(activity);
	}
/**
 * 整个系统退出的时候调用
 */
	    public void exit() {
	        try {
	           for (Activity activity : mList) {
	              if (activity != null)
	                 activity.finish();
	                 }
	             } catch (Exception e) {
	                 e.printStackTrace();
	             } finally {
	                System.exit(0);
	             }
	}
	
	
	public  NetworkInfo getNetStatus(){
		 NetworkInfo info=getnetstatus();
		return info;
	}
	private   NetworkInfo getnetstatus(){
		    ConnectivityManager mConnMgr;
		    mConnMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		  NetworkInfo aActiveInfo = mConnMgr.getActiveNetworkInfo(); // 获取活动网络连接信息
		return aActiveInfo;
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
			//跟apppstart中的recevier区别开
			filter.setPriority(1000);
			registerReceiver(netBroadcastReceiver, filter);
	}
	/**
	 * 监听网络状态的receiver，网络状态一发生变化就会接收
	 * @author Administrator
	 *
	 */
	private class MyReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			 String action = intent.getAction();
			    Log.d(CLASS_NAME, "PfDataTransReceiver receive action " + action);
			    if(TextUtils.equals(action, CONNECTIVITY_CHANGE_ACTION)){//网络变化的时候会发送通知
			        Log.d(CLASS_NAME, "网络变化了");
			    
				    netInfo= getNetStatus();
				    if (netInfo!=null) {
						NetAvalible=true;
						Log.d(CLASS_NAME, "网络可用");
					}else {
						Log.d(CLASS_NAME, "已经断网");
						NetAvalible=false;
					}
				    Log.d(CLASS_NAME, "aActiveInfo"+netInfo);
			        return;
			    }
		}
		
	}
	
	
	 /**
     * 实现实时位置回调监听
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            
        Longitude=""+location.getLongitude();
            //纬度
        Latitude=""+location.getLatitude();
            
            
            if (location.getLocType() == BDLocation.TypeGpsLocation){// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
             
            
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            addr_info=location.getAddrStr();
            Log.i("APP", sb.toString());
            Log.d(CLASS_NAME, Longitude+";"+Latitude+":"+addr_info);
           // mLocationClient.setEnableGpsRealTimeTransfer(true);
        }


    }
	
	
}
