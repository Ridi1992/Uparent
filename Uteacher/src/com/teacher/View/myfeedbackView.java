package com.teacher.View;


import java.util.ArrayList;

import javax.crypto.spec.PSource;

import com.lester.uteacher.Feedback;
import com.lester.uteacher.R;
import com.lester.uteacher.texttool.Texttool;
import com.teacher.data.FeedBackData;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class myfeedbackView extends LinearLayout{

	private LinearLayout layout;
	private Context context;
	private TextView titel0,titel1;
	private int position;
	private Feedback mcontext;
	private EditText et;
	public myfeedbackView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
	}
	public void indata(Feedback mcontext, int arg0) {
		position=arg0;
		this.mcontext=mcontext;
		FeedBackData feedBackData=mcontext.data.get(arg0);
		String content=feedBackData.getContent();
		String subcontent=feedBackData.getSubcontent();
		titel0=(TextView) this.findViewById(R.id.titel0);
		titel1=(TextView) findViewById(R.id.titel1);
		layout=(LinearLayout) findViewById(R.id.myadd_ll);
		if(content==null || content.equals("")){
			titel0.setVisibility(View.GONE);
		}else{
			titel0.setVisibility(View.VISIBLE);
		}
		titel0.setText(content);
		titel1.setText(subcontent);
		et=(EditText)this.findViewById(R.id.et);
		if(feedBackData.isInput()){
			et.setVisibility(View.VISIBLE);
			Texttool.setText(et, myfeedbackView.this.mcontext.data.get(position).getSpecial_act());
			if(mcontext.show){
				et.setFocusable(false);
			}else{
				et.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						myfeedbackView.this.mcontext.mAdapter.selectposition=position;
						return false;
					}
				});
				et.addTextChangedListener(new TextWatcher() {
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
					}
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count,
							int after) {
					}
					@Override
					public void afterTextChanged(Editable s) {
						if(s!=null){
							myfeedbackView.this.mcontext.data.get(position).setSpecial_act(s.toString());
						}
					}
				});
			}
			
		}else{
			et.setVisibility(View.GONE);
			final ArrayList<ImageView> arrayList=new ArrayList<ImageView>();
			for(int i=0;i<feedBackData.getOptions().size();i++){
				View view = LayoutInflater.from(context).inflate(R.layout.myfeedbackt_item, null);
				((TextView) view.findViewById(R.id.str)).setText(feedBackData.getOptions().get(i).getTitel());
				final ImageView check=(ImageView) view.findViewById(R.id.check);
				if(feedBackData.getOptions().get(i).isIscheck()){
					check.setImageResource(R.drawable.feed1);
				}else{
					check.setImageResource(R.drawable.feed0);
				}
				if(!mcontext.show){
					arrayList.add(check);
					final int j=i;
					view.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							for(int k=0;k<myfeedbackView.this.mcontext.data.get(position).getOptions().size();k++){
								myfeedbackView.this.mcontext.data.get(position).getOptions().get(k).setIscheck(false);
								if(j==k){
									arrayList.get(k).setImageResource(R.drawable.feed1);
								}else{
									arrayList.get(k).setImageResource(R.drawable.feed0);
								}
							}
							myfeedbackView.this.mcontext.data.get(position).getOptions().get(j).setIscheck(true);
//							myfeedbackView.this.mcontext.refresh();
						}
					});
				}
				layout.addView(view);
			}
		}
	}
	
	public void setInputTypeIsNumber(){
		et.setInputType(InputType.TYPE_CLASS_PHONE);
	}
	
	public void getfocus(){
		et.requestFocus();
	}
}
