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
	 public void setView_W_H(WindowManager wm, ImageView view,Bitmap bitmap){
		 if(view!=null && bitmap!=null){
	        int width = wm.getDefaultDisplay().getWidth();
	        int height = wm.getDefaultDisplay().getHeight();
	        int bitmapwidth=bitmap.getWidth();
	        int bitmapheight=bitmap.getHeight();
	        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(      
	                LinearLayout.LayoutParams.FILL_PARENT,      
	                LinearLayout.LayoutParams.WRAP_CONTENT      
	        );   
	        linearParams.width = width;
	        linearParams.height = (int) (width*((float)bitmapheight/(float)bitmapwidth));// �ؼ��ĸ�ǿ������
	        view.setLayoutParams(linearParams);
		 }
	    }
	 public void setViewparent_W_H(WindowManager wm, View view,Bitmap bitmap){
		 if(view!=null && bitmap!=null){	
		 //��ȡ��Ļ���
//	        WindowManager wm = this.getWindowManager(); 
	        int width = wm.getDefaultDisplay().getWidth();
	        int height = wm.getDefaultDisplay().getHeight();
	        
	        //��ȡBitmap�Ŀ�͸�
//	        Bitmap bitmap = BitmapFactory.decodeResource(.getResources(), drawable_id);
	        int bitmapwidth=bitmap.getWidth();
	        int bitmapheight=bitmap.getHeight();
	        Log.i("bitmapheight", "��=="+bitmapheight);
	        
	        //���ÿ��
	        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(      
	                LinearLayout.LayoutParams.FILL_PARENT,      
	                LinearLayout.LayoutParams.WRAP_CONTENT      
	        );   
	        linearParams.width = width;// �ؼ��ĸ�ǿ������
	        linearParams.height = (int) (width*((float)bitmapheight/(float)bitmapwidth));// �ؼ��ĸ�ǿ������
//	        Log.i("��͸�===", "��="+linearParams.width+"��=="+linearParams.height);
	        view.setLayoutParams(linearParams); // ʹ���úõĲ��ֲ���Ӧ�õ��ؼ�myGrid 
//	        ((ImageView) view).setImageBitmap(bitmap);
		 }
	    }
	 /**����bitmap�͸��ؼ��Ŀ��ǿ������ImageView�Ŀ�ߣ���ֹ����ʧ��*/
	 public void setImageview_h_w(Context context,int width, View view){
	        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(      
	                LinearLayout.LayoutParams.FILL_PARENT,      
	                LinearLayout.LayoutParams.WRAP_CONTENT      
	        );
	        linearParams.width = (int) ((width-dip2px(context,105))/3);// 95Ϊ��ͼƬ��ռdp
	        linearParams.height =(int) ((width-dip2px(context,105))/3);//(int) ((float)linearParams.width*((float)bitmapheight/(float)bitmapwidth));// �ؼ��ĸ�ǿ������
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
	        linearParams.width = (int) ((width-dip2px(context,10))/3);// 95Ϊ��ͼƬ��ռdp
	        linearParams.height =(int) ((width-dip2px(context,10))/3);//(int) ((float)linearParams.width*((float)bitmapheight/(float)bitmapwidth));// �ؼ��ĸ�ǿ������
//	        Log.i("��͸�===", "��="+linearParams.width+"��=="+linearParams.height);
	        view.setLayoutParams(linearParams);
//	        }
		 }
	 /**����bitmap�͸��ؼ��Ŀ��ǿ������ImageView�Ŀ�ߣ���ֹ����ʧ��*/
	 public void setImageview_h_w_forum(Context context,int width, View view,Bitmap bitmap){
		 if(view!=null && bitmap!=null){
	        int bitmapwidth=bitmap.getWidth();
	        int bitmapheight=bitmap.getHeight();
	        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(      
	                LinearLayout.LayoutParams.FILL_PARENT,      
	                LinearLayout.LayoutParams.WRAP_CONTENT      
	        );
	        linearParams.width = (int) ((width-dip2px(context,20))/3);// 95Ϊ��ͼƬ��ռdp
//	        linearParams.height = linearParams.width;
	        linearParams.height = (int) ((float)linearParams.width*((float)bitmapheight/(float)bitmapwidth));// �ؼ��ĸ�ǿ������
//	        Log.i("��͸�===", "��="+linearParams.width+"��=="+linearParams.height);
	        view.setLayoutParams(linearParams);
	        }
		 if(view!=null && bitmap==null){
			 LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(      
		                LinearLayout.LayoutParams.FILL_PARENT,      
		                LinearLayout.LayoutParams.WRAP_CONTENT      
		        );
		        linearParams.width = (int) ((width-dip2px(context,20))/3);// 95Ϊ��ͼƬ��ռdp
		        linearParams.height = linearParams.width;
//		        linearParams.height = (int) ((float)linearParams.width*((float)bitmapheight/(float)bitmapwidth));// �ؼ��ĸ�ǿ������
		        view.setLayoutParams(linearParams); 
		 }
		 }
		/**����bitmap�͸��ؼ��Ŀ��ǿ������ImageView�Ŀ�ߣ���ֹ����ʧ��*/
		 public void setImageview_h_w_identity(Context context,int width, View view,Bitmap bitmap){
			 if(view!=null && bitmap!=null){
		        int bitmapwidth=bitmap.getWidth();
		        int bitmapheight=bitmap.getHeight();
		        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(      
		                LinearLayout.LayoutParams.FILL_PARENT,      
		                LinearLayout.LayoutParams.WRAP_CONTENT      
		        );
		        linearParams.width = (int) (width-dip2px(context,30));// 95Ϊ��ͼƬ��ռdp
		        linearParams.height = (int) ((float)linearParams.width*((float)bitmapheight/(float)bitmapwidth));// �ؼ��ĸ�ǿ������
//		        Log.i("��͸�===", "��="+linearParams.width+"��=="+linearParams.height);
		        view.setLayoutParams(linearParams);
		        }
			 }
		 	/** 
		     * �����ֻ��ķֱ��ʴ� dp �ĵ�λ ת��Ϊ px(����) 
		     */
			public static int dip2px(Context context, float dpValue) {  
		        final float scale = context.getResources().getDisplayMetrics().density;  
		        Log.i("px===", ""+(dpValue * scale + 0.5f));
		        return (int) (dpValue * scale + 0.5f);  
			}
}
