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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class AdaptiveSkillsActivity extends FinalActivity {
	
	private FinalDb dataBase;
	private List<Lesson> lessonList;
	private List<Domain> domainList;
	

	
	@ViewInject(id=R.id.iv_adaptive_community_living,click = "imageViewclick") ImageView iv_adaptive_community_living;
	@ViewInject(id=R.id.iv_adaptive_prevocational,click = "imageViewclick") ImageView iv_adaptive_prevocational;
	@ViewInject(id=R.id.iv_adaptive_liveing,click = "imageViewclick") ImageView iv_adaptive_liveing;
	@ViewInject(id=R.id.iv_adaptive_health,click = "imageViewclick") ImageView iv_adaptive_health;
	@ViewInject(id=R.id.iv_adaptive_self_awareness,click = "imageViewclick") ImageView iv_adaptive_self_awareness;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adaptive_skill_activity);
		
		dataBase =  DatabaseUtil.getDatabase(this);
		lessonList = dataBase.findAllByWhere(Lesson.class, "module='Matching' or module='ReceptiveLabel' and domainId='3'", "groupId");
	
		domainList = dataBase.findAllByWhere(Domain.class,"");
	
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
		View view = findViewById(R.id.adaptive_skill_background);
		view.setBackgroundResource(0);
		System.gc();
	}
	public void imageViewclick(View view){
		Intent intent = new Intent();
		intent.setClass(AdaptiveSkillsActivity.this, LessonActivity.class);
		Bundle bundle = new Bundle();
		switch(view.getId()){
		case R.id.iv_adaptive_community_living:
		     bundle.putInt("subDomainId", 13221);
			break;
		case R.id.iv_adaptive_prevocational:
			bundle.putInt("subDomainId", 13225);
			break;
		case R.id.iv_adaptive_liveing:
			bundle.putInt("subDomainId", 13212);
			break;
		case R.id.iv_adaptive_health:
			bundle.putInt("subDomainId", 13230);
			break;
		case R.id.iv_adaptive_self_awareness:
			bundle.putInt("subDomainId", 13217);
			break;
		
		}
		intent.putExtras(bundle);
		AdaptiveSkillsActivity.this.startActivity(intent);	
	}
	
}
