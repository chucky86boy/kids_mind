package com.mb.kids_mind.Item;

import java.io.Serializable;
import java.util.ArrayList;

public class AdviceItem2 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -508122559968784855L;

	String result;
	String advice_user_name;
	String user_name;
	String advice_date;
	
	@Override
	public String toString() {
		return "AdviceItem [result=" + result + ", advice_user_name="
				+ advice_user_name + ", user_name=" + user_name
				+ ", advice_date=" + advice_date + ", data=" + data + "]";
	}
	public String getAdvice_date() {
		return advice_date;
	}
	public void setAdvice_date(String advice_date) {
		this.advice_date = advice_date;
	}

	ArrayList<CommentList> data = new ArrayList<CommentList>();
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getAdvice_user_name() {
		return advice_user_name;
	}
	public void setAdvice_user_name(String advice_user_name) {
		this.advice_user_name = advice_user_name;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	
	public ArrayList<CommentList> getData() {
		return data;
	}
	
	public void setData(ArrayList<CommentList> data) {
		this.data = data;
	}
	
}
