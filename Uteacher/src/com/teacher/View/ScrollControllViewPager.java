package com.teacher.View;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class ScrollControllViewPager extends ViewPager {
	private final String TAG = ScrollControllViewPager.class.getSimpleName();
	private boolean scroll = true;

	public ScrollControllViewPager(Context context) {
		super(context);
	}

	public ScrollControllViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * @param scroll
	 */
	public void setScroll(boolean scroll) {
		this.scroll = scroll;
	}

	@Override
	public void scrollTo(int x, int y) {
		super.scrollTo(x, y);
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		if (!scroll)
			return false;
		else
//			return true;
			return super.onTouchEvent(arg0);
	}
	float d=0;
	float L=0;
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			d=event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			L=event.getX();
			if((-20>=L-d)||(L-d>=20)){
				System.out.println("滑动");
				return true;
//				return super.onInterceptTouchEvent(event);
			}else{
				System.out.println("点击");
				return false;
			}
		}
		return super.onInterceptTouchEvent(event);
//		if (!scroll)
//			return false;
//		else
////			return true;
//			return super.onInterceptTouchEvent(event);
	}

	@Override
	public void setCurrentItem(int item, boolean smoothScroll) {
		super.setCurrentItem(item, smoothScroll);
	}

	@Override
	public void setCurrentItem(int item) {
		super.setCurrentItem(item);
	}
}
