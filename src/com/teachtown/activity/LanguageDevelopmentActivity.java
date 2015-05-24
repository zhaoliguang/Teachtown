package com.teachtown.activity;

import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.view.ViewInject;

import com.hfut.teachtown.R;
import com.teachtown.model.Domain;
import com.teachtown.model.Lesson;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class LanguageDevelopmentActivity extends FinalActivity {
	private FinalDb dataBase;
	private List<Lesson> lessonList;
	private List<Domain> domainList;
	

	
	@ViewInject(id=R.id.iv_language_dev_label,click = "imageViewclick") ImageView iv_languagearts_conventions;
	@ViewInject(id=R.id.iv_language_dev_speech,click = "imageViewclick") ImageView iv_languagearts_awareness;
	@ViewInject(id=R.id.iv_language_dev_auditory_dis,click = "imageViewclick") ImageView iv_language_dev_auditory_dis;
	@ViewInject(id=R.id.iv_language_dev_auditory_pro,click = "imageViewclick") ImageView iv_languagearts_reading;
	@ViewInject(id=R.id.iv_language_dev_visual,click = "imageViewclick") ImageView iv_language_dev_visual;
	@ViewInject(id=R.id.iv_language_dev_calendar,click = "imageViewclick") ImageView iv_language_dev_calendar;
	
	public LanguageDevelopmentActivity() {
		// TODO Auto-generated constructor stub
	}

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.language_development_activity);
		
		
	}
	public void imageViewclick(View view){
		Intent intent = new Intent();
		intent.setClass(LanguageDevelopmentActivity.this, LessonActivity.class);
		Bundle bundle = new Bundle();
		switch(view.getId()){
		case R.id.iv_language_dev_label:
		     bundle.putInt("subDomainId", 13259);
			break;
		case R.id.iv_language_dev_speech:
			bundle.putInt("subDomainId", 13272);
			break;
		case R.id.iv_language_dev_auditory_dis:
			bundle.putInt("subDomainId", 13286);
			break;
		case R.id.iv_language_dev_auditory_pro:
			bundle.putInt("subDomainId", 13278);
			break;
		case R.id.iv_language_dev_visual:
			bundle.putInt("subDomainId", 13290);
			break;
		case R.id.iv_language_dev_calendar:
			bundle.putInt("subDomainId", 13284);
			break;
		}
		intent.putExtras(bundle);
		LanguageDevelopmentActivity.this.startActivity(intent);	
	}
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		View view = findViewById(R.id.language_development_background);
		view.setBackgroundResource(0);
		System.gc();
	}

}
