package com.mb.kids_mind.Item;

import java.io.Serializable;

public class SimilarItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7676789635640642918L;

	
	String image_path;
	String image_title;
	String advice_contents;
	String user_age;
	String detail_id;
	int res;
	public int getRes() {
		return res;
	}
	public void setRes(int res) {
		this.res = res;
	}
	public String getImage_path() {
		return image_path;
	}
	public void setImage_path(String image_path) {
		this.image_path = image_path;
	}
	public String getImage_title() {
		return image_title;
	}
	public void setImage_title(String image_title) {
		this.image_title = image_title;
	}
	public String getAdvice_contents() {
		return advice_contents;
	}
	public void setAdvice_contents(String advice_contents) {
		this.advice_contents = advice_contents;
	}
	public String getUser_age() {
		return user_age;
	}
	@Override
	public String toString() {
		return "SimilarItem [image_path=" + image_path + ", image_title="
				+ image_title + ", advice_contents=" + advice_contents
				+ ", user_age=" + user_age + ", detail_id=" + detail_id + "]";
	}
	public void setUser_age(String user_age) {
		this.user_age = user_age;
	}
	public String getDetail_id() {
		return detail_id;
	}
	public void setDetail_id(String detail_id) {
		this.detail_id = detail_id;
	};
}
