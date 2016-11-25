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
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.dream.framework.utils.dialog.LodingDialog;
import com.google.zxing.WriterException;
import com.lester.company.http.HttpURL;
import com.lester.headview.CustomImageView;
import com.lester.school.loader.ImageLoader;
import com.lester.school.requst.Constants;
import com.lester.school.requst.PublicRequest;
import com.lester.uparent.R;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.Fixpic;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.lester.uteacher.tool.Usercode;
import com.lester.uteacher.tool.Userhead;
import com.lester.uteacher.tool.Userinfo;
import com.zxing.activity.CaptureActivity;
import com.zxing.encoding.EncodingHandler;


public class GenerateCode extends Activity {
	private TextView resultTextView;
	private ImageView qrImgImageView;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.generatecode);
        setinfo();
//        isSubParent();
        getdata();
        qrImgImageView = (ImageView) this.findViewById(R.id.iv_qr_image);
        findViewById(R.id.back).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
    }
    private void setinfo() {
    	String gender=Userinfo.getchildidsex(GenerateCode.this);
    	if(gender.equals("F")){
			((ImageView)findViewById(R.id.sex)).setImageResource(R.drawable.nv);
		}else{
			((ImageView)findViewById(R.id.sex)).setImageResource(R.drawable.nan);
		}
		Texttool.setText(GenerateCode.this, R.id.childname, Userinfo.getchildname(GenerateCode.this));
		sethead();
    }
    private void sethead() {
    	CustomImageView headimg=(CustomImageView)findViewById(R.id.childhead);
//    	if(Userhead.getchildhead(Userinfo.getchildid(GenerateCode.this))!=null){//先从本地获取头像
//    		headimg.setBitmap(Userhead.getchildhead(Userinfo.getchildid(GenerateCode.this)));
//    	}else{
    		String head=Userinfo.getchildphoto(GenerateCode.this);
    		if(head!=null){
    			new ImageLoader(this).DisplayImage(head, headimg);
    			 }
//    	}
    	
    }
	private void getdata() {
		// TODO Auto-generated method stub
    	lldialog=LodingDialog.DialogFactor(GenerateCode.this, "正在获取数据", true);
		PublicRequest.getInstance(handler).getcode(Constants.GETCODE,
				Userinfo.getmobile_phone(GenerateCode.this),
				Userinfo.getchildid(GenerateCode.this)
				);
	}
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			try {
				if(lldialog!=null){
					lldialog.dismiss();
			}
			switch (msg.what) {
			case Constants.GETCODE:
				String code=(String) msg.obj;
				if(code!=null){
					Bitmap qrCodeBitmap = EncodingHandler.createQRCode(code, 500);
					qrImgImageView.setImageBitmap(qrCodeBitmap);
					 Fixpic fix=new Fixpic();
					 fix.setView_W_H(getWindowManager(),qrImgImageView,qrCodeBitmap);
					Usercode usercode=new Usercode();//保存图片
					usercode.saveQrCodePicture(GenerateCode.this,qrCodeBitmap);
					 setScreenBrightness(255);
				}else{
					Toast.makeText(getApplicationContext(), "二维码生成失败", 0).show();
				}
				break;

			case 404:
				Toast.makeText(getApplicationContext(), msg.obj.toString(), 0).show();
				break;
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		};
	};
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if (requestCode==0&&resultCode==RESULT_OK) {
			String str=data.getStringExtra("result");
			if (!str.equals("")) {
				resultTextView.setText(str);
			}
		}
    
    }
	//验证是否有权限
			public void isSubParent() {
				if(Net.isNetworkAvailable(getApplicationContext())){
					return;
				}
				lldialog=LodingDialog.DialogFactor(GenerateCode.this, "正在检查权限", false);
				new Thread(new Runnable() {
			        public void run() {
			            String service = "subParentService";
			            String method = "isSubParent";
			            String token = MD5.MD5(service+method+Constants.Key);
			            String params = 
			            		"{studentId:'"+Userinfo.getchildid(GenerateCode.this)+
			            		"',parentId:'"+Userinfo.getid(GenerateCode.this)+
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
			    				String jsonData = jsonObj.getString("data");
			    				JSONObject jsonObject = new JSONObject(jsonData);
			    				final Boolean isReceive=jsonObject.getBoolean("isReceive");
			    				runOnUiThread(new Runnable() {
									public void run() {
										if(lldialog!=null){
											lldialog.dismiss();
										}
										if(isReceive){
											getdata();
										}else{
											Toast.makeText(getApplicationContext(), "您没有接送权限", 0).show();
//											finish();
										}
									}
								});
//			    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<ArrayList<ParentData>>(){}.getType());
//			    				handler.sendMessage(handler.obtainMessage(Constants.GETPANRENTLIST, object));
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
	 * 设置屏幕亮度
	 * @param paramInt
	 */
	private void setScreenBrightness(int paramInt){
		Window localWindow = getWindow();
		WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
		float f = paramInt / 255.0F;
		localLayoutParams.screenBrightness = f;
		localWindow.setAttributes(localLayoutParams);
		}
}