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
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
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
import com.lester.school.requst.Constants;
import com.lester.school.requst.JsonUtil;
import com.lester.uteacher.leave.Leavelist;
import com.lester.uteacher.mymessage.Memessage;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.lester.uteacher.tool.Userinfo;





public final class My_message extends Activity {
	private int pageNum=1;//分页起始页
	private int dpageNum=1;//加载分页
	private PullToRefreshListView mPullRefreshListView;
	private BaseAdapter mAdapter;
	private LodingDialog lldialog;
	private ArrayList<Memessage.datas> datas =new ArrayList<Memessage.datas>();
	private String selectclassid;
	private View myview;
	private String id;
	private int reposition;
	private int ISWATH;
	private String maxtime="";
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
		setContentView(R.layout.my_message);
		setAdapter();
		initview();
		loadData(Constants.DOWNLOAD);
		lldialog=LodingDialog.DialogFactor(My_message.this, "正在获取数据", true);
	}

	private void setAdapter() {
		mAdapter=new BaseAdapter() {
			@Override
			public View getView(final int position,View convertView, ViewGroup parent) {
				convertView=getLayoutInflater().inflate(R.layout.mymessage_item, null);
				Texttool.setText(convertView, R.id.content, datas.get(position).getContent());
//				Texttool.setText(convertView, R.id.title, datas.get(position).get);
				String date=datas.get(position).getSend_time();
				Texttool.setText(convertView, R.id.release_time, date);
				final View view=convertView;
				convertView.setOnLongClickListener(new OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {

						System.out.println("长安了");
						final Dialog dialog = new Dialog(My_message.this, R.style.MyDialog);   
						dialog.setCanceledOnTouchOutside(true);
						View view=(View)getLayoutInflater().inflate(R.layout.dialogmakesure, null);
						Texttool.setText(view, R.id.title, "删除这条消息");
						view.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								reposition=position;
								Deletedata(datas.get(position).getId());
								dialog.dismiss();
							}
						});
						view.findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						});
							dialog.setContentView(view);
							dialog.show();
						return false;
					}
				});
				convertView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(!datas.get(position).getHas_read()){
							reposition=position;
							id=datas.get(position).getId();
							hasRead();
						}
						
					}
				});
				setstatus(convertView,datas.get(position).getHas_read());
				
				return convertView;
			}
			
			
			@Override
			public long getItemId(int position) {
				return 0;
			}
			@Override
			public Object getItem(int position) {
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
	private void setstatus(View convertView, Boolean has_read) {
		if(!has_read){
			convertView.setClickable(true);
			TextView te=(TextView) convertView.findViewById(R.id.log);
			te.setBackgroundResource(R.drawable.u70);
		}else{
			convertView.setClickable(false);
			TextView te=(TextView) convertView.findViewById(R.id.log);
			te.setVisibility(View.GONE);
		}
	}
	@SuppressWarnings("unchecked")
	private void initview() {
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
		
		findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			try {
				if(lldialog!=null){
					lldialog.dismiss();
				}
			switch (msg.what) {
			case Constants.MYMESSAGE:
				mPullRefreshListView.onRefreshComplete();
				ArrayList<Memessage.datas> getdata=(ArrayList<Memessage.datas>) msg.obj;
				System.out.println("数据");
				if(ISWATH==Constants.REFRESH){
					ArrayList<Memessage.datas> temp =new ArrayList<Memessage.datas>();
					if(getdata!=null && getdata.size()!=0){
						temp=datas;
						datas=getdata;
						for(int i=0;i<temp.size();i++ ){
							datas.add(temp.get(i));
						}
						mAdapter.notifyDataSetChanged();
					}else{
						Toast.makeText(getApplicationContext(), "没有新的信息", 0).show();
					}
				}else if(getdata!=null && getdata.size()!=0){
					for(int i=0;i<getdata.size();i++ ){
						datas.add(getdata.get(i));
					}
					System.out.println("加载返回");
					mAdapter.notifyDataSetChanged();
				}else{
					Toast.makeText(getApplicationContext(), "没有更多信息", 0).show();
				}
				break;
			case Constants.HASREAD://点击后状态变为已读
				if(msg.obj.toString().equals("提交成功")){
					System.out.println("已读");
					datas.get(reposition).setHas_read(true);
					mAdapter.notifyDataSetChanged();
				}
				break;
				
			case Constants.DELETEDATA://点击后状态变为已读
				if(msg.obj.toString().equals("提交成功")){
					datas.remove(reposition);
					mAdapter.notifyDataSetChanged();
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
	 * 我的消息
	 */
	private void getmymessage() {
		if(Net.isNetworkAvailable(getApplicationContext())){
			handler.sendMessage(handler.obtainMessage(406));
			return;
		}
		final String pageNum=""+this.pageNum;
		final String pageSize="10";
//		lldialog=LodingDialog.DialogFactor(My_message.this, "正在获取数据", true);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "messageService";
	            String method = "searchPageByLoginId";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = 
	            		"{loginId:'"+Userinfo.getmobile_phone(My_message.this)+
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
	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<ArrayList<Memessage.datas>>(){}.getType());
	    				handler.sendMessage(handler.obtainMessage(Constants.MYMESSAGE, object));
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
	 * 删除消息
	 * @param string 
	 */
	private void Deletedata(final String messgaeid) {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		
		lldialog=LodingDialog.DialogFactor(My_message.this, "正在提交数据", true);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "messageService";
	            String method = "deleteMessage";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = "{id:'"+messgaeid+"'}";
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
//	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<ArrayList<Memessage.datas>>(){}.getType());
	    				handler.sendMessage(handler.obtainMessage(Constants.DELETEDATA, "提交成功"));
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
	 * 已读消息
	 */
	private void hasRead() {
		if(Net.isNetworkAvailable(getApplicationContext())){
			handler.sendMessage(handler.obtainMessage(406));
			return;
		}
		lldialog=LodingDialog.DialogFactor(My_message.this, "正在提交数据", true);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "messageService";
	            String method = "hasRead";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = "{id:'"+id+"'}";
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
//	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<ArrayList<Memessage.datas>>(){}.getType());
	    				handler.sendMessage(handler.obtainMessage(Constants.HASREAD, "提交成功"));
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
	
	private void loadData(int what){
		if(Net.isNetworkAvailable(getApplicationContext())){
			handler.sendMessage(handler.obtainMessage(406));
			return;
		}
		switch (what) {
		case Constants.REFRESH:
			pageNum=1;
			if(datas.size()!=0){
				maxtime=datas.get(0).getSend_time();
				ISWATH=Constants.REFRESH;
				getmymessage();
			}else{
				mPullRefreshListView.onRefreshComplete();
			}
			
			break;

		case Constants.DOWNLOAD:
//			System.out.println("加载datas.size()=="+datas.size());
//			if((datas.size()%10)==0){//取余等于0,10的倍数
//				pageNum=datas.size()/10+1;
				pageNum=dpageNum++;
				maxtime="";
				ISWATH=Constants.DOWNLOAD;
				getmymessage();
//			}else{
//				handler.sendMessage(handler.obtainMessage(406));
//				Toast.makeText(getApplicationContext(), "没有更多信息", 0).show();
//			}
			
			break;
		}
	}
}
