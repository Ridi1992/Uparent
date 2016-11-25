package com.lester.uparent;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.baidu.mobstat.StatService;
import com.lester.headview.CustomImageView;
import com.lester.uparent.R;
import com.lester.uteacher.tool.Userhead;
import com.lester.uteacher.tool.Userinfo;

public class CodeDetail extends Activity implements OnClickListener{
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
		setContentView(R.layout.codedetail);
		initview();
	}

private void initview() {
	// TODO Auto-generated method stub
	findViewById(R.id.makesure).setOnClickListener(this);
	findViewById(R.id.back).setOnClickListener(this);
}

@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	switch (v.getId()) {
	case R.id.makesure://确认按钮
		
		break;
	case R.id.back://确认按钮
		finish();
		break;
		}
	}
}
