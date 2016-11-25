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
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.dream.framework.utils.dialog.LodingDialog;
import com.google.gson.reflect.TypeToken;
import com.lester.company.http.HttpURL;
import com.lester.school.requst.Constants;
import com.lester.school.requst.JsonUtil;
import com.lester.slidecutlistview.CustomSwipeListView;
import com.lester.uteacher.adapter.SlidListviewAdapter;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.lester.uteacher.tool.Userinfo;
import com.teacher.data.ParentData;

public class ParentManage extends Activity implements OnClickListener,OnItemClickListener{
	private SlidListviewAdapter adapter;
	private LodingDialog lldialog;
	private ArrayList<ParentData> data;
	public static ParentManage manage;
	public int selectposition;
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
		manage=this;
		setContentView(R.layout.goton);
		initview();
//		isSubParent();
		getparentlist();
	}

	private void initview() {
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.add).setOnClickListener(this);
		
		
		CustomSwipeListView slideListView = (CustomSwipeListView) findViewById(R.id.slideCutListView);
		adapter=new SlidListviewAdapter(ParentManage.this);
		slideListView.setAdapter(adapter);
		slideListView.setOnItemClickListener(this);
		slideListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int position, long arg3) {
				
				final Dialog dialog = new Dialog(ParentManage.this, R.style.MyDialog);   
				dialog.setCanceledOnTouchOutside(true);
				View view=(View)getLayoutInflater().inflate(R.layout.dialogmakesure, null);
				Texttool.setText(view, R.id.title, "删除此联系人");
				view.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						selectposition=position;
						delete(data.get(position).getpId());
						dialog.dismiss();
					}
				});
				view.findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
					dialog.setContentView(view);
					dialog.show();
				return true;
			}
		});
	}

	public void refresh(){
		data=new ArrayList<ParentData>();
		adapter.setData(null);
		adapter.notifyDataSetChanged();
		getparentlist();
	}
	@Override
	public void onClick(View v) {
		Intent intent ;
	switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.add:
			intent=new Intent(ParentManage.this,AddConstact.class);
			intent.putExtra("show",false);
			startActivityForResult(intent, 101);
			break;
		}
	}
	
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			try {
				if(lldialog!=null){
					lldialog.dismiss();
				}
			switch (msg.what) {
			case Constants.GETPANRENTLIST:
				data =(ArrayList<ParentData>) msg.obj;
				if(data!=null){
					adapter.setData(data);
					adapter.notifyDataSetChanged();
				}else{
					Toast.makeText(getApplicationContext(), "还没有从家长", 0).show();
				}
				break;
			case Constants.DELETESUBPARENT:
				data.remove(selectposition);
				adapter.setData(data);
				adapter.notifyDataSetChanged();
				break;
			case 404:
				Toast.makeText(getApplicationContext(), msg.obj.toString(), 0).show();
				break;
			}
			} catch (Exception e) {
			}
		}
	};
	//验证是否有权限
	/*public void isSubParent() {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		lldialog=LodingDialog.DialogFactor(ParentManage.this, "正在检查权限", false);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "subParentService";
	            String method = "isSubParent";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = 
	            		"{studentId:'"+Userinfo.getchildid(ParentManage.this)+
	            		"',parentId:'"+Userinfo.getid(ParentManage.this)+
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
	    				JSONObject jsonObject = new JSONObject(jsonData);
	    				final Boolean mainAgent=jsonObject.getBoolean("mainAgent");
	    				runOnUiThread(new Runnable() {
							public void run() {
								if(lldialog!=null){
									lldialog.dismiss();
								}
								if(mainAgent){
									getparentlist();
			    				}else{
			    					Toast.makeText(getApplicationContext(), "您不是主家长，没有管理权限", 0).show();
			    				}
							}
						});
//	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<ArrayList<ParentData>>(){}.getType());
//	    				handler.sendMessage(handler.obtainMessage(Constants.GETPANRENTLIST, object));
	        		}else{
	    				handler.sendMessage(handler.obtainMessage(404, jsonObj.getString("message")));
	    			}
	            } catch (Exception e) {
	                e.printStackTrace();
	            } finally {

	            }
	        }
	    }).start();
	}*/
	public void getparentlist() {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		lldialog=LodingDialog.DialogFactor(ParentManage.this, "正在获取数据", false);
		new Thread(new Runnable() {
			public void run() {
				String service = "subParentService";
				String method = "searchByStudentId";
				String token = MD5.MD5(service+method+Constants.Key);
				String params = 
						"{studentId:'"+Userinfo.getchildid(ParentManage.this)+
						"',mainParentId:'"+Userinfo.getid(ParentManage.this)+
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
						String jsonData = jsonObj.getString("datas");
						Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<ArrayList<ParentData>>(){}.getType());
						handler.sendMessage(handler.obtainMessage(Constants.GETPANRENTLIST, object));
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
	public void delete(final String subParentId) {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		lldialog=LodingDialog.DialogFactor(ParentManage.this, "正在删除", false);
		new Thread(new Runnable() {
			public void run() {
				String service = "subParentService";
				String method = "delete";
				String token = MD5.MD5(service+method+Constants.Key);
				String params = 
						"{studentId:'"+Userinfo.getchildid(ParentManage.this)+
						"',subParentId:'"+subParentId+
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
//						String jsonData = jsonObj.getString("datas");
//						Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<ArrayList<ParentData>>(){}.getType());
						handler.sendMessage(handler.obtainMessage(Constants.DELETESUBPARENT, ""));
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent =new Intent(ParentManage.this,AddConstact.class);
		intent.putExtra("show",true);
		intent.putExtra("data",data.get(arg2));
		startActivity(intent);
	}
}
