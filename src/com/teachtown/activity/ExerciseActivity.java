package com.teachtown.activity;



import java.util.List;

import com.hfut.teachtown.R;
import com.teachtown.adapter.ExerciseListAdapter;
import com.teachtown.model.Exercise;
import com.teachtown.model.Lesson;
import com.teachtown.model.TestResultSync;
import com.teachtown.utils.DatabaseUtil;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.view.ViewInject;

public class ExerciseActivity extends FinalActivity {
	
	private LayoutInflater inflater;
	private ExerciseListAdapter exerciseListAdapter;
	private List<Exercise> exerciseList;
	private List<TestResultSync> testResultList;
	private FinalDb database;
	private String module;
	private int lessonHandle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exercise);
		Bundle bundle = this.getIntent().getExtras();
		lessonHandle = bundle.getInt("lessonHandle");
		module = bundle.getString("module");
		database = DatabaseUtil.getDatabase(this);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View headerView = inflater.inflate(R.layout.exercise_list_item, null);
		View footerView = inflater.inflate(R.layout.exercise_list_item, null);
		
		exerciseList = database.findAllByWhere(Exercise.class, "lessonHandle="+lessonHandle, "exerciseNumber");
		testResultList = database.findAllByWhere(TestResultSync.class, "lessonHandle="+lessonHandle, "exerciseNumber");
		exerciseListAdapter = new ExerciseListAdapter(ExerciseActivity.this, lessonHandle,exerciseList,testResultList,database,module);
		
	}
	
}
