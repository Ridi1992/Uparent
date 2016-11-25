package com.lester.uteacher;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.lester.headview.CustomImageView;
import com.lester.uteacher.mystudent.Student;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.Loadhead;

public class Mystudent_detail extends Activity implements OnClickListener{

	private Student.datas datas;
	

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
	
	setContentView(R.layout.studentdetail);
	initview();
	}

	private void initview() {
		datas=(Student.datas) getIntent().getSerializableExtra("datas");
		findViewById(R.id.back).setOnClickListener(this);
		CustomImageView headimg=(CustomImageView)findViewById(R.id.childhead);
		String head=datas.getPhoto();
		if(head!=null && (!head.equals(""))){
				new Loadhead(this,head, headimg).execute();
			 }
		Texttool.setText(Mystudent_detail.this, R.id.studentname, datas.getName());
		Texttool.setText(Mystudent_detail.this, R.id.id_card, datas.getId_card());
//		Texttool.setText(Mystudent_detail.this, R.id.birthplace, datas.getBirthplace());
//		Texttool.setText(Mystudent_detail.this, R.id.registered_place, datas.getRegistered_place());
		Texttool.setText(Mystudent_detail.this, R.id.urgent_link_man, datas.getUrgent_link_man());
		Texttool.setText(Mystudent_detail.this, R.id.urgent_link_phone, datas.getUrgent_link_phone());
		Texttool.setText(Mystudent_detail.this, R.id.address, datas.getAddress());
		Texttool.setText(Mystudent_detail.this, R.id.entry_time, "入园时间\t\t"+datas.getEntry_time().substring(0, 10));
		Texttool.setText(Mystudent_detail.this, R.id.birthday, "生日\t\t "+datas.getBirthday().substring(0, 10));
		Texttool.setText(Mystudent_detail.this, R.id.nation, datas.getNation_display());
		if(datas.getGender().equals("F")){
			((ImageView)findViewById(R.id.sex)).setImageResource(R.drawable.nv);
		}else{
			((ImageView)findViewById(R.id.sex)).setImageResource(R.drawable.nan);
		}
		TextView  textView=(TextView) findViewById(R.id.status);
		switch (datas.getPosition_status()) {
		case 1:
			Texttool.setText(textView, "车上");
			textView.setBackgroundResource(R.drawable.ischeshang);
			break;
		case 2:
			Texttool.setText(textView, "在校");
			textView.setBackgroundResource(R.drawable.isschool);
			break;
		case 3:
			Texttool.setText(textView, "离校");
			textView.setBackgroundResource(R.drawable.notinschool);
			break;
		}
		setparentinfo();
	}

	/**
	 * 设置家长信息
	 */
	private void setparentinfo() {
	View view =findViewById(R.id.layout);
		for(int i=0;i<datas.getParents().size();i++){
			View v=getLayoutInflater().inflate(R.layout.mystudent_parentinfo, null);
			CustomImageView headimg=(CustomImageView)v.findViewById(R.id.parenthead);
			String head=datas.getParents().get(i).getParentPhoto();
			if(head!=null && (!head.equals(""))){
					new Loadhead(this,head, headimg).execute();
				 }
			
			if(datas.getParents().get(i).getParentGender().equals("女")){
				((ImageView)v.findViewById(R.id.sex)).setImageResource(R.drawable.nv);
			}else{
				((ImageView)v.findViewById(R.id.sex)).setImageResource(R.drawable.nan);
			}
			
			Texttool.setText(v, R.id.name, datas.getParents().get(i).getParentName());
//			Texttool.setText(v, R.id.company, datas.getParents().get(i).getParentCompany());
//			Texttool.setText(v, R.id.education, datas.getParents().get(i).getParentEducation());
			Texttool.setText(v, R.id.tel, datas.getParents().get(i).getParentMobilePhone());
			Texttool.setText(v, R.id.parentIdCard, datas.getParents().get(i).getParentIdCard());
			final int j=i;
			v.findViewById(R.id.call).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String Tel=datas.getParents().get(j).getParentMobilePhone();
					if(Tel==null || Tel.equals("")){
						Toast.makeText(Mystudent_detail.this, "电话号码为空", 0).show();
					}else{
						Log.i("", Tel);
						Intent intent = new Intent();
						intent.setAction(Intent.ACTION_CALL);
						intent.setData(Uri.parse("tel:"+Tel));
						startActivity(intent);
					}
				}
			});
			/*v.findViewById(R.id.sendmessage).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(My_student.isclassteacher){
						Intent intent =new Intent(Mystudent_detail.this,Sendmessage.class);
						intent.putExtra("datas", datas.getParents().get(j));
						startActivity(intent);
					}else{
						Toast.makeText(getApplicationContext(), "您不是带班老师", 0).show();
					}
				
				}
			});*/
			((ViewGroup) view).addView(v);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;

		default:
			break;
		}
	}
}
