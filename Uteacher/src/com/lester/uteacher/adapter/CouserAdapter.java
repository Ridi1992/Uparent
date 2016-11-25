package com.lester.uteacher.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import com.lester.uteacher.AddCourse;
import com.lester.uteacher.R;
import com.lester.uteacher.texttool.Texttool;
import com.teacher.data.CouserData;
import com.teacher.datedialog.DateTimePickerDialog;
import com.teacher.datedialog.DateTimePickerDialog.OnDateTimeSetListener;

public class CouserAdapter extends BaseAdapter{

	private AddCourse mcontext;
	ArrayList<CouserData> data;
	private int page;
	public CouserAdapter(ArrayList<CouserData> arrayList, AddCourse addCourse,int page) {
		// TODO Auto-generated constructor stub
		this.data=arrayList;
		this.mcontext=addCourse;
		this.page=page;
	}

	@Override
	public int getCount() {
		if(data!=null){
			return data.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public View getView( int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		final int j=position;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=mcontext.getLayoutInflater().inflate(R.layout.couserlist_contentitem, null);
			holder.starttime=(TextView) convertView.findViewById(R.id.starttime);
			holder.endtime=(TextView) convertView.findViewById(R.id.endtime);
			holder.titel=(TextView) convertView.findViewById(R.id.titel);
			holder.content=(TextView) convertView.findViewById(R.id.content);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.starttime.setText(data.get(position).getStarttime());
		holder.endtime.setText(data.get(position).getEndtime());
		holder.titel.setText(data.get(position).getTitle());
		holder.content.setText(data.get(position).getContent());
		if(!mcontext.show){//添加状态下才可以点击
			holder.starttime.setHint("选择");
			holder.endtime.setHint("选择");
			holder.content.setHint("点击添加");
			holder.titel.setHint("点击添加");
			holder.starttime.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					showstartdateDialog(j);
				}
			});
			holder.endtime.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					showenddateDialog(j);
				}
			});
			holder.content.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					showEidtDialog(j,1,mcontext.couserdata.get(page).get(j).getContent());
				}
			});
//			holder.titel.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					showEidtDialog(j,0,mcontext.couserdata.get(page).get(j).getTitle());
//				}
//			});
		}
		/*if(position%2==0){
			convertView.setBackgroundColor(mcontext.getResources().getColor(R.color.white));
		}else{
			convertView.setBackgroundColor(mcontext.getResources().getColor(R.color.backhuise));
		}*/
		return convertView;
	}
	private class ViewHolder{
		TextView starttime;
		TextView endtime;
		TextView titel;
		TextView content;
	}
	
	private void showEidtDialog(final int position,final int type,String hint){
		final Dialog dialog = new Dialog(mcontext, R.style.MyDialog);   
		dialog.setCanceledOnTouchOutside(true);
		final View view=(View)mcontext.getLayoutInflater().inflate(R.layout.dialogeditcontent, null);
		final EditText et=(EditText) view.findViewById(R.id.et);
		Texttool.setText(et, hint);
		view.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				reTurncontent(position,et.getText().toString(),type);
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
	}
	
	private void  reTurncontent(int position,String content,int type){
		if(type==0){//改标题
			for(int i=0;i<mcontext.couserdata.size();i++){
				mcontext.couserdata.get(i).get(position).setTitle(content);
			}
		}else{
			mcontext.couserdata.get(page).get(position).setContent(content);
		}//改内容
		mcontext.refresh();
	}
	private void  reTurnstartdate(int position,String time){
		for(int i=0;i<mcontext.couserdata.size();i++){
			mcontext.couserdata.get(i).get(position).setStarttime(time);
		}
		mcontext.refresh();
	}
	private void  reTurnenddate(int position,String time){
		for(int i=0;i<mcontext.couserdata.size();i++){
			mcontext.couserdata.get(i).get(position).setEndtime(time);
		}
		mcontext.refresh();
	}
	
	public void showstartdateDialog(final int position)
	{
		DateTimePickerDialog dialog  = new DateTimePickerDialog(mcontext, System.currentTimeMillis());
		dialog.setOnDateTimeSetListener(new OnDateTimeSetListener()
	      {
			public void OnDateTimeSet(AlertDialog dialog, long date)
			{
				reTurnstartdate(position,getStringDate(date).subSequence(11, 16).toString());
			}
		});
		dialog.show();
	}
	public void showenddateDialog(final int position)
	{
		DateTimePickerDialog dialog  = new DateTimePickerDialog(mcontext, System.currentTimeMillis());
		dialog.setOnDateTimeSetListener(new OnDateTimeSetListener()
		{
			public void OnDateTimeSet(AlertDialog dialog, long date)
			{
				reTurnenddate(position,getStringDate(date).subSequence(11, 16).toString());
			}
		});
		dialog.show();
	}
	/**
	* 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
	*
	*/
	public static String getStringDate(Long date) 
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(date);
		return dateString;
	}
}
