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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.dream.framework.utils.dialog.LodingDialog;
import com.google.gson.reflect.TypeToken;
import com.lester.company.http.HttpURL;
import com.lester.school.requst.Constants;
import com.lester.school.requst.JsonUtil;
import com.lester.uparent.schoolinfo.Schoolinfo.data;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.lester.uteacher.tool.Userinfo;

public class Schoolinfo extends Activity implements OnClickListener{
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
		setContentView(R.layout.schoolinfo);
		initview();
		getSchoolinfo();
	}

	private void initview() {
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.pingjia).setOnClickListener(this);
		findViewById(R.id.lishipingjia).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		Intent intent ;
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.pingjia:
			intent =new Intent(Schoolinfo.this,Comment.class);
			startActivity(intent);
			break;
		case R.id.lishipingjia:
			intent = new Intent(Schoolinfo.this,Commentlist.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	private void setInfo(data data) {
		Texttool.setText(Schoolinfo.this, R.id.name, data.getName());
		Texttool.setText(Schoolinfo.this, R.id.linkp, data.getLinkMan());
		Texttool.setText(Schoolinfo.this, R.id.linktel, data.getLinkPhone());
		Texttool.setText(Schoolinfo.this, R.id.address, data.getAddress());

		WebView webview=(WebView) findViewById(R.id.webview);
		String htmlData=data.getIntroduction();
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
				case Constants.GETSCHOLLINFO:
					com.lester.uparent.schoolinfo.Schoolinfo.data data = (com.lester.uparent.schoolinfo.Schoolinfo.data) msg.obj;
					if(data!=null){
						setInfo(data);
					}
					break;
				case 404:
					Toast.makeText(getApplicationContext(), msg.obj.toString(), 0).show();
					break;
				}
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}
	};
	/**
	 * 获取校园信息
	 */
	private void getSchoolinfo() {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		lldialog=LodingDialog.DialogFactor(Schoolinfo.this, "正在获取数据", true);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "schoolService";
	            String method = "getByStudentId";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = 
	            		"{studentId:'"+Userinfo.getchildid(Schoolinfo.this)+
	            		"',loginId:'"+Userinfo.getmobile_phone(Schoolinfo.this)+
	            		"',token:'"+token+
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
	    				String jsonData = jsonObj.getString("data");
	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<com.lester.uparent.schoolinfo.Schoolinfo.data>(){}.getType());
	    				handler.sendMessage(handler.obtainMessage(Constants.GETSCHOLLINFO, object));
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
