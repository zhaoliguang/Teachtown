package com.teachtown.model;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

@Table(name="Asset")
public class Asset {
	@Id(column="assetId")
	private int assetId;
	private String extension;
	private int image_Width;
	private int image_Height;
	private String caption;
	private int diskSize;
	public int getAssetId() {
		return assetId;
	}
	public void setAssetId(int assetId) {
		this.assetId = assetId;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public int getImage_Width() {
		return image_Width;
	}
	public void setImage_Width(int image_Width) {
		this.image_Width = image_Width;
	}
	public int getImage_Height() {
		return image_Height;
	}
	public void setImage_Height(int image_Height) {
		this.image_Height = image_Height;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public int getDiskSize() {
		return diskSize;
	}
	public void setDiskSize(int diskSize) {
		this.diskSize = diskSize;
	}
}
