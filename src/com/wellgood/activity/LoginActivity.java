package com.wellgood.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.pc.ioc.inject.InjectBinder;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectLayer;
import com.android.pc.ioc.inject.InjectListener;
import com.android.pc.ioc.inject.InjectMethod;
import com.android.pc.ioc.inject.InjectView;
import com.android.pc.ioc.view.listener.OnClick;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wellgood.application.APP;
import com.wellgood.contract.MyData;
import com.wellgood.utils.ResponseParse;
@InjectLayer(value = R.layout.activity_login)
public class LoginActivity extends Activity {
	
	private static String CLASS_NAME;
    private String TAG = this.getClass().getSimpleName();
    /***请求队列 **/
    private RequestQueue volleyRequestQueue;
	/**   字符串请求对象  **/
	public static StringRequest stringRequest;
	
	
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
    
  //注解
  	@InjectView(binders = { @InjectBinder(method = "click", listeners = { OnClick.class }) })
  	Button login_button,december_button,register_button;				//我的行业 
  	
    @InjectView
   EditText username,password;

    public LoginActivity(){
    	CLASS_NAME=getClass().getName();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        Log.d(CLASS_NAME, "oncreate()");
        Show(CLASS_NAME+"oncreate()");
    	// 1.创建请求队列  
	    volleyRequestQueue = Volley.newRequestQueue(this);  

    }
    @InjectInit
	private void init() {
    	
    }
    @InjectMethod(@InjectListener(ids = { R.id.login_button }, listeners = { OnClick.class }))
    public void OnClick(View view){
    	switch (view.getId()) {
		case R.id.login_button:
			Log.d(CLASS_NAME, "点击了loginbutton");
			//Login();
			Intent intent = new Intent (LoginActivity.this,MainActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
    }
    
    /**
     * 登陆操作，在这里调用 登陆的网络请求，通过异步类实现
     */
    public void Login(){
    	Log.d(CLASS_NAME, "进入login（）");
        //请求队列
    	 
    	 MyData.getRememberFlag();
    	 JSONObject user = new JSONObject();  
    	 //取得账号密码
	   usr_name=username.getText().toString();
	   usr_password=password.getText().toString();
	   String msg="请输入：";
	   if(usr_name==null){
		   //Show("")
		   msg=msg+"用户名";
		   if (usr_password==null) {
			msg=msg+"和密码";
			dialog(msg);
			return;
		}
				  
	   }
	   
	    try {
		    	 user.put("usr_name", usr_name);  
		    	 user.put("usr_pwd", usr_password);
		} catch (JSONException e) {
			
				e.printStackTrace();
		}  
	    
	    
	    pd = ProgressDialog.show(this,"正在登陆...","Please Wait...");
	    requestURL=new String("http://192.168.0.45:8080/platform/user/login?user="+user.toString());
	    
    	//开始异步网络任务	
		new PostTask().execute(requestURL);
		
    }
    public void Show(String string){
    	Toast.makeText(this, string, Toast.LENGTH_LONG);
    }
   
    /**
     * PostTask 为asynctask的子类
     * @author Windows 7
     * 第一个参数 doinbackground（）中调用，第二个参数在onprogressupdate中调用，第三个在onpostexecute中调用

     */
	public final class PostTask extends AsyncTask<String, Integer, String>{				
		
		
		
		
		/**  
	     * 这里的String参数对应AsyncTask中的第一个参数   
	     * 这里的String返回值对应AsyncTask的第三个参数  
	     * 该方法并不运行在UI线程当中，主要用于异步操作，所有在该方法中不能对UI当中的空间进行设置和修改  
	     * 但是可以调用publishProgress方法触发onProgressUpdate对UI进行操作  
	     */  
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			//
				Log.d(CLASS_NAME, "doInBackground...正在请求"+params[0]);
		   try {
			   
			   
			   
			//开始进行网络操作
					//这里params【0】穿入的是登陆的url
					    // 3.json post请求处理  
						 stringRequest = new StringRequest(params[0],
									new Response.Listener<String>() {
										@Override
										public void onResponse(String response) {
											//Log.d(CLASS_NAME,"response"+ response);
											responseString=response;
											Log.d(CLASS_NAME,"responsestring"+ responseString);
											publishProgress(100);
										}
									}, new Response.ErrorListener() {
										@Override
										public void onErrorResponse(VolleyError error) {
											Log.e(CLASS_NAME,"error" +error.getMessage(), error);
										}
									});  
					   
					    // 4.请求对象放入请求队列  
					    volleyRequestQueue.add(stringRequest);  
			
				
				
				
		   } catch (Exception e) {
				// TODO: handle exception
			   e.printStackTrace();
			}
		   return responseString;
				
		}
		
		/**这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）  
	     * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置  */
		@Override
		protected void onPostExecute(String resault){
			super.onPostExecute(resault);
			Log.d(CLASS_NAME, "异步任务返回的结果是："+resault);
			Toast.makeText(LoginActivity.this, resault, Toast.LENGTH_LONG).show();
			//在这里解析
			JSONObject jsonObject=new JSONObject();
			jsonObject=ResponseParse.parseLoginResponse(resault);
			
			try {
				success=jsonObject.getBoolean("success");
				message=jsonObject.getString("message");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//登陆成功之后的下一次操作
			Show(message);
			Intent intent=new Intent(LoginActivity.this,MainActivity.class);
			startActivity(intent);
			
		}
		//UI上更新进度
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			if (values[0]==100) {
				pd.dismiss();
			}
		}


	}
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
}
