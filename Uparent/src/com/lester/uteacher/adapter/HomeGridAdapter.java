package com.lester.uteacher.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lester.uparent.R;

public class HomeGridAdapter extends BaseAdapter {
	private  ArrayList<Integer> imgid;
	private  ArrayList<String> itemname;
	private Context context;
	private WindowManager windowManager;
	public HomeGridAdapter(Context applicationContext, ArrayList<Integer> imgid, 
			ArrayList<String> itemname, WindowManager windowManager) {
		this.context=applicationContext;
		this.imgid=imgid;
		this.itemname=itemname;
		this.windowManager=windowManager;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return itemname.size();
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
		convertView=LayoutInflater.from(context).inflate(R.layout.homeitem, null);
		ImageView img=(ImageView) convertView.findViewById(R.id.itemimg);
//		Fixpic fix=new Fixpic();
//		fix.sethomeitem(windowManager,img,
//				BitmapFactory.decodeResource(
//						 context.getResources(), R.drawable.u117));
		img.setImageResource(imgid.get(position));
		TextView te=(TextView) convertView.findViewById(R.id.text);
		te.setText(itemname.get(position));
		return convertView;
	}

}
