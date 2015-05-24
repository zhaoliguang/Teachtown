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

public class MathsActivity extends FinalActivity {
	private FinalDb dataBase;
	private List<Lesson> lessonList;
	private List<Domain> domainList;
	@ViewInject(id=R.id.iv_maths_counting,click = "imageViewclick") ImageView iv_maths_counting;
	@ViewInject(id=R.id.iv_maths_geometry,click = "imageViewclick") ImageView iv_maths_geometry;
	@ViewInject(id=R.id.iv_maths_algebraic,click = "imageViewclick") ImageView iv_maths_algebraic;
	@ViewInject(id=R.id.iv_maths_measurements,click = "imageViewclick") ImageView iv_maths_measurements;
	
	public MathsActivity() {
		// TODO Auto-generated constructor stub
	}

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maths_activity);
		
		
	}
	public void imageViewclick(View view){
		Intent intent = new Intent();
		intent.setClass(MathsActivity.this, LessonActivity.class);
		Bundle bundle = new Bundle();
		switch(view.getId()){
		case R.id.iv_maths_counting:
		     bundle.putInt("subDomainId", 13362);
			break;
		case R.id.iv_maths_geometry:
			bundle.putInt("subDomainId", 13376);
			break;
		case R.id.iv_maths_algebraic:
			bundle.putInt("subDomainId", 13370);
			break;
		case R.id.iv_maths_measurements:
			bundle.putInt("subDomainId", 13380);
			break;
		}
		intent.putExtras(bundle);
		MathsActivity.this.startActivity(intent);	
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
		View view = findViewById(R.id.maths_backound);
		view.setBackgroundResource(0);
		System.gc();
	}

}
