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
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.lester.uteacher.tool.Userinfo;
import com.teacher.data.ParentData;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class AddConstact  extends Activity implements OnClickListener{
   
	private ArrayList<CheckBox> relation;
	private ArrayList<CheckBox> haveRight;
	private LodingDialog lldialog;
	private Boolean show;
	private String subParentId;
	private Boolean changeisReceive;
	private int changerelationship;
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
		setContentView(R.layout.addcontact);
		getwhat();
		initview();
		setData();
	}

	private void getwhat() {
		show=getIntent().getBooleanExtra("show", false);
	}

	private void setData() {
		if(show){//修改
			Texttool.setText(AddConstact.this, R.id.bartitle,"修改");
			findViewById(R.id.subparentname).setFocusable(false);
			findViewById(R.id.subtel).setFocusable(false);
			findViewById(R.id.subcard).setFocusable(false);
			ParentData data=(ParentData) getIntent().getSerializableExtra("data");
			
			subParentId=data.getpId();
			
		
			Texttool.setText(AddConstact.this, R.id.subparentname, data.getSubParentName());
			Texttool.setText(AddConstact.this, R.id.subtel,data.getMobilePhone());
			Texttool.setText(AddConstact.this, R.id.subcard, data.getIdCard());
	
			if(data.getIsReceive()){
				haveRight.get(0).setChecked(true);
			}else{
				haveRight.get(1).setChecked(true);
			}
			int relationship=data.getRelationship();
			switch (relationship) {
			case 1:
				relation.get(0).setChecked(true);
				break;
			case 2:
				relation.get(1).setChecked(true);
				break;
			case 3:
				relation.get(2).setChecked(true);
				break;
			}
		}else{
			Texttool.setText(AddConstact.this, R.id.bartitle,"添加");
		}
		Texttool.setText(AddConstact.this, R.id.childname, Userinfo.getchildname(AddConstact.this));
		Texttool.setText(AddConstact.this, R.id.parentname, Userinfo.getname(AddConstact.this));
	}

	private void initview() {
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.save).setOnClickListener(this);
		
		relation=new ArrayList<CheckBox>();
		relation.add((CheckBox) findViewById(R.id.check2));
		relation.add((CheckBox) findViewById(R.id.check0));
		relation.add((CheckBox) findViewById(R.id.check1));
		for(int i=0;i<3;i++){
			relation.get(i).setOnCheckedChangeListener(new checklistener0(i));
		}
		haveRight=new ArrayList<CheckBox>();
		haveRight.add((CheckBox) findViewById(R.id.check3));
		haveRight.add((CheckBox) findViewById(R.id.check4));
		for(int i=0;i<2;i++){
			haveRight.get(i).setOnCheckedChangeListener(new checklistener1(i));
		}
	}

	public class checklistener0 implements OnCheckedChangeListener {
		int i;
		public checklistener0(int i) {
			this.i=i;
		}
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			for(CheckBox checkBox:relation){
				checkBox.setChecked(false);
			}
			relation.get(i).setChecked(isChecked);
		}
		
	}
	public class checklistener1 implements OnCheckedChangeListener {
		int i;
		public checklistener1(int i) {
			this.i=i;
		}
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			for(CheckBox checkBox:haveRight){
				checkBox.setChecked(false);
			}
			haveRight.get(i).setChecked(isChecked);
		}
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.back:
					finish();
				break;
			case R.id.save:
				if(show){
					mychange();
				}else{
					mysave();
				}
				
				break;
		}
	}

	private void mychange() {
		if(cansave()){
			boolean isReceive=false;
			int is = 0;
			for(int i=0;i<2;i++){
				if(haveRight.get(i).isChecked()){
					is=i;
					break;
				}
			}
			if(is==0){
				isReceive=true;
				changeisReceive=isReceive;
			}
			int  relationship=0;
			for(int i=0;i<3;i++){
				if(relation.get(i).isChecked()){
					relationship=i+1;
					changerelationship=relationship;
					break;
				}
			}
			try {
				JSONObject jsonObject=new JSONObject();
				jsonObject.put("studentId", "'"+Userinfo.getchildid(AddConstact.this)+"'");
				jsonObject.put("subParentId", "'"+subParentId+"'");
				jsonObject.put("schoolId", "'"+Userinfo.getchildschoolid(AddConstact.this)+"'");
				jsonObject.put("isReceive", "'"+isReceive+"'");
				jsonObject.put("relationship", "'"+relationship+"'");
				changeparentinfo(jsonObject.toString().replaceAll("\"", ""));
			} catch (Exception e) {
				
			}
		}
	}

	private void mysave() {
		if(cansave()){
			String subParentName=Texttool.Gettext(AddConstact.this, R.id.subparentname);
			String subParentMobilePhone=Texttool.Gettext(AddConstact.this, R.id.subtel);
			String subParentIdCard=Texttool.Gettext(AddConstact.this, R.id.subcard);
			boolean isReceive=false;
			int is = 0;
			for(int i=0;i<2;i++){
				if(haveRight.get(i).isChecked()){
					is=i;
					break;
				}
			}
			if(is==0){
				isReceive=true;
			}
			int  relationship=0;
			for(int i=0;i<3;i++){
				if(relation.get(i).isChecked()){
					relationship=i+1;
					break;
				}
			}
			
			try {
				JSONObject jsonObject=new JSONObject();
				jsonObject.put("studentId", "'"+Userinfo.getchildid(AddConstact.this)+"'");
				jsonObject.put("parentId", "'"+Userinfo.getid(AddConstact.this)+"'");
				jsonObject.put("schoolId", "'"+Userinfo.getchildschoolid(AddConstact.this)+"'");
				jsonObject.put("subParentName", "'"+subParentName+"'");
				jsonObject.put("subParentMobilePhone", "'"+subParentMobilePhone+"'");
				jsonObject.put("isReceive", "'"+isReceive+"'");
				jsonObject.put("subParentIdCard", "'"+subParentIdCard+"'");
				jsonObject.put("relationship", "'"+relationship+"'");
				saveparent(jsonObject.toString().replaceAll("\"", ""));
			} catch (Exception e) {
				
			}
		}
	}

	private boolean cansave() {
		if(!Texttool.Havecontent(AddConstact.this, R.id.subparentname)){
			Toast.makeText(getApplicationContext(), "请输入从家长姓名", 0).show();
			return false;
		}
		if(Texttool.Gettext(AddConstact.this, R.id.subparentname).length()>40){
			Toast.makeText(getApplicationContext(), "家长姓名长度不能超过40个字", 0).show();
			return false;
		}
		if(!Texttool.Havecontent(AddConstact.this, R.id.subtel)){
			Toast.makeText(getApplicationContext(), "请输入电话号码", 0).show();
			return false;
		}
		if(!Texttool.Havecontent(AddConstact.this, R.id.subcard)){
			Toast.makeText(getApplicationContext(), "请输入身份证号", 0).show();
			return false;
		}
		EditText et=(EditText) findViewById(R.id.subtel);
		if(!Texttool.Pattern_phone(Texttool.Gettext(et))){
			Toast.makeText(getApplicationContext(), "请输入正确的电话号码", 0).show();
			return false;
		}
		if(!show){//尽在添加新的联系人的时候验证身份证号
			EditText card=(EditText) findViewById(R.id.subcard);
			if(!Texttool.Patternidcard(Texttool.Gettext(card))){
				Toast.makeText(getApplicationContext(), "请输入正确的身份证号码", 0).show();
				return false;
			}
		}
		
		boolean b=false;
		for(int i=0;i<3;i++){
			if(relation.get(i).isChecked()){
				b=true;
				break;
			}
		}
		if(!b){
			Toast.makeText(getApplicationContext(), "请选择家长与孩子的关系", 0).show();
			return false;
		}
		boolean c=false;
		for(int i=0;i<2;i++){
			if(haveRight.get(i).isChecked()){
				c=true;
			}
		}
		if(!c){
			Toast.makeText(getApplicationContext(), "请选择家长是否有权接送孩子", 0).show();
			return false;
		}
		return true;
	}
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			try {
				if(lldialog!=null){
					lldialog.dismiss();
				}
			switch (msg.what) {
			case Constants.SAVESUBPARENTDATA:
				Toast.makeText(getApplicationContext(), "保存成功", 0).show();
				ParentManage.manage.getparentlist();
				finish();
				break;
			case Constants.CHANGESUNPARENTINFO:
				Toast.makeText(getApplicationContext(), "修改成功", 0).show();
				ParentManage.manage.refresh();
				finish();
				break;
			case 404:
				Toast.makeText(getApplicationContext(), msg.obj.toString(), 0).show();
				break;
			case 406:
				finish();
				Toast.makeText(getApplicationContext(), msg.obj.toString(), 0).show();
				break;
			}
			} catch (Exception e) {
			}
		}
	};
	private void saveparent(final String data) {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		lldialog=LodingDialog.DialogFactor(AddConstact.this, "正在保存数据", false);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "subParentService";
	            String method = "save";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = data;
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
//	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<ArrayList<ParentData>>(){}.getType());
	    				handler.sendMessage(handler.obtainMessage(Constants.SAVESUBPARENTDATA, ""));
	        		}else{
	    				handler.sendMessage(handler.obtainMessage(404, jsonObj.getString("message")));
	    			}
	            } catch (Exception e) {
	                e.printStackTrace();
	            } finally {

	            }
	        }
	    }).start();
	}
	private void changeparentinfo(final String data) {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		lldialog=LodingDialog.DialogFactor(AddConstact.this, "正在保存数据", false);
		new Thread(new Runnable() {
			public void run() {
				String service = "subParentService";
				String method = "update";
				String token = MD5.MD5(service+method+Constants.Key);
				String params = data;
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
//	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<ArrayList<ParentData>>(){}.getType());
						handler.sendMessage(handler.obtainMessage(Constants.CHANGESUNPARENTINFO, ""));
					}else{
						handler.sendMessage(handler.obtainMessage(406, jsonObj.getString("message")));
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					
				}
			}
		}).start();
	}
}
