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
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.dream.framework.utils.dialog.LodingDialog;
import com.google.gson.reflect.TypeToken;
import com.lester.company.http.HttpURL;
import com.lester.school.requst.Constants;
import com.lester.school.requst.JsonUtil;
import com.lester.uteacher.adapter.FeedBackAdapter;
import com.lester.uteacher.info.Teacherinfo;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.lester.uteacher.tool.Timetool;
import com.lester.uteacher.tool.Userinfo;
import com.teacher.data.FeedBackData;
import com.teacher.data.GetFeedbackdata;

public class Feedback extends Activity implements OnClickListener{
	private LodingDialog lldialog;
	private Userinfo userinfo=new Userinfo();
	private ListView listview;
	public FeedBackAdapter mAdapter;
	public ArrayList<FeedBackData> data;
	private String  studentId;
	public Boolean show;
	

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
		studentId=getIntent().getStringExtra("studentId");
		setContentView(R.layout.feedback);
		initview();
		addfootview();
		intData();
		setStudentname();
		getshow();
	}
	
	private void setStudentname(){
		String name=getIntent().getStringExtra("studentname");
		String classname=getIntent().getStringExtra("classname");
		Texttool.setText(Feedback.this, R.id.studentname, "姓名："+name);
		Texttool.setText(Feedback.this, R.id.classname, "班级："+classname);
	}

	private void getshow() {
		show=getIntent().getBooleanExtra("show", false);
		if(show){
			findViewById(R.id.studentname_ll).setVisibility(View.GONE);//展示的时候不用显示学生姓名和班级
			String id=getIntent().getStringExtra("id");
			findViewById(R.id.ok).setVisibility(View.GONE);
			findViewById(R.id.homework).setFocusable(false);
			getfeedbackdata(id);
		}else{
			setTitel(Timetool.getnowtime().substring(0,10));
			findViewById(R.id.ok).setVisibility(View.VISIBLE);
			findViewById(R.id.bar_ll).setVisibility(View.GONE);
			setHomeworkcanfocus(false);
			hasInputHomework(studentId);
		}
	}
	//设置标题
	private void setTitel(String titel){
		Texttool.setText(Feedback.this, R.id.bartitle, titel);
	}

	private void addfootview(){
		View view=getLayoutInflater().inflate(R.layout.button, null);
		view.findViewById(R.id.ok).setOnClickListener(this);
		listview.addFooterView(view);
		view.findViewById(R.id.homework).setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mAdapter.selectposition=0;
				return false;
			}
		});
	}
	
	private void intData() {
		data=new ArrayList<FeedBackData>();
		adddata("入园情况","入园时间","正常","迟到","","",false);
		adddata("","情况表现","愉快","普通","哭闹","沮丧",false);
		adddata("","招呼","主动","被动","不予理睬、没反应","",false);
		adddata("","特殊行为","","","","",true);
		
		adddata("用餐情况","食量","佳","普通","不佳","",false);
		adddata("","速度","佳","普通","不佳","",false);
		adddata("","情绪","愉快","普通","哭闹","不佳",false);
		adddata("","特殊行为","","","","",true);
		
		adddata("上课情况","精神","佳","普通","不佳","",false);
		adddata("","情绪","佳","普通","不佳","",false);
		adddata("","参与度","主动","被动","没反应","",false);
		adddata("","与同学互动","佳","普通","没反应","",false);
		adddata("","特殊行为","","","","",true);
		
		adddata("生理状况","排便次数","","","","",true);
		adddata("","小便次数","频繁","普通","少","",false);
		adddata("","排汗现象","多量","正常","很少","",false);
		
		adddata("午睡情况","入睡前时间","短","长","不睡","",false);
		adddata("","生理表现","正常","咳嗽","呼吸不顺","",false);
		adddata("","特殊行为","","","","",true);
		
		adddata("学习成效","听的能力","优秀","好","普通","",false);
		adddata("","说的能力","优秀","好","普通","",false);
		adddata("","语言","优秀","好","普通","",false);
		adddata("","社会","优秀","好","普通","",false);
		adddata("","健康","优秀","好","普通","",false);
		adddata("","艺术","优秀","好","普通","",false);
		adddata("","科学","优秀","好","普通","",false);
//		adddata("","整体表现","","","","",true);
		
		
		ArrayList<String > number=new ArrayList<String>();
		number.add("001001");
		number.add("001002");
		number.add("001003");
		number.add("001004");
		number.add("002001");
		number.add("002002");
		number.add("002003");
		number.add("002004");
		number.add("003001");
		number.add("003002");
		number.add("003003");
		number.add("003004");
		number.add("003005");
		
		number.add("004001");
		number.add("004002");
		number.add("004003");
		
		number.add("005001");
		number.add("005002");
		number.add("005003");
	
		number.add("006001");
		number.add("006002");
		number.add("006003");
		number.add("006004");
		number.add("006005");
		number.add("006006");
		number.add("006007");
//		number.add("006008");
		
		for(int i=0;i<data.size();i++){
			data.get(i).setNumber(number.get(i));
		}
		mAdapter=new FeedBackAdapter(Feedback.this);
		mAdapter.setData(data);
		listview.setAdapter(mAdapter);
	}
	
	public void refresh(){
		mAdapter.setData(data);
		mAdapter.notifyDataSetChanged();
	}
	
	private void adddata(
			String content ,
			String subcontent ,
			String titel0,
			String titel1,
			String titel2,
			String titel3,
			Boolean isInput
			){
		FeedBackData backData=new FeedBackData();
		backData.indata();//初始化数组
		backData.setContent(content);
		backData.setSubcontent(subcontent);
		backData.setInput(isInput);
		backData=addtitel(backData,titel0);
		backData=addtitel(backData,titel1);
		backData=addtitel(backData,titel2);
		backData=addtitel(backData,titel3);
		data.add(backData);
	}
	private FeedBackData addtitel(FeedBackData data, String titel) {
		FeedBackData backData=data;
		if(titel==null || titel.equals("")){
			
		}else{
			com.teacher.data.Option option=new com.teacher.data.Option();
			option.setTitel(titel);
			option.setIscheck(false);
			backData.getOptions().add(option);
		}
		return backData;
	}


	private void initview() {
		listview=(ListView) findViewById(R.id.list);
		findViewById(R.id.back).setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.ok://提交
			if(cansend()){
				try {
					ArrayList<String > number=new ArrayList<String>();
					number.add("001001");
					number.add("001002");
					number.add("001003");
					number.add("001004");
					number.add("002001");
					number.add("002002");
					number.add("002003");
					number.add("002004");
					number.add("003001");
					number.add("003002");
					number.add("003003");
					number.add("003004");
					number.add("003005");
					
					number.add("004001");
					number.add("004002");
					number.add("004003");
					
					number.add("005001");
					number.add("005002");
					number.add("005003");
				
					number.add("006001");
					number.add("006002");
					number.add("006003");
					number.add("006004");
					number.add("006005");
					number.add("006006");
					number.add("006007");
//					number.add("006008");
					
					JSONObject json=new JSONObject();
					JSONArray arry=new JSONArray();
					for(int i=0;i<data.size();i++){
						JSONObject temp=new JSONObject();
						temp.put("number", "'"+number.get(i)+"'");
						if(data.get(i).isInput()){
							temp.put("type", "'2'");
							temp.put("feedbackOptionOrder", "''");
							String str="";
							if(data.get(i).getSpecial_act()==null||data.get(i).getSpecial_act().trim().equals("")){
								str="无";
							}else{
								str=data.get(i).getSpecial_act();
							}
							temp.put("answer", "'"+str+"'");
						}else{
							temp.put("type", "'1'");
							for(int j=0;j<data.get(i).getOptions().size();j++){
								if(data.get(i).getOptions().get(j).isIscheck()){
									temp.put("feedbackOptionOrder", "'"+String.valueOf(j)+"'");
									break;
								}	
							}
							temp.put("answer", "''");
						}
						arry.put(temp);
					}
					json.put("answers",arry);
					json.put("homework","'"+Texttool.Gettext(Feedback.this, R.id.homework)+"'");
					json.put("studentId","'"+studentId+"'");
					json.put("loginId","'"+userinfo.getmobile_phone(Feedback.this)+"'");
					
					String str=json.toString().replaceAll("\"", "");
					System.out.println(str);
					save(str);
				} catch (Exception e) {
					System.out.println(e.toString());
				}
			}else{
				Toast.makeText(getApplicationContext(), "请完善反馈信息", 0).show();
			}
			break;
		}
	}
	
	private boolean cansend() {
		if(!Texttool.Havecontent(Feedback.this, R.id.homework)){
			return false;
		}
		if(Texttool.Gettext(Feedback.this, R.id.homework).length()>100){
			Toast.makeText(getApplicationContext(), "家庭作业最多只能输入100个字", 0).show();
			return false;
		}
		
		for(int i=0;i<data.size();i++){
			if(data.get(i).isInput()){
				if(data.get(i).getSpecial_act()==null || data.get(i).getSpecial_act().equals("")){
					
				}else{
					if(data.get(i).getSpecial_act().length()>100){
						Toast.makeText(getApplicationContext(), "特殊行为最多只可输入100个字", 0).show();
						return false;
					}
				}
			}else{
				if(data.get(i).getOptions()==null){
					System.out.println("1");
					return false;
				}else{
					boolean can=false;
					for(int j=0;j<data.get(i).getOptions().size();j++){
						if(data.get(i).getOptions().get(j).isIscheck()){
							can=true;
							break;
						}	
					}
					
					if(!can){
						return false;
					}
				}
			}
		}
		return true;
	}
	
	private void setHomeworkcanfocus(Boolean b){
		findViewById(R.id.homework).setEnabled(b);
	}

	private void setStar(float star){
		RatingBar bar=(RatingBar) findViewById(R.id.bar);
		bar.setRating(star);
	}
	Handler handler =new Handler(){
		public void handleMessage(android.os.Message msg) {
			try {
				if(lldialog!=null){
					lldialog.dismiss();
				}
			
			switch (msg.what) {
			case Constants.HASHOMEWORK:
				String homework=msg.obj.toString();
				if(homework==null||homework.equals("")){
					setHomeworkcanfocus(true);
				}else{
					Log.i("homework1", homework);
					Texttool.setText(Feedback.this, R.id.homework, homework);
					setHomeworkcanfocus(false);
				}
				break;
			case Constants.SAVEFEEDBACK:
				Toast.makeText(getApplicationContext(), "提交成功", 0).show();
				Feedback_selectChild.activity.updata(studentId);
				finish();
				break;
			case Constants.GETFEEDBACKDATA:
				GetFeedbackdata feedbackdata=(GetFeedbackdata) msg.obj;
				if(feedbackdata!=null){
					initdata(feedbackdata);
					refresh();
					setTitel(feedbackdata.getDate().substring(0, 10));
					setStar(feedbackdata.getStarNum());
					Texttool.setText(Feedback.this, R.id.homework, feedbackdata.getHomework());
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
	protected void initdata(GetFeedbackdata feedbackdata) {
		for(int i=0;i<data.size();i++){
				for(int y=0;y<feedbackdata.getAnswers().size();y++){
					if(data.get(i).getNumber().equals(feedbackdata.getAnswers().get(y).getFeedbackQuestionNumber())){
						if(data.get(i).isInput()){
							data.get(i).setSpecial_act(feedbackdata.getAnswers().get(y).getAnswer());
						}else{
							for(int j=0;j<data.get(i).getOptions().size();j++){
								if(j==feedbackdata.getAnswers().get(y).getFeedbackOptionOrder()){
									data.get(i).getOptions().get(j).setIscheck(true);
								}else{
									data.get(i).getOptions().get(j).setIscheck(false);
								}
							}
						}
					break;
				}
			}
		}
	}
	private void save(final String data) {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		lldialog=LodingDialog.DialogFactor(Feedback.this, "正在提交数据", true);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "feedbackEverydayService";
	            String method = "save";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = data;
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
	    				handler.sendMessage(handler.obtainMessage(Constants.SAVEFEEDBACK,"提交成功" ));
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

	private void getfeedbackdata(final String id) {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		lldialog=LodingDialog.DialogFactor(Feedback.this, "正在获取数据", true);
		new Thread(new Runnable() {
			public void run() {
				String service = "feedbackEverydayService";
				String method = "getById";
				String token = MD5.MD5(service+method+Constants.Key);
				String params = "{id:'"+id+"'}";
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
	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<GetFeedbackdata>(){}.getType());
						handler.sendMessage(handler.obtainMessage(Constants.GETFEEDBACKDATA,object ));
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
	private void hasInputHomework(final String studentId) {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		lldialog=LodingDialog.DialogFactor(Feedback.this, "正在获取数据", true);
		new Thread(new Runnable() {
			public void run() {
				String service = "feedbackEverydayService";
				String method = "validateSave";
				String token = MD5.MD5(service+method+Constants.Key);
				String params = "{studentId:'"+studentId+"'}";
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
						String homework = jsonObj.getJSONObject("data").getString("homework");
//						JSONObject jsonObj1=new JSONObject(jsonData);
//						String homework=jsonObj1.getString("homework");
						System.out.println(homework+"homework");
//						Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<GetFeedbackdata>(){}.getType());
						handler.sendMessage(handler.obtainMessage(Constants.HASHOMEWORK,homework ));
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
