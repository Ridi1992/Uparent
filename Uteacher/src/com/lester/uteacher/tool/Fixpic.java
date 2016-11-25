package com.lester.uteacher.tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Fixpic  {
	/**根据bitmap的宽高强制设置ImageView的宽高，防止比例失调*/
	 public void setView_W_H(WindowManager wm, ImageView view,Bitmap bitmap){
	    	//获取屏幕宽高
		 if(view!=null && bitmap!=null){
	        int width = wm.getDefaultDisplay().getWidth();
	        int height = wm.getDefaultDisplay().getHeight();
	        //获取Bitmap的宽和高
	        int bitmapwidth=bitmap.getWidth();
	        int bitmapheight=bitmap.getHeight();
//	        Log.i("bitmapheight", "高=="+bitmapheight);
	        //设置宽高
	        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(      
	                LinearLayout.LayoutParams.FILL_PARENT,      
	                LinearLayout.LayoutParams.WRAP_CONTENT      
	        );   
	        linearParams.width = width;// 控件的高强制设置
	        linearParams.height = (int) (width*((float)bitmapheight/(float)bitmapwidth));// 控件的高强制设置
//	        Log.i("宽和高===", "宽="+linearParams.width+"高=="+linearParams.height);
	        view.setLayoutParams(linearParams); // 使设置好的布局参数应用到控件myGrid 
		 }
	    }
	 
	 public void sethomeitem(WindowManager wm, ImageView view,Bitmap bitmap){
	    	//获取屏幕宽高
		 if(view!=null && bitmap!=null){
	        int width = wm.getDefaultDisplay().getWidth();
	        int height = wm.getDefaultDisplay().getHeight();
	        //获取Bitmap的宽和高
	        int bitmapwidth=bitmap.getWidth();
	        int bitmapheight=bitmap.getHeight();
//	        Log.i("bitmapheight", "高=="+bitmapheight);
	        //设置宽高
	        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(      
	                LinearLayout.LayoutParams.FILL_PARENT,      
	                LinearLayout.LayoutParams.WRAP_CONTENT      
	        );   
	        linearParams.width = width/4;// 控件的高强制设置
	        linearParams.height = width/4;// 控件的高强制设置
//	        Log.i("宽和高===", "宽="+linearParams.width+"高=="+linearParams.height);
	        view.setLayoutParams(linearParams); // 使设置好的布局参数应用到控件myGrid 
		 }
	    }
	/**根据bitmap的宽高强制设置view的宽高，防止比例失调*/
	 public void setViewparent_W_H(WindowManager wm, View view,Bitmap bitmap){
		 if(view!=null && bitmap!=null){	
		 //获取屏幕宽高
//	        WindowManager wm = this.getWindowManager(); 
	        int width = wm.getDefaultDisplay().getWidth();
	        int height = wm.getDefaultDisplay().getHeight();
	        
	        //获取Bitmap的宽和高
//	        Bitmap bitmap = BitmapFactory.decodeResource(.getResources(), drawable_id);
	        int bitmapwidth=bitmap.getWidth();
	        int bitmapheight=bitmap.getHeight();
	        Log.i("bitmapheight", "高=="+bitmapheight);
	        
	        //设置宽高
	        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(      
	                LinearLayout.LayoutParams.FILL_PARENT,      
	                LinearLayout.LayoutParams.WRAP_CONTENT      
	        );   
	        linearParams.width = width;// 控件的高强制设置
	        linearParams.height = (int) (width*((float)bitmapheight/(float)bitmapwidth));// 控件的高强制设置
//	        Log.i("宽和高===", "宽="+linearParams.width+"高=="+linearParams.height);
	        view.setLayoutParams(linearParams); // 使设置好的布局参数应用到控件myGrid 
//	        ((ImageView) view).setImageBitmap(bitmap);
		 }
	    }
	 /**根据bitmap和父控件的宽高强制设置ImageView的宽高，防止比例失调*/
	 public void setImageview_h_w(Context context,int width, View view){
	        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(      
	                LinearLayout.LayoutParams.FILL_PARENT,      
	                LinearLayout.LayoutParams.WRAP_CONTENT      
	        );
	        linearParams.width = (int) ((width-dip2px(context,105))/3);// 95为非图片所占dp
	        linearParams.height =(int) ((width-dip2px(context,105))/3);//(int) ((float)linearParams.width*((float)bitmapheight/(float)bitmapwidth));// 控件的高强制设置
	        view.setLayoutParams(linearParams);
		 }
	 public void setmarktImageview_h_w(Context context,int width, View view){
//		 if(view!=null && bitmap!=null){
//	        int bitmapwidth=bitmap.getWidth();
//	        int bitmapheight=bitmap.getHeight();
	        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(      
	                LinearLayout.LayoutParams.FILL_PARENT,      
	                LinearLayout.LayoutParams.WRAP_CONTENT      
	        );
	        linearParams.width = (int) ((width-dip2px(context,10))/3);// 95为非图片所占dp
	        linearParams.height =(int) ((width-dip2px(context,10))/3);//(int) ((float)linearParams.width*((float)bitmapheight/(float)bitmapwidth));// 控件的高强制设置
//	        Log.i("宽和高===", "宽="+linearParams.width+"高=="+linearParams.height);
	        view.setLayoutParams(linearParams);
//	        }
		 }
	 /**根据bitmap和父控件的宽高强制设置ImageView的宽高，防止比例失调*/
	 public void setImageview_h_w_forum(Context context,int width, View view,Bitmap bitmap){
		 if(view!=null && bitmap!=null){
	        int bitmapwidth=bitmap.getWidth();
	        int bitmapheight=bitmap.getHeight();
	        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(      
	                LinearLayout.LayoutParams.FILL_PARENT,      
	                LinearLayout.LayoutParams.WRAP_CONTENT      
	        );
	        linearParams.width = (int) ((width-dip2px(context,20))/3);// 95为非图片所占dp
	        linearParams.height = (int) ((float)linearParams.width*((float)bitmapheight/(float)bitmapwidth));// 控件的高强制设置
//	        Log.i("宽和高===", "宽="+linearParams.width+"高=="+linearParams.height);
	        view.setLayoutParams(linearParams);
	        }
		 }
		/**根据bitmap和父控件的宽高强制设置ImageView的宽高，防止比例失调*/
		 public void setImageview_h_w_identity(Context context,int width, View view,Bitmap bitmap){
			 if(view!=null && bitmap!=null){
		        int bitmapwidth=bitmap.getWidth();
		        int bitmapheight=bitmap.getHeight();
		        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(      
		                LinearLayout.LayoutParams.FILL_PARENT,      
		                LinearLayout.LayoutParams.WRAP_CONTENT      
		        );
		        linearParams.width = (int) (width-dip2px(context,30));// 95为非图片所占dp
		        linearParams.height = (int) ((float)linearParams.width*((float)bitmapheight/(float)bitmapwidth));// 控件的高强制设置
//		        Log.i("宽和高===", "宽="+linearParams.width+"高=="+linearParams.height);
		        view.setLayoutParams(linearParams);
		        }
			 }
		 	/** 
		     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
		     */
			public static int dip2px(Context context, float dpValue) {  
		        final float scale = context.getResources().getDisplayMetrics().density;  
		        Log.i("px===", ""+(dpValue * scale + 0.5f));
		        return (int) (dpValue * scale + 0.5f);  
			    }
}
