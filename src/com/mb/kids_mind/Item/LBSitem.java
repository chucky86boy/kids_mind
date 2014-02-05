package com.mb.kids_mind.Item;

import java.io.Serializable;

public class LBSitem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3632874982512783332L;
private String Image_path;
private String address;
private String number;
private String name;
private String contents;
public String getImage_path() {
	return Image_path;
}
public void setImage_path(String image_path) {
	Image_path = image_path;
}
public String getAddress() {
	return address;
}
public void setAddress(String address) {
	this.address = address;
}
public String getNumber() {
	return number;
}
public void setNumber(String number) {
	this.number = number;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getContents() {
	return contents;
}
public void setContents(String contents) {
	this.contents = contents;
}
@Override
public String toString() {
	return "LBSitem [Image_path=" + Image_path + ", address=" + address
			+ ", number=" + number + ", name=" + name + ", contents="
			+ contents + "]";
}

}
