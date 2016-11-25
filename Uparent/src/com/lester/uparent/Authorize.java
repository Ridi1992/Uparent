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
import android.provider.ContactsContract.Data;
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
import com.lester.uparent.authorize.Authordata;
import com.lester.uteacher.leave.Leavelist;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.lester.uteacher.tool.Timetool;
import com.lester.uteacher.tool.Userinfo;





/**
 * @author Administrator
 *委托列表
 */
public final class Authorize extends Activity implements OnClickListener{

	
	static final int MENU_MANUAL_REFRESH = 0;
	static final int MENU_DISABLE_SCROLL = 1;
	static final int MENU_SET_MODE = 2;
	static final int MENU_DEMO = 3;
	private PullToRefreshListView mPullRefreshListView;
	private BaseAdapter mAdapter;
	private LodingDialog lldialog;
	private int pageNum=1;//分页起始页
	private ArrayList<Authordata.datas> datas =new ArrayList<Authordata.datas>();
//	private Boolean CANGETDATA=true;
	private int ISWATH;
	private String starttime="";
	public static Authorize authorize;
	private Boolean isReceive;//是否有委托权限
	/** Called when the activity is first created. */
	
	/**
	 * 刷新
	 */
	public void havanewdata(){
		lldialog=LodingDialog.DialogFactor(Authorize.this, "正在获取数据", true);
		datas=new ArrayList<Authordata.datas>();
		mAdapter.notifyDataSetChanged();
		loadData(Constants.DOWNLOAD);
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
		authorize=this;
		setContentView(R.layout.authorize);
		setadapter();
		initview();
		isSubParent();
	}

	private void setadapter() {
		mAdapter=new BaseAdapter() {
			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				if(convertView==null){
					convertView=getLayoutInflater().inflate(R.layout.authorize_item, null);
					}
//				CustomImageView headimg=(CustomImageView) convertView.findViewById(R.id.childhead);
//				if(datas.get(position).getStudentPhoto()!=null &&
//							 (!datas.get(position).getStudentPhoto().equals(""))){
//						new Loadhead(datas.get(position).getStudentPhoto(), headimg).execute();
//					 }
				Texttool.setText(convertView, R.id.studentname, datas.get(position).getStudentName());
				Texttool.setText(convertView, R.id.agent_time, datas.get(position).getAgentTime());
				Texttool.setText(convertView, R.id.agent_name, datas.get(position).getAgentName());
				convertView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent =new Intent(Authorize.this,AuthorApply_detail.class);
						intent.putExtra("agentOrderId", datas.get(position).getId());
						intent.putExtra("agentPhone", datas.get(position).getAgentPhone());
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
		mPullRefreshListView.setMode(Mode.DISABLED);//设置刷新方式
	}
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			try {
				if(lldialog!=null ){//获取班级列表后直接请求学生数据，所以此处不取消弹框
					lldialog.dismiss();
			}
			switch (msg.what) {
			case Constants.GETAUTHORIZE:
				mPullRefreshListView.onRefreshComplete();
				ArrayList<Authordata.datas> getdata=(ArrayList<Authordata.datas>) msg.obj;
				if(ISWATH==Constants.REFRESH){
					ArrayList<Authordata.datas> temp =new ArrayList<Authordata.datas>();
					if(getdata!=null && getdata.size()!=0){
						temp=datas;
						datas=getdata;
						for(int i=0;i<temp.size();i++ ){
							datas.add(temp.get(i));
						}
						mAdapter.notifyDataSetChanged();
					}else{
						Toast.makeText(getApplicationContext(), "没有新的委托信息", 0).show();
					}
				}else if(getdata!=null && getdata.size()!=0){
					for(int i=0;i<getdata.size();i++ ){
						datas.add(getdata.get(i));
					}
					mAdapter.notifyDataSetChanged();
				}else{
					Toast.makeText(getApplicationContext(), "没有更多委托信息", 0).show();
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
	//验证是否有权限
		public void isSubParent() {
			if(Net.isNetworkAvailable(getApplicationContext())){
				return;
			}
			lldialog=LodingDialog.DialogFactor(Authorize.this, "正在获取数据", false);
			new Thread(new Runnable() {
		        public void run() {
		            String service = "subParentService";
		            String method = "isSubParent";
		            String token = MD5.MD5(service+method+Constants.Key);
		            String params = 
		            		"{studentId:'"+Userinfo.getchildid(Authorize.this)+
		            		"',parentId:'"+Userinfo.getid(Authorize.this)+
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
		    				JSONObject jsonObject = new JSONObject(jsonData);
		    				isReceive=jsonObject.getBoolean("isReceive");
		    				runOnUiThread(new Runnable() {
								public void run() {
									loadData(Constants.DOWNLOAD);
								}
							});
//		    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<ArrayList<ParentData>>(){}.getType());
//		    				handler.sendMessage(handler.obtainMessage(Constants.GETPANRENTLIST, object));
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
	/**
	 * 列表
	 */
	private void agentOrderService() {
		if(Net.isNetworkAvailable(getApplicationContext()) ){
			handler.sendMessage(handler.obtainMessage(406));
			return;
		}
		final String pageNum=""+this.pageNum;
		final String pageSize="10";
//		lldialog=LodingDialog.DialogFactor(Authorize.this, "正在获取数据", true);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "agentOrderService";
	            String method = "searchPage";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = 
	            		"{studentId:'"+Userinfo.getchildid(Authorize.this)+
	            		"',loginId:'"+Userinfo.getmobile_phone(Authorize.this)+
	            		"',pageNum:"+"1"+
	            		",pageSize:"+"100"+
	            		",startTime:'"+starttime+//20天前的日期
	            		"',token:'"+token+
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
	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<ArrayList<Authordata.datas>>(){}.getType());
	    				handler.sendMessage(handler.obtainMessage(Constants.GETAUTHORIZE, object));
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
			if(isReceive){
				Intent intent =new Intent(Authorize.this,AuthorApply.class);
				startActivityForResult(intent, 0);
			}else{
				Toast.makeText(getApplicationContext(), "您没有添加委托的权限", 0).show();
			}
			break;
		}
	}

	private void loadData(int what){
		switch (what) {
		case Constants.REFRESH:
			pageNum=1;
			ISWATH=Constants.REFRESH;
			if(datas.size()!=0){
				starttime=datas.get(0).getCreateDate();
			}else{
				handler.sendMessage(handler.obtainMessage(406));
				break;
			}
			agentOrderService();
			break;

		case Constants.DOWNLOAD:
			System.out.println("加载datas.size()=="+datas.size());
			if((datas.size()%10)==0){//取余等于0,10的倍数
				pageNum=datas.size()/10+1;
				starttime=Timetool.daybefor(20);
				ISWATH=Constants.DOWNLOAD;
				agentOrderService();
			}else{
				handler.sendMessage(handler.obtainMessage(406));
				Toast.makeText(getApplicationContext(), "没有更多委托信息", 0).show();
			}
			
			break;
		}
	}
}
