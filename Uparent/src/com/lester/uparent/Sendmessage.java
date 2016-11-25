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

import com.baidu.mobstat.StatService;
import com.dream.framework.utils.dialog.LodingDialog;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.lester.company.http.HttpURL;
import com.lester.school.requst.Constants;
import com.lester.school.requst.JsonUtil;
import com.lester.uparent.teacherlist.GetMessgae;
import com.lester.uparent.teacherlist.Teacherdata;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.lester.uteacher.tool.Timetool;
import com.lester.uteacher.tool.Userinfo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Administrator
 *发私信
 */
public class Sendmessage extends Activity implements OnClickListener{
	private LodingDialog lldialog;
	private Teacherdata.datas datas;
	private ArrayList<GetMessgae.datas> list;
	private BaseAdapter adapter;
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
	requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		datas=(Teacherdata.datas) getIntent().getSerializableExtra("datas");
		setContentView(R.layout.sendmessgae);
		setAdapter();
		initview();
		privateChatService();
	}
	private void setAdapter() {
		adapter =new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				convertView=getLayoutInflater().inflate(R.layout.message_item, null);
				Texttool.setText(convertView, R.id.name, list.get(position).getSender_name());
				Texttool.setText(convertView, R.id.content, list.get(position).getContent());
				Texttool.setText(convertView, R.id.time, 
						list.get(position).getSend_time().substring(0,10));
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
	private void initview() {
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.makesure).setOnClickListener(this);
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
//				getannouncement();
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

		case R.id.makesure:
			if(!Texttool.Havecontent(Sendmessage.this, R.id.abnormalReason)){
				Toast.makeText(getApplicationContext(), "消息内容不能为空", 0).show();
				return;
			}
			if(Texttool.Gettext((TextView) findViewById(R.id.abnormalReason)).length()>50){
				Toast.makeText(getApplicationContext(), "消息长度不能大于50，当前长度"+
						Texttool.Gettext((TextView) findViewById(R.id.abnormalReason)), 0).show();
				return;
			}
			sendmessage();
			break;
		default:
			
			break;
		}
	}
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			try {
				if(lldialog!=null && msg.what!=Constants.GETCLASS){//获取班级列表后直接请求学生数据，所以此处不取消弹框
					lldialog.dismiss();
			}
			switch (msg.what) {
			
			case Constants.GETMESSAGE:
				list = (ArrayList<GetMessgae.datas>) msg.obj;
				adapter.notifyDataSetChanged();
				break;
			case Constants.SENDMESSAGE:
				Toast.makeText(getApplicationContext(), msg.obj.toString(), 0).show();
				((TextView) findViewById(R.id.abnormalReason)).setText("");
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
	 * 获取私信列表
	 */
	private void privateChatService() {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		lldialog=LodingDialog.DialogFactor(Sendmessage.this, "正在获取数据", true);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "privateChatService";
	            String method = "search";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = "{loginId:'"+datas.getMobile_phone()+
	            		"',receiverLoginId:'"+Userinfo.getmobile_phone(Sendmessage.this)+
	            		"',startTime:'"+Timetool.daybefor(20)+//获取20天内的消息
	            		"',endTime:'"+Timetool.getnowtime()+
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
	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<ArrayList<GetMessgae.datas>>(){}.getType());
	    				handler.sendMessage(handler.obtainMessage(Constants.GETMESSAGE, object));
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
	 * 发私信
	 */
	private void sendmessage() {
	if(Net.isNetworkAvailable(getApplicationContext())){
		return;
	}
	lldialog=LodingDialog.DialogFactor(Sendmessage.this, "正在提交数据", true);
	new Thread(new Runnable() {
        public void run() {
            String service = "privateChatService";
            String method = "save";
            String token = MD5.MD5(service+method+Constants.Key);
            String params = "{loginId:'"+Userinfo.getmobile_phone(Sendmessage.this)+
            		"',receiverLoginId:'"+datas.getMobile_phone()+
            		"',receiverName:'"+datas.getName()+
            		"',token:'"+token+
            		"',senderName:'"+Userinfo.getname(Sendmessage.this)+
            		"',content:'"+Texttool.Gettext((TextView) findViewById(R.id.abnormalReason))+
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
//    				String jsonData = jsonObj.getString("datas");
//    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<ArrayList<Student.datas>>(){}.getType());
    				handler.sendMessage(handler.obtainMessage(Constants.SENDMESSAGE, "提交成功"));
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
