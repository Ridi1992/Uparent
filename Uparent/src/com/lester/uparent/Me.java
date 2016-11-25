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

import com.baidu.mobstat.StatService;
import com.dream.framework.utils.dialog.LodingDialog;
import com.google.gson.reflect.TypeToken;
import com.lester.company.http.HttpURL;
import com.lester.headview.CustomImageView;
import com.lester.parent.me.Parentinfodata;
import com.lester.parent.me.Parentinfodata.data;
import com.lester.school.loader.ImageLoader;
import com.lester.school.requst.Constants;
import com.lester.school.requst.JsonUtil;
import com.lester.uparent.childinfo.Student;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.Base64;
import com.lester.uteacher.tool.BitMap;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.lester.uteacher.tool.PhotoPickUtil;
import com.lester.uteacher.tool.Userhead;
import com.lester.uteacher.tool.Userinfo;
import com.lester.uteacher.tool.PhotoPickUtil.OnPhotoPickedlistener;

import android.R.menu;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class Me extends Activity implements OnClickListener,OnPhotoPickedlistener {
	private LodingDialog lldialog;
	private PhotoPickUtil photoPickUtil;
	private Bitmap bmp;
	private Userhead userhead=new Userhead();
	private String provePhoto;
	
	
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
		setContentView(R.layout.me);
		initview();
		getParentInfo();
	}

	private void initview() {
		try {
			findViewById(R.id.logoff).setOnClickListener(this);
			findViewById(R.id.changepassword).setOnClickListener(this);
			findViewById(R.id.backkk).setOnClickListener(this);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	private void setinfo(data data) {
			Texttool.setText(Me.this, R.id.name, data.getName());
			Texttool.setText(Me.this, R.id.id_card,data.getId_card());
			Texttool.setText(Me.this, R.id.telephone,data.getTelephone());
			Texttool.setText(Me.this, R.id.mobile_phone,data.getMobile_phone());
			Texttool.setText(Me.this, R.id.company, data.getCompany());
			Texttool.setText(Me.this, R.id.des, data.getDescription());
			String sex= data.getGender();
			if(sex.equals("F")){
				((ImageView)findViewById(R.id.sex)).setImageResource(R.drawable.nv);
			}else{
				((ImageView)findViewById(R.id.sex)).setImageResource(R.drawable.nan);
			}
			
			
			String url=data.getPhoto();
			CustomImageView headimg=(CustomImageView)findViewById(R.id.head);
			
//			Bitmap mybm=userhead.gethead(Userinfo.getmobile_phone(Me.this));
//			if(mybm!=null){
//				setimg(mybm);
//			}else{
				if(url!=null && (!url.equals(""))){
					new ImageLoader(this).DisplayImage(url, headimg);
				 }
//			}
			
			headimg.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					photoPickUtil=new PhotoPickUtil(Me.this, Me.this);
					photoPickUtil.doPickPhotoAction(true, 300, 300);
				}
			});
		
		
	};
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		photoPickUtil.pickResult(requestCode, resultCode, data);
	}
	@Override
	public void photoPicked(String path, Bitmap bmp) {
		if(bmp!=null){
				this.bmp=bmp;
				setimg(bmp);
				provePhoto = Base64.encode(BitMap.Bitmap2Bytes(bmp));
				updateParentHeadImg();
				System.out.println("头像");
		}
	}
	private void setimg(Bitmap bmp2) {
		CustomImageView headimg=(CustomImageView)findViewById(R.id.head);
		headimg.setImageBitmap(bmp2);
	}
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.logoff:
			

			final Dialog dialog = new Dialog(Me.this, R.style.MyDialog);   
			dialog.setCanceledOnTouchOutside(true);
			View view=(View)getLayoutInflater().inflate(R.layout.dialogmakesure, null);
			view.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Userinfo.logoff(Me.this);
					Userinfo.logoffchild(Me.this);
					Intent  intent=new Intent(Me.this,Login.class);
					startActivity(intent);
					MainActivity.activity.finish();
					finish();
					dialog.dismiss();
				}
			});
			view.findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
				dialog.setContentView(view);
				dialog.show();
			
			
			
			break;
		case R.id.changepassword:
			intent=new Intent(Me.this,Changepassword.class);
			startActivity(intent);
			break;
		case R.id.backkk:
			finish();
			break;
		default:
			break;
		}
	}
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			try {
				if(lldialog!=null){
					lldialog.dismiss();
				}
				switch (msg.what) {
				case Constants.GETPARENTINFO:
					Parentinfodata.data data=(com.lester.parent.me.Parentinfodata.data) msg.obj;
					setinfo(data);
					break;
				case Constants.UPDATAHEAD_PARENT:
//					userhead.savehead(bmp,Userinfo.getmobile_phone(Me.this));保存头像
					String headurl=(String) msg.obj;
					Userinfo.setparentphoto(Me.this, headurl);
					Toast.makeText(getApplicationContext(), "头像修改成功", 0).show();
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
	 * 修改家长头像
	 */
	private void updateParentHeadImg() {
		if(Net.isNetworkAvailable(getApplicationContext())){
			handler.sendMessage(handler.obtainMessage(406));
			return;
		}
		lldialog=LodingDialog.DialogFactor(Me.this, "正在提交数据", false);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "parentService";
	            String method = "updateParentHeadImg";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = 
	            		"{loginId:'"+Userinfo.getmobile_phone(Me.this)+
	            		"',headImg:'"+provePhoto+
	            		"'}";
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
	        			JSONObject jsonObjurl = new JSONObject(jsonData);
	        			String headurl=jsonObjurl.getString("photo");
//	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<Teacherinfo.data>(){}.getType());
	    				handler.sendMessage(handler.obtainMessage(Constants.UPDATAHEAD_PARENT, headurl));
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
	 * 获取家长信息
	 */
	private void getParentInfo() {
		if(Net.isNetworkAvailable(getApplicationContext())){
			handler.sendMessage(handler.obtainMessage(406));
			return;
		}
		lldialog=LodingDialog.DialogFactor(Me.this, "正在获取数据", false);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "parentService";
	            String method = "getByLoginId";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = 
	            		"{loginId:'"+Userinfo.getmobile_phone(Me.this)+"'}";
	            NameValuePair pair1 = new BasicNameValuePair("service", service);
	            NameValuePair pair2 = new BasicNameValuePair("method", method);
	            NameValuePair pair3 = new BasicNameValuePair("token", token);
	            NameValuePair pair4 = new BasicNameValuePair("params", params);
	            Log.i("service", service);
		           Log.i("method", method);
		           Log.i("token", token);
		           Log.i("params", params);
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
	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<Parentinfodata.data>(){}.getType());
	    				handler.sendMessage(handler.obtainMessage(Constants.GETPARENTINFO, object));
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
