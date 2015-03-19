package com.teachtown.model;

import java.util.Date;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;
@Table(name="TestResult_Sync")
public class TestResultSync {
	@Id(column="id")
	private int id;
	private String sessionGuid;
	private Date dateTaken;
	private String testResultGuid;
	private int lessonHandle;
	private int exerciseNumber;
	private int durationSeconds;
	private int percentCorrect;
	private int percentPrompted;
	private int percentTimeout;
	private int finalPromptLevel;
	private int pauseDuration;
	public String getSessionGuid() {
		return sessionGuid;
	}
	public void setSessionGuid(String sessionGuid) {
		this.sessionGuid = sessionGuid;
	}
	public Date getDateTaken() {
		return dateTaken;
	}
	public void setDateTaken(Date dateTaken) {
		this.dateTaken = dateTaken;
	}
	public String getTestResultGuid() {
		return testResultGuid;
	}
	public void setTestResultGuid(String testResultGuid) {
		this.testResultGuid = testResultGuid;
	}
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
	public int getDurationSeconds() {
		return durationSeconds;
	}
	public void setDurationSeconds(int durationSeconds) {
		this.durationSeconds = durationSeconds;
	}
	public int getPercentCorrect() {
		return percentCorrect;
	}
	public void setPercentCorrect(int percentCorrect) {
		this.percentCorrect = percentCorrect;
	}
	public int getPercentPrompted() {
		return percentPrompted;
	}
	public void setPercentPrompted(int percentPrompted) {
		this.percentPrompted = percentPrompted;
	}
	public int getPercentTimeout() {
		return percentTimeout;
	}
	public void setPercentTimeout(int percentTimeout) {
		this.percentTimeout = percentTimeout;
	}
	public int getFinalPromptLevel() {
		return finalPromptLevel;
	}
	public void setFinalPromptLevel(int finalPromptLevel) {
		this.finalPromptLevel = finalPromptLevel;
	}
	public int getPauseDuration() {
		return pauseDuration;
	}
	public void setPauseDuration(int pauseDuration) {
		this.pauseDuration = pauseDuration;
	}
	
}
