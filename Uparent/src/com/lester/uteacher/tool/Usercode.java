package com.lester.uteacher.tool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.widget.Toast;
/**
 * 用户二维码图片操作
 *
 */
public class Usercode {
	/**
	 * 保存生成的二維碼圖片
	 */
	public void saveQrCodePicture(Context context, Bitmap qrCodeBitmap) {
		File file1=new File("/sdcard/uparent/user/");
		if(!file1.exists()){
			file1.mkdirs();//不存在则新建路径
		}
		// 创建一个位于SD卡上的文件
		String imgname="code"+".png";
		File qrImage = new File("/sdcard/uparent/user/",imgname);
		if(qrImage.exists())
		{
			qrImage.delete();
		}
		try {
			qrImage.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(qrImage);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if(qrCodeBitmap == null)
		{
			Toast.makeText(context,"二维码为空", Toast.LENGTH_SHORT).show();
			return;			
		}
		qrCodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		try {
			fOut.flush();
			fOut.close();
//			Toast.makeText(context,"二维码生成成功", 0).show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 是否有邀请码
	 */
	public Boolean haveCode(){
		String imgname="code"+".png";
		File qrImage = new File("/sdcard/uparent/user/",imgname);
		if(!qrImage.exists()){
			return false;//不存在则新建路径
		}else{
			return true;
		}
	}
	/**
	 * 返回二维码所在路径
	 */
	public String  getCodePath(){
		if(haveCode()){
			String str=Environment.getExternalStorageDirectory().getAbsolutePath()+"/uparent/user/"+"code"+".png";
			return str;
		}else{
			return "";
		}
	}
}
