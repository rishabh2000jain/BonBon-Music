package com.example.quitemusic.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class SongInfo{

	public void setStreamURL(String streamURL) {
		this.streamURL = streamURL;
	}

	@SerializedName("streamURL")
	private String  streamURL;

	@SerializedName("thumbnail")
	private String thumbnail;

	@SerializedName("comments")
	private List<CommentsItem> comments;

	@SerializedName("publishedAt")
	private String publishedAt;

	@SerializedName("author")
	private Author author;

	@SerializedName("description")
	private String description;

	@SerializedName("title")
	private String title;

	@SerializedName("duration")
	private int duration;

	@SerializedName("playCount")
	private int playCount;

	@SerializedName("likesCount")
	private int likesCount;

	@SerializedName("commentsCount")
	private int commentsCount;

	@SerializedName("recommendedSongs")
	private List<Object> recommendedSongs;

	@SerializedName("genre")
	private String genre;

	@SerializedName("id")
	private String id;

	@SerializedName("embed")
	private String embed;

	@SerializedName("trackURL")
	private String trackURL;

	public String  getStreamURL(){
		return streamURL;
	}

	public String getThumbnail(){
		return thumbnail;
	}

	public List<CommentsItem> getComments(){
		return comments;
	}

	public String getPublishedAt(){
		return publishedAt;
	}

	public Author getAuthor(){
		return author;
	}

	public String getDescription(){
		return description;
	}

	public String getTitle(){
		return title;
	}

	public int getDuration(){
		return duration;
	}

	public int getPlayCount(){
		return playCount;
	}

	public int getLikesCount(){
		return likesCount;
	}

	public int getCommentsCount(){
		return commentsCount;
	}

	public List<Object> getRecommendedSongs(){
		return recommendedSongs;
	}

	public String getGenre(){
		return genre;
	}

	public String getId(){
		return id;
	}

	public String getEmbed(){
		return embed;
	}

	public String getTrackURL(){
		return trackURL;
	}
}