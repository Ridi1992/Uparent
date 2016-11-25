package com.lester.uparent;

import java.text.SimpleDateFormat;
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
import com.lester.uparent.teacherlist.Teacherdata;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.lester.uteacher.tool.Timetool;
import com.lester.uteacher.tool.Userinfo;
import com.ycf.blog_05_chinesechoosedemo.datedialog.DateTimePickerDialog;
import com.ycf.blog_05_chinesechoosedemo.datedialog.DateTimePickerDialog.OnDateTimeSetListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Administrator
 *请假
 */
public class LeaveApply extends Activity implements
 OnClickListener{

	private final int GETDATE=100; 
	private int id;
	private LodingDialog lldialog;
	private String starttime;
	private String endtime;
	private String content;
	
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
		setContentView(R.layout.leaveapply);
		initView();
	}

	private void initView() {
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.starttime).setOnClickListener(this);
		findViewById(R.id.endtime).setOnClickListener(this);
		findViewById(R.id.save).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.starttime:
			id=R.id.starttime;
			showDialog();
			break;
		case R.id.endtime:
			id=R.id.endtime;
			showDialog();
			break;
		case R.id.back:
			finish();
			break;
		case R.id.save:
			TextView te=(TextView) findViewById(R.id.starttime);
			TextView te1=(TextView) findViewById(R.id.endtime);
			TextView te2=(TextView) findViewById(R.id.content);
			starttime=Texttool.Gettext(te);
			endtime=Texttool.Gettext(te1);
			content=Texttool.Gettext(te2);
			if(!Texttool.Havecontent(LeaveApply.this, R.id.starttime)){
				Toast.makeText(getApplicationContext(), "开始日期不能为空", 0).show();
				return;
			}
			if(!Texttool.Havecontent(LeaveApply.this, R.id.endtime)){
				Toast.makeText(getApplicationContext(), "结束日期不能为空", 0).show();
				return;
			}
			if(!Texttool.Havecontent(LeaveApply.this, R.id.content)){
				Toast.makeText(getApplicationContext(), "请假原因不能为空", 0).show();
				return;
			}
			if(Timetool.getTime(endtime)-Timetool.getTime(starttime)<0 || 
					Timetool.getTime(starttime)-Timetool.getTime(Timetool.getnowtime())<0){
				Toast.makeText(getApplicationContext(), "请输入正确的请假时间", 0).show();
				return;
			}
			if(content.length()>50){
				Toast.makeText(getApplicationContext(), "请假原因不能超过50个字，当前字数为"+content.length(), 0).show();
				return;
			}
			send();
			break;
		}
	}
	
	public void showDialog()
	{
		DateTimePickerDialog dialog  = new DateTimePickerDialog(this, System.currentTimeMillis());
		dialog.setOnDateTimeSetListener(new OnDateTimeSetListener()
	      {
			public void OnDateTimeSet(AlertDialog dialog, long date)
			{
//				Toast.makeText(LeaveApply.this, "您输入的日期是："+getStringDate(date), Toast.LENGTH_LONG).show();
				Texttool.setText(LeaveApply.this,id, getStringDate(date));
			}
		});
		dialog.show();
	}
	/**
	* 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
	*
	*/
	public static String getStringDate(Long date) 
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(date);
		
		return dateString;
	}
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			try {
				if(lldialog!=null){
					lldialog.dismiss();
				}
				switch (msg.what) {
				case Constants.SENDLEAVE:
					Toast.makeText(getApplicationContext(), msg.obj.toString(), 0).show();
					LeaveList.leaveList.havenewdata();
					finish();
					break;
				case 404:
					Toast.makeText(getApplicationContext(), msg.obj.toString(), 0).show();
					break;
				}
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		};
	};
	
	/**
	 * 请假
	 */
	private void send() {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		lldialog=LodingDialog.DialogFactor(LeaveApply.this, "正在提交数据", false);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "leaveRecordService";
	            String method = "save";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = 
	            		"{studentId:'"+Userinfo.getchildid(LeaveApply.this)+
	            		"',loginId:'"+Userinfo.getmobile_phone(LeaveApply.this)+
	            		"',token:'"+token+
	            		"',startTime:'"+starttime+
	            		"',endTime:'"+endtime+
	            		"',content:'"+content+
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
//	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<ArrayList<Teacherdata.datas>>(){}.getType());
	    				handler.sendMessage(handler.obtainMessage(Constants.SENDLEAVE, "提交成功"));
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
