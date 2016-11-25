package com.lester.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dream.framework.utils.dialog.LodingDialog;
import com.lester.company.http.HttpURL;
import com.lester.headview.CustomImageView;
import com.lester.school.loader.ImageLoader;
import com.lester.school.requst.Constants;
import com.lester.uparent.GenerateCode;
import com.lester.uparent.MainActivity;
import com.lester.uparent.R;
import com.lester.uparent.SelectMyChild;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.lester.uteacher.tool.Userinfo;

public class homeFragment extends Fragment{

	private View view;
	private CustomImageView headimg;
	private LodingDialog lldialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view=getActivity().getLayoutInflater().inflate(R.layout.homefragment, 
				(ViewGroup) getActivity().findViewById(R.id.mainactivity),
				false);
		headimg=(CustomImageView)view.findViewById(R.id.head);
		String head=Userinfo.getchildphoto(getActivity());
		headimg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isSubParent();
			}
		});
		setHead(head);
		setChildname();
		setChildstatus(MainActivity.position_status);
		fixheadimgsize();
	}
	
	/**
	 * 记得在配置文件中加入以下代码
	 * <supports-screens
		android:smallScreens="true"
		android:normalScreens="true"
		android:largeScreens="true"
		android:resizeable="true"
		android:anyDensity="true" />
	 */
	private void fixheadimgsize(){
		LinearLayout.LayoutParams layoutParams=(android.widget.LinearLayout.LayoutParams) headimg.getLayoutParams();
		DisplayMetrics metric = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels; // 屏幕宽度（像素）
		int height = metric.heightPixels; // 屏幕高度（像素）
		float density = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）
		int densityDpi = metric.densityDpi; // 屏幕密度DPI（120 / 160 / 240）
		layoutParams.width=width/2;
		layoutParams.height=width/2;
		
		headimg.setLayoutParams(layoutParams);
	}
	
	public void setHead(String headurl){
		if(headurl!=null){
			new ImageLoader(getActivity()).DisplayImage(headurl, headimg);
		}
	}
	
	public void setChildname(){
		String name=Userinfo.getchildname(MainActivity.activity);
		Texttool.setText(view, R.id.childname, name);
	}
	public void setChildstatus(int status){
		//position_status含义，在校车1  在校园2  离校3
		String childstatus="";
		switch (status) {
		case 1:
			 childstatus="车上";
			break;
		case 2:
			 childstatus="在校";
			break;
		case 3:
			 childstatus="离校";
			break;
		}
		Texttool.setText(view, R.id.childstatus, childstatus);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ViewGroup p = (ViewGroup) view.getParent();
		if (p != null) {
			p.removeAllViewsInLayout();
		}
		return view;
	}
	
	//验证是否有权限
	public void isSubParent() {
		if(Net.isNetworkAvailable(getActivity())){
			return;
		}
		lldialog=LodingDialog.DialogFactor(getActivity(), "正在检查权限", false);
		new Thread(new Runnable() {
	        public void run() {
	            String service = "subParentService";
	            String method = "isSubParent";
	            String token = MD5.MD5(service+method+Constants.Key);
	            String params = 
	            		"{studentId:'"+Userinfo.getchildid(getActivity())+
	            		"',parentId:'"+Userinfo.getid(getActivity())+
	            		"'}";
	           Log.i("service", service);
	           Log.i("method", method);
	           Log.i("token", token);
	           Log.i("params", params);
	            NameValuePair pair1 = new BasicNameValuePair("service", service);
	            NameValuePair pair2 = new BasicNameValuePair("method", method);
	            NameValuePair pair3 = new BasicNameValuePair("token", token);
	            NameValuePair pair4 = new BasicNameValuePair("params", params);
	            List<NameValuePair> bodyParams = new ArrayList<NameValuePair>();
	            bodyParams.add(pair1);
	            bodyParams.add(pair2);
	            bodyParams.add(pair3);
	            bodyParams.add(pair4);
	            try {
	                HttpEntity requestHttpEntity = new UrlEncodedFormEntity(bodyParams, HTTP.UTF_8);
	                // URL使用基本URL即可，其中不需要加参数
	                HttpPost httpPost = new HttpPost(HttpURL.URL);
	                // 将请求体内容加入请求中
	                httpPost.setEntity(requestHttpEntity);
	                // 需要客户端对象来发送请求
	                HttpClient httpClient = new DefaultHttpClient();
	                // 发送请求
	                HttpResponse response = httpClient.execute(httpPost);
	                HttpEntity entity = response.getEntity();
	        		byte[] data = EntityUtils.toByteArray(entity);
	        		String result=new String(data,"UTF-8");
	        		Log.i("", "\n返回数据 : " + result);
	        		JSONObject jsonObj = new JSONObject(result);
	        		if (jsonObj.getString("success").equals("true")) {
	    				String jsonData = jsonObj.getString("data");
	    				JSONObject jsonObject = new JSONObject(jsonData);
	    				final Boolean isReceive=jsonObject.getBoolean("isReceive");
	    				getActivity().runOnUiThread(new Runnable() {
							public void run() {
								if(lldialog!=null){
									lldialog.dismiss();
								}
								if(isReceive){
									Intent intent =new Intent(getActivity(),GenerateCode.class);
									startActivity(intent);
								}else{
									Toast.makeText(getActivity(), "您没有接送权限", 0).show();
//									finish();
								}
							}
						});
//	    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<ArrayList<ParentData>>(){}.getType());
//	    				handler.sendMessage(handler.obtainMessage(Constants.GETPANRENTLIST, object));
	        		}else{
	    				handler.sendMessage(handler.obtainMessage(404, jsonObj.getString("message")));
	    			}
	            } catch (Exception e) {
	                e.printStackTrace();
	            } finally {

	            }
	        }
	    }).start();
	}
	
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			try {
				if(lldialog!=null){
					lldialog.dismiss();
			}
			switch (msg.what) {

			case 404:
				Toast.makeText(getActivity(), msg.obj.toString(), 0).show();
				break;
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		};
	};
}
