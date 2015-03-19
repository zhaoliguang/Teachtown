package com.teachtown.activity;

import java.util.List;

import com.hfut.teachtown.R;
import com.teachtown.adapter.CurriculumListAdapter;
import com.teachtown.model.Lesson;
import com.teachtown.utils.DatabaseUtil;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.view.ViewInject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class CurriculumActivity extends FinalActivity {
	
	private FinalDb dataBase;
	private CurriculumListAdapter curriculumlistAdapter;
	private List<Lesson> lessonList;
	@ViewInject(id=R.id.lv_curriculum) ListView lv_curriculum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.computer_curriculum);
		dataBase =  DatabaseUtil.getDatabase(this);
		lessonList = dataBase.findAllByWhere(Lesson.class, "module='Matching'", "groupId");
		curriculumlistAdapter = new CurriculumListAdapter(CurriculumActivity.this,lessonList,dataBase);
		lv_curriculum.setAdapter(curriculumlistAdapter);
		
    }
}
