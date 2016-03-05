package com.wellgood.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectLayer;
import com.hikvision.vmsnetsdk.CameraInfo;
import com.hikvision.vmsnetsdk.ControlUnitInfo;
import com.hikvision.vmsnetsdk.RegionInfo;
import com.wellgood.adapter.GridItem;
import com.wellgood.application.APP;
import com.wellgood.camera.CameraUtils;
import com.wellgood.camera.Constants;
import com.wellgood.camera.MsgIds;
import com.wellgood.contract.Contract;
/**
 * 开始界面
 * @author ZhaoJizhuang
 *
 */
@InjectLayer(R.layout.appstart)
public class APPStart extends Activity {
	public static String CLASS_NAME="APPStart";
	private int[] images=new int[]{R.drawable.c1,R.drawable.c2,R.drawable.c3,R.drawable.c4,R.drawable.c5};
	ProgressBar pb;
	public static final int prom=0x33;
    //设置滚动条可见  
    public int pro;
    public TextView textView;

	

    public static final String SAVED_DATA_KEY = "SAVED_DATA";
    private static final int FETCH_DATA_TASK_DURATION = 500;

    /** 发送消息的对象 */
    private MsgHandler          handler       = new MsgHandler();
    /**  控制中心列表**/
    List<ControlUnitInfo> ctrlUnitList = new ArrayList<ControlUnitInfo>();
    /**摄像头列表**/
    List<CameraInfo> cameraList = new ArrayList<CameraInfo>();
    List<CameraInfo> cameraList1 = new ArrayList<CameraInfo>();
    List<CameraInfo> cameraList2 = new ArrayList<CameraInfo>();
    List<CameraInfo> cameraList3 = new ArrayList<CameraInfo>();
    List<CameraInfo> cameraList4 = new ArrayList<CameraInfo>();
    List<CameraInfo> cameraList5 = new ArrayList<CameraInfo>();
    List<CameraInfo> cameraList6 = new ArrayList<CameraInfo>();
    List<CameraInfo> cameraList7 = new ArrayList<CameraInfo>();
    /**区域列表**/
    List<RegionInfo> regionList=new ArrayList<RegionInfo>();
    /**二级区域列表**/
    List<RegionInfo> subregionList=new ArrayList<RegionInfo>();
    public static String  servAddr = "http://112.12.17.3";
    public static String userName="dbwl" ;
    public static String password="12345" ;
    /**个人摄像头的路径**/
    //private String path="0/0/0/1";
    
    //private BroadcastReceiver netReceiver=new MyReceiver();
   /* String userName="test" ;
    String password="12345" ;*/
	View view;

	@InjectInit
	private void init(){
		Log.d(CLASS_NAME, "init()");
/*		Intent intent = new Intent (APPStart.this,LoginActivity.class);
    	//pb.setProgress(100);
		//Intent intent = new Intent (APPStart.this,ListActivity.class);
		//Intent intent = new Intent (APPStart.this,WallView.class);		
    	APPStart.this.finish();
		startActivity(intent);*/		
		
	}
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appstart);
		  //ViewUtils.inject(this);
		APP.getIns().addActivity(this);
		pb = (ProgressBar)findViewById(R.id.progressBar);  
		
		
		
		//注册监听网络的receiver
		//registerDateTransReceiver();
		
		
		
		//textView=(TextView) findViewById(R.id.probartext);
		   setProgressBarIndeterminateVisibility(true);  
		   
			if (isServiceRunning(APPStart.this,"com.wellgood.service.BackGroundService")) {
				
				
				APPStart.this.finish();
			}else {
				loadThread();
			}
			
		
		

	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
			
	}


    /**
     * 监听网络状态的receiver 的action
     */
	public static final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    /**
     * 注册监听网络的receiver
     */
/*	private void registerDateTransReceiver() {
			Log.d(CLASS_NAME, "register receiver " + CONNECTIVITY_CHANGE_ACTION);
			IntentFilter filter = new IntentFilter();
			filter.addAction(CONNECTIVITY_CHANGE_ACTION);
			filter.setPriority(1000);
			registerReceiver(netReceiver, filter);
	}*/
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		//unregisterReceiver(netReceiver);
		handler=null;
		super.onDestroy();
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
			        //断网了
			        if (getActiveNetwork(APPStart.this)==null) {
			        	//断网操作
			        	//跳转到网络设置
			        	Toast.makeText(APPStart.this, "已经断网", Toast.LENGTH_LONG).show();
			        	dialog();
			        	
						//Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");   
						//   startActivity(wifiSettingsIntent);   
					}
			        //连上网了
			        	else {
			        		//联网操作
			        		//getCameraListThread();
			        		Toast.makeText(APPStart.this, "已经连上网", Toast.LENGTH_LONG).show();
			        		
					}
			        return;
			    }
		}
		
	}
    /**
     * 这个方法返回的aActiveInfo可以判断网络的有无，如果返回的是null，
     * 这时候是断网了，如果返回对象不为空，则是连上了网。
     * 在返回的NetworkInfo对象里，可以有对象的方法获取更多的当前网络信息，比如是wifi还是cmwap等，就不多说了。
     * @param context
     * @return
     */
	public static NetworkInfo getActiveNetwork(Context context){
	    if (context == null)
	        return null;
	    ConnectivityManager mConnMgr = (ConnectivityManager) context
	            .getSystemService(Context.CONNECTIVITY_SERVICE);
	    if (mConnMgr == null)
	        return null;
	    NetworkInfo aActiveInfo = mConnMgr.getActiveNetworkInfo(); // 获取活动网络连接信息
	    return aActiveInfo;
	}
	
	
	
	

	/** handler  所以我们只要
	 * 在run方法中------发送Message,
	 * 在Handler里handleMessage----接收Message，
	 * 通过不同的Message执行不同的任务。  **/
	
	
    @SuppressLint("HandlerLeak")				//屏蔽lint错误
    private final class MsgHandler extends Handler {

   

		@SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                	
             /*   case MsgIds.GET_CAMERA_FAIL:
                	Log.d(CLASS_NAME, "获取摄像头列表失败");
                	break;*/
             /*   case prom:
                	pb.setProgress((Integer)msg.obj);
                	//textView.setText((Integer)msg.obj+"%");
                	break;*/
                case Contract.LOAD_COMPLATED:
                	//fillListData();
                	Intent intent = new Intent (APPStart.this,LoginActivity.class);
                	//Intent intent = new Intent (APPStart.this,MainActivity.class);
                	//pb.setProgress(100);
        			//Intent intent = new Intent (APPStart.this,ListActivity.class);
        			//Intent intent = new Intent (APPStart.this,WallView.class);		
                	APPStart.this.finish();
        			startActivity(intent);			
        			
                	break;
                	
                default:
                break;
            }

        }
    }
	
       
    protected void loadThread() {
 			Log.d(CLASS_NAME, "getCameraList");
 			  new Thread() {
 	                public void run() {

 	                	try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							handler.sendEmptyMessage(Contract.LOAD_COMPLATED);
						}
 	                
 	                	handler.sendEmptyMessage(Contract.LOAD_COMPLATED);
 			         };
 	         }.start();
 		}
	
	
	
	
        /**判断某个service是否已经在运行
         * @author zhaojizhuang
         * @param mContext 
         * @param className 类名与manifest中一致
         * @return  <code>true</code> or <code>false</code>
         */
        public static boolean isServiceRunning(Context mContext,String className) {  
            boolean isRunning = false;  
            ActivityManager activityManager = (ActivityManager)  
            mContext.getSystemService(Context.ACTIVITY_SERVICE);   
            List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(200);  
            Log.d(CLASS_NAME, "serviceList"+serviceList);
            if (!(serviceList.size()>0)) {  
                return false;  
            }  
      
            for (int i=0; i<serviceList.size(); i++) {  
            	  Log.d(CLASS_NAME, "serviceList"+serviceList.get(i).service.getClassName());
                if (serviceList.get(i).service.getClassName().equals(className) == true) {  
                    isRunning = true;  
                    break;  
                }  
            }  
            return isRunning;  
    }  
	
    	@Override
		@Deprecated
		protected Dialog onCreateDialog(int id) {
			// TODO Auto-generated method stub
			return super.onCreateDialog(id);
			
		}




		/** 确认dialog select 0为布放，1位撤防**/
    	private void dialog() {
    		AlertDialog.Builder builder = new Builder(APPStart.this);
    		builder.setMessage("处于断网状态，请检查网络设置");
    		builder.setTitle("提示");
    		builder.setPositiveButton("确认", new OnClickListener() {
    						@Override
    						public void onClick(DialogInterface dialog, int which) {
    							   dialog.dismiss();
    						//跳转到网络设置
    						Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");   
    						   startActivity(wifiSettingsIntent);   
    						APPStart.this.finish();
    						}
    		});
    		builder.setNegativeButton("取消", new OnClickListener() {
    						 @Override
    				 public void onClick(DialogInterface dialog, int which) {
    						dialog.dismiss();
    						APPStart.this.finish();
    						 }
    		});
    		
    		builder.create().show();
    	}





	
	
	
	
	
}
