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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.dream.framework.utils.dialog.LodingDialog;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lester.company.http.HttpURL;
import com.lester.headview.CustomImageView;
import com.lester.school.loader.ImageLoader;
import com.lester.school.requst.Constants;
import com.lester.school.requst.JsonUtil;
import com.lester.uteacher.goton.Classlist;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.lester.uteacher.tool.Userinfo;
import com.teacher.View.CircleImageView;
import com.teacher.data.StudentData_feedback;

public class Feedback_selectChild extends Activity implements OnClickListener{

	private BaseAdapter mAdapter;
	private PullToRefreshListView RefreshListView ;
	private LodingDialog lldialog;
	private List<Classlist.datas> classdatas;
	private String selectclassid;
	private ArrayList<StudentData_feedback> data_feedbacks;
	public static Feedback_selectChild activity;
	

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
		setContentView(R.layout.feedback_selectchild);
		initview();
		setAdapter();
		getclass();
	}
	
	private void initview() {
		findViewById(R.id.back).setOnClickListener(this);
		RefreshListView=(PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		findViewById(R.id.selectclass).setOnClickListener(this);
	}
	
	private void setAdapter() {
		final ImageLoader imageLoader=new ImageLoader(getApplicationContext());
		mAdapter=new BaseAdapter() {
			@SuppressLint("ResourceAsColor")
			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				if(convertView==null){
					convertView=getLayoutInflater().inflate(R.layout.feed_selectchild_item, null);
				}
				Texttool.setText(convertView, R.id.name, data_feedbacks.get(position).getName());
				if(data_feedbacks.get(position).getHasFeedback()){
					Texttool.setText(convertView, R.id.hasFeedback, "已反馈");
					((TextView)convertView.findViewById(R.id.hasFeedback)).setTextColor(getResources().getColor(R.color.blue));
				}else{
					Texttool.setText(convertView, R.id.hasFeedback, "未反馈");
					((TextView)convertView.findViewById(R.id.hasFeedback)).setTextColor(getResources().getColor(R.color.huise));
				}
				ImageView sex=(ImageView) convertView.findViewById(R.id.sex);
				if(data_feedbacks.get(position).getGender().equals("M")){
					sex.setImageResource(R.drawable.nan);
				}else{
					sex.setImageResource(R.drawable.nv);
				}
				
				String url=data_feedbacks.get(position).getPhoto();	
				CircleImageView head=(CircleImageView) convertView.findViewById(R.id.head);
				if(url!=null){
					imageLoader.DisplayImage(url, head);
				}else{
					head.setImageResource(R.drawable.u122);
				}
				convertView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						
						if(data_feedbacks.get(position).getHasFeedback()){
							Toast.makeText(getApplicationContext(), "无需再次反馈", 0).show();
						}else{
							String classname="";
							for(Classlist.datas temp:classdatas){
								if(temp.getId().equals(selectclassid)){
									classname=temp.getName();
									break;
								}
							}
							Intent intent =new Intent(Feedback_selectChild.this,Feedback.class);
							intent.putExtra("show", false);
							intent.putExtra("studentId", data_feedbacks.get(position).getId());
							intent.putExtra("studentname", data_feedbacks.get(position).getName());
							intent.putExtra("classname", classname);
							startActivity(intent);
						}
					}
				});
				convertView.findViewById(R.id.check).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent =new Intent(Feedback_selectChild.this,Feedback_History.class);
						intent.putExtra("studentId", data_feedbacks.get(position).getId());
						startActivity(intent);
					}
				});
				return convertView;
			}
			@Override
			public long getItemId(int position) {
				return 0;
			}
			@Override
			public Object getItem(int position) {
				return null;
			}
			@Override
			public int getCount() {
				if(data_feedbacks!=null){
					return data_feedbacks.size();
				}
				return 0;
			}
		};
		RefreshListView.setAdapter(mAdapter);
		RefreshListView.setMode(Mode.DISABLED);
	}
	
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.back:
			finish();
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
					data_feedbacks= new ArrayList<StudentData_feedback>();
					mAdapter.notifyDataSetChanged();
					lldialog=LodingDialog.DialogFactor(Feedback_selectChild.this, "正在获取数据", false);
					getstudent();
				}
				popupWindow.dismiss();
			}
		});
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(false);
		popupWindow.setBackgroundDrawable(new ColorDrawable());
		popupWindow.showAsDropDown(findViewById(id));
	}
	//更新数据
	public void updata(String id){
		/*for(int i=0;i<data_feedbacks.size();i++){
			if(id.equals(data_feedbacks.get(i).getId())){
				data_feedbacks.get(i).setHasFeedback(true);
				break;
			}
		}
		mAdapter.notifyDataSetChanged();*/
		data_feedbacks= new ArrayList<StudentData_feedback>();
		mAdapter.notifyDataSetChanged();
		lldialog=LodingDialog.DialogFactor(Feedback_selectChild.this, "正在获取数据", false);
		getstudent();
	}
	Handler handler =new Handler(){
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			try {
			switch (msg.what) {
			case Constants.GETCLASS:
				classdatas=(List<Classlist.datas>) msg.obj;
				if(classdatas!=null && classdatas.size()!=0){
					if(classdatas.get(0).getId()!=null&& !classdatas.get(0).getId().equals("null")){
						selectclassid=classdatas.get(0).getId();
						getstudent();
						findViewById(R.id.selectclass).setVisibility(View.VISIBLE);//可以选择班级
					}else{
						if(lldialog!=null){
							lldialog.dismiss();
						}
						findViewById(R.id.selectclass).setVisibility(View.GONE);
						Toast.makeText(getApplicationContext(), "您不是带班老师", 0).show();
					}
				}else{
					if(lldialog!=null){
						lldialog.dismiss();
					}
					findViewById(R.id.selectclass).setVisibility(View.GONE);
					Toast.makeText(getApplicationContext(), "您不是带班老师", 0).show();
				}
				break;
			case Constants.GETLISTDATA_STUDENT://学生数据
				if(lldialog!=null){
					lldialog.dismiss();
				}
				data_feedbacks=(ArrayList<StudentData_feedback>) msg.obj;
				mAdapter.notifyDataSetChanged();
				break;
			case 404:
				if(lldialog!=null){
					lldialog.dismiss();
				}
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
		lldialog=LodingDialog.DialogFactor(Feedback_selectChild.this, "正在获取数据", true);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "clazzService";
	            String method = "search";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = "{loginId:'"+Userinfo.getmobile_phone(Feedback_selectChild.this)+"'}";
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
	                HttpPost httpPost = new HttpPost(HttpURL.URL);
	                httpPost.setEntity(requestHttpEntity);
	                HttpClient httpClient = new DefaultHttpClient();
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
	            } catch (Exception e) {
	                e.printStackTrace();
	            } finally {

	            }
	        }
	    }).start();
	}
	private void getstudent() {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
//		lldialog=LodingDialog.DialogFactor(Feedback_selectChild.this, "正在获取数据", true);
		new Thread(new Runnable() {
			public void run() {
				String service = "feedbackEverydayService";
				String method = "searchStudentByClazzId";
				String token = MD5.MD5(service+method+Constants.Key);
				String params = "{clazzId:'"+selectclassid+"'}";
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
						Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<ArrayList<StudentData_feedback>>(){}.getType());
						handler.sendMessage(handler.obtainMessage(Constants.GETLISTDATA_STUDENT, object));
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
