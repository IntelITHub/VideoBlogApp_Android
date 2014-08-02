package com.iih.android.videoblog;

import static com.iih.android.videoblog.CommonUtilities.EXTRA_MESSAGE;

import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.iih.android.videoblog.Parsing.PostDataAndGetData;
import com.iih.android.videoblog.commonutill.AppSharedPrefrence;
import com.iih.android.videoblog.commonutill.CommonVariable;


public class SplashScreenActivity extends Activity {

	HashMap<String, String> Paramter;
	PostDataAndGetData psd;
	HashMap<String, String> Parameter = null;
	private final int COUNTRY = 0,STATE = 1;
	String regId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);
		
				mHandler.sendEmptyMessageDelayed(0, 4000);

				// checkNotNull(SERVER_URL, "SERVER_URL");
				// checkNotNull(SENDER_ID, "SENDER_ID");
				// Make sure the device has the proper dependencies.
				GCMRegistrar.checkDevice(SplashScreenActivity.this);
				// Make sure the manifest was properly set - comment out this line
				// while developing the app, then uncomment it when it's ready.
				GCMRegistrar.checkManifest(SplashScreenActivity.this);
				// setContentView(R.layout.main);
				// mDisplay = (TextView) findViewById(R.id.display);
				/**
				 * Added by JIgnesh for showing activity as popup
				 */
				registerReceiver(mHandleMessageReceiver, new IntentFilter(CommonUtilities.DISPLAY_MESSAGE_ACTION));
				regId = GCMRegistrar.getRegistrationId(SplashScreenActivity.this);
				if (regId.equals("")) {
					// Automatically registers application on startup.
					GCMRegistrar.register(SplashScreenActivity.this, CommonUtilities.SENDER_ID);
				}
				regId = GCMRegistrar.getRegistrationId(SplashScreenActivity.this);
				CommonVariable.getInstance().setGCM_id(regId);

		//Dont Call webservice
		//CallWsCountry();
		
	}
//	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//
////			String newMessage = intent.getExtras().getString(
////					CommonUtilities.EXTRA_MESSAGE);
////			AppTypeDetails.getInstance(mActivity).SetSms(newMessage);
////			Intent mintent = new Intent(mActivity, SmsDetailAct.class);
////			mintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////			mintent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
////			mActivity.startActivity(mintent);
//
//			// mDisplay.append(newMessage + "\n");
//
//			// Start Activity hear
//		}
//	};
	
	
	/**
	 * Receiving push messages
	 * */
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());

			/**
			 * Take appropriate action on this message
			 * depending upon your app requirement
			 * For now i am just displaying it on the screen
			 * */

			// Showing received message
//			lblMessage.append(newMessage + "\n");			
			Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();

			// Releasing wake lock
			WakeLocker.release();
		}
	};
	@Override
	public void onBackPressed() {
		//super.onBackPressed();

	}

//	@Override
//		protected void onPause() {
//			// TODO Auto-generated method stub
//		unregisterReceiver(mHandleMessageReceiver);	
//		super.onPause();
//		}
//
//	@Override
//	protected void onDestroy() {
//		try {
//			//unregisterReceiver(new MyReceiver(zActivity));
//			unregisterReceiver(mHandleMessageReceiver);
//			
//			GCMRegistrar.onDestroy(getApplicationContext());
//		} catch (Exception e) {
//			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
//		}
//		super.onDestroy();
//	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
//			AppSharedPrefrence.getInstance(SplashScreenActivity.this).getCountry();
			if (AppSharedPrefrence.getInstance(SplashScreenActivity.this).getLanguage().equalsIgnoreCase("")) {
				startActivity(new Intent(SplashScreenActivity.this, Language.class));
				finish();
			}else{
				callSwitchLang(AppSharedPrefrence.getInstance(SplashScreenActivity.this).getLanguage());
				startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
				finish();
			}
			
		};
	};
	
	private void callSwitchLang(String langCode) {
		Locale locale = new Locale(langCode);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
		// onCreate(null);
	}

	/**
	 * Calling the web-service of Country
	 */

//	public void CallWsCountry() {
//
//		psd = new PostDataAndGetData(SplashScreenActivity.this, null, null,"GET",CommonUtility.BaseUrl2 +"getCountries",COUNTRY, 0, false, false);
//		psd.execute();
//	}
	/**
	 * Calling the web-service of State
	 */

//	public void CallWsState() {
//
//		psd = new PostDataAndGetData(SplashScreenActivity.this, null, null,"GET",CommonUtility.BaseUrl2 +"getState",STATE, 0, false, false);
//		psd.execute();
//	}

//	LinkedHashMap<String, Object> temp;
//	ArrayList<LinkedHashMap<String, Object>> jsonData;
//	ArrayList<Object> arrayList;
//	LinkedHashMap<String, Object> data;
//	ArrayList<Object> tempData;
//
//	@SuppressWarnings("unchecked")
//	@Override
//	public void GetResult(Object jsonDataO, int responce) {
//		try {
//			arrayList = (ArrayList<Object>) jsonDataO;
//			ArrayList<LinkedHashMap<String, Object>> jsonDataTemp = (ArrayList<LinkedHashMap<String, Object>>) jsonDataO;
//			int size = arrayList.size();
//			if (size != 0) {
//				temp = (LinkedHashMap<String, Object>) arrayList.get(0);
//				data= (LinkedHashMap<String, Object>) temp.get("message");
//				if (data.get("success").toString().equalsIgnoreCase("1")) {
//					jsonData = jsonDataTemp;
//
//					switch (responce) {
//					case COUNTRY:
//						CommonVariable.getInstance().setcountry(jsonDataO);
////						try {
////							AppSharedPrefrence.getInstance(SplashScreenActivity.this).setCountry(jsonDataO);
////						} catch (Exception e) {
////							e.printStackTrace();
////						}
//						CallWsState();
//						break;
//					case STATE:
//						CommonVariable.getInstance().setState(jsonDataO);
//						mHandler.sendEmptyMessageDelayed(0, 0);
//						break;
//					default:
//						break;
//					}
//				  } else { CommonUtility.showAlert(data.get("msg").toString(),SplashScreenActivity.this); }
//			} else {
//				CommonUtility.showAlert(getString(R.string.there_is_some_problem_in_getting_data_),SplashScreenActivity.this);
//			}
//		} catch (Exception e) {
//			CommonUtility.showAlert(getString(R.string.there_is_some_problem_in_getting_data_),SplashScreenActivity.this);
//		}
//	}
}
