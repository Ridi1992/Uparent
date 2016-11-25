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
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.best.cropimage.CropImageActivity;
import com.dream.framework.utils.dialog.LodingDialog;
import com.google.gson.reflect.TypeToken;
import com.lester.company.http.HttpURL;
import com.lester.headview.CustomImageView;
import com.lester.school.loader.ImageLoader;
import com.lester.school.requst.Constants;
import com.lester.school.requst.JsonUtil;
import com.lester.uparent.childinfo.Student;
import com.lester.uparent.childinfo.Student.datas;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.Base64;
import com.lester.uteacher.tool.BitMap;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.lester.uteacher.tool.PhotoPickUtil;
import com.lester.uteacher.tool.PhotoPickUtil.OnPhotoPickedlistener;
import com.lester.uteacher.tool.Userhead;
import com.lester.uteacher.tool.Userinfo;

public class Childinfo extends Activity implements OnClickListener,OnPhotoPickedlistener {
	private LodingDialog lldialog;
	public static Childinfo activity;
	private PhotoPickUtil photoPickUtil;
	private Bitmap bmp;
	private String provePhoto;
	private Userhead userhead=new Userhead();
	private int imgid;
	private Student.datas  redata;
	
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
		photoPickUtil=new PhotoPickUtil(Childinfo.this, Childinfo.this);
		setContentView(R.layout.studentdetail);
		initview();
		Childinfo();
	}

	private void initview() {
		// TODO Auto-generated method stub
		findViewById(R.id.back).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		photoPickUtil.pickResult(requestCode, resultCode, data);
	}
	/* (non-Javadoc)
	 * @see com.lester.uteacher.tool.PhotoPickUtil.OnPhotoPickedlistener#photoPicked(java.lang.String, android.graphics.Bitmap)
	 */
	@Override
	public void photoPicked(String path, Bitmap bmp) {
		 Intent intent = new Intent(Childinfo.this, CropImageActivity.class);
         intent.putExtra("path", path);
         intent.putExtra("info", "2");
         startActivity(intent);
//		if(bmp!=null){
//			this.bmp=bmp;
//			System.out.println("头像");
//			setimg(bmp);
//			provePhoto = Base64.encode(BitMap.Bitmap2Bytes(bmp));
//			updateChildHeadImg();
//		}
	}
	
	public void setImg(String path){
		this.bmp=BitMap.getBitmap(path);
		if(bmp!=null){
			CustomImageView headimg=(CustomImageView)findViewById(imgid);
			headimg.setImageBitmap(bmp);
			provePhoto = Base64.encode(BitMap.Bitmap2Bytes(bmp));
			updateChildHeadImg();
		}
	}
	private void setInfo(datas data) {
		CustomImageView headimg=(CustomImageView)findViewById(R.id.childhead);
		String head=data.getPhoto();
		if(head!=null && (!head.equals(""))){
			new ImageLoader(this).DisplayImage(head, headimg);
			 }
		headimg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				imgid=R.id.childhead;
				photoPickUtil.doPickPhotoAction(false, 300, 300);
			}
		});
		Texttool.setText(Childinfo.this, R.id.studentname, data.getName());
		Texttool.setText(Childinfo.this, R.id.studentclass, data.getClazzName());
		Texttool.setText(Childinfo.this, R.id.nation, data.getNation_display());
		Texttool.setText(Childinfo.this, R.id.id_card, data.getId_card());
		Texttool.setText(Childinfo.this, R.id.birthplace, data.getBirthplace());
		Texttool.setText(Childinfo.this, R.id.registered_place, data.getRegistered_place());
		Texttool.setText(Childinfo.this, R.id.urgent_link_man, data.getUrgent_link_man());
		Texttool.setText(Childinfo.this, R.id.urgent_link_phone, data.getUrgent_link_phone());
		Texttool.setText(Childinfo.this, R.id.address, data.getAddress());
		Texttool.setText(Childinfo.this, R.id.entry_time, "入园时间 "+data.getEntry_time().substring(0, 10));
		Texttool.setText(Childinfo.this, R.id.birthday, "生日 "+data.getBirthday().substring(0, 10));
		if(data.getGender().equals("F")){
			((ImageView)findViewById(R.id.sex)).setImageResource(R.drawable.nv);
		}else if(data.getGender().equals("M")){
			((ImageView)findViewById(R.id.sex)).setImageResource(R.drawable.nan);
		}
		TextView  textView=(TextView) findViewById(R.id.status);
		switch (data.getPosition_status()) {
		case 1:
			Texttool.setText(textView, "车上");
			textView.setBackgroundResource(R.drawable.ischeshang);
			break;
		case 2:
			Texttool.setText(textView, "在校");
			textView.setBackgroundResource(R.drawable.isschool);
			break;
		case 3:
			Texttool.setText(textView, "离校");
			textView.setBackgroundResource(R.drawable.notinschool);
			break;
		}
		try {
			
			setparentinfo(data);
		} catch (Exception e) {
			System.out.println("措意"+e.toString());
		}
		
	};
	/**
	 * 设置家长信息
	 * @param datas 
	 */
	private void setparentinfo(final datas datas) {
	View view =findViewById(R.id.layout);
		for(int i=0;i<datas.getParent().size();i++){
			View v=getLayoutInflater().inflate(R.layout.mystudent_parentinfo, null);
			CustomImageView headimg=(CustomImageView)v.findViewById(R.id.parenthead);
			String head=datas.getParent().get(i).getParentPhoto();
			if(head!=null && (!head.equals(""))){
				new ImageLoader(this).DisplayImage(head, headimg);
				 }
			
			if(datas.getParent().get(i).getParentGender().equals("F")){
				((ImageView)v.findViewById(R.id.sex)).setImageResource(R.drawable.nv);
			}else if(datas.getParent().get(i).getParentGender().equals("M")){
				((ImageView)v.findViewById(R.id.sex)).setImageResource(R.drawable.nan);
			}
			
			Texttool.setText(v, R.id.name, datas.getParent().get(i).getParentName());
			Texttool.setText(v, R.id.id_card, datas.getParent().get(i).getParentIdcard());
//			Texttool.setText(v, R.id.company, datas.getParent().get(i).getParentCompany());
//			Texttool.setText(v, R.id.education, datas.getParent().get(i).getParentEducation());
			Texttool.setText(v, R.id.tel, datas.getParent().get(i).getParentTelephone());
			((ViewGroup) view).addView(v);
		}
	}
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			try {
				if(lldialog!=null){
					lldialog.dismiss();
				}
				switch (msg.what) {
				case Constants.GETCHILDINFO:
					Student.datas data=(Student.datas) msg.obj;
					if(data!=null){
						setInfo(data);
						redata=data;
					}
					
					break;
				case Constants.UPDATAHEAD_CHILD:
					Toast.makeText(getApplicationContext(), msg.obj.toString(), 0).show();
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
	 * 获取孩子信息
	 */
	private void Childinfo() {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		lldialog=LodingDialog.DialogFactor(Childinfo.this, "正在获取数据", true);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "studentService";
	            String method = "getById";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = 
	            		"{id:'"+Userinfo.getchildid(Childinfo.this)+"'}";
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
	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<Student.datas>(){}.getType());
	    				handler.sendMessage(handler.obtainMessage(Constants.GETCHILDINFO, object));
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
	 * 修改小孩头像
	 */
	private void updateChildHeadImg() {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		lldialog=LodingDialog.DialogFactor(Childinfo.this, "正在提交数据", false);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "parentService";
	            String method = "updateChildHeadImg";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = 
	            		"{studentId:'"+redata.getId()+
	            		"',loginId:'"+Userinfo.getmobile_phone(Childinfo.this)+
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
//	    				String jsonData = jsonObj.getString("data");
//	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<Teacherinfo.data>(){}.getType());
	    				handler.sendMessage(handler.obtainMessage(Constants.UPDATAHEAD_CHILD, "头像修改成功"));
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
