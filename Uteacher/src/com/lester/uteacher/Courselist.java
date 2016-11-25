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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;

import com.baidu.mobstat.StatService;
import com.dream.framework.utils.dialog.LodingDialog;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.lester.company.http.HttpURL;
import com.lester.school.requst.Constants;
import com.lester.school.requst.JsonUtil;
import com.lester.uparent.announcement.Announcementdata;
import com.lester.uteacher.adapter.CourserlistAdapter;
import com.lester.uteacher.goton.Classlist;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.lester.uteacher.tool.Userhead;
import com.lester.uteacher.tool.Userinfo;
import com.teacher.data.CouserListData;

public class Courselist extends Activity implements OnClickListener,OnItemClickListener{
	public static Courselist activity;
	private LodingDialog lldialog;
	private Userhead userhead=new Userhead();
	private ListView listview;
	private CourserlistAdapter mAdapter;
	public ArrayList<CouserListData> couserListDatas;
	private List<Classlist.datas> classdatas;
	private String selectclassid;
	

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
		activity=this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.courselist);
		initview();
		intData();
		getclass();
	}
//刷新数据
	public void refresh(){
		couserListDatas=new ArrayList<CouserListData>();
		mAdapter.notifyDataSetChanged();
		lldialog=LodingDialog.DialogFactor(Courselist.this, "正在获取数据", false);
		getlistdata(selectclassid);
	}
	private void intData() {
		mAdapter=new CourserlistAdapter(Courselist.this);
		mAdapter.setData(couserListDatas);
		listview.setAdapter(mAdapter);
		listview.setOnItemClickListener(this);
	}

	private void initview() {
		listview=(ListView) findViewById(R.id.listview);
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.add).setOnClickListener(this);
		findViewById(R.id.selectclass).setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.add:
			intent =new Intent(Courselist.this,AddCourse.class);
			intent.putExtra("classId", selectclassid);
			startActivity(intent);
			break;
		case R.id.selectclass:
			initPopWindow(R.id.selectclass);
			break;
		}
	}
	private void initPopWindow(final int id) {
		View contentView_pop = LayoutInflater.from(getApplicationContext())
				.inflate(R.layout.pop_list, null);
		contentView_pop.setBackgroundResource(R.color.backhuise);
		final PopupWindow popupWindow = new PopupWindow(
				findViewById(R.id.fenleilayout), getWindowManager().getDefaultDisplay().getWidth()/3, LayoutParams.WRAP_CONTENT);
		popupWindow.setContentView(contentView_pop);
		final ListView mListView = (ListView) contentView_pop
				.findViewById(R.id.pop_listView);
		BaseAdapter adapter=new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				convertView=getLayoutInflater().inflate(R.layout.poplistitem, null);
				((TextView) convertView.findViewById(R.id.itemtext)).setText(classdatas.get(position).getName());
				return convertView;
			}
			@Override
			public long getItemId(int position) {
				return position;
			}
			@Override
			public Object getItem(int position) {
				return null;
			}
			@Override
			public int getCount() {
				if(classdatas!=null){
					return classdatas.size();
				}
				return 0;
			}
		};
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if(!selectclassid.equals(classdatas.get(arg2).getId())){//选择了另外一个班级，才进行数据刷新
					selectclassid=classdatas.get(arg2).getId();
					couserListDatas=new ArrayList<CouserListData>();
					mAdapter.setData(null);
					mAdapter.notifyDataSetChanged();
					lldialog=LodingDialog.DialogFactor(Courselist.this, "正在获取数据", false);
					getlistdata(selectclassid);
				}
				popupWindow.dismiss();
			}
		});
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(false);
		popupWindow.setBackgroundDrawable(new ColorDrawable());
		popupWindow.showAsDropDown(findViewById(id));
	}
	
	private void showAddbt(){
		findViewById(R.id.add).setVisibility(View.VISIBLE);
	}
	@SuppressLint("HandlerLeak") Handler handler =new Handler(){
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			try {
				if(lldialog!=null){
					lldialog.dismiss();
				}
			switch (msg.what) {
			case Constants.GETCLASS:
				classdatas=(List<Classlist.datas>) msg.obj;
				if(classdatas!=null && classdatas.size()!=0){
					if(classdatas.get(0).getId()!=null&& !classdatas.get(0).getId().equals("null")){
						selectclassid=classdatas.get(0).getId();
						getlistdata(classdatas.get(0).getId());
						showAddbt();
						findViewById(R.id.selectclass).setVisibility(View.VISIBLE);//可以选择班级
					}else{
						if(lldialog!=null){
							lldialog.dismiss();
						}
						findViewById(R.id.selectclass).setVisibility(View.GONE);
						Toast.makeText(getApplicationContext(), "您不是带班老师", 0).show();
					}
				}else{
					findViewById(R.id.selectclass).setVisibility(View.GONE);
					Toast.makeText(getApplicationContext(), "您不是带班老师", 0).show();
				}
				break;
			case Constants.GETLISTDATA://课表数据
				if(lldialog!=null){
					lldialog.dismiss();
				}
				ArrayList<CouserListData> datas=(ArrayList<CouserListData>) msg.obj;
				if(datas!=null && datas.size()!=0){
					couserListDatas=datas;
					mAdapter.setData(couserListDatas);
					mAdapter.notifyDataSetChanged();
				}else{
					Toast.makeText(getApplicationContext(), "还没有您的课表信息", 0).show();
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
	/**
	 * 获取班级列表
	 */
	private void getclass() {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		lldialog=LodingDialog.DialogFactor(Courselist.this, "正在获取数据", true);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "clazzService";
	            String method = "search";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = "{loginId:'"+Userinfo.getmobile_phone(Courselist.this)+"'}";
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
	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<List<Classlist.datas>>(){}.getType());
	    				handler.sendMessage(handler.obtainMessage(Constants.GETCLASS, object));
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
	private void getlistdata(final String classId) {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
//		lldialog=LodingDialog.DialogFactor(Courselist.this, "正在获取数据", true);
		new Thread(new Runnable() {
			public void run() {
				String service = "clazzScheduleService";
				String method = "searchByClazzId";
				String token = MD5.MD5(service+method+Constants.Key);
				String params = "{clazzId:'"+classId+"'}";
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
						Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<ArrayList<CouserListData>>(){}.getType());
						handler.sendMessage(handler.obtainMessage(Constants.GETLISTDATA, object));
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
	/*private void getteacherinfo() {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		lldialog=LodingDialog.DialogFactor(Courselist.this, "正在获取数据", true);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "teacherService";
	            String method = "getByLoginId";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = "{loginId:'"+Userinfo.getmobile_phone(Courselist.this)+"'}";
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
	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<Teacherinfo.data>(){}.getType());
	    				handler.sendMessage(handler.obtainMessage(Constants.GETTEACHERINFO, object));
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
	}*/

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		StartToAddcourserActivity(arg2);
	}

	public void StartToAddcourserActivity(int position){
		Intent intent=new Intent(Courselist.this,AddCourse.class);
		intent.putExtra("id", couserListDatas.get(position).getId());
		intent.putExtra("show", true);
		startActivity(intent);
	}
}
