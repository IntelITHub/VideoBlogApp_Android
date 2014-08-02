package com.iih.android.videoblog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iih.android.TwitterData.Twitter_Handler;
import com.iih.android.TwitterData.Twitter_Handler.TwDialogListener;
import com.iih.android.videoblog.Parsing.PostDataAndGetData;
import com.iih.android.videoblog.Parsing.UploadPicture;
import com.iih.android.videoblog.Parsing.parseListner;
import com.iih.android.videoblog.commonutill.AppSharedPrefrence;
import com.iih.android.videoblog.commonutill.CommonUtility;
import com.iih.android.videoblog.commonutill.CommonVariable;
import com.iih.android.videoblog.gallery.PostSettingActivity;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebook.OnLoginListener;
import com.sromku.simple.fb.SimpleFacebook.OnProfileRequestListener;
import com.sromku.simple.fb.entities.Profile;

public class RegisterActivity extends Activity implements parseListner {

	public static SimpleFacebook mSimpleFacebook;
	private Twitter_Handler mTwitter;

	// Twitter Auth Key
//	public final String consumer_key = "k1FNrBhSrQv5SkU2bzaLg";
//	public final String secret_key = "PukXNGxeLDXuwUjnQV0ULtOQ64hXvKuBeQPXGVfLmKg";

	public final String consumer_key = "ct3IXFnfUicdvFPbVGN5jg";
	public final String secret_key = "AD0RAHWWaJv9gf0GZsXlNgPNMNCywkSdVvr5upDVHg";

	private String TAG = "RegisterActivity";

	private TextView actiontitle;
	private Button btn_signup;
	private Button btnTakepic;

	private Button btn_Facebook;
	private Button btn_twitter;

	private Button actionleft;
	private Button actionRight;
	private final int ResponseCode = 0;
	private final int FBResponseCode = 1;
	private final int TwitterResponseCode = 2;
	
	private EditText edtYourname;
	private EditText edtUsername;
	private EditText edtEmail;
	private EditText edtPassword;
	private EditText edtRepetepass;
	private String yourname;
	private String username;
	private String email;
	private String password;
	private String reptPass;
	private UploadPicture upload;
	private ImageView imgProfilePic;
	private int SELECT_PHOTO = 110;
	private String[] cameraPrompt;
	private Bitmap bmp;
	private String strUID = "";
	private Button btnRotateLeft;
	private Button btnRotateRight;
	
	private String mFbToken;

	private URL image_value;
	private String android_id;

	private String ipaddress = "";

	private boolean flagleft = false;
	private boolean flagRight = false;

	int currentRotation = 0;

	private Uri fileUri; // file url to store image

	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	public static final int MEDIA_TYPE_IMAGE = 1;
	private static final String IMAGE_DIRECTORY_NAME = "VideoBlog";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registration);

		cameraPrompt = new String[] { getResources().getString(R.string.from_camera), getResources().getString(R.string.from_gallery) };

		initialization();

		if (Build.VERSION.SDK_INT >= 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						ipaddress = inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (Exception ex) {
			Log.e("IP Address", ex.toString());
		}

		btn_signup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent intent = new
				// Intent(RegisterActivity.this,Termsandcondition.class);
				// startActivity(intent);
				submitAction();
			}
		});

		btnTakepic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showImagePickerDialog(cameraPrompt);
			}
		});

		btn_Facebook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mSimpleFacebook.login(mFBLoginListener);
			}
		});

		btn_twitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mTwitter = new Twitter_Handler(RegisterActivity.this,consumer_key, secret_key);
				mTwitter.setListener(twitterLoginDialogListener);

				if (mTwitter.hasAccessToken()) {
					// this will post data in asyn background thread
					// showTwittDialog();
					CallWsTwitterRegister();
					/*Intent intent = new Intent(RegisterActivity.this,Termsandcondition.class);
					startActivity(intent);
					finish();*/
				} else {
					mTwitter.authorize();
				}
			}
		});

		btnRotateLeft.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				imgProfilePic.setBackground(null);
				imgProfilePic.setImageBitmap(bmp);

				new RotateLeftAsyncTask(RegisterActivity.this).execute();
			}
		});

		btnRotateRight.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				imgProfilePic.setBackground(null);
				imgProfilePic.setImageBitmap(bmp);

				new RotateRightAsyncTask(RegisterActivity.this).execute();
			}
		});
		btnRotateLeft.setVisibility(View.GONE);
		btnRotateRight.setVisibility(View.GONE);

		TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		android_id = tm.getDeviceId();

	}


	private int getImageOrientation() {
		final String[] imageColumns = { MediaStore.Images.Media._ID,
				MediaStore.Images.ImageColumns.ORIENTATION };
		final String imageOrderBy = MediaStore.Images.Media._ID + " DESC";
		Cursor cursor = getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageColumns,
				null, null, imageOrderBy);

		if (cursor.moveToFirst()) {
			int orientation = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION));
			cursor.close();
			return orientation;
		} else {
			return 0;
		}
	}

	private class RotateLeftAsyncTask extends AsyncTask<Void, Void, Void> {
		private ProgressDialog bar;
		Activity act;

		public RotateLeftAsyncTask(Activity activity) {
			act =activity;
		}

		protected Void doInBackground(Void... args) {
			// do background work here

			// 1. figure out the amount of degrees
			// int rotation = getImageRotation();
			int rotation = getImageOrientation();

			// 2. rotate matrix by postconcatination
			Matrix matrix = new Matrix();

//			if(flagleft == false){
//				matrix.postRotate(rotation - 90);
//				flagleft = true;
//			}else{
//				matrix.postRotate(rotation + 90);
//				flagleft = false;
//			}

			if(currentRotation == -360){
				currentRotation = 0;
			}

			currentRotation = currentRotation - 90 ;
			matrix.postRotate(currentRotation);

			// 3. create Bitmap from rotated matrix
			Bitmap sourceBitmap = BitmapFactory.decodeFile(CommonVariable.getInstance().getRegistrationFilePath());
			bmp = Bitmap.createBitmap(sourceBitmap, 0, 0,sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix,true);

			// Bitmap bmp = createBitmap();
			OutputStream stream = null;
			try {
				stream = new FileOutputStream(CommonVariable.getInstance().getRegistrationFilePath());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			bmp.compress(CompressFormat.JPEG, 100, stream);

			try {
				stream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPreExecute() {

			try {
						// TODO Auto-generated method stub
						bar = new ProgressDialog(RegisterActivity.this,ProgressDialog.THEME_HOLO_LIGHT);
						bar.setIndeterminate(true);
						bar.setTitle(act.getString(R.string.rotating_));
						bar.setIcon(act.getResources().getDrawable(R.drawable.ic_launcher));
						bar.show();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			super.onPreExecute();
		}

		@SuppressLint("NewApi")
		protected void onPostExecute(Void result) {

			// do UI work here
			imgProfilePic.setBackground(null);
			imgProfilePic.setImageBitmap(bmp);
			try {
				bar.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private class RotateRightAsyncTask extends AsyncTask<Void, Void, Void> {
		private ProgressDialog bar;
		Activity act;

		public RotateRightAsyncTask(Activity activity) {
			act =activity;
		}

		protected Void doInBackground(Void... args) {
			// do background work here

			// 1. figure out the amount of degrees
			// int rotation = getImageRotation();
			int rotation = getImageOrientation();

			// 2. rotate matrix by postconcatination
			Matrix matrix = new Matrix();

//			if(flagRight == false){
//				matrix.postRotate(rotation + 90);
//				flagRight = true;
//			}else{
//				matrix.postRotate(rotation - 90);
//				flagRight = false;
//			}

			if(currentRotation == 360){
				currentRotation = 0;
			}

			currentRotation = currentRotation + 90 ;
			matrix.postRotate(currentRotation);
		
			// 3. create Bitmap from rotated matrix
			Bitmap sourceBitmap = BitmapFactory.decodeFile(CommonVariable.getInstance().getRegistrationFilePath());
			bmp = Bitmap.createBitmap(sourceBitmap, 0, 0,sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix,true);

			// Bitmap bmp = createBitmap();
			OutputStream stream = null;
			try {
				stream = new FileOutputStream(CommonVariable.getInstance().getRegistrationFilePath());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			bmp.compress(CompressFormat.JPEG, 100, stream);

			try {
				stream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPreExecute() {

			
			try {
						// TODO Auto-generated method stub
						bar = new ProgressDialog(RegisterActivity.this,ProgressDialog.THEME_HOLO_LIGHT);
						bar.setIndeterminate(true);
						bar.setTitle(act.getString(R.string.rotating_));
						bar.setIcon(act.getResources().getDrawable(R.drawable.ic_launcher));
						bar.show();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			super.onPreExecute();
		}

		@SuppressLint("NewApi")
		protected void onPostExecute(Void result) {

			// do UI work here
			imgProfilePic.setBackground(null);
			imgProfilePic.setImageBitmap(bmp);
			try {
				bar.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSimpleFacebook = SimpleFacebook.getInstance(this);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		CustomeAlertYesNo customedialog = new CustomeAlertYesNo(RegisterActivity.this);
//		customedialog.show();
		super.onBackPressed();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {

//			upload.setPic(imgProfilePic, data);

			String path = fileUri.getPath();
			CommonVariable.getInstance().setRegistrationFilePath(path);


			try {
				File f = new File(CommonVariable.getInstance().getRegistrationFilePath());
				ExifInterface exif = new ExifInterface(f.getPath());
				int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);

				int angle = 0;

				if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
					angle = 90;
				} else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
					angle = 180;
				} else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
					angle = 270;
				}

				Matrix mat = new Matrix();
				mat.postRotate(angle);
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 2;

				Bitmap bmp1 = BitmapFactory.decodeStream(new FileInputStream(f), null, options);

				bmp = Bitmap.createBitmap(bmp1, 0, 0, bmp1.getWidth(),bmp1.getHeight(), mat, true);
				OutputStream stream = new FileOutputStream(CommonVariable.getInstance().getRegistrationFilePath());

				bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);

				imgProfilePic.setImageBitmap(bmp);

				stream.close();

			} catch (IOException e) {
				Log.w("TAG", "-- Error in setting image");
			} catch (OutOfMemoryError oom) {
				Log.w("TAG", "-- OOM Error in setting image");
			}

			btnRotateLeft.setVisibility(View.VISIBLE);
			btnRotateRight.setVisibility(View.VISIBLE);

		} else if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PHOTO) {
			try {
				imgProfilePic.setImageBitmap(upload.decodeUri(data.getData()));

				CommonVariable.getInstance().setRegistrationFilePath(upload.mCurrentPhotoPath);

				btnRotateLeft.setVisibility(View.VISIBLE);
				btnRotateRight.setVisibility(View.VISIBLE);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private void initialization() {
		actiontitle = (TextView) findViewById(R.id.action_title);
		actiontitle.setText(R.string.register);

		actionleft = (Button) findViewById(R.id.action_Left);
		actionleft.setVisibility(View.INVISIBLE);

		actionRight = (Button) findViewById(R.id.action_right);
		actionRight.setVisibility(View.INVISIBLE);

		btn_signup = (Button) findViewById(R.id.btn_signup);
		btnTakepic = (Button) findViewById(R.id.btn_takeapicture);

		btn_Facebook = (Button) findViewById(R.id.btn_facebook);
		btn_twitter = (Button) findViewById(R.id.btn_twitter);

		edtYourname = (EditText) findViewById(R.id.edt_yourname);
		edtUsername = (EditText) findViewById(R.id.edt_username);
		edtEmail = (EditText) findViewById(R.id.edt_email);
		edtPassword = (EditText) findViewById(R.id.edt_password);
		edtRepetepass = (EditText) findViewById(R.id.edt_rptpassword);
		imgProfilePic = (ImageView) findViewById(R.id.img_profilepic);
		btnRotateLeft = (Button) findViewById(R.id.btn_rotateLeft);
		btnRotateRight = (Button) findViewById(R.id.btn_rotateRight);
	}

	public void submitAction() {

		yourname = edtYourname.getText().toString().trim();
		email = edtEmail.getText().toString().trim();
		username = edtUsername.getText().toString().trim();
		password = edtPassword.getText().toString().trim();
		reptPass = edtRepetepass.getText().toString().trim();

		if (isEmpty(yourname))
			CommonUtility.showAlert(getString(R.string.please_enter_your_name_),RegisterActivity.this);
		else if (isEmpty(username))
			CommonUtility.showAlert(getString(R.string.please_enter_user_name_),RegisterActivity.this);
		else if (isEmpty(email))
			CommonUtility.showAlert(getString(R.string.please_enter_email_address),RegisterActivity.this);
		else if (!CommonUtility.checkingEmail(email))
			CommonUtility.showAlert(getString(R.string.please_enter_valid_email),RegisterActivity.this);
		else if (isEmpty(password))
			CommonUtility.showAlert(getString(R.string.please_enter_password_),RegisterActivity.this);
		else if (password.length() < 6)
			CommonUtility.showAlert(getString(R.string.password_must_be_more_then_6_character_),RegisterActivity.this);
		else if (!reptPass.equalsIgnoreCase(password))
			CommonUtility.showAlert(getString(R.string.password_and_repeat_password_should_be_same_),RegisterActivity.this);
		else {
				CallWsRegister();
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
							//upload = new UploadPicture(RegisterActivity.this);
							//upload.dispatchTakePictureIntent(100);
							captureImage();
							break;
						case 1:
							upload = new UploadPicture(RegisterActivity.this);
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

	private void captureImage() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);


		// start the image capture Intent
		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	}

	/*
	 * Here we store the file url as it will be null after returning from camera
	 * app
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// save file url in bundle as it will be null on scren orientation
		// changes
		outState.putParcelable("file_uri", fileUri);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		// get the file url
		fileUri = savedInstanceState.getParcelable("file_uri");
	}

	/*
	 * Creating file uri to store image/video
	 */
	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/*
	 * returning image / video
	 */
	private static File getOutputMediaFile(int type) {

		// External sdcard location
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),IMAGE_DIRECTORY_NAME);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "+ IMAGE_DIRECTORY_NAME + " directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()).format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator+ "IMG_" + timeStamp + ".jpg");
//		} else if (type == MEDIA_TYPE_VIDEO) {
//			mediaFile = new File(mediaStorageDir.getPath() + File.separator+ "VID_" + timeStamp + ".mp4");
		} else {
			return null;
		}
		return mediaFile;
	}

	/**
	 * Calling the web-service
	 */
	HashMap<String, String> Paramter;
	PostDataAndGetData psd;
	HashMap<String, String> Parameter = null;

	public void CallWsRegister() {
		Parameter = new HashMap<String, String>();

		Parameter.put("vEmail", email);
		Parameter.put("vName", yourname);
		Parameter.put("vPassword", password);
		Parameter.put("vUsername", username);
		Parameter.put("eLoginType", "REGISTER");
		Parameter.put("vType", "Android");
		Parameter.put("tDeviceToken", CommonVariable.getInstance().getGCM_id());
		Parameter.put("vDeviceid",android_id);
		Parameter.put("vDevicename",android.os.Build.MODEL);
		Parameter.put("vIP", ipaddress);
		Parameter.put("iLangId", AppSharedPrefrence.getInstance(RegisterActivity.this).getLanguageID());
		

		psd = new PostDataAndGetData(RegisterActivity.this, null, Parameter,
				"POST", CommonUtility.BaseUrl2 + "userRegistration",ResponseCode, 0, false, true);
		if (upload != null) {

			// psd.setFileUpload(true);
			psd.isFilePost(true);
			HashMap<String, String> fileArray = new HashMap<String, String>();
			//fileArray.put("vPicture", upload.mCurrentPhotoPath);
			fileArray.put("vPicture", AppSharedPrefrence.getInstance(RegisterActivity.this).getFilePath());
			psd.setFile(fileArray);
		}
		try {
			psd.execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void CallWsFBRegister() {
		Parameter = new HashMap<String, String>();

		Parameter.put("vName", yourname);
		Parameter.put("vPicture",""+image_value);
//		Parameter.put("vPassword", password);
		Parameter.put("vUsername", username);
		Parameter.put("eLoginType", "FACEBOOK");
		Parameter.put("vFacebookId", strUID);
		Parameter.put("tFacebookToken", mFbToken);
		Parameter.put("vType", "Android");
		Parameter.put("tDeviceToken ", CommonVariable.getInstance().getGCM_id());
		Parameter.put("vDeviceid",android_id);
		Parameter.put("vDevicename",android.os.Build.MODEL);
		Parameter.put("vIP", ipaddress);
		Parameter.put("iLangId", AppSharedPrefrence.getInstance(RegisterActivity.this).getLanguageID());
		

		psd = new PostDataAndGetData(RegisterActivity.this, null, Parameter,
				"POST", CommonUtility.BaseUrl2 + "userRegistration",
				FBResponseCode, 0, false, true);

		try {
			psd.execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void CallWsTwitterRegister() {
		Parameter = new HashMap<String, String>();

//		Parameter.put("vPassword", password);
		Parameter.put("vPicture", mTwitter.getUProPic());
//		Parameter.put("vEmail", mTwitter.getUsername());
		Parameter.put("vName", mTwitter.getUsername());
		Parameter.put("vUsername", mTwitter.getScreenname());
		Parameter.put("eLoginType", "TWITTER");
		Parameter.put("vTwitterId", ""+mTwitter.getToken());
		Parameter.put("tTwitterToken", mTwitter.getTokenSecret());
		Parameter.put("vType", "Android");
		Parameter.put("tDeviceToken", CommonVariable.getInstance().getGCM_id());
		Parameter.put("vDeviceid",android_id);
		Parameter.put("vDevicename",android.os.Build.MODEL);
		Parameter.put("vIP", ipaddress);
		Parameter.put("iLangId", AppSharedPrefrence.getInstance(RegisterActivity.this).getLanguageID());
//		1) vTwitterId (Access token)
//		2) tTwitterToken (Access token secret)
		psd = new PostDataAndGetData(RegisterActivity.this, null, Parameter,
				"POST", CommonUtility.BaseUrl2 + "userRegistration",
				TwitterResponseCode, 0, false, true);

		try {
			psd.execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * Web-service Response
	 */

	LinkedHashMap<String, Object> temp;
	ArrayList<LinkedHashMap<String, Object>> jsonData;
	ArrayList<Object> arrayList;
	LinkedHashMap<String, Object> data;
	Intent intent;

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
					
					case ResponseCode:
						data=(LinkedHashMap<String, Object>) temp.get("data");
						CommonVariable.getInstance().setMember_id(data.get("iMemberId").toString());

						intent = new Intent(RegisterActivity.this,Termsandcondition.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
						startActivity(intent);
						finish();
						break;
					case FBResponseCode:

						data=(LinkedHashMap<String, Object>) temp.get("data");
						CommonVariable.getInstance().setMember_id(data.get("iMemberId").toString());
						Toast.makeText(RegisterActivity.this,"Logged In successfully", Toast.LENGTH_LONG).show();
						intent = new Intent(RegisterActivity.this,Termsandcondition.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
						startActivity(intent);
						finish();
						break;

					case TwitterResponseCode:

						try {
							data=(LinkedHashMap<String, Object>) temp.get("data");
							CommonVariable.getInstance().setMember_id(data.get("iMemberId").toString());

							intent = new Intent(RegisterActivity.this, Termsandcondition.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
							startActivity(intent);
							finish();
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;

					default:
						break;
					}
				} else {
					CommonUtility.showAlert(data.get("msg").toString(),RegisterActivity.this);
				}
			} else {
				CommonUtility.showAlert(getString(R.string.there_is_some_problem_in_getting_data_), RegisterActivity.this);
			}
		} catch (Exception e) {
			CommonUtility.showAlert(getString(R.string.there_is_some_problem_in_getting_data_), RegisterActivity.this);
		}

	}

	// Login with facebook
	public OnLoginListener mFBLoginListener = new OnLoginListener() {

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
//					strPicture=profile.getPicture();
					
					
					try {
						image_value = new URL("http://graph.facebook.com/"+strUID+"/picture") ;
						Log.d("profle Pic",""+image_value);
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}

					CallWsFBRegister();
				}
			});

		}

		@Override
		public void onNotAcceptingPermissions() {
			Toast.makeText(RegisterActivity.this,"You didn't accept read permissions", Toast.LENGTH_LONG).show();
		}
	};

	// Logion with Twitter
	private final TwDialogListener twitterLoginDialogListener = new TwDialogListener() {

		@Override
		public void onError(String value) {
			Toast.makeText(RegisterActivity.this, getString(R.string.login_failed),Toast.LENGTH_LONG).show();
			mTwitter.resetAccessToken();
		}

		@Override
		public void onComplete(String value) {
			// showTwittDialog();
			CallWsTwitterRegister();
//			Intent intent = new Intent(RegisterActivity.this,Termsandcondition.class);
//			startActivity(intent);
//			finish();
		}
	};

}
