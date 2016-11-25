package com.lester.school.clippic;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;

import com.lester.clip.view.ClipImageLayout;
import com.lester.uparent.R;
/**
 * 自定义的图片裁剪功能
 *
 */
public class ClipImg extends Activity
{
	private ClipImageLayout mClipImageLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clipimg);
		mClipImageLayout = (ClipImageLayout) findViewById(R.id.id_clipImageLayout);
		String path=getIntent().getStringExtra("path");
		File file = new File(path);
   	 if (file.exists()) {
   		 Bitmap bm=BitmapFactory.decodeFile(path);
//   		 Bitmap bm=fitBitmap(path);
   		 mClipImageLayout.mZoomImageView.setImageBitmap(bm);
   	 	}
   	 	findViewById(R.id.ok).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bitmap bitmap = mClipImageLayout.clip();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				byte[] datas = baos.toByteArray();
				Intent intent = new Intent();
				intent.putExtra("bitmap", datas);
				setResult(-1, intent);
				finish();
			}
		});
   	 findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * 改变图片分辨率
	 */
	private Bitmap fitBitmap(String path){
			BitmapFactory.Options opts = new Options(); 
			// 不读取像素数组到内存中，仅读取图片的信息                 
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, opts); 
			// 从Options中获取图片的分辨率                 
			int imageHeight = opts.outHeight;  
			int imageWidth = opts.outWidth; 
			// 获取Android屏幕的服务                 
			WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE); 
			// 获取屏幕的分辨率，getHeight()、getWidth已经被废弃掉了                 
			// 应该使用getSize()，但是这里为了向下兼容所以依然使用它们                 
			int windowHeight = wm.getDefaultDisplay().getHeight();  
			int windowWidth = wm.getDefaultDisplay().getWidth();  
			// 计算采样率                 
			int scaleX = imageWidth / windowWidth; 
			Log.i("压缩图片", "imageWidth="+imageWidth);
			Log.i("压缩图片", "windowWidth="+windowWidth);
			int scaleY = imageHeight / windowHeight; 
			int scale = 1; 
			// 采样率依照最大的方向为准                 
			if (scaleX > scaleY && scaleY >= 1) { 
				scale = scaleX; 
				Log.i("压缩图片,采码率", "scale="+scale);
				}
				if (scaleX < scaleY && scaleX >= 1) 
				{ 
					scale = scaleY;
					} 
					// false表示读取图片像素数组到内存中，依照设定的采样率                 
					opts.inJustDecodeBounds = false; 
					// 采样率                 
					opts.inSampleSize = scale; 
					Bitmap bitmap = BitmapFactory.decodeFile(path, opts);
					return bitmap;
			}
}
