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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.dream.framework.utils.dialog.LodingDialog;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lester.company.http.HttpURL;
import com.lester.headview.CustomImageView;
import com.lester.school.requst.Constants;
import com.lester.school.requst.JsonUtil;
import com.lester.uteacher.goton.Classlist;
import com.lester.uteacher.leave.Leavelist;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.Loadhead;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.lester.uteacher.tool.Timetool;
import com.lester.uteacher.tool.Userinfo;





/**
 * @author Administrator
 *请假列表
 */
public final class LeaveList extends Activity implements OnClickListener{

	public static LeaveList activity;
	static final int MENU_MANUAL_REFRESH = 0;
	static final int MENU_DISABLE_SCROLL = 1;
	static final int MENU_SET_MODE = 2;
	static final int MENU_DEMO = 3;
	private PullToRefreshListView mPullRefreshListView;
	private BaseAdapter mAdapter;
	private LodingDialog lldialog;
	private String selectclassid;//根据班级id获取孩子信息
	private int pageNum=1;//分页起始页
	private int dpageNum=1;
	private ArrayList<Leavelist.datas> datas =new ArrayList<Leavelist.datas>();
	private List<Classlist.datas> classdatas;
	private int ISWATH;
	private int reposition;
	private String maxtime="";
	
	
	

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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity=this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.leavelist);
		setadapter();
		initview();
		getclass();
	}
	//将状态变为已读
	public void refresh(String time){
		datas.get(reposition).setProcess_status("2");
		datas.get(reposition).setHandle_time(time);
		mAdapter.notifyDataSetChanged();
	}
	
	private void setadapter() {
		// TODO Auto-generated method stub
		mAdapter=new BaseAdapter() {
			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				if(convertView==null){
					convertView=getLayoutInflater().inflate(R.layout.levealist_item, null);
				}
				CustomImageView headimg=(CustomImageView) convertView.findViewById(R.id.childhead);
				if(datas.get(position).getStudentPhoto()!=null &&
							 (!datas.get(position).getStudentPhoto().equals(""))){
						new Loadhead(LeaveList.this,datas.get(position).getStudentPhoto(), headimg).execute();
					 }
				setStatus(convertView.findViewById(R.id.log),datas.get(position).getProcess_status());//消息状态
				Texttool.setText(convertView, R.id.childname, datas.get(position).getStudentName());
				Texttool.setText(convertView, R.id.content, datas.get(position).getContent());
				Texttool.setText(convertView, R.id.submit_time, datas.get(position).getSubmit_time()
						.substring(0, 10));
				Texttool.setText(convertView, R.id.time, 
						"假期\t"+
						datas.get(position).getStart_time().substring(0, 10)
						+"至"
						+datas.get(position).getEnd_time().substring(0, 10));
				
				convertView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						reposition=position;
						Intent intent =new Intent(LeaveList.this,Leavedetail.class);
						intent.putExtra("data", datas.get(position));
						startActivityForResult(intent, 0);
					}
				});
				
				
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
				if(datas!=null){
					return datas.size();
				}else{
					return 0;
				}
			}
		};
	}
	private void setStatus(View log,String process_status) {
		//process_status,1是待处理，2是已处理
		if(process_status.equals("1")){
			log.setBackgroundResource(R.drawable.u70);
		}else{
			log.setVisibility(View.GONE);
		}
	}
	@SuppressWarnings("unchecked")
	private void initview() {
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.selectclass).setOnClickListener(this);
		
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				System.out.println("刷新");
				loadData(Constants.REFRESH);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				System.out.println("刷新");
				loadData(Constants.DOWNLOAD);
			}
		});
		ListView actualListView = mPullRefreshListView.getRefreshableView();
		registerForContextMenu(actualListView);
		actualListView.setAdapter(mAdapter);
		mPullRefreshListView.setMode(Mode.BOTH);//设置刷新方式
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
				if(!selectclassid.equals(classdatas.get(arg2).getId())){//选择了另外一个班级，才进行数据刷新
					selectclassid=classdatas.get(arg2).getId();
					datas=new ArrayList<Leavelist.datas>();
					mAdapter.notifyDataSetChanged();
					dpageNum=1;
					loadData(Constants.DOWNLOAD);
				}
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
	@SuppressLint("HandlerLeak")
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			try {
				if(lldialog!=null && msg.what!=Constants.GETCLASS){//获取班级列表后直接请求学生数据，所以此处不取消弹框
					lldialog.dismiss();
			}
			switch (msg.what) {
			case Constants.GETLEAVEDATA:
				mPullRefreshListView.onRefreshComplete();
				ArrayList<Leavelist.datas> getdata=(ArrayList<Leavelist.datas>) msg.obj;
				System.out.println("数据");
				if(ISWATH==Constants.REFRESH){
					ArrayList<Leavelist.datas> temp =new ArrayList<Leavelist.datas>();
					if(getdata!=null && getdata.size()!=0){
						temp=datas;
						datas=getdata;
						for(int i=0;i<temp.size();i++ ){
							datas.add(temp.get(i));
						}
						mAdapter.notifyDataSetChanged();
					}else{
						Toast.makeText(getApplicationContext(), "没有新的请假信息", 0).show();
					}
				}else if(getdata!=null && getdata.size()!=0){
					for(int i=0;i<getdata.size();i++ ){
						datas.add(getdata.get(i));
					}
					mAdapter.notifyDataSetChanged();
				}else{
					Toast.makeText(getApplicationContext(), "没有更多请假信息", 0).show();
				}
				break;
			case Constants.GETCLASS:
				classdatas=(List<Classlist.datas>) msg.obj;
				if(classdatas!=null && classdatas.size()!=0){
					if(classdatas.get(0).getId()!=null && !classdatas.get(0).getId().equals("null")){
						selectclassid=classdatas.get(0).getId();
						loadData(Constants.DOWNLOAD);
						findViewById(R.id.selectclass).setVisibility(View.VISIBLE);//可以选择班级
					}else{
						Toast.makeText(getApplicationContext(), "您不是带班老师", 0).show();
						if(lldialog!=null){
							lldialog.dismiss();
						}
						mPullRefreshListView.setMode(Mode.DISABLED);
					}
				}else{
					Toast.makeText(getApplicationContext(), "您不是带班老师", 0).show();
					if(lldialog!=null){
						lldialog.dismiss();
					}
					mPullRefreshListView.setMode(Mode.DISABLED);
				}
				break;
				
			case 404:
				Toast.makeText(getApplicationContext(), msg.obj.toString(), 0).show();
				break;
				
			case 406:
				mPullRefreshListView.onRefreshComplete();
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
		lldialog=LodingDialog.DialogFactor(LeaveList.this, "正在获取数据", true);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "clazzService";
	            String method = "search";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = "{loginId:'"+Userinfo.getmobile_phone(LeaveList.this)+"'}";
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
	private void leaveRecordService() {
		if(Net.isNetworkAvailable(getApplicationContext())){
			handler.sendMessage(handler.obtainMessage(406));
			return;
		}
		final String pageNum=""+this.pageNum;
		final String pageSize="10";
		new Thread(new Runnable() {
	        public void run() {
	            String service = "leaveRecordService";
	            String method = "searchPageByClazzId";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = 
	            		"{clazzId:'"+selectclassid+
	            		"',pageNum:"+pageNum+
	            		",pageSize:"+pageSize+
	            		",maxTime:'"+maxtime+
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
	    				String jsonData = jsonObj.getString("datas");
	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<ArrayList<Leavelist.datas>>(){}.getType());
	    				handler.sendMessage(handler.obtainMessage(Constants.GETLEAVEDATA, object));
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
			
		default:
			break;
		}
	}
	private void loadData(int what){
		if(Net.isNetworkAvailable(getApplicationContext())){
			handler.sendMessage(handler.obtainMessage(406));
			return;
		}
		switch (what) {
		case Constants.REFRESH:
			pageNum=1;
			if(datas.size()!=0){
				maxtime=datas.get(0).getSubmit_time();
				ISWATH=Constants.REFRESH;
				leaveRecordService();
			}else{
				mPullRefreshListView.onRefreshComplete();
			}
			
			break;

		case Constants.DOWNLOAD:
				pageNum=dpageNum++;
				maxtime="";
				ISWATH=Constants.DOWNLOAD;
				leaveRecordService();
			
			break;
		}
	}
}
