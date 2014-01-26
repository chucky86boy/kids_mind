package com.mb.kids_mind.listener;

import android.view.View;

/**
 * Interface for pen selection
 * 
 * @author Mike
 *
 */
public interface PageChagedListener {
 
	/**
	 * Called when a pen is selected
	 * 
	 * @param pen
	 */
	public void onPageChange(int position);
 
}
