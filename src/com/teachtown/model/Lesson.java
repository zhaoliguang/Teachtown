package com.teachtown.model;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

@Table(name="Lesson")
public class Lesson {
	@Id(column="lessonHandle")
	private int lessonHandle;
	private int curriculumId;
	private String name;
	private String chineseName;
	private int masterDomainId;
	private int domainId;
	private int subDomainId;
	private int subSubDomainId;
	private int rank;
	private String module;
	private int iconAssetId;
	private int groupId;
	private int backgroundAssetId;
	private int frameAssetId;
	private int frameHighlightAssetId;
	private String description;
	private String trialName;
	private String domainName;
	private String correctPercent;
	public String getCorrectPercent() {
		return correctPercent;
	}
	public void setCorrectPercent(String correctPercent) {
		this.correctPercent = correctPercent;
	}
	public int getLessonHandle() {
		return lessonHandle;
	}
	public void setLessonHandle(int lessonHandle) {
		this.lessonHandle = lessonHandle;
	}
	public int getCurriculumId() {
		return curriculumId;
	}
	public void setCurriculumId(int curriculumId) {
		this.curriculumId = curriculumId;
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
	public int getMasterDomainId() {
		return masterDomainId;
	}
	public void setMasterDomainId(int masterDomainId) {
		this.masterDomainId = masterDomainId;
	}
	public int getDomainId() {
		return domainId;
	}
	public void setDomainId(int domainId) {
		this.domainId = domainId;
	}
	public int getSubDomainId() {
		return subDomainId;
	}
	public void setSubDomainId(int subDomainId) {
		this.subDomainId = subDomainId;
	}
	public int getSubSubDomainId() {
		return subSubDomainId;
	}
	public void setSubSubDomainId(int subSubDomainId) {
		this.subSubDomainId = subSubDomainId;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public int getIconAssetId() {
		return iconAssetId;
	}
	public void setIconAssetId(int iconAssetId) {
		this.iconAssetId = iconAssetId;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public int getBackgroundAssetId() {
		return backgroundAssetId;
	}
	public void setBackgroundAssetId(int backgroundAssetId) {
		this.backgroundAssetId = backgroundAssetId;
	}
	public int getFrameAssetId() {
		return frameAssetId;
	}
	public void setFrameAssetId(int frameAssetId) {
		this.frameAssetId = frameAssetId;
	}
	public int getFrameHighlightAssetId() {
		return frameHighlightAssetId;
	}
	public void setFrameHighlightAssetId(int frameHighlightAssetId) {
		this.frameHighlightAssetId = frameHighlightAssetId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTrialName() {
		return trialName;
	}
	public void setTrialName(String trialName) {
		this.trialName = trialName;
	}
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	
	
	

}
