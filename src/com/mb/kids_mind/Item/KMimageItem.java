package com.mb.kids_mind.Item;

import java.io.Serializable;

public class KMimageItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1029920452260846907L;

	String sort;
	int imgres;
	int position;
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	boolean flag;
	public int getImgres() {
		return imgres;
	}
	public void setImgres(int imgres) {
		this.imgres = imgres;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	@Override
	public String toString() {
		return "KMimageItem [sort=" + sort + ", imgres=" + imgres + ", flag="
				+ flag + "]";
	}

	
}
