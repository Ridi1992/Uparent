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
import com.lester.company.http.HttpURL;
import com.lester.school.requst.Constants;
import com.lester.school.requst.JsonUtil;
import com.lester.uteacher.adapter.BannerAdapter;
import com.lester.uteacher.adapter.RecipesAdapter;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.teacher.View.ScrollControllViewPager;
import com.teacher.data.CouserData;
import com.teacher.data.GetCoutserData;
import com.teacher.data.Recipedata;

import android.R.array;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

public class Recipes extends Activity implements OnClickListener{
	private ArrayList<View> barview;
	public ScrollControllViewPager mViewPager;
	public ArrayList< ArrayList<CouserData> > couserdata;
	private ArrayList<View> mViewList;
	public ArrayList<RecipesAdapter> adapters;
	public BannerAdapter mAdapter;
	private String schoolId,week,year,time;
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
		super.onCreate(savedInstanceState);
		schoolId=getIntent().getStringExtra("schoolId");
		week=getIntent().getStringExtra("week");
		year=getIntent().getStringExtra("year");
		time=getIntent().getStringExtra("time");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.recipes);
		initview();
		getdata();
		setTitel("第"+week+"周", time );
	}
	private void initview() {
		findViewById(R.id.back).setOnClickListener(this);
	}
	private void  setTitel(String titel,String righttitel){
		Texttool.setText(Recipes.this, R.id.bartitle,titel);
		Texttool.setText(Recipes.this, R.id.selectclass,righttitel);
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
		adapters=new ArrayList<RecipesAdapter>();
		for(int i=0;i<7;i++){
			View view = inflater.inflate(R.layout.courselist_item, null);
			RecipesAdapter adapter=new RecipesAdapter(couserdata.get(i),Recipes.this,i);
			adapters.add(adapter);
			ListView listView=(ListView) view.findViewById(R.id.listview);
			listView.setAdapter(adapter);
			mViewList.add(view);
		}
		mAdapter = new BannerAdapter(mViewList,getApplicationContext());
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(new onPageChangeListener());
	}
	public class onPageChangeListener implements OnPageChangeListener{
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		@Override
		public void onPageSelected(int arg0) {
			setbarColor(arg0);
		}
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
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.back:
			finish();
			}
	}
	private void setbarColor(int p){
		for(int i=0;i<barview.size();i++){
			barview.get(i).setBackgroundColor(getResources().getColor(R.color.chengse));
		}
		mViewPager.setCurrentItem(p);
		barview.get(p).setBackgroundColor(getResources().getColor(R.color.barchengse));
	}
	
	
	Handler handler =new Handler(){
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			try {
				if(lldialog!=null){
					lldialog.dismiss();
				}
			switch (msg.what) {
			case Constants.GETRECIPESDATA:
				ArrayList<Recipedata> data=(ArrayList<Recipedata>) msg.obj;
				if(data!=null){
					ingetdata(data);
					initData();
//					setContent(getcoutserData);
					setbarColor(0);
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
	
	private void ingetdata(ArrayList<Recipedata> data) {
		couserdata=new ArrayList<ArrayList<CouserData>>();
		for(int k=0;k<7;k++){
		 ArrayList<CouserData> temp=new ArrayList<CouserData>();
		 switch (k) {
			case 0:
				for(int u=0;u<data.size();u++){
					com.teacher.data.CouserData tempdata = new CouserData();
					tempdata.setStarttime(data.get(u).getTime());
					
					tempdata.setTitle(data.get(u).getMonday());
					
					temp.add(tempdata);
				}
				break;
			case 1:
				for(int u=0;u<data.size();u++){
					com.teacher.data.CouserData tempdata = new CouserData();
					tempdata.setStarttime(data.get(u).getTime());
					
					tempdata.setTitle(data.get(u).getTuesday());
					
					temp.add(tempdata);
				}
				break;
			case 2:
				for(int u=0;u<data.size();u++){
					com.teacher.data.CouserData tempdata = new CouserData();
					tempdata.setStarttime(data.get(u).getTime());
					
					tempdata.setTitle(data.get(u).getWednesday());
					
					temp.add(tempdata);
				}
				break;
			case 3:
				for(int u=0;u<data.size();u++){
					com.teacher.data.CouserData tempdata = new CouserData();
					tempdata.setStarttime(data.get(u).getTime());
					
					tempdata.setTitle(data.get(u).getThursday());
					
					temp.add(tempdata);
				}
				break;
			case 4:
				for(int u=0;u<data.size();u++){
					com.teacher.data.CouserData tempdata = new CouserData();
					tempdata.setStarttime(data.get(u).getTime());
					
					tempdata.setTitle(data.get(u).getFriday());
					
					temp.add(tempdata);
				}
				break;
			case 5:
				for(int u=0;u<data.size();u++){
					com.teacher.data.CouserData tempdata = new CouserData();
					tempdata.setStarttime(data.get(u).getTime());
					
					tempdata.setTitle(data.get(u).getSaturday());
					
					temp.add(tempdata);
				}
				break;
			case 6:
				for(int u=0;u<data.size();u++){
					com.teacher.data.CouserData tempdata = new CouserData();
					tempdata.setStarttime(data.get(u).getTime());
					tempdata.setTitle(data.get(u).getSunday());
					temp.add(tempdata);
				}
				break;
			}
			couserdata.add(temp);
		}
	}
	private void getdata() {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		lldialog=LodingDialog.DialogFactor(Recipes.this, "正在获取数据", true);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "recipeService";
	            String method = "getRecipeByYearWeek";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = "{schoolId:'"+schoolId+"',"
	            		+ "year:'"+year+"',"
	            		+ "week:'"+week+"'}";
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
	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<ArrayList<Recipedata>>(){}.getType());
						handler.sendMessage(handler.obtainMessage(Constants.GETRECIPESDATA, object));
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
