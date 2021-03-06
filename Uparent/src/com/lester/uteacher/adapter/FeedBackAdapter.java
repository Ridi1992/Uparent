package com.lester.uteacher.adapter;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lester.uparent.Feedback;
import com.lester.uparent.R;
import com.teacher.View.myfeedbackView;
import com.teacher.data.FeedBackData;

public class FeedBackAdapter extends BaseAdapter{

	
	private Feedback mcontext;
	private ArrayList<FeedBackData > data;
	

	public FeedBackAdapter(Feedback feedback) {
		// TODO Auto-generated constructor stub
		mcontext=feedback;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(ArrayList<FeedBackData> data) {
		this.data = data;
	}
	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
			view =mcontext.getLayoutInflater().inflate(R.layout.myfeedbacklistview, null);
			myfeedbackView childview=(myfeedbackView) view.findViewById(R.id.listitem);
			childview.indata(mcontext,arg0);
		return view;
	}
	
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int getCount() {
		if(data!=null){
			return data.size();
		}
		return 0;
	}

}
