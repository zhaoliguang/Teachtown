package com.teachtown.adapter;

import java.text.DecimalFormat;
import java.util.List;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.core.AsyncTask;

import com.hfut.teachtown.R;
import com.teachtown.activity.ExerciseActivity;
import com.teachtown.activity.ShowTestResult;
import com.teachtown.activity.TeachTownApp;
import com.teachtown.activity.TrialJointAttentionActivity;
import com.teachtown.activity.TrialMatchingActivity;
import com.teachtown.activity.TrialMultipleChoiceActivity;
import com.teachtown.activity.TrialReceptiveLabeActivity;
import com.teachtown.model.Domain;
import com.teachtown.model.Exercise;
import com.teachtown.model.Lesson;
import com.teachtown.model.Student;
import com.teachtown.model.TestResultSync;
import com.teachtown.model.Trial;
import com.teachtown.utils.UnityUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LessonGridViewAdapter extends BaseAdapter {
	
	private Context context;
	private LayoutInflater inflater;
	private List<Lesson> lessonList;
	private FinalDb dataBase;
	private TestResultSync  studentResult;
	private List<Trial> trialList;
	private double correctPercent;
	Viewholder holder;
	public LessonGridViewAdapter(Context context, List<Lesson> lessonList,FinalDb dataBase) {
		// TODO Auto-generated constructor stub
		super();
		this.context=context;
		inflater = LayoutInflater.from(context);
		this.lessonList = lessonList;
		this.dataBase = dataBase;
		//List<TestResultSync> testResultList = dataBase.findAllByWhere(TestResultSync.class, "studentId="+TeachTownApp.getStudentId());
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lessonList.size();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//Viewholder holder;
		
		final Lesson lesson = lessonList.get(position);
	  //  new AsyncDatabaseTask().execute(lesson.getLessonHandle());
		if (convertView == null) {
			 convertView = inflater.inflate(R.layout.gridview_item,
			    		parent,false);
			    holder = new Viewholder();
			    holder.tv_lesson_name = ((TextView) convertView
			  	      .findViewById(R.id.tv_lesson_name));
			    holder.lesson_info = ((ImageView) convertView
				  	      .findViewById(R.id.lesson_info));
			    
			    holder.tv_trial_name = ((TextView) convertView
			      .findViewById(R.id.tv_trial_name));
			    holder.tv_lesson_type = ((TextView) convertView
			  	      .findViewById(R.id.tv_lesson_type));
			    holder.tv_scorevalue = ((TextView) convertView
			  	      .findViewById(R.id.tv_scorevalue));
			    holder.iv_difficult = ((ImageView) convertView
			  	      .findViewById(R.id.iv_difficult));
			    holder.iv_level = ((ImageView) convertView
			  	      .findViewById(R.id.iv_level));
			    convertView.setTag(holder);
	   } else {
		   holder = (Viewholder) convertView.getTag();
	   }
	    
	    Domain domain = dataBase.findById(lesson.getDomainId(), Domain.class);
	   List<Trial> TrialeList = dataBase.findAllByWhere(Trial.class, "lessonHandle="+lesson.getLessonHandle());
	    int lessonHandle = lesson.getLessonHandle();
	    holder.tv_lesson_name.setText(lesson.getChineseName());   
	    holder.lesson_info.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
			    Bundle bundle = new Bundle();
			    String module = lesson.getModule();
			    int handle = lesson.getLessonHandle();
			    trialList = dataBase.findAllByWhere(Trial.class, "lessonHandle="+handle, "trialId");
			    int numDistractors = trialList.get(0).getNumDistractors();
			    bundle.putInt("lessonHandle", handle);
			    bundle.putString("lessonName", lesson.getChineseName());
				  if(module.equals("Matching")){
				    	 intent.putExtras(bundle);
				    	intent.setClass(context, TrialMatchingActivity.class);
				    }
				    else if(module.equals("ReceptiveLabel")){
				    	 bundle.putInt("numDistractors", numDistractors);
				    	 intent.putExtras(bundle);
				    	 intent.setClass(context, TrialReceptiveLabeActivity.class);
				    	 
				    }else if(module.equals("JointAttention")){
				    	 intent.putExtras(bundle);
				    	intent.setClass(context, TrialJointAttentionActivity.class);
				    	
				    }else if(module.equals("MultipleChoice")){
				    	 intent.putExtras(bundle); 
				    	intent.setClass(context, TrialMultipleChoiceActivity.class);
				    }
				  context.startActivity(intent);	

			}
		});
	    
	    holder.tv_scorevalue.setOnClickListener(new OnClickListener() {					
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
			    Bundle bundle = new Bundle();
			    bundle.putInt("lessonHandle", lesson.getLessonHandle());
			    bundle.putString("lessonName", lesson.getChineseName());
			    intent.putExtras(bundle); 
			    intent.setClass(context, ShowTestResult.class);	
			    context.startActivity(intent);
			}
		   });
		List<TestResultSync> studentResultList = dataBase.findAllByWhere(TestResultSync.class, "studentId="+TeachTownApp.getStudentId()+" and lessonHandle="+lessonHandle);
		if(studentResultList.size()>0){
			double testCount = 0;
			double correctCount = 0;
			for(TestResultSync testResult:studentResultList){
				testCount+=testResult.getTestCount()+testResult.getPromptTestCount();
				correctCount+=testResult.getCorrectCount()+testResult.getPromptCorrectCount();
				
			}
			
			correctPercent=correctCount/testCount;
		}
		else{
			correctPercent = 0;
		}
		int difficultLevel = lesson.getGroupId();
		correctPercent = correctPercent*100;
		switch(difficultLevel){
		case 1:
			holder.iv_difficult.setImageDrawable(context.getResources().getDrawable(R.drawable.difficulty_1star));
	    	holder.iv_level.setImageDrawable(context.getResources().getDrawable(R.drawable.lv00_stone));
			break;
		case 2:
			holder.iv_difficult.setImageDrawable(context.getResources().getDrawable(R.drawable.difficulty2star));
	    	holder.iv_level.setImageDrawable(context.getResources().getDrawable(R.drawable.lv01_copper));
			break;
		case 3:
			holder.iv_difficult.setImageDrawable(context.getResources().getDrawable(R.drawable.difficulty_3star));
	    	holder.iv_level.setImageDrawable(context.getResources().getDrawable(R.drawable.lv02_silver));
			break;
		case 4:
	    	holder.iv_difficult.setImageDrawable(context.getResources().getDrawable(R.drawable.difficulty_4star));
	    	holder.iv_level.setImageDrawable(context.getResources().getDrawable(R.drawable.lv03_gold));
			break;
		case 5:
			holder.iv_difficult.setImageDrawable(context.getResources().getDrawable(R.drawable.difficulty_5star));
	    	holder.iv_level.setImageDrawable(context.getResources().getDrawable(R.drawable.lv04_diamond));
			break;
		}
		
	    if(correctPercent<=25)
	    {
	    	holder.tv_scorevalue.setTextColor(Color.rgb(28, 31, 30));
	    	
	    }else if(correctPercent<=50){
	    	holder.tv_scorevalue.setTextColor(Color.rgb(0, 102, 51));
	    }else if(correctPercent<=75){
	    	holder.tv_scorevalue.setTextColor(Color.rgb(0, 102, 153));
	    	
	    }else if(correctPercent<=100){
	    	holder.tv_scorevalue.setTextColor(Color.rgb(255, 153, 0));

	    }
	    
	    DecimalFormat df=new   java.text.DecimalFormat("#");
		String score= df.format(correctPercent);
		holder.tv_scorevalue.setText(score+"%");
	    holder.tv_lesson_type.setText(domain.getChineseName()); 
	   StringBuilder trialBuilder = new StringBuilder();
	  
	    for(Trial trial:TrialeList){
	    	trialBuilder.append(trial.getChineseName()).append(",");
	    	holder.tv_trial_name.setText(trialBuilder.toString());
	    }
	   
	    	

	   return convertView;
	}
	
	class AsyncDatabaseTask extends AsyncTask<Integer, Integer, List<Trial>>{

		@Override
		protected List doInBackground(Integer... lessonHandle) {
			// TODO Auto-generated method stub
			return dataBase.findAllByWhere(Trial.class, "lessonHandle="+lessonHandle[0].intValue());
		}
		
		@Override
		protected void onPostExecute(List<Trial> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			StringBuilder trialBuilder = new StringBuilder();
		    for(Trial trial:result){
		    	trialBuilder.append(trial.getChineseName()).append(",");
		    	holder.tv_trial_name.setText(trialBuilder.toString());
	    }
		}
	}

	public List<Lesson> getLessonList() {
		return lessonList;
	}

	public void setLessonList(List<Lesson> lessonList) {
		this.lessonList = lessonList;
	} 
}

class Viewholder{
	
		public TextView tv_lesson_name;
		public TextView tv_trial_name;
		public TextView tv_lesson_type;
		public TextView tv_scorevalue;
		
		public ImageView iv_difficult;
		public ImageView lesson_info;
		public ImageView iv_level;
}

