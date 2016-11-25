package com.lester.uparent;

import java.util.HashMap;
import java.util.Random;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.CommonDialog;
import cn.smssdk.gui.RegisterPage;

import com.baidu.mobstat.StatService;
import com.dream.framework.utils.dialog.LodingDialog;
import com.lester.school.requst.Constants;
import com.lester.school.requst.PublicRequest;
import com.lester.uparent.R;
import com.lester.uparent.login.logindata;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Userinfo;

public class Login extends Activity implements OnClickListener, Callback{
	private LodingDialog lldialog;
	private Dialog pd;
	// 短信注册，随机产生头像
		private static final String[] AVATARS = {
			"http://tupian.qqjay.com/u/2011/0729/e755c434c91fed9f6f73152731788cb3.jpg",
			"http://99touxiang.com/public/upload/nvsheng/125/27-011820_433.jpg",
			"http://img1.touxiang.cn/uploads/allimg/111029/2330264224-36.png",
			"http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339485237265.jpg",
			"http://diy.qqjay.com/u/files/2012/0523/f466c38e1c6c99ee2d6cd7746207a97a.jpg",
			"http://img1.touxiang.cn/uploads/20121224/24-054837_708.jpg",
			"http://img1.touxiang.cn/uploads/20121212/12-060125_658.jpg",
			"http://img1.touxiang.cn/uploads/20130608/08-054059_703.jpg",
			"http://diy.qqjay.com/u2/2013/0422/fadc08459b1ef5fc1ea6b5b8d22e44b4.jpg",
			"http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339510584349.jpg",
			"http://img1.touxiang.cn/uploads/20130515/15-080722_514.jpg",
			"http://diy.qqjay.com/u2/2013/0401/4355c29b30d295b26da6f242a65bcaad.jpg"
		};
		
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		if(Userinfo.islogin(Login.this)){
			Intent intent= new Intent ();
			//未选择小孩则进入选小孩界面
			if(Userinfo.getchildclassid(Login.this)==null||Userinfo.getchildclassid(Login.this).equals("")){
				intent.setClass(getApplicationContext(), SelectMyChild.class);
				startActivity(intent);
				finish();
			}else{
				intent.setClass(getApplicationContext(), MainActivity.class);
				startActivity(intent);
				finish();
			}
		}
			setContentView(R.layout.login);
			findViewById(R.id.ok).setOnClickListener(this);
			findViewById(R.id.zhuce).setOnClickListener(this);
			findViewById(R.id.getpassword).setOnClickListener(this);
			initSDK();
	}

	@Override
	public void onClick(View v) {
		Intent intent =new Intent();
		switch (v.getId()) {
		case R.id.ok:
//			intent.setClass(getApplicationContext(), MainActivity.class);
//			startActivity(intent);
//			finish();
			String loginId,password;
			if(!Texttool.Havecontent((TextView) findViewById(R.id.name))){
				Toast.makeText(getApplicationContext(), "账号不能为空", 0).show();
				break;
				}else{
					loginId=((TextView) findViewById(R.id.name)).getText().toString();
				}
			if(!Texttool.Havecontent((TextView) findViewById(R.id.password))){
				Toast.makeText(getApplicationContext(), "密码不能为空", 0).show();
				break;
				}else{
					password=((TextView) findViewById(R.id.password)).getText().toString();
					password=MD5.MD5(password);
				}
			lldialog=LodingDialog.DialogFactor(Login.this, "正在登录", true);
			PublicRequest.getInstance(handler).login(Constants.LOGIN,
					loginId, 
					password);
			break;

		case R.id.zhuce:
			intent.setClass(getApplicationContext(), Register.class);
			startActivity(intent);
			break;
		case R.id.getpassword:
			
			com.lester.uparent.RegisterPage page = new com.lester.uparent.RegisterPage();
			page.show(getApplicationContext());

			break;
		}
	}
	private void initSDK() {
		// 初始化短信SDK
		SMSSDK.initSDK(this, "9cc03e02a296", "6f304eff08982e0a078bcba21068518d");
		final Handler handler = new Handler(this);
		EventHandler eventHandler = new EventHandler() {
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
			}
		};
		// 注册回调监听接口
		SMSSDK.registerEventHandler(eventHandler);
//		ready = true;
		// 获取新好友个数
//		showDialog();
//		SMSSDK.getNewFriendsCount();

	}
	//更新，新好友个数
		private void refreshViewCount(Object data){
			int newFriendsCount = 0;
			try {
				newFriendsCount = Integer.parseInt(String.valueOf(data));
			} catch (Throwable t) {
				newFriendsCount = 0;
			}
//			if(newFriendsCount > 0){
//				tvNum.setVisibility(View.VISIBLE);
//				tvNum.setText(String.valueOf(newFriendsCount));
//			}else{
//				tvNum.setVisibility(View.GONE);
//			}
			if (pd != null && pd.isShowing()) {
				pd.dismiss();
			}
		}
	//弹出加载框
		private void showDialog(){
			if (pd != null && pd.isShowing()) {
				pd.dismiss();
			}
			pd = CommonDialog.ProgressDialog(this);
			pd.show();
		}
	// 提交用户信息
		private void registerUser(String country, String phone) {
			Random rnd = new Random();
			int id = Math.abs(rnd.nextInt());
			String uid = String.valueOf(id);
			String nickName = "SmsSDK_User_" + uid;
			String avatar = AVATARS[id % 12];
			SMSSDK.submitUserInfo(uid, nickName, avatar, country, phone);
		}
		/**
		 * 验证界面
		 */
		private void getcode() {
			// TODO Auto-generated method stub
			RegisterPage registerPage = new RegisterPage();
			registerPage.setRegisterCallback(new EventHandler() {
				public void afterEvent(int event, int result, Object data) {
					// 解析注册结果
					if (result == SMSSDK.RESULT_COMPLETE) {
						@SuppressWarnings("unchecked")
						HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
						String country = (String) phoneMap.get("country");
						String phone = (String) phoneMap.get("phone");
						// 提交用户信息
						registerUser(country, phone);
					}
				}
			});
			registerPage.show(this);
		}
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			try {
			
				if(lldialog!=null){
					lldialog.dismiss();
			}
			switch (msg.what) {
			case Constants.LOGIN:
				logindata.data data=(com.lester.uparent.login.logindata.data) msg.obj;
					Userinfo.setinfo(Login.this,data);//保存用户信息
					Toast.makeText(getApplicationContext(), "登录成功", 0).show();
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(), SelectMyChild.class);
					startActivity(intent);
					finish();
				break;
			case 406:
				Toast.makeText(getApplicationContext(), "登录失败!"+msg.obj.toString(), 0).show();
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

	public boolean handleMessage(Message msg) {
	if (pd != null && pd.isShowing()) {
		pd.dismiss();
	}
	int event = msg.arg1;
	int result = msg.arg2;
	Object data = msg.obj;
	if (event == SMSSDK.EVENT_SUBMIT_USER_INFO) {
		// 短信注册成功后，返回MainActivity,然后提示新好友
		if (result == SMSSDK.RESULT_COMPLETE) {
			Toast.makeText(this, "验证成功，请输入新密码", Toast.LENGTH_SHORT).show();
		} else {
			((Throwable) data).printStackTrace();
		}
	} else if (event == SMSSDK.EVENT_GET_NEW_FRIENDS_COUNT){
		if (result == SMSSDK.RESULT_COMPLETE) {
			refreshViewCount(data);
		} else {
			((Throwable) data).printStackTrace();
		}
	}else{
//		Toast.makeText(this, "验证失败", Toast.LENGTH_SHORT).show();
	}
	return false;
}
}
