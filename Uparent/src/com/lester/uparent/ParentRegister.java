package com.lester.uparent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

import com.baidu.mobstat.StatService;
import com.lester.uparent.R;
import com.lester.uteacher.tool.PhotoPickUtil;
import com.lester.uteacher.tool.PhotoPickUtil.OnPhotoPickedlistener;

public class ParentRegister extends Activity implements OnClickListener,OnPhotoPickedlistener {
	private PhotoPickUtil photoPickUtil;
	
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
		setContentView(R.layout.parentregister);
		 initview();
	}
	private void initview() {
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.camera).setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
	switch (v.getId()) {
	case R.id.back:
		finish();
		break;
	case R.id.camera:
		photoPickUtil=new PhotoPickUtil(ParentRegister.this, this);
		photoPickUtil.doPickPhotoAction(true, 300, 300);
		break;
	default:
		break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		photoPickUtil.pickResult(requestCode, resultCode, data);
	}
	@Override
	public void photoPicked(String path, Bitmap bmp) {
		if(bmp!=null){
			setimg(bmp);
		}
	}
	private void setimg(Bitmap bmp) {
		// TODO Auto-generated method stub
		ImageView img =(ImageView) findViewById(R.id.camera);
		img.setImageBitmap(bmp);
	}
}
