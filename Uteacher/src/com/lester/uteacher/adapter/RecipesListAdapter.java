package com.lester.uteacher.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lester.uteacher.R;
import com.lester.uteacher.texttool.Texttool;
import com.teacher.data.Recipelist_data;

public class RecipesListAdapter extends BaseAdapter{

//	private Courselist mcontext;
	private Context mcontext;
	
	private ArrayList<Recipelist_data> data;

	public RecipesListAdapter(Context mcontext) {
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
			convertView=LayoutInflater.from(mcontext).inflate(R.layout.recipelist_item, null);
		}
		Texttool.setText(convertView, R.id.time, data.get(position).getTime());
		Texttool.setText(convertView, R.id.week, "第"+data.get(position).getWeek()+"周");
		return convertView;
	}


	public void setData(ArrayList<Recipelist_data> data) {
		this.data=data;
	}
}
