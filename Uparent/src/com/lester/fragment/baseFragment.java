package com.lester.fragment;

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

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.dream.framework.utils.dialog.LodingDialog;
import com.lester.company.http.HttpURL;
import com.lester.school.requst.Constants;
import com.lester.uparent.Announcement;
import com.lester.uparent.Announcement_Class;
import com.lester.uparent.Authorize;
import com.lester.uparent.Childinfo;
import com.lester.uparent.Courselist;
import com.lester.uparent.Feedback_History;
import com.lester.uparent.GenerateCode;
import com.lester.uparent.LeaveList;
import com.lester.uparent.Me;
import com.lester.uparent.My_message;
import com.lester.uparent.ParentManage;
import com.lester.uparent.R;
import com.lester.uparent.RecipesList;
import com.lester.uparent.Schoolbus;
import com.lester.uparent.Schoolinfo;
import com.lester.uparent.SendLog;
import com.lester.uparent.Teacherlist;
import com.lester.uteacher.adapter.HomeGridAdapter;
import com.lester.uteacher.tool.Fixpic;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.lester.uteacher.tool.Usercode;
import com.lester.uteacher.tool.Userinfo;
import com.zxing.encoding.EncodingHandler;

public class baseFragment extends Fragment{

	private LodingDialog lldialog;
	private HomeGridAdapter gridadapter;
	private View view;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		view=getActivity().getLayoutInflater().inflate(R.layout.basefragment, null);
		indata();
		initview();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return view;
	}

	private void indata() {
		ArrayList<Integer> imgid=new ArrayList<Integer>();
		ArrayList<String> itemname=new ArrayList<String>();
		imgid.add(R.drawable.homeitem1);
		imgid.add(R.drawable.homeitem2);
		imgid.add(R.drawable.homeitem3);
		imgid.add(R.drawable.homeitem4);
		imgid.add(R.drawable.homeitem5);
		imgid.add(R.drawable.homeitem6);
		imgid.add(R.drawable.homeitem7);
		imgid.add(R.drawable.homeitem8);
		imgid.add(R.drawable.homeitem9);
		imgid.add(R.drawable.homeitem10);
		itemname.add("扫码接送");
		itemname.add("委托接送");
		itemname.add("校车接送");
		itemname.add("病事假单");
		itemname.add("校园公告");
		itemname.add("班级公告");
		itemname.add("每周课程");
		itemname.add("每周食谱");
		itemname.add("提示信息");
		itemname.add("接送记录");
		gridadapter=new HomeGridAdapter(getActivity(),imgid,itemname,getActivity().getWindowManager());
	}
	private void initview() {
		
		ImageView img=(ImageView) view.findViewById(R.id.homeimg);
		Fixpic fix=new Fixpic();
		fix.setView_W_H(getActivity().getWindowManager(),img,
				BitmapFactory.decodeResource(
						 getActivity().getResources(), R.drawable.u117));
	
		
		GridView gridview=(GridView) view.findViewById(R.id.gripview);
		gridview.setAdapter(gridadapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				switch (arg2) {
				case 0:
					
					isSubParent1();
					
					break;
				case 1://班级公告
					
					intent.setClass(getActivity(), Authorize.class);
					startActivity(intent);
					break;
				case 2:
					intent.setClass(getActivity(), Schoolbus.class);
					startActivity(intent);
					break;
				case 3:
					startToLeaveListActivity();
					
					break;
				case 4:
					
					intent.setClass(getActivity(), Announcement.class);
					startActivity(intent);
					break;
				case 5:
					intent.setClass(getActivity(), Announcement_Class.class);
					startActivity(intent);
					break;
				case 6:
					
					intent.setClass(getActivity(), Courselist.class);
					startActivity(intent);
					
					break;
				case 7:
					
					intent.setClass(getActivity(), RecipesList.class);
					startActivity(intent);
					
					
					break;
				case 8:
					intent.setClass(getActivity(), My_message.class);
					startActivity(intent);
					break;
				case 9:
					
					intent.setClass(getActivity(), SendLog.class);
					startActivity(intent);

					break;
				}
			}
		});
	}
	
	private void startToLeaveListActivity() {
		// TODO Auto-generated method stub
		Intent intent=new Intent();
		intent.setClass(getActivity(), LeaveList.class);
		startActivity(intent);
	}
	//验证是否有权限
		public void isSubParent() {
			if(Net.isNetworkAvailable(getActivity())){
				return;
			}
			lldialog=LodingDialog.DialogFactor(getActivity(), "正在检查权限", false);
			new Thread(new Runnable() {
		        public void run() {
		            String service = "subParentService";
		            String method = "isSubParent";
		            String token = MD5.MD5(service+method+Constants.Key);
		            String params = 
		            		"{studentId:'"+Userinfo.getchildid(getActivity())+
		            		"',parentId:'"+Userinfo.getid(getActivity())+
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
		    				final Boolean isReceive=jsonObject.getBoolean("isReceive");
		    				getActivity().runOnUiThread(new Runnable() {
								public void run() {
									if(lldialog!=null){
										lldialog.dismiss();
									}
									if(isReceive){
										startToLeaveListActivity();
									}else{
										Toast.makeText(getActivity(), "您没有委托权限", 0).show();
//										finish();
									}
								}
							});
//		    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<ArrayList<ParentData>>(){}.getType());
//		    				handler.sendMessage(handler.obtainMessage(Constants.GETPANRENTLIST, object));
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
		public void isSubParent1() {
			if(Net.isNetworkAvailable(getActivity())){
				return;
			}
			lldialog=LodingDialog.DialogFactor(getActivity(), "正在检查权限", false);
			new Thread(new Runnable() {
				public void run() {
					String service = "subParentService";
					String method = "isSubParent";
					String token = MD5.MD5(service+method+Constants.Key);
					String params = 
							"{studentId:'"+Userinfo.getchildid(getActivity())+
							"',parentId:'"+Userinfo.getid(getActivity())+
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
							final Boolean isReceive=jsonObject.getBoolean("isReceive");
							getActivity().runOnUiThread(new Runnable() {
								public void run() {
									if(lldialog!=null){
										lldialog.dismiss();
									}
									if(isReceive){
										Intent intent=new Intent();
										intent.setClass(getActivity(), GenerateCode.class);
										startActivity(intent);
									}else{
										Toast.makeText(getActivity(), "您没有接送权限", 0).show();
//										finish();
									}
								}
							});
//		    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<ArrayList<ParentData>>(){}.getType());
//		    				handler.sendMessage(handler.obtainMessage(Constants.GETPANRENTLIST, object));
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
		Handler handler=new Handler(){
			public void handleMessage(android.os.Message msg) {
				try {
					if(lldialog!=null){
						lldialog.dismiss();
				}
				switch (msg.what) {

				case 404:
					Toast.makeText(getActivity(), msg.obj.toString(), 0).show();
					break;
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			};
		};
}
