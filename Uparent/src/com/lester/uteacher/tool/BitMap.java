package com.lester.uteacher.tool;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.WindowManager;

public class BitMap {

	public static Bitmap returnBitMap(String url) {   
        URL myFileUrl = null;   
        Bitmap bitmap = null;   
        try {   
         myFileUrl = new URL(url);   
        } catch (MalformedURLException e) {   
         e.printStackTrace();   
        }   
        try {   
         HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();   
         conn.setDoInput(true);   
         conn.connect();   
         InputStream is = conn.getInputStream();   
         bitmap = BitmapFactory.decodeStream(is);   
         is.close();   
        } catch (IOException e) {   
         e.printStackTrace();   
        }   
        return bitmap;   
     } 
	
	public static Bitmap drawableToBitmap(Drawable drawable){ 
		int width = drawable.getIntrinsicWidth(); 
		int height = drawable.getIntrinsicHeight(); 
		Bitmap bitmap = Bitmap.createBitmap(width, height, 
		drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 
		: Bitmap.Config.RGB_565); 
		Canvas canvas = new Canvas(bitmap); 
		drawable.setBounds(0,0,width,height); 
		drawable.draw(canvas); 
		return bitmap; 
	}
	
	/**
	 * 把Bitmap转Byte
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * 本地获取图片转换成位图
	 * @param 图片路径
	 * @return 位图
	 */
	public static Bitmap getBitmap(String path){
		File file = new File(path);
   	 if (file.exists()) {
   		 	Bitmap bm=BitmapFactory.decodeFile(path);
   			return bm;
   	 	}else{
   	 		return null;
   	 	}
	}
	
}
