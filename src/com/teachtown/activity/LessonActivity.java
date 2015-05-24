package com.teachtown.activity;

import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.core.AsyncTask;

import com.hfut.teachtown.R;
import com.teachtown.adapter.CurriculumListAdapter;
import com.teachtown.adapter.LessonGridViewAdapter;
import com.teachtown.model.Domain;
import com.teachtown.model.Lesson;
import com.teachtown.model.Trial;
import com.teachtown.utils.DatabaseUtil;
import com.teachtown.utils.UnityUtils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class LessonActivity extends FinalActivity {
	
	private FinalDb dataBase;
	private LessonGridViewAdapter lessonGridViewAdapter;
	private List<Lesson> lessonList;
	private List<Domain> domainList;
	private int subDomainId;

	
	@ViewInject(id=R.id.gv_lesson) GridView gv_lesson;
	@ViewInject(id=R.id.et_search) EditText et_search;
	@ViewInject(id=R.id.iv_return,click = "imageViewClick") ImageView iv_return;
	@ViewInject(id=R.id.im_search_button,click = "imageViewClick") ImageView im_search_button;
	
	
	public LessonActivity() {
		// TODO Auto-generated constructor stub
	}
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i("LessonActivity", "onCreate");
		setContentView(R.layout.lesson_list_activity);
		UnityUtils.showProgress(LessonActivity.this,"正在加载课程信息...");
		Bundle bundle = this.getIntent().getExtras();
		subDomainId = bundle.getInt("subDomainId");
		dataBase =  DatabaseUtil.getDatabase(this);
		System.out.println("subDomainId:"+subDomainId);
		lessonList = dataBase.findAllByWhere(Lesson.class, "subDomainId="+subDomainId+ " and (module='Matching' or module='ReceptiveLabel')","groupId");
		
		lessonGridViewAdapter = new LessonGridViewAdapter(LessonActivity.this,lessonList,dataBase);
		gv_lesson.setAdapter(lessonGridViewAdapter);
		UnityUtils.stopProgress();
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("LessonActivity", "onResume");

		lessonGridViewAdapter.notifyDataSetChanged();
	}
	public void imageViewClick(View view){
		switch(view.getId()){
		
		case R.id.iv_return:
			finish();
			break;
		case R.id.im_search_button:
			String search = et_search.getText().toString().trim(); 
			if(search.equals("")){
				Toast.makeText(LessonActivity.this, "请输入查找课程名", Toast.LENGTH_SHORT).show();
				return;
			}
			else{
				
				new AsyncDatabaseTask().execute(search);
				
			}

			break;
		}			
		 
	}

	
	class AsyncDatabaseTask extends AsyncTask<String, Integer, List<Lesson>>{

		@Override
		protected List doInBackground(String... lessonName) {
			// TODO Auto-generated method stub
			return dataBase.findAllByWhere(Lesson.class, "subDomainId="+subDomainId+ " and (module='Matching' or module='ReceptiveLabel')"+" and chineseName like '%"+lessonName[0]+"%'");
		}
		
		@Override
		protected void onPostExecute(List<Lesson> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result.size()==0){
				Toast.makeText(LessonActivity.this, "没有要查找的课程,请重新输入！", Toast.LENGTH_SHORT);
				
			}
				
			lessonGridViewAdapter = new LessonGridViewAdapter(LessonActivity.this,result,dataBase);
			gv_lesson.setAdapter(lessonGridViewAdapter);
			lessonGridViewAdapter.notifyDataSetChanged();
		}
	} 
	
}
