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

public class CognitiveSkillActivity extends FinalActivity {
	
	private FinalDb dataBase;
	private List<Lesson> lessonList;
	private List<Domain> domainList;
	

	
	@ViewInject(id=R.id.iv_congnitive_fiction,click = "imageViewclick") ImageView iv_congnitive_fiction;
	@ViewInject(id=R.id.iv_congnitive_feature_class,click = "imageViewclick") ImageView iv_congnitive_feature_class;
	@ViewInject(id=R.id.iv_congnitive_sorting,click = "imageViewclick") ImageView iv_congnitive_sorting;
	@ViewInject(id=R.id.iv_congnitive_discriminations,click = "imageViewclick") ImageView iv_congnitive_discriminations;
	@ViewInject(id=R.id.iv_congnitive_shapes,click = "imageViewclick") ImageView iv_congnitive_shapes;
	public CognitiveSkillActivity() {
		// TODO Auto-generated constructor stub
	}

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.congnitive_skills_activity);
	
		dataBase =  DatabaseUtil.getDatabase(this);
		lessonList = dataBase.findAllByWhere(Lesson.class, "module='Matching' or module='ReceptiveLabel' and domainId='2'", "groupId");
	
		domainList = dataBase.findAllByWhere(Domain.class,"");
		
		
	}
	public void imageViewclick(View view){
		Intent intent = new Intent();
		intent.setClass(CognitiveSkillActivity.this, LessonActivity.class);
		Bundle bundle = new Bundle();
		switch(view.getId()){
		case R.id.iv_congnitive_fiction:
		     bundle.putInt("subDomainId", 13242);
			break;
		case R.id.iv_congnitive_feature_class:
			bundle.putInt("subDomainId", 13256);
			break;
		case R.id.iv_congnitive_sorting:
			bundle.putInt("subDomainId", 13237);
			break;
		case R.id.iv_congnitive_discriminations:
			bundle.putInt("subDomainId", 13245);
			break;
		case R.id.iv_congnitive_shapes:
			bundle.putInt("subDomainId", 13233);
			break;
		
		}
		intent.putExtras(bundle);
		CognitiveSkillActivity.this.startActivity(intent);	
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
		View view = findViewById(R.id.congnitive_skill_background);
		view.setBackgroundResource(0);
		System.gc();
		
	}

}
