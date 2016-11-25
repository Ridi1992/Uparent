/**   
 * @Title: LodingDialog.java
 * @Package com.dream.framework.utils.dialog
 * @Description:
 * @author wuguozhi
 * @date 2014年12月26日 下午3:43:32 
 */
package com.dream.framework.utils.dialog;





import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.lester.uparent.R;

/**
 * @ClassName: LodingDialog
 * @Description:
 * @author wuguozhi
 * @date 2014年12月26日 下午3:43:32
 */
public class LodingDialog extends Dialog {

	private Context context = null;

	public LodingDialog(Context context) {
		super(context);
		 setOwnerActivity((Activity)context);
		this.context = context;
	}

	public LodingDialog(Context context, int theme) {
		super(context,theme);
		this.context = context;
	}
	
	public static LodingDialog DialogFactor(final Context context,String strMessage,boolean bl){
		LodingDialog lodingDialog=new LodingDialog(context, R.style.LodingDialog);
		lodingDialog.setContentView(R.layout.dialog_loading);
		TextView tvMsg = (TextView)lodingDialog.findViewById(R.id.loading_title);
		lodingDialog.setCanceledOnTouchOutside (bl);
    	if (tvMsg != null){
    		tvMsg.setText(strMessage);
    	}
    	/**
    	 * 禁用到返回键
    	 */
		/*lodingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					return true;
				}
				return false;
			}
		});*/
    	lodingDialog.show();
		return lodingDialog;
	}
}
