package com.wellgood.fragment;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.pc.ioc.inject.InjectBinder;
import com.android.pc.ioc.inject.InjectView;
import com.android.pc.ioc.view.listener.OnClick;
import com.android.pc.util.Handler_Inject;
import com.wellgood.activity.R;
import com.wellgood.contract.Contract;
import com.wellgood.contract.MyData;
import com.wellgood.service.SimpleClient;

/**
 * 反馈消息
 * 
 * @author zhaojizhuang
 * 
 */
public class FeedbackFragment extends Fragment {
	
	public static String CLASS_NAME = "FeedbackFragment";
	
	EditText et_sendmessage;  //editview  不用注解，注解对于edtitext还不完善
	@InjectView(binders = { @InjectBinder(method = "OnClick", listeners = { OnClick.class }) })
	Button btn_feedback_submit; // 提交按钮
	
	View rootView;
	Handler handler=new MyHandler();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_feedback, container,false);
		Handler_Inject.injectOrther(this, rootView);
		
		et_sendmessage = (EditText) rootView.findViewById(R.id.et_sendmessage);

		return rootView;
	}
	
    @SuppressLint("HandlerLeak")				//屏蔽lint错误
    private final  class MyHandler extends Handler{
    	  @SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			
			/**登陆成功**/
			case Contract.FEEDBACK_SUCCESS:
				showtoast((String) msg.obj);
				et_sendmessage.setText("");
			default:
				break;
			}
			
		}
    	
    }
	
	
    public void OnClick(View view){
    	switch (view.getId()) {
    	
		case R.id.btn_feedback_submit:							//反馈
			Log.d(CLASS_NAME, "点击了反馈");
			
			sendMessageThread();
			break;
		default:
			break;
		}
    }

	public JSONObject sendMessage(String content) {
		JSONObject ss = null;
		


		try {
			JSONObject comment=new JSONObject();
			comment.put("usr_id", MyData.getUserID());
			comment.put("title", "用户反馈");
		
			comment.put("content", content);
			String URL = Contract.CONNECT_HOST + "/comment/add?comment="+comment;
			Log.d(CLASS_NAME, "sendmessageURL:"+URL);
			ss = SimpleClient.doGet(URL, null);
			Log.d(CLASS_NAME, "senmessage" + ss);

		} catch (Exception e1) {
			e1.printStackTrace();
		}

		return ss;
	}

	public void sendMessageThread() {

		new Thread() {
			public void run() {

				try {
					String comment=et_sendmessage.getText().toString();
					
					if (comment.length()==0) {
						Message msg= new Message();
						msg.what=Contract.FEEDBACK_SUCCESS;
						msg.obj="请输入评论内容!";
						handler.sendMessage(msg);
						return;
					}
					JSONObject jsonObject = sendMessage(comment);
					if (jsonObject!=null) {
						Message msg= new Message();
						msg.what=Contract.FEEDBACK_SUCCESS;
						msg.obj="反馈提交成功！感谢您的支持";
						handler.sendMessage(msg);
						
					}
					Log.d(CLASS_NAME, "sendMessage:jsonObject" + jsonObject);
					// bufang();
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			};
		}.start();

	}
	public void showtoast(String message){
		Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
	}
}
