package com.wellgood.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.android.pc.ioc.inject.InjectLayer;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.wellgood.application.APP;
import com.wellgood.camera.CameraFragment;
import com.wellgood.fragment.InterviewFragment;
import com.wellgood.fragment.Security;
import com.wellgood.fragment.Public;
import com.wellgood.fragment.Business;
import com.wellgood.fragment.Message;
import com.wellgood.fragment.Settings;
import com.wellgood.fragment.WallView;
import com.wellgood.fragment.WallView1;
/**
 * @author zhaojizhuang
 *	功能描述：主程序入口 maintab
 * 通过加载5个fragment 利用fragmenttabhost进行切换
 */
@ContentView(R.layout.main_tab_layout)
public class MainActivity extends FragmentActivity{	
	
	public static String CLASS_NAME;
	//定义FragmentTabHost对象，并绑定
	@ViewInject(android.R.id.tabhost)
	private FragmentTabHost mTabHost;
	
	//定义一个布局
	private LayoutInflater layoutInflater;
		
	//定义数组来存放Fragment界面
	private Class fragmentArray[] = {
			
			CameraFragment.class,				//公共板块
			WallView1.class,				//商盟板块
			Security.class,				//安防板块
			Message.class,				//消息板块
			Settings.class				//设置板块
			};
	
	//定义数组来存放tab按钮图片
	private int mImageViewArray[] = {
			R.drawable.tab_home_btn,
			R.drawable.tab_message_btn,
			R.drawable.tab_selfinfo_btn,
			R.drawable.tab_square_btn,
			R.drawable.tab_more_btn
			};
	
	//Tab选项卡的文字
	private String mTextviewArray[] = {"公共", "商盟", "安防", "消息", "设置"};
	
	//构造函数得到类名
	public MainActivity(){
		CLASS_NAME=getClass().getName();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        //注入
        ViewUtils.inject(this);
        
        //初始化tabhost
        initView();

        
    }
	/**
	 * 返回键不退出
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			moveTaskToBack(false);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	/* @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.main, menu);
	        return true;
	    }*/
	    
	/**
	 * 初始化组件
	 */
	private void initView(){
		//实例化布局对象
		layoutInflater = LayoutInflater.from(this);
				
		//实例化TabHost对象，得到TabHost
		//mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		//setup在加载了view之后调用
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);	
		
		//得到fragment的个数
		int count = fragmentArray.length;	
				
		for(int i = 0; i < count; i++){	
			//为每一个Tab按钮设置图标、文字和内容
			TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
			//将Tab按钮添加进Tab选项卡中 绑定fragment
			mTabHost.addTab(tabSpec, fragmentArray[i], null);
			//设置Tab按钮的背景
			mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
		
		
			 
		
		}
		//设置默认的首先显示tab
		mTabHost.setCurrentTab(2);
		
	}
				
	/**
	 * 给Tab按钮设置图标和文字
	 */
	private View getTabItemView(int index){
		View view = layoutInflater.inflate(R.layout.tab_item_view, null);
	
		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
		imageView.setImageResource(mImageViewArray[index]);
		
		TextView textView = (TextView) view.findViewById(R.id.textview);		
		textView.setText(mTextviewArray[index]);
	
		return view;
	}
}
