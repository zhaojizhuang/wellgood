package com.wellgood.utils;

import org.json.JSONException;
import org.json.JSONObject;

import dalvik.annotation.TestTargetClass;

/**
 * {@link ResponseParse} 
 * 解析服务器回复的消息
 * @author Windows 7
 *
 */
public class ResponseParse {
	public  static JSONObject parseLoginResponse(String loginResponse){
		JSONObject jsonObject = null;
		String temp=loginResponse;
		temp=temp.replaceAll("\'", "\"");
		int length=temp.length();
		temp=temp.substring(7, length-2);
		try {
			jsonObject=new JSONObject(temp);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
		
	}
}
