package com.iih.android.videoblog.commonutill;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class CommonVariable {

	private static CommonVariable instance = null;
	private String Category_id, post_id, member_id, imgfile_path, Profiler_id,file_Type,GCM_id,Reg_Imagepath;
	private Boolean flagAllpost;
	private int scroll;
	private ArrayList<Object> CountryJson,NewsfeedJason,allpost;
	private LinkedHashMap<String, Object> data;

	public synchronized static CommonVariable getInstance() {
		if (instance == null) {
			instance = new CommonVariable();
		}
		return instance;
	}

	public String getCategoryId() {
		return Category_id;
	}

	public void setCategoryId(String category_id) {
		this.Category_id = category_id;
	}

	public String getPost_id() {
		return post_id;
	}

	public void setPost_id(String post_id) {
		this.post_id = post_id;
	}

	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}

	public String getProfiler_id() {
		return Profiler_id;
	}

	public void setProfiler_id(String profiler_id) {
		this.Profiler_id = profiler_id;
	}

	
	
	public void setcountry(ArrayList<Object> CountryJson) {
		this.CountryJson = CountryJson;
	}
	public Object getcountry() {

		return CountryJson;
	}
	public void setNewsfeed(ArrayList<Object> newsfeed) {
		this.NewsfeedJason = newsfeed;
	}
	public Object getNewsFeed() {
		return NewsfeedJason;
	}
	
	public void setGCM_id(String GCM_id) {
		this.GCM_id = GCM_id;
	}

	public String getGCM_id() {
		return GCM_id;
	}

	public String getRegistrationFilePath() {

		return Reg_Imagepath;
	}

	public void setRegistrationFilePath(String Reg_imgfile_path) {
		this.Reg_Imagepath = Reg_imgfile_path;
	}

	public void setFlagPost(boolean value) {
		this.flagAllpost = value;
	}

	public Boolean getFlagPost() {
		return false;
	}

	public void setScroll(int scroll) {
		this.scroll = scroll;
	}

	public int getScroll() {
		return scroll;
	}

	public void setAllPostTemp(ArrayList<Object> post) {
		this.allpost = post;
	}
	public Object getAllpostTemp() {
		return allpost;
	}

	public void setAllPostData(LinkedHashMap<String, Object> data) {
		this.data = data;
	}
	public Object getAllpostData() {
		return allpost;
	}

	public void clear() {
		instance = null;
	}

}
