package com.teachtown.activity;


import com.hfut.teachtown.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

public class MainMenuActivity extends FinalActivity  {
	
	@ViewInject(id = R.id.im_adaptive_skills, click = "imageViewlick") ImageView im_adaptive_skills;
	@ViewInject(id = R.id.im_language_arts, click = "imageViewlick") ImageView im_language_arts; 
	@ViewInject(id = R.id.im_cognitive_skills, click = "imageViewlick") ImageView im_cognitive_skills; 
	@ViewInject(id = R.id.im_language_development, click = "imageViewlick") ImageView im_language_development; 
	@ViewInject(id = R.id.im_maths, click = "imageViewlick") ImageView im_maths; 
	@ViewInject(id = R.id.im_social_emotional, click = "imageViewlick") ImageView im_social_emotional; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
	}
	public void imageViewlick(View view){
		Intent intent;
		switch(view.getId()){
		
		case R.id.im_adaptive_skills:
			intent=new Intent(MainMenuActivity.this,AdaptiveSkillsActivity.class);
			startActivity(intent);
			break;
		case R.id.im_language_arts:
			intent=new Intent(MainMenuActivity.this,LanguageArtsActivity.class);
			startActivity(intent);
			break;
		case R.id.im_cognitive_skills:
			intent=new Intent(MainMenuActivity.this,CognitiveSkillActivity.class);
			startActivity(intent);
			break;
		case R.id.im_maths:
			intent=new Intent(MainMenuActivity.this,MathsActivity.class);
			startActivity(intent);
			break;
		case R.id.im_language_development:
			intent=new Intent(MainMenuActivity.this,LanguageDevelopmentActivity.class);
			startActivity(intent);
			break;
		case R.id.im_social_emotional:
			intent=new Intent(MainMenuActivity.this,SocialEmotionalActivity.class);
			startActivity(intent);
			break;
			

		}
		
	}
}
