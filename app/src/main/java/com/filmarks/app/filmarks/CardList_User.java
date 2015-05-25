package com.filmarks.app.filmarks;

import android.graphics.Bitmap;

/**
 * Created by user on 15/04/24.
 */
public class CardList_User {

	private final Bitmap bitmap;
	private String title, comment, watched, url;
	private int day, point, comCount, favCount;

	public CardList_User(Bitmap bitmap, String account, int day, String title, int point, String comment, int comCount, int favCount, String watched, String url) {
		this.bitmap = bitmap;
		this.title = title;
		this.day = day;
		this.point = point;
		this.comment = comment;
		this.comCount = comCount;
		this.favCount = favCount;
		this.watched = watched;
		this.url = url;
	}

	public Bitmap getBitmap() { return bitmap; }
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	public String getComment() { return comment; }
	public void setComment(String comment) { this.comment = comment; }
	public String getWatched() { return watched; }
	public void setWatched(String watched) { this.watched = watched; }
	public int getDay() { return day; }
	public void setDay(int day) { this.day = day; }
	public int getPoint() { return point; }
	public void setPoint(int point) { this.point = point; }
	public int getComCount() { return comCount; }
	public void setComCount(int comCount) { this.comCount = comCount; }
	public int getFavCount() { return favCount; }
	public void setFavCount(int favCount) { this.favCount = favCount; }
	public String getUrl() { return url; }
	public void setUrl(String url) { this.url = url; }
}
