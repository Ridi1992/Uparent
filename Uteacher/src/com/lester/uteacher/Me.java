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
import com.best.cropimage.CropImageActivity;
import com.dream.framework.utils.dialog.LodingDialog;
import com.example.jpushdemo.ExampleApplication;
import com.google.gson.reflect.TypeToken;
import com.lester.company.http.HttpURL;
import com.lester.headview.CustomImageView;
import com.lester.school.requst.Constants;
import com.lester.school.requst.JsonUtil;
import com.lester.uteacher.info.Medata;
import com.lester.uteacher.info.Teacherinfo;
import com.lester.uteacher.info.Teacherinfo.data;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.Base64;
import com.lester.uteacher.tool.BitMap;
import com.lester.uteacher.tool.Loadhead;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.lester.uteacher.tool.PhotoPickUtil;
import com.lester.uteacher.tool.Userhead;
import com.lester.uteacher.tool.Userinfo;
import com.lester.uteacher.tool.PhotoPickUtil.OnPhotoPickedlistener;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class Me extends Activity implements OnClickListener,OnPhotoPickedlistener {
	public static Me activity;
	private LodingDialog lldialog;
	private PhotoPickUtil photoPickUtil;
	private Bitmap bmp;
	private String provePhoto;
	private Userhead userhead=new Userhead();
	private int PHOTO_REQUEST_CUT=10001;
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
		activity=this;
		photoPickUtil=new PhotoPickUtil(Me.this, this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.me);
		initview();
		getteacherinfo();
	}

	private void initview() {
	findViewById(R.id.logoff).setOnClickListener(this);
	findViewById(R.id.changepassword).setOnClickListener(this);
	findViewById(R.id.back).setOnClickListener(this);
	findViewById(R.id.teacherhead).setOnClickListener(this);
	}
	private void setinfo(data data) {
		Texttool.setText(Me.this, R.id.name, data.getName());
		Texttool.setText(Me.this, R.id.listBuslineName, data.getListBuslineName());
		Texttool.setText(Me.this, R.id.nation, data.getNation_display());
//		Texttool.setText(Me.this, R.id.telephone, data.getTelephone());
		Texttool.setText(Me.this, R.id.mobile_phone, data.getMobile_phone());
		Texttool.setText(Me.this, R.id.id_card, data.getId_card());
//		Texttool.setText(Me.this, R.id.email, data.getEmail());
		Texttool.setText(Me.this, R.id.schoolName, data.getSchoolName());
//		Texttool.setText(Me.this, R.id.education, data.getEducation());
//		Texttool.setText(Me.this, R.id.remark, data.getRemark());
		
		if(data.getGender().equals("F")){
			((ImageView)findViewById(R.id.sex)).setImageResource(R.drawable.nv);
		}else{
			((ImageView)findViewById(R.id.sex)).setImageResource(R.drawable.nan);
		}
		CustomImageView headimg=(CustomImageView)findViewById(R.id.teacherhead);

		Log.i("头像", data.getPhoto());
			if(data.getPhoto()!=null && (!data.getPhoto().equals(""))){
				System.out.println("头像"+data.getPhoto());
				new Loadhead(this,data.getPhoto(), headimg).execute();
			 }
			String classname="";
		if(data.getClazzName()!=null){
			for(int i=0;i<data.getClazzName().size();i++){
				classname+=data.getClazzName().get(i)+"\t\t";
			}
		}
		Texttool.setText(Me.this, R.id.clazzName , classname);
//		try {
//			switch (data.getJob()) {
//			case 1:
//				Texttool.setText(Me.this, R.id.job, "初级");
//				break;
//			case 2:
//				Texttool.setText(Me.this, R.id.job,"中级" );
//				break;
//			case 3:
//				Texttool.setText(Me.this, R.id.job, "高级");
//				break;
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
			
		
		
	};
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
					Userinfo.logofff(Me.this);
					Intent intent=new Intent(Me.this,Login.class);
					startActivity(intent);
					((ExampleApplication) getApplication()).setAlias("0");
					finish();
					MainActivity.activity.finish();
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
		case R.id.back:
			finish();
			break;
		case R.id.teacherhead://选择图片
			
			photoPickUtil.doPickPhotoAction(false, 640, 640);
			
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		photoPickUtil.pickResult(requestCode, resultCode, data);
		
	}
	
	@Override
	public void photoPicked(String path, Bitmap bmp) {
		 Intent intent = new Intent(Me.this, CropImageActivity.class);
        intent.putExtra("path", path);
        startActivity(intent);

	}
	public void setimg(String path) {
		try {
			bmp=BitMap.getBitmap(path);
			if(bmp!=null){
				CustomImageView headimg=(CustomImageView)findViewById(R.id.teacherhead);
				headimg.setImageBitmap(bmp);
				provePhoto = Base64.encode(BitMap.Bitmap2Bytes(bmp));
				updateTeacherHeadImg();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	Handler handler =new Handler(){
		public void handleMessage(android.os.Message msg) {
			try {
				if(lldialog!=null){
					lldialog.dismiss();
				}
			
			switch (msg.what) {
			case Constants.GETTEACHERINFO:
				Teacherinfo.data data=(com.lester.uteacher.info.Teacherinfo.data) msg.obj;
				setinfo(data);
				break;

			case Constants.UPDATAHEAD:
				Toast.makeText(getApplicationContext(), "头像更新成功", 0).show();
				break;
				
			default:
				break;
			}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	};
	private void getteacherinfo() {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		lldialog=LodingDialog.DialogFactor(Me.this, "正在获取数据", true);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "teacherService";
	            String method = "getByLoginId";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = "{loginId:'"+Userinfo.getmobile_phone(Me.this)+"'}";
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
	}

	private void updateTeacherHeadImg() {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		lldialog=LodingDialog.DialogFactor(Me.this, "正在提交数据", false);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "teacherService";
	            String method = "updateTeacherHeadImg";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = "{loginId:'"+Userinfo.getmobile_phone(Me.this)+
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
	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<Medata.data>(){}.getType());
	    				handler.sendMessage(handler.obtainMessage(Constants.UPDATAHEAD, object));
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
