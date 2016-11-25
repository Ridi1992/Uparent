package com.lester.uparent;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.dream.framework.utils.dialog.LodingDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lester.headview.CustomImageView;
import com.lester.school.loader.ImageLoader;
import com.lester.school.requst.Constants;
import com.lester.school.requst.PublicRequest;
import com.lester.uparent.R;
import com.lester.uparent.publicclass.Childdatas;
import com.lester.uparent.publicclass.Childdatas.datas;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.Timetool;
import com.lester.uteacher.tool.Userinfo;





public final class SelectMyChild extends Activity implements
OnClickListener,OnItemClickListener{

	static final int MENU_MANUAL_REFRESH = 0;
	static final int MENU_DISABLE_SCROLL = 1;
	static final int MENU_SET_MODE = 2;
	static final int MENU_DEMO = 3;
	private PullToRefreshListView mPullRefreshListView;
	private BaseAdapter mAdapter;
	private LodingDialog lldialog;
	private List<Childdatas.datas> getdata;
	
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
		setContentView(R.layout.selectmychild);
		lldialog=LodingDialog.DialogFactor(SelectMyChild.this, "正在获取数据", true);
		PublicRequest.getInstance(handler).selectmychild(Constants.SELECTMYCHILD,
				Userinfo.getid(SelectMyChild.this), 
				Userinfo.getmobile_phone(SelectMyChild.this));
		
		setadapter();
		initview();
	}

	private void setadapter() {
		mAdapter=new BaseAdapter() {
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				convertView=getLayoutInflater().inflate(R.layout.mychilditem, null);
				Texttool.setText(convertView, R.id.name, getdata.get(position).getName());
				int age=Integer.parseInt(getdata.get(position).getBirthday().substring(0,4));
				Texttool.setText(convertView, R.id.age, (Timetool.getyear()-age)+"岁");
				Texttool.setText(convertView, R.id.schoolName, getdata.get(position).getSchoolName());
				CustomImageView imageView=((CustomImageView)convertView.findViewById(R.id.childhead));
				 if(getdata.get(position).getPhoto()!=null &&
						 (!getdata.get(position).getPhoto().equals(""))){
					 new ImageLoader(SelectMyChild.this).DisplayImage(getdata.get(position).getPhoto(), imageView);
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
				if(getdata!=null){
					return getdata.size();
				}else{
					return 0;
				}
			}
		};
		
	}

	@SuppressLint("HandlerLeak")
	private void initview() {
		findViewById(R.id.back).setOnClickListener(this);;
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				
			}
		});

		ListView actualListView = mPullRefreshListView.getRefreshableView();
		actualListView.setAdapter(mAdapter);
		actualListView.setOnItemClickListener(this);
		mPullRefreshListView.setMode(Mode.DISABLED);//设置刷新方式
	}
	Handler handler=new Handler(){
		@SuppressLint("ShowToast")
		public void handleMessage(android.os.Message msg) {
			try {
			
				if(lldialog!=null){
					lldialog.dismiss();
			}
			switch (msg.what) {
			case Constants.SELECTMYCHILD:
				getdata =(List<datas>) msg.obj;
				if(getdata.size()!=0){
					mAdapter.notifyDataSetChanged();
				}
				break;

			case 404:
				Toast.makeText(getApplicationContext(), msg.obj.toString(), 0).show();
				break;
			}
			} catch (Exception e) {
				// TODO: handle exception
			}
		};
	};
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		 
		try {
			System.out.println("点击了第"+arg2);
			Userinfo.setchildinfo(SelectMyChild.this,getdata.get(arg2-1));
			Intent intent= new Intent ();
			intent.setClass(SelectMyChild.this, MainActivity.class);
			startActivity(intent);
			finish();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
	}
}
