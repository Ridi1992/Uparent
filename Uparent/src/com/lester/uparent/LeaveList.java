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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.dream.framework.utils.dialog.LodingDialog;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lester.company.http.HttpURL;
import com.lester.headview.CustomImageView;
import com.lester.school.requst.Constants;
import com.lester.school.requst.JsonUtil;
import com.lester.uparent.announcement.Announcementdata;
import com.lester.uteacher.leave.Leavelist;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.lester.uteacher.tool.Userinfo;





/**
 * @author Administrator
 *请假列表
 */
public final class LeaveList extends Activity implements OnClickListener{

	static final int MENU_MANUAL_REFRESH = 0;
	static final int MENU_DISABLE_SCROLL = 1;
	static final int MENU_SET_MODE = 2;
	static final int MENU_DEMO = 3;
	private PullToRefreshListView mPullRefreshListView;
	private BaseAdapter mAdapter;
	private LodingDialog lldialog;
	private int pageNum=1;//分页起始页
	private int dpageNum=1;
	private ArrayList<Leavelist.datas> datas =new ArrayList<Leavelist.datas>();
	private int ISWATH;
	private String maxtime="";
	public static LeaveList leaveList;
	
	 public void havenewdata(){
		 lldialog=LodingDialog.DialogFactor(LeaveList.this, "正在获取数据", true);
			loadData(Constants.REFRESH);
	 }
	 
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
		setContentView(R.layout.leavelist);
		setadapter();
		initview();
		loadData(Constants.DOWNLOAD);
		lldialog=LodingDialog.DialogFactor(LeaveList.this, "正在获取数据", true);
		leaveList=this;
	}

	private void setadapter() {
		mAdapter=new BaseAdapter() {
			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				convertView=getLayoutInflater().inflate(R.layout.levealist_item, null);
//				CustomImageView headimg=(CustomImageView) convertView.findViewById(R.id.childhead);
//				if(datas.get(position).getStudentPhoto()!=null &&
//							 (!datas.get(position).getStudentPhoto().equals(""))){
//						new Loadhead(datas.get(position).getStudentPhoto(), headimg).execute();
//					 }
				
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
						Intent intent =new Intent(LeaveList.this,Leavelistdetail.class);
						intent.putExtra("datas", datas.get(position));
						startActivity(intent);
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

	@SuppressWarnings("unchecked")
	private void initview() {
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.qingjia).setOnClickListener(this);
		
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
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			try {
				if(lldialog!=null ){//获取班级列表后直接请求学生数据，所以此处不取消弹框
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
	            String method = "searchPageByStudentId";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = 
	            		"{studentId:'"+Userinfo.getchildid(LeaveList.this)+
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
		case R.id.qingjia:
			Intent intent =new Intent(LeaveList.this,LeaveApply.class);
			startActivityForResult(intent, 0);
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
		 	System.out.println("加载");
//			if((datas.size()%10)==0){//取余等于0,10的倍数
				pageNum=dpageNum++;
				maxtime="";
				ISWATH=Constants.DOWNLOAD;
				leaveRecordService();
//			}else{
//				handler.sendMessage(handler.obtainMessage(406));
//				Toast.makeText(getApplicationContext(), "没有更多请假信息", 0).show();
//			}
			
			break;
		}
	}
}
