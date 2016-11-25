package com.seotech.dialog;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DialogUtil {
	private static Dialog dialog;
	private static ListView lv ;
	static MyAdapter adapter;
	static PopAdapter popadapter;
	private static PopupWindow popupWindow = null;
	
	public interface Listener {
		void SureOnClick();

		void CancleOnClick();

	}
	public interface SListener {
		void SureOnClick(List<Boolean> checklist);
		
		void CancleOnClick();
		
	}
//	public interface MyOnItemClickListener{
//		void ItemOnClick(int position);
//	}
//	

	public static Dialog createBaseDialog(Activity context, String title,
			Boolean settileVisibility, String text, View.OnClickListener onClickListener) {
		View inflater = LayoutInflater.from(context).inflate(
				R.layout.dialog_base, null);
		dialog = new Dialog(context, R.style.MyDialog);
		dialog.setContentView(inflater, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		dialog.show();
		// �����Ļ��࣬dialog����ʧ
		dialog.setCanceledOnTouchOutside(false);
		View line=(View)inflater.findViewById(R.id.line1);
		TextView titleTv = (TextView) inflater
				.findViewById(R.id.dialog_base_title_tv);
		TextView textTv = (TextView) inflater
				.findViewById(R.id.dialog_base_text_tv);
		titleTv.setText(title);
		textTv.setText(text);
		Button confirmBtn = (Button) inflater
				.findViewById(R.id.dialog_base_confirm_btn);
		if (!settileVisibility) {
			titleTv.setVisibility(View.GONE);
			line.setVisibility(View.GONE);
		}
		// ����dialog���button
		confirmBtn.setOnClickListener(onClickListener);
		return dialog;
	}
	
	public static PopupWindow createPopupWindow(Activity context,int id,
			 ArrayList<String> list,Double d, OnItemClickListener onClickListener) {
		if (popupWindow == null) {
			View view = LayoutInflater.from(context).inflate(
					R.layout.dialog_pop_list, null);
			WindowManager manger = (WindowManager)context.getSystemService(context.WINDOW_SERVICE);
			@SuppressWarnings("deprecation")
			int width = (int) (manger.getDefaultDisplay().getWidth() / d);
			lv = (ListView) view.findViewById(R.id.pop_listive);
			popadapter = new PopAdapter(context,list,null);
			lv.setAdapter(popadapter);
			// ����һ��PopuWidow����
			popupWindow = new PopupWindow(view, width,
					LayoutParams.WRAP_CONTENT);
		}
		// ʹ��ۼ�
		popupWindow.setFocusable(true);
		// ����������������ʧ
		popupWindow.setOutsideTouchable(true);
		// �����Ϊ�˵��������Back��Ҳ��ʹ����ʧ�����Ҳ�����Ӱ����ı���
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		// ��ʾ��λ��Ϊ:��Ļ�Ŀ�ȵ�һ��-PopupWindow�ĸ߶ȵ�һ��
		int xPos = windowManager.getDefaultDisplay().getWidth() / 2
				- popupWindow.getWidth() / 2;
		
	    popupWindow.showAsDropDown(context.findViewById(id), xPos, 0);
		
		lv.setOnItemClickListener(onClickListener);
		return popupWindow;
	}
	
	public static void dismissPopWindow(){
		if(popupWindow!=null){
			popupWindow.dismiss();
		}
	}

	public static Dialog createBaseCustomDialog(Activity context, String title,Boolean settileVisibility,
			String text, final Listener onClickListener) {
		View inflater = LayoutInflater.from(context).inflate(
				R.layout.dialog_base_custom, null);
		dialog = new Dialog(context, R.style.MyDialog);
		dialog.setContentView(inflater);
		/*new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT)*/
		dialog.show();
		// �����Ļ��࣬dialog����ʧ
		dialog.setCanceledOnTouchOutside(false);
		TextView titleTv = (TextView) inflater
				.findViewById(R.id.dialog_base_title_tv_custom);
		TextView textTv = (TextView) inflater
				.findViewById(R.id.dialog_base_text_tv_custom);
		titleTv.setText(title);
		textTv.setText(text);
		Button confirmBtnSure = (Button) inflater
				.findViewById(R.id.dialog_base_confirm_btn_custom_sure);
		Button confirmBtnCancel = (Button) inflater
				.findViewById(R.id.dialog_base_confirm_btn_custom_cancel);
		View line=(View)inflater.findViewById(R.id.line2);
		if (!settileVisibility) {
			titleTv.setVisibility(View.GONE);
			line.setVisibility(View.GONE);
		}
		// ����dialog���button
		confirmBtnSure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickListener.CancleOnClick();
			}
		});
		confirmBtnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickListener.CancleOnClick();

			}
		});
		return dialog;
	}

	public static void dismiss() {
		if(dialog!=null){
			dialog.dismiss();
		}
	}
	
	public static Dialog createCheckList(Activity activity,String titel,Boolean settileVisibility,List<String> list, final Boolean isSingle,final SListener onClickListener){
		final  View inflater =LayoutInflater.from(activity).inflate(R.layout.dialog_singleselection_view, null);
		dialog=new Dialog(activity, R.style.MyDialog);
		dialog.setContentView(inflater);
		dialog.show();
		// �����Ļ��࣬dialog����ʧ
		dialog.setCanceledOnTouchOutside(false);
		TextView titleTv=(TextView) inflater.findViewById(R.id.dialog_base_singl);
		titleTv.setText(titel);
		ListView listView=(ListView) inflater.findViewById(R.id.dialog_listview_singl);
		final MySinglAdapter adapter=new MySinglAdapter(activity,list,null,isSingle);
		listView.setAdapter(adapter);
		if (!settileVisibility) {
			titleTv.setVisibility(View.GONE);
		}
		
		final Button confirmBtnSure = (Button) inflater
				.findViewById(R.id.dialog_base_singl_sure);
		final Button confirmBtnCancel = (Button) inflater
				.findViewById(R.id.dialog_base_singl_cancel);
		confirmBtnSure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickListener.SureOnClick(adapter.checklist);
				dialog.dismiss();
			}
		});
		confirmBtnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickListener.CancleOnClick();
				dialog.dismiss();
			}
		});
		
		
		return dialog;
		
	}
	/**
	 * @param context 
	 * @param title ����
	 * @param settileVisibility �Ƿ���ʾ������
	 * @param notback ���÷��ؼ�
	 * @param text ����
	 * @param listener ����
	 * @return
	 */
	public static Dialog createBaseListDialog(Activity context, String title,Boolean titleVisibility,Boolean OnTouchOutside,Boolean notback,
			List<String> text,OnItemClickListener listener) {
		View inflater = LayoutInflater.from(context).inflate(
				R.layout.dialog_base_list, null);
		
	    dialog = new Dialog(context, R.style.MyDialog);
		dialog.setContentView(inflater, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		dialog.show();
		// �����Ļ��࣬dialog����ʧ
		dialog.setCanceledOnTouchOutside(OnTouchOutside);
		TextView titleTv = (TextView) inflater
				.findViewById(R.id.dialog_base_title_tv_list);
		titleTv.setText(title);
		ListView listView=(ListView) inflater.findViewById(R.id.dialog_listview_title_tv_list);
		listView.setAdapter(new MyAdapter(context, text, null));
		View line=(View)inflater.findViewById(R.id.line3);
		if (!titleVisibility) {
			titleTv.setVisibility(View.GONE);
			line.setVisibility(View.GONE);
			inflater.findViewById(R.id.line4).setVisibility(View.GONE);
		}
		listView.setOnItemClickListener(listener);
		if(notback){
			dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
				 @Override
				 public boolean onKey(DialogInterface dialog, int keyCode,
						 KeyEvent event) {
					 if (keyCode == KeyEvent.KEYCODE_BACK) {
						 return true;
					 }
					 	return false;
					 }
				 });
		}
		
		
		return dialog;
	}
	/**
	 * �ڲ��� List dialog  ������
	 */
	static class MyAdapter extends BaseAdapter{
		private Activity activity;
		private LayoutInflater layout;
		private List<String> list;
		public MyAdapter(Activity activity,List<String> list, LayoutInflater layout) {
			this.activity = activity;
			this.list=list;
			this.layout = LayoutInflater.from(activity);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder=null;
			if (convertView==null) {
				holder=new ViewHolder();
				convertView=layout.inflate(R.layout.dialog_list_item, null);
				holder.text=(TextView) convertView.findViewById(R.id.listview_item);
				convertView.setTag(holder);
			}
			else{
				holder=(ViewHolder) convertView.getTag();
			}
			holder.text.setText(list.get(position));
			
			return convertView;
		}
		class ViewHolder{
		private TextView text;	
		}
		
	}
	/**
	 * �ڲ��� singl dialog  ������
	 */
	static class MySinglAdapter extends BaseAdapter{
		private Activity activity;
		private LayoutInflater layout;
		private List<String> list;
		public List<Boolean> checklist = new ArrayList<Boolean>() ;
		private boolean isSingle=false;
		public MySinglAdapter(Activity activity,List<String> list ,LayoutInflater layout,boolean isSingle
				) {
			this.activity = activity;
			this.layout = LayoutInflater.from(activity);
			this.list = list;
			this.isSingle=isSingle;
			for(int i=0;i<this.list.size();i++){
				checklist.add(false);
			}
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list!=null?list.size():0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder=null;
			if (convertView==null) {
				holder=new ViewHolder();
				convertView=layout.inflate(R.layout.dialog_singleselection_item, null);
				holder.text=(TextView) convertView.findViewById(R.id.dialog_base_singl);
				holder.box=(CheckBox) convertView.findViewById(R.id.checkBox_singl);
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			holder.box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						if(isSingle){
							for(int i=0;i<checklist.size();i++){
								if(i!=position){
									checklist.set(i, false);
								}else{
									checklist.set(i, isChecked);
								}
							}
							notifyDataSetChanged();
						}
					}
					checklist.set(position, isChecked);
				}
			});
			
			holder.box.setChecked(checklist.get(position));
			
			holder.text.setText(list.get(position));
			return convertView;
		}
		
		class ViewHolder{
			private TextView text;	
			private CheckBox box;
			}
		
	}
	/**
	 * �ڲ��� List dialog  ������
	 */
	static class PopAdapter extends BaseAdapter{
		private Activity activity;
		private LayoutInflater layout;
		private List<String> list;
	
		public PopAdapter(Activity activity,List<String> list, LayoutInflater layout) {
			this.activity = activity;
			this.list=list;
			this.layout = LayoutInflater.from(activity);
			
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder=null;
			if (convertView==null) {
				holder=new ViewHolder();
				convertView=layout.inflate(R.layout.dialog_pop_item, null);
				holder.text=(TextView) convertView.findViewById(R.id.pop_item);
				convertView.setTag(holder);
			}
			else{
				holder=(ViewHolder) convertView.getTag();
			}
			
			holder.text.setText(list.get(position));
			
			return convertView;
		}
		class ViewHolder{
		private TextView text;	
		}
		
	}
	
}
