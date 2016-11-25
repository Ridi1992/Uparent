package com.lester.uparent;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.baidu.mobstat.StatService;
import com.lester.uparent.R;
import com.lester.uteacher.adapter.MyStudentListviewAdapter;

public class MyStudent extends Activity implements OnClickListener , OnItemClickListener{
	private MyStudentListviewAdapter adapter;
	
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
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mystudent);
		initview();
	}

	private void initview() {
		findViewById(R.id.back).setOnClickListener(this);
		ListView myListView = (ListView) findViewById(R.id.slideCutListView);
		ArrayList<String> name=new ArrayList<String>();
        for(int i =0;i<40;i++){
        	name.add("item"+i);
        }
		adapter=new MyStudentListviewAdapter(getApplicationContext(),name);
		myListView.setAdapter(adapter);
		myListView.setOnItemClickListener(this);
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent =new Intent();
		intent.setClass(getApplicationContext(), ParentRegister.class);
		startActivity(intent);
	}

}
