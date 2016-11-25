package com.lester.uteacher;

import java.util.ArrayList;
import java.util.LinkedList;
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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;

import com.baidu.mobstat.StatService;
import com.dream.framework.utils.dialog.LodingDialog;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lester.company.http.HttpURL;
import com.lester.headview.CustomImageView;
import com.lester.school.requst.Constants;
import com.lester.school.requst.JsonUtil;
import com.lester.uteacher.goton.Classlist;
import com.lester.uteacher.mystudent.Student;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.Base64;
import com.lester.uteacher.tool.BitMap;
import com.lester.uteacher.tool.Loadhead;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.lester.uteacher.tool.Userinfo;





/**
 * @author Administrator
 *接送登记
 */
public final class My_student extends Activity implements OnClickListener {

	static final int MENU_MANUAL_REFRESH = 0;
	static final int MENU_DISABLE_SCROLL = 1;
	static final int MENU_SET_MODE = 2;
	static final int MENU_DEMO = 3;

	private LinkedList<String> mListItems;
	private PullToRefreshListView mPullRefreshListView;
	private BaseAdapter mAdapter;
	private LodingDialog lldialog;
	private String selectclassid;//根据班级id获取孩子信息
	private ArrayList<Student.datas> list;
	private Boolean Cancheck=true;
	private View gridview;
	private TextView mytextview;
	private int  logposition;
	private List<Classlist.datas> classdatas;
	public static Boolean isclassteacher=true;//判断是否是带班老师
	/** Called when the activity is first created. */
	
	
	

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
		setContentView(R.layout.mystudent);
		setAdapter();
		initview();
		getclass();
	}

	private void setAdapter() {
		mAdapter=new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				try {
				convertView = getLayoutInflater().inflate(R.layout.mystudentlistview_item, null);
				CustomImageView headimg=(CustomImageView)convertView.findViewById(R.id.childhead);
				String head=list.get(position).getPhoto();
				if(head!=null && (!head.equals(""))){
						new Loadhead(My_student.this,head, headimg).execute();
					 }
				
				if(list.get(position).getGender().equals("F")){
					((ImageView)convertView.findViewById(R.id.sex)).setImageResource(R.drawable.nv);
				}else{
					((ImageView)convertView.findViewById(R.id.sex)).setImageResource(R.drawable.nan);
				}
				final int myposition=position;
				convertView.findViewById(R.id.call).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						String Tel=list.get(myposition).getUrgent_link_phone();
						if(Tel==null || Tel.equals("")){
							Toast.makeText(getApplicationContext(), "电话号码为空", 0).show();
						}else{
							Log.i("", Tel);
							Intent intent = new Intent();
							intent.setAction(Intent.ACTION_CALL);
							intent.setData(Uri.parse("tel:"+Tel));
							startActivity(intent);
						}
					}
				});
				Texttool.setText(convertView,R.id.childname, list.get(position).getName());
				Texttool.setText(convertView,R.id.gender_display, list.get(position).getGender_display());
//				Texttool.setText(convertView,R.id.address, list.get(position).getAddress());
				TextView textview=(TextView) convertView.findViewById(R.id.status);
				setstaust(convertView,textview,list.get(position).getPosition_status(),position);//设置当前状态
				} catch (Exception e) {
					System.out.println("错误"+e.toString());
				}
				return convertView;
			}
			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getCount() {
				if(list!=null){
					return list.size();
				}else{
					return 0;
				}
			}
		};
	}
	private void setstaust(final View view,final TextView textView, int position_status,final int position) {
		switch (position_status) {
		//position_status含义，在校车1  在校园2  离校3
		case 1:
			Texttool.setText(textView, "车上");
			textView.setBackgroundResource(R.drawable.ischeshang);
			break;
		case 2:
			Texttool.setText(textView, "在校");
			textView.setBackgroundResource(R.drawable.isschool);
			break;
		case 3:
			Texttool.setText(textView, "离校");
			textView.setBackgroundResource(R.drawable.notinschool);
			break;
		}
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(Cancheck){
					Intent intent = new Intent(getApplicationContext(),Mystudent_detail.class);
					intent.putExtra("datas", list.get(position));
					startActivityForResult(intent, 1);
				}
			}
		});
	}
	private void initview() {
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.selectclass).setOnClickListener(this);
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
			}
		});

		mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible(){
//				Toast.makeText(MyStudent.this, "End of List!", Toast.LENGTH_SHORT).show();
			}
		});
		ListView actualListView = mPullRefreshListView.getRefreshableView();
		actualListView.setAdapter(mAdapter);
		mPullRefreshListView.setMode(Mode.DISABLED);//设置刷新方式
	}
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
				searchByClazzId();
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
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			try {
				if(lldialog!=null && msg.what!=Constants.GETCLASS){//获取班级列表后直接请求学生数据，所以此处不取消弹框
					lldialog.dismiss();
			}
			switch (msg.what) {
			case Constants.GETSTUDENTDATA:
				list=(ArrayList<Student.datas>) msg.obj;
				if(list!=null && list.size()!=0){
					mAdapter.notifyDataSetChanged();
				}else{
					Toast.makeText(getApplicationContext(), "暂无数据", 0).show();
				}
				break;
			case Constants.GETCLASS:
				classdatas=(List<Classlist.datas>) msg.obj;
				if(classdatas!=null&&classdatas.size()!=0){
					if(classdatas.get(0).getSchool_id()!=null){
						selectclassid=classdatas.get(0).getId();
						searchByClazzId();
//						Cancheck=false;//带班老师只能查看学生状态，不能改变学生状态
						System.out.println("带班老师");
						findViewById(R.id.selectclass).setVisibility(View.VISIBLE);//可以选择班级
					}else{
//						Cancheck=true;
						searchByBusTeacherLoginId();
						System.out.println("不是带班老师");
					}
				}else{
					searchByBusTeacherLoginId();
					findViewById(R.id.selectclass).setVisibility(View.GONE);
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
	 * 获取班级列表
	 */
	private void getclass() {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		lldialog=LodingDialog.DialogFactor(My_student.this, "正在获取数据", true);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "clazzService";
	            String method = "search";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = "{loginId:'"+Userinfo.getmobile_phone(My_student.this)+"'}";
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
	 * 班主任身份获取孩子信息
	 */
	private void searchByClazzId() {
		isclassteacher=true;
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
//		lldialog=LodingDialog.DialogFactor(MyStudent.this, "正在获取数据", true);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "studentService";
	            String method = "searchByClazzId";
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
	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<ArrayList<Student.datas>>(){}.getType());
	    				handler.sendMessage(handler.obtainMessage(Constants.GETSTUDENTDATA, object));
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
	 * 跟车老师获取孩子信息
	 */
	private void searchByBusTeacherLoginId() {
		isclassteacher=false;
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
//		lldialog=LodingDialog.DialogFactor(MyStudent.this, "正在获取数据", true);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "studentService";
	            String method = "searchByBusTeacherLoginId";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = "{loginId:'"+Userinfo.getmobile_phone(My_student.this)+"'}";
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
	    				handler.sendMessage(handler.obtainMessage(Constants.GETSTUDENTDATA, object));
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
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.selectclass:
			initPopWindow(R.id.selectclass);
			break;
		
		}
	}
}
