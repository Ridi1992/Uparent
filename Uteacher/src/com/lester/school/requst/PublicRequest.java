package com.lester.school.requst;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import android.os.Handler;

import com.google.gson.reflect.TypeToken;
import com.lester.company.http.HttpURL;
import com.lester.company.http.Interface;
import com.lester.uteacher.info.Teacherinfo;
import com.lester.uteacher.register.GetData;
import com.lester.uteacher.tool.MD5;

public class PublicRequest{
	
	private static PublicRequest instance;
	private static Handler handler;
	public static PublicRequest getInstance(Handler handler){
		instance=new PublicRequest();
		PublicRequest.handler=handler;
		return instance;
	}
	
	/**
	 * 幼儿园列表
	 * @throws NoSuchAlgorithmException 
	 */
	public void getschool(int what){
		String service=  "schoolService";
		String method=  "searchAll";
		String str="service="+service+
				"&method="+method+
				"&token="+MD5.MD5(service+method+Constants.Key);
		String[][] param=new String[1][];
		param[0]=new String[]{"str",str};
		HttpUtilPHP.invokePost(myHandler,what,
				HttpURL.URL, new TypeToken<List<GetData>>(){}, param);
	}
	/**
	 * 注册
	 * @throws NoSuchAlgorithmException 
	 */
	public void register(int what,
			String school_id,
			String 	name,
			String mobilePhone,
			String gender,
			String idCard
			){
		String service=  "teacherRegistService";
		String method=  "regist";
		String str="service="+service+
				"&method="+method+
				"&token="+MD5.MD5(service+method+Constants.Key);
		String[][] param=new String[6][];
		param[0]=new String[]{"str",str};
		param[1]=new String[]{"school_id",school_id};
		param[2]=new String[]{"name",name};
		param[3]=new String[]{"mobile_phone",mobilePhone};
		param[4]=new String[]{"gender",gender};
		param[5]=new String[]{"id_card",idCard};
		HttpUtilPHP.invokePost(myHandler,what,
				HttpURL.URL, new TypeToken<List<String>>(){}, param);
	}
	/**
	 * 登录
	 */
	public void login(int what,
			String loginId,
			String 	password
			){
		String service=  "teacherService";
		String method=  "login";
		String str="service="+service+
				"&method="+method+
				"&token="+MD5.MD5(service+method+Constants.Key);
		String[][] param=new String[3][];
		param[0]=new String[]{"str",str};
		param[1]=new String[]{"loginId",loginId};
		param[2]=new String[]{"password",password};
		HttpUtilPHP.invokePost(myHandler,what,
				HttpURL.URL, new TypeToken<Teacherinfo.data>(){}, param);
	}
	/**
	 * 重置密码
	 * @param what
	 * @param loginId
	 * @param password
	 */
	public void getpassword(int what,
			String 	loginId,
			String 	newPassword
			){
		String service=  "teacherService";
		String method=  "setNewPassword";
		String str="service="+service+
				"&method="+method+
				"&token="+MD5.MD5(service+method+Constants.Key);
		String[][] param=new String[3][];
		param[0]=new String[]{"str",str};
		param[1]=new String[]{"loginId",loginId};
		param[2]=new String[]{"newPassword",newPassword};
		HttpUtilPHP.invokePost(myHandler,what,
				HttpURL.URL, new TypeToken<List<String>>(){}, param);
	}
	public void codedetail(int what,
			String loginId,
			String 	content
			){
		String service=  "outSchoolService";
		String method=  "readQRCode";
		String str="service="+service+
				"&method="+method+
//				"&token=test";
				"&token="+MD5.MD5(service+method+Constants.Key);
		String[][] param=new String[3][];
		param[0]=new String[]{"str",str};
		param[1]=new String[]{"loginId",loginId};
		param[2]=new String[]{"content",content};
		HttpUtilPHP.invokePost(myHandler,what,
				HttpURL.URL, new TypeToken<String>(){}, param);
	}
	private Handler myHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
//			case 404:
//				handler.handleMessage(handler.obtainMessage(404, "编码错误"));
//				break;
			default:
				handler.handleMessage(handler.obtainMessage(msg.what, msg.obj));
				break;
			}
		};
		
	};
	

}
