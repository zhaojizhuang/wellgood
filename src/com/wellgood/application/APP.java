package com.wellgood.application;

import com.android.pc.ioc.app.Ioc;
import com.android.pc.util.Handler_SharedPreferences;
import com.hik.mcrsdk.MCRSDK;
import com.hik.mcrsdk.rtsp.RtspClient;

import android.app.Application;
import android.content.SharedPreferences;
/**
 * 存放全局变量
 * {@link SharedPreferences} 保存
 * @author ZhaoJizhuang
 *
 */
public class APP extends Application {

public static APP app;
	
	
	@Override
	public void onCreate() {
		 app = this;
		 
		 //初始化ioc框架
		Ioc.getIoc().init(this);
	    super.onCreate();
	    /** 初始化视频框架 **/
	    MCRSDK.init();
        RtspClient.initLib();
        MCRSDK.setPrint(1, null);
	    
	    
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

	//视频调用的
	public static APP getIns() {
		return app;
	}
	
}
