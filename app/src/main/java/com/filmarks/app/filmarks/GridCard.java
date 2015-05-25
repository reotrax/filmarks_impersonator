package com.filmarks.app.filmarks;

import android.graphics.Bitmap;

/**
 * Created by user on 15/05/23.
 */
public class GridCard {
	private String title;
	private Bitmap bitmap;
	private int commentCount, likeCount, day;
	private float rating;

	public GridCard(String title, Bitmap bitmap, int commentCount, int likeCount, float rating, int day) {
		this.day = day;
		this.bitmap = bitmap;
		this.title = title;
		this.commentCount = commentCount;
		this.likeCount = likeCount;
		this.rating = rating;
	}

	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public int getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	public int getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}
	public float getRating() {
		return rating;
	}
	public void setRating(float rating) {
		this.rating = rating;
	}
}
