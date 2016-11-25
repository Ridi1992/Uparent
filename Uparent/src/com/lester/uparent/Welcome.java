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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.baidu.mobstat.StatService;
import com.dream.framework.utils.dialog.MakesureDialog;
import com.dream.framework.utils.dialog.MakesureDialog.cancell;
import com.dream.framework.utils.dialog.MakesureDialog.ok;
import com.lester.company.http.HttpURL;
import com.lester.school.requst.Constants;
import com.lester.school.requst.PublicRequest;
import com.lester.uparent.login.logindata;
import com.lester.uteacher.adapter.BannerAdapter;
import com.lester.uteacher.tool.Appinfo;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.lester.uteacher.tool.Userinfo;


public class Welcome extends Activity{
	
	private ViewPager mViewPager;
	private BannerAdapter mAdapter;
	private ArrayList<View> mViewList;
	private int emun;//
	private ImageView[] indicator_imgs;//
	Handler handler = new Handler(){
		public void  handleMessage(android.os.Message msg){
			Intent  intent = new Intent();
			switch (msg.what) {
			case Constants.LOGIN://登录成功
				logindata.data data=(com.lester.uparent.login.logindata.data) msg.obj;
					Userinfo.setinfo(Welcome.this,data);//保存用户信息
					handler.sendEmptyMessageDelayed(0, 1000);
				break;
			case Constants.CHECKUP://有新的升级
				String url=msg.obj.toString();
				showDialog(url);
				break;
			case 406://断网
				Userinfo.logoffchild(Welcome.this);
				Userinfo.logoff(Welcome.this);
				handler.sendEmptyMessageDelayed(0, 1000);
				break;
			case 408://没有新的版本
				if(Userinfo.islogin(Welcome.this)){
					PublicRequest.getInstance(handler).login(Constants.LOGIN,
							Userinfo.getmobile_phone(Welcome.this), 
							Userinfo.getpassword(Welcome.this));
				}else{
					handler.sendEmptyMessageDelayed(0, 1000);
				}
				break;
				
			case 0:
				
				intent.putExtra("info", "");
				intent.setClass(Welcome.this, Login.class);
				startActivity(intent);
				finish();
				
				break;
			}
		}
	};
	
	private void showDialog(final String url){
		MakesureDialog dialog=new MakesureDialog(Welcome.this, new ok() {
			@Override
			public void ok() {
				Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse(url));
				Welcome.this.startActivity(localIntent);
				finish();
			}
		}, new cancell() {
			@Override
			public void cancell() {
				finish();
			}
		});
		dialog.show();
	}
	
	
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
		setContentView(R.layout.welcome);
		SharedPreferences shared = getSharedPreferences("uparent", 0);
		Boolean IsFirst=shared.getBoolean("IsFirst", false);
		if(IsFirst){
			if(Net.isNetworkAvailable(Welcome.this)){
				finish();
				return;
			}
			findViewById(R.id.qidong).setVisibility(View.VISIBLE);
			checkUpdata(Appinfo.getVersionCode(getApplicationContext()));//检查升级
		}else{
			Editor editor = shared.edit();
			editor.putBoolean("IsFirst", true);
			editor.commit();
			mViewPager = (ViewPager) findViewById(R.id.vp);
			initData();
		}
	}

	/**
	 * 检查升级
	 */
	private void checkUpdata(final String versionCode) {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		new Thread(new Runnable() {
			public void run() {
				String service = "clientVersionService";
				String method = "checkVersion";
				String token = MD5.MD5(service+method+Constants.Key);
				String params = "{clientOS:'Android"
						+"',type:'"
						+"2"
						+"',versionCode:"
						+versionCode+"}";
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
						String url = jsonObj.getJSONObject("data").getString("url");
//						Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<GetFeedbackdata>(){}.getType());
						handler.sendMessage(handler.obtainMessage(Constants.CHECKUP,url ));
					}else{
						handler.sendMessage(handler.obtainMessage(408, jsonObj.getString("message")));
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
				}
			}
		}).start();
	}
	private void initData() {
		mViewList = new ArrayList<View>();
		LayoutInflater inflater = getLayoutInflater();
		ArrayList<Integer> resId=new ArrayList<Integer>();
		resId.add(R.drawable.w1);
		resId.add(R.drawable.w2);
		resId.add(R.drawable.w3);
		resId.add(R.drawable.w4);
		for(int i=0;i<3;i++){
			View view = inflater.inflate(R.layout.wz_page, null);
			((ImageView)view.findViewById(R.id.img)).setImageResource(resId.get(i));
			mViewList.add(view);
		}
		View view = inflater.inflate(R.layout.wz_page, null);
		((ImageView)view.findViewById(R.id.img)).setImageResource(resId.get(3));
		view.findViewById(R.id.img).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("info", "");
				intent.setClass(Welcome.this, Login.class);
				startActivity(intent);
				finish();
			}
		});
		mViewList.add(view);
		mAdapter = new BannerAdapter(mViewList,getApplicationContext());
		mViewPager.setAdapter(mAdapter);
		emun = mViewList.size();
		indicator_imgs = new ImageView[emun];
		initIndicator();
	}
	private void initIndicator(){
		ImageView imgView;
		View v =findViewById(R.id.indicator);
		for (int i = 0; i < emun ; i++) {
			imgView = new ImageView(this);
			LinearLayout.LayoutParams params_linear = new LinearLayout.LayoutParams(12,12);
			params_linear.setMargins(7, 10, 7, 60);
			imgView.setLayoutParams(params_linear);
			indicator_imgs[i] = imgView;
			if (i == 0) { 
					indicator_imgs[i].setBackgroundResource(R.drawable.indicator_focused);
				} else {
					indicator_imgs[i].setBackgroundResource(R.drawable.indicator);
				}
			((ViewGroup)v).addView(indicator_imgs[i]);
		}
			mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				for (int i = 0; i < indicator_imgs.length; i++) {
					indicator_imgs[i].setBackgroundResource(R.drawable.indicator);
				}
				indicator_imgs[arg0].setBackgroundResource(R.drawable.indicator_focused);
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}
}
