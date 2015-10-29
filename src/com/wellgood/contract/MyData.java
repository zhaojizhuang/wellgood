package com.wellgood.contract;

import com.wellgood.application.APP;

/**全局变量操作工具**/
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
}
