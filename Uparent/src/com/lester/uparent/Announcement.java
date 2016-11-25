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
import android.app.ListActivity;
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
import android.widget.ImageView;
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
import com.lester.school.loader.myImageLoader;
import com.lester.school.requst.Constants;
import com.lester.school.requst.JsonUtil;
import com.lester.uparent.R;
import com.lester.uparent.announcement.Announcementdata;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.lester.uteacher.tool.Userinfo;





public final class Announcement extends ListActivity{
	private int pageNum=1;//分页起始页
	private int dpageNum=1;
//	private Boolean CANGETDATA=true;//是否可以请求数据
	private PullToRefreshListView mPullRefreshListView;
	private BaseAdapter mAdapter;
	private LodingDialog lldialog;
	private myImageLoader mImageLoader;
	private ArrayList<Announcementdata.datas> datas =new ArrayList<Announcementdata.datas>();
	private int ISWATH;
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
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.announcement);
//		getannouncement();
		setAdapter();
		initview();
		lldialog=LodingDialog.DialogFactor(Announcement.this, "正在获取数据", true);
		loadData(Constants.DOWNLOAD);
	}

	

	private void setAdapter() {
		mAdapter=new BaseAdapter() {
			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				if(convertView==null){
					convertView=getLayoutInflater().inflate(R.layout.announcement_item, null);
				}
				ImageView headimg=(ImageView) convertView.findViewById(R.id.titleimg);
				mImageLoader=new myImageLoader(getApplicationContext());
				 if(datas.get(position).getTitleImg()!=null){
					 mImageLoader.DisplayImage(datas.get(position).getTitleImg(), 
							 headimg);
				 }else{
					 headimg.setImageResource(R.drawable.u122);
				 }
				
				Texttool.setText(convertView, R.id.title, datas.get(position).getTitle());
				String date=datas.get(position).getRelease_time().substring(0, 10);
				Texttool.setText(convertView, R.id.release_time, date);
				convertView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent= new Intent(getApplicationContext(),Announcementdetail.class);
						intent.putExtra("id", datas.get(position).getId());
						intent.putExtra("serveice", "schoolNoticeService");
						startActivity(intent);
					}
				});
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
			case Constants.GETANOUNCEMENT:
				mPullRefreshListView.onRefreshComplete();
				
				ArrayList<Announcementdata.datas> getdata=(ArrayList<Announcementdata.datas>) msg.obj;
				if(ISWATH==Constants.REFRESH){
					ArrayList<Announcementdata.datas> temp =new ArrayList<Announcementdata.datas>();
					if(getdata!=null && getdata.size()!=0){
						temp=datas;
						datas=getdata;
						for(int i=0;i<temp.size();i++ ){
							datas.add(temp.get(i));
						}
						mAdapter.notifyDataSetChanged();
					}else{
						Toast.makeText(getApplicationContext(), "没有新的公告", 0).show();
					}
				}else if(getdata!=null && getdata.size()!=0){
					for(int i=0;i<getdata.size();i++ ){
						datas.add(getdata.get(i));
					}
					mAdapter.notifyDataSetChanged();
				}else{
					Toast.makeText(getApplicationContext(), "没有更多公告", 0).show();
				}
				
				break;
			case 404:
				mPullRefreshListView.onRefreshComplete();
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
	 * 获取公告内容
	 */
	private void getannouncement() {
		if(Net.isNetworkAvailable(getApplicationContext())){
			handler.sendMessage(handler.obtainMessage(406));
			return;
		}
		final String pageNum=""+this.pageNum;
		final String pageSize="10";
		new Thread(new Runnable() {
	        public void run() {
	            String service = "schoolNoticeService";
	            String method = "searchPage";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = 
	            		"{schoolId:'"+Userinfo.getchildschoolid(Announcement.this)+
	            		"',pageNum:"+pageNum+
	            		",pageSize:"+pageSize+
	            		",loginId:'"+Userinfo.getmobile_phone(Announcement.this)+
	            		"',token:'"+token+
	            		"',maxTime:'"+maxtime+
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
	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<ArrayList<Announcementdata.datas>>(){}.getType());
	    				handler.sendMessage(handler.obtainMessage(Constants.GETANOUNCEMENT, object));
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
			Announcement.this.pageNum=1;
			if(datas.size()!=0){
				maxtime=datas.get(0).getRelease_time();
				ISWATH=Constants.REFRESH;
				getannouncement();
			}else{
				mPullRefreshListView.onRefreshComplete();
			}
			break;

		case Constants.DOWNLOAD:
				pageNum=dpageNum++;
				maxtime="";
				ISWATH=Constants.DOWNLOAD;
				getannouncement();
			
			break;
		}
	}
}
