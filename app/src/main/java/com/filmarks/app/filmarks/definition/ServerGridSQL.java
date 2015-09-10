package com.filmarks.app.filmarks.definition;

/**
 * Created by user on 15/04/24.
 */
public class ServerGridSQL {

	private String title, titleUrl, reviewDate, raiting,comCount,favCount,titleNo;

	// 計１３個
	public ServerGridSQL(String reviewDate, String raiting, String comCount,
						 String favCount, String titleUrl, String title, String titleNo) {

		this.reviewDate = reviewDate;
		this.raiting = raiting;
		this.comCount = comCount;
		this.favCount = favCount;
		this.title = title;
		this.titleNo = titleNo;
		this.titleUrl = titleUrl;
	}

	public String getTitle() {
		return title;
	}

	public ServerGridSQL setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getTitleUrl() {
		return titleUrl;
	}

	public ServerGridSQL setTitleUrl(String titleUrl) {
		this.titleUrl = titleUrl;
		return this;
	}

	public String getReviewDate() {
		return reviewDate;
	}

	public ServerGridSQL setReviewDate(String reviewDate) {
		this.reviewDate = reviewDate;
		return this;
	}

	public String getRaiting() {
		return raiting;
	}

	public ServerGridSQL setRaiting(String raiting) {
		this.raiting = raiting;
		return this;
	}

	public String getComCount() {
		return comCount;
	}

	public ServerGridSQL setComCount(String comCount) {
		this.comCount = comCount;
		return this;
	}

	public String getFavCount() {
		return favCount;
	}

	public ServerGridSQL setFavCount(String favCount) {
		this.favCount = favCount;
		return this;
	}

	public String getTitleNo() {
		return titleNo;
	}

	public ServerGridSQL setTitleNo(String titleNo) {
		this.titleNo = titleNo;
		return this;
	}
}
