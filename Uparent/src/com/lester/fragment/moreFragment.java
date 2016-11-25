package com.lester.fragment;

import java.util.ArrayList;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;

import com.lester.uparent.Announcement;
import com.lester.uparent.Announcement_Class;
import com.lester.uparent.Authorize;
import com.lester.uparent.Childinfo;
import com.lester.uparent.Commentlist;
import com.lester.uparent.Courselist;
import com.lester.uparent.Feedback_History;
import com.lester.uparent.GenerateCode;
import com.lester.uparent.LeaveList;
import com.lester.uparent.Me;
import com.lester.uparent.My_message;
import com.lester.uparent.ParentManage;
import com.lester.uparent.R;
import com.lester.uparent.RecipesList;
import com.lester.uparent.Schoolbus;
import com.lester.uparent.Schoolinfo;
import com.lester.uparent.SendLog;
import com.lester.uparent.Teacherlist;
import com.lester.uteacher.adapter.HomeGridAdapter;
import com.lester.uteacher.tool.Fixpic;

public class moreFragment extends Fragment{

	private HomeGridAdapter gridadapter;
	private View view;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		view=getActivity().getLayoutInflater().inflate(R.layout.morefragment, null);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		indata();
		initview();
		return view;
	}

	private void indata() {
		ArrayList<Integer> imgid=new ArrayList<Integer>();
		ArrayList<String> itemname=new ArrayList<String>();
//		imgid.add(R.drawable.qunzuxinxi);
		imgid.add(R.drawable.homeitem12);
//		imgid.add(R.drawable.xiaoxi);
		imgid.add(R.drawable.pingjia);
		imgid.add(R.drawable.homeitem15);
//		itemname.add("群组信息");
		itemname.add("幼儿表现");
//		itemname.add("关注提示");
		itemname.add("家长评估");
		itemname.add("紧急联系");
		gridadapter=new HomeGridAdapter(getActivity(),imgid,itemname,getActivity().getWindowManager());
	}
	private void initview() {
		
		ImageView img=(ImageView) view.findViewById(R.id.homeimg);
		Fixpic fix=new Fixpic();
		fix.setView_W_H(getActivity().getWindowManager(),img,
				BitmapFactory.decodeResource(
						 getActivity().getResources(), R.drawable.u117));
		GridView gridview=(GridView) view.findViewById(R.id.gripview);
		gridview.setAdapter(gridadapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				switch (arg2) {
				case 0:
					intent.setClass(getActivity(), Feedback_History.class);
					startActivity(intent);
					break;
				case 1:
					intent.setClass(getActivity(), Commentlist.class);
					startActivity(intent);
					break;
				case 2:
					intent.setClass(getActivity(), Teacherlist.class);
					startActivity(intent);
					break;
				}
			}
		});
	}
}
