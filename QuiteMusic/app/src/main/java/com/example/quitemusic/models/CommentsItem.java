package com.example.quitemusic.models;

import com.google.gson.annotations.SerializedName;

public class CommentsItem{

	@SerializedName("createdAt")
	private String createdAt;

	@SerializedName("author")
	private Author author;

	@SerializedName("content")
	private String content;

	public String getCreatedAt(){
		return createdAt;
	}

	public Author getAuthor(){
		return author;
	}

	public String getContent(){
		return content;
	}
}