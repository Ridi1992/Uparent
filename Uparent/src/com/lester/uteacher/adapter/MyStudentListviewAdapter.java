package com.lester.uteacher.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lester.uparent.R;

public class MyStudentListviewAdapter extends BaseAdapter {
private ArrayList<String> name;
private Context context;
//private Boolean [] ischeck;
	public MyStudentListviewAdapter(Context context, ArrayList<String> name) {
		this.name=name;
		this.context=context;
//		if(name.size()!=0){
//			ischeck=new Boolean[name.size()];
//			for(int i=0; i<name.size();i++){
//				ischeck[i]=false;
//			}
//		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return name.size();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		if(convertView == null ){
			convertView=LayoutInflater.from(context).inflate(R.layout.mystudentlistview_item, null);
		}
//		TextView bt=(TextView) convertView.findViewById(R.id.bt);
//	       final View view=convertView;
//			bt.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					System.out.println("撤销按钮");
////					new  CustomSwipeListView(context).cancelSlide(view);
//				}
//			});
//		final CheckBox ch=(CheckBox) convertView.findViewById(R.id.check);
//		ch.setChecked(ischeck[position]);
//		ch.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				ischeck[position]=ch.isChecked();
//			}
//		});	
		return convertView;
	}

}
