package com.teachtown.model;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

@Table(name="Domain")
public class Domain {
	@Id(column="domainId")
	private int domainId;
	private int domainType;
	private int curriculumId;
	private String name;
	private String description;
	private int iconAssetId;
	private int iconHighlightAssetId;
	public int getDomainId() {
		return domainId;
	}
	public void setDomainId(int domainId) {
		this.domainId = domainId;
	}
	public int getDomainType() {
		return domainType;
	}
	public void setDomainType(int domainType) {
		this.domainType = domainType;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getIconAssetId() {
		return iconAssetId;
	}
	public void setIconAssetId(int iconAssetId) {
		this.iconAssetId = iconAssetId;
	}
	public int getIconHighlightAssetId() {
		return iconHighlightAssetId;
	}
	public void setIconHighlightAssetId(int iconHighlightAssetId) {
		this.iconHighlightAssetId = iconHighlightAssetId;
	}


}
