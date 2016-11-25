package com.lester.uparent;

import java.io.Serializable;
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
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.lester.company.http.HttpURL;
import com.lester.headview.CustomImageView;
import com.lester.school.requst.Constants;
import com.lester.school.requst.JsonUtil;
import com.lester.slidecutlistview.CustomSwipeListView;
import com.lester.uparent.sendlog.Sendlogdata;
import com.lester.uparent.teacherlist.Teacherdata;
import com.lester.uteacher.adapter.SlidListviewAdapter;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.lester.uteacher.tool.Userinfo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author Administrator
 *教师列表
 */
public class SendLog extends Activity implements OnClickListener{
	private BaseAdapter adapter;
	private LodingDialog lldialog;
	private  ArrayList<Sendlogdata.datas>  data =new ArrayList<Sendlogdata.datas>();
	private PullToRefreshListView mPullRefreshListView;
	
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
		setContentView(R.layout.sendlog);
		setAdapter();
		initview();
		getData();
	}

	private void setAdapter() {
		adapter=new BaseAdapter() {
			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				try {
					if(convertView==null){
						convertView=getLayoutInflater().inflate(R.layout.sendlog_item, null);
					}
					CustomImageView headimg=(CustomImageView) convertView.findViewById(R.id.childhead);
//					if(data.get(position).getPhoto()!=null &&
//								 (!data.get(position).getPhoto().equals(""))){
//							new Loadhead(data.get(position).getPhoto(), headimg).execute();
//						 }
					Texttool.setText(convertView, R.id.studentname, data.get(position).getTransfer_time());

					Texttool.setText(convertView, R.id.agent_time, data.get(position).getType());
					String str="";
					if(data.get(position).getType()!=null && (data.get(position).getType().equals("离校记录"))){
						headimg.setImageResource(R.drawable.out);
						switch (data.get(position).getModel()) {
//						离校家长扫描接送(1)、校车接送(2)、委托接送(4)、拍照接送(7)、撤销（100）
						case 1:
							str="扫码接送";
							break;
						case 2:
							str="校车接送";
							break;
						case 4:
							str="委托接送";
							break;
						case 7:
							str="拍照接送";
							break;
						default:
							str="恢复";
							break;
						}
					}else if(data.get(position).getType()!=null && data.get(position).getType().equals("入校记录")){
						headimg.setImageResource(R.drawable.income);
						switch (data.get(position).getModel()) {
//						离校家长扫描接送(1)、校车接送(2)、委托接送(4)、拍照接送(7)、撤销（100）
						case 1:
							str="上校车";
							break;
						case 2:
							str="入校园";
							break;
						case 3:
							str="恢复";
							break;
						}
					}
					Texttool.setText(convertView, R.id.agent_name,str);
				} catch (Exception e) {
					// TODO: handle exception
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
				if(data!=null){
					return data.size();
				}else{
					return 0;
				}
			}
		};
		
	}

	private void initview() {
		findViewById(R.id.back).setOnClickListener(this);
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
			public void onLastItemVisible() {
//				Toast.makeText(Announcement.this, "End of List!", Toast.LENGTH_SHORT).show();
			}
		});
		ListView actualListView = mPullRefreshListView.getRefreshableView();
		registerForContextMenu(actualListView);
		actualListView.setAdapter(adapter);
		mPullRefreshListView.setMode(Mode.DISABLED);//设置刷新方式
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;

		default:
			break;
		}
		
	}

	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			try {
				if(lldialog!=null){
					lldialog.dismiss();
				}
				switch (msg.what) {
				case Constants.SENDLOGDATA:
					data=(ArrayList<Sendlogdata.datas>) msg.obj;
					if(data!=null&&data.size()!=0){
						adapter.notifyDataSetChanged();
					}
					break;
				case 404:
					Toast.makeText(getApplicationContext(), msg.obj.toString(), 0).show();
					break;
				}
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		};
	};
	/**
	 * 接送记录
	 */
	private void getData() {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		lldialog=LodingDialog.DialogFactor(SendLog.this, "正在获取数据", true);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "studentService";
	            String method = "searchInOutRecordByStudentId";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = 
	            		"{studentId:'"+Userinfo.getchildid(SendLog.this)+
	            		"',loginId:'"+Userinfo.getmobile_phone(SendLog.this)+"'}";
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
	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<ArrayList<Sendlogdata.datas>>(){}.getType());
	    				handler.sendMessage(handler.obtainMessage(Constants.SENDLOGDATA, object));
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
	
	
}
