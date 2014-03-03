package com.mb.kids_mind.Item;

import java.io.Serializable;

public class SimilarItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7676789635640642918L;
	public String question_id;
	public String date;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getQuestion_id() {
		return question_id;
	}
	public void setQuestion_id(String question_id) {
		this.question_id = question_id;
	}
	public String advice_image;
	public String detail_id;
	public int advice_id;
	public int advice_user_id;
	public int getAdvice_user_id() {
		return advice_user_id;
	}
	public void setAdvice_user_id(int advice_user_id) {
		this.advice_user_id = advice_user_id;
	}
	public int user_id;
	public String comment_text;
	public String getAdvice_image() {
		return advice_image;
	}
	public void setAdvice_image(String advice_image) {
		this.advice_image = advice_image;
	}
	public String getDetail_id() {
		return detail_id;
	}
	public void setDetail_id(String detail_id) {
		this.detail_id = detail_id;
	}
	public int getAdvice_id() {
		return advice_id;
	}
	public void setAdvice_id(int advice_id) {
		this.advice_id = advice_id;
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
	@Override
	public String toString() {
		return "SimilarItem [advice_image=" + advice_image + ", detail_id="
				+ detail_id + ", advice_id=" + advice_id + ", user_id="
				+ user_id + ", comment_text=" + comment_text + "]";
	}
	
}
