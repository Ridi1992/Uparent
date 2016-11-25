package com.lester.uteacher.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lester.uparent.R;
import com.lester.uteacher.texttool.Texttool;
import com.teacher.data.CouserListData;

public class CourserlistAdapter extends BaseAdapter{

	private Context mcontext;
	private ArrayList<CouserListData> data;
	
	/**
	 * @param data the data to set
	 */
	public void setData(ArrayList<CouserListData> data) {
		this.data = data;
	}


	public CourserlistAdapter(Context mcontext) {
		this.mcontext=mcontext;
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
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView=LayoutInflater.from(mcontext).inflate(R.layout.courserlist_item, null);
		}
		Texttool.setText(convertView, R.id.titel, data.get(position).getClazzName()+"课表");
		Texttool.setText(convertView, R.id.week, "第"+data.get(position).getWeek()+"周");
		Texttool.setText(convertView, R.id.time, data.get(position).getTime());
		return convertView;
	}
}
