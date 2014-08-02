package com.iih.android.videoblog;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iih.android.TwitterData.Twitter_Handler;
import com.iih.android.TwitterData.Twitter_Handler.TwDialogListener;
import com.iih.android.videoblog.Parsing.PostDataAndGetData;
import com.iih.android.videoblog.Parsing.parseListner;
import com.iih.android.videoblog.commonutill.AppSharedPrefrence;
import com.iih.android.videoblog.commonutill.CommonUtility;
import com.iih.android.videoblog.commonutill.CommonVariable;
import com.iih.android.videoblog.commonutill.CustomeAlertYesNo;
import com.iih.android.videoblog.gallery.CameraActivity;
import com.iih.android.videoblog.newsfeed.NewsFeedScreen;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebook.OnLoginListener;
import com.sromku.simple.fb.SimpleFacebook.OnProfileRequestListener;
import com.sromku.simple.fb.entities.Profile;

public class LoginActivity extends Activity implements parseListner {

	private static String TAG = "LoginActivity";
	public static SimpleFacebook mSimpleFacebook;
	private Twitter_Handler mTwitter;

	// Twitter Auth Key
	//public final String consumer_key = "k1FNrBhSrQv5SkU2bzaLg";
	//public final String secret_key = "PukXNGxeLDXuwUjnQV0ULtOQ64hXvKuBeQPXGVfLmKg";

	public final String consumer_key = "ct3IXFnfUicdvFPbVGN5jg";
	public final String secret_key = "AD0RAHWWaJv9gf0GZsXlNgPNMNCywkSdVvr5upDVHg";

	private TextView actiontitle;
	private Button actionleft;
	private Button actionRight;
	private Button btnLogin;
	private Button btnRegister;
	private EditText edtUsername;
	private EditText edtPassword;
	private final int ResponseCode = 0;
	private final int FBResponseCode = 1;
	private final int TwtResponseCode = 2;

	private String username;
	private String password;
	private String mFbToken;

	private Button btn_Facebook;
	private Button btn_twitter;

	private CheckBox chk_Remember;

	public PostDataAndGetData psd;
	private TextView txtForgotpwd;
	private String yourname;
	private String strUID = "";
	private URL image_value;

	private String android_id;

	PackageInfo info;

	private String ipaddress = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		if (Build.VERSION.SDK_INT >= 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		//check Device Ip Address
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						ipaddress = inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (Exception ex) {
			Log.e("IP Address", ex.toString());
		}

//		//for getting facebook api key
//		try {
//			info = getPackageManager().getPackageInfo("com.iih.android.videoblog",PackageManager.GET_SIGNATURES);
//			for (Signature signature : info.signatures) {
//				MessageDigest md;
//				md = MessageDigest.getInstance("SHA");
//				md.update(signature.toByteArray());
//				String something = new String(Base64.encode(md.digest(), 0));
//				// String something = new
//				// String(Base64.encodeBytes(md.digest()));
//				Log.e("hash key", something);
//			}
//		} catch (NameNotFoundException e1) {
//			Log.e("name not found", e1.toString());
//		} catch (NoSuchAlgorithmException e) {
//			Log.e("no such an algorithm", e.toString());
//		} catch (Exception e) {
//			Log.e("exception", e.toString());
//		}

		initialization();

		chk_Remember.setChecked(AppSharedPrefrence.getInstance(LoginActivity.this).getRemember());
		if (chk_Remember.isChecked()) {
			edtUsername.setText(AppSharedPrefrence.getInstance(LoginActivity.this).getUsername());
			//edtPassword.setText(AppSharedPrefrence.getInstance(LoginActivity.this).getPassword());
		}

		btnRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
				startActivity(intent);
//				finish();
			}
		});
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				LoginAction();
			}
		});
		btn_Facebook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mSimpleFacebook.login(mFBLoginListener);
			}
		});

		btn_twitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mTwitter = new Twitter_Handler(LoginActivity.this,consumer_key, secret_key);
				mTwitter.setListener(twitterLoginDialogListener);

				if (mTwitter.hasAccessToken()) {
					// this will post data in asyn background thread
					// showTwittDialog();
					CallWsTwitterLogin();
					/*Intent intent = new Intent(LoginActivity.this,Termsandcondition.class);
					startActivity(intent);
					finish();*/
				} else {
					mTwitter.authorize();
				}
			}
		});

		chk_Remember.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				username = edtUsername.getText().toString().trim();
				//password = edtPassword.getText().toString().trim();

				if (((CheckBox) v).isChecked()) {
					AppSharedPrefrence.getInstance(LoginActivity.this).setRemember(true);
				} else {
					AppSharedPrefrence.getInstance(LoginActivity.this).setRemember(false);
				}
			}
		});

		txtForgotpwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				forgotPasswordAction(v);
			}
		});
		
//		callSwitchLang(AppSharedPrefrence.getInstance(LoginActivity.this).getLanguage());
		TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		android_id = tm.getDeviceId();
	}

	private void callSwitchLang(String langCode) {
		Locale locale = new Locale(langCode);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
		onCreate(null);
	}
	public void forgotPasswordAction(View v) {
		ForgotPasswordDialog d = new ForgotPasswordDialog(LoginActivity.this);
		d.show();
	}

	private void initialization() {

		actiontitle = (TextView) findViewById(R.id.action_title);
		actiontitle.setText(getResources().getString(R.string.login));

		actionleft = (Button) findViewById(R.id.action_Left);
		actionleft.setVisibility(View.INVISIBLE);

		actionRight = (Button) findViewById(R.id.action_right);
		actionRight.setVisibility(View.INVISIBLE);

		btnLogin = (Button) findViewById(R.id.btn_loginbtn);
		btnRegister = (Button) findViewById(R.id.btn_registerbtn);
		edtUsername = (EditText) findViewById(R.id.edt_username);
		edtPassword = (EditText) findViewById(R.id.edt_Password);
		btn_Facebook = (Button) findViewById(R.id.btn_facebook);
		btn_twitter = (Button) findViewById(R.id.btn_twitter);
		chk_Remember = (CheckBox) findViewById(R.id.remember_me);
		txtForgotpwd = (TextView) findViewById(R.id.txt_forgotpwd);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSimpleFacebook = SimpleFacebook.getInstance(this);
	}

	/**
	 * Calling the web-service
	 */
	HashMap<String, String> Paramter;
	HashMap<String, String> Parameter = null;

	
//	eLoginType ( Possible value REGISTER,FACEBOOK,TWITTER )
//	vFacebookId ( IF Login with FACEBOOK )
//	tFacebookToken
//	vTwitterId ( IF Login with TWITTER )
//	tTwitterToken
//	iMemberId
	
	public void CallWsFBLogin() {
		Parameter = new HashMap<String, String>();

//		Parameter.put("vPassword", password);
		Parameter.put("vPicture",""+image_value);
//		Parameter.put("vEmail", username);
		Parameter.put("vName", yourname);
		Parameter.put("vUsername", username);
		Parameter.put("eLoginType", "FACEBOOK");
		Parameter.put("vFacebookId", strUID);
		Parameter.put("tFacebookToken", mFbToken);
		Parameter.put("tDeviceToken", CommonVariable.getInstance().getGCM_id());
		Parameter.put("vIP", ipaddress);
		Parameter.put("vDeviceid",android_id);
		Parameter.put("iLangId", AppSharedPrefrence.getInstance(LoginActivity.this).getLanguageID());
		//Parameter.put("iMemberId", CommonVariable.getInstance().getMember_id());

		psd = new PostDataAndGetData(LoginActivity.this, null, Parameter,
				"POST", CommonUtility.BaseUrl2 + "userLogin", FBResponseCode,0, false, true);
		try {
			psd.execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void CallWsTwitterLogin() {
		Parameter = new HashMap<String, String>();

//		Parameter.put("vPassword", password);
		Parameter.put("vPicture", mTwitter.getUProPic());
//		Parameter.put("vEmail", mTwitter.getUsername());
		Parameter.put("vName", mTwitter.getUsername());
		Parameter.put("vUsername", mTwitter.getScreenname());
		Parameter.put("eLoginType", "TWITTER");
		Parameter.put("tDeviceToken", CommonVariable.getInstance().getGCM_id());
		Parameter.put("vDeviceid",android_id);
//		Parameter.put("vTwitterId", ""+mTwitter.getUID());
		Parameter.put("vTwitterId", mTwitter.getToken());
		Parameter.put("tTwitterToken", mTwitter.getTokenSecret());
		Parameter.put("vIP", ipaddress);
		Parameter.put("iLangId", AppSharedPrefrence.getInstance(LoginActivity.this).getLanguageID());
		//Parameter.put("iMemberId", CommonVariable.getInstance().getMember_id());

		psd = new PostDataAndGetData(LoginActivity.this, null, Parameter,"POST", CommonUtility.BaseUrl2 + "userLogin", TwtResponseCode,0, false, true);

		try {
			psd.execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void CallWsLogin() {
		Parameter = new HashMap<String, String>();

		Parameter.put("vEmail", username);
		Parameter.put("vPassword", password);
		Parameter.put("eLoginType", "REGISTER");
		Parameter.put("vDeviceid",android_id);
		Parameter.put("vIP", ipaddress);
		Parameter.put("tDeviceToken", CommonVariable.getInstance().getGCM_id());
		Parameter.put("iLangId", AppSharedPrefrence.getInstance(LoginActivity.this).getLanguageID());

		psd = new PostDataAndGetData(LoginActivity.this, null, Parameter,"POST", CommonUtility.BaseUrl2 + "userLogin", ResponseCode, 0,false, true);
		try {
			psd.execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void LoginAction() {

		username = edtUsername.getText().toString().trim();
		password = edtPassword.getText().toString().trim();

		if(chk_Remember.isChecked()){
			AppSharedPrefrence.getInstance(LoginActivity.this).setUsername(username);
			//AppSharedPrefrence.getInstance(LoginActivity.this).setPassword(password);
		}else{
			AppSharedPrefrence.getInstance(LoginActivity.this).setUsername("");
			AppSharedPrefrence.getInstance(LoginActivity.this).setPassword("");
		}

		if (isEmpty(username))
			CommonUtility.showAlert(getString(R.string.please_enter_user_name_),LoginActivity.this);
		else if (isEmpty(password))
			CommonUtility.showAlert(getString(R.string.please_enter_password_), LoginActivity.this);
		else {
			CallWsLogin();
		}
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str.trim())) {
			return true;
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data);
	}

	@Override
	public void onBackPressed() {
		CustomeAlertYesNo customedialog = new CustomeAlertYesNo(LoginActivity.this);
		customedialog.show();
		// super.onBackPressed();
	}

	// Login with facebook
	public OnLoginListener mFBLoginListener = new OnLoginListener() {

		private ProgressDialog bar;
		@Override
		public void onFail(String reason) {
			Log.w(TAG, "Failed to login");
		}

		@Override
		public void onException(Throwable throwable) {
			
			Log.e(TAG, "Bad thing happened", throwable);
		}

		@Override
		public void onThinking() {
			// show progress bar or something to the user while login is
			// happening
			
		}

		@Override
		public void onLogin() {

			mSimpleFacebook.getProfile(new OnProfileRequestListener() {

				@Override
				public void onFail(String reason) {
				}

				@Override
				public void onException(Throwable throwable) {

				}

				@Override
				public void onThinking() {

				}
				@Override
				public void onComplete(Profile profile) {


					strUID = profile.getId();
					yourname=profile.getName();
					username=profile.getUsername();
					mFbToken=mSimpleFacebook.getAccessToken();

					try {
						
//						strPicture=profile.getPicture();
						image_value = new URL("http://graph.facebook.com/"+strUID+"/picture") ;
						Log.d("profle Pic",""+image_value);
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
					CallWsFBLogin();
				}
			});

		}

		@Override
		public void onNotAcceptingPermissions() {
			Toast.makeText(LoginActivity.this,"You didn't accept read permissions.", Toast.LENGTH_LONG).show();
		}
	};

	// Login with Twitter
	private final TwDialogListener twitterLoginDialogListener = new TwDialogListener() {

		@Override
		public void onError(String value) {
			Toast.makeText(LoginActivity.this, R.string.login_failed,Toast.LENGTH_LONG).show();
			mTwitter.resetAccessToken();
		}

		@Override
		public void onComplete(String value) {
			// showTwittDialog();
			CallWsTwitterLogin();
//			Intent intent = new Intent(LoginActivity.this, NewsFeedScreen.class);
//			startActivity(intent);
//			finish();
			
		}
	};

	/**
	 * Web-service Response
	 */

	LinkedHashMap<String, Object> temp;
	ArrayList<LinkedHashMap<String, Object>> jsonData;
	ArrayList<Object> arrayList;

	LinkedHashMap<String, Object> data;

	@SuppressWarnings("unchecked")
	@Override
	public void GetResult(Object jsonDataO, int responce) {
		try {
			arrayList = (ArrayList<Object>) jsonDataO;
			ArrayList<LinkedHashMap<String, Object>> jsonDataTemp = (ArrayList<LinkedHashMap<String, Object>>) jsonDataO;
			int size = arrayList.size();
			if (size != 0) {
				temp = (LinkedHashMap<String, Object>) arrayList.get(0);

				data= (LinkedHashMap<String, Object>) temp.get("message");
				if (data.get("success").toString().equalsIgnoreCase("1")) {
					jsonData = jsonDataTemp;
//					tempData=(ArrayList<Object>) temp.get("data");
					data=(LinkedHashMap<String, Object>) temp.get("data");
					switch (responce) {
					case ResponseCode:

						try {
							CommonVariable.getInstance().setMember_id(data.get("iMemberId").toString());

							if(data.get("iLangId").toString().equals("en")){
								callSwitchLang("en_US");
							}else{
								callSwitchLang("pt_PT");
							}

							/**
							 * When User select Camera mode is selected then Directly camera activity is open
							 */
							if(data.get("eCameraMode").toString().equals("ON")){
								AppSharedPrefrence.getInstance(LoginActivity.this).setCameraMode(true);
							}else
							{
								AppSharedPrefrence.getInstance(LoginActivity.this).setCameraMode(false);
							}

							if(AppSharedPrefrence.getInstance(LoginActivity.this).getCameraMode()){
								Intent intent = new Intent(LoginActivity.this,CameraActivity.class);
								startActivity(intent);
								finish();
							}else{
								Intent intent = new Intent(LoginActivity.this,NewsFeedScreen.class);
								startActivity(intent);
								finish();
							}

						} catch (Exception e) {
							e.printStackTrace();
						}

						break;

					case FBResponseCode:

						if(data.get("iLangId").toString().equals("en")){
							callSwitchLang("en_US");
						}else{
							callSwitchLang("pt_PT");
						}

						if(data.get("eCameraMode").toString().equals("ON")){
							AppSharedPrefrence.getInstance(LoginActivity.this).setCameraMode(true);
						}else
						{
							AppSharedPrefrence.getInstance(LoginActivity.this).setCameraMode(false);
						}

						try {
							CommonVariable.getInstance().setMember_id(data.get("iMemberId").toString());

							/**
							 * When User select Camera mode is selected then Directly camera activity is open
							 */
							if(AppSharedPrefrence.getInstance(LoginActivity.this).getCameraMode()){
								Intent intent = new Intent(LoginActivity.this,CameraActivity.class);
								startActivity(intent);
								finish();
							}else{
								Intent intent = new Intent(LoginActivity.this,NewsFeedScreen.class);
								startActivity(intent);
								finish();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

						break;

					case TwtResponseCode:

						if(data.get("iLangId").toString().equals("en")){
							callSwitchLang("en_US");
						}else{
							callSwitchLang("pt_PT");
						}

						if(data.get("eCameraMode").toString().equals("ON")){
							AppSharedPrefrence.getInstance(LoginActivity.this).setCameraMode(true);
						}else
						{
							AppSharedPrefrence.getInstance(LoginActivity.this).setCameraMode(false);
						}

						try {
							CommonVariable.getInstance().setMember_id(data.get("iMemberId").toString());

							/**
							 * When User select Camera mode is selected then Directly camera activity is open
							 */
							if(AppSharedPrefrence.getInstance(LoginActivity.this).getCameraMode()){
								Intent intent = new Intent(LoginActivity.this,CameraActivity.class);
								startActivity(intent);
								finish();
							}else{
								Intent intent = new Intent(LoginActivity.this,NewsFeedScreen.class);
								startActivity(intent);
								finish();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

						break;
					default:
						break;

					}
				} else {
					
					if(data.get("msg").toString().equals("you are not registered")){
						//If it is new user then it redirect to registration page
						CommonUtility.showAlertForMedia(data.get("msg").toString(),LoginActivity.this);
					}else{
						CommonUtility.showAlert(data.get("msg").toString(),LoginActivity.this);
					}
				}
			} else {
				CommonUtility.showAlert(getString(R.string.there_is_some_problem_in_getting_data_), LoginActivity.this);
			}
		} catch (Exception e) {
			CommonUtility.showAlert(getString(R.string.there_is_some_problem_in_getting_data_), LoginActivity.this);
		}

	}
}
