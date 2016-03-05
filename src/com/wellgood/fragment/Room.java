package com.wellgood.fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pc.ioc.inject.InjectBinder;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectView;
import com.android.pc.ioc.view.listener.OnClick;
import com.android.pc.util.Handler_Inject;
import com.tencent.android.tpush.XGPushTextMessage;
import com.util.UtilAudioPlay;
import com.wellgood.activity.MyCameraActivity;
import com.wellgood.activity.R;
import com.wellgood.application.APP;
import com.wellgood.camera.TempData;
import com.wellgood.contract.AlarmInfo;
import com.wellgood.contract.Contract;
import com.wellgood.contract.MyData;
import com.wellgood.service.SimpleClient;

/**
 * {@link Security55}子版块 -----room
 * 
 * @author Windows 7
 * 
 */
public class Room extends BaseFragment {

	public static String CLASS_NAME = "Room";
	public static int command = 0;
	/**
	 * 绑定按钮
	 */
	@InjectView(binders = { @InjectBinder(method = "OnClick", listeners = { OnClick.class }) })
	Button bufang_button, // 布放按钮
			chefang_button, // 撤防按钮
			camera_button; // 摄像头按钮

	//@InjectView(binders = { @InjectBinder(method = "OnClick", listeners = { OnClick.class }) })
	TextView device_status_info, // 报警主机当前状态
			alarm_process_status,//撤布防指令状态
			device_onlinestatus; // 报警主机的在线离线状态
	@InjectView(binders = { @InjectBinder(method = "OnClick", listeners = { OnClick.class }) })
	RelativeLayout device_status_refresh; // 刷新状态

	/** 摄像头列表 **/
	/*
	 * @InjectView(binders = { @InjectBinder(method = "onItemClick", listeners =
	 * { OnItemClick.class }) }) ListView camera_list;
	 * 
	 * @InjectView private ImageView line;
	 */
	/*
	 * @InjectView private FrameLayout contain; private CameraAdapter adapter;
	 */
	View view;
	/** 广播接收器 **/
	BroadcastReceiver receiver;
	/** 实例化handler对象 **/
	public MyHandler handler = new MyHandler();
	public Context context;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_room, null);
		Handler_Inject.injectOrther(this, view);
		Log.d(CLASS_NAME, "onCreateView");
		context = view.getContext();
		device_status_info=(TextView) view.findViewById(R.id.device_status_info); // 报警主机当前状态
		alarm_process_status=(TextView) view.findViewById(R.id.alarmprocess);//撤布防指令状态
		device_onlinestatus=(TextView) view.findViewById(R.id.device_onlinestatus); // 报警主机的在线离线状态
		// 注册广播接收器，1、动态、2、静态
		// 方法1、动态注册广播接收器
		receiver = new MyBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Contract.ALARM_ACTION); // 这里的Action要和Service发送广播时使用的Action一样
		view.getContext().registerReceiver(this.receiver, filter); // 动态注册BroadcastReceiver

		if (APP.host_status == null) {
			device_onlinestatus.setText("未知");
			
			command = 0;
			enableButton(command);
			getHostDataThread();
		}else if(APP.RC_status == null) {
			device_status_info.setText("状态未知");
		}
		else {
			if (APP.host_status!=null) {
				if (APP.host_status) {
					device_onlinestatus.setText("在线");
				} else {
					device_onlinestatus.setText("离线");
				}
			}
			if (APP.RC_status!=null) {
				if (APP.RC_status) {
					command = 2;
					enableButton(command);
					device_status_info.setText("已布防");
				} else {
					command = 1;
					enableButton(command);
					device_status_info.setText("已撤防");
				}
			}
			

		}

		return view;
	}



	/**
	 * 按钮监听方法
	 * 
	 * @param view
	 */
	public void OnClick(View view) {
		switch (view.getId()) {
		/**
		 * 布放按钮
		 */
		case R.id.bufang_button:

			Log.d(CLASS_NAME, "点击了bufang_button");
			dialog("确定布防？", 0);
			break;
		case R.id.chefang_button:

			Log.d(CLASS_NAME, "点击了chefang_button");

			dialog("确定撤防？", 1);

			break;
		case R.id.device_status_refresh:
			getHostDataThread();
			showStatus("正在刷新... ...");
//			Toast.makeText(view.getContext(), "正在刷新... ...", Toast.LENGTH_LONG)
//					.show();
			Log.d(CLASS_NAME, "点击了device_status_refresh");
			break;
		case R.id.camera_button:
			Log.d(CLASS_NAME, "点击了camera_button");
			Intent intent = new Intent(getActivity(), MyCameraActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	/**
	 * handler 所以我们只要 在run方法中------发送Message,
	 * 在Handler里handleMessage----接收Message， 通过不同的Message执行不同的任务。
	 **/

	@SuppressLint("HandlerLeak")
	// 屏蔽lint错误
	private final class MyHandler extends Handler {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {

			/** 发送布放指令成功 **/
			case Contract.SEND_BUFANG_MESSAGE_SUCCESS:
				Log.d(CLASS_NAME, "发送布防指令成功");
				enableButton(2);
				showStatus("发送布防指令成功");
				//Toast.makeText(context, "发送布防指令成功", Toast.LENGTH_LONG).show();
				break;
			/** 发送布放指令失败 **/
			case Contract.SEND_BUFANG_MESSAGE_FAILED:
				Log.d(CLASS_NAME, "发送布防指令失败");
				enableButton(1);
				showStatus( "发送布防指令失败");
				//Toast.makeText(context, "发送布防指令失败", Toast.LENGTH_LONG).show();
				break;

			/** 发送撤放指令成功 **/
			case Contract.SEND_CHEFANG_MESSAGE_SUCCESS:
				 showStatus("发送撤防指令成功");
//				Toast.makeText(context, (String) "发送撤防指令成功", Toast.LENGTH_LONG)
//						.show();
				enableButton(1);
				Log.d(CLASS_NAME, "发送撤防指令成功");
				break;
			/** 发送撤放指令失败 **/
			case Contract.SEND_CHEFANG_MESSAGE_FAILED:
				enableButton(2);
				showStatus("发送撤防指令失败");
				//Toast.makeText(context, "发送撤防指令失败", Toast.LENGTH_LONG).show();
				Log.d(CLASS_NAME, "发送撤防指令失败");
				break;
			case Contract.GET_HOST_MESSAGE_SUCCESS:
				showStatus("报警主机状态更新成功！");
				//Toast.makeText(context, "发送撤防指令失败", Toast.LENGTH_LONG).show();
				Log.d(CLASS_NAME, "报警主机状态更新成功！");
				if (APP.host_status==null) {
					device_onlinestatus.setText("未知");
				}else if(APP.host_status){
					
			
					device_onlinestatus.setText("在线");
				} else {
					device_onlinestatus.setText("离线");
				}
				if (APP.RC_status==null) {
					device_status_info.setText("状态未知");
				}
				else if (APP.RC_status) {
					
					
					command = 2;
					enableButton(command);
					device_status_info.setText("已布防");
				} else {
					command = 1;
					enableButton(command);
					device_status_info.setText("已撤防");
				}
				break;
			case Contract.GET_HOST_MESSAGE_FAILED:
					showStatus("报警主机状态更新失败！");
					//Toast.makeText(context, "发送撤防指令失败", Toast.LENGTH_LONG).show();
					Log.d(CLASS_NAME, "报警主机状态更新失败！");
					break;
			default:
				break;
			}

		}

	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Log.d(CLASS_NAME, "on item click");
		Toast.makeText(getActivity(), "点击了item" + arg2, Toast.LENGTH_SHORT)
				.show();
	}

	/** 布放 **/
	public void buFang() {
		// 点击之后不能再点击布防
		command = 2;
		enableButton(command);

		new Thread() {
			public void run() {
				Log.d(CLASS_NAME, "布放thread-run。。");

				try {

					String host = MyData.getHostID();
					Log.d(CLASS_NAME, "MyData.getHostID()" + host);
					String buFangUrl = Contract.CONNECT_HOST
							+ "/host/addLine?hostIds=" + host;
					Log.d(CLASS_NAME, "buFangUrl:" + buFangUrl);
					JSONObject responseJsonObject = SimpleClient.doGet(
							buFangUrl, null);
					Log.d(CLASS_NAME, "bufangresponse：" + responseJsonObject);
					boolean status = responseJsonObject.getBoolean("success");
					if (status == true) {
						Message msg = new Message();
						msg.what = Contract.SEND_BUFANG_MESSAGE_SUCCESS;
						msg.obj = responseJsonObject.get("data");
						handler.sendMessage(msg);
					} else {
						Message msg = new Message();
						msg.what = Contract.SEND_BUFANG_MESSAGE_FAILED;
						msg.obj = "filed";
						handler.sendMessage(msg);
					}

				} catch (Exception e1) {
					e1.printStackTrace();
				}

			};
		}.start();

	}

	/** 撤防 **/
	public void cheFang() {
		// 撤防之后不能再点击撤防
		command = 1;
		enableButton(command);
		new Thread() {
			public void run() {
				Log.d(CLASS_NAME, "撤防thread-run。。");

				try {

					String host = MyData.getHostID();
					Log.d(CLASS_NAME, "MyData.getHostID()" + host);
					String cheFangUrl = Contract.CONNECT_HOST
							+ "/host/removeLine?hostIds=" + host;
					Log.d(CLASS_NAME, "cheFangUrl:" + cheFangUrl);
					JSONObject responseJsonObject = SimpleClient.doGet(
							cheFangUrl, null);
					Log.d(CLASS_NAME, "chefangresponse:" + responseJsonObject);
					boolean status = responseJsonObject.getBoolean("success");
					if (status == true) {
						Message msg = new Message();
						msg.what = Contract.SEND_CHEFANG_MESSAGE_SUCCESS;
						msg.obj = responseJsonObject.get("data");
						handler.sendMessage(msg);
					} else {
						Message msg = new Message();
						msg.what = Contract.SEND_CHEFANG_MESSAGE_FAILED;
						msg.obj = "filed";
						handler.sendMessage(msg);
					}

				} catch (Exception e1) {
					e1.printStackTrace();
				}

			};
		}.start();

	}
	
	public void getHostDataThread(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
					try {
						
						
						if (APP.NetAvalible) {
							Log.d(CLASS_NAME, "NetAvalible"+APP.NetAvalible);
							//工作在这里做 请求主机状态
							JSONObject jsonObject=getHostData();
							Log.i(CLASS_NAME, "jsonObject"+jsonObject);
							if (jsonObject!=null) {
			    				JSONObject data=jsonObject.getJSONObject("data");
			    				JSONArray datas=data.getJSONArray("datas");
			    			
								//Log.d(CLASS_NAME, "datas:"+datas);
							JSONObject hostdata=datas.getJSONObject(0);
								//Log.d(CLASS_NAME, "hostdata:"+hostdata);
								String h=hostdata.getString("host_id");
								Log.d(CLASS_NAME, "host_id"+h);
							
								String host_num=hostdata.getString("host_num");
								Log.d(CLASS_NAME, "host_num"+host_num);
								APP.host_status=hostdata.getBoolean("host_status");
								
									String rcstatus=hostdata.getString("rCStatus");
									Log.d(CLASS_NAME, "rcstatus:"+rcstatus);
									if ("TRUE".equals(rcstatus)||"true".equals(rcstatus)||"FALSE".equals(rcstatus)||"false".equals(rcstatus)) {
										APP.RC_status=hostdata.getBoolean("rCStatus");
									}else  {
										APP.RC_status=null;
									} 
								
								
								Message message=new Message();
								message.what=Contract.GET_HOST_MESSAGE_SUCCESS;
								message.obj="success";
								handler.sendMessage(message);
							}
							else {
								Message message=new Message();
								message.what=Contract.GET_HOST_MESSAGE_FAILED;
								message.obj="failed";
								handler.sendMessage(message);
							}
						}
						
						
					} catch (Exception e) {
						// TODO: handle exception
						Message message=new Message();
						message.what=Contract.GET_HOST_MESSAGE_FAILED;
						message.obj="failed";
						handler.sendMessage(message);
					}


					
				
					
					
			}
		}).start();
	}
    public JSONObject getHostData(){
       	JSONObject ss=null;
    	JSONObject page=new JSONObject();
	
    	Log.d(CLASS_NAME, "getHostID()");
    			
    			try {
    				page.put("pageNo", "1");
    				page.put("pageSize", "2");
    				JSONObject host=new JSONObject();
    				host.put("page", page);
    				String getHostURL=Contract.CONNECT_HOST+"/host/page?host="+host.toString();
    				Log.d(CLASS_NAME, "getHostURL"+getHostURL);
    				ss=SimpleClient.doGet(getHostURL,null);
    				//Log.d(CLASS_NAME, "getHostID"+ss);
    				
    			} catch (Exception e1) {
    				e1.printStackTrace();
    			}
    			
    			return ss;
    	
    }
	/** 确认dialog select 0为布放，1位撤防 **/
	protected void dialog(String string, final int select) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage(string);
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (select == 0) {
					buFang();
				} else if (select == 1) {
					cheFang();
				}

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

	/**
	 * receiver 接收service发过来的的消息
	 * 
	 * @author Windows 7
	 * @date 2015-10-30
	 */
	private class MyBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(CLASS_NAME, "command" + command);
			XGPushTextMessage message =TempData.getIns().getMessage();
			Log.d(CLASS_NAME, "传递过来的message是："+message);
			String title= message.getTitle();
			String content=message.getContent();
			
			String customContent = message.getCustomContent();
			if (customContent != null && customContent.length() != 0) {
				try {
					JSONObject obj = new JSONObject(customContent);
					// key1为前台配置的key
					if (!obj.isNull("type")) {
						int type = obj.getInt("type");
						Log.d(CLASS_NAME, "get custom value:" + type);
						switch (type) {
						case Contract.OPERA_LINE:
							if (obj.getBoolean("onlinestatus")==true) {
								device_onlinestatus.setText("在线");
								APP.host_status=true;
							}else {
								device_onlinestatus.setText("离线");
								APP.host_status=false;
							}
							if (obj.getBoolean("rcstatus")==true) {
								APP.RC_status=true;
								device_status_info.setText("已布防");
								alarm_process_status.setText("已布防成功！");
								command = 2;
								enableButton(command);
								UtilAudioPlay.playAudioFile(getActivity(), R.raw.bufang);
							}else {
								APP.RC_status=false;
								command = 1;
								enableButton(command);
								device_status_info.setText("已撤防");
								alarm_process_status.setText("已撤防成功！");
								UtilAudioPlay.playAudioFile(getActivity(), R.raw.chefang);
							}
							
							break;
							
						case Contract.OPERA_LINE_PROGRESS:
							String realtimeStatus=obj.getString("realtimeStatus");
							alarm_process_status.setText(realtimeStatus);
							
						
							break;
							
						default:
							break;
						}
						
						
						
					}
					// ...
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			
		}
	}

	/**
	 * 0 两个按钮都可以点击， 1只能点击布防， 2只能点击撤防 布防之后结果没出来之前不能再点击布防，等服务器返回之后才能继续操作
	 * 
	 * @param command
	 *            0 均可 1 只布防，2 只撤防
	 */
	public void enableButton(int command) {
		switch (command) {
		case 0:
			bufang_button.setEnabled(true);
			chefang_button.setEnabled(true);
			break;
		case 1:
			bufang_button.setEnabled(true);
			chefang_button.setEnabled(false);
			break;
		case 2:
			bufang_button.setEnabled(false);
			chefang_button.setEnabled(true);
			break;
		default:
			break;
		}

	}
public void showStatus(String string){
	alarm_process_status.setText(string);
	
}
}
