package com.lester.uteacher.adapter;

import java.util.ArrayList;




import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class BannerAdapter extends PagerAdapter {
	private int count;
	private ArrayList<View> mViewList;
	private Context mContext;
	
	public BannerAdapter(ArrayList<View> list,Context c){
		mViewList = list;
		this.count=mViewList.size();
		mContext = c;
	}

	@Override
	public int getCount() {
		if (mViewList != null) {
			return mViewList.size();
		}
		return 0;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		
		View view = mViewList.get(position % count);
		container.addView(view);
 		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				Log.i("goods_id", "goods_id="+Fragment_home.goods_ids[position]);
//				Intent intent = new Intent();
//				intent.setAction("banner");
//				intent.putExtra("sign", Fragment_home.sign[position]);
//				intent.putExtra("id", Fragment_home.goods_ids[position]);
//				intent.setClass(mContext, ToyDetailActivity.class);
//				mContext.startActivity(intent);
			}
		});
		return view;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		View view = mViewList.get(position%count);
		container.removeView(view);
	}
	

}
