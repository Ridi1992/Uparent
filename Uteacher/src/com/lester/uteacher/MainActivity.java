package com.lester.uteacher;

import java.util.ArrayList;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.baidu.mobstat.SendStrategyEnum;
import com.baidu.mobstat.StatService;
import com.example.jpushdemo.ExampleApplication;
import com.example.jpushdemo.ExampleUtil;
import com.lester.uteacher.adapter.HomeGridAdapter;
import com.lester.uteacher.info.Teacherinfo;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.Fixpic;
import com.lester.uteacher.tool.Userinfo;
import com.zxing.activity.CaptureActivity;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;

public class MainActivity extends Activity {
	public static MainActivity activity;
	private HomeGridAdapter gridadapter;
	
	private NotificationCompat.Builder mBuilder;
	public NotificationManager mNotificationManager;
	private String TAG="极光";
	private int notifyId = 100;
	public static boolean isForeground = true;
	

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
		setContentView(R.layout.activity_main);
		activity=this;
		indata();
		initview();
		
		registerMessageReceiver();  // used for receive msg
		((ExampleApplication) getApplication()).setAlias(Userinfo.getmobile_phone(MainActivity.this));
	
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
    
	private void indata() {
		String str=Userinfo.getschoolName(MainActivity.this);
		Texttool.setText(MainActivity.this, R.id.title, str);
		
		ArrayList<Integer> imgid=new ArrayList<Integer>();
		ArrayList<String> itemname=new ArrayList<String>();
		imgid.add(R.drawable.homeitem1);
		imgid.add(R.drawable.homeitem2);
		imgid.add(R.drawable.homeitem3);
		imgid.add(R.drawable.homeitem4);
		imgid.add(R.drawable.homeitem5);
		imgid.add(R.drawable.homeitem6);
		imgid.add(R.drawable.homeitem7);
		imgid.add(R.drawable.homeitem8);
		imgid.add(R.drawable.homeitem9);
//		imgid.add(R.drawable.homeitem10);
		imgid.add(R.drawable.homeitem11);
		imgid.add(R.drawable.homeitem12);
		imgid.add(R.drawable.homeitem13);
		imgid.add(R.drawable.homeitem14);
		imgid.add(R.drawable.homeitem15);
		imgid.add(R.drawable.aboutour);
		itemname.add("离校扫码");
		itemname.add("校车入校");
		itemname.add("校车离校");
		itemname.add("入校接收");
		itemname.add("异常接送");
		itemname.add("病事假单");
		itemname.add("接送状态");
		itemname.add("班级公告");
		itemname.add("校园公告");
//		itemname.add("在线互动");
		itemname.add("日常信息");
		itemname.add("老师信息");
		itemname.add("每周课程");
		itemname.add("每日表现");
		itemname.add("每周食谱");
		itemname.add("关于我们");
		gridadapter=new HomeGridAdapter(getApplicationContext(),imgid,itemname,getWindowManager());
	}
	private void initview() {
		// TODO Auto-generated method stub
//		ImageView img=(ImageView) findViewById(R.id.homeimg);
//		Fixpic fix=new Fixpic();
//		fix.setView_W_H(getWindowManager(),img,
//				BitmapFactory.decodeResource(
//						 getApplicationContext().getResources(), R.drawable.u117));
		GridView gridview=(GridView) findViewById(R.id.gripview);
		gridview.setAdapter(gridadapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				switch (arg2) {
				case 0:
//					intent.setClass(getApplicationContext(), GenerateCode.class);
//					startActivity(intent);
					Intent openCameraIntent = new Intent(MainActivity.this,CaptureActivity.class);
					startActivity(openCameraIntent);
					break;
				case 1://入校上车
					intent.setClass(getApplicationContext(), GotOn.class);
					intent.putExtra("info", 1);
					startActivity(intent);
					break;
				case 2://离校上车
					intent.setClass(getApplicationContext(), GotOn.class);
					intent.putExtra("info", 2);
					startActivity(intent);
					break;
				case 3://入校接送
					intent.setClass(getApplicationContext(), GotOn.class);
					intent.putExtra("info", 3);
					startActivity(intent);
					break;
				case 4://接送登记
					intent.setClass(getApplicationContext(), Studentlogin.class);
					startActivity(intent);
					break;
				case 5://请假单
					intent.setClass(getApplicationContext(), LeaveList.class);
					startActivity(intent);
					break;
				case 6://我的学生
					intent.setClass(getApplicationContext(), My_student.class);
					startActivity(intent);
					break;
				case 7://班级公告
					intent.setClass(getApplicationContext(), Announcement_Class.class);
					startActivity(intent);
					break;
				case 8://校园公告
					intent.setClass(getApplicationContext(), Announcement.class);
					startActivity(intent);
					break;
				case 9://我的消息
					intent.setClass(getApplicationContext(), My_message.class);
					startActivity(intent);
					break;
				case 10://个人信息
					intent.setClass(getApplicationContext(), Me.class);
					startActivity(intent);
					break;
				case 11://课程表
					intent.setClass(getApplicationContext(), Courselist.class);
					startActivity(intent);
					break;
				case 12://反馈
					intent.setClass(getApplicationContext(), Feedback_selectChild.class);
					startActivity(intent);
					break;
				case 13://每周食谱
					intent.setClass(getApplicationContext(), RecipesList.class);
					startActivity(intent);
					break;
				case 14://关于我们
					intent.setClass(getApplicationContext(), Aboutour.class);
					startActivity(intent);
					break;
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
		if(model==null || !model.equals("teacher")){
			return;
		}
		if(type!=null && type.equals("message")){

			mBuilder.setAutoCancel(true)//点击后让通知将消失  
					.setContentTitle("优管家")
					.setContentText(messge)
					.setTicker("优管家："+"您有新的消息");
			//点击的意图ACTION是跳转到Intent
			Intent resultIntent = new Intent(this, My_message.class);
//			resultIntent.putExtra("id", "132");
			resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			mBuilder.setContentIntent(pendingIntent);
			mNotificationManager.notify(notifyId, mBuilder.build());
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mMessageReceiver);
	}
}
