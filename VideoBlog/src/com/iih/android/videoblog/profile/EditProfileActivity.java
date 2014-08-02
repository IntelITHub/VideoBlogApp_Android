package com.iih.android.videoblog.profile;

import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.Facebook;
import com.iih.android.TwitterData.Twitter_Handler;
import com.iih.android.videoblog.LoginActivity;
import com.iih.android.videoblog.R;
import com.iih.android.videoblog.Parsing.PostDataAndGetData;
import com.iih.android.videoblog.Parsing.UploadPicture;
import com.iih.android.videoblog.Parsing.parseListner;
import com.iih.android.videoblog.commonutill.CommonUtility;
import com.iih.android.videoblog.commonutill.CommonVariable;
import com.iih.android.videoblog.setting.AccountSetting;
import com.iih.android.videoblog.setting.GeneralSetting;
import com.iih.loadwebimage.imageutils.ImageLoader;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebook.OnLogoutListener;

public class EditProfileActivity extends Activity implements parseListner {

	private SimpleFacebook mSimpleFacebook;
	private Twitter_Handler mTwitter;
	public final String consumer_key = "8QrInt4Slc7KiyRc3L4ww";
	public final String secret_key = "dSXcjXQbDTxhNt8yOyhKpcPOZbrRDulVWw9MafoMMQ";

	protected static final String TAG = EditProfileActivity.class.getName();

	private TextView action_title;
	private TextView generalsetting;
	private TextView edtPicture;
	private Button actionleft;
	private Button actionRight;
	private final int CallProfile = 0;
	private final int SaveProfile = 1;
	private final int callLogout = 2;
	private EditText edtName;
	private EditText edtUsername;
	private EditText edtUrlWebsite;
	private EditText edtDescription;
	private EditText edtchngpass;
	private EditText edtEmail;
	private EditText edtPhone;
	private ImageView imgProfilePic;
	private Button btnSave;

	private String strName;
	private String strUsername;
	private String strUrlWebsite;
	private String strDescription;
	private String strchngpass;
	private String strEmail;
	private String strPhone;
	UploadPicture upload;
	private int SELECT_PHOTO = 110;
	private String[] cameraPrompt;
	public ImageLoader imageLoader;

	private TextView txt_Logout;

	private String ipaddress = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editprofile);
	
		cameraPrompt = new String[] { getResources().getString(R.string.from_camera), getResources().getString(R.string.from_gallery) };

		initialization();

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

		generalsetting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(EditProfileActivity.this,GeneralSetting.class);
				startActivity(intent);
				finish();
			}
		});

		actionRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(EditProfileActivity.this,AccountSetting.class);
				startActivity(intent);
				finish();
			}
		});

		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				submitAction();
			}
		});
		edtPicture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showImagePickerDialog(cameraPrompt);
			}
		});
		
		actionleft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		txt_Logout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Logout From Facebook,Twitter and normal
				
				CallwsLogout();
			}
		});
		ClGetProfile();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();

		Intent intent = new Intent(EditProfileActivity.this,ProfileActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		mSimpleFacebook = SimpleFacebook.getInstance(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK && requestCode == 100) {
			upload.setPic(imgProfilePic, data);
		} else if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PHOTO) {
			try {
				imgProfilePic.setImageBitmap(upload.decodeUri(data.getData()));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public void showImagePickerDialog(String[] str) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.myBackgroundStyle);
		builder.setTitle(getResources().getString(R.string.upload_image));
		builder.setIcon(R.drawable.ic_launcher);
		builder.setSingleChoiceItems(str, -1,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							upload = new UploadPicture(EditProfileActivity.this);
							upload.dispatchTakePictureIntent(100);
							break;
						case 1:
							upload = new UploadPicture(EditProfileActivity.this);
							Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
							photoPickerIntent.setType("image/*");
							startActivityForResult(photoPickerIntent,SELECT_PHOTO);
							break;

						default:
							break;
						}
						dialog.dismiss();
					}

				});
		builder.setPositiveButton(getResources().getString(R.string.cancel), null);
		builder.show();
	}

	public void submitAction() {

		strName = edtName.getText().toString().trim();
		strUsername = edtUsername.getText().toString().trim();
		strUrlWebsite = edtUrlWebsite.getText().toString().trim();
		strDescription = edtDescription.getText().toString().trim();
		strchngpass = edtchngpass.getText().toString().trim();
		strEmail = edtEmail.getText().toString().trim();
		strPhone = edtPhone.getText().toString().trim();

		if (isEmpty(strName))
			CommonUtility.showAlert(getString(R.string.please_enter_your_name_),EditProfileActivity.this);
		else if (isEmpty(strUsername))
			CommonUtility.showAlert(getString(R.string.please_enter_user_name_),EditProfileActivity.this);
		else if (isEmpty(strUrlWebsite))
			CommonUtility.showAlert(getString(R.string.please_enter_website_url),EditProfileActivity.this);
		else if (isEmpty(strDescription))
			CommonUtility.showAlert(getString(R.string.please_enter_description),EditProfileActivity.this);
//		else if (isEmpty(strchngpass))
//			CommonUtility.showAlert(getString(R.string.please_enter_password_),EditProfileActivity.this);
//		else if (strchngpass.length() < 6)
//			CommonUtility.showAlert(getString(R.string.password_must_be_more_then_6_character_),EditProfileActivity.this);
		else if (isEmpty(strEmail))
			CommonUtility.showAlert(getString(R.string.please_enter_email_address),EditProfileActivity.this);
		else if (!CommonUtility.checkingEmail(strEmail))
			CommonUtility.showAlert(getString(R.string.please_enter_valid_email),EditProfileActivity.this);
		else if (isEmpty(strPhone))
			CommonUtility.showAlert(getString(R.string.please_enter_phone_no),EditProfileActivity.this);
		else {
			ClSaveProfile();
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

	private void initialization() {
		action_title = (TextView) findViewById(R.id.action_title);
		action_title.setText(R.string.edit_profile);

		actionleft = (Button) findViewById(R.id.action_Left);
		actionleft.setBackgroundResource(R.drawable.actionbar_back);

		actionRight = (Button) findViewById(R.id.action_right);

		generalsetting = (TextView) findViewById(R.id.GeneralSetting);

		edtName = (EditText) findViewById(R.id.name);
		edtUsername = (EditText) findViewById(R.id.Username);
		edtUrlWebsite = (EditText) findViewById(R.id.url_website);
		edtDescription = (EditText) findViewById(R.id.description);
		edtchngpass = (EditText) findViewById(R.id.chngpass);
		edtEmail = (EditText) findViewById(R.id.Email);
		edtPhone = (EditText) findViewById(R.id.phone);
		imgProfilePic = (ImageView) findViewById(R.id.profilePic);
		btnSave = (Button) findViewById(R.id.btn_save);
		edtPicture = (TextView) findViewById(R.id.editpicture);
		imageLoader=new ImageLoader(getApplicationContext());

		txt_Logout = (TextView)findViewById(R.id.Logout);
	}

	/**
	 * Calling the web-service For Profile Details
	 */
	HashMap<String, String> Paramter;
	PostDataAndGetData psd;
	HashMap<String, String> Parameter = null;

	public void ClGetProfile() {
		Parameter = new HashMap<String, String>();

		// commonVariable=CommonVariable.getInstance();

		String member_id = CommonVariable.getInstance().getMember_id();

		// getProfileDetails&iMemberId=71

		Parameter.put("iMemberId", member_id);

		psd = new PostDataAndGetData(EditProfileActivity.this, null, Parameter,
				"POST", CommonUtility.BaseUrl2 + "getProfileDetails",CallProfile, 0, false, true);
		// if (upload != null) {
		// psd.setFileUpload(true);
		// HashMap<String, String> fileArray = new HashMap<String, String>();
		// fileArray.put("userfile", upload.mCurrentPhotoPath);
		// psd.setFile(fileArray);
		// }
		psd.execute();
	}

	/**
	 * Calling the web-service For Save Profile
	 */
	// HashMap<String, String> Paramter;
	// PostDataAndGetData psd;
	// HashMap<String, String> Parameter = null;
	public void ClSaveProfile() {
		Parameter = new HashMap<String, String>();


		String member_id =CommonVariable.getInstance().getMember_id();

		// getProfileDetails&iMemberId=71

		Parameter.put("iMemberId", member_id);
		Parameter.put("vName", strName);
		Parameter.put("vEmail", strEmail);
		Parameter.put("vURL", strUrlWebsite);
		Parameter.put("vPhone", strPhone);
		Parameter.put("tDescription", strDescription);
		Parameter.put("vPassword", strchngpass);
		Parameter.put("vUsername", strUsername);

		psd = new PostDataAndGetData(EditProfileActivity.this, null, Parameter,
				"POST", CommonUtility.BaseUrl2 + "setProfileDetails",SaveProfile, 0, false, true);

		if (upload != null) {
//			psd.setFileUpload(true);
			psd.isFilePost(true);
			HashMap<String, String> fileArray = new HashMap<String, String>();
			fileArray.put("vPicture", upload.mCurrentPhotoPath);
			psd.setFile(fileArray);
		}
		
		psd.execute();
	}

	public void CallwsLogout() {

		String member_id = CommonVariable.getInstance().getMember_id();


		psd = new PostDataAndGetData(EditProfileActivity.this, null, null,
				"GET", CommonUtility.BaseUrl2 + "userLogout" + "&iMemberId="+ member_id + "&vIP=" + ipaddress ,callLogout, 0, false, true);

		
		try {
			psd.execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private OnLogoutListener mOnLogoutListener = new OnLogoutListener()
	{

		@Override
		public void onFail(String reason)
		{
			Log.w(TAG, "Failed to login");
		}

		@Override
		public void onException(Throwable throwable)
		{
			Log.e(TAG,"Exception: " + throwable.getMessage());
			Log.e(TAG, "Bad thing happened", throwable);
		}

		@Override
		public void onThinking()
		{
			// show progress bar or something to the user while login is happening
		}

		@Override
		public void onLogout()
		{
			// change the state of the button or do whatever you want
			//Toast.makeText(EditProfileActivity.this, "Facebook Logout Successfully.", Toast.LENGTH_LONG).show();
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
				
				switch (responce) {
				case CallProfile:
					data=(LinkedHashMap<String, Object>) temp.get("data");
					edtName.setText(data.get("vName").toString());
					edtUsername.setText(data.get("vUsername").toString());
					edtUrlWebsite.setText(data.get("vURL").toString());
					edtDescription.setText(data.get("tDescription").toString());
					edtEmail.setText(data.get("vEmail").toString());
					edtPhone.setText(data.get("vPhone").toString());
//					CommonUtility.loadImage(new AQuery(EditProfileActivity.this),temp.get("vPicture").toString(), imgProfilePic);
					imageLoader.DisplayImage(data.get("vPicture").toString(), imgProfilePic);
					break;

				case SaveProfile:

					showAlert(data.get("msg").toString(),EditProfileActivity.this);

					break;

				case callLogout:

					//Facebook fb = new Facebook("270774929751562");
					
					mSimpleFacebook.logout(mOnLogoutListener);

					//mSimpleFacebook.clean();

					mTwitter = new Twitter_Handler(EditProfileActivity.this, consumer_key,secret_key);
					mTwitter.resetAccessToken();

					//showAlert(data.get("msg").toString(),EditProfileActivity.this);

					Intent intent = new Intent(EditProfileActivity.this,LoginActivity.class);
					startActivity(intent);
					finish();

					break;

				default:
					break;
				}
				 } else {
				 CommonUtility.showAlert(data.get("msg").toString(), EditProfileActivity.this);
				 }
			} else {
				CommonUtility.showAlert(getString(R.string.there_is_some_problem_in_getting_data_),EditProfileActivity.this);
			}
		} catch (Exception e) {
			CommonUtility.showAlert(getString(R.string.there_is_some_problem_in_getting_data_), EditProfileActivity.this);
		}
	}

	public void showAlert(String message, Activity act) {
		Builder builder = new AlertDialog.Builder(act);
		builder.setTitle(getString(R.string.message));
		builder.setIcon(R.drawable.ic_launcher);
		builder.setMessage(message);
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				finish();
			}
		});
		builder.show();
	}
}
