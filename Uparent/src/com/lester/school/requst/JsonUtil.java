package com.lester.school.requst;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @类名: JsonUtil<br>
 *
 * @包名：com.gbframe.monopoly.util<br>
 *
 * @应用名称：渭南专卖监管移动终�?br>
 *
 * @�?��单位：西安卓邦软件有限公�?br>
 *
 * @功能说明�?br>
 *
 * @author�?Administrator<br>
 *
 * @version�?.0 2014-2-26上午9:34:27 Administrator 发布
 */
public abstract class JsonUtil {
	
	private static GsonBuilder	builder	= new GsonBuilder();
	
	public static Gson instance() {
		Gson gson = builder.setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		return gson;
	}
	
//	public static int getResult(Object object) {
//		if (null == object) {
//			return 0;
//		}
//		JSONObject jsonObject = null;
//		try {
//			jsonObject = new JSONObject(object.toString());
//			return Integer.parseInt(jsonObject.get("result").toString().trim());
//		}
//		catch (JSONException e) {
//			e.printStackTrace();
//			return 0;
//		}
//	}
//	
//	public static int getIrid(Object object) {
//		if (null == object) {
//			return 0;
//		}
//		JSONObject jsonObject = null;
//		try {
//			jsonObject = new JSONObject(object.toString());
//			return Integer.parseInt(jsonObject.get("irid").toString().trim());
//		}
//		catch (JSONException e) {
//			e.printStackTrace();
//			return 0;
//		}
//	}
//	
//	public static List<String[]> getJsonObj(String jsonData) {
//		List<String[]> data = new ArrayList<String[]>();
//		jsonData = jsonData.trim().substring(1, jsonData.length() - 1);
//		String[] items = jsonData.split(",");
//		for (String item : items) {
//			data.add(item.split("="));
//		}
//		return data;
//	}
	// public static List<String []> getJsonObj(String jsonData){
	// List<String[]> data = new ArrayList<String[]>();
	// JsonReader reader = new JsonReader(new StringReader(jsonData));
	// try {
	// reader.beginObject(); // �?��解析对象
	// while (reader.hasNext()) {
	// String [] item = new String[2];
	// item[0] = reader.nextName();
	// item[1] = null == reader.nextString() ? "" : reader.nextString();
	// data.add(item);
	// }
	// reader.endObject();
	// }
	// catch (IOException e) {
	// e.printStackTrace();
	// }
	// return data;
	// }
}
