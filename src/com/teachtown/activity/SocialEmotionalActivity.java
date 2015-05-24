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

public class SocialEmotionalActivity extends FinalActivity {
	
	private FinalDb dataBase;
	private List<Lesson> lessonList;
	private List<Domain> domainList;
	@ViewInject(id=R.id.iv_social_leisure,click = "imageViewclick") ImageView iv_social_leisure;
	@ViewInject(id=R.id.iv_social_awareness,click = "imageViewclick") ImageView iv_social_awareness;
	@ViewInject(id=R.id.iv_social_relations,click = "imageViewclick") ImageView iv_social_relations;
	@ViewInject(id=R.id.iv_social_attention,click = "imageViewclick") ImageView iv_social_attention;
	@ViewInject(id=R.id.iv_social_emotions,click = "imageViewclick") ImageView iv_social_emotion;
	
	public SocialEmotionalActivity() {
		// TODO Auto-generated constructor stub
	}

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.social_emotional_activity);
		
		
	}
	public void imageViewclick(View view){
		Intent intent = new Intent();
		intent.setClass(SocialEmotionalActivity.this, LessonActivity.class);
		Bundle bundle = new Bundle();
		switch(view.getId()){
		case R.id.iv_social_leisure:
		     bundle.putInt("subDomainId", 13308);
			break;
		case R.id.iv_social_awareness:
			bundle.putInt("subDomainId", 13296);
			break;
		case R.id.iv_social_relations:
			bundle.putInt("subDomainId", 13314);
			break;
		case R.id.iv_social_attention:
			bundle.putInt("subDomainId", 13294);
			break;
		case R.id.iv_social_emotions:
			bundle.putInt("subDomainId", 13303);
			break;
		}
		intent.putExtras(bundle);
		SocialEmotionalActivity.this.startActivity(intent);	
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
		View view = findViewById(R.id.social_emotional_background);
		view.setBackgroundResource(0);
		System.gc();
	}

}
