package com.mb.kids_mind.Item;

import java.io.Serializable;
import java.util.ArrayList;

public class SItem2 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -508122559968784855L;

	String result;
	
	

	ArrayList<SimilarItem> data = new ArrayList<SimilarItem>();



	public String getResult() {
		return result;
	}



	public void setResult(String result) {
		this.result = result;
	}



	public ArrayList<SimilarItem> getData() {
		return data;
	}



	public void setData(ArrayList<SimilarItem> data) {
		this.data = data;
	}



	@Override
	public String toString() {
		return "SItem2 [result=" + result + ", data=" + data + "]";
	}
	
	
}
