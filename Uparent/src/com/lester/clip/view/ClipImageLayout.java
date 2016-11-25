package com.lester.clip.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

public class ClipImageLayout extends RelativeLayout
{

	public ClipZoomImageView mZoomImageView;
	private ClipImageBorderView mClipImageView;

	private int mHorizontalPadding = 20;

	public ClipImageLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		mZoomImageView = new ClipZoomImageView(context);
		mClipImageView = new ClipImageBorderView(context);

		android.view.ViewGroup.LayoutParams lp = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		
		/**
		 * 杩欓噷娴嬭瘯锛岀洿鎺ュ啓姝讳簡鍥剧墖锛岀湡姝ｄ娇鐢ㄨ繃绋嬩腑锛屽彲浠ユ彁鍙栦负鑷畾涔夊睘锟?
		 */
//		mZoomImageView.setImageDrawable(getResources().getDrawable(
//				R.drawable.a));
//		mZoomImageView.setScaleType(ScaleType.CENTER_CROP);
		this.addView(mZoomImageView, lp);
		this.addView(mClipImageView, lp);

		
		// 璁＄畻padding鐨刾x
		mHorizontalPadding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources()
						.getDisplayMetrics());
		mZoomImageView.setHorizontalPadding(mHorizontalPadding);
		mClipImageView.setHorizontalPadding(mHorizontalPadding);
	}

	/**
	 * 瀵瑰鍏竷璁剧疆杈硅窛鐨勬柟锟?鍗曚綅涓篸p
	 * 
	 * @param mHorizontalPadding
	 */
	public void setHorizontalPadding(int mHorizontalPadding)
	{
		this.mHorizontalPadding = mHorizontalPadding;
	}

	/**
	 * 瑁佸垏鍥剧墖
	 * 
	 * @return
	 */
	public Bitmap clip()
	{
		return mZoomImageView.clip();
	}

}
