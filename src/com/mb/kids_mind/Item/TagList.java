package com.mb.kids_mind.Item;

import java.io.Serializable;
import java.util.ArrayList;

public class TagList implements Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = -3720520053995681701L;
String tag_id;
String tag_name;
String question_id;
ArrayList<DetailListItem> dlist=new ArrayList<DetailListItem>();
public String getTag_id() {
	return tag_id;
}
public void setTag_id(String tag_id) {
	this.tag_id = tag_id;
}
public String getTag_name() {
	return tag_name;
}
public void setTag_name(String tag_name) {
	this.tag_name = tag_name;
}
public String getQuestion_id() {
	return question_id;
}
public void setQuestion_id(String question_id) {
	this.question_id = question_id;
}
public ArrayList<DetailListItem> getDlist() {
	return dlist;
}
public void setDlist(ArrayList<DetailListItem> dlist) {
	this.dlist = dlist;
}


}
