package com.iih.android.videoblog;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.iih.android.videoblog.commonutill.CommonUtility;

public class ForgotPasswordDialog extends Dialog implements
		android.view.View.OnClickListener, Runnable {
	private Button cancel, send;
	private EditText email_id;

	private ProgressDialog pd;
	private String Success = "";
	private String Msg = "";
	private boolean ErrorMsg = false;
	private Activity activity;

	public ForgotPasswordDialog(Activity c) {
		super(c);
		activity = c;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setCanceledOnTouchOutside(false);
		setContentView(R.layout.forgot_pwd_dialog);

		email_id = (EditText) findViewById(R.id.email_id);

		cancel = (Button) findViewById(R.id.btn_cancel);
		cancel.setOnClickListener(this);

		send = (Button) findViewById(R.id.btn_submit_2);
		send.setOnClickListener(this);
	}

	public void onClick(View v) {
		int id = v.getId();

		if (id == R.id.btn_cancel) {
			dismiss();
		} else if (id == R.id.btn_submit_2) {
			String email = email_id.getText().toString().trim();

			// Msg = activity.getString(R.string.dialog_susses);
			// CommonDialog cd = new CommonDialog(activity," ", Msg);
			// cd.show();

			if (email.equals("") || email.equals(" ") || email == null) {
				// CommonDialog cd = new CommonDialog(activity,
				// activity.getString(R.string.alert), "Please Enter Email");
				// cd.show();

				CommonUtility.showAlert(getContext().getString(R.string.please_enter_email_address), activity);

			} else if (!CommonUtility.checkingEmail(email)) {

				CommonUtility.showAlert(getContext().getString(R.string.please_enter_valid_email), activity);
			} else {


				pd = ProgressDialog.show(activity, getContext().getString(R.string.please_wait), activity.getString(R.string.loading_),false, false);
				pd.setIcon(R.drawable.ic_launcher);

				Thread t = new Thread(ForgotPasswordDialog.this);
				t.start();

				// if (CommonUtility.checkConn(activity) == true) {
				// pd = ProgressDialog.show(activity, "", "Loading...", true,
				// false);
				// getExecuteHttpRequest(email);
				// } else if (CommonUtility.checkConn(activity
				// .getApplicationContext()) == false) {
				// showAlert("Sorry, No internet connectivity found");
				// }
			}

			// if(cs.equals(""))
			// {
			// MobileWaleUtility.showAlertOk(activity, "Error",
			// "Please Enter Email Address");
			// }
			// else if(!MobileWaleUtility.checkingEmail(cs))
			// {
			// MobileWaleUtility.showAlertOk(activity, "Error",
			// "Please Enter Valid Email Address");
			// }
			// else
			// {
			// pd = ProgressDialog.show(activity, "Please Wait",
			// "Loading...",false, false);
			// pd.setIcon(R.drawable.icon);
			// Thread t = new Thread(this);
			// t.start();
			// }
			dismiss();
		}
	}

	public void resetPassword(String email) {
		int responseCode = -1;
		Success = "";
		Msg = "";
		ErrorMsg = false;

//		String str_url = "http://54.191.5.223/videoblog_web/service?action=forgotPassword&vEmail="+ email;
		String str_url = "http://54.191.200.49/videoblog_web/service?action=forgotPassword&vEmail="+ email;
		try {
			URL url = new URL(str_url);

			URLConnection url_conn = url.openConnection();
			HttpURLConnection http_conn = (HttpURLConnection) url_conn;
			http_conn.setRequestMethod("GET");
			http_conn.setConnectTimeout(5000);
			http_conn.connect();
			responseCode = http_conn.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStream is = http_conn.getInputStream();
				String result = CommonUtility.convertStreamToString(is);
				JSONTokener t = new JSONTokener(result);
				JSONObject parentObject = new JSONObject(result);

				/**
				 * This 2 line is used when json Array is get as a response Here
				 * object directly get
				 */
				// JSONArray a = new JSONArray(t);
				JSONObject o = parentObject.getJSONObject("message");
				Success = o.getString("success");
				// Msg = data.getString("msg");

				if (!Success.equals("1")) {
					ErrorMsg = true;
				} else {
					Msg = o.getString("msg");
				}
			} else {
				ErrorMsg = true;
				Msg = "Connection Error";
			}
		} catch (Exception e) {
			ErrorMsg = true;
			Msg = e.getMessage();
		}
	}


	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message message) {

		
			pd.dismiss();

			dismiss();
			if (ErrorMsg) {

				// Msg = activity.getString(R.string.dialog_susses);
				CommonUtility.showAlert(getContext().getString(R.string.email_address_not_found), activity);

			} else {

				CommonUtility.showAlert(Msg, activity);
			}
		}
	};

	@Override
	public void run() {
		// TODO Auto-generated method stub

		final Thread t = new Thread() {
			@Override
			public void run() {
				try {
					resetPassword(email_id.getText().toString());
					handler.sendEmptyMessage(0);
				} finally {

				}
			}
		};
		t.start();

		

					
		
		
	}

}