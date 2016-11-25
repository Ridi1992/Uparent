package com.lester.uteacher.texttool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

public class Texttool {
	public static void setText(View view, int id,String text){
		TextView te=(TextView) view.findViewById(id);
		if(te!=null){
			if(text!=null && !text.equals("")){
				te.setText(text);
			}
		}
	}
	public static void setText(Activity a, int id,String text){
		TextView te=(TextView) a.findViewById(id);
		if(te!=null){
			if(text!=null && !text.equals("")){
				te.setText(text);
				System.out.println("text==="+text);
			}
		}
	}
	public static void setText(TextView view,String text){
		if(view!=null){
			if(text!=null && !text.equals("")){
				view.setText(text);
			}
		}
	}
	/**
	 * 是否有内容
	 */
	public static Boolean Havecontent(TextView te){
			if(te !=null && !(te.getText().toString().equals(""))){
				return true;
			}else{
				return false;
			}
		
	}
	
	public static Boolean Havecontent(Activity a,int id){
		TextView text=(TextView) a.findViewById(id);
		if(text !=null && !(text.getText().toString().equals(""))){
			return true;
		}else{
			return false;
		}
	
	}
	public static String Gettext(TextView te){
		if(te!=null){
			if(!te.getText().equals("")){
				return te.getText().toString();
			}else{
				return "";
			}
		}else{
			return "";
		}
		
	}
	public static String Gettext(Activity a,int id){
		TextView te=(TextView) a.findViewById(id);
		if(te!=null){
			if(!te.getText().equals("")){
				return te.getText().toString();
			}else{
				return "";
			}
		}else{
			return "";
		}
		
	}
	
	/**
	 * 验证身份证
	 */
	public static Boolean  Patternidcard(String str){
		String idcard = "^([1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3})|([1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X))$";
		Pattern regex = Pattern.compile(idcard);
		Matcher matcher = regex.matcher(str);
		boolean flagPhone = matcher.matches();
		return flagPhone;
	}
	
	/**
	 * 验证手机号
	 */
	public static  Boolean  Pattern_phone(String str){
		String PHONE = "^(((13[0-9])|(15([0-9]))|(18[0-9])|(17[0-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$";
		String EMAIL = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern regex = Pattern.compile(PHONE);
		Matcher matcher = regex.matcher(str);
		boolean flagPhone = matcher.matches();
		return flagPhone;
	}
	
	//导致TextView异常换行的原因：安卓默认数字、字母不能为第一行以后每行的开头字符，因为数字、字母为半角字符 
    //所以我们只需要将半角字符转换为全角字符即可，方法如下 
    public static String ToSBC(String input) { 
    	 char[] c = input.toCharArray();  
    	   for (int i = 0; i< c.length; i++) {  
    	       if (c[i] == 12288) {
    	         c[i] = (char) 32;  
    	         continue;  
    	       }if (c[i]> 65280&& c[i]< 65375)  
    	          c[i] = (char) (c[i] - 65248);  
    	       }  
	   return new String(c); 
    } 
    
    /**
     *  替换、过滤特殊字符  
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String StringFilter(String str) throws PatternSyntaxException{  
        str=str.replaceAll("【","[").replaceAll("】","]").replaceAll("！","!");//替换中文标号  
        String regEx="[『』]"; // 清除掉特殊字符  
        Pattern p = Pattern.compile(regEx);  
        Matcher m = p.matcher(str);  
     return m.replaceAll("").trim();  
    }
} 
