package com.iih.android.videoblog.commonutill;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * This Logger class helps to print log messages when app is in debugging mode.
 * 
 */
public class Logger {

	/**
	 * Make this field is true for see the log results otherwise make it as
	 * false.
	 */
	public final static boolean DEBUG =true;
	public final static String TAG = "SaftyAPP";

	/**
	 * 
	 * @param tag
	 *            TAG
	 * @param message
	 *            Message
	 */
	public static void d(String tag, String message) {
		if (DEBUG) {
			Log.d(tag, message);
		}
	}

	/**
	 * 
	 * @param tag
	 *            TAG
	 * @param message
	 *            Message
	 */
	public static void e(String tag, String message) {
		if (DEBUG) {
			Log.e(tag, message);
		}
	}

	/**
	 * 
	 * @param tag
	 *            TAG
	 * @param message
	 *            Message
	 */
	public static void v(String tag, String message) {
		if (DEBUG) {
			Log.v(tag, message);
		}
	}

	/**
	 * 
	 * @param tag
	 *            TAG
	 * @param message
	 *            Message
	 */
	public static void w(String tag, String message) {
		if (DEBUG) {
			Log.w(tag, message);
		}
	}

	/**
	 * 
	 * @param tag
	 *            TAG
	 * @param message
	 *            Message
	 */
	public static void i(String tag, String message) {
		if (DEBUG) {
			Log.i(tag, message);
		}
	}

	/**
	 * 
	 * @param tag
	 *            TAG
	 * @param message
	 *            Message
	 */
	public static void toast(Context mContext, String message) {
		if (DEBUG) {
			Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
		}
	}

}
