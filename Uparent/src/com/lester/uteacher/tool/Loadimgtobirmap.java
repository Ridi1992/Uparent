package com.lester.uteacher.tool;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.lester.school.requst.Constants;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

public class Loadimgtobirmap {
	public Bitmap returnBitMap(Handler handler,String url) {   
        URL myFileUrl = null;   
        Bitmap bitmap = null;   
        try {   
         myFileUrl = new URL(url);   
         Log.i("imgurl",url);
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
//         handler.sendMessage(handler.obtainMessage(Constants.GETHEAD, bitmap));
        } catch (IOException e) {   
         e.printStackTrace();   
        }   
        return bitmap;   
     } 
	public Bitmap getimgBitMap(String url) {   
        URL myFileUrl = null;   
        Bitmap bitmap = null;   
        try {   
         myFileUrl = new URL(url);   
         Log.i("imgurl", url);
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
}
