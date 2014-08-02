package com.iih.android.videoblog.commonutill;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.iih.android.videoblog.R;
import com.iih.android.videoblog.RegisterActivity;

public class CommonUtility {

	public static boolean isBackPress = false;

	private ImageButton home, searchPost, videoView,Notification, profile;
	public String categorytype;

//	public static String BaseUrl1 ="http://184.107.213.34/~techiest/php/videoblog/webservice/?action=";
//	public static String BaseUrl2 ="http://54.191.5.223/videoblog_web/service?action=";
	public static String BaseUrl2 ="http://54.191.200.49/videoblog_web/service?action=";
	//40: public static String BaseUrl2 ="http://09php.com/php/videoblog/service?action="; 
	public CommonUtility(Activity act) {
		acct = act;
		// share = PreferenceManager.getDefaultSharedPreferences(acct);
	}

	private Activity acct;
	

	public void findViewById() {
		home = (ImageButton) acct.findViewById(R.id.imgbtn_Home);
		home.setOnClickListener((OnClickListener) acct);
//		home = (ImageButton) acct.findViewById(R.id.imgbtn_home);
//		home.setOnClickListener((OnClickListener) acct);
		searchPost = (ImageButton) acct.findViewById(R.id.imgbtn_search_category);
		searchPost.setOnClickListener((OnClickListener) acct);
		videoView = (ImageButton) acct.findViewById(R.id.imgbtn_camera);
		videoView.setOnClickListener((OnClickListener) acct);
		Notification = (ImageButton) acct.findViewById(R.id.imgbtn_notification);
		Notification.setOnClickListener((OnClickListener) acct);
		profile = (ImageButton) acct.findViewById(R.id.imgbtn_profile);
		profile.setOnClickListener((OnClickListener) acct);

//		Flow = acct.getIntent().getExtras().getString("flow");
		if (categorytype.equalsIgnoreCase("home")) {
			home.setBackgroundResource(R.drawable.home_tab_h);
		} else if (categorytype.equalsIgnoreCase("searchcategory")) {
			searchPost.setBackgroundResource(R.drawable.category_tab_h);
		} else if (categorytype.equalsIgnoreCase("videoView")) {
			videoView.setBackgroundResource(R.drawable.camera_tab_h);
		}else if (categorytype.equalsIgnoreCase("Notification")) {
			Notification.setBackgroundResource(R.drawable.notification_tab_h); 
		}else if (categorytype.equalsIgnoreCase("profile")) {
			profile.setBackgroundResource(R.drawable.profile_tab_h);
		}
	}


	
//	

	
	// private SharedPreferences share;

	// Checking Internet Connection
	public static boolean checkConn(Context ctx) {
		ConnectivityManager conMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (conMgr != null) {
			NetworkInfo i = conMgr.getActiveNetworkInfo();
			if (i != null) {
				if (!i.isConnected())
					return false;
				if (!i.isAvailable())
					return false;
			}

			if (i == null)
				return false;

		} else
			return false;

		return true;
	}

	// Checking Email Validations
	public static boolean checkingEmail(String email) {
		if (email.equals(""))
			return true;
		else {
			String expression = "^[a-z][a-z|0-9|]*([_][a-z|0-9]+)*([.][a-z|0-9]+([_][a-z|0-9]+)*)?@[a-z][a-z|0-9|]*\\.([a-z][a-z|0-9]*(\\.[a-z][a-z|0-9]*)?)$";
			CharSequence inputStr = email;
			Pattern pattern = Pattern.compile(expression,
					Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(inputStr);
			if (matcher.matches())
				return true;
			else
				return false;
		}
	}

	// ConvertStream To String
	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String dateFormat(String input) {

		SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date date = null;
		try {
			try {
				date = form.parse(input);

			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ParseException e) {

			e.printStackTrace();
		}
		SimpleDateFormat postFormater = new SimpleDateFormat("MMM dd, yyyy h:mmaa");
		String output = postFormater.format(date);

		return output;
	}
	public static String timeFormat(String input) {

		SimpleDateFormat form = new SimpleDateFormat("HH:mm:ss");
		java.util.Date date = null;
		try {
			try {
				date = form.parse(input);

			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ParseException e) {

			e.printStackTrace();
		}
		SimpleDateFormat postFormater = new SimpleDateFormat("h:mm aa");
		String output = postFormater.format(date);

		return output;
	}
	
	
	public static void showAlert(String message, Activity act) {
		Builder builder = new AlertDialog.Builder(act);
		builder.setTitle(R.string.message);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setMessage(message);
		builder.setPositiveButton(act.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}

	public static void showAlertForMedia(String message, final Activity act) {
		Builder builder = new AlertDialog.Builder(act);
		builder.setTitle(R.string.message);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setMessage(message);
		builder.setPositiveButton(act.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Intent intent = new Intent(act,RegisterActivity.class);
				act.startActivity(intent);
			}
		});
		builder.show();
	}

	public static void showAlertYesNo(String message, Activity act) {
		Builder builder = new AlertDialog.Builder(act);
		builder.setTitle(R.string.message);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setMessage(message);
		builder.setPositiveButton(act.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}

	public static String[] getLatAndLong(Activity mContext) {
		LocationUtility mLocation = new LocationUtility(mContext);
		return new String[] { String.valueOf(mLocation.getCurrentLatitude()),String.valueOf(mLocation.getCurrentLongitude()),String.valueOf(mLocation.isGPSON()) };
	}

	//Get Device Ip address
	public static String getDeviceIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (Exception ex) {
			Log.e("IP Address", ex.toString());
		}
		return null;
	}
}
