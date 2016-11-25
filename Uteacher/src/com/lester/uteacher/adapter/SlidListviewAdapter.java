package com.lester.uteacher.adapter;

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

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.dream.framework.utils.dialog.LodingDialog;
import com.google.gson.reflect.TypeToken;
import com.lester.company.http.HttpURL;
import com.lester.headview.CustomImageView;
import com.lester.school.requst.Constants;
import com.lester.school.requst.JsonUtil;
import com.lester.slidecutlistview.CustomSwipeListView;
import com.lester.uteacher.GotOn;
import com.lester.uteacher.R;
import com.lester.uteacher.GotOn.redata;
import com.lester.uteacher.goton.Student;
import com.lester.uteacher.goton.Student.datas;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.Loadhead;
import com.lester.uteacher.tool.MD5;
import com.lester.uteacher.tool.Net;
import com.lester.uteacher.tool.Timetool;
import com.lester.uteacher.tool.Userinfo;

public class SlidListviewAdapter extends BaseAdapter {
private List<datas> datas;
private Context context;
private Handler handler;
public Boolean [] ischeck;
public LodingDialog lldialog;
private Activity a;
	public SlidListviewAdapter(Activity a, Context context, List<datas> datas, Handler handler) {
		this.datas=datas;
		this.context=context;
		this.handler=handler;
		this.a=a;
		if(this.datas!=null && this.datas.size()!=0){
			ischeck=new Boolean[this.datas.size()];
			GotOn.canslid = new Boolean [datas.size()];
			GotOn.status = new TextView [datas.size()];
			GotOn.check = new CheckBox [datas.size()];
			for(int i=0; i<this.datas.size();i++){
				ischeck[i]=false;
			}
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(datas!=null ){
			return datas.size();
		}else{
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView=LayoutInflater.from(context).inflate(R.layout.listview_item_slideview, null);
		}
		CustomImageView headimg=(CustomImageView) convertView.findViewById(R.id.childhead);
		if(datas.get(position).getPhoto()!=null &&
					 (!datas.get(position).getPhoto().equals(""))){
				new Loadhead(a,datas.get(position).getPhoto(), headimg).execute();
			 }
//		headimg.setImageResource(R.drawable.ic_launcher);
		if(datas.get(position).getPosition_status()!=2){//不在校时显示请假状态
			if(datas.get(position).getHasLeaveRecord()){//请假了显示“请假”
				convertView.findViewById(R.id.hasLeaveRecord).setVisibility(View.VISIBLE);
			}else{//没请假不显示“请假”
				convertView.findViewById(R.id.hasLeaveRecord).setVisibility(View.GONE);
			}
		}else{//在校时就算请假了也要隐藏请假状态
			convertView.findViewById(R.id.hasLeaveRecord).setVisibility(View.GONE);
		}
		
		Texttool.setText(convertView, R.id.childname, datas.get(position).getName());
		Texttool.setText(convertView, R.id.address, datas.get(position).getAddress());
		TextView bt=(TextView) convertView.findViewById(R.id.bt);
		final View view=convertView;
		final int reposition=position;
			bt.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					System.out.println("撤销按钮");
					new  CustomSwipeListView(context).cancelSlide(view);
					enrtyBusRestore(reposition, view);
				}
			});
		final CheckBox ch=(CheckBox) convertView.findViewById(R.id.check);
		ch.setChecked(ischeck[position]);
		convertView.findViewById(R.id.checkbt).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(GotOn.check[reposition].isClickable()){
					if(ch.isChecked()){
						ch.setChecked(false);
					}else{
						ch.setChecked(true);
					}
					ischeck[reposition]=ch.isChecked();
				}
			}
		});
//		ch.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				ischeck[position]=ch.isChecked();
//			}
//		});	
		GotOn.check[position]=ch;
		GotOn.status[position]=(TextView) convertView.findViewById(R.id.position_status);
		GotOn.setstatus(GotOn.status[position],datas.get(position).getPosition_status(),position);//设置学生状态	
		return convertView;
	}
	/**
	 * 还原
	 *
	 */
	private  void enrtyBusRestore (int position,View view){
			final int i=position;
			if(Net.isNetworkAvailable(context)){
				return;
			}
			GotOn.reposition=i;
			lldialog=LodingDialog.DialogFactor(a, "正在还原", false);
			new Thread(new Runnable() {
		        public void run() {
		            String service = GotOn.outservice;
		            String method = GotOn.outmethod;
		            String token = MD5.MD5(service+method+Constants.Key);
		            String params = "{loginId :'"+Userinfo.getmobile_phone(a)+
		            		"',studentId:'"+datas.get(i).getId()+
		            		"',transferTime:'"+Timetool.gettime()+"'}";
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
		    				Object  object=JsonUtil.instance().fromJson(jsonData, new TypeToken<redata>(){}.getType());
		    				handler.sendMessage(handler.obtainMessage(Constants.ENRTYBUSRESTORE, object));
		    			}else{
		    				handler.sendMessage(handler.obtainMessage(404, jsonObj.getString("message")));
		    			}
		                // 显示响应
		            } catch (Exception e) {
		                e.printStackTrace();
		            } finally {

		            }
		        }
		    }).start();
		}
		
//	}
}
