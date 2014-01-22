package com.mb.kids_mind.Item;

import java.io.Serializable;

public class KidsMindResultItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1133518448901810924L;

	String title;
	String tag_id;
	
	boolean flag;

	public String getTag_id() {
		return tag_id;
	}

	public void setTag_id(String tag_id) {
		this.tag_id = tag_id;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
