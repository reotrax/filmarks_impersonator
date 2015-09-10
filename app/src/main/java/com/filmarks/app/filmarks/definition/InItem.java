package com.filmarks.app.filmarks.definition;

import android.graphics.Bitmap;

/**
 * Created by user on 15/06/02.
 */
public class InItem {
	private String title;
	private String[] item;
	private Bitmap[] bitmaps;
	private String[] sub;

	public InItem(Bitmap[] bitmaps, String title, String[] item, String[] sub) {
		this.bitmaps = bitmaps;
		this.title = title;
		this.item = item;
		this.sub = sub;
	}

	public Bitmap[] getBitmaps() {
		return bitmaps;
	}

	public InItem setBitmaps(Bitmap[] bitmaps) {
		this.bitmaps = bitmaps;
		return this;
	}

	public String[] getSub() {
		return sub;
	}

	public InItem setSub(String[] sub) {
		this.sub = sub;
		return this;
	}

	public String[] getItem() {
		return item;
	}

	public InItem setItem(String[] item) {
		this.item = item;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public InItem setTitle(String title) {
		this.title = title;
		return this;
	}

}
