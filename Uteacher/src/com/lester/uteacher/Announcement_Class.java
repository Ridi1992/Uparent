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

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
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
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lester.company.http.HttpURL;
import com.lester.school.loader.myImageLoader;
import com.lester.school.requst.Constants;
import com.lester.school.requst.JsonUtil;
import com.lester.uparent.announcement.Announcementdata;
import com.lester.uteacher.goton.Classlist;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.lester.uteacher.tool.Userinfo;





public final class Announcement_Class extends Activity implements OnClickListener{
	private int pageNum=1;//分页起始页
	private int dpageNum=1;
	private PullToRefreshListView mPullRefreshListView;
	private BaseAdapter mAdapter;
	private LodingDialog lldialog;
	private ArrayList<Announcementdata.datas> datas =new ArrayList<Announcementdata.datas>();
	private String selectclassid="";
	private List<Classlist.datas> classdatas;
	private myImageLoader mImageLoader;
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
		setContentView(R.layout.announcement);
		getclass();
		setAdapter();
		initview();
	}

	

	private void setAdapter() {
		mAdapter=new BaseAdapter() {
			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				convertView=getLayoutInflater().inflate(R.layout.announcement_item, null);
				ImageView headimg=(ImageView) convertView.findViewById(R.id.titleimg);
				mImageLoader=new myImageLoader(getApplicationContext());
				 if(datas.get(position).getTitleImg()!=null){
					 mImageLoader.DisplayImage(datas.get(position).getTitleImg(), 
							 headimg);
				 }else{
					 headimg.setImageResource(R.drawable.logo);
				 }
				Texttool.setText(convertView, R.id.title, datas.get(position).getTitle());
				String date=datas.get(position).getRelease_time().substring(0, 10);
				Texttool.setText(convertView, R.id.release_time, date);
				convertView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent= new Intent(getApplicationContext(),Announcementdetail.class);
						intent.putExtra("id", datas.get(position).getId());
						intent.putExtra("serveice", "clazzNoticeService");
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
		Texttool.setText(Announcement_Class.this, R.id.bartitle, "班级公告");
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
		findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
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
					datas=new ArrayList<Announcementdata.datas>();
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
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			try {
				if(lldialog!=null && msg.what!=Constants.GETCLASS){
					lldialog.dismiss();
				}
			switch (msg.what) {
			case Constants.GETCLASS:
				classdatas=(List<Classlist.datas>) msg.obj;
				if(classdatas!=null && classdatas.size()!=0){
					if(classdatas.get(0).getId()!=null&& !classdatas.get(0).getId().equals("null")){
						selectclassid=classdatas.get(0).getId();
						loadData(Constants.DOWNLOAD);
						findViewById(R.id.selectclass).setVisibility(View.VISIBLE);//可以选择班级
					}
				}else{
					if(lldialog!=null){
						lldialog.dismiss();
					}
					Toast.makeText(getApplicationContext(), "您不是带班老师", 0).show();
					mPullRefreshListView.setMode(Mode.DISABLED);
				}
				break;
			case Constants.GETANOUNCEMENT:
				ArrayList<Announcementdata.datas> getdata=(ArrayList<Announcementdata.datas>) msg.obj;
				mPullRefreshListView.onRefreshComplete();
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
		lldialog=LodingDialog.DialogFactor(Announcement_Class.this, "正在获取数据", true);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "clazzService";
	            String method = "search";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = "{loginId:'"+Userinfo.getmobile_phone(Announcement_Class.this)+"'}";
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
	 * 获取公告内容
	 */
	private void getannouncement() {
		if(selectclassid==null
				||selectclassid.equals("")){
			handler.sendMessage(handler.obtainMessage(404,"参数错误，请检查"));
			return;
		}
		if(Net.isNetworkAvailable(getApplicationContext())){
			handler.sendMessage(handler.obtainMessage(406));
			return;
		}
		final String pageNum=""+this.pageNum;
		final String pageSize="10";
//		lldialog=LodingDialog.DialogFactor(Announcement_Class.this, "正在获取数据", true);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "clazzNoticeService";
	            String method = "searchPage";
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



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.selectclass:
			initPopWindow(R.id.selectclass);
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
				maxtime=datas.get(0).getRelease_time();
				ISWATH=Constants.REFRESH;
				getannouncement();
			}else{
				mPullRefreshListView.onRefreshComplete();
			}
			
			break;

		case Constants.DOWNLOAD:
//			System.out.println("加载datas.size()=="+datas.size());
//			if((datas.size()%10)==0){//取余等于0,10的倍数
				pageNum=dpageNum++;
				maxtime="";
				ISWATH=Constants.DOWNLOAD;
				getannouncement();
//			}else{
//				handler.sendMessage(handler.obtainMessage(406));
//				Toast.makeText(getApplicationContext(), "没有更多公告", 0).show();
//			}
			
			break;
		}
	}
}
