package com.lester.uparent;



import com.baidu.mobstat.StatService;
import com.dream.framework.utils.dialog.LodingDialog;
import com.lester.uteacher.leave.Leavelist;
import com.lester.uteacher.texttool.Texttool;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

public class Leavelistdetail extends Activity implements OnClickListener{
	private LodingDialog lldialog;
	private Leavelist.datas datas;
	@Override
	protected void onResume() {
		super.onResume();
	 StatService.onResume(this);
	}
	@Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.leavelistdetail);
		datas=(Leavelist.datas) getIntent().getSerializableExtra("datas");
		initview();
	}

	private void initview() {
		findViewById(R.id.back).setOnClickListener(this);
		if(datas!=null){
			Texttool.setText(Leavelistdetail.this, R.id.starttime,"开始日期："+ datas.getStart_time().substring(0,16));
			Texttool.setText(Leavelistdetail.this, R.id.endtime, "结束日期："+datas.getEnd_time().substring(0,16));
			Texttool.setText(Leavelistdetail.this, R.id.content, datas.getContent());
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;

		default:
			break;
		}
	}
	
	

}
