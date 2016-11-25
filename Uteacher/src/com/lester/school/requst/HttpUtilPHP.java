package com.lester.school.requst;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.google.gson.reflect.TypeToken;
import com.lester.school.requst.Constants;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class HttpUtilPHP {
	
	
	/**
	 * 
	 * @param handler
	 * @param handlerTag
	 * @param invokeMethodName
	 * @param params
	 */
	public static void invokePost(Handler handler, int handlerTag,
			String invokeMethodName,final TypeToken returnType,String[][] params) {
			MyAsyncTask asyncTask = new MyAsyncTask(handler, handlerTag,
					returnType,invokeMethodName);
			asyncTask.execute(params);
	}
	
	private static class MyAsyncTask extends AsyncTask<String[][], Integer, Void> {
		
		private String invokeMethodName;
		private Handler handler;
		private int handlerTag;
		private TypeToken returnType;

		public MyAsyncTask(Handler handler, int handlerTag, TypeToken returnType,
				String invokeMethodName) {
			this.invokeMethodName = invokeMethodName;
			this.handlerTag = handlerTag;
			this.handler = handler;
			this.returnType=returnType;
		}

		@Override
		protected Void doInBackground(String[][]... params) {
			return taskLoad(this, handler,handlerTag, invokeMethodName,returnType, params);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

	}


	@SuppressWarnings("rawtypes")
	private static Void taskLoad(AsyncTask task, Handler handler,int handlerTag,
		String invokeMethodName,final TypeToken returnType, String[][]... params) {
	String result=null;
	Object object=null;
	try {
		String parmer="";
		if(params!=null){
			String[][] param=params[0];
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < param.length; i++) {
				buffer.append(param[i][1]).append("    ");
				if(i!=0){
					parmer+=param[i][0]+":'"+param[i][1]+"',";
				}
			}
			if(param.length>1)parmer=parmer.substring(0, parmer.length()-1);
			parmer=param[0][1]+"&params={"+parmer+"}";
			Log.i("", "开始远程调用， 方法名  : " + invokeMethodName
					+ "\n参数为 : " + parmer);
		}
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(invokeMethodName);
		if (!parmer.isEmpty()){
			StringEntity entity = new StringEntity(parmer,HTTP.UTF_8);
			entity.setContentType("application/x-www-form-urlencoded");
			request.setEntity(entity); 
		}
		HttpResponse response = client.execute(request);  
		HttpEntity entity = response.getEntity();
		byte[] data = EntityUtils.toByteArray(entity);
		result=new String(data,"UTF-8");
		Log.i("", "远程调用方法名  : " + invokeMethodName
				+ "\n返回数据 : " + result);
		JSONObject jsonObj = new JSONObject(result);
//		JSONObject jsonstatus = jsonObj.getJSONObject("status");
		Log.i("success==",jsonObj.getString("success"));
		switch (handlerTag) {
		case Constants.GETSCHOOL:
			if (jsonObj.getString("success").equals("true")) {
				String jsonData = jsonObj.getString("datas");
//				Log.i("jsonData==",jsonData);
				object=JsonUtil.instance().fromJson(jsonData, returnType.getType());
				handler.sendMessage(handler.obtainMessage(handlerTag, object));
			}
			break;

		case Constants.REGISTER:
			if (jsonObj.getString("success").equals("true")) {
//				String jsonData = jsonObj.getString("message");
//				Log.i("jsonData==",jsonData);
//				object=JsonUtil.instance().fromJson(jsonData, returnType.getType());
				handler.sendMessage(handler.obtainMessage(handlerTag, "注册成功"));
			}else{
				String jsonData = jsonObj.getString("message");
				handler.sendMessage(handler.obtainMessage(handlerTag, jsonData));
			}
			break;
		case Constants.LOGIN:
			if (jsonObj.getString("success").equals("true")) {
				String jsonData = jsonObj.getString("data");
//				Log.i("jsonData==",jsonData);
				object=JsonUtil.instance().fromJson(jsonData, returnType.getType());
				handler.sendMessage(handler.obtainMessage(handlerTag, object));
			}else{
				handler.sendMessage(handler.obtainMessage(406, "账户名或密码错误"));
			}
			break;
		case Constants.GETPASSWORD:
			if (jsonObj.getString("success").equals("true")) {
//				String jsonData = jsonObj.getString("message");
//				Log.i("jsonData==",jsonData);
//				object=JsonUtil.instance().fromJson(jsonData, returnType.getType());
				handler.sendMessage(handler.obtainMessage(handlerTag, "密码修改成功"));
			}else{
				handler.sendMessage(handler.obtainMessage(handlerTag, "密码修改失败"));
			}
			break;
		case Constants.CODEDETAIL:
//			if (jsonObj.getString("success").equals("true")) {
//				String jsonData = jsonObj.getString("data");
//				Log.i("jsonData==",jsonData);
//				object=JsonUtil.instance().fromJson(jsonData, returnType.getType());
				handler.sendMessage(handler.obtainMessage(handlerTag, result));
//			}else{
//				handler.sendMessage(handler.obtainMessage(handlerTag, "密码修改失败"));
//			}
			break;
		}
		
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		handler.sendMessage(handler.obtainMessage(404, "请求失败！"));
	} catch (ClientProtocolException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		handler.sendMessage(handler.obtainMessage(404, "请求失败"));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		handler.sendMessage(handler.obtainMessage(404, "亲，网络异常~~！"));
	} catch (Exception e) {
		e.printStackTrace();
		handler.sendMessage(handler.obtainMessage(404, "亲，网络异常~~！"));
	}
	return null;
	
}


}
