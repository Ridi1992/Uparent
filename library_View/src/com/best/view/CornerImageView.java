package com.best.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;


/**
 * @author Best
 * 带圆角的图片
 */
public class CornerImageView extends BaseImageView {

    public CornerImageView(Context context) {
        super(context);
    }

    public CornerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public CornerImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public static Bitmap getBitmap(int width,int height, int pixels) { 
		Bitmap output = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output); 
		final int color = 0xff424242; 
		final Paint paint = new Paint(); 
		final Rect rect = new Rect(0, 0, width, height); 
		final RectF rectF = new RectF(rect); 
		final float roundPx = pixels; 
		paint.setAntiAlias(true); 
		canvas.drawARGB(0, 0, 0, 0); 
		paint.setColor(color); 
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint); 
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN)); 
		canvas.drawBitmap(output, rect, rect, paint); 
		return output; 
		} 
    @Override
    public Bitmap getBitmap() {
        return getBitmap(getWidth(), getHeight(),10);
    }
}
