package com.lester.uteacher;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.dream.framework.utils.dialog.LodingDialog;
import com.lester.school.requst.Constants;
import com.lester.school.requst.PublicRequest;
import com.lester.uteacher.register.GetData;
import com.lester.uteacher.texttool.Texttool;


public class Register  extends Activity implements OnClickListener{
	private BaseAdapter adapter;
	private ArrayList<GetData> data;
	private String 	school_id;
	private LodingDialog lldialog;
	
	

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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register);
		initview();
	}

	private void initview() {
		findViewById(R.id.xuanzeyoueryuan).setOnClickListener(this);
		findViewById(R.id.sex).setOnClickListener(this);
		findViewById(R.id.makesure).setOnClickListener(this);
		findViewById(R.id.back).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.xuanzeyoueryuan:
			if(data==null){
				lldialog=LodingDialog.DialogFactor(Register.this, "正在获取数据", true);
				data=new ArrayList<GetData>();
				getschool();
			}else{
				initPopWindow(R.id.xuanzeyoueryuan,data);
			}
			
			break;
		case R.id.sex:
			ArrayList<String> data=new ArrayList<String>();
			data.add("男");
			data.add("女");
			initPopWindowSex(R.id.sex,data);
			break;
		case R.id.makesure://确认
			String 	name,
					mobilePhone,
					gender,
					idCard;
			if(!Texttool.Havecontent((TextView) findViewById(R.id.name))){
				Toast.makeText(getApplicationContext(), "姓名不能为空", 0).show();
				break;
				}
			if(!Texttool.Havecontent((TextView) findViewById(R.id.sex))){
				Toast.makeText(getApplicationContext(), "性别不能为空", 0).show();
				break;
				}
			if(!Texttool.Havecontent((TextView) findViewById(R.id.cardnumber))){
				Toast.makeText(getApplicationContext(), "身份证号不能为空", 0).show();
				break;
				}
			String str=((EditText) findViewById(R.id.cardnumber)).getText().toString();
			if(!Patternidcard(str)){
				Toast.makeText(getApplicationContext(), "请输入正确的身份证号", 0).show();
				break;
			}
			if(!Texttool.Havecontent((TextView) findViewById(R.id.xuanzeyoueryuan))){
				Toast.makeText(getApplicationContext(), "幼儿园不能为空", 0).show();
				break;
				}
			if(!Texttool.Havecontent((TextView) findViewById(R.id.phone))){
				Toast.makeText(getApplicationContext(), "电话号码不能为空", 0).show();
				break;
				}else if(!Pattern(Texttool.Gettext((TextView) findViewById(R.id.phone)))){
					Toast.makeText(getApplicationContext(), "电话号码格式不正确", 0).show();
					break;
				}
			name=Texttool.Gettext((TextView) findViewById(R.id.name));
			mobilePhone=Texttool.Gettext((TextView) findViewById(R.id.phone));
			gender=Texttool.Gettext((TextView) findViewById(R.id.sex));
			idCard=Texttool.Gettext((TextView) findViewById(R.id.cardnumber));
			lldialog=LodingDialog.DialogFactor(Register.this, "正在注册", true);
			PublicRequest.getInstance(handler).register(Constants.REGISTER,
					school_id,
					name, 
					mobilePhone, 
					gender, 
					idCard);
			break;
		}
	}
		private Boolean  Pattern(String str){
			String PHONE = "^(((13[0-9])|(15([0-9]))|(18[0-9])|(17[0-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$";
			String EMAIL = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(PHONE);
			Matcher matcher = regex.matcher(str);
			boolean flagPhone = matcher.matches();
			return flagPhone;
		}
		
		/**
		 * 验证身份证
		 */
		private Boolean  Patternidcard(String str){
			String idcard = "^([1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3})|([1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X))$";
			Pattern regex = Pattern.compile(idcard);
			Matcher matcher = regex.matcher(str);
			boolean flagPhone = matcher.matches();
			return flagPhone;
		}
		private void initPopWindowSex(final int id, final ArrayList<String> data) {
			// 加载popupWindow的布局文件
			View contentView_pop = LayoutInflater.from(getApplicationContext())
					.inflate(R.layout.pop_list, null);
			// 设置popupWindow的背景颜色
			contentView_pop.setBackgroundResource(R.color.backhuise);
			// 声明一个弹出框
			final PopupWindow popupWindow = new PopupWindow(
					findViewById(R.id.fenleilayout), 300, LayoutParams.WRAP_CONTENT);
			// 为弹出框设定自定义的布局
			popupWindow.setContentView(contentView_pop);
			final ListView mListView = (ListView) contentView_pop
					.findViewById(R.id.pop_listView);
			adapter=new BaseAdapter() {
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					convertView=getLayoutInflater().inflate(R.layout.poplistitem, null);
					((TextView) convertView.findViewById(R.id.itemtext)).setText(data.get(position));
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
					if(data!=null){
						return data.size();
					}
					return 0;
				}
			};
			mListView.setAdapter(adapter);
			mListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					TextView te=(TextView) findViewById(id);
					te.setText(data.get(arg2));
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
	/**
	 * 新建一个popupWindow弹出框 popupWindow是一个阻塞式的弹出框，这就意味着在我们退出这个弹出框之前，程序会一直等待，
	 * 这和AlertDialog不同哦，AlertDialog是非阻塞式弹出框，AlertDialog弹出的时候，后台可是还可以做其他事情的哦。
	 * @param data 
	 * @param sex 
	 */
	private void initPopWindow(final int id, final ArrayList<GetData> data) {
		// 加载popupWindow的布局文件
		View contentView_pop = LayoutInflater.from(getApplicationContext())
				.inflate(R.layout.pop_list, null);
		// 设置popupWindow的背景颜色
		contentView_pop.setBackgroundResource(R.color.backhuise);
		// 声明一个弹出框
		final PopupWindow popupWindow = new PopupWindow(
				findViewById(R.id.fenleilayout), 300, LayoutParams.WRAP_CONTENT);
		// 为弹出框设定自定义的布局
		popupWindow.setContentView(contentView_pop);
		final ListView mListView = (ListView) contentView_pop
				.findViewById(R.id.pop_listView);
		adapter=new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				convertView=getLayoutInflater().inflate(R.layout.poplistitem, null);
				((TextView) convertView.findViewById(R.id.itemtext)).setText(data.get(position).getName());
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
				if(data!=null){
					return data.size();
				}
				return 0;
			}
		};
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				TextView te=(TextView) findViewById(id);
				te.setText(data.get(arg2).getName());
				school_id=data.get(arg2).getId();
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
	private void getschool(){
			PublicRequest.getInstance(handler).getschool(Constants.GETSCHOOL);
	}
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			try {
			
			if(lldialog!=null){
				lldialog.dismiss();
			}
			switch (msg.what) {
			case Constants.REGISTER:
				if(msg.obj.toString().equals("注册成功")){
					Toast.makeText(getApplicationContext(), "注册成功", 0).show();
					finish();
				}else{
					Toast.makeText(getApplicationContext(), "注册失败!"+msg.obj.toString(), 0).show();
				}
				break;
			case Constants.GETSCHOOL:
				data=new ArrayList<GetData>();
				List<GetData> getdata=(List<GetData>) msg.obj;
				for(int i = 0; i<getdata.size();i++){
					data.add(getdata.get(i));
				}
				
				initPopWindow(R.id.xuanzeyoueryuan,data);
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
}
