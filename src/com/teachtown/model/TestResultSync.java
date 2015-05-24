package com.teachtown.model;

import java.util.Date;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;
@Table(name="TestResult_Sync")
public class TestResultSync {
	@Id(column="sessionGuid")
	private String sessionGuid;
	public double getPromptCorrectCount() {
		return promptCorrectCount;
	}
	public void setPromptCorrectCount(double promptCorrectCount) {
		this.promptCorrectCount = promptCorrectCount;
	}
	public double getPercentPrompted() {
		return percentPrompted;
	}
	public void setPercentPrompted(double percentPrompted) {
		this.percentPrompted = percentPrompted;
	}
	public double getPromptTestCount() {
		return promptTestCount;
	}
	public void setPromptTestCount(double promptTestCount2) {
		this.promptTestCount = promptTestCount2;
	}
	private String dateTaken;
	
	private int testResultGuid;
	private int lessonHandle;
	private int trialId;
	private String exerciseName;
	private String lessonName;
	private double promptCorrectCount;
	private double percentCorrect;
	private double percentPrompted;
	private int percentTimeout;
	private int finalPromptLevel;
	private double promptTestCount;
	private double testCount;
	private double correctCount;
	private int studentId;
	public String getSessionGuid() {
		return sessionGuid;
	}
	public void setSessionGuid(String sessionGuid) {
		this.sessionGuid = sessionGuid;
	}
	public String getDateTaken() {
		return dateTaken;
	}
	public void setDateTaken(String dateTaken) {
		this.dateTaken = dateTaken;
	}
	public int getTestResultGuid() {
		return testResultGuid;
	}
	public void setTestResultGuid(int testResultGuid) {
		this.testResultGuid = testResultGuid;
	}
	public int getLessonHandle() {
		return lessonHandle;
	}
	public void setLessonHandle(int lessonHandle) {
		this.lessonHandle = lessonHandle;
	}
	public int getTrialId() {
		return trialId;
	}
	public void setTrialId(int trialId) {
		this.trialId = trialId;
	}
	
	public double getPercentCorrect() {
		return percentCorrect;
	}
	public void setPercentCorrect(double correctPercent) {
		this.percentCorrect = correctPercent;
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
	
	public String getExerciseName() {
		return exerciseName;
	}
	public void setExerciseName(String exerciseName) {
		this.exerciseName = exerciseName;
	}
	public int getStudentId() {
		return studentId;
	}
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
	public double getTestCount() {
		return testCount;
	}
	public void setTestCount(double testCount2) {
		this.testCount = testCount2;
	}
	public double getCorrectCount() {
		return correctCount;
	}
	public void setCorrectCount(double correct) {
		this.correctCount = correct;
	}
	public String getLessonName() {
		return lessonName;
	}
	public void setLessonName(String lessonName) {
		this.lessonName = lessonName;
	}
	
}
