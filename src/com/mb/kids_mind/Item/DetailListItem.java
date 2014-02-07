package com.mb.kids_mind.Item;

import java.io.Serializable;

public class DetailListItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7398932154402038704L;

	String detail_id;
	String detail_tilte;
	String detail_content;
	String detail_image;
	String tag_id;
	int position;
	boolean flag;
	
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public String getDetail_id() {
		return detail_id;
	}
	public void setDetail_id(String detail_id) {
		this.detail_id = detail_id;
	}
	public String getDetail_tilte() {
		return detail_tilte;
	}
	public void setDetail_tilte(String detail_tilte) {
		this.detail_tilte = detail_tilte;
	}
	public String getDetail_content() {
		return detail_content;
	}
	public void setDetail_content(String detail_content) {
		this.detail_content = detail_content;
	}
	public String getDetail_image() {
		return detail_image;
	}
	public void setDetail_image(String detail_image) {
		this.detail_image = detail_image;
	}
	public String getTag_id() {
		return tag_id;
	}
	public void setTag_id(String tag_id) {
		this.tag_id = tag_id;
	}
}
