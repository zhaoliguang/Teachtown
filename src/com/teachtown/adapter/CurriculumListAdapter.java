package com.teachtown.adapter;

import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import net.tsz.afinal.FinalDb;

import com.hfut.teachtown.R;
import com.teachtown.activity.ExerciseActivity;
import com.teachtown.model.Domain;
import com.teachtown.model.Exercise;
import com.teachtown.model.Lesson;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CurriculumListAdapter extends BaseAdapter {
	
	private Context context;
	private LayoutInflater inflater;
	private List<Lesson> lessonList;
	private FinalDb dataBase;
	public CurriculumListAdapter(Context context, List<Lesson> lessonList,FinalDb dataBase) {
	
		super();
		this.context=context;
		inflater = LayoutInflater.from(context);
		this.lessonList = lessonList;
		this.dataBase = dataBase;
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
	   
		Holder holder;
		if (convertView == null) {
	    convertView = inflater.inflate(R.layout.curriculum_list_item,
	    		parent,false);
	    holder = new Holder();
	    holder.ll_lesson_title = (View)convertView.findViewById(R.id.ll_lesson_title);
	    holder.icon_big_iv = ((ImageView) convertView
	      .findViewById(R.id.icon_big_iv));
	    holder.tv_lesson_name = ((TextView) convertView
	      .findViewById(R.id.tv_lesson_name));
	    holder.tv_domain_name = ((TextView) convertView
	      .findViewById(R.id.tv_domain_name));
	    holder.tv_level = ((TextView)convertView
	      .findViewById(R.id.tv_level));
	    holder.tv_exercises =  ((TextView)convertView
	  	      .findViewById(R.id.tv_exercises));
	    holder.tv_text3 = ((TextView)convertView
	  	      .findViewById(R.id.tv_text3));
	    holder.tv_icon3 = ((ImageView)convertView
	  	      .findViewById(R.id.tv_icon3));
	    holder.tv_level = ((TextView)convertView
	  	      .findViewById(R.id.tv_description));
  
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
		   holder = (Holder) convertView.getTag();
	   }
	    final Lesson lesson = lessonList.get(position);
	    Domain domain = dataBase.findById(lesson.getDomainId(), Domain.class);
	    List<Exercise> exerciseList = dataBase.findAllByWhere(Exercise.class, "lessonHandle="+lesson.getLessonHandle());
	    
	    holder.tv_lesson_name.setText(lesson.getChineseName());   
	    holder.ll_lesson_title.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
			     intent.setClass(context, ExerciseActivity.class);
			     
			     Bundle bundle = new Bundle();
			     bundle.putInt("lessonHandle", lesson.getLessonHandle());
			     bundle.putString("module", lesson.getModule());
			     intent.putExtras(bundle);
				 context.startActivity(intent);	
			}
		});
	    holder.tv_domain_name.setText(domain.getChineseName()); 
	    String exerciseString="";
	    for(Exercise exercise:exerciseList){
	    	exerciseString+=exercise.getExerciseNumber()+"."+exercise.getChineseName()+"\n";
	    }
	    holder.tv_exercises.setText(exerciseString);
	    

//	   // 设置隔行颜色
//	   if (position % 2 != 0) {
//	    convertView.setBackgroundResource(R.drawable.listview_color_1);
//	   } else {
//	    convertView.setBackgroundResource(R.drawable.listview_color_2);
//	   }
	   return convertView;
	  }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lessonList.size();
	}
	public List<Lesson> getLessonList() {
		return lessonList;
	}
	public void setLessonList(List<Lesson> lessonList) {
		this.lessonList = lessonList;
	}
	
	
	}
class Holder{
		public View ll_lesson_title;
		public ImageView icon_big_iv;
		public TextView tv_lesson_name;
		public ImageView small_icon_iv;
		public TextView tv_domain_name;
		public TextView tv_level;
		public TextView tv_exercises;
		public TextView tv_text3;
		public ImageView tv_icon3;
		public TextView tv_description;
		
}
