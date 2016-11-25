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
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.baidu.mobstat.SendStrategyEnum;
import com.baidu.mobstat.StatService;
import com.best.cropimage.CropImageActivity;
import com.example.jpushdemo.ExampleApplication;
import com.example.jpushdemo.ExampleUtil;
import com.lester.company.http.HttpURL;
import com.lester.fragment.baseFragment;
import com.lester.fragment.homeFragment;
import com.lester.fragment.meFragment;
import com.lester.fragment.moreFragment;
import com.lester.headview.CustomImageView;
import com.lester.parent.me.Parentinfodata;
import com.lester.school.loader.ImageLoader;
import com.lester.school.requst.Constants;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.lester.uteacher.tool.Userinfo;

public class MainActivity extends FragmentActivity implements OnClickListener{

	private int PHOTO_REQUEST_CUT=10001;
	public static MainActivity activity;
	public static Parentinfodata.data data;//家长信息
	private NotificationCompat.Builder mBuilder;
	public NotificationManager mNotificationManager;
	private String TAG="极光";
	private int notifyId = 100;
	public static boolean isForeground = true;
	List<Fragment> fragments = new ArrayList<Fragment>();
	public meFragment mmeFragment;
	homeFragment mhomeFragment;
	public static int position_status=0;
	private ArrayList<ImageView> barimgview;
	private ArrayList<TextView> bartextview;
	private int [] img_pre={R.drawable.homebar0,R.drawable.homebar2,R.drawable.homebar4,R.drawable.homebar6};
	private int [] img_nor={R.drawable.homebar1,R.drawable.homebar3,R.drawable.homebar5,R.drawable.homebar7};
	@Override
	protected void onResume() {
		super.onResume();
	 StatService.onResume(this);
		getchildurl();
		if(mhomeFragment!=null){
			mhomeFragment.setChildname();
		}
	}
	@Override
    public void onPause() {
        super.onPause();

        StatService.onPause(this);
    }
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		
	
		activity=this;
		Texttool.setText(MainActivity.this, R.id.title, Userinfo.getchildschool(MainActivity.this));
		initview();
		
    	setfragment(0);
		
		registerMessageReceiver();  // used for receive msg
		((ExampleApplication)getApplication()).setAlias(Userinfo.getmobile_phone(MainActivity.this));
		StatService();
	}
	
	   
    /**
     * 百度统计
     */
    private void StatService(){
    	StatService.setSessionTimeOut(30);
        StatService.setOn(this, StatService.EXCEPTION_LOG);
        StatService.setLogSenderDelayed(0);
        StatService.setSendLogStrategy(this, SendStrategyEnum.SET_TIME_INTERVAL, 1, false);
        StatService.setDebugOn(true);
    }
    
	private void initview(){
		findViewById(R.id.selectchild).setOnClickListener(this);
		
        mhomeFragment=new homeFragment();
        baseFragment mbaseFragment=new baseFragment();
        moreFragment mmoreFragment=new moreFragment();
        mmeFragment=new meFragment();
		fragments.add(mhomeFragment);
		fragments.add(mbaseFragment);
		fragments.add(mmoreFragment);
		fragments.add(mmeFragment);
		
		barimgview=new ArrayList<ImageView>();
		bartextview=new ArrayList<TextView>();
		barimgview.add((ImageView) findViewById(R.id.bar_img0));
		barimgview.add((ImageView) findViewById(R.id.bar_img1));
		barimgview.add((ImageView) findViewById(R.id.bar_img2));
		barimgview.add((ImageView) findViewById(R.id.bar_img3));
		bartextview.add( (TextView) findViewById(R.id.bar_text0));
		bartextview.add( (TextView) findViewById(R.id.bar_text1));
		bartextview.add( (TextView) findViewById(R.id.bar_text2));
		bartextview.add( (TextView) findViewById(R.id.bar_text3));
		findViewById(R.id.bar0).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setfragment(0);
			}
		});
		findViewById(R.id.bar1).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setfragment(1);
			}
		});
		findViewById(R.id.bar2).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setfragment(2);
			}
		});
		findViewById(R.id.bar3).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setfragment(3);
			}
		});
	}
	
	private void setfragment(int i){
		for(int j=0;j<barimgview.size();j++){
			barimgview.get(j).setImageResource(img_nor[j]);
			bartextview.get(j).setTextColor(getResources().getColor(R.color.bar_text_nor));
		}
		bartextview.get(i).setTextColor(getResources().getColor(R.color.bar_text_pre));
		barimgview.get(i).setImageResource(img_pre[i]);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.layout,fragments.get(i));
		transaction.commit();
	}
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			try {
				switch (msg.what) {
				case Constants.GETCHILDURL:
					String headurl=(String) msg.obj;
					System.out.println("孩子头像是"+headurl);
					Userinfo.setchildphoto(MainActivity.this, headurl);
					if(headurl!=null){
						if(mhomeFragment!=null){
							mhomeFragment.setHead(headurl);
						}
						CustomImageView headimg=(CustomImageView)findViewById(R.id.head);
						new ImageLoader(MainActivity.this).DisplayImage(headurl, headimg);
					}
					break;
				case 404:
//					Toast.makeText(getApplicationContext(), msg.obj.toString(), 0).show();
					break;
				}
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}

	};
	private void getchildurl() {
		if(Net.isNetworkAvailable(getApplicationContext())){
			return;
		}
		new Thread(new Runnable() {
	        public void run() {
	            String service = "studentService";
	            String method = "getPhtotById";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = 
	            		"{id:'"+Userinfo.getchildid(MainActivity.this)+
	            		"'}";
	            NameValuePair pair1 = new BasicNameValuePair("service", service);
	            NameValuePair pair2 = new BasicNameValuePair("method", method);
	            NameValuePair pair3 = new BasicNameValuePair("token", token);
	            NameValuePair pair4 = new BasicNameValuePair("params", params);
	            Log.i("service", service);
		           Log.i("method", method);
		           Log.i("token", token);
		           Log.i("params", params);
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
	    				JSONObject jsonObjurl=new JSONObject(jsonData);
	    				String url=jsonObjurl.getString("photo");
	    				int position_status=jsonObjurl.getInt("position_status");
	    				setstatus(position_status);
//	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<Teacherinfo.data>(){}.getType());
	    				handler.sendMessage(handler.obtainMessage(Constants.GETCHILDURL, url));
	    			}else{
//	    				handler.sendMessage(handler.obtainMessage(404, jsonObj.getString("message")));
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
	 * 更改学生状态
	 * @param position_status
	 */
	private void setstatus(final int position_status){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(mhomeFragment!=null){
					MainActivity.this.position_status=position_status;
					mhomeFragment.setChildstatus(position_status);
				}
			}
		});
	}
	
	
	

	private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";
	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		registerReceiver(mMessageReceiver, filter);
	}
	public class MessageReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
              String messge = intent.getStringExtra(KEY_MESSAGE);
              String extras = intent.getStringExtra(KEY_EXTRAS);
              StringBuilder showMsg = new StringBuilder();
              showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
              if (!ExampleUtil.isEmpty(extras)) {
            	  showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
              }
//              setCostomMsg(showMsg.toString());
              System.out.println("收到消息"+messge+extras);
              initNotify();
              showIntentActivityNotify(messge,extras);
			}
		}
	}
	
	
	
	/** 初始化通知栏 */
	private void initNotify(){
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mBuilder = new NotificationCompat.Builder(this);
		PendingIntent pendingIntent= PendingIntent.getActivity(this, 1, new Intent(), Notification.FLAG_AUTO_CANCEL);
		mBuilder.setContentTitle("")
				.setContentText("")
				.setContentIntent(pendingIntent)
//				.setNumber(number)//显示数量
				.setTicker("")//通知首次出现在通知栏，带上升动画效果的
				.setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
				.setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
//				.setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消  
				.setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
				.setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
				.setDefaults(Notification.DEFAULT_SOUND)
//					Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
				.setSmallIcon(R.drawable.nof);
	}
	/** 显示通知栏点击跳转到指定Activity 
	 * @param data 
	 * @throws JSONException */
	public void showIntentActivityNotify(String messge, String extras){
		// Notification.FLAG_ONGOING_EVENT --设置常驻 Flag;Notification.FLAG_AUTO_CANCEL 通知栏上点击此通知后自动清除此通知
//		notification.flags = Notification.FLAG_AUTO_CANCEL; //在通知栏上点击此通知后自动清除此通知
		JSONObject jsonObj;
		String id;
		String type = null;
		String model = null;
		try {
			jsonObj = new JSONObject(extras);
			id=jsonObj.getString("id");
			type=jsonObj.getString("type");
			model=jsonObj.getString("model");
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		if(model==null || !model.equals("parent")){
			return;
		}
		if(type!=null&&type.equals("message")){

			mBuilder.setAutoCancel(true)//点击后让通知将消失  
					.setContentTitle("优管家")
					.setContentText(messge)
					.setTicker("优管家："+"您有新的消息");
			//点击的意图ACTION是跳转到Intent
			Intent resultIntent = new Intent(this, My_message.class);
			resultIntent.putExtra("id", "132");
			resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			mBuilder.setContentIntent(pendingIntent);
			mNotificationManager.notify(notifyId, mBuilder.build());
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.selectchild:
			Intent intent =new Intent(MainActivity.this,SelectMyChild.class);
			startActivity(intent);
			break;
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mMessageReceiver);
	}
}
	
