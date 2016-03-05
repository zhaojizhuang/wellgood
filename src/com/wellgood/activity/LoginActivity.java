package com.wellgood.activity;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.pc.ioc.inject.InjectBinder;
import com.android.pc.ioc.inject.InjectView;
import com.android.pc.ioc.view.listener.OnClick;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.common.Constants;
import com.wellgood.application.APP;
import com.wellgood.contract.Contract;
import com.wellgood.contract.MyData;
import com.wellgood.service.SimpleClient;
import com.wellgood.update.UpdateManager;
/**
 * 登陆界面
 * @author Windows 7
 *	@date  2015-11-3
 */
//@InjectLayer(value = R.layout.activity_login)
public class LoginActivity extends Activity {
	/**服务器平台的地址 **/
	String Cookie;
	String usr_id;
	String host_id;
	boolean login_status;
	String login_message;
	
	/**   字符串请求URL  **/
	public static  String alarm_id="201510112342290296";

	/** 实例化handler对象**/
	public MyHandler handler=new MyHandler();
	
	private static String CLASS_NAME;
	
	
	/**    请求url  **/
    private String requestURL;
    /**   登陆进度条  **/
    private ProgressDialog pd;
    
    
    /** 服务器回复的字符串  **/
    private String responseString;
    /** 登陆成功与否**/
    private Boolean success=false;
    /**登陆错误类型 **/
    private String message;
    
    /**  用户名和密码  **/
    private String usr_name;
    private String usr_password;
    /** 是否记住密码**/
    private Boolean isRemeber;					
    
  /**
   * 绑定按钮
   */
  	@InjectView(binders = { @InjectBinder(method = "OnClick", listeners = { OnClick.class }) })
  	View 	login_button,register_btn;					//登陆按钮
  		//	december_button,				//忘记密码
  		//	register_button;				//注册
  	
   // @InjectView
   EditText username							//用户名
   			,password;									//密码
	

    public LoginActivity(){
    	CLASS_NAME=getClass().getName();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    	APP.getIns().addActivity(this);
        username=(EditText) findViewById(R.id.username);
        password=(EditText) findViewById(R.id.password);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        if (MyData.getRememberFlag()) {
        	usr_name=MyData.getName();
        	Log.d(CLASS_NAME, "usr_name=MyData.getName();"+usr_name);
        	username.setText(usr_name.toString());
        	usr_password=MyData.getPassword();
        	Log.d(CLASS_NAME, "	usr_password=MyData.getPassword();"+usr_password);
        	password.setText(usr_password);
			//Login1();
		}
        Log.d(CLASS_NAME, "oncreate()");
     // 检查更新
     		UpdateManager manager = new UpdateManager(this);

     		manager.checkUpdate();
    }
    
    /**
     * 按钮监听方法
     * @param view
     */
    public void OnClick(View view){
    	switch (view.getId()) {
    	/**
    	 * 登陆按钮
    	 */
		case R.id.login_button:
			
			Log.d(CLASS_NAME, "点击了登陆");
        	usr_name=username.getText().toString();
     	   
        	usr_password=password.getText().toString();
        	if (APP.NetAvalible==true) {
        		Login1();
			}
			else {
				netdialog();
			}
			
			break;
			case R.id.register_btn:
				Log.d(CLASS_NAME, "点击了注册");
				Intent intent =new Intent(this, RegisteActivity.class);
				startActivity(intent);
				break;
		/**
		 * 忘记密码
		 */
		//case R.id.december_button:
			
		//	Log.d(CLASS_NAME, "点击了忘记密码");
			//Login();
			/*Intent intent = new Intent (LoginActivity.this,MainActivity.class);
			startActivity(intent);*/
		//	break;
		/**
		 * 注册按钮
		 */
		//case R.id.register_button:
			
		//	Log.d(CLASS_NAME, "点击了注册");
			//Login();
			/*Intent intent = new Intent (LoginActivity.this,MainActivity.class);
			startActivity(intent);*/
		//	break;
		default:
			break;
		}
    }
	/** handler  所以我们只要
	 * 在run方法中------发送Message,
	 * 在Handler里handleMessage----接收Message，
	 * 通过不同的Message执行不同的任务。  **/
	
	
    @SuppressLint("HandlerLeak")				//屏蔽lint错误
    private final  class MyHandler extends Handler{
    	  @SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			
			/**登陆成功**/
			case Contract.LOGIN_SUCCESS:
				pd.dismiss();
				ShowToast("登录成功！");
				//dialog("登陆成功！");
				saveUser();
				getUserThread();
				gethost();
//				if (isServiceRunning(LoginActivity.this,"com.wellgood.service.BackGroundService")) {
//					
//					Toast.makeText(LoginActivity.this,"服务已经在运行", Toast.LENGTH_SHORT).show();
//					stopService(new Intent(LoginActivity.this,BackGroundService.class));
//					startService(new Intent(LoginActivity.this,BackGroundService.class));
//				}else {
//	        		//启动服务
//					startService(new Intent(LoginActivity.this,BackGroundService.class));
//				}
				
				startActivity(new Intent(LoginActivity.this,MainActivity.class));
				LoginActivity.this.finish();
				break;
				
				/**登录失败 **/
			case Contract.LOGIN_FAILED:
				pd.dismiss();
				dialog((String)msg.obj);
				break;
			case Contract.GETUSER_ID_SUCCESS:
				registeXinge(MyData.getUserID());
				break;
			case Contract.GETUSER_ID_FAILED:
				ShowToast("获取用户ID失败！");
				break;
			default:
				break;
			}
			
		}
    	
    }
    /**
     * 注册信鸽推送
     * @param account
     */
    public void registeXinge(String account) {
    	
		// 注册接口
		XGPushManager.registerPush(getApplicationContext(),account,
				new XGIOperateCallback() {
					@Override
					public void onSuccess(Object data, int flag) {
						Log.w(Constants.LogTag,
								"+++ register push sucess. token:" + data);
//						m.obj = "+++ register push sucess. token:" + data;
//						m.sendToTarget();
						Log.d(CLASS_NAME,"+++ register push sucess. token:" + data);
					}

					@Override
					public void onFail(Object data, int errCode, String msg) {
						Log.w(Constants.LogTag,
								"+++ register push fail. token:" + data
										+ ", errCode:" + errCode + ",msg:"
										+ msg);
//
//						m.obj = "+++ register push fail. token:" + data
//								+ ", errCode:" + errCode + ",msg:" + msg;
//						m.sendToTarget();
					}
				});
    	
	}
    
    /**
     * saveuser保存用户名和密码，以便下次自动登陆
     */
    public void saveUser(){
    	MyData.saveName(usr_name);
    	MyData.savePassword(usr_password);
    	//记住密码
    	MyData.saveRememberFlag(true);
    }
    
    /**
     * 登录
     */
    public void Login1(){
    	
    	
    	Log.d(CLASS_NAME, "进入login（）1");
    	
    	JSONObject user = new JSONObject();  
/*        	usr_name=username.getText().toString();
    	   
        	usr_password=password.getText().toString();*/
        	
        	Log.d(CLASS_NAME, "usr_name:"+usr_name);
        	Log.d(CLASS_NAME, "usr_password:"+usr_password);
        	String msg="请重新输入：";
    	   if(usr_name.length()<=2||usr_password.length()<=2){
    		   msg=msg+"用户名和密码";
    			
    			dialog(msg);
    			
    		   return;  
    	   }
    	   
    	    try {
		    	 user.put("usr_name", usr_name);  
		    	 user.put("usr_pwd", usr_password);
		} catch (JSONException e) {
			
				e.printStackTrace();
		}  
    	   
    	   
    	    pd = ProgressDialog.show(this,"正在登陆...","请稍后...");
    	    requestURL=new String(Contract.CONNECT_HOST+"/user/login?user="+user.toString());
    	    
    	    Log.d(CLASS_NAME, "Login()1");
    	    
    	
    	new Thread(){
    		public void run(){
    			
    			JSONObject response;
    			try {
    				//网络请求
    				Log.d(CLASS_NAME, "login-requestURL"+requestURL);
    				response = SimpleClient.doGet(requestURL,null);
    				Log.d(CLASS_NAME, "loginresponse"+response);
    				
    				
    				//请求返回的数据
    				  
			           
						try {
							JSONObject js=response.getJSONObject("data");
							login_status=js.getBoolean("success");
							login_message=js.getString("message");
							Log.d(CLASS_NAME, "login_status:"+login_status);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Log.e(CLASS_NAME, "e"+e);
						}
						if (login_status==true) {
							JSONObject getuser=getUser();
							Log.d(CLASS_NAME, "getuser"+getuser);
							 // 登陆成功
				            Message msg = new Message();
							msg.what =Contract.LOGIN_SUCCESS;
							msg.obj =login_message;
							handler.sendMessage(msg);
							//checkLogin(usr_name);
						}
						else {
						// 登陆失败
			            Message msg = new Message();
						msg.what =Contract.LOGIN_FAILED;
						msg.obj =login_message;
						handler.sendMessage(msg);
					}
    				
    				
    				
    				
    			} catch (Exception e1) {
    				e1.printStackTrace();
    			}
    			
    		};
    	}.start();
    	
    }
    public JSONObject checkLogin(String user_name){
    	JSONObject ss=null;
    	   String URL=new String(Contract.CONNECT_HOST+"/user/isLogin");
    	Log.d(CLASS_NAME, "Login()");
    			
    			try {
    				ss=SimpleClient.doGet(URL,null);
    				Log.d(CLASS_NAME, "logincheck"+ss);
    				
    			} catch (Exception e1) {
    				e1.printStackTrace();
    			}
    			
    			return ss;
    	
    }
    public JSONObject getUser(){
    	JSONObject ss=null;
 	   String URL=new String(Contract.CONNECT_HOST+"/user/getUser");
 	Log.d(CLASS_NAME, "getUser()");
 			
 			try {
 				ss=SimpleClient.doGet(URL,null);
 			
 				
 			} catch (Exception e1) {
 				e1.printStackTrace();
 			}
 			
 			return ss;
    }
    
    public void getUserThread(){
    	
      	new Thread(){
    		public void run(){
    			String userID=null;
    			
    			try {
    				JSONObject jsonObject=getUserID();
    				JSONObject data=jsonObject.getJSONObject("data");
    				Log.d(CLASS_NAME, "getuserid:"+data);
    				if (data!=null) {
    					 userID=data.getString("id");
        				MyData.saveUserID(userID);
        				  Message msg = new Message();
  						msg.what =Contract.GETUSER_ID_SUCCESS;
  						msg.obj =userID;
  						handler.sendMessage(msg);
					}else {
						  Message msg = new Message();
	  						msg.what =Contract.GETUSER_ID_FAILED;
	  						msg.obj =userID;
	  						handler.sendMessage(msg);
					}
    				
    				//bufang();
    			} catch (Exception e1) {
    				e1.printStackTrace();
    			}
    			
    		};
    	}.start();
    	
    	
    }
    
    
    public void gethost(){
    	
      	new Thread(){
    		public void run(){
    			
    			
    			try {
    				JSONObject jsonObject=getHostID();
    				JSONObject data=jsonObject.getJSONObject("data");
    				JSONArray datas=data.getJSONArray("datas");
    				JSONObject host3333=datas.getJSONObject(0);
    				host_id=host3333.getString("host_id");
    				MyData.saveHostID(host_id);
    				Log.d(CLASS_NAME, "host_id"+host_id);
    				//bufang();
    			} catch (Exception e1) {
    				e1.printStackTrace();
    			}
    			
    		};
    	}.start();
    	
    	
    }
    public void bufang(){
    	new Thread(){
    		public void run(){
    			
    			
    			try {
    				
    				String buFangURL=Contract.CONNECT_HOST+"/host/addLine?hostIds="+host_id;
        			Log.d(CLASS_NAME, "buFangURL"+buFangURL);
    				JSONObject jsonObject=SimpleClient.doGet(buFangURL,null);
    				
    				Log.d(CLASS_NAME, "bufang responsejsonObject"+jsonObject);
    			} catch (Exception e1) {
    				e1.printStackTrace();
    			}
    			
    		};
    	}.start();
    }
    
    public JSONObject getUserID(){
       	JSONObject ss=null;
    	JSONObject page=new JSONObject();
	
    	Log.d(CLASS_NAME, "getUserID()");
    			
    			try {
    				
    				JSONObject user=new JSONObject();
    				String getUserURL=Contract.CONNECT_HOST+"/user/getUser";
    				ss=SimpleClient.doGet(getUserURL,null);
    				Log.d(CLASS_NAME, "getUserID"+ss);
    				
    			} catch (Exception e1) {
    				e1.printStackTrace();
    			}
    			
    			return ss;
    	
    }
    public JSONObject getHostID(){
       	JSONObject ss=null;
    	JSONObject page=new JSONObject();
	
    	Log.d(CLASS_NAME, "getHostID()");
    			
    			try {
    				page.put("pageNo", "1");
    				page.put("pageSize", "2");
    				JSONObject host=new JSONObject();
    				host.put("page", page);
    				String getHostURL=Contract.CONNECT_HOST+"/host/page?host="+host.toString();
    				ss=SimpleClient.doGet(getHostURL,null);
    				Log.d(CLASS_NAME, "getHostID"+ss);
    				
    			} catch (Exception e1) {
    				e1.printStackTrace();
    			}
    			
    			return ss;
    	
    }
    /**
     * 显示toast ，值为传入的string
     * @param string
     */
    public void ShowToast(String string){
    	Toast.makeText(this, string, Toast.LENGTH_LONG);
    }
    /**
     * 显示dialog ，值为传入的string
     * @param string
     */

	protected void dialog(String string) {
			AlertDialog.Builder builder = new Builder(LoginActivity.this);
			builder.setMessage(string);
			builder.setTitle("提示");
			builder.setPositiveButton("确认", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
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
	/** 确认dialog **/
	private void netdialog() {
		AlertDialog.Builder builder = new Builder(LoginActivity.this);
		builder.setMessage("处于断网状态，请检查网络设置");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							   dialog.dismiss();
						//跳转到网络设置
						Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");   
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
	
}
