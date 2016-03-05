package com.wellgood.fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.android.pc.ioc.inject.InjectBinder;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectView;
import com.android.pc.ioc.view.listener.OnItemClick;
import com.android.pc.ioc.view.listener.OnItemLongClick;
import com.android.pc.util.Handler_Inject;
import com.wellgood.activity.MessageInfoActivity;
import com.wellgood.activity.R;
import com.wellgood.adapter.InterviewAdapter;
import com.wellgood.application.APP;
import com.wellgood.contract.Contract;
import com.wellgood.database.DataBaseContract;
import com.wellgood.service.SimpleClient;

/*
 * Author: pan Email:gdpancheng@gmail.com
 * Created Date:2013-8-4
 * Copyright @ 2013 BU
 * Description: 绫绘弿杩?
 *
 * History:
 */
public class InterviewFragment extends BaseFragment {

	private static  String CLASS_NAME ;
	//@InjectView(binders = { @InjectBinder(method = "clicks", listeners = { OnItemClick.class }), @InjectBinder(method = "itemLongclick", listeners = { OnItemLongClick.class }) })
	//@InjectView(binders = { @InjectBinder(method = "onItemClick", listeners = { OnItemClick.class }) })

	@InjectView(binders = { @InjectBinder(method = "onItemClick", listeners = { OnItemClick.class }), @InjectBinder(method = "itemLongclick", listeners = { OnItemLongClick.class }) })
	//@InjectView
	 ListView interview_list;
	@InjectView
	private FrameLayout contain;
	private InterviewAdapter adapter;
	
	public InterviewFragment(){
		CLASS_NAME=getClass().getName();
	}
	View rootView;
	

    /** 发送消息的对象 */
    private MsgHandler          handler       = new MsgHandler();
    
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
                	
                case Contract.GET_MESSAGES_SUCCESS:
                	Log.d(CLASS_NAME, "获取信息列表成功");
                	adapter.notifyDataSetChanged();
                	endProgress();
                	break;
                case Contract.GET_MESSAGES_FAILED:
                	Log.d(CLASS_NAME, "获取信息列表失败");
                	break;
                	
                default:
                break;
            }

        }
    }
    
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.inflater = inflater;
		
		if (rootView == null) {
			  rootView = inflater.inflate(R.layout.activity_interview, container, false);
			}
			// 缓存的viewiew需要判断是否已经被加过parent，
			// 如果有parent需要从parent删除，要不然会发生这个view已经有parent的错误。
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null) {
			  parent.removeView(rootView);
			}

		
		
		Handler_Inject.injectFragment(this, rootView);
		Log.d(CLASS_NAME, "createview");
		//APP.messagesList=null;
		//APP.messagesList=new ArrayList<ContentValues>();
		Message();
		return rootView;
	}

	
	
	
	
	@InjectInit
	private void init(){
		
		
		adapter = new InterviewAdapter(activity, APP.messagesList);
		interview_list.setAdapter(adapter);
		setProgress(contain);
		startProgress();
		}
		

	public void onItemClick(AdapterView<?> arg0, View arg1, int potision, long arg3) {
		Log.d(CLASS_NAME, "on item click");
		//InterviewInfoFragment fragment = new InterviewInfoFragment();
		Intent intent = new Intent();
        //设置起始Activity和目标Activity,表示数据从这个Activity传到下个Activity
        intent.setClass(getActivity(),MessageInfoActivity.class);
        //一次绑定多个数据
        Bundle bundle = new Bundle();
        String msg_content=APP.messagesList.get(potision).getAsString("msg_content");
        bundle.putString("msg_content",msg_content);
        intent.putExtras(bundle);
        //打开目标Activity
        startActivity(intent);

	}
	
    
    public void Message(){
    	Log.d(CLASS_NAME, "Message()");
    	new Thread(){
    		public void run(){
    			
    			try {
    				
    			JSONObject response=getMessage();
    				
    				Log.d(CLASS_NAME, "getMessage responsejsonObject"+response);
    			} catch (Exception e1) {
    				e1.printStackTrace();
    			}
    			
    		};
    	}.start();
    }
    
    public JSONObject getMessage(){
       	JSONObject ss=null;
    	Log.d(CLASS_NAME, "getMessage()");
    			
    			try {
    			
    				
    				String getMessageURL=Contract.CONNECT_HOST+"/user/getMessage";
    				Log.d(CLASS_NAME, "getMessageURL"+getMessageURL);
    				ss=SimpleClient.doGet(getMessageURL,null);
    				Log.d(CLASS_NAME, "getMessage"+ss);
    				
    				JSONArray messages=ss.getJSONArray("data");
    			
    				for (int i = 0; i < messages.length(); i++) {
    					ContentValues values=new ContentValues();
    					String msg_type=((JSONObject) messages.get(i)).getString("msg_type");
    					values.put("msg_type", msg_type);
    					String msg_src=((JSONObject) messages.get(i)).getString("msg_src");
    					values.put("msg_src", msg_src);
    					String msg_sum=((JSONObject) messages.get(i)).getString("msg_sum");
    					values.put("msg_sum", msg_sum);
    					String msg_content=((JSONObject) messages.get(i)).getString("msg_content");
    					values.put("msg_content", msg_content);
    					String modifyDate=((JSONObject) messages.get(i)).getString("modifyDate");
    					values.put("modifyDate", modifyDate);
    					if (APP.messagesList.size()!=messages.length()) {
    						APP.messagesList.add(values);
						}
    					
					}
    				Log.e(CLASS_NAME, "messagesList"+APP.messagesList);
    				//键值对
    				ContentValues values=new ContentValues();
    				values.put(DataBaseContract.Column.ID, "2");
    				if (APP.messagesList!=null) {
    					handler.sendEmptyMessage(Contract.GET_MESSAGES_SUCCESS);
					}else if (APP.messagesList==null){
    					handler.sendEmptyMessage(Contract.GET_MESSAGES_FAILED);
					}
    				
    				
    			} catch (Exception e1) {
    				e1.printStackTrace();
    			}
    			
    			return ss;
    	
    }
	
}