package com.dream.framework.utils.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;

import com.lester.uparent.R;
import com.lester.uparent.Welcome;
import com.lester.uteacher.texttool.Texttool;

public class MakesureDialog {
	
	
	private Dialog dialog;   
	private Activity activity;
	private ok isok;
	private cancell iscancell;
	public MakesureDialog(Activity activity,ok isok,cancell iscancell){
		this.activity=activity;
		this.isok=isok;
		this.iscancell=iscancell;
		dialog = new Dialog(this.activity, R.style.MyDialog); 
	}
	public void show(){
		dialog.setCanceledOnTouchOutside(false);
		View view=(View)activity.getLayoutInflater().inflate(R.layout.dialogmakesure, null);
		Texttool.setText(view, R.id.title, "有新版本啦，立即升级？");
		view.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isok.ok();
				dialog.dismiss();
			}
		});
		view.findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				iscancell.cancell();
				dialog.dismiss();
			}
		});
			dialog.setContentView(view);
			dialog.show();
	}
	
	public interface ok{
		void ok();
	}
	public interface cancell{
		void cancell();
	}
}
