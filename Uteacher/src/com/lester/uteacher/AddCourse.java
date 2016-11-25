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
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract.Data;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.dream.framework.utils.dialog.LodingDialog;
import com.google.gson.reflect.TypeToken;
import com.lester.company.http.HttpURL;
import com.lester.school.requst.Constants;
import com.lester.school.requst.JsonUtil;
import com.lester.uteacher.adapter.BannerAdapter;
import com.lester.uteacher.adapter.CouserAdapter;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.lester.uteacher.tool.Userinfo;
import com.teacher.View.ScrollControllViewPager;
import com.teacher.View.VerticalScrollView;
import com.teacher.data.CouserData;
import com.teacher.data.GetCoutserData;

public class AddCourse extends Activity implements OnClickListener{
	private LodingDialog lldialog;
	public ScrollControllViewPager mViewPager;
	public BannerAdapter mAdapter;
	private ArrayList<View> mViewList;
	public ArrayList< ArrayList<CouserData> > couserdata;
	private ArrayList<View> barview;
	public ArrayList<CouserAdapter> adapters;
	private String classId;
	public boolean show=false;
	private GetCoutserData tempdata;
	private Boolean isadd=true;//是否是添加
	

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
//		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		show=getIntent().getBooleanExtra("show", false);
		setContentView(R.layout.addcourse);
//		setfullScroll();
		initview();
		getData();
		findViewById(R.id.send).setVisibility(View.GONE);//默认隐藏提交按钮
	}
	
	
	/**
	 * 滚动到顶部
	 */
	private void setfullScroll(){
		VerticalScrollView scrollView=(VerticalScrollView) findViewById(R.id.scrollview);
		scrollView.fullScroll(ScrollView.FOCUS_UP);
	}
	//设置标题
	
	private void setTitel(String titel,String righttitel){
		Texttool.setText(AddCourse.this, R.id.bartitle, titel);
		Texttool.setText(AddCourse.this, R.id.selectclass, righttitel);
	}
	
	
	private void getData() {
		// TODO Auto-generated method stub
		if(show){//仅仅显示
			String id=getIntent().getStringExtra("id");
			if(id!=null){
				getinfo(id);
			}
		}else{
			classId=getIntent().getStringExtra("classId");
			addinfo(classId);
			couserdata=new ArrayList<ArrayList<CouserData>>();
		}
	}
	/**
	 * 是否显示发送按钮
	 * @param show
	 */
	private void Showsendbt(boolean show){
		if(show){
			findViewById(R.id.send).setVisibility(View.VISIBLE);
//			((EditText)findViewById(R.id.parentword)).setFocusable(true);
//			((EditText)findViewById(R.id.aim)).setFocusable(true);
//			((EditText)findViewById(R.id.parentword)).setEnabled(true);
//			((EditText)findViewById(R.id.aim)).setEnabled(true);
		}else{
			findViewById(R.id.send).setVisibility(View.GONE);
			((EditText)findViewById(R.id.parentword)).setEnabled(false);
			((EditText)findViewById(R.id.aim)).setEnabled(false);
		}
	}
	private void initview() {
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.send).setOnClickListener(this);
	}
	private void initData() {
		barview=new ArrayList<View>();
		barview.add(findViewById(R.id.bar0));
		barview.add(findViewById(R.id.bar1));
		barview.add(findViewById(R.id.bar2));
		barview.add(findViewById(R.id.bar3));
		barview.add(findViewById(R.id.bar4));
		barview.add(findViewById(R.id.bar5));
		barview.add(findViewById(R.id.bar6));
		for(int i=0;i<barview.size();i++){
			barview.get(i).setOnClickListener(new barListener());
		}
		mViewPager = (ScrollControllViewPager) findViewById(R.id.vp);
		mViewList = new ArrayList<View>();
		LayoutInflater inflater = getLayoutInflater();
		adapters=new ArrayList<CouserAdapter>();
		for(int i=0;i<7;i++){
			View view = inflater.inflate(R.layout.courselist_item, null);
			CouserAdapter adapter=new CouserAdapter(couserdata.get(i),AddCourse.this,i);
			adapters.add(adapter);
			ListView listView=(ListView) view.findViewById(R.id.listview);
			listView.setAdapter(adapter);
			mViewList.add(view);
		}
		mAdapter = new BannerAdapter(mViewList,getApplicationContext());
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(new onPageChangeListener());
	}
	public class barListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.bar0:
				setbarColor(0);
				break;
			case R.id.bar1:
				setbarColor(1);
				break;
			case R.id.bar2:
				setbarColor(2);
				break;
			case R.id.bar3:
				setbarColor(3);
				break;
			case R.id.bar4:
				setbarColor(4);
				break;
			case R.id.bar5:
				setbarColor(5);
				break;
			case R.id.bar6:
				setbarColor(6);
				break;
			}
		}
	}
	public class onPageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			setbarColor(arg0);
		}
	}
	public void refresh(){
		for(int i=0;i<adapters.size();i++){
			adapters.get(i).notifyDataSetChanged();
		}
	}
	private void setbarColor(int p){
		for(int i=0;i<barview.size();i++){
			barview.get(i).setBackgroundColor(getResources().getColor(R.color.chengse));
		}
		mViewPager.setCurrentItem(p);
		barview.get(p).setBackgroundColor(getResources().getColor(R.color.barchengse));
	}
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.send:
			System.out.println(couserdata.toString());
			if(canSend()){
				try {
				
				
				JSONArray courseInfo=new JSONArray();
				
					for(int u=0;u<couserdata.get(0).size();u++){
						JSONObject json=new JSONObject();
						json.put("startTime", "'"+couserdata.get(0).get(u).getStarttime()+"'");
						json.put("endTime", "'"+couserdata.get(0).get(u).getEndtime()+"'");
						json.put("typeName", "'"+couserdata.get(0).get(u).getTitle()+"'");
						json.put("no", "'"+String.valueOf(u+1)+"'");
						courseInfo.put(json);
					}
				JSONObject jsonObject=new JSONObject();
				for(int k=0;k<couserdata.size();k++){
					JSONArray temp=new JSONArray();
					for(int u=0;u<couserdata.get(k).size();u++){
						JSONObject json=new JSONObject();
						json.put("name", "'"+couserdata.get(k).get(u).getContent()+"'");
						json.put("no", "'"+String.valueOf(u+1)+"'");
						temp.put(json);
					}
					String key="";
					switch (k) {
					case 0:
						key="mon";
						break;
					case 1:
						key="tues";
						break;
					case 2:
						key="wed";
						break;
					case 3:
						key="thur";
						break;
					case 4:
						key="fri";
						break;
					case 5:
						key="sat";
						break;
					case 6:
						key="sun";
						break;
					}
					jsonObject.put(key, temp);
				}
				jsonObject.put("courseInfo", courseInfo);
				jsonObject.put("currentObject", "'"+Texttool.Gettext(AddCourse.this,R.id.aim)+"'");
				jsonObject.put("parentWork", "'"+Texttool.Gettext(AddCourse.this,R.id.parentword)+"'");
				jsonObject.put("week", "'"+tempdata.getWeek()+"'");
				jsonObject.put("year", "'"+tempdata.getYear()+"'");
				jsonObject.put("schoolId", "'"+Userinfo.getschoolid(AddCourse.this)+"'");
				jsonObject.put("clazzId", "'"+classId+"'");
				System.out.println(jsonObject.toString().replaceAll("\"", ""));
				saveinfo(jsonObject.toString().replaceAll("\"", "").replaceAll("null", ""));
				} catch (Exception e) {
					
				}
			}
			break;
		case R.id.add:
			intent =new Intent(AddCourse.this,AddCourse.class);
			startActivity(intent);
			break;
		}
	}
	
	public boolean canSend(){
		if(!Texttool.Havecontent(AddCourse.this, R.id.aim)){
			Toast.makeText(getApplicationContext(), "请填写本周目标", 0).show();
			return false;
		}
		if(!Texttool.Havecontent(AddCourse.this, R.id.parentword)){
			Toast.makeText(getApplicationContext(), "请填写家长工作", 0).show();
			return false;
		}
		if(Texttool.Gettext(AddCourse.this, R.id.aim).length()>100){
			Toast.makeText(getApplicationContext(), "本周目标最多能输入100个字", 0).show();
			return false;
		}
		if(Texttool.Gettext(AddCourse.this, R.id.parentword).length()>100){
			Toast.makeText(getApplicationContext(), "家长工作最多能输入100个字", 0).show();
			return false;
		}
		for(int k=0;k<couserdata.size();k++){
			 ArrayList<CouserData> temp=couserdata.get(k);
				for(int u=0;u<temp.size();u++){
					if(temp.get(u).getTitle()==null || temp.get(u).getTitle().equals("")){
						Toast.makeText(getApplicationContext(), "课程标题不能为空", 0).show();
						return false;
					}
					if(temp.get(u).getTitle()!=null && temp.get(u).getTitle().length()>40){
						Toast.makeText(getApplicationContext(), "课程标题不能超过40个字", 0).show();
						return false;
					}
					if(temp.get(u).getContent()!=null && temp.get(u).getContent().length()>40){
						Toast.makeText(getApplicationContext(), "课程内容不能超过40个字", 0).show();
						return false;
					}
					
				}
			}
		return true;
	}
	@SuppressLint("HandlerLeak")
	Handler handler =new Handler(){
		public void handleMessage(android.os.Message msg) {
			try {
				if(lldialog!=null){
					lldialog.dismiss();
				}
			
			switch (msg.what) {
			
				case Constants.SAVECOUSERINFO:
					
					Courselist.activity.refresh();
					finish();
					Toast.makeText(getApplicationContext(), msg.obj.toString(), 0).show();
					
					break;
				case Constants.GETECOUSERINFO:
					GetCoutserData getcoutserData=(GetCoutserData) msg.obj;
					if(getcoutserData!=null){
						Showsendbt(false);
						ingetdata(getcoutserData);
						initData();
						setContent(getcoutserData);
						setbarColor(0);
						setTitel("第"+getcoutserData.getWeek()+"周",getcoutserData.getTime());
					}
					break;
				case Constants.ADDCOUSERINFO://需要添加
					GetCoutserData getcoutserData1=(GetCoutserData) msg.obj;
					if(getcoutserData1!=null){
						isadd=true;
						tempdata=getcoutserData1;
						ingetdata(getcoutserData1);
						initData();
						setContent(getcoutserData1);
						setbarColor(0);
						Showsendbt(true);
						setTitel("第"+getcoutserData1.getWeek()+"周",getcoutserData1.getTime());
					}
					break;
				case 406://验证结果：已经添加过课表，只能修改
					GetCoutserData getcoutserData2=(GetCoutserData) msg.obj;
					if(getcoutserData2!=null){
						isadd=false;
						tempdata=getcoutserData2;
						ingetdata(getcoutserData2);
						initData();
						setContent(getcoutserData2);
						setbarColor(0);
						Showsendbt(true);
						setTitel("第"+getcoutserData2.getWeek()+"周",getcoutserData2.getTime());
//						Toast.makeText(getApplicationContext(), "您已添加过本周课表，无需再次添加", 1).show();
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
	 * 
	 * 处理数据
	 * @param getcoutserData
	 */
	private void ingetdata(GetCoutserData getcoutserData) {
		couserdata=new ArrayList<ArrayList<CouserData>>();
		for(int k=0;k<getcoutserData.getScheduleRecords().size();k++){//第几天
		 ArrayList<CouserData> temp=new ArrayList<CouserData>();
			for(int u=0;u<getcoutserData.getCourseInfo().size();u++){//第几节课
				System.out.println("di"+u);
				for(int n=0;n<getcoutserData.getCourseInfo().size();n++){
					if(getcoutserData.getCourseInfo().get(n).getNo()-1==u){
						String starttime=getcoutserData.getCourseInfo().get(u).getStartTime();
						String endtime=getcoutserData.getCourseInfo().get(u).getEndTime();
						String titel=getcoutserData.getCourseInfo().get(u).getName();
						com.teacher.data.CouserData tempdata = new CouserData();
						tempdata.setStarttime(starttime);
						tempdata.setEndtime(endtime);
						tempdata.setTitle("第"+(int)(u+1)+"节课");
						tempdata.setContent(getcoutserData.getScheduleRecords().get(k).get(u).getName());
						temp.add(tempdata);
						break;
					}
				}
				
			}
			couserdata.add(temp);
		}
	}
	/**
	 * 设置标题，目标等数据
	 * @param getcoutserData
	 */
	private void setContent(GetCoutserData getcoutserData) {
		Texttool.setText(AddCourse.this, R.id.aim, getcoutserData.getCurrent_object());
		Texttool.setText(AddCourse.this, R.id.parentword, getcoutserData.getParent_work());
	}
	private void saveinfo(final String str) {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		if(isadd){
			lldialog=LodingDialog.DialogFactor(AddCourse.this, "正在添加课表", true);
		}else{
			lldialog=LodingDialog.DialogFactor(AddCourse.this, "正在修改课表", true);
		}
		new Thread(new Runnable() {
	        public void run() {
	            String service = "clazzScheduleService";
	            String method = "save";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = str;
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
//	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<Teacherinfo.data>(){}.getType());
	    				if(isadd){
	    					handler.sendMessage(handler.obtainMessage(Constants.SAVECOUSERINFO, "添加课表成功"));
	    				}else{
	    					handler.sendMessage(handler.obtainMessage(Constants.SAVECOUSERINFO, "修改课表成功"));
	    				}
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
	private void getinfo(final String id) {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		lldialog=LodingDialog.DialogFactor(AddCourse.this, "正在获取数据", true);
		new Thread(new Runnable() {
			public void run() {
				String service = "clazzScheduleService";
				String method = "getScheduleDetailById";
				String token = MD5.MD5(service+method+Constants.Key);
				String params = "{id:'"+id+"'}";
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
	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<GetCoutserData>(){}.getType());
						handler.sendMessage(handler.obtainMessage(Constants.GETECOUSERINFO, object));
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
	/**
	 * 验证
	 * @param id
	 */
	private void addinfo(final String clazzId) {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		lldialog=LodingDialog.DialogFactor(AddCourse.this, "正在获取验证信息", true);
		new Thread(new Runnable() {
			public void run() {
				String service = "clazzScheduleService";
				String method = "add";
				String token = MD5.MD5(service+method+Constants.Key);
				String params = "{clazzId:'"+clazzId+"'}";
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
					String jsonData = jsonObj.getString("data");
					Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<GetCoutserData>(){}.getType());
					if (jsonObj.getString("success").equals("true")) {
						handler.sendMessage(handler.obtainMessage(Constants.ADDCOUSERINFO, object));
					}else{
						handler.sendMessage(handler.obtainMessage(406, object));
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
