package com.teachtown.model;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

@Table(name="Exercise")
public class Exercise {
	@Id(column="id")
	private int id;
	private int lessonHandle;
	private int exerciseNumber;
	private String name;
	private String chineseName;
	public int getLessonHandle() {
		return lessonHandle;
	}
	public void setLessonHandle(int lessonHandle) {
		this.lessonHandle = lessonHandle;
	}
	public int getExerciseNumber() {
		return exerciseNumber;
	}
	public void setExerciseNumber(int exerciseNumber) {
		this.exerciseNumber = exerciseNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getChineseName() {
		return chineseName;
	}
	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	

}
