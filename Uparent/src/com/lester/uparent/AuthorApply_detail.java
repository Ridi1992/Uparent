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
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.dream.framework.utils.dialog.LodingDialog;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.WriterException;
import com.lester.company.http.HttpURL;
import com.lester.school.requst.Constants;
import com.lester.school.requst.JsonUtil;
import com.lester.uparent.authorize.Authordetaildata;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.BitMap;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.lester.uteacher.tool.Usercode;
import com.lester.uteacher.tool.Userhead;
import com.lester.uteacher.tool.Userinfo;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.platformtools.Util;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zxing.encoding.EncodingHandler;


/**
 * @author Administrator
 */
public class AuthorApply_detail extends Activity implements
 OnClickListener{

	private Tencent mTencent;
	private String   AppId="1104828184";
	private Usercode usercode=new Usercode();//保存图片;
	private final int GETDATE=100; 
	private int id;
	private LodingDialog lldialog;
	private String  agentOrderId,agentPhone;
	private Authordetaildata.data data;
	private ImageView qrImgImageView;
	private IWXAPI api;
	private static final int THUMB_SIZE = 150;
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
		setContentView(R.layout.authorapply_detail);
		agentOrderId=getIntent().getStringExtra("agentOrderId");
		agentPhone=getIntent().getStringExtra("agentPhone");
		
		mTencent = Tencent.createInstance(AppId, this.getApplicationContext());
		
		
		api = WXAPIFactory.createWXAPI(this,"wxe73e0c30a15e4a64", true);
		api.registerApp("wxe73e0c30a15e4a64");  
		//应用签名：2324f7af44057e71a78e3515621fada2,微信分享平台注册时的签名，全部为小写，去掉“：”
		
		initView();
		getdatail();
	}

	private void initView() {
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.share).setOnClickListener(this);
		qrImgImageView = (ImageView) this.findViewById(R.id.iv_qr_image);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.share:
			
			Showdialog();
			
			
		
			break;
		}
	}

	
	
	private void Showdialog() {
		final Dialog dialog = new Dialog(AuthorApply_detail.this, R.style.MyDialog);   
		dialog.setCanceledOnTouchOutside(true);
		View view=(View)getLayoutInflater().inflate(R.layout.shareqqandwchat, null);
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		view.findViewById(R.id.wechat).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				shareToWechat();
				//启动
				dialog.dismiss();
			}
		});
		view.findViewById(R.id.qq).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickShareToQQ();
				dialog.dismiss();
			}
		});
		
			dialog.setContentView(view);
			dialog.show();
		
	}

	/**
	 * 微信分享
	 */
	private void shareToWechat(){
//		
		Bitmap bmp = BitMap.getBitmap(usercode.getCodePath());
		WXImageObject imgObj = new WXImageObject(bmp);
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = imgObj;
		Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
		bmp.recycle();
		msg.thumbData = Util.bmpToByteArray(thumbBmp, true);  // 设置缩略图
//		msg.description=Userinfo.getname(AuthorApply_detail.this)+"委托您接"+
//		        Userinfo.getchildname(AuthorApply_detail.this)+"放学";
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("img");
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneSession;
		api.sendReq(req);
		
	}
	private void setinfo() throws WriterException {
		if(data.getContent()!=null){
			Bitmap qrCodeBitmap = EncodingHandler.createQRCode(data.getContent(), 350);
			qrImgImageView.setImageBitmap(qrCodeBitmap);
			usercode.saveQrCodePicture(AuthorApply_detail.this,qrCodeBitmap);
			Texttool.setText(AuthorApply_detail.this,R.id.studentname,data.getStudentName());
			Texttool.setText(AuthorApply_detail.this,R.id.agentname,data.getAgentName());
			Texttool.setText(AuthorApply_detail.this,R.id.agentid,data.getAgentIdCard());
			Texttool.setText(AuthorApply_detail.this,R.id.agenttime,data.getAgentTime());
			Texttool.setText(AuthorApply_detail.this,R.id.agentPhone,agentPhone);
			Texttool.setText(AuthorApply_detail.this,R.id.hint,"二维码仅限"+data.getAgentTime()+"当天有效!");
		}else{
			Toast.makeText(getApplicationContext(), "获取数据失败", 0).show();
		}
		
	};
	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			try {
				if(lldialog!=null){
					lldialog.dismiss();
				}
				switch (msg.what) {
				case Constants.GETAUTHORDETAIL:
					data=(com.lester.uparent.authorize.Authordetaildata.data) msg.obj;
					if(data!=null){
						setinfo();
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
	 * 保存
	 */
	private void getdatail() {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		lldialog=LodingDialog.DialogFactor(AuthorApply_detail.this, "正在获取数据", true);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "agentOrderService";
	            String method = "getById";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = 
	            		"{agentOrderId:'"+agentOrderId+
	            		"',loginId:'"+Userinfo.getmobile_phone(AuthorApply_detail.this)+
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
	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<Authordetaildata.data>(){}.getType());
	    				handler.sendMessage(handler.obtainMessage(Constants.GETAUTHORDETAIL, object));
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
	
	
	
	//以下为qq分享内容
	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	mTencent.onActivityResult(requestCode, resultCode, data);//qq回调用
	}
	
	
	private void onClickShareToQQ() {
		System.out.println("图片地址="+usercode.getCodePath());
		 Bundle params = new Bundle();
		    params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,usercode.getCodePath());
		    params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "优管家（家长端）");
		    params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
		    params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
		    mTencent.shareToQQ(AuthorApply_detail.this, params, new BaseUiListener());
		
	}

	private class BaseUiListener implements IUiListener {
		@Override
		public void onError(UiError e) {
			System.out.println("错误"+"code:" + e.errorCode + ", msg:"
					+ e.errorMessage + ", detail:" + e.errorDetail);
			}
		@Override
		public void onCancel() {
			System.out.println("取消");
			}
		@Override
		public void onComplete(Object arg0) {
			System.out.println("完成");
			}
		}
	
}
