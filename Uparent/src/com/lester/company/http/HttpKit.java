package com.lester.company.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpKit {

	public static HttpResponse reg(String url,String parmer) throws ClientProtocolException, IOException{
		//创建HttpClient
		HttpClient client = new DefaultHttpClient();
		//提交方法
//		HttpGet request = new HttpGet(url);
		HttpPost request = new HttpPost(url);
		if (parmer != null) {
			StringEntity entity = new StringEntity(parmer,HTTP.UTF_8);
			entity.setContentType("application/x-www-form-urlencoded");
			request.setEntity(entity); 
		}
		
		HttpResponse response = client.execute(request);  
		return response;
	}
	
	public static String getData(HttpResponse response) throws IOException{
		HttpEntity entity = response.getEntity();
		//拉数据到数组�?
		byte[] data = EntityUtils.toByteArray(entity);
		//转换成字符串
		return new String(data,"UTF-8");
//		return new String(data,HTTP.UTF_8);
	}

}
