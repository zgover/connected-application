// Zachary Gover
// JAV1 - 1608
// Book

package com.example.zachary.connectedapplication;

public class Book {

	/**
	 * MARK: Global Properties
	 */

	private String title = "";
	private String author = "";
	private String publisher = "";
	private String pubDate = "";
	private String description = "";
	private String imgUrl;

	/**
	 * MARK: Class Initializer
	 */

	public Book() {}

	/**
	 * MARK: Getter and Setters
	 */

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImgUrl() {
		return this.imgUrl;
	}

	public void setImgUrl(String url) {
		this.imgUrl = url;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
