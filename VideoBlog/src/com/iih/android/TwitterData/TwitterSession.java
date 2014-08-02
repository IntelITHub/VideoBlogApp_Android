package com.iih.android.TwitterData;

import twitter4j.auth.AccessToken;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class TwitterSession {
	private final SharedPreferences sharedPref;
	private final Editor editor;

	private static final String TWEET_AUTH_KEY = "auth_key";
	private static final String TWEET_AUTH_SECRET_KEY = "auth_secret_key";
	private static final String TWEET_USER_NAME = "user_name";
	private static final String SHARED = "Twitter_Preferences";
	
	private static final String TWEET_USERDESC = "user_desc";
	private static final String TWEET_PROPIC = "user_pic";
	private static final String TWEET_USCREENNAME = "uscreen_name";
	private static final String TWEET_UID ="uid" ;
	
	public TwitterSession(Context context) {
		sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);

		editor = sharedPref.edit();
	}
//	(mAccessToken, user.getName(),user.getDescription(),user.getId(),user.getMiniProfileImageURL(),user.getScreenName())
	public void storeAccessToken(AccessToken accessToken, String username, String desc, long uid, String ProPicUrl, String ScreenName) {
		editor.putString(TWEET_AUTH_KEY, accessToken.getToken());
		editor.putString(TWEET_AUTH_SECRET_KEY, accessToken.getTokenSecret());
		editor.putString(TWEET_USER_NAME, username);

		editor.putString(TWEET_USERDESC, desc);
		editor.putLong(TWEET_UID, uid);
		editor.putString(TWEET_PROPIC, ProPicUrl);
		editor.putString(TWEET_USCREENNAME, ScreenName);

		editor.commit();
	}

	public void resetAccessToken() {
		editor.putString(TWEET_AUTH_KEY, null);
		editor.putString(TWEET_AUTH_SECRET_KEY, null);
		editor.putString(TWEET_USER_NAME, null);

		editor.commit();
	}

	public String getUsername() {
		return sharedPref.getString(TWEET_USER_NAME, "");
	}

	public String getUserDesc() {
		return sharedPref.getString(TWEET_USERDESC, "");
	}
	public Long getUID() {
		return sharedPref.getLong(TWEET_UID, 0);
	}
	public String getUProPic() {
		return sharedPref.getString(TWEET_PROPIC, "");
	}
	public String getScreenname() {
		return sharedPref.getString(TWEET_USCREENNAME, "");
	}
	public String getToken() {
		return sharedPref.getString(TWEET_AUTH_KEY, null);
	}
	public String getTokenSecret() {
		return sharedPref.getString(TWEET_AUTH_SECRET_KEY, null);
	}
	public AccessToken getAccessToken() {
		String token = sharedPref.getString(TWEET_AUTH_KEY, null);
		String tokenSecret = sharedPref.getString(TWEET_AUTH_SECRET_KEY, null);

		if (token != null && tokenSecret != null)
			return new AccessToken(token, tokenSecret);
		else
			return null;
	}
}