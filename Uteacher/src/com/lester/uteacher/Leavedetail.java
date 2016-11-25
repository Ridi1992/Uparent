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
import com.dream.framework.utils.dialog.LodingDialog;
import com.google.gson.reflect.TypeToken;
import com.lester.company.http.HttpURL;
import com.lester.school.requst.Constants;
import com.lester.school.requst.JsonUtil;
import com.lester.uteacher.goton.Classlist;
import com.lester.uteacher.leave.Leavelist;
import com.lester.uteacher.leave.Leavelist.datas;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.lester.uteacher.tool.Timetool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Toast;

public class Leavedetail extends Activity implements OnClickListener{
	private Leavelist.datas data;
	private LodingDialog lldialog;
	private Boolean hasread=false;
	private String handletime="";

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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.leavedetail);
		data=(datas) getIntent().getSerializableExtra("data");
		initView();
		if(data.getProcess_status().equals("1")){
			hasDone();
		}else{
			setInfo(null);
		}
	}

	private void setInfo(String time) {
		Texttool.setText(Leavedetail.this, R.id.starttime, data.getStart_time().substring(0, 16));
		Texttool.setText(Leavedetail.this, R.id.endtime, data.getEnd_time().substring(0, 16));
		Texttool.setText(Leavedetail.this, R.id.submittime, data.getSubmit_time().substring(0, 16));
		Texttool.setText(Leavedetail.this, R.id.name, data.getStudentName());
		Texttool.setText(Leavedetail.this, R.id.content, data.getContent());
		if(time == null && data.getHandle_time()!=null){
			Texttool.setText(Leavedetail.this, R.id.handletime, data.getHandle_time().substring(0,16));
		}else if(time!=null){
			Texttool.setText(Leavedetail.this, R.id.handletime, time.substring(0,16));
		}
	}

	private void initView() {
		findViewById(R.id.back).setOnClickListener(this);;
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			if(hasread){
				Intent intent = new Intent();
				intent.putExtra("time", handletime);
				setResult(101,intent);
			}
			finish();
			break;

		default:
			break;
		}
		
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {  
			if(hasread){
				Intent intent = new Intent();
				intent.putExtra("time", handletime);
				setResult(101,intent);
			}
			finish();
            return true;  
        } else  
            return super.onKeyDown(keyCode, event); 
	};
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			try {
				if(lldialog!=null && msg.what!=Constants.GETCLASS){//获取班级列表后直接请求学生数据，所以此处不取消弹框
					lldialog.dismiss();
			}
			switch (msg.what) {
			case Constants.READLEAVE:
				hasread=true;
				com.lester.uteacher.Leavedetail.data data=(com.lester.uteacher.Leavedetail.data) msg.obj;
				handletime=data.getHandle_time();
				setInfo(data.getHandle_time());
				LeaveList.activity.refresh(handletime);//已读之后变状态
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
	 * 将消息标记为已读
	 */
	private void hasDone() {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		lldialog=LodingDialog.DialogFactor(Leavedetail.this, "", true);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "leaveRecordService";
	            String method = "hasDone";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = "{id:'"+data.getId()+"'}";
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
	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<com.lester.uteacher.Leavedetail.data>(){}.getType());
	    				handler.sendMessage(handler.obtainMessage(Constants.READLEAVE, object));
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
	public class data {
		private String student_id;
		private String school_id;
		private String version;
		private String content;
		private String id;
		private String handle_time;
		private String end_time;
		private String process_status;
		private String start_time;
		private String teacher_id;
		private String submit_time;
		private String parent_id;
		/**
		 * @return the student_id
		 */
		public String getStudent_id() {
			return student_id;
		}
		/**
		 * @param student_id the student_id to set
		 */
		public void setStudent_id(String student_id) {
			this.student_id = student_id;
		}
		/**
		 * @return the school_id
		 */
		public String getSchool_id() {
			return school_id;
		}
		/**
		 * @param school_id the school_id to set
		 */
		public void setSchool_id(String school_id) {
			this.school_id = school_id;
		}
		/**
		 * @return the version
		 */
		public String getVersion() {
			return version;
		}
		/**
		 * @param version the version to set
		 */
		public void setVersion(String version) {
			this.version = version;
		}
		/**
		 * @return the content
		 */
		public String getContent() {
			return content;
		}
		/**
		 * @param content the content to set
		 */
		public void setContent(String content) {
			this.content = content;
		}
		/**
		 * @return the id
		 */
		public String getId() {
			return id;
		}
		/**
		 * @param id the id to set
		 */
		public void setId(String id) {
			this.id = id;
		}
		/**
		 * @return the handle_time
		 */
		public String getHandle_time() {
			return handle_time;
		}
		/**
		 * @param handle_time the handle_time to set
		 */
		public void setHandle_time(String handle_time) {
			this.handle_time = handle_time;
		}
		/**
		 * @return the end_time
		 */
		public String getEnd_time() {
			return end_time;
		}
		/**
		 * @param end_time the end_time to set
		 */
		public void setEnd_time(String end_time) {
			this.end_time = end_time;
		}
		/**
		 * @return the process_status
		 */
		public String getProcess_status() {
			return process_status;
		}
		/**
		 * @param process_status the process_status to set
		 */
		public void setProcess_status(String process_status) {
			this.process_status = process_status;
		}
		/**
		 * @return the start_time
		 */
		public String getStart_time() {
			return start_time;
		}
		/**
		 * @param start_time the start_time to set
		 */
		public void setStart_time(String start_time) {
			this.start_time = start_time;
		}
		/**
		 * @return the teacher_id
		 */
		public String getTeacher_id() {
			return teacher_id;
		}
		/**
		 * @param teacher_id the teacher_id to set
		 */
		public void setTeacher_id(String teacher_id) {
			this.teacher_id = teacher_id;
		}
		/**
		 * @return the submit_time
		 */
		public String getSubmit_time() {
			return submit_time;
		}
		/**
		 * @param submit_time the submit_time to set
		 */
		public void setSubmit_time(String submit_time) {
			this.submit_time = submit_time;
		}
		/**
		 * @return the parent_id
		 */
		public String getParent_id() {
			return parent_id;
		}
		/**
		 * @param parent_id the parent_id to set
		 */
		public void setParent_id(String parent_id) {
			this.parent_id = parent_id;
		}

	}
}
