package com.lester.uteacher.tool;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.text.format.Time;

/**
 * 获取当前系统时间
 *
 */
public class Timetool {
	public static String gettime(){
		 Time time = new Time("GMT+8");       
	     time.setToNow();      
	     int year = time.year;      
	     int month = time.month;      
	     int day = time.monthDay;      
	     int minute = time.minute;      
	     int hour = time.hour;      
	     int sec = time.second;      
//	     myTextView.setText("当前时间为：" + year +       
//	                         "年 " + month +       
//	                         "月 " + day +       
//	                         "日 " + hour +       
//	                         "时 " + minute +       
//	                         "分 " + sec +       
//	                         "秒");  
		return year + 
				"- " + month +       
				"-" + day +       
				"\t " + hour + 
				":" + minute +       
				":" + sec;
	}
	public static long timetool(long day){
		long ll=(long) (day*24*60*60*1000.0f);
		System.out.println("day="+day+"时间戳="+ll);
		return ll;
	}
	 /**
	 * 获取当前时间
	 */
	public static String getnowtime(){
		 return trans_time( (long) (System.currentTimeMillis()/1000f));
	 }
	/**
	 * 将时间转换成字符串
	 */
	public static String trans_time(long time){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			long lcc_time = Long.valueOf(time); 
			String re_StrTime = sdf.format(new Date(time * 1000L));  
			System.out.println("时间为："+re_StrTime);
			return re_StrTime; 
		} 
		catch (Exception e) {
			return ""; 
		}
	}
	/**
	 *返回day天前的日期
	 */
	public static String daybefor(long day){
		long time1=(long) (System.currentTimeMillis()-timetool(day));//当前时间减去day天的时间
		return trans_time((long) (time1/1000f));
	}
}
