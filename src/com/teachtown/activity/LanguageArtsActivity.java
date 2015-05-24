package com.teachtown.activity;

import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.view.ViewInject;

import com.hfut.teachtown.R;
import com.teachtown.model.Domain;
import com.teachtown.model.Lesson;
import com.teachtown.utils.DatabaseUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class LanguageArtsActivity extends FinalActivity {
	private FinalDb dataBase;
	private List<Lesson> lessonList;
	private List<Domain> domainList;
	

	
	@ViewInject(id=R.id.iv_languagearts_conventions,click = "imageViewclick") ImageView iv_languagearts_conventions;
	@ViewInject(id=R.id.iv_languagearts_awareness,click = "imageViewclick") ImageView iv_languagearts_awareness;
	@ViewInject(id=R.id.iv_languagearts_concepts,click = "imageViewclick") ImageView iv_languagearts_concepts;
	@ViewInject(id=R.id.iv_languagearts_reading,click = "imageViewclick") ImageView iv_languagearts_reading;
	
	public LanguageArtsActivity() {
		// TODO Auto-generated constructor stub
	}

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lanauage_arts_activity);
		dataBase =  DatabaseUtil.getDatabase(this);
		//lessonList = dataBase.findAllByWhere(Lesson.class, "module='Matching' or module='ReceptiveLabel' and domainId='1'", "groupId");
	
		//domainList = dataBase.findAllByWhere(Domain.class,"");
		
	}
	public void imageViewclick(View view){
		Intent intent = new Intent();
		intent.setClass(LanguageArtsActivity.this, LessonActivity.class);
		Bundle bundle = new Bundle();
		switch(view.getId()){
		case R.id.iv_languagearts_conventions:
		     bundle.putInt("subDomainId", 13395);
			break;
		case R.id.iv_languagearts_awareness:
			bundle.putInt("subDomainId", 13389);
			break;
		case R.id.iv_languagearts_concepts:
			bundle.putInt("subDomainId", 13384);
			break;
		case R.id.iv_languagearts_reading:
			bundle.putInt("subDomainId", 13399);
			break;
		}
		intent.putExtras(bundle);
		LanguageArtsActivity.this.startActivity(intent);	
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
		View view = findViewById(R.id.language_arts_background);
		view.setBackgroundResource(0);
		System.gc();
	}

}
