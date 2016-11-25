package com.lester.uteacher.tool;

import com.lester.uparent.login.logindata.data;
import com.lester.uparent.publicclass.Childdatas.datas;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


/**
 * 用户信息处理类
 * @author Administrator
 *
 */
public class Userinfo {
	public static String str="uparent";
	public static String mychild="mychild";
	public static void setinfo(Activity a, data data){
		SharedPreferences shared =a.getSharedPreferences(str, 0);
		Editor editor = shared.edit();
		editor.putBoolean("login", true);//已登录
		editor.putBoolean("isDefalutPhoto", data.isDefalutPhoto());
		editor.putString("password", data.getPassword());
		editor.putString("photo",data.getPhoto());
		editor.putString("id", data.getId());
		editor.putString("name",data.getName());
		editor.putString("mobile_phone", data.getMobile_phone());
		editor.putString("tel", data.getTelephone());
		editor.putString("id_card", data.getId_card());
		editor.putString("company", data.getCompany());
		editor.putString("sex", data.getGender());
		editor.putString("des", data.getDescription());
		editor.commit();
	}
	public static void setpassword(Activity a, String password){
		SharedPreferences shared =a.getSharedPreferences(str, 0);
		Editor editor = shared.edit();
		editor.putString("password", password);
		editor.commit();
	}
	
	/**
	 * 保存家长头像url
	 * @param a
	 * @param photo
	 */
	public static void setparentphoto(Activity a, String photo){
		SharedPreferences shared =a.getSharedPreferences(str, 0);
		Editor editor = shared.edit();
		editor.putString("photo",photo);
		editor.commit();
		System.out.println("头像保存成功");
	}
	public static String getname(Activity a){
		SharedPreferences shared =a.getSharedPreferences(str, 0);
		return shared.getString("name", "");
	}
	public static String getidcard(Activity a){
		SharedPreferences shared =a.getSharedPreferences(str, 0);
		return shared.getString("id_card", "");
	}
	public static String getpassword(Activity a){
		SharedPreferences shared =a.getSharedPreferences(str, 0);
		return shared.getString("password", "");
	}
	public static boolean isDefalutPhoto(Activity a){
		SharedPreferences shared =a.getSharedPreferences(str, 0);
		return shared.getBoolean("isDefalutPhoto", false);
	}
	public static String getsex(Activity a){
		SharedPreferences shared =a.getSharedPreferences(str, 0);
		return shared.getString("sex", "");
	}
	public static String getdes(Activity a){
		SharedPreferences shared =a.getSharedPreferences(str, 0);
		return shared.getString("des", "");
	}
	public static String getphoto(Activity a){
		SharedPreferences shared =a.getSharedPreferences(str, 0);
		return shared.getString("photo", "");
	}
	public static String getcompany(Activity a){
		SharedPreferences shared =a.getSharedPreferences(str, 0);
		return shared.getString("company", "");
	}
	public static String gettel(Activity a){
		SharedPreferences shared =a.getSharedPreferences(str, 0);
		return shared.getString("tel", "");
	}
	public static String getid_card(Activity a){
		SharedPreferences shared =a.getSharedPreferences(str, 0);
		return shared.getString("id_card", "");
	}
	public static Boolean islogin(Activity a){
		SharedPreferences shared =a.getSharedPreferences(str, 0);
		if(shared!=null){
			return shared.getBoolean("login", false);
		}else{
			return false;
		}
	}
	public static String getid(Activity a){
		SharedPreferences shared =a.getSharedPreferences(str, 0);
		return shared.getString("id", "");
	}
	public static String getmobile_phone(Activity a){
		SharedPreferences shared =a.getSharedPreferences(str, 0);
		return shared.getString("mobile_phone", "");
	}
	public static void setchildinfo(Activity a, datas datas) {
		SharedPreferences shared =a.getSharedPreferences(mychild, 0);
		Editor editor = shared.edit();
		editor.putBoolean("havechild", true);//已选择孩子
		editor.putString("id", datas.getId());
		editor.putString("name", datas.getName());
		editor.putString("id_card", datas.getId_card());
		editor.putString("school_name", datas.getSchoolName());
		editor.putString("school_id", datas.getSchool_id());
		editor.putString("photo", datas.getPhoto());
		editor.putString("classid", datas.getClazz_id());
		editor.putString("sex", datas.getGender());
		editor.putString("clazzName", datas.getClazzName());
		editor.commit();
	}
	public static void setchildphoto(Activity a, String photo) {
		SharedPreferences shared =a.getSharedPreferences(mychild, 0);
		Editor editor = shared.edit();
		editor.putString("photo", photo);
		editor.commit();
	}
	/**
	 * 是否已经选择孩子
	 * @param a
	 * @return
	 */
	public static Boolean havechild(Activity a){
		SharedPreferences shared =a.getSharedPreferences(mychild, 0);
		return shared.getBoolean("havechild", false);
	}
	public static String getchildid(Activity a){
		SharedPreferences shared =a.getSharedPreferences(mychild, 0);
		return shared.getString("id", "");
	}
	public static String getchildidsex(Activity a){
		SharedPreferences shared =a.getSharedPreferences(mychild, 0);
		return shared.getString("sex", "");
	}
	public static String getchildschool(Activity a){
		SharedPreferences shared =a.getSharedPreferences(mychild, 0);
		return shared.getString("school_name", "");
	}
	public static String getchildphoto(Activity a){
		SharedPreferences shared =a.getSharedPreferences(mychild, 0);
		return shared.getString("photo", "");
	}
	public static String getchildname(Activity a){
		SharedPreferences shared =a.getSharedPreferences(mychild, 0);
		return shared.getString("name", "");
	}
	public static String getchildschoolid(Activity a){
		SharedPreferences shared =a.getSharedPreferences(mychild, 0);
		return shared.getString("school_id", "");
	}
	public static String getchildclassid(Activity a){
		SharedPreferences shared =a.getSharedPreferences(mychild, 0);
		return shared.getString("classid", "");
	}
	public static String getchildclassname(Activity a){
		SharedPreferences shared =a.getSharedPreferences(mychild, 0);
		return shared.getString("clazzName", "");
	}
	
	
	
	/**
	 * 注销家长信息
	 */
	public static void logoff(Activity a){
		SharedPreferences shared =a.getSharedPreferences(str, 0);
		Editor editor = shared.edit();
		editor.putBoolean("login", false);//已登录
		editor.putString("password", "");
		editor.putString("photo","");
		editor.putString("id", "");
		editor.putString("name","");
		editor.putString("mobile_phone", "");
		editor.putString("tel", "");
		editor.putString("id_card", "");
		editor.putString("company", "");
		editor.putString("sex","");
		editor.putString("des", "");
		editor.commit();
	}
	/**
	 * 注销孩子信息
	 */
	public static void logoffchild(Activity a){
		SharedPreferences shared =a.getSharedPreferences(mychild, 0);
		Editor editor = shared.edit();
		editor.putBoolean("havechild", false);//已选择孩子
		editor.putString("id", "");
		editor.putString("name", "");
		editor.putString("id_card", "");
		editor.putString("school_name","");
		editor.putString("school_id", "");
		editor.putString("photo", "");
		editor.putString("classid", "");
		editor.putString("sex", "");
		editor.putString("clazzName", "");
		editor.commit();
	}
}
