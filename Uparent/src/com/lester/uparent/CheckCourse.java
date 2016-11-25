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
import com.teacher.View.ScrollControllViewPager;
import com.teacher.data.CouserData;
import com.teacher.data.GetCoutserData;

/**
 * @author Best
 *查看课表
 */
public class CheckCourse extends Activity implements OnClickListener{
	private LodingDialog lldialog;
	public ScrollControllViewPager mViewPager;
	public BannerAdapter mAdapter;
	private ArrayList<View> mViewList;
	public ArrayList< ArrayList<CouserData> > couserdata;
	private ArrayList<View> barview;
	public ArrayList<CouserAdapter> adapters;
	private String id;
	
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
		id=getIntent().getStringExtra("id");
		setContentView(R.layout.showcourse);
		initview();
		getcourserdata();
	}

	private void initview() {
		findViewById(R.id.back).setOnClickListener(this);
	}
	private void  setTitel(String titel,String righttitel){
		Texttool.setText(this, R.id.bartitle,titel);
		Texttool.setText(this, R.id.selectclass,righttitel);
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
		for(int i=0;i<couserdata.size();i++){
			View view = inflater.inflate(R.layout.courselist_item, null);
			CouserAdapter adapter=new CouserAdapter(couserdata.get(i),CheckCourse.this,i);
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
			case Constants.GETECOUSERINFO:
				GetCoutserData getcoutserData=(GetCoutserData) msg.obj;
				if(getcoutserData!=null){
					ingetdata(getcoutserData);
					initData();
					setContent(getcoutserData);
					setbarColor(0);
					setTitel("第"+getcoutserData.getWeek()+"周",getcoutserData.getTime());
				}else{
					finish();
					Toast.makeText(getApplicationContext(), "无课程信息", 0).show();
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
	 * 
	 * 处理数据
	 * @param getcoutserData
	 */
	private void ingetdata(GetCoutserData getcoutserData) {
		couserdata=new ArrayList<ArrayList<CouserData>>();
		for(int k=0;k<getcoutserData.getScheduleRecords().size();k++){//第几天
		 ArrayList<CouserData> temp=new ArrayList<CouserData>();
			for(int u=0;u<getcoutserData.getCourseInfo().size();u++){//第几节课
				for(int n=0;n<getcoutserData.getCourseInfo().size();n++){
					if(getcoutserData.getCourseInfo().get(n).getNo()-1==u){
						String starttime=getcoutserData.getCourseInfo().get(u).getStartTime();
						String endtime=getcoutserData.getCourseInfo().get(u).getEndTime();
						String titel=getcoutserData.getCourseInfo().get(u).getName();
						com.teacher.data.CouserData tempdata = new CouserData();
						tempdata.setStarttime(starttime);
						tempdata.setEndtime(endtime);
						tempdata.setTitle(titel);
						tempdata.setContent(getcoutserData.getScheduleRecords().get(k).get(u).getName());
						temp.add(tempdata);
						break;
					}
				}
			}
			couserdata.add(temp);
		}
		
		for(int i=0;i<couserdata.get(0).size();i++){
			System.out.println(i+couserdata.get(0).get(i).getContent());
		}
	}
	/**
	 * 设置标题，目标等数据
	 * @param getcoutserData
	 */
	private void setContent(GetCoutserData getcoutserData) {
		Texttool.setText(CheckCourse.this, R.id.aim, getcoutserData.getCurrent_object());
		Texttool.setText(CheckCourse.this, R.id.parentword, getcoutserData.getParent_work());
	}
	private void getcourserdata() {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		lldialog=LodingDialog.DialogFactor(CheckCourse.this, "正在获取数据", true);
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
}
