package com.lester.uteacher.adapter;

import java.util.ArrayList;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lester.headview.CustomImageView;
import com.lester.school.loader.ImageLoader;
import com.lester.slidecutlistview.CustomSwipeListView;
import com.lester.uparent.My_message;
import com.lester.uparent.ParentManage;
import com.lester.uparent.R;
import com.lester.uteacher.texttool.Texttool;
import com.teacher.data.ParentData;

public class SlidListviewAdapter extends BaseAdapter {
private ArrayList<ParentData> data;
private ParentManage context;
private ImageLoader imageLoader;
	public SlidListviewAdapter(ParentManage context) {
		this.context=context;
		imageLoader=new ImageLoader(context);
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		if(convertView == null ){
			convertView=LayoutInflater.from(context).inflate(R.layout.listview_item_slideview, null);
		}
		CustomImageView imageView=(CustomImageView) convertView.findViewById(R.id.head);
		if(data.get(position).getPhoto()!=null){
			imageLoader.DisplayImage(data.get(position).getPhoto(), imageView);
		}else{
			imageView.setImageResource(R.drawable.parent);
		}
		Texttool.setText(convertView, R.id.name, data.get(position).getSubParentName());
		TextView info=(TextView) convertView.findViewById(R.id.info);
		if(data.get(position).getIsReceive()){
			Texttool.setText(convertView, R.id.info, "可接送");
			info.setBackgroundResource(R.drawable.ischeshang);
		}else{
			Texttool.setText(convertView, R.id.info, "不可接送");
			info.setBackgroundResource(R.drawable.nohave);
		}
		
		TextView bt=(TextView) convertView.findViewById(R.id.bt);
	       final View view=convertView;
			bt.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new  CustomSwipeListView(context).cancelSlide(view);
					context.selectposition=position;
					context.delete(data.get(position).getpId());
				}
			});
		return convertView;
	}

	public void setData(ArrayList<ParentData> data) {
		this.data=data;
	}

}
