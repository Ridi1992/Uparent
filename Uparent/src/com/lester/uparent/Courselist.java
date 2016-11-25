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
import org.apache.http.params.CoreConnectionPNames;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.baidu.mobstat.StatService;
import com.dream.framework.utils.dialog.LodingDialog;
import com.google.gson.reflect.TypeToken;
import com.lester.company.http.HttpURL;
import com.lester.school.requst.Constants;
import com.lester.school.requst.JsonUtil;
import com.lester.uteacher.adapter.CourserlistAdapter;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.lester.uteacher.tool.Userhead;
import com.lester.uteacher.tool.Userinfo;
import com.teacher.data.CouserListData;

public class Courselist extends Activity implements OnClickListener , OnItemClickListener{
	private LodingDialog lldialog;
	private Userinfo userinfo=new Userinfo();
	private ListView listview;
	private CourserlistAdapter mAdapter;
	public ArrayList<CouserListData> couserListDatas;
	
	
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
		setContentView(R.layout.courselist);
		initview();
		intData();
		getlistdata(userinfo.getchildid(Courselist.this));
	}

	private void intData() {
		mAdapter=new CourserlistAdapter(Courselist.this);
		mAdapter.setData(couserListDatas);
		listview.setAdapter(mAdapter);
	}

	private void initview() {
		listview=(ListView) findViewById(R.id.listview);
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.add).setOnClickListener(this);
		findViewById(R.id.selectclass).setOnClickListener(this);
		listview.setOnItemClickListener(this);
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
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent =new Intent(Courselist.this,CheckCourse.class);
		intent.putExtra("id", couserListDatas.get(arg2).getId());
		startActivity(intent);
	}
	Handler handler =new Handler(){
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			try {
				if(lldialog!=null){
					lldialog.dismiss();
				}
			switch (msg.what) {
			case Constants.GETLISTDATA://课表数据
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
	private void getlistdata(final String studentId) {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		lldialog=LodingDialog.DialogFactor(Courselist.this, "正在获取数据", true);
		new Thread(new Runnable() {
			public void run() {
				String service = "clazzScheduleService";
				String method = "searchByStudentId";
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
						String jsonData = jsonObj.getString("data");
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
}
