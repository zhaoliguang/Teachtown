package com.teachtown.adapter;

import java.util.List;

import net.tsz.afinal.FinalDb;

import com.hfut.teachtown.R;
import com.teachtown.activity.ExerciseActivity;
import com.teachtown.activity.TestFindActivity;
import com.teachtown.activity.TrialMatchingActivity;
import com.teachtown.model.Domain;
import com.teachtown.model.Exercise;
import com.teachtown.model.Lesson;
import com.teachtown.model.TestResultSync;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ExerciseListAdapter extends BaseAdapter implements OnClickListener{
	
	

	private Context context;
	private LayoutInflater inflater;
	private List<Exercise> exerciseList;
	private List<TestResultSync> testResultList;
	private FinalDb dataBase;
	private int lessonHandle;
	public ExerciseListAdapter(Context context,int lessonHandle, List<Exercise> exerciseList,List<TestResultSync> testResultList) 
	{
		
		super();
		this.context = context;
		this.lessonHandle = lessonHandle;
		this.exerciseList = exerciseList;
		this.testResultList = testResultList;
		this.inflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return exerciseList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
	    convertView = inflater.inflate(R.layout.exercise_list_item,
	    		parent,false);
	    holder = new ViewHolder();
	    holder.tv_exercise = ((TextView) convertView
	  	      .findViewById(R.id.tv_exercise));
	    holder.tv_pre_test = ((TextView) convertView
	      .findViewById(R.id.tv_pre_test));
	    holder.tv_prompt = ((TextView) convertView
	  	      .findViewById(R.id.tv_prompt));
	    holder.tv_prompt_level1 = ((TextView) convertView
	  	      .findViewById(R.id.tv_prompt_level1));
	    holder.tv_prompt_level2 = ((TextView) convertView
	  	      .findViewById(R.id.tv_prompt_level2));
	    holder.tv_post_test = ((TextView) convertView
	  	      .findViewById(R.id.tv_post_test));
	    convertView.setTag(holder);
	    
	    // 设置表格内容宽度，与表头对应
//	    holder.item1bjzb.setWidth(w * 0);
//	    holder.item2bjzb.setWidth(w * 3);
//	    holder.item3bjzb.setWidth(w * 6);
//	    holder.item4bjzb.setWidth(w * 6);
//
//
//	    holder.item1bjzb.setText("");
//	    holder.item2bjzb.setText("");
//	    holder.item3bjzb.setText("");
//	    holder.item4bjzb.setText("");
	    // holder.item4bjzb.setText("");
	    // holder.item5bjzb.setText("");
	   } else {
		   holder = (ViewHolder) convertView.getTag();
	   }
		Exercise exercise = exerciseList.get(position);
		if(position==0){
			 holder.tv_pre_test.setOnClickListener(this);
			 holder.tv_prompt.setOnClickListener(this);
			 holder.tv_prompt_level1.setOnClickListener(this);
			 holder.tv_prompt_level2.setOnClickListener(this);
			 holder.tv_post_test.setOnClickListener(this);
		}
		else{
			
//			 if(testResultList.size()!=0){
//			    	TestResult_Sync testResult = testResultList.get(position);
//				
//				    holder.tv_test_date.setText(testResult.getDateTaken().toString());
//				    holder.tv_correct.setText(testResult.getPercentCorrect()); 
//				    holder.tv_prompted.setText(testResult.getPercentPrompted()); 
//				    holder.tv_prompt_level.setText(testResult.getFinalPromptLevel()); 
//			    }
			holder.tv_exercise.setText(exercise.getName());
			holder.tv_pre_test.setText("-");
			holder.tv_prompt.setText("-");
			holder.tv_prompt_level1.setText("-");
			holder.tv_prompt_level2.setText("-");
			holder.tv_post_test.setText("-");	
		} 
	   
	   
	    

//	   // 设置隔行颜色
//	   if (position % 2 != 0) {
//	    convertView.setBackgroundResource(R.drawable.listview_color_1);
//	   } else {
//	    convertView.setBackgroundResource(R.drawable.listview_color_2);
//	   }
	   return convertView;
	}
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
	    Bundle bundle = new Bundle();
	    bundle.putInt("lessonHandle", lessonHandle);
	    intent.putExtras(bundle);
	    intent.setClass(context, TrialMatchingActivity.class);
		switch(view.getId()){
		case R.id.tv_pre_test:
			
			context.startActivity(intent);	
			break;
		case R.id.tv_prompt:
			context.startActivity(intent);	
			break;
		case R.id.tv_prompt_level1:
			context.startActivity(intent);	
			break;
		case R.id.tv_prompt_level2:
			context.startActivity(intent);	
			break;
		case R.id.tv_post_test:
			context.startActivity(intent);	
			break;
	}
}
}
class ViewHolder{
	public TextView tv_exercise;
	public TextView tv_pre_test;
	public TextView tv_prompt;
	public TextView tv_prompt_level1;
	public TextView tv_prompt_level2;
	public TextView tv_post_test;	
}


