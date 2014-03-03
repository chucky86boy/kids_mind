package com.mb.kids_mind.Item;

import java.io.Serializable;

public class CommentList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1445534209516641976L;

	private int user_id;
	private String comment_text;
	private String comment_date;

	public String getComment_date() {
		return comment_date;
	}

	public void setComment_date(String comment_date) {
		this.comment_date = comment_date;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getComment_text() {
		return comment_text;
	}

	public void setComment_text(String comment_text) {
		this.comment_text = comment_text;
	}

}
