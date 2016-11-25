package com.lester.fragment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import cn.jpush.android.util.ac;

import com.best.cropimage.CropImageActivity;
import com.dream.framework.utils.dialog.LodingDialog;
import com.example.jpushdemo.ExampleApplication;
import com.google.gson.reflect.TypeToken;
import com.lester.company.http.HttpURL;
import com.lester.headview.CustomImageView;
import com.lester.parent.me.Parentinfodata;
import com.lester.parent.me.Parentinfodata.data;
import com.lester.school.loader.ImageLoader;
import com.lester.school.requst.Constants;
import com.lester.school.requst.JsonUtil;
import com.lester.uparent.Aboutour;
import com.lester.uparent.Changepassword;
import com.lester.uparent.Childinfo;
import com.lester.uparent.Login;
import com.lester.uparent.MainActivity;
import com.lester.uparent.ParentManage;
import com.lester.uparent.R;
import com.lester.uparent.Schoolinfo;
import com.lester.uparent.SelectMyChild;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.Base64;
import com.lester.uteacher.tool.BitMap;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.lester.uteacher.tool.PhotoPickUtil;
import com.lester.uteacher.tool.PhotoPickUtil.OnPhotoPickedlistener;
import com.lester.uteacher.tool.Userinfo;
import com.seotech.dialog.DialogUtil;

public class meFragment extends Fragment implements OnClickListener{
	private LodingDialog lldialog;
	public  PhotoPickUtil photoPickUtil;
	private int PHOTO_REQUEST_CUT=10001;
//	private String provePhoto;
	private View view;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view=getActivity().getLayoutInflater().inflate(R.layout.meragment, null);
		initview();
		if(MainActivity.data==null){
			getParentInfo();
		}else{
			setinfo(MainActivity.data);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return view;
	}
	

	private void initview() {
			view.findViewById(R.id.logoff).setOnClickListener(this);
			view.findViewById(R.id.changepassword).setOnClickListener(this);
			view.findViewById(R.id.parentmange).setOnClickListener(this);
			view.findViewById(R.id.childinfo).setOnClickListener(this);
			view.findViewById(R.id.schoolinfo).setOnClickListener(this);
			view.findViewById(R.id.aboutour).setOnClickListener(this);
	}
	private void setinfo(data data) {
			Texttool.setText(view, R.id.name, data.getName());
			Texttool.setText(view, R.id.id_card,data.getId_card());
			Texttool.setText(view, R.id.telephone,data.getTelephone());
			Texttool.setText(view, R.id.mobile_phone,data.getMobile_phone());
//			Texttool.setText(view, R.id.company, data.getCompany());
//			Texttool.setText(view, R.id.des, data.getDescription());
			String sex= data.getGender();
			if(sex.equals("F")){
				((ImageView)view.findViewById(R.id.sex)).setImageResource(R.drawable.nv);
			}else{
				((ImageView)view.findViewById(R.id.sex)).setImageResource(R.drawable.nan);
			}
			
			
			String url=data.getPhoto();
			CustomImageView headimg=(CustomImageView)view.findViewById(R.id.head);
			
			if(url!=null && (!url.equals(""))){
				new ImageLoader(getActivity()).DisplayImage(url, headimg);
			 }
			headimg.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					photoPickUtil=new PhotoPickUtil((MainActivity)getActivity(),meFragment.this,new OnPhotoPickedlistener() {
						@Override
						public void photoPicked(String path, Bitmap bmp) {
							System.out.println("path=="+path);
							 Intent intent = new Intent(getActivity(), CropImageActivity.class);
		                     intent.putExtra("path", path);
		                     intent.putExtra("info", "1");
		                     startActivity(intent);
						}
					});
					photoPickUtil.doPickPhotoAction(false, 640, 640);
				}
			});
	};
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		photoPickUtil.pickResult(requestCode, resultCode, data);
//		if(PHOTO_REQUEST_CUT==requestCode&&resultCode==Activity.RESULT_OK){
//			setImg(data.getStringExtra("path"));
//		}
	}
	
	public void setImg(String path){
		Bitmap bmp=BitMap.getBitmap(path);
		if(bmp!=null){
			setimg(bmp);
			updateParentHeadImg(Base64.encode(BitMap.Bitmap2Bytes(bmp)));
		}else{
			Toast.makeText(getActivity(), "头像修改失败", 0).show();
		}
	}
//	/**
//     * 图片按比例大小压缩方法（根据Bitmap图片压缩）：
//     * @param image
//     * @return
//     */
//    private Bitmap comp(Bitmap image) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();        
//        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
//        if( baos.toByteArray().length / 1024>1024) {//判断如果图片大于100kb,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出    
//            baos.reset();//重置baos即清空baos
//            image.compress(Bitmap.CompressFormat.PNG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
//        }
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
//        BitmapFactory.Options newOpts = new BitmapFactory.Options();
//        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
//        newOpts.inJustDecodeBounds = true;
//        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
//        newOpts.inJustDecodeBounds = false;
//        int w = newOpts.outWidth;
//        int h = newOpts.outHeight;
//        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
//        float hh = 800f;//这里设置高度为800f
//        float ww = 480f;//这里设置宽度为480f
//        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
//        int be = 1;//be=1表示不缩放
//        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
//            be = (int) (newOpts.outWidth / ww);
//        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
//            be = (int) (newOpts.outHeight / hh);
//        }
//        if (be <= 0)
//            be = 1;
//        newOpts.inSampleSize = be;//设置缩放比例
//        newOpts.inPreferredConfig = Config.RGB_565;//降低图片从ARGB888到RGB565
//        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
//        isBm = new ByteArrayInputStream(baos.toByteArray());
//        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
//        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
//    }
//    /**
//     * 质量压缩法：
//     * @param image
//     * @return
//     */
//    private Bitmap compressImage(Bitmap image) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        image.compress(Bitmap.CompressFormat.PNG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//        int options = 100;
//        while ( baos.toByteArray().length>100 && baos.toByteArray().length / 1024>100) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩        
//            baos.reset();//重置baos即清空baos
//            options -= 10;//每次都减少10
//            image.compress(Bitmap.CompressFormat.PNG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
//        }
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
//        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
//        return bitmap;
//    }
	private void setimg(Bitmap bmp2) {
		CustomImageView headimg=(CustomImageView)view.findViewById(R.id.head);
		headimg.setImageBitmap(bmp2);
	}
	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.logoff:

			final Dialog dialog = new Dialog(getActivity(), R.style.MyDialog);   
			dialog.setCanceledOnTouchOutside(true);
			View view=(View)getActivity().getLayoutInflater().inflate(R.layout.dialogmakesure, null);
			view.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MainActivity.data=null;
					Userinfo.logoff(getActivity());
					Userinfo.logoffchild(getActivity());
					Intent  intent=new Intent(getActivity(),Login.class);
					startActivity(intent);
					((ExampleApplication)getActivity().getApplication()).setAlias("0");
					MainActivity.activity.finish();
					getActivity().finish();
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
			intent=new Intent(getActivity(),Changepassword.class);
			startActivity(intent);
			break;
		case R.id.parentmange:
			canManage();
			break;
		case R.id.childinfo:
			intent.setClass(getActivity(), Childinfo.class);
			startActivity(intent);
			break;
		case R.id.schoolinfo:
			intent.setClass(getActivity(), Schoolinfo.class);
			startActivity(intent);
			break;
		case R.id.aboutour:
			intent.setClass(getActivity(), Aboutour.class);
			startActivity(intent);
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
					MainActivity.data=(com.lester.parent.me.Parentinfodata.data) msg.obj;
					setinfo(MainActivity.data);
					break;
				case Constants.CANMANAGE:
					int mainAgent= (Integer) msg.obj;
					if(mainAgent==1){
						System.out.println("有权");
						Intent intent =new Intent();
						intent.setClass(getActivity(), ParentManage.class);
						startActivity(intent);
					}else{
						System.out.println("无权");
						DialogUtil.createBaseDialog(getActivity(), "提示", true,  "您没有管理权限", new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								DialogUtil.dismiss();
							}
						} );
					}
					break;
				case Constants.UPDATAHEAD_PARENT:
//					userhead.savehead(bmp,Userinfo.getmobile_phone(Me.this));保存头像
					String headurl=(String) msg.obj;
					Userinfo.setparentphoto(getActivity(), headurl);
					MainActivity.data=null;
					Toast.makeText(getActivity().getApplicationContext(), "头像修改成功", 0).show();
					break;
				case 404:
					DialogUtil.createBaseDialog(getActivity(), "提示", true,  msg.obj.toString(), new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							DialogUtil.dismiss();
						}
					} );
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
	private void updateParentHeadImg(final String provePhoto) {
		if(Net.isNetworkAvailable(getActivity())){
			handler.sendMessage(handler.obtainMessage(406));
			return;
		}
		lldialog=LodingDialog.DialogFactor(getActivity(), "正在提交数据", false);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "parentService";
	            String method = "updateParentHeadImg";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = 
	            		"{loginId:'"+Userinfo.getmobile_phone(getActivity())+
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
	 * 是否可以管理家长
	 * @param data
	 */
	private void canManage() {
		if(Net.isNetworkAvailable(getActivity())){
			return;
		}
		lldialog=LodingDialog.DialogFactor(getActivity(), "正在获取数据", false);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "subParentService";
	            String method = "isSubParent";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = "{studentId:'"+Userinfo.getchildid(getActivity())+"',"+
	            				"parentId:'"+Userinfo.getid(getActivity())+"'}";
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
	    				boolean  mainAgent = jsonObj.getJSONObject("data").getBoolean("mainAgent");
	    				int log=0;
	    				if(mainAgent){
	    					log=1;
	    				}else{
	    					log=0;
	    				}
//	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<ArrayList<ParentData>>(){}.getType());
	    				handler.sendMessage(handler.obtainMessage(Constants.CANMANAGE, log));
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
	/**
	 * 获取家长信息
	 */
	private void getParentInfo() {
		if(Net.isNetworkAvailable(getActivity())){
			handler.sendMessage(handler.obtainMessage(406));
			return;
		}
		lldialog=LodingDialog.DialogFactor(getActivity(), "正在获取数据", false);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "parentService";
	            String method = "getByLoginId";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = 
	            		"{loginId:'"+Userinfo.getmobile_phone(getActivity())+"'}";
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
