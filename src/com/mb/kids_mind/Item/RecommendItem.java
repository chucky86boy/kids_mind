package com.mb.kids_mind.Item;

import java.io.Serializable;

public class RecommendItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8358744798115819971L;
	
	int res;
	String title;
	String contents;
	@Override
	public String toString() {
		return "RecommendItem [res=" + res + ", title=" + title + ", contents="
				+ contents + "]";
	}
	public int getRes() {
		return res;
	}
	public void setRes(int res) {
		this.res = res;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}

}
