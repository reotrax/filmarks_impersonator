package com.filmarks.app.filmarks;

import android.graphics.Bitmap;

/**
 * Created by user on 15/04/26.
 */
public class Reviews {

	private String name,title,comment;
	private int day;
	private float rating;
	private Bitmap pict;

	public Reviews(String name, int day, String title, float rating, String comment, Bitmap pict) {
		this.name = name;
		this.day = day;
		this.title = title;
		this.rating = rating;
		this.comment = comment;
		this.pict = pict;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public float getRating() {
		return rating;
	}
	public void setRating(float rating) {
		this.rating = rating;
	}
	public Bitmap getPict() {
		return pict;
	}
	public void setPict(Bitmap pict) {
		this.pict = pict;
	}
}
