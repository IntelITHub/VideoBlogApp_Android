package com.iih.android.videoblog;

import android.content.Context;
import android.content.Intent;

public final class CommonUtilities {

	// give your server registration url here
	// static final String SERVER_URL =
	// "http://10.0.2.2/gcm_server_php/register.php";
//	static final String SERVER_URL = "http://54.191.5.223/videoblog_web/service?action=userRegistration";
	static final String SERVER_URL = "http://54.191.200.49/videoblog_web/service?action=userRegistration";
	//http://54.191.5.223/videoblog_web/service
	// Google project id
	//its test account id of intelithub
	//static final String SENDER_ID = "469579553288";
	static final String SENDER_ID = "114214773236";

	/**
	 * Tag used on log messages.
	 */
	static final String TAG = "AndroidHive GCM";

	static final String DISPLAY_MESSAGE_ACTION = "com.iih.android.videoblog.DISPLAY_MESSAGE";

	static final String EXTRA_MESSAGE = "message";

	/**
	 * Notifies UI to display a message.
	 * <p>
	 * This method is defined in the common helper because it's used both by the
	 * UI and the background service.
	 * 
	 * @param context
	 *            application's context.
	 * @param message
	 *            message to be displayed.
	 */
	static void displayMessage(Context context, String message) {
		Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
		intent.putExtra(EXTRA_MESSAGE, message);
		context.sendBroadcast(intent);
	}
}
