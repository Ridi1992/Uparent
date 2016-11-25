package com.lester.headview;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.lester.uparent.R;
import com.teacher.View.BaseImageView;



/**
 * 自定义View，实现圆角，圆形等效�?
 * 
 * @author zhy
 * 
 */
public class CustomImageView extends BaseImageView {

    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public static Bitmap getBitmap(int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        canvas.drawOval(new RectF(0.0f, 0.0f, width, height), paint);

        return bitmap;
    }

    @Override
    public Bitmap getBitmap() {
        return getBitmap(getWidth(), getHeight());
    }
}



/*View
{

	*//**
	 * TYPE_CIRCLE / TYPE_ROUND
	 *//*
	private int type;
	private static final int TYPE_CIRCLE = 0;
	private static final int TYPE_ROUND = 1;

	*//**
	 * 图片
	 *//*
	private Bitmap mSrc;

	*//**
	 * 圆角的大�?
	 *//*
	private int mRadius;

	*//**
	 * 控件的宽�?
	 *//*
	private int mWidth;
	*//**
	 * 控件的高�?
	 *//*
	private int mHeight;

	public CustomImageView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public CustomImageView(Context context)
	{
		this(context, null);
	}

	*//**
	 * 初始化一些自定义的参�?
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 *//*
	public CustomImageView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);

		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomImageView, defStyle, 0);

		int n = a.getIndexCount();
		for (int i = 0; i < n; i++)
		{
			int attr = a.getIndex(i);
			switch (attr)
			{
			case R.styleable.CustomImageView_src:
				mSrc = BitmapFactory.decodeResource(getResources(), a.getResourceId(attr, 0));
				break;
			case R.styleable.CustomImageView_type:
				type = a.getInt(attr, 0);// 默认为Circle
				break;
			case R.styleable.CustomImageView_borderRadius:
				type = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f,
						getResources().getDisplayMetrics()));// 默认�?0DP
				break;
			}
		}
		a.recycle();
	}

	*//**
	 * 计算控件的高度和宽度
	 *//*
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		*//**
		 * 设置宽度
		 *//*
		int specMode = MeasureSpec.getMode(widthMeasureSpec);
		int specSize = MeasureSpec.getSize(widthMeasureSpec);

		if (specMode == MeasureSpec.EXACTLY)// match_parent , accurate
		{
			mWidth = specSize;
		} else
		{
			// 由图片决定的�?
			int desireByImg = getPaddingLeft() + getPaddingRight() + mSrc.getWidth();
			if (specMode == MeasureSpec.AT_MOST)// wrap_content
			{
				mWidth = Math.min(desireByImg, specSize);
			}
		}

		*//***
		 * 设置高度
		 *//*

		specMode = MeasureSpec.getMode(heightMeasureSpec);
		specSize = MeasureSpec.getSize(heightMeasureSpec);
		if (specMode == MeasureSpec.EXACTLY)// match_parent , accurate
		{
			mHeight = specSize;
		} else
		{
			int desire = getPaddingTop() + getPaddingBottom() + mSrc.getHeight();
			if (specMode == MeasureSpec.AT_MOST)// wrap_content
			{
				mHeight = Math.min(desire, specSize);
			}
		}
		setMeasuredDimension(mWidth, mHeight);

	}

	*//**
	 * 绘制
	 *//*
	@Override
	protected void onDraw(Canvas canvas)
	{

		switch (type)
		{
		// 如果是TYPE_CIRCLE绘制圆形
		case TYPE_CIRCLE:

			int min = Math.min(mWidth, mHeight);
			*//**
			 * 长度如果不一致，按小的�?进行压缩
			 *//*
			mSrc = Bitmap.createScaledBitmap(mSrc, min, min, false);

			canvas.drawBitmap(createCircleImage(mSrc, min), 0, 0, null);
			break;
		case TYPE_ROUND:
			canvas.drawBitmap(createRoundConerImage(mSrc), 0, 0, null);
			break;

		}

	}
	
	*//**
	 * 设置图片
	 * @param image
	 *//*
	public void setBitmap(Bitmap image){
		if(image!=null){
//			int min = Math.min(mWidth, mHeight);
			this.mSrc=Bitmap.createScaledBitmap(image, 150, 150, false);
			invalidate();
		}
	}
	
	*//**
	 * 根据原图和变长绘制圆形图�?
	 * 
	 * @param source
	 * @param min
	 * @return
	 *//*
	private Bitmap createCircleImage(Bitmap source, int min)
	{
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap target = Bitmap.createBitmap(min, min, Config.ARGB_8888);
		*//**
		 * 产生�?��同样大小的画�?
		 *//*
		Canvas canvas = new Canvas(target);
		*//**
		 * 首先绘制圆形
		 *//*
		canvas.drawCircle(min / 2, min / 2, min / 2, paint);
		*//**
		 * 使用SRC_IN，参考上面的说明
		 *//*
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		*//**
		 * 绘制图片
		 *//*
		canvas.drawBitmap(source, 0, 0, paint);
		return target;
	}

	*//**
	 * 根据原图添加圆角
	 * 
	 * @param source
	 * @return
	 *//*
	private Bitmap createRoundConerImage(Bitmap source)
	{
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap target = Bitmap.createBitmap(mWidth, mHeight, Config.ARGB_8888);
		Canvas canvas = new Canvas(target);
		RectF rect = new RectF(0, 0, source.getWidth(), source.getHeight());
		canvas.drawRoundRect(rect, 50f, 50f, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(source, 0, 0, paint);
		return target;
	}
	
	
	
}*/
