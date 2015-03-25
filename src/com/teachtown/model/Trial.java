package com.teachtown.model;

import android.os.Parcel;
import android.os.Parcelable;
import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

@Table(name="Trial")
public class Trial implements Parcelable{
	@Id(column="trialId")
	private int trialId;
	private String name;
	private String chineseName;
	private int lessonHandle;
	private int exerciseNumber;
	private int cueSoundDelaySeconds;
	private int numDistractors;
	private int rl_ShareDistractors;
	private int mc_ShareDistractors;
	private int mc_AssetCombination;
	private int ma_ExactMatches;
	private int ja_TargetPosition;
	private int ja_DistractorPositionBitMask;
	private int ol_BackgroundAssetId;
	private String ol_TargetRawData;
	private int ol_TrialType;
	private int postSoundAssetId;
	private String module;
	public int getTrialId() {
		return trialId;
	}
	public void setTrialId(int trialId) {
		this.trialId = trialId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public int getCueSoundDelaySeconds() {
		return cueSoundDelaySeconds;
	}
	public void setCueSoundDelaySeconds(int cueSoundDelaySeconds) {
		this.cueSoundDelaySeconds = cueSoundDelaySeconds;
	}
	public int getNumDistractors() {
		return numDistractors;
	}
	public void setNumDistractors(int numDistractors) {
		this.numDistractors = numDistractors;
	}
	public int getRl_ShareDistractors() {
		return rl_ShareDistractors;
	}
	public void setRl_ShareDistractors(int rl_ShareDistractors) {
		this.rl_ShareDistractors = rl_ShareDistractors;
	}
	public int getMc_ShareDistractors() {
		return mc_ShareDistractors;
	}
	public void setMc_ShareDistractors(int mc_ShareDistractors) {
		this.mc_ShareDistractors = mc_ShareDistractors;
	}
	public int getMc_AssetCombination() {
		return mc_AssetCombination;
	}
	public void setMc_AssetCombination(int mc_AssetCombination) {
		this.mc_AssetCombination = mc_AssetCombination;
	}
	public int getMa_ExactMatches() {
		return ma_ExactMatches;
	}
	public void setMa_ExactMatches(int ma_ExactMatches) {
		this.ma_ExactMatches = ma_ExactMatches;
	}
	public int getJa_TargetPosition() {
		return ja_TargetPosition;
	}
	public void setJa_TargetPosition(int ja_TargetPosition) {
		this.ja_TargetPosition = ja_TargetPosition;
	}
	public int getJa_DistractorPositionBitMask() {
		return ja_DistractorPositionBitMask;
	}
	public void setJa_DistractorPositionBitMask(int ja_DistractorPositionBitMask) {
		this.ja_DistractorPositionBitMask = ja_DistractorPositionBitMask;
	}
	public int getOl_BackgroundAssetId() {
		return ol_BackgroundAssetId;
	}
	public void setOl_BackgroundAssetId(int ol_BackgroundAssetId) {
		this.ol_BackgroundAssetId = ol_BackgroundAssetId;
	}
	public String getOl_TargetRawData() {
		return ol_TargetRawData;
	}
	public void setOl_TargetRawData(String ol_TargetRawData) {
		this.ol_TargetRawData = ol_TargetRawData;
	}
	public int getOl_TrialType() {
		return ol_TrialType;
	}
	public void setOl_TrialType(int ol_TrialType) {
		this.ol_TrialType = ol_TrialType;
	}
	public int getPostSoundAssetId() {
		return postSoundAssetId;
	}
	public void setPostSoundAssetId(int postSoundAssetId) {
		this.postSoundAssetId = postSoundAssetId;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getChineseName() {
		return chineseName;
	}
	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
