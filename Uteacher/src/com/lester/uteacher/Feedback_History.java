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
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lester.company.http.HttpURL;
import com.lester.school.requst.Constants;
import com.lester.school.requst.JsonUtil;
import com.lester.uteacher.goton.Classlist;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.teacher.data.Feedback_list;
import com.teacher.data.StudentData_feedback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Toast;

public class Feedback_History extends Activity implements OnClickListener,OnItemClickListener{
	
	private BaseAdapter mAdapter;
	private PullToRefreshListView RefreshListView ;
	private ArrayList<Feedback_list> data;
	private LodingDialog lldialog;
	private String studentId;

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
		studentId=getIntent().getStringExtra("studentId");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.feedback_history);
		initview();
		setAdapter();
		getfeedbacklist();
	}
	
	private void initview() {
		findViewById(R.id.back).setOnClickListener(this);
		RefreshListView=(PullToRefreshListView) findViewById(R.id.pull_refresh_list);
	}
	
	private void setAdapter() {
		mAdapter=new BaseAdapter() {
			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				if(convertView==null){
					convertView=getLayoutInflater().inflate(R.layout.feed_history_item, null);
				}
				Texttool.setText(convertView, R.id.name, data.get(position).getStudentName());
				Texttool.setText(convertView, R.id.time, data.get(position).getDate().substring(0, 10));
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
				if(data!=null){
					return data.size();
				}
				return 0;
			}
		};
		RefreshListView.setAdapter(mAdapter);
		RefreshListView.setMode(Mode.DISABLED);
		RefreshListView.setOnItemClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		}
	}
	Handler handler =new Handler(){
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			try {
				if(lldialog!=null){
					lldialog.dismiss();
				}
				switch (msg.what) {
				case Constants.GETFEEDBACKLIST:
					data=(ArrayList<Feedback_list>) msg.obj;
					if(data==null || data.size()==0){
						Toast.makeText(getApplicationContext(), "暂无数据", 0).show();
					}else{
						mAdapter.notifyDataSetChanged();
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
	private void getfeedbacklist() {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		
		lldialog=LodingDialog.DialogFactor(Feedback_History.this, "正在获取数据", true);
		new Thread(new Runnable() {
			public void run() {
				String service = "feedbackEverydayService";
				String method = "searchPageByStudentId";
				String token = MD5.MD5(service+method+Constants.Key);
				String params = "{studentId:'"+studentId+"'}";
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
						Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<ArrayList<Feedback_list>>(){}.getType());
						handler.sendMessage(handler.obtainMessage(Constants.GETFEEDBACKLIST, object));
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent(Feedback_History.this,Feedback.class);
		intent.putExtra("show", true);
		intent.putExtra("id", data.get(arg2-1).getId());
		startActivity(intent);
	}
}
