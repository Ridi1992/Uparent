package com.teacher.View;


import com.lester.uparent.Feedback;
import com.lester.uparent.R;
import com.lester.uteacher.texttool.Texttool;
import com.teacher.data.FeedBackData;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
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
		}else{
			et.setVisibility(View.GONE);
			for(int i=0;i<feedBackData.getOptions().size();i++){
				View view = LayoutInflater.from(context).inflate(R.layout.myfeedbackt_item, null);
				((TextView) view.findViewById(R.id.str)).setText(feedBackData.getOptions().get(i).getTitel());
				ImageView check=(ImageView) view.findViewById(R.id.check);
				if(feedBackData.getOptions().get(i).isIscheck()){
					check.setImageResource(R.drawable.feed1);
				}else{
					check.setImageResource(R.drawable.feed0);
				}
				layout.addView(view);
			}
		}
	}
}
