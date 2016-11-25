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
import com.lester.school.requst.Constants;
import com.lester.school.requst.JsonUtil;
import com.lester.school.requst.PublicRequest;
import com.lester.uparent.authorize.Authordetaildata;
import com.lester.uparent.teacherlist.Teacherdata;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.BitMap;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.lester.uteacher.tool.Timetool;
import com.lester.uteacher.tool.Usercode;
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

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Administrator
 *请假
 */
public class AuthorApply extends Activity implements
 OnClickListener{
	private Tencent mTencent;
	private String   AppId="1104828184";
	private IWXAPI api;
	private static final int THUMB_SIZE = 150;
	
	public static AuthorApply authorApply;
	private final int GETDATE=100; 
	private int id;
	private LodingDialog lldialog;
	private String  agentTime,
					agentName,
					agentIdCard,
					agentphone;
	private Usercode usercode=new Usercode();//保存图片;
	private Boolean havenew=false;
	
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
		setContentView(R.layout.authorapply);
		mTencent = Tencent.createInstance(AppId, this.getApplicationContext());
		
		
		api = WXAPIFactory.createWXAPI(this,"wxe73e0c30a15e4a64", true);
		api.registerApp("wxe73e0c30a15e4a64");  
		//应用签名：2324f7af44057e71a78e3515621fada2,微信分享平台注册时的签名，全部为小写，去掉“：”
		
		
		
		authorApply=AuthorApply.this;
		initView();
	}

	private void initView() {
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.starttime).setOnClickListener(this);
		findViewById(R.id.save).setOnClickListener(this);
	}
@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
	// TODO Auto-generated method stub
	if (keyCode == KeyEvent.KEYCODE_BACK ) {  
		if(havenew){
			Authorize.authorize.havanewdata();
		}
		finish();
        return true;  
    } 
	return super.onKeyDown(keyCode, event);
}
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.starttime:
			intent = new Intent(AuthorApply.this,GetDate.class);
			startActivityForResult(intent, GETDATE);
			break;
		case R.id.back:
			if(havenew){
				Authorize.authorize.havanewdata();
			}
			finish();
			break;
		case R.id.save:
			if(havenew){
				Toast.makeText(getApplicationContext(), "无需反复提交", 0).show();
				return;
			}
			TextView agentname=(TextView) findViewById(R.id.agentname);
			TextView agentid=(TextView) findViewById(R.id.agentid);
			TextView time=(TextView) findViewById(R.id.starttime);
			TextView agentphonet=(TextView) findViewById(R.id.agentphone);
			
			agentName=Texttool.Gettext(agentname);
			agentIdCard=Texttool.Gettext(agentid);
			agentTime=Texttool.Gettext(time);
			agentphone=Texttool.Gettext(agentphonet);
			if(!Texttool.Havecontent(AuthorApply.this, R.id.agentname)){
				Toast.makeText(getApplicationContext(), "代理人不能为空", 0).show();
				return;
			}
			if(!Texttool.Havecontent(AuthorApply.this, R.id.agentid)){
				Toast.makeText(getApplicationContext(), "代理人身份证不能为空", 0).show();
				return;
			}
			if(!Texttool.Patternidcard(agentIdCard)){
				Toast.makeText(getApplicationContext(), "请输入正确的身份证号", 0).show();
				return;
			}
			if(!Texttool.Havecontent(AuthorApply.this, R.id.agentphone)){
				Toast.makeText(getApplicationContext(), "代理人电话不能为空", 0).show();
				return;
			}
			if(!Texttool.Pattern_phone(agentphone)){
				Toast.makeText(getApplicationContext(), "请输入正确的电话号码", 0).show();
				return;
			}
			if(!Texttool.Havecontent(AuthorApply.this, R.id.starttime)){
				Toast.makeText(getApplicationContext(), "预计接送日期不能为空", 0).show();
				return;
			}
			
			if(Timetool.getTime1(agentTime)-Timetool.getTime1(Timetool.getnowtime().substring(0,10))<0){
				Toast.makeText(getApplicationContext(), "请输入正确的接送时间", 0).show();
				return;
			}
			
			if(agentName.length()>50){
				Toast.makeText(getApplicationContext(), "代理人姓名长度不能大于50，当前长度"+agentName.length(), 0).show();
				return;
			}
			send();
			
			
			break;
		}
	}


	public void  settime(String time){
		Texttool.setText(AuthorApply.this, R.id.starttime, time);
	}
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			try {
				if(lldialog!=null){
					lldialog.dismiss();
				}
				switch (msg.what) {
				case Constants.APPLYSAVE:
					havenew=true;
					Authordetaildata.data data=(com.lester.uparent.authorize.Authordetaildata.data) msg.obj;
					Bitmap qrCodeBitmap = EncodingHandler.createQRCode(data.getContent(), 350);
					usercode.saveQrCodePicture(AuthorApply.this,qrCodeBitmap);
					showDialog(qrCodeBitmap,data.getAgentTime());
					break;
				case 404:
					Toast.makeText(getApplicationContext(), msg.obj.toString(), 0).show();
					break;
				}
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}

		private void showDialog(Bitmap qrCodeBitmap,String time) {

			final Dialog dialog = new Dialog(AuthorApply.this, R.style.MyDialog);   
			dialog.setCanceledOnTouchOutside(true);
			View view1=(View)getLayoutInflater().inflate(R.layout.dialogcode, null);
			TextView hint=(TextView) view1.findViewById(R.id.hint);
			hint.setText("二维码仅限"+time+"当天有效!");
			ImageView imageview=(ImageView) view1.findViewById(R.id.iv_qr_image);
			imageview.setImageBitmap(qrCodeBitmap);
			view1.findViewById(R.id.share).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					Showdialog();
					
					dialog.dismiss();
				}
			});
				dialog.setContentView(view1);
				dialog.show();
		};
	};
	private void Showdialog() {
		final Dialog dialog = new Dialog(AuthorApply.this, R.style.MyDialog);   
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
	private void onClickShareToQQ() {
		System.out.println("图片地址="+usercode.getCodePath());
		 Bundle params = new Bundle();
		    params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,usercode.getCodePath());
		    params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "优管家（家长端）");
		    params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
		    params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
		    mTencent.shareToQQ(AuthorApply.this, params, new BaseUiListener());
		
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
	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 保存
	 */
	private void send() {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		lldialog=LodingDialog.DialogFactor(AuthorApply.this, "正在提交数据", true);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "agentOrderService";
	            String method = "save";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = 
	            		"{studentId:'"+Userinfo.getchildid(AuthorApply.this)+
	            		"',loginId:'"+Userinfo.getmobile_phone(AuthorApply.this)+
	            		"',token:'"+token+
	            		"',agentTime:'"+ agentTime+
	            		"',agentName:'"+agentName+
	            		"',agentIdCard:'"+agentIdCard+
	            		"',agentPhone:'"+agentphone+
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
	    				handler.sendMessage(handler.obtainMessage(Constants.APPLYSAVE, object));
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
