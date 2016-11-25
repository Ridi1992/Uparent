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
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.dream.framework.utils.dialog.LodingDialog;
import com.google.gson.reflect.TypeToken;
import com.lester.company.http.HttpURL;
import com.lester.school.requst.Constants;
import com.lester.school.requst.JsonUtil;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.lester.uteacher.tool.Timetool;
import com.lester.uteacher.tool.Userinfo;
import com.teacher.data.Commentdata;
import com.teacher.data.Commentdata_yanzheng;
import com.teacher.data.ParentData;

public class Comment  extends Activity implements OnClickListener{
   
	private LodingDialog lldialog;
	private ListView listview;
	private BaseAdapter adapter;
	private float[]ratbar =new float[6];
	private Boolean show=false;
	private Commentdata data;
	private Commentdata_yanzheng tempdata;
	
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
		setContentView(R.layout.comment);
		initview();
		setAdapter();
		getshow();
	}

	private void  setTitel(String titel,String righttitel){
		Texttool.setText(this, R.id.bartitle,titel);
		Texttool.setText(this, R.id.selectclass,righttitel);
	}
	
	private void setContent(String childname,String classname,String time,String name){
		Texttool.setText(this, R.id.childname,childname);
		Texttool.setText(this, R.id.classname,classname);
		Texttool.setText(this, R.id.time,time);
		Texttool.setText(this, R.id.name,name);
	}

	private void getshow() {
		show=getIntent().getBooleanExtra("show", false);
		tempdata=(Commentdata_yanzheng) getIntent().getSerializableExtra("tempdata");
		if(show){
			showbt(false);
			String id=getIntent().getStringExtra("id");
			getcommentdata(id);
		}else{
			showbt(true);
			setTitel("第"+tempdata.getWeek()+"周",tempdata.getTime());
			Userinfo userinfo=new Userinfo();
			setContent(userinfo.getchildname(Comment.this),userinfo.getchildclassname(Comment.this),
					Timetool.getnowtime(),userinfo.getname(Comment.this));
		}
	}



	private void setAdapter() {
		final ArrayList<String > titel=new ArrayList<String>();
		titel.add("饮食安排");
		titel.add("课程安排");
		titel.add("活动安排");
		titel.add("教学质量");
		titel.add("服务质量");
		titel.add("总体评价");
 		adapter=new BaseAdapter() {
			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				if(convertView==null){
					convertView=getLayoutInflater().inflate(R.layout.comment_item, null);
				}
				Texttool.setText(convertView, R.id.titel, titel.get(position));
				RatingBar bar=(RatingBar) convertView.findViewById(R.id.bar);
				bar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
					@Override
					public void onRatingChanged(RatingBar ratingBar, float rating,
							boolean fromUser) {
						ratbar[position]=rating;
					}
				});
				if(data!=null && data.getOptions()!=null && data.getOptions().size()!=0){
					
					for(int i=0;i<data.getOptions().size();i++){
						if(position+1==data.getOptions().get(i).getNo()){
							bar.setRating(data.getOptions().get(i).getValue());
							break;
						}
					}
					
				}
				if(show){
					bar.setOnTouchListener(new OnTouchListener() {
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							
							return true;//不可点击
						}
					});
				}
				return convertView;
			}
			
			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return titel.size();
			}
		};
		listview.setAdapter(adapter);
	}


	private void initview() {
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.save).setOnClickListener(this);
		listview=(ListView) findViewById(R.id.listview);
	}

	private void showbt(Boolean b){
		if(b){
			findViewById(R.id.save).setVisibility(View.VISIBLE);
		}else{
			findViewById(R.id.save).setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.back:
					finish();
				break;
			case R.id.save:
				try {
					String n="'";
					JSONObject jsonObject=new JSONObject();
					JSONArray array=new JSONArray();
					for(int i=0;i<ratbar.length;i++){
						JSONObject temp=new JSONObject();
						temp.put("no", n+(i+1)+n);
						temp.put("name", n+ratbar[i]+n);
						array.put(temp);
					}
					jsonObject.put("option", array);
					jsonObject.put("studentId", n+Userinfo.getchildid(Comment.this)+n);
					jsonObject.put("parentId", n+Userinfo.getid(Comment.this)+n);
					jsonObject.put("schoolId", n+Userinfo.getchildschoolid(Comment.this)+n);
					jsonObject.put("week", n+tempdata.getWeek()+n);//验证时返回
					jsonObject.put("year", n+tempdata.getYear()+n);//验证时返回
					save(jsonObject.toString().replace("\"", ""));
				} catch (Exception e) {
					// TODO: handle exception
				}
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
				case Constants.SAVECOMMENT:
					Toast.makeText(getApplicationContext(), "提交成功", 0).show();
					Comment.this.finish();
					Commentlist.commentlist.refresh();
					break;
					
				case Constants.GETCOMMENTDATA:
					data=(Commentdata) msg.obj;
					if(data!=null){
						setTitel("第"+data.getWeek()+"周",data.getTimeZone());
						adapter.notifyDataSetChanged();
						setContent(data.getStudentName(),data.getClazzName(),
								data.getCreateDate(),data.getParentName());
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
	private void save(final String data) {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		lldialog=LodingDialog.DialogFactor(Comment.this, "正在提交数据", false);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "parentAppraiseService";
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
	    				handler.sendMessage(handler.obtainMessage(Constants.SAVECOMMENT, ""));
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
	private void getcommentdata(final String id) {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		lldialog=LodingDialog.DialogFactor(Comment.this, "正在获取数据", false);
		new Thread(new Runnable() {
			public void run() {
				String service = "parentAppraiseService";
				String method = "getById";
				String token = MD5.MD5(service+method+Constants.Key);
				String params = "{id:'"+id+"'}";
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
	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<Commentdata>(){}.getType());
						handler.sendMessage(handler.obtainMessage(Constants.GETCOMMENTDATA, object));
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
	
}
