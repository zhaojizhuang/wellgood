package com.wellgood.contract;

import com.wellgood.application.APP;

/**全局变量操作工具
 * 用于将常用的变量存到sheredpreference**/
public class MyData {
	/**存储用户名到缓存**/
	public static void saveName(String name){
		APP.app.setData("usr_name",name );
	}
	/**取出缓存的用户名**/
	public static String getName(){
		String name =APP.app.getData("usr_name");
		return name;
	}
	/**存储用户名到缓存**/
	public static void savePassword(String password){
		APP.app.setData("usr_password",password );
	}
	/**取出缓存的用户名**/
	public static String getPassword(){
		String name =APP.app.getData("usr_password");
		return name;
	}
	
	
	
	/**存储记住账号状态到缓存**/
	public static void saveRememberFlag(Boolean isRemember){
		String temp=Boolean.toString(isRemember);
		APP.app.setData("isRemember",temp );
	}
	/**取得记住账号状态 **/
	public static Boolean getRememberFlag(){
		String temp=APP.app.getData("isRemember");
		Boolean isRemeber=Boolean.valueOf(temp).booleanValue();
		return isRemeber;
	}
	
	/**存储用户名到缓存**/
	public static void saveHostID(String host_id){
		APP.app.setData("host_id",host_id );
	}
	/**取出缓存的用户名**/
	public static String getHostID(){
		String host_id =APP.app.getData("host_id");
		return host_id;
	}
	/**存储摄像头id到缓存**/
	public static void saveCameraID(String cameraID){
		APP.app.setData("cameraID",cameraID );
	}
	/**取出缓存的摄像头id**/
	public static String getCameraID(){
		String host_id =APP.app.getData("cameraID");
		return host_id;
	}
	/**存储用户user_id到缓存**/
	public static void saveUserID(String user_id){
		APP.app.setData("user_id",user_id );
	}
	/**取出缓存的用户user_id**/
	public static String getUserID(){
		String user_id =APP.app.getData("user_id");
		return user_id;
	}
	/**存储记住主机状态到缓存**/
	public static void saveHostStatus(Boolean host_status){
		String temp=Boolean.toString(host_status);
		APP.app.setData("host_status",temp );
	}
	/**取得记住主机状态 **/
	public static Boolean getHostStatus(){
		String temp=APP.app.getData("host_status");
		Boolean host_status=Boolean.valueOf(temp).booleanValue();
		return host_status;
	}
	/**存储记住撤布防状态到缓存**/
	public static void saveRCStatus(Boolean RC_status){
		String temp=Boolean.toString(RC_status);
		APP.app.setData("RC_status",temp );
	}
	/**取得记住撤布防状态 **/
	public static Boolean getRCStatus(){
		String temp=APP.app.getData("RC_status");
		Boolean RC_status=Boolean.valueOf(temp).booleanValue();
		return RC_status;
	}
	
}
