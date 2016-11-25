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
import com.lester.headview.CustomImageView;
import com.lester.school.requst.Constants;
import com.lester.school.requst.JsonUtil;
import com.lester.slidecutlistview.CustomSwipeListView;
import com.lester.uteacher.adapter.SlidListviewAdapter;
import com.lester.uteacher.code.Codedata;
import com.lester.uteacher.goton.Classlist;
import com.lester.uteacher.goton.Student;
import com.lester.uteacher.register.GetData;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.Loadhead;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.lester.uteacher.tool.Timetool;
import com.lester.uteacher.tool.Userinfo;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;

public class GotOn extends Activity implements OnClickListener{
	private SlidListviewAdapter adapter;
	private LodingDialog lldialog;
	private static int info;
	private ArrayList<Student.datas> datas=new ArrayList<Student.datas>();
	public static TextView [] status;//学生状态，在校，车上，离校
	public static Boolean [] canslid;//记录可以侧滑的item位置
	public static CheckBox [] check;//
	private Boolean [] position ;//确认成功后改变view时使用
	public static int reposition ;//还原的时候记录所需要还原的item
	private String getservice,getmethod;//获取学生列表
	private String inservice,inmethod;//上车
	private String orderStatus;
	public static String outservice,outmethod;//还原
	private String selectclassid;//根据班级id获取孩子信息
	private List<Classlist.datas> classdatas;
	private CustomSwipeListView slideListView;
	

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
		setContentView(R.layout.goton);
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.makesure).setOnClickListener(this);
		info=getIntent().getIntExtra("info", 0);
		initview();
		getstudent();
	}

	private void getstudent() {
		switch (info) {
		case 1://入校上车
			Texttool.setText(GotOn.this, R.id.bartitle, "校车入校");
			getservice="studentService";
			getmethod="searchForInOutByBusTeacherLoginId";
			inservice="inSchoolService";
			inmethod="enrtyBus";
			outservice="inSchoolService";
			outmethod="enrtyBusRestore";
			orderStatus="in";
			getstudentinfo();
			break;
		case 2://离校上车
			Texttool.setText(GotOn.this, R.id.bartitle, "校车离校");
			getservice="studentService";
			getmethod="searchForInOutByBusTeacherLoginId";
			inservice="outSchoolService";
			inmethod="enrtyBus";
			outservice="outSchoolService";
			outmethod="enrtyBusRestore";
			orderStatus="out";
			getstudentinfo();
			break;
		case 3://学生接送
			Texttool.setText(GotOn.this, R.id.bartitle, "入校接收");
			getclass();
			inservice="inSchoolService";
			inmethod="enrtySchool";
			outservice="inSchoolService";
			outmethod="enrtySchoolRestore";
			break;
		}
	}

	

	private void initview() {
		adapter=new SlidListviewAdapter(GotOn.this,getApplicationContext(),datas,handler);
		slideListView = (CustomSwipeListView) findViewById(R.id.slideCutListView);
		slideListView.setAdapter(adapter);
		findViewById(R.id.selectclass).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
	switch (v.getId()) {
	case R.id.back:
		finish();
		break;
	case R.id.selectclass:
		initPopWindow(R.id.selectclass);
		break;
	case R.id.makesure:
		gotonmakesure();
		break;
		}
		
	}
	private void paixu() {
		//在校车1  在校园2  离校3
		Log.i("tag", "排序");
		ArrayList<Student.datas> mydatas = new ArrayList<Student.datas>();
		switch (info) {
		case 1://入校
			for(int i=0;i<datas.size();i++){
				if (datas.get(i).getPosition_status()==3) {
					mydatas.add(datas.get(i));
				}
			}
			for(int i=0;i<datas.size();i++){
				if (datas.get(i).getPosition_status()==1) {
					mydatas.add(datas.get(i));
				}
			}
			for(int i=0;i<datas.size();i++){
				if (datas.get(i).getPosition_status()==2) {
					mydatas.add(datas.get(i));
				}
			}
			break;

		case 2://离校
			for(int i=0;i<datas.size();i++){
				if (datas.get(i).getPosition_status()==2) {
					mydatas.add(datas.get(i));
				}
			}
			for(int i=0;i<datas.size();i++){
				if (datas.get(i).getPosition_status()==1) {
					mydatas.add(datas.get(i));
				}
			}
			for(int i=0;i<datas.size();i++){
				if (datas.get(i).getPosition_status()==3) {
					mydatas.add(datas.get(i));
				}
			}
			break;
		case 3://入校接受
			for(int i=0;i<datas.size();i++){
				if (datas.get(i).getPosition_status()==3) {
					mydatas.add(datas.get(i));
				}
			}
			for(int i=0;i<datas.size();i++){
				if (datas.get(i).getPosition_status()==1) {
					mydatas.add(datas.get(i));
				}
			}
			for(int i=0;i<datas.size();i++){
				if (datas.get(i).getPosition_status()==2) {
					mydatas.add(datas.get(i));
				}
			}
			break;
		}
		
		System.out.println("排序完成");
		datas=new ArrayList<Student.datas>();
		datas=mydatas;
		
	}
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			try {
				
				if(adapter!=null && adapter.lldialog!=null){
					adapter.lldialog.dismiss();//取消还原弹框
				}
				
				if(lldialog!=null&&msg.what!=Constants.GETCLASS){//获取班级列表后直接请求学生数据，所以此处不取消弹框
					lldialog.dismiss();
				
				}
			switch (msg.what) {
			case Constants.GOTON:
				ArrayList<Student.datas> getdata=(ArrayList<Student.datas>) msg.obj;
				if(getdata!=null){
					datas=getdata;
					paixu();
					initview();
				}
				break;
			case Constants.ENRTYBUS:
				Toast.makeText(getApplicationContext(), msg.obj.toString(), 0).show();
				int j=0;
				for(int i=0;i<position.length;i++){
					if(position[i]){
						if(info==3){
							datas.get(i).setPosition_status(2);//在校
							datas.get(i).setHasLeaveRecord(false);//没请假
						}else{
							datas.get(i).setPosition_status(1);//车上
						}
						j=i;
					}
				}
				paixu();
				initview();
				break;
			case Constants.ENRTYBUSRESTORE:
				Toast.makeText(getApplicationContext(), "还原成功", 0).show();
				if(adapter.lldialog!=null){
					adapter.lldialog.dismiss();//取消弹框
				}
				redata mredata=(redata) msg.obj;
				if(mredata==null){
					return;
				}
				datas.get(reposition).setPosition_status(mredata.getPosition_status());
				if(info!=1){//入校上车还原后台没有反请假状态
					datas.get(reposition).setHasLeaveRecord(mredata.isHasLeaveRecord());
				}
				paixu();
				initview();
				break;
			case Constants.GETCLASS:
				classdatas=(List<Classlist.datas>) msg.obj;
				if(classdatas!=null&&classdatas.size()!=0){
					if(classdatas.get(0).getSchool_id()!=null){
						selectclassid=classdatas.get(0).getId();
						getstudentinfobyclassid();
						findViewById(R.id.selectclass).setVisibility(View.VISIBLE);//可以选择班级
					}
				}else{
					Toast.makeText(getApplicationContext(), "您不是带班老师", 0).show();
					if(lldialog!=null){
						lldialog.dismiss();
					}
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
	 * 新建一个popupWindow弹出框 popupWindow是一个阻塞式的弹出框，这就意味着在我们退出这个弹出框之前，程序会一直等待，
	 * 这和AlertDialog不同哦，AlertDialog是非阻塞式弹出框，AlertDialog弹出的时候，后台可是还可以做其他事情的哦。
	 * @param data 
	 * @param sex 
	 */
	private void initPopWindow(final int id) {
		// 加载popupWindow的布局文件
		View contentView_pop = LayoutInflater.from(getApplicationContext())
				.inflate(R.layout.pop_list, null);
		// 设置popupWindow的背景颜色
		contentView_pop.setBackgroundResource(R.color.backhuise);
		// 声明一个弹出框
		final PopupWindow popupWindow = new PopupWindow(
				findViewById(R.id.fenleilayout), getWindowManager().getDefaultDisplay().getWidth()/3, LayoutParams.WRAP_CONTENT);
		// 为弹出框设定自定义的布局
		popupWindow.setContentView(contentView_pop);
		final ListView mListView = (ListView) contentView_pop
				.findViewById(R.id.pop_listView);
		BaseAdapter adapter=new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				convertView=getLayoutInflater().inflate(R.layout.poplistitem, null);
				((TextView) convertView.findViewById(R.id.itemtext)).setText(classdatas.get(position).getName());
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
				if(classdatas!=null){
					return classdatas.size();
				}
				return 0;
			}
		};
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				selectclassid=classdatas.get(arg2).getId();
				getstudentinfobyclassid();
				popupWindow.dismiss();
			}
		});
		// 设定当你点击editText时，弹出的输入框是啥样子的。这里设置默认为数字输入哦，这时候你会发现你输入非数字的东西是不行的哦
		// editText.setInputType(InputType.TYPE_CLASS_NUMBER);
		/*
		 * 这个popupWindow.setFocusable(true);非常重要，如果不在弹出之前加上这条语句，你会很悲剧的发现，你是无法在
		 * editText中输入任何东西的
		 * 。该方法可以设定popupWindow获取焦点的能力。当设置为true时，系统会捕获到焦点给popupWindow
		 * 上的组件。默认为false哦.该方法一定要在弹出对话框之前进行调用。
		 */
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(false);
		popupWindow.setBackgroundDrawable(new ColorDrawable());
		/*
		 * popupWindow.showAsDropDown（View view）弹出对话框，位置在紧挨着view组件
		 * showAsDropDown(View anchor, int xoff, int yoff)弹出对话框，位置在紧挨着view组件，x y
		 * 代表着偏移量 showAtLocation(View parent, int gravity, int x, int y)弹出对话框
		 * parent 父布局 gravity 依靠父布局的位置如Gravity.CENTER x y 坐标值
		 */
		popupWindow.showAsDropDown(findViewById(id));
	}
	/**
	 * 获取班级列表
	 */
	private void getclass() {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		lldialog=LodingDialog.DialogFactor(GotOn.this, "正在获取数据", false);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "clazzService";
	            String method = "search";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = "{loginId:'"+Userinfo.getmobile_phone(GotOn.this)+"'}";
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
	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<List<Classlist.datas>>(){}.getType());
	    				handler.sendMessage(handler.obtainMessage(Constants.GETCLASS, object));
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
	 * 入校上车
	 */
	private void getstudentinfobyclassid() {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		new Thread(new Runnable() {
	        public void run() {
	            String service = "studentService";
	            String method = "searchForInOutByClazzId";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = "{clazzId:'"+selectclassid+"'}";
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
	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<List<Student.datas>>(){}.getType());
	    				handler.sendMessage(handler.obtainMessage(Constants.GOTON, object));
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
	 * 入校上车
	 */
	private void getstudentinfo() {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		lldialog=LodingDialog.DialogFactor(GotOn.this, "正在获取数据", false);
		new Thread(new Runnable() {
	        public void run() {
	            String service = getservice;
	            String method = getmethod;
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = "{loginId:'"+Userinfo.getmobile_phone(GotOn.this)+
	            		"',orderStatus:'"+orderStatus+"'}";
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
	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<ArrayList<Student.datas>>(){}.getType());
	    				handler.sendMessage(handler.obtainMessage(Constants.GOTON, object));
	    			}else{
	    				handler.sendMessage(handler.obtainMessage(404, "您不是跟车老师"));
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
	 * 确认上车
	 */
	private void gotonmakesure() {
		String studentIds="";
		 try {
		position= new Boolean [adapter.ischeck.length];
		int j=0;
		for(int i=0;i<adapter.ischeck.length;i++){
			if(adapter.ischeck[i]){
				j++;
				position[i]=true;
				if(studentIds.equals("")){
					studentIds=datas.get(i).getId();//id就是学生id
				}else{
					studentIds=studentIds+","+datas.get(i).getId();
				}
			}else{
				position[i]=false;
			}
		}
		if(j==0){
			return;
		}
		 } catch (Exception e) {
				System.out.println("错误+"+e.toString());
			}
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		final String studentId=studentIds;
		if(studentId==null||studentId.equals("")){
			return;
		}
		lldialog=LodingDialog.DialogFactor(GotOn.this, "正在确认", false);
		new Thread(new Runnable() {
	        public void run() {
	            String service = inservice;
	            String method = inmethod;
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = "{studentIds:'"+studentId+
	            		"',loginId:'"+Userinfo.getmobile_phone(GotOn.this)+"'}";
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
//	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<List<Student.datas>>(){}.getType());
	    				handler.sendMessage(handler.obtainMessage(Constants.ENRTYBUS, "确认成功"));
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
	 * 修改学生状态
	 */
	@SuppressLint("ResourceAsColor")
	public static void setstatus(TextView textView, int position_status,int position) {
		switch (position_status) {
		//position_status含义，在校车1  在校园2  离校3
		case 1:
			switch (info) {
			case 1://入校上车
				canslid[position]=true;
				check[position].setClickable(false);
				Texttool.setText(textView, "车上");
				textView.setBackgroundResource(R.drawable.ischeshang);
				break;
			case 2://离校上车
				canslid[position]=true;
				check[position].setClickable(false);
				Texttool.setText(textView, "车上");
				textView.setBackgroundResource(R.drawable.ischeshang);
				break;
			case 3://入校接收
				canslid[position]=false;
				check[position].setClickable(true);
				Texttool.setText(textView, "车上");
				textView.setBackgroundResource(R.drawable.ischeshang);
				break;
				}
			break;
		case 2:
			switch (info) {
			case 1:
				canslid[position]=false;
				check[position].setClickable(false);
				Texttool.setText(textView, "在校");
				textView.setBackgroundResource(R.drawable.isschool);
				break;
			case 2:
				canslid[position]=false;
				check[position].setClickable(true);
				Texttool.setText(textView, "在校");
				textView.setBackgroundResource(R.drawable.isschool);
				break;
			case 3:
				canslid[position]=true;
				check[position].setClickable(false);
				Texttool.setText(textView, "在校");
				textView.setBackgroundResource(R.drawable.isschool);
				break;
				}
			break;
		case 3:
			switch (info) {
			case 1:
				canslid[position]=false;
				check[position].setClickable(true);
				Texttool.setText(textView, "离校");
				textView.setBackgroundResource(R.drawable.notinschool);
				break;
			case 2:
				canslid[position]=false;
				check[position].setClickable(false);
				Texttool.setText(textView, "离校");
				textView.setBackgroundResource(R.drawable.notinschool);
				break;
			case 3:
				canslid[position]=false;
				check[position].setClickable(true);
				Texttool.setText(textView, "离校");
				textView.setBackgroundResource(R.drawable.notinschool);
				break;
				}
			break;
		}
	}
	
	public class redata{
		private int  position_status;
		private boolean hasLeaveRecord;
		public int getPosition_status() {
			return position_status;
		}
		public void setPosition_status(int position_status) {
			this.position_status = position_status;
		}
		public boolean isHasLeaveRecord() {
			return hasLeaveRecord;
		}
		public void setHasLeaveRecord(boolean hasLeaveRecord) {
			this.hasLeaveRecord = hasLeaveRecord;
		}
	}
}
