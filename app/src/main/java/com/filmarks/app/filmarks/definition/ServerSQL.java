package com.filmarks.app.filmarks.definition;

/**
 * Created by user on 15/04/24.
 */
public class ServerSQL {

	private String title,comment, titleUrl,date, raiting,comCount,favCount,watched,want,userUrl,user,userNo,titleNo;

	// 計１３個
	public ServerSQL(String date, String raiting,
					 String comCount, String favCount, String watched, String want,
					 String titleUrl, String userUrl, String user, String userNo, String title, String titleNo, String comment) {

		this.date = date;
		this.raiting = raiting;
		this.comCount = comCount;
		this.favCount = favCount;
		this.watched = watched;
		this.want = want;
		this.titleUrl = titleUrl;
		this.userUrl = userUrl;
		this.user = user;
		this.userNo = userNo;
		this.title = title;
		this.titleNo = titleNo;
		this.comment = comment;
	}

	public String getTitle() {
		return title;
	}

	public ServerSQL setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getComment() {
		return comment;
	}

	public ServerSQL setComment(String comment) {
		this.comment = comment;
		return this;
	}

	public String getTitleUrl() {
		return titleUrl;
	}

	public ServerSQL setTitleUrl(String titleUrl) {
		this.titleUrl = titleUrl;
		return this;
	}

	public String getDate() {
		return date;
	}

	public ServerSQL setDate(String date) {
		this.date = date;
		return this;
	}

	public String getRaiting() {
		return raiting;
	}

	public ServerSQL setRaiting(String raiting) {
		this.raiting = raiting;
		return this;
	}

	public String getComCount() {
		return comCount;
	}

	public ServerSQL setComCount(String comCount) {
		this.comCount = comCount;
		return this;
	}

	public String getFavCount() {
		return favCount;
	}

	public ServerSQL setFavCount(String favCount) {
		this.favCount = favCount;
		return this;
	}

	public String getWatched() {
		return watched;
	}

	public ServerSQL setWatched(String watched) {
		this.watched = watched;
		return this;
	}

	public String getWant() {
		return want;
	}

	public ServerSQL setWant(String want) {
		this.want = want;
		return this;
	}

	public String getUserUrl() {
		return userUrl;
	}

	public ServerSQL setUserUrl(String userUrl) {
		this.userUrl = userUrl;
		return this;
	}

	public String getUser() {
		return user;
	}

	public ServerSQL setUser(String user) {
		this.user = user;
		return this;
	}

	public String getUserNo() {
		return userNo;
	}

	public ServerSQL setUserNo(String userNo) {
		this.userNo = userNo;
		return this;
	}

	public String getTitleNo() {
		return titleNo;
	}

	public ServerSQL setTitleNo(String titleNo) {
		this.titleNo = titleNo;
		return this;
	}
}
