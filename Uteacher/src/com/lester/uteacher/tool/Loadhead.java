package com.lester.uteacher.tool;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.lester.school.loader.FileCache;
import com.lester.school.loader.MemoryCache;
import com.lester.uteacher.R;


/**
 * @author Administrator
 *返回压缩小图
 */
public class Loadhead {
	
	MemoryCache memoryCache = new MemoryCache();  
    FileCache fileCache;  
    private String url;
    private ImageView imageView;
    private Map<ImageView, String> imageViews = Collections  
            .synchronizedMap(new WeakHashMap<ImageView, String>());  
    // 线程�?
    ExecutorService executorService;  
    private Context context;
    
    public Loadhead(Context context,String url, ImageView imageView) {  
        fileCache = new FileCache(context);  
        executorService = Executors.newFixedThreadPool(5);  
        this.context=context;
        this.url=url;
        this.imageView=imageView;
    }  
    // 当进入listview时默认的图片，可换成你自己的默认图片  
    final int stub_id = R.drawable.u122; 
  
    public void execute(){
    	 imageViews.put(imageView, url);  
         // 先从内存缓存中查�? 
         Bitmap bitmap = memoryCache.get(url);  
         if (bitmap != null){  
             imageView.setImageBitmap(bitmap);  
         }else {  
             // 若没有的话则�?��新线程加载图�? 
             queuePhoto(url, imageView);  
//             imageView.setImageResource(stub_id);  
         }
    }
    
    private void queuePhoto(String url, ImageView imageView) {  
        PhotoToLoad p = new PhotoToLoad(url, imageView);  
        executorService.submit(new PhotosLoader(p));  
    }
  
    private Bitmap getBitmap(String url) {  
        File f = fileCache.getFile(url);  
  
        // 先从文件缓存中查找是否有  
        Bitmap b = decodeFile(f);  
        if (b != null)  
            return b;  
        // �?��从指定的url中下载图�? 
        try {  
            Bitmap bitmap = null;  
            URL imageUrl = new URL(url);  
            HttpURLConnection conn = (HttpURLConnection) imageUrl  
                    .openConnection();  
            conn.setConnectTimeout(30000);  
            conn.setReadTimeout(30000);  
            conn.setInstanceFollowRedirects(true);  
            InputStream is = conn.getInputStream();  
            OutputStream os = new FileOutputStream(f);  
            CopyStream(is, os);  
            os.close();  
            bitmap = decodeFile(f);  
            return bitmap;  
        } catch (Exception ex) {  
            ex.printStackTrace();  
            return null;  
        }  
    }  
    // decode这个图片并且按比例缩放以减少内存消�?，虚拟机对每张图片的缓存大小也是有限制的  
    private Bitmap decodeFile(File f) {  
        try {  
            // decode image size  
            BitmapFactory.Options o = new BitmapFactory.Options();  
            o.inJustDecodeBounds = true;  
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);  
            // Find the correct scale value. It should be the power of 2.  
            final int REQUIRED_SIZE = 400;  
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            Log.i("压缩图片,宽", "Width="+o.outWidth);
            Log.i("压缩图片,宽", "Height="+o.outHeight);
            int scale = 1;  
            while (true) {  
                if (width_tmp / 2 < REQUIRED_SIZE  
                        || height_tmp / 2 < REQUIRED_SIZE)  
                    break;  
                width_tmp /= 2;  
                height_tmp /= 2;  
                scale *= 2;  
            }  
            // decode with inSampleSize  
            BitmapFactory.Options o2 = new BitmapFactory.Options(); 
            o2.inJustDecodeBounds = false;
            o2.inSampleSize = scale; 
            Log.i("压缩图片,采码率", "scale="+scale);
            
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);  
        } catch (FileNotFoundException e) {  
        }  
        return null;  
    }  
  
    // Task for the queue  
    private class PhotoToLoad {  
        public String url;  
        public ImageView imageView;  
  
        public PhotoToLoad(String u, ImageView i) {  
            url = u;  
            imageView = i;  
        }  
    }  
  
    class PhotosLoader implements Runnable {  
        PhotoToLoad photoToLoad;  
  
        PhotosLoader(PhotoToLoad photoToLoad) {  
            this.photoToLoad = photoToLoad;  
        }  
  
        @Override  
        public void run() { 
        	System.out.println("aaaaaaaaaaaaaaaaaaaaa");
            if (imageViewReused(photoToLoad))  
                return;  
            Bitmap bmp = getBitmap(photoToLoad.url);  
            memoryCache.put(photoToLoad.url, bmp);  
            if (imageViewReused(photoToLoad))  
                return;
            System.out.println("bbbbbbbbbbbbbbbbbbbbbbbb");
            BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);  
            // 更新的操作放在UI线程�? 
//            Activity a = (Activity) photoToLoad.imageView.getContext();  
            ((Activity)context).runOnUiThread(bd);
        }  
    }  
    /** 
     * 防止图片错位 
     *  
     * @param photoToLoad 
     * @return 
     */  
    boolean imageViewReused(PhotoToLoad photoToLoad) {  
        String tag = imageViews.get(photoToLoad.imageView);  
        if (tag == null || !tag.equals(photoToLoad.url))  
            return true;  
        return false;  
    }  
    // 用于在UI线程中更新界�? 
    class BitmapDisplayer implements Runnable {  
        Bitmap bitmap;  
        PhotoToLoad photoToLoad;  
        public BitmapDisplayer(Bitmap b, PhotoToLoad p) {  
            bitmap = b;  
            photoToLoad = p;  
            System.out.println("cccccccccccccccccccccccccc");
        }  
        public void run() {  
            if (imageViewReused(photoToLoad))  
                return;  
            if (bitmap != null)  
                photoToLoad.imageView.setImageBitmap(bitmap);  
            else  
                photoToLoad.imageView.setImageResource(stub_id);  
        }  
    }  
    public void clearCache() {  
        memoryCache.clear();  
        fileCache.clear();  
    }  
    public static void CopyStream(InputStream is, OutputStream os) {  
        final int buffer_size = 1024;  
        try {  
            byte[] bytes = new byte[buffer_size];  
            for (;;) {  
                int count = is.read(bytes, 0, buffer_size);  
                if (count == -1)  
                    break;  
                os.write(bytes, 0, count);  
            }  
        } catch (Exception ex) {  
        }  
    }  
}

//
//public class Loadhead extends AsyncTask<Bitmap, Integer, Bitmap > {
//	private CustomImageView myimageView;
//	private String imgurl;
//	private Userhead userhead;
//	public Loadhead(String url, CustomImageView imageView) {
//		myimageView=imageView;
//		imgurl=url;
//		userhead=new Userhead();
//	}
//	@Override
//	protected void onPreExecute() {
//		// TODO 自动生成的方法存根
//		super.onPreExecute();
//	}
//	@Override
//	protected Bitmap doInBackground(Bitmap... params) {
//		Bitmap bm = null;
//		String str=imgurl.replace("/", "");
//		str=str.replace(":", "");
//		str=str.replace(".", "");
//		try {
//			if(userhead.getuserhead(str)!=null){
//				return userhead.getuserhead(str);//先从本地获取
//			}else{
//				final Loadimgtobirmap loadhead=new Loadimgtobirmap();//下载
//				bm = loadhead.getimgBitMap(imgurl);
//				userhead.saveuserhead(bm,str);//保存到本地
//			}
//		} catch (Exception e) {
//			System.out.println("获取图片错误"+ e.toString());
//		}
//		return bm;
//	}
//	@Override
//	protected void onPostExecute(Bitmap bm) {
//		super.onPostExecute(bm);
//		if(bm!=null){
//			try {
//				myimageView.setImageBitmap(bm);
//			} catch (Exception e) {
//				System.out.println("头像设置失败"+e.toString());
//			}
//		}
//	}
//}
