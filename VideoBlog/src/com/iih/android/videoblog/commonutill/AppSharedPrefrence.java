package com.iih.android.videoblog.commonutill;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * h: This Singleton class handles to store data corresponding App
 * details which are stored in {@link SharedPreferences}
 */
public class AppSharedPrefrence {

	private SharedPreferences sh;

	private AppSharedPrefrence() {

	}

	private AppSharedPrefrence(Context mContext) {
		sh = PreferenceManager.getDefaultSharedPreferences(mContext);
	}

	private static AppSharedPrefrence instance = null;

	/**
	 * 
	 * @param mContext
	 * @return {@link AppSharedPrefrence}
	 */
	public synchronized static AppSharedPrefrence getInstance(Context mContext) {

		if (instance == null) {
			instance = new AppSharedPrefrence(mContext);
		}
		return instance;
	}

	public Boolean getIsFree() {
		return sh.getBoolean("isfree", true);
	}

	public void setIsFree(Boolean IsFree) {
		sh.edit().putBoolean("isfree", IsFree).commit();
	}

	public Boolean getRemember() {
		return sh.getBoolean("remember", false);
	}

	public void setRemember(Boolean remember) {
		sh.edit().putBoolean("remember", remember).commit();
	}

	public String getUsername() {
		return sh.getString("username", "");
	}

	public void setUsername(String username) {
		sh.edit().putString("username", username).commit();
	}

	public String getPassword() {
		return sh.getString("password", "");
	}

	public void setPassword(String password) {
		sh.edit().putString("password", password).commit();
	}
	public Boolean getIsLocation() {
		return sh.getBoolean("location", false);
	}

	public void setIsLocation(Boolean IsFree) {
		sh.edit().putBoolean("location", IsFree).commit();
//		
	}
	
	public String getLanguage() {
		return sh.getString("language", "");
	}

	public void setLanguage(String Language) {
		sh.edit().putString("language", Language).commit();
	}

	public String getState() {
		return sh.getString("state", "");
	}

	public void setState(String state) {
		sh.edit().putString("state", state).commit();
	}

	public String getCounty() {
		return sh.getString("county", "");
	}

	public void setCounty(String county) {
		sh.edit().putString("county", county).commit();
	}

	public String getStateID() {
		return sh.getString("stateid", "");
	}

	public void setStateID(String stateid) {
		sh.edit().putString("stateid", stateid).commit();
	}

	public String getCountyID() {
		return sh.getString("countyid", "");
	}

	public void setCountyID(String countyid) {
		sh.edit().putString("countyid", countyid).commit();
	}

	public boolean getCameraMode() {
		return sh.getBoolean("camera", false);
	}

	public void setCameraMode(Boolean camera) {
		sh.edit().putBoolean("camera", camera).commit();
	}

	public String getLanguageID() {
		return sh.getString("langId", "");
	}

	public void setLanguageID(String langId) {
		sh.edit().putString("langId", langId).commit();
	}

	public String getSelectedLanguage() {
		return sh.getString("sele_lang", "");
	}

	public void setSelectedLanguage(String selec_lang) {
		sh.edit().putString("sele_lang", selec_lang).commit();
	}

	public String GetVideoPath() {
		return sh.getString("videopath", "");
	}

	public void SetVideoPath(String video) {
		sh.edit().putString("videopath", video).commit();
	}

	public int Getindex() {
		return sh.getInt("index", 0);
	}

	public void Setindex(int index) {
		sh.edit().putInt("index", index).commit();
	}

	public int GetTop() {
		return sh.getInt("top", 0);
	}

	public void SetTop(int top) {
		sh.edit().putInt("top", top).commit();
	}

	public String getPostOrCategory() {
		return sh.getString("post", "");
	}

	public void setPostOrCategory(String post) {
		sh.edit().putString("post", post).commit();
	}
	
	public String getFilePath() {
		return sh.getString("path", "");
	}

	public void setFilePath(String path) {
		sh.edit().putString("path", path).commit();
	}
	
	public String getFileType() {
		return sh.getString("type", "");
	}

	public void setFileType(String type) {
		sh.edit().putString("type", type).commit();
	}

//	public void setCountry(Object Country) {
//		Gson gson = new Gson();
//		String json = gson.toJson(Country);
////		prefsEditor.putString("MyObject", json);
//		
//		sh.edit().putString("country", json).commit();
//	}
//	public String getCountry() {
//		return sh.getString("country", "");
//	}
	public void clear() {
		sh.edit().clear().commit();
	}

}
