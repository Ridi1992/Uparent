package com.lester.uparent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.dream.framework.utils.dialog.LodingDialog;
import com.lester.school.requst.Constants;
import com.lester.school.requst.PublicRequest;
import com.lester.uparent.R;
import com.lester.uteacher.tool.MD5;

public class GetPassWord extends Activity implements OnClickListener{
	private final String PHONE = "^(((13[0-9])|(15([0-9]))|(18[0-9])|(17[0-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$";
	private final String EMAIL = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	private EditText mPhoneNum;
	private Handler mHandler = new Handler();// 全局handler
	private static int s = 60;
	private EditText mPassWord;
	private EditText mPassWord2;
	private LodingDialog lldialog;
	private String phone;
	
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
	// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	
		setContentView(R.layout.getpassword);
		phone=getIntent().getStringExtra("phone");
		intview();
	
	}
private void intview() {
	// TODO 自动生成的方法存根
	mPhoneNum=(EditText)findViewById(R.id.mPhoneNum);
	TextView back=(TextView)findViewById(R.id.back);
	back.setOnClickListener(this);
	TextView register=(TextView)findViewById(R.id.getcode);//获取验证码
	register.setOnClickListener(this);
	findViewById(R.id.makesure).setOnClickListener(this);
	mPassWord = (EditText) findViewById(R.id.register_password);
	mPassWord2 = (EditText) findViewById(R.id.register_password2);
}
@Override
public void onClick(View v) {
	// TODO 自动生成的方法存根
	Intent intent;
	switch (v.getId()) {
	case R.id.back:
		finish();
	break;
	
	case R.id.makesure://法律声明
	if (mPassWord.getText().toString() == null
			|| mPassWord.getText().toString().equals("")) {
		Toast.makeText(this, "请输入密码", Toast.LENGTH_LONG)
				.show();
	} else if (mPassWord2.getText().toString() == null
			|| mPassWord2.getText().toString().equals("")) {
		Toast.makeText(this, "请确认密码", Toast.LENGTH_LONG)
				.show();
	} else if (!mPassWord.getText().toString()
			.equals(mPassWord2.getText().toString())) {
		Toast.makeText(this, "两次密码不一致，请重新输入",
				Toast.LENGTH_LONG).show();
	} else {
		lldialog=LodingDialog.DialogFactor(GetPassWord.this, "正在重置密码", true);
		PublicRequest.getInstance(handler).getpassword(Constants.GETPASSWORD,
				phone, 
				MD5.MD5(mPassWord.getText().toString()));
	
	}
	break;

	}
	}
Handler handler=new Handler(){
	public void handleMessage(android.os.Message msg) {
		try {
		
			if(lldialog!=null){
				lldialog.dismiss();
		}
		switch (msg.what) {
		case Constants.GETPASSWORD:
			if(msg.obj.toString().equals("密码修改成功")){
				Toast.makeText(getApplicationContext(), "密码修改成功", 0).show();
				finish();
			}else{
				Toast.makeText(getApplicationContext(), "密码修改失败!", 0).show();
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
}
