package com.mb.kids_mind.Item;

import java.io.Serializable;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class AlbumItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7861555049577425583L;
	public String image_path;
	public String title;
	public String date;
	public String questioin;
	public String newmessage;
	public String getImage_path() {
		return image_path;
	}
	public void setImage_path(String image_path) {
		this.image_path = image_path;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getQuestioin() {
		return questioin;
	}
	public void setQuestioin(String questioin) {
		this.questioin = questioin;
	}
	public String getNewmessage() {
		return newmessage;
	}
	public void setNewmessage(String newmessage) {
		this.newmessage = newmessage;
	}
	@Override
	public String toString() {
		return "AlbumItem [image_path=" + image_path + ", title=" + title
				+ ", date=" + date + ", questioin=" + questioin
				+ ", newmessage=" + newmessage + "]";
	}
	
	
}