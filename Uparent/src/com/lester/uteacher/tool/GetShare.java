package com.lester.uteacher.tool;


import android.content.SharedPreferences;

public class GetShare {
	public static String user_id(SharedPreferences shared){
		if(shared.getString("user_id", "").equals("")){
			return "";
		}else{
			return shared.getString("user_id", "");
		}
	}
	/**
	 * 判断身份是否认证
	 */
	public static boolean isindentity(SharedPreferences shared){
		if(shared.getBoolean("isindentity", false)){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 获取用户昵称
	 */
	public static String nick_name(SharedPreferences shared){
		if(shared.getString("nick_name", "").equals("")){
			return "匿名用户";
		}else{
			return shared.getString("nick_name", "");
		}
	}
}
