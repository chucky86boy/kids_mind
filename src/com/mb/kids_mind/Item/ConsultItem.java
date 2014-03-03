package com.mb.kids_mind.Item;

import java.io.Serializable;

import android.widget.CheckBox;

public class ConsultItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1666033861022741993L;
	String name;
	String img_path;
	int res;
	String title;
	String address;
	String center;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImg_path() {
		return img_path;
	}
	public void setImg_path(String img_path) {
		this.img_path = img_path;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCenter() {
		return center;
	}
	public void setCenter(String center) {
		this.center = center;
	}
	@Override
	public String toString() {
		return "ConsultItem [name=" + name + ", img_path=" + img_path
				+ ", res=" + res + ", title=" + title + ", address=" + address
				+ ", center=" + center + "]";
	}
	
	

}
