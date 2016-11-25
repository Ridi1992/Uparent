package com.lester.uteacher.tool;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
public class Appinfo {
	  public static String getVersionName(Context context) {
			try {
				 PackageManager pm = context.getPackageManager();
					PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
					String versionName = null;
					if (pi != null) {
						versionName = pi.versionName == null ? "null" : pi.versionName;
					}
				return  versionName;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	  public static String getVersionCode(Context context) {
			try {
				 PackageManager pm = context.getPackageManager();
					PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
					String versionCode = null ;
					if (pi != null) {
						versionCode = pi.versionCode + "";
					}
				return  versionCode;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} 
	 /**
	  * 获取制造商
	 * @return
	 */
	public static String getMODEL(){
		 return android.os.Build.MODEL;
	 }
	 
	 /**
	  * 
	  * 获取设备型号
	 * @return
	 */
	public static String getRELEASE(){
		 return  android.os.Build.VERSION.RELEASE;
	 }
}
