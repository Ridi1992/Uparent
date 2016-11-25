package com.lester.uteacher.adapter;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lester.uteacher.Courselist;
import com.lester.uteacher.R;
import com.lester.uteacher.texttool.Texttool;
import com.teacher.data.CouserListData;

public class CourserlistAdapter extends BaseAdapter{

	private Courselist mcontext;
	private ArrayList<CouserListData> data;
	
	/**
	 * @param data the data to set
	 */
	public void setData(ArrayList<CouserListData> data) {
		this.data = data;
	}


	public CourserlistAdapter(Courselist courselist) {
		// TODO Auto-generated constructor stub
		mcontext=courselist;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=mcontext.getLayoutInflater().inflate(R.layout.courserlist_item, null);
			holder.view=convertView;
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		Texttool.setText(holder.view, R.id.titel, data.get(position).getClazzName()+"课程表");
		Texttool.setText(holder.view, R.id.time, data.get(position).getTime());
		Texttool.setText(holder.view, R.id.week, "第"+data.get(position).getWeek()+"周");
		return convertView;
	}

	class ViewHolder{
		View view;
	}
	
}
