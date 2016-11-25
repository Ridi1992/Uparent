package com.lester.uteacher.adapter;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lester.uteacher.R;
import com.lester.uteacher.Recipes;
import com.lester.uteacher.texttool.Texttool;
import com.teacher.data.CouserData;

public class RecipesAdapter extends BaseAdapter{

	private Recipes mcontext;
	ArrayList<CouserData> data;
	private int page;
	public RecipesAdapter(ArrayList<CouserData> arrayList, Recipes addCourse,int page) {
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
			convertView=mcontext.getLayoutInflater().inflate(R.layout.recipes_contentitem, null);
			holder.starttime=(TextView) convertView.findViewById(R.id.starttime);
//			holder.endtime=(TextView) convertView.findViewById(R.id.endtime);
			holder.titel=(TextView) convertView.findViewById(R.id.titel);
//			holder.content=(TextView) convertView.findViewById(R.id.content);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		Texttool.setText(holder.starttime, data.get(position).getStarttime());
//		Texttool.setText(holder.endtime, data.get(position).getEndtime());
		Texttool.setText(holder.titel, data.get(position).getTitle());
//		Texttool.setText(holder.content, data.get(position).getContent());

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
}
