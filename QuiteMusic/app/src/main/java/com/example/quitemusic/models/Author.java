package com.example.quitemusic.models;

import com.google.gson.annotations.SerializedName;

public class Author{

	@SerializedName("urn")
	private Object urn;

	@SerializedName("createdAt")
	private Object createdAt;

	@SerializedName("followers")
	private int followers;

	@SerializedName("avatarURL")
	private Object avatarURL;

	@SerializedName("profile")
	private String profile;

	@SerializedName("name")
	private String name;

	@SerializedName("username")
	private String username;

	public Object getUrn(){
		return urn;
	}

	public Object getCreatedAt(){
		return createdAt;
	}

	public int getFollowers(){
		return followers;
	}

	public Object getAvatarURL(){
		return avatarURL;
	}

	public String getProfile(){
		return profile;
	}

	public String getName(){
		return name;
	}

	public String getUsername(){
		return username;
	}
}