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

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.dream.framework.utils.dialog.LodingDialog;
import com.google.gson.reflect.TypeToken;
import com.lester.company.http.HttpURL;
import com.lester.headview.CustomImageView;
import com.lester.school.requst.Constants;
import com.lester.school.requst.JsonUtil;
import com.lester.uteacher.code.Codedata;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.Loadhead;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.lester.uteacher.tool.Usercode;
import com.lester.uteacher.tool.Userinfo;

public class CodeDetail extends Activity implements OnClickListener{
	private String code;
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
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	 requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.codedetail);
	code=getIntent().getStringExtra("result");
	initview();
	getchildinfo();
}
private void initview() {
	// TODO Auto-generated method stub
	findViewById(R.id.back).setOnClickListener(this);
}
private void getchildinfo() {
	if(Net.isNetworkAvailable(getApplicationContext())){
		return;
	}
	lldialog=LodingDialog.DialogFactor(CodeDetail.this, "正在获取数据", true);
	new Thread(new Runnable() {
        public void run() {
            String service = "outSchoolService";
            String method = "readQRCode";
            String token = MD5.MD5(service+method+Constants.Key);
            String params = "{loginId:'"+Userinfo.getmobile_phone(CodeDetail.this)+"',"
            		+ "content:'"+code+"'}";
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
    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<Codedata.data>(){}.getType());
    				handler.sendMessage(handler.obtainMessage(Constants.CODEDETAIL, object));
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
private class parentmakesure implements OnClickListener{
	private String studentId;
	private String parentId;
	parentmakesure( String studentId, String parentId){
		this.studentId=studentId;
		this.parentId=parentId;
	}
	
	@Override
	public void onClick(View v) {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		lldialog=LodingDialog.DialogFactor(CodeDetail.this, "正在确认", true);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "outSchoolService";
	            String method = "leaveWithParent";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = "{loginId:'"+Userinfo.getmobile_phone(CodeDetail.this)+"',"
	            		+ "studentId:'"+studentId+"',"
	            		+"parentId:'"+parentId+"'}";
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
//	    				String jsonData = jsonObj.getString("data");
//	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<Codedata.data>(){}.getType());
	    				handler.sendMessage(handler.obtainMessage(Constants.MAKESURE, "确认成功"));
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
	
	private class agentmakesure implements OnClickListener{
		private String agentOrderId;
		agentmakesure( String agentOrderId){
			this.agentOrderId=agentOrderId;
		}
		
		@Override
		public void onClick(View v) {
			if(Net.isNetworkAvailable(getApplicationContext())){
				return;
			}
			lldialog=LodingDialog.DialogFactor(CodeDetail.this, "正在确认", true);
			new Thread(new Runnable() {
		        public void run() {
		            String service = "outSchoolService";
		            String method = "leaveWithAgent";
		            String token = MD5.MD5(service+method+Constants.Key);
		            String params = "{loginId:'"+Userinfo.getmobile_phone(CodeDetail.this)+"',"
		            		+"agentOrderId:'"+agentOrderId+"'}";
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
//		    				String jsonData = jsonObj.getString("data");
//		    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<Codedata.data>(){}.getType());
		    				handler.sendMessage(handler.obtainMessage(Constants.MAKESURE, "确认成功"));
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

	
Handler handler=new Handler(){
	public void handleMessage(android.os.Message msg) {
		try {
		
			if(lldialog!=null){
				lldialog.dismiss();
		}
		switch (msg.what) {
		case Constants.CODEDETAIL:
			Codedata.data getdata=(Codedata.data) msg.obj;
			CustomImageView imageView=((CustomImageView)findViewById(R.id.childhead));
			 if(getdata.getStudentPhoto()!=null &&
					 (!getdata.getStudentPhoto().equals(""))){
				new Loadhead(CodeDetail.this,getdata.getStudentPhoto(), imageView).execute();
			 }
			Texttool.setText(CodeDetail.this, R.id.childname, getdata.getStudentName());
			Texttool.setText(CodeDetail.this, R.id.studentClass, getdata.getStudentClass());
			Texttool.setText(CodeDetail.this, R.id.studentIdCard, getdata.getStudentIdCard());
			if(getdata.getStudentGender().equals("F")){
				((ImageView)findViewById(R.id.childsex)).setImageResource(R.drawable.nv);
			}else{
				((ImageView)findViewById(R.id.childsex)).setImageResource(R.drawable.nan);
			}
			if(getdata.getOutModel().equals("4")){//委托
				findViewById(R.id.parent).setVisibility(View.GONE);
				findViewById(R.id.agent).setVisibility(View.VISIBLE);
				Texttool.setText(CodeDetail.this, R.id.bartitle, "委托接送");
				Texttool.setText(CodeDetail.this, R.id.parentname, getdata.getParentName());
				Texttool.setText(CodeDetail.this, R.id.agentName, getdata.getAgentName());
				Texttool.setText(CodeDetail.this, R.id.agentIdCard, getdata.getAgentIdCard());
				Texttool.setText(CodeDetail.this, R.id.agentPhone, getdata.getAgentPhone());
				if( !getdata.getPosition_status().equals("3")){
					findViewById(R.id.makesure).setOnClickListener(new agentmakesure(getdata.getAgentOrderId()));
				}else{
					final String studentname=getdata.getStudentName();
					findViewById(R.id.makesure).setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Toast.makeText(getApplicationContext(),studentname+"已经被接走", 0).show();
						}
					});
					
				}
			}else if(getdata.getOutModel().equals("1")){//家长
				findViewById(R.id.agent).setVisibility(View.GONE);
				findViewById(R.id.parent).setVisibility(View.VISIBLE);
				Texttool.setText(CodeDetail.this, R.id.bartitle, "家长接送");
				CustomImageView imageView1=((CustomImageView)findViewById(R.id.parenthead));
				 if(getdata.getParentPhoto()!=null &&
						 (!getdata.getParentPhoto().equals(""))){
					new Loadhead(CodeDetail.this,getdata.getParentPhoto(), imageView1).execute();
				 }
				if(getdata.getParentGender().equals("F")){
					((ImageView)findViewById(R.id.gridsex)).setImageResource(R.drawable.nv);
				}else{
					((ImageView)findViewById(R.id.gridsex)).setImageResource(R.drawable.nan);
				}
				Texttool.setText(CodeDetail.this, R.id.parentname1, getdata.getParentName());
				Texttool.setText(CodeDetail.this, R.id.parentphone, getdata.getParentPhone());
				Texttool.setText(CodeDetail.this, R.id.parentcardid, getdata.getParentIdCard());
				if(!getdata.getPosition_status().equals("3")){
					findViewById(R.id.makesure).setOnClickListener(new parentmakesure(getdata.getStudentId(),getdata.getParentId()));
				}else{
					final String studentname=getdata.getStudentName();
					findViewById(R.id.makesure).setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Toast.makeText(getApplicationContext(),studentname+"已经被接走", 0).show();
						}
					});
				}
			}
			break;
		case Constants.MAKESURE:
			Toast.makeText(getApplicationContext(), msg.obj.toString(), 0).show();
			finish();
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



@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	switch (v.getId()) {
	case R.id.back://确认按钮
		finish();
		break;
		}
	}
}
