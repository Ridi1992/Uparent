package com.lester.uparent;

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
import com.lester.uparent.bus.Busdata;
import com.lester.uparent.bus.Busdata.data;
import com.lester.uparent.teacherlist.Teacherdata;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.lester.uteacher.tool.Userinfo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Toast;

public class Schoolbus extends Activity implements OnClickListener{
	private LodingDialog lldialog;
	
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
		setContentView(R.layout.schoolbus);
		initview();
		getSchoolbus();
	}

	private void initview() {
		findViewById(R.id.back).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;

		default:
			break;
		}
		
	}
	private void setContent(final data data) {
		Texttool.setText(Schoolbus.this, R.id.address, data.getLineName()+"\t\t\t"+data.getBusLicense());
		Texttool.setText(Schoolbus.this, R.id.num,"座位数"+data.getSeatCount());
		Texttool.setText(Schoolbus.this, R.id.teacherName,data.getTeacherName());
		if(data.getTeacherGender().equals("F")){
			Texttool.setText(Schoolbus.this, R.id.sex,"女");
		}else{
			Texttool.setText(Schoolbus.this, R.id.sex,"男");
		}
		Texttool.setText(Schoolbus.this, R.id.teacherPhone,data.getTeacherPhone());
		findViewById(R.id.call).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String Tel=data.getTeacherPhone();
				if(Tel==null || Tel.equals("")){
					Toast.makeText(Schoolbus.this, "电话号码为空", 0).show();
				}else{
					Log.i("", Tel);
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_CALL);
					intent.setData(Uri.parse("tel:"+Tel));
					startActivity(intent);
				}
				
			}
		});
		
		WebView webview=(WebView) findViewById(R.id.webview);
		String htmlData=data.getBusTime();
		if(htmlData!=null){
			htmlData=htmlData.replaceAll("&amp;","");
			htmlData=htmlData.replaceAll("quot;","\"");
			htmlData=htmlData.replaceAll("lt;","<");
			htmlData=htmlData.replaceAll("gt;",">");
			webview.loadDataWithBaseURL(null,htmlData,"text/html","utf-8",null);
		}
	};
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			try {
				if(lldialog!=null){
					lldialog.dismiss();
				}
				switch (msg.what) {
				case Constants.GETSCHOOLBUS:
					Busdata.data data= (com.lester.uparent.bus.Busdata.data) msg.obj;
					if(data!=null){
						setContent(data);
					}else{
						Toast.makeText(getApplicationContext(), "没有线路信息！", 0).show();
					}
					break;
				case 404:
					Toast.makeText(getApplicationContext(), msg.obj.toString(), 0).show();
					break;
				}
			} catch (Exception e) {
			}
		}

		
	};
private void getSchoolbus() {
		
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		lldialog=LodingDialog.DialogFactor(Schoolbus.this, "正在获取数据", true);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "busLineService";
	            String method = "getByStudentId";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = 
	            		"{studentId:'"+Userinfo.getchildid(Schoolbus.this)+"'}";
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
	    				String jsonData = jsonObj.getString("data");
	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<Busdata.data>(){}.getType());
	    				handler.sendMessage(handler.obtainMessage(Constants.GETSCHOOLBUS, object));
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
