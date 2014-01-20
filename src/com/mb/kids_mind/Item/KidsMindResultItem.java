package com.mb.kids_mind.Item;

import java.io.Serializable;

public class KidsMindResultItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1133518448901810924L;

	String title;
	boolean flag;

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
