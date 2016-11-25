package com.lester.uteacher;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.baidu.mobstat.StatService;
import com.dream.framework.utils.dialog.LodingDialog;
import com.google.gson.reflect.TypeToken;
import com.lester.company.http.HttpURL;
import com.lester.school.requst.Constants;
import com.lester.school.requst.JsonUtil;
import com.lester.uteacher.goton.Classlist;
import com.lester.uteacher.leave.Leavedata;
import com.lester.uteacher.leave.Leavedata.datas;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.lester.uteacher.tool.Userinfo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class Changepassword extends Activity implements OnClickListener{
	private LodingDialog lldialog;
	private String newPassword,newPassword1,oldPassword;

	@Override
	protected void onResume() {
		super.onResume();
	 StatService.onResume(this);
	}
	@Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.changepassword);
	initview();
	}

	private void initview() {
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.makesure).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.makesure:
			oldPassword=((TextView) findViewById(R.id.oldpassword)).getText().toString();
			newPassword=((TextView) findViewById(R.id.newpassword)).getText().toString();
			newPassword1=((TextView) findViewById(R.id.newpassword1)).getText().toString();
			
			if(!Texttool.Havecontent(Changepassword.this, R.id.oldpassword)){
				Toast.makeText(getApplicationContext(), "旧密码不能为空", 0).show();
				break;
			}
			if(!Texttool.Havecontent(Changepassword.this, R.id.newpassword)){
				Toast.makeText(getApplicationContext(), "请输入新密码", 0).show();	
				break;
				}
			if(!Texttool.Havecontent(Changepassword.this, R.id.newpassword1)){
				Toast.makeText(getApplicationContext(), "确认新密码", 0).show();
				break;
			}
			if(!MD5.MD5(oldPassword).equals(Userinfo.getpassword(Changepassword.this))){
				Toast.makeText(getApplicationContext(), "旧密码不正确", 0).show();
				break;
			}
			
			if(!newPassword.equals(newPassword1)){
				Toast.makeText(getApplicationContext(), "新密码不一致", 0).show();
				break;
			}
			changpassword();
			break;
		}
	}
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			try {
				if(lldialog!=null && msg.what!=Constants.GETCLASS){//获取班级列表后直接请求学生数据，所以此处不取消弹框
					lldialog.dismiss();
			}
			switch (msg.what) {
			
			case Constants.CHANGEPPASSWORD:
				if(msg.obj.toString().equals("修改成功")){
					Toast.makeText(getApplicationContext(), "修改成功", 0).show();
					Userinfo.setpassword(Changepassword.this, MD5.MD5(newPassword));
					finish();
				}
		
			break;
			case 404:
				Toast.makeText(getApplicationContext(), msg.obj.toString(), 0).show();
				break;
			}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	};
	private void changpassword() {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		lldialog=LodingDialog.DialogFactor(Changepassword.this, "正在提交数据", true);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "teacherService";
	            String method = "modifyPassword";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = "{loginId:'"+Userinfo.getmobile_phone(Changepassword.this)+
	            		"',newPassword:'"+MD5.MD5(newPassword)+
	            		"',oldPassword:'"+MD5.MD5(oldPassword)+
	            		"'}";
	            Log.i("service", service);
	            Log.i("method", method);
	            Log.i("token", token);
	            Log.i("params", params);
	            NameValuePair pair1 = new BasicNameValuePair("service", service);
	            NameValuePair pair2 = new BasicNameValuePair("method", method);
	            NameValuePair pair3 = new BasicNameValuePair("token", token);
	            NameValuePair pair4 = new BasicNameValuePair("params", params);
	            List<NameValuePair> bodyParams = new ArrayList<NameValuePair>();
	            bodyParams.add(pair1);
	            bodyParams.add(pair2);
	            bodyParams.add(pair3);
	            bodyParams.add(pair4);
	            try {
	                HttpEntity requestHttpEntity = new UrlEncodedFormEntity(bodyParams, HTTP.UTF_8);
	                // URL使用基本URL即可，其中不需要加参数
	                HttpPost httpPost = new HttpPost(HttpURL.URL);
	                // 将请求体内容加入请求中
	                httpPost.setEntity(requestHttpEntity);
	                // 需要客户端对象来发送请求
	                HttpClient httpClient = new DefaultHttpClient();
	                // 发送请求
	                HttpResponse response = httpClient.execute(httpPost);
	                HttpEntity entity = response.getEntity();
	        		byte[] data = EntityUtils.toByteArray(entity);
	        		String result=new String(data,"UTF-8");
	        		Log.i("", "\n返回数据 : " + result);
	        		JSONObject jsonObj = new JSONObject(result);
	        		if (jsonObj.getString("success").equals("true")) {
//	    				String jsonData = jsonObj.getString("datas");
//	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<List<Leavedata.datas>>(){}.getType());
	    				handler.sendMessage(handler.obtainMessage(Constants.CHANGEPPASSWORD, "修改成功"));
	    			}else{
	    				handler.sendMessage(handler.obtainMessage(404, jsonObj.getString("message")));
	    			}
	                // 显示响应
	            } catch (Exception e) {
	                e.printStackTrace();
	            } finally {
	            }
	        }
	    }).start();
	}
	
}
