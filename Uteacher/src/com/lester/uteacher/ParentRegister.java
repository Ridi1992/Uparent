package com.lester.uteacher;

import java.io.IOException;
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
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.dream.framework.utils.dialog.LodingDialog;
import com.google.gson.reflect.TypeToken;
import com.lester.company.http.HttpURL;
import com.lester.school.loader.ImageLoader;
import com.lester.school.requst.Constants;
import com.lester.school.requst.JsonUtil;
import com.lester.uteacher.leave.Leavedata;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.Appinfo;
import com.lester.uteacher.tool.Base64;
import com.lester.uteacher.tool.BitMap;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.lester.uteacher.tool.PhotoPickUtil;
import com.lester.uteacher.tool.PhotoPickUtil.OnPhotoPickedlistener;
import com.lester.uteacher.tool.Userinfo;
import com.teacher.View.CircleImageView;
import com.teacher.data.ParentData;

/**
 * @author Administrator
 *异常接送，家长登记
 */
public class ParentRegister extends Activity implements OnClickListener,OnPhotoPickedlistener {
	private PhotoPickUtil photoPickUtil;
	private Bitmap bmp;
	private LodingDialog lldialog;
	private Leavedata.datas datas;
	private ArrayList<ParentData> data;///家长的信息
	
	
	

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
		setContentView(R.layout.parentregister);
		datas=(Leavedata.datas) getIntent().getSerializableExtra("datas");
		initview();
	}
	private void initview() {
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.camera).setOnClickListener(this);
		findViewById(R.id.makesure).setOnClickListener(this);
		findViewById(R.id.select).setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
	switch (v.getId()) {
	case R.id.back:
		finish();
		break;
	case R.id.select:
		if(data!=null){
			initPopWindow(R.id.select);
		}else{
			getparentlist();
		}
		break;
	case R.id.camera:
		photoPickUtil=new PhotoPickUtil(ParentRegister.this, this);
		photoPickUtil.doTakePhoto(false, 300, 300 );
		break;
	case R.id.makesure:
		String abnormalReason=((TextView) findViewById(R.id.abnormalReason)).getText().toString();
		String agentName=((TextView) findViewById(R.id.name)).getText().toString();
		String agentIdCard=((TextView) findViewById(R.id.idcard)).getText().toString();
		if(!Texttool.Havecontent(ParentRegister.this,R.id.name)){
			Toast.makeText(getApplicationContext(), "姓名不能为空", 0).show();
			return;
		}
		if(agentName.length()>50){
			Toast.makeText(getApplicationContext(), "姓名长度不能大于50，当前长度"+agentName.length(), 0).show();
			return;
		}
		if(!Texttool.Havecontent(ParentRegister.this,R.id.idcard)){
			Toast.makeText(getApplicationContext(), "身份证不能为空", 0).show();
			return;
		}
		String te=((TextView) findViewById(R.id.idcard)).getText().toString(); 
		if(!Texttool.Patternidcard(te)){
			Toast.makeText(getApplicationContext(), "身份证格式不正确", 0).show();
			return;
		}
		if(!Texttool.Havecontent(ParentRegister.this,R.id.abnormalReason)){
			Toast.makeText(getApplicationContext(), "原因不能为空", 0).show();
			return;
		}
		if(bmp==null){
			Toast.makeText(getApplicationContext(), "照片不能为空", 0).show();
			return;
		}
		if(abnormalReason.length()>50){
			Toast.makeText(getApplicationContext(), "异常原因长度不能大于50，当前长度"+abnormalReason.length(), 0).show();
			return;
		}
		String provePhoto = Base64.encode(BitMap.Bitmap2Bytes(bmp));
		
		outAbnormalService(
				abnormalReason,
				provePhoto,
				agentName,
				agentIdCard
				);
		break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		photoPickUtil.pickResult(requestCode, resultCode, data);
	}
	private void initPopWindow(final int id) {
		// 加载popupWindow的布局文件
		View contentView_pop = LayoutInflater.from(getApplicationContext())
				.inflate(R.layout.pop_list, null);
		// 设置popupWindow的背景颜色
		contentView_pop.setBackgroundResource(R.color.backhuise);
		// 声明一个弹出框
		@SuppressWarnings("deprecation")
		final PopupWindow popupWindow = new PopupWindow(
				findViewById(R.id.fenleilayout),getWindowManager().getDefaultDisplay().getWidth()/3, LayoutParams.WRAP_CONTENT);
		// 为弹出框设定自定义的布局
		popupWindow.setContentView(contentView_pop);
		final ListView mListView = (ListView) contentView_pop
				.findViewById(R.id.pop_listView);
		BaseAdapter adapter=new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				convertView=getLayoutInflater().inflate(R.layout.poplistitem, null);
				((TextView) convertView.findViewById(R.id.itemtext)).setText(data.get(position).getSubParentName());
				return convertView;
			}
			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}
			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				if(data!=null){
					return data.size();
				}
				return 0;
			}
		};
		mListView.setAdapter(adapter);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(false);
		popupWindow.setBackgroundDrawable(new ColorDrawable());
		popupWindow.showAsDropDown(findViewById(id));
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Texttool.setText(ParentRegister.this, R.id.idcard, data.get(arg2).getIdCard());
				Texttool.setText(ParentRegister.this, R.id.name, data.get(arg2).getSubParentName());
				CircleImageView parentimg=(CircleImageView) findViewById(R.id.parentphoto);
				String imgurl=data.get(arg2).getPhoto();
				if(imgurl!=null){
					ImageLoader imageLoader=new ImageLoader(ParentRegister.this);
					imageLoader.DisplayImage(imgurl, parentimg);
				}
				popupWindow.dismiss();
			}
		});
	}
	@Override
	public void photoPicked(String path, Bitmap bmp) {
		if(bmp!=null){
			this.bmp=bmp;
			setimg(bmp);
			System.out.println("图片");
		}else if(path!=null){
			System.out.println("路径");
			if(fitBitmap(path)!=null){
				this.bmp=fitBitmap(path);
				setimg(this.bmp);
			}
		}
		
	}
	private void setimg(Bitmap bmp) {
		ImageView img =(ImageView) findViewById(R.id.camera);
		img.setImageBitmap(bmp);
	}
	/**
	 * 改变图片分辨率,使图片刚好和手机屏幕适配
	 */
	private  Bitmap fitBitmap(String path){
			BitmapFactory.Options opts = new Options(); 
			// 不读取像素数组到内存中，仅读取图片的信息                 
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, opts); 
			// 从Options中获取图片的分辨率                 
			int imageHeight = opts.outHeight;  
			int imageWidth = opts.outWidth; 
			// 获取Android屏幕的服务                 
			WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE); 
			// 获取屏幕的分辨率，getHeight()、getWidth已经被废弃掉了                 
			// 应该使用getSize()，但是这里为了向下兼容所以依然使用它们                 
//			int windowHeight = wm.getDefaultDisplay().getHeight();  
//			int windowWidth = wm.getDefaultDisplay().getWidth();  
			int windowHeight = 480;  
			int windowWidth = 240;  
			// 计算采样率                 
			int scaleX = imageWidth / windowWidth; 
			Log.i("压缩图片", "imageWidth="+imageWidth);
			Log.i("压缩图片", "windowWidth="+windowWidth);
			int scaleY = imageHeight / windowHeight; 
			int scale = 1; 
			// 采样率依照最大的方向为准                 
			if (scaleX > scaleY && scaleY >= 1) { 
				scale = scaleX; 
				Log.i("压缩图片,采码率", "scale="+scale);
				}
				if (scaleX < scaleY && scaleX >= 1) 
				{ 
					scale = scaleY;
					Log.i("压缩图片,采码率", "scale="+scale);
				}
			// false表示读取图片像素数组到内存中，依照设定的采样率                 
			opts.inJustDecodeBounds = false; 
			// 采样率                 
			opts.inSampleSize = scale; 
			Bitmap bitmap = BitmapFactory.decodeFile(path, opts);
			return rotaingBitmap(readPictureDegree(path),bitmap);
	}
	
	/**
     * 读取图片属性：旋转的角度
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree  = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
    public static Bitmap rotaingBitmap(int angle , Bitmap bitmap) {  
        //旋转图片 动作   
        Matrix matrix = new Matrix();;  
        matrix.postRotate(angle);  
        System.out.println("angle2=" + angle);  
        // 创建新的图片   
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,  
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);  
        return resizedBitmap;  
    }
	Handler handler=new Handler(){
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			try {
				if(lldialog!=null && msg.what!=Constants.GETCLASS){//获取班级列表后直接请求学生数据，所以此处不取消弹框
					lldialog.dismiss();
			}
			switch (msg.what) {
			case Constants.GETSTUDENTSEND:
				if(msg.obj.toString().equals("提交成功")){
					Toast.makeText(getApplicationContext(), "提交成功", 0).show();
					Studentlogin.activity.refresh();
					finish();
				}
				break;
			case Constants.GETPANRENTLIST:
				data =(ArrayList<ParentData>) msg.obj;
				if(data!=null){
					if(data.size()==0){
						Toast.makeText(getApplicationContext(), "还未添加从家长", 0).show();
					}else{
						initPopWindow(R.id.select);
					}
				}else{
					Toast.makeText(getApplicationContext(), "还没有从家长", 0).show();
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
	 * 提交
	 */
	private void outAbnormalService(
			final String abnormalReason,
			final String provePhoto,
			final String agentName,
			final String agentIdCard
			) {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		lldialog=LodingDialog.DialogFactor(ParentRegister.this, "正在提交", true);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "outAbnormalService";
	            String method = "save";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = 
	            		"{loginId:'"+Userinfo.getmobile_phone(ParentRegister.this)+
	            		"',studentId:'"+datas.getId()+
	            		"',abnormalReason:'"+abnormalReason+
	            		"',provePhoto:'"+provePhoto+
	            		"',agentName:'"+agentName+
	            		"',agentIdCard:'"+agentIdCard+
	            		"'}";
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
//	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<List<Leavedata.datas>>(){}.getType());
	    				handler.sendMessage(handler.obtainMessage(Constants.GETSTUDENTSEND, "提交成功"));
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
	public void getparentlist() {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		lldialog=LodingDialog.DialogFactor(ParentRegister.this, "正在获取家长信息", false);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "subParentService";
	            String method = "searchAllParentByStudentId";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = 
	            		"{studentId:'"+datas.getId()+"'}";
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
	    				String jsonData = jsonObj.getString("datas");
	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<ArrayList<ParentData>>(){}.getType());
	    				handler.sendMessage(handler.obtainMessage(Constants.GETPANRENTLIST, object));
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
