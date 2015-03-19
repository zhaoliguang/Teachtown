package com.teachtown.activity;


import com.hfut.teachtown.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

public class MainMenuActivity extends FinalActivity  {
	
	  @ViewInject(id = R.id.curriculum_iv, click = "imageViewlick") ImageView curriculumImageView;  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_ui);
	}
	public void imageViewlick(View view){
		switch(view.getId()){
		
		case R.id.curriculum_iv:
			Intent intent=new Intent(MainMenuActivity.this,CurriculumActivity.class);
			startActivity(intent);
			break;
			
		
		}
		
	}
}
