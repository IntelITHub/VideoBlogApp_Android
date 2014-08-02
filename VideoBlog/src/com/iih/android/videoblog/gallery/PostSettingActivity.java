package com.iih.android.videoblog.gallery;

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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.iih.android.TwitterData.Twitter_Handler;
import com.iih.android.TwitterData.Twitter_Handler.TwDialogListener;
import com.iih.android.videoblog.LoginActivity;
import com.iih.android.videoblog.R;
import com.iih.android.videoblog.Parsing.PostDataAndGetData;
import com.iih.android.videoblog.Parsing.UploadPicture;
import com.iih.android.videoblog.Parsing.parseListner;
import com.iih.android.videoblog.commonutill.AppSharedPrefrence;
import com.iih.android.videoblog.commonutill.CommonUtility;
import com.iih.android.videoblog.commonutill.CommonVariable;
import com.iih.android.videoblog.newsfeed.NewsFeedScreen;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebook.OnLoginListener;
import com.sromku.simple.fb.SimpleFacebook.OnProfileRequestListener;
import com.sromku.simple.fb.entities.Profile;

public class PostSettingActivity extends Activity implements parseListner {

	protected static final String TAG = PostSettingActivity.class.getName();

	private TextView action_title;

	// Twitter Auth Key
//	public final String consumer_key = "k1FNrBhSrQv5SkU2bzaLg";
//	public final String secret_key = "PukXNGxeLDXuwUjnQV0ULtOQ64hXvKuBeQPXGVfLmKg";

	public final String consumer_key = "ct3IXFnfUicdvFPbVGN5jg";
	public final String secret_key = "AD0RAHWWaJv9gf0GZsXlNgPNMNCywkSdVvr5upDVHg";

	private Button actionleft;
	private Button actionRight;

	private EditText edt_Headline;
	private EditText edt_Description;
	private ImageView img_PostImage;
	private TextView txt_categoryName;
	private TextView txt_Addtomap;
	private EditText txt_county;
	private EditText txt_state;
	private EditText txt_City;

	private String strCounty = "";
	private String strState ="";
	private String strCity = "";

	private String Headline = "";
	private String Description ="";

	private Button btn_Share;

	private String android_id;

	private final int PostSettingRcode = 0,CatagoryRcode = 1,FBResponseCode=2,TwtResponseCode=3;

	private AlertDialog catalertDialog = null;
	//private AlertDialog StateCountyalertDialog = null;

	UploadPicture upload;

	private Twitter_Handler mTwitter;

	private String[] LatLong;
	private String Latitude = "";
	private String Longitude = "";

	private ListView Categorylistview;
//	private ListView Countrylistview;
//	private ListView Statelistview;
//
//	private String SelectedCountyID;
//
//	private String SelectedStateID;
//
	private String SelectedCategory;
	private String SelectedCategoryId;

	private Button btnRotateLeft;
	private Button btnRotateRight;

	private SimpleFacebook mSimpleFacebook;

	private ToggleButton toggleButton_FB;
	private ToggleButton toggleButton_Twitter;

	ArrayList<String> CategoryName = new ArrayList<String>();
	ArrayList<String> CategoryId = new ArrayList<String>();
//
//	ArrayList<String> CountyName = new ArrayList<String>();
//	ArrayList<String> CountryID = new ArrayList<String>();
//
//	ArrayList<String> StateName = new ArrayList<String>();
//	ArrayList<String> StateId = new ArrayList<String>();
//
//	ArrayList<Object> StateCountryJSONData;
//
//	LinkedHashMap<String, Object> StateCountyTemp;
//	LinkedHashMap<String, Object> StateCountyData;
//	ArrayList<Object> tempCountryState;
//
	CatgeoryListAdapter categoryadapter;
//	CountyListAdapter Countryadapter;
//	StateListAdapter stateadapter;
//
//	TextWatcherCountryState myTextWatcherCountyState;


	private Bitmap bmp;

	private String username;
	private String yourname;
	private String strUID = "";
	private URL image_value;
	private String mFbToken;
//	private String mShareType = "REGISTER";
	private String mSharefecebook = "";
	private String mShareTwitter = "";

	double lat = 0;
	double Long = 0;

	private String ipaddress = "";

	private boolean flagleft = false;
	private boolean flagRight = false;

	int currentRotation = 0;

	/**
	 * Calling the web-service
	 */
	HashMap<String, String> Paramter;
	PostDataAndGetData psd;
	HashMap<String, String> Parameter = null;
	

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.postsettings);

		LatLong = CommonUtility.getLatAndLong(PostSettingActivity.this);
		Latitude = LatLong[0];
		Longitude = LatLong[1];

		try {
			lat = Double.parseDouble(Latitude);
			Long = Double.parseDouble(Longitude);
		} catch (NumberFormatException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
//		double lat = Double.valueOf(Latitude);
//		double Long = Double.valueOf(Longitude);

		try {
			Geocoder geocoder;
			List<Address> addresses;
			geocoder = new Geocoder(this, Locale.getDefault());
			addresses = geocoder.getFromLocation(lat, Long, 1);

			//String address = addresses.get(0).getAddressLine(0);
			//String city = addresses.get(0).getAddressLine(1);
			//String newstring = city.replaceAll(",","");
			//String country = addresses.get(1).getAddressLine(2);

			try {
				if (addresses != null && addresses.size() >= 0) {
					strCity = addresses.get(0).getLocality();
					strCounty = addresses.get(0).getCountryName();
					strState = addresses.get(0).getAdminArea();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		initialization();

		//check Device Ip Address
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


		actionleft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PostSettingActivity.this,CustomGalleryActivity.class);
				startActivity(intent);
				finish();
			}
		});

		btn_Share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Headline =edt_Headline.getText().toString().trim();
				Description = edt_Description.getText().toString().trim();
				Log.e("Headline","Headline "+Headline);
				if(isEmpty(Headline)){
					CommonUtility.showAlert(getString(R.string.please_enter_post_headline_), PostSettingActivity.this);
				}else if(isEmpty(SelectedCategory)){
					CommonUtility.showAlert(getString(R.string.please_select_category_), PostSettingActivity.this);
				//}
				//else if(isEmpty(Latitude)){
					//CommonUtility.showAlert("Please Select Loaction.", PostSettingActivity.this);
//				}else if(isEmpty(City)){
//					CommonUtility.showAlert("Please Enter City.", PostSettingActivity.this);
				}else{

					if (AppSharedPrefrence.getInstance(PostSettingActivity.this).getFileType().equalsIgnoreCase("Video")) {
						MediaPlayer mp = MediaPlayer.create(PostSettingActivity.this, Uri.parse(AppSharedPrefrence.getInstance(PostSettingActivity.this).getFilePath()));
						int duration = mp.getDuration();
						mp.release();

//						String durationCOnv = String.format("%d min, %d sec",
//								TimeUnit.MILLISECONDS.toMinutes(duration),
//								TimeUnit.MILLISECONDS.toSeconds(duration)- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));

						int durationMinute = Integer.parseInt(String.format("%d",TimeUnit.MILLISECONDS.toMinutes(duration)));

						//check video Duration
						if(durationMinute > 1){
							CommonUtility.showAlert(getString(R.string.video_duration_is_more_than_1_minute_), PostSettingActivity.this);
						}else{
							CallWsPostSetting();
						}
					}else{
						
						CallWsPostSetting();
					}
					//

//					File file = new File(CommonVariable.getInstance().getFilePath());
//					long length = file.length();
//					length = length / 1024;
//
//					if(length < 15360){
//
//					//CallWsPostSetting();
//					}else{
//						CommonUtility.showAlert("Video Size is More than 15 MB", PostSettingActivity.this);
//					}
				}
			}
		});

		txt_Addtomap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//Getting current latitude and longitude
				if(AppSharedPrefrence.getInstance(PostSettingActivity.this).getIsLocation()){
					LatLong = CommonUtility.getLatAndLong(PostSettingActivity.this);
					Latitude = LatLong[0];
					Longitude = LatLong[1];
					txt_Addtomap.setText(Latitude + "," + Longitude);
				}else{
					locationDialog();
				}
			}
		});

		txt_categoryName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SetCategoriesDialogue();
			}
		});

//		txt_county.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				//SetCountyDialogue();
//			}
//		});

//		txt_state.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if(isEmpty(SelectedCountyID)){
//					Toast.makeText(PostSettingActivity.this, R.string.please_select_country_first_,Toast.LENGTH_LONG).show();
//				}else{
//					SetStateDialogue();
//				}
//				
//			}
//		});

		btnRotateLeft.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				//jignesh
				img_PostImage.setBackground(null);
				img_PostImage.setImageBitmap(bmp);

				new RotateLeftAsyncTask(PostSettingActivity.this).execute();

			}
		});

		btnRotateRight.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				//jignesh
				img_PostImage.setBackground(null);
				img_PostImage.setImageBitmap(bmp);

				new RotateRightAsyncTask(PostSettingActivity.this).execute();

			}
		});

		/**
		 * set thumbnails to imageview
		 */
		try {

			if (AppSharedPrefrence.getInstance(PostSettingActivity.this).getFileType().equalsIgnoreCase("Image")) {
				btnRotateLeft.setVisibility(View.VISIBLE);
				btnRotateRight.setVisibility(View.VISIBLE);
				Bitmap bmp ;//= BitmapFactory.decodeFile(CommonVariable.getInstance().getFilePath());
				bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(AppSharedPrefrence.getInstance(PostSettingActivity.this).getFilePath()),250, 250, true);
				img_PostImage.setImageBitmap(bmp);

//				try {
//					File f = new File(CommonVariable.getInstance().getRegistrationFilePath());
//					ExifInterface exif = new ExifInterface(f.getPath());
//					int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
//
//					int angle = 0;
//
//					if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
//						angle = 90;
//					} else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
//						angle = 180;
//					} else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
//						angle = 270;
//					}
//
//					Matrix mat = new Matrix();
//					mat.postRotate(angle);
//					BitmapFactory.Options options = new BitmapFactory.Options();
//					options.inSampleSize = 2;
//
//					Bitmap bmp1 = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
//
//					bmp = Bitmap.createBitmap(bmp1, 0, 0, bmp1.getWidth(),bmp1.getHeight(), mat, true);
//					OutputStream stream = new FileOutputStream(CommonVariable.getInstance().getRegistrationFilePath());
//
//					//bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
//
//					img_PostImage.setImageBitmap(bmp);
//	
//				} catch (IOException e) {
//					Log.w("TAG", "-- Error in setting image");
//				} catch (OutOfMemoryError oom) {
//					Log.w("TAG", "-- OOM Error in setting image");
//				}

				
				
			} else {
				btnRotateLeft.setVisibility(View.GONE);
				btnRotateRight.setVisibility(View.GONE);
				Bitmap bitmap =ThumbnailUtils.createVideoThumbnail(AppSharedPrefrence.getInstance(PostSettingActivity.this).getFilePath(),MediaStore.Video.Thumbnails.MINI_KIND);
				img_PostImage.setImageBitmap(bitmap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		/**
		 * Rotate Button file type
		 */
//		if (CommonVariable.getInstance().getFilePath()==null && CommonVariable.getInstance().getFilePath()=="") {
//			btnRotate.setVisibility(View.GONE);
//		}else{
//			btnRotate.setVisibility(View.VISIBLE);
//		}

		toggleButton_FB.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				Headline =edt_Headline.getText().toString().trim();
				Description = edt_Description.getText().toString().trim();

				if(toggleButton_FB.isChecked()){
					mSimpleFacebook.login(mFBLoginListener);
					mSharefecebook="FACEBOOK";
					//mSimpleFacebook.publish(feed, onPublishListener);
				}
			}
		});

		toggleButton_Twitter.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if(toggleButton_Twitter.isChecked()){
				mShareTwitter="TWITTER";
				mTwitter = new Twitter_Handler(PostSettingActivity.this,consumer_key, secret_key);
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
			}
		});

		//Call webservice of category
		CallWsCategory();

		TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		android_id = tm.getDeviceId();
	}

	
// Allow Location or not
	public void locationDialog(){

		final Dialog dialog = new Dialog(PostSettingActivity.this);

		dialog.getWindow();

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// tell the Dialog to use the dialog.xml as it's layout
		// description
		dialog.setContentView(R.layout.custom_dialog);
		// dialog.setTitle(null);
			

			TextView txt = (TextView) dialog.findViewById(R.id.txt_dia);

			txt.setText(R.string.location_is_not_allowed_do_you_want_to_allow_);

			Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
			Button btnNo = (Button) dialog.findViewById(R.id.btn_no);

			btnYes.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					AppSharedPrefrence.getInstance(PostSettingActivity.this).setIsLocation(true);
					LatLong = CommonUtility.getLatAndLong(PostSettingActivity.this);
					Latitude = LatLong[0];
					Longitude = LatLong[1];
					txt_Addtomap.setText(Latitude + "," + Longitude);
					dialog.dismiss();
				}
			});

			btnNo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					AppSharedPrefrence.getInstance(PostSettingActivity.this).setIsLocation(false);
					txt_Addtomap.setText(getString(R.string.add_to_map));
					dialog.dismiss();
				}
			});

			dialog.show();
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
//		action_title = (TextView) findViewById(R.id.action_title);
//		action_title.setText("Logo");

		actionleft = (Button) findViewById(R.id.action_Left);
		actionleft.setBackgroundResource(R.drawable.actionbar_back);

		actionRight = (Button) findViewById(R.id.action_right);
		actionRight.setVisibility(View.INVISIBLE);

		btn_Share = (Button)findViewById(R.id.sharebtn);

		edt_Headline = (EditText)findViewById(R.id.Headline);
		img_PostImage = (ImageView)findViewById(R.id.PostImage);
		edt_Description = (EditText)findViewById(R.id.Description);
		txt_categoryName =(TextView)findViewById(R.id.category_txt);
		txt_Addtomap = (TextView)findViewById(R.id.map);
		btnRotateLeft = (Button) findViewById(R.id.btn_rotateLeft);
		btnRotateRight = (Button) findViewById(R.id.btn_rotateRight);
		txt_county = (EditText) findViewById(R.id.Country);
		txt_state = (EditText)findViewById(R.id.State);
		txt_City = (EditText)findViewById(R.id.City);

		toggleButton_FB =(ToggleButton)findViewById(R.id.toggleButton_FB);
		toggleButton_Twitter =(ToggleButton)findViewById(R.id.toggleButton_Twitter);

		if(strCounty.equals("")){
			txt_county.setHint(getString(R.string.country));
			txt_state.setHint(getString(R.string.state));
			txt_City.setHint(getString(R.string.city));
		}else{
			txt_county.setText(strCounty);
			txt_state.setText(strState);
			txt_City.setText(strCity);
		}

		// iF County is selected in first time then it show selected every time 
//		String SelectedCounty = AppSharedPrefrence.getInstance(PostSettingActivity.this).getCounty();
//		if(!SelectedCounty.equals("")){
//			txt_county.setText(SelectedCounty);
//		}
//
//		String SelectedState = AppSharedPrefrence.getInstance(PostSettingActivity.this).getState();
//		if(!SelectedState.equals("")){
//			txt_state.setText(SelectedState);
//		}
	}


	/**
	 * Set Category to Dialog
	 */
	private void SetCategoriesDialogue() {

			AlertDialog.Builder myDialog = new AlertDialog.Builder(PostSettingActivity.this,R.style.myBackgroundStyle);

			Categorylistview = new ListView(PostSettingActivity.this);
			Categorylistview.setDividerHeight(1);
			//ListArray = new ArrayList<String>());
			LinearLayout layout = new LinearLayout(PostSettingActivity.this);
			layout.setOrientation(LinearLayout.VERTICAL);
			layout.setBackgroundColor(Color.WHITE);
			layout.addView(Categorylistview);
			myDialog.setView(layout);

			categoryadapter = new CatgeoryListAdapter(PostSettingActivity.this,CategoryName); 
			Categorylistview.setAdapter(categoryadapter);

			Categorylistview.setOnItemClickListener(new OnItemClickListener() {

				@SuppressWarnings("unchecked")
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
					catalertDialog.dismiss();
					categoryadapter = (CatgeoryListAdapter) arg0.getAdapter();
//					temp = (LinkedHashMap<String, Object>) categorylistAdapter.getItem(position);

					tempData=new ArrayList<Object>();
					tempData=(ArrayList<Object>) tempCategory.get("data");
					data=(LinkedHashMap<String, Object>) tempData.get(position);
					SelectedCategory = data.get("vCategory").toString();

					txt_categoryName.setText(SelectedCategory);

					//selected Category Id pass to parameter
					SelectedCategoryId = data.get("iCategoryId").toString();
				}
			});
			catalertDialog = myDialog.show();
			catalertDialog.getWindow().setLayout(400, 400);
			WindowManager.LayoutParams wnlp = catalertDialog.getWindow().getAttributes();
			wnlp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
			catalertDialog.getWindow().setAttributes(wnlp);
		}

	/**
	 * Set County to Dialog
	 */
	//@SuppressWarnings("unchecked")
//	private void SetCountyDialogue() {
//
//			AlertDialog.Builder myDialog = new AlertDialog.Builder(PostSettingActivity.this,R.style.myBackgroundStyle);
//
//			final EditText edt_County = new EditText(PostSettingActivity.this);
//			Countrylistview = new ListView(PostSettingActivity.this);
//			Countrylistview.setDividerHeight(1);
//			edt_County.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.search, 0);
//			edt_County.setTextSize(20);
//			//ListArray = new ArrayList<String>());
//			LinearLayout layout = new LinearLayout(PostSettingActivity.this);
//			layout.setOrientation(LinearLayout.VERTICAL);
//			layout.setBackgroundResource(R.color.themecolor);
//			layout.addView(edt_County);
//			layout.addView(Countrylistview);
//			myDialog.setView(layout);
//
//			//Getting StateCountyList Object From SplashScreen
//			StateCountryJSONData = (ArrayList<Object>)  CommonVariable.getInstance().getcountry();
////			String s= AppSharedPrefrence.getInstance(PostSettingActivity.this).getCountry();
////			StateCountryJSONData =  AppSharedPrefrence.getInstance(PostSettingActivity.this).getCountry();
//			StateCountyTemp = (LinkedHashMap<String, Object>) StateCountryJSONData.get(0);
//			tempCountryState=(ArrayList<Object>) StateCountyTemp.get("data");
//
//			for (int i = 0; i < tempCountryState.size(); i++) {
//			StateCountyData = (LinkedHashMap<String, Object>) tempCountryState.get(i);
//
//			CountyName.add(StateCountyData.get("vCountry").toString());
//			CountryID.add(StateCountyData.get("iCountryId").toString());
//
//			}
//			Countryadapter = new CountyListAdapter(PostSettingActivity.this,CountyName,CountryID); 
//			Countrylistview.setAdapter(Countryadapter);
//
//			myTextWatcherCountyState = new TextWatcherCountryState(edt_County, Countrylistview,PostSettingActivity.this,CountyName,CountryID);
//			edt_County.addTextChangedListener(myTextWatcherCountyState);
//
//
//			Countrylistview.setOnItemClickListener(new OnItemClickListener() {
//
//				@SuppressWarnings("unchecked")
//				@Override
//				public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
//					StateCountyalertDialog.dismiss();
//
////					Countryadapter = (CountyListAdapter) arg0.getAdapter();
////					//CountyTemp = (LinkedHashMap<String, Object>) contryadapter.getItem(position);
////
////					tempCountryState=new ArrayList<Object>();
////					tempCountryState=(ArrayList<Object>) StateCountyTemp.get("data");
////
////					StateCountyData=(LinkedHashMap<String, Object>) tempCountryState.get(position);
////					SelectedCountry = StateCountyData.get("vCountry").toString();
//
//					//If user select county first time then its shows selected every timme when post
//					AppSharedPrefrence.getInstance(PostSettingActivity.this).setCounty(CountyName.get(position));
//					AppSharedPrefrence.getInstance(PostSettingActivity.this).setCountyID(CountryID.get(position));
//
//					txt_county.setText(CountyName.get(position));
//
//					StateName.clear();
//					//selected Country Id pass to parameter
//					SelectedCountyID = CountryID.get(position);
//				}
//			});
//			StateCountyalertDialog = myDialog.show();
//			StateCountyalertDialog.getWindow().setLayout(400, 400);
//			WindowManager.LayoutParams wnlp = StateCountyalertDialog.getWindow().getAttributes();
//			wnlp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
//			StateCountyalertDialog.getWindow().setAttributes(wnlp);
//		}

	/**
	 * Set State to Dialog
	 */
	@SuppressWarnings("unchecked")
//	private void SetStateDialogue() {
//
//			AlertDialog.Builder myDialog = new AlertDialog.Builder(PostSettingActivity.this,R.style.myBackgroundStyle);
//
//			final EditText edt_State= new EditText(PostSettingActivity.this);
//			Statelistview = new ListView(PostSettingActivity.this);
//			Statelistview.setDividerHeight(1);
//			//ListArray = new ArrayList<String>());
//			edt_State.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.search, 0);
//			edt_State.setTextSize(20);
//			LinearLayout layout = new LinearLayout(PostSettingActivity.this);
//			layout.setOrientation(LinearLayout.VERTICAL);
//			layout.setBackgroundResource(R.color.themecolor);
//			layout.addView(edt_State);
//			layout.addView(Statelistview);
//			myDialog.setView(layout);
//
//			//Getting CountyList Object From SplashScreen
//			StateCountryJSONData = (ArrayList<Object>)  CommonVariable.getInstance().getState();
//			StateCountyTemp = (LinkedHashMap<String, Object>) StateCountryJSONData.get(0);
//			tempCountryState=(ArrayList<Object>) StateCountyTemp.get("data");
//
//			//Select state based on countyID
//			if (StateName.size() == 0) {
//				for (int i = 0; i < tempCountryState.size(); i++) {
//					StateCountyData = (LinkedHashMap<String, Object>) tempCountryState.get(i);
//					if (SelectedCountyID != null) {
//
//						// if (StateName.size() == 0) {
//						if (StateCountyData.get("iCountryId").toString().equals(SelectedCountyID)) {
//							StateName.add(StateCountyData.get("vState").toString());
//							StateId.add(StateCountyData.get("iStateId").toString());
//						}
//						// }
//					}
//				}
//			}
//			stateadapter = new StateListAdapter(PostSettingActivity.this,StateName,StateId);
//			Statelistview.setAdapter(stateadapter);
//
//			myTextWatcherCountyState = new TextWatcherCountryState(edt_State, Statelistview,PostSettingActivity.this,StateName,StateId);
//			edt_State.addTextChangedListener(myTextWatcherCountyState);
//
//			Statelistview.setOnItemClickListener(new OnItemClickListener() {
//
//				@SuppressWarnings("unchecked")
//				@Override
//				public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
//					StateCountyalertDialog.dismiss();
//
////					Statecontryadapter = (StateCountyListAdapter) arg0.getAdapter();
////					//CountyTemp = (LinkedHashMap<String, Object>) contryadapter.getItem(position);
////
////					tempCountryState=new ArrayList<Object>();
////					tempCountryState=(ArrayList<Object>) StateCountyTemp.get("data");
////
////					StateCountyData=(LinkedHashMap<String, Object>) tempCountryState.get(position);
////					SelectedState = StateCountyData.get("vState").toString();
//
//					//If user select state first time then its shows selected every timme when post
//					AppSharedPrefrence.getInstance(PostSettingActivity.this).setState(StateName.get(position));
//					AppSharedPrefrence.getInstance(PostSettingActivity.this).setStateID(StateId.get(position));
//	
//					txt_state.setText(StateName.get(position));
//					SelectedStateID = StateId.get(position);
//
//					//selected State Id pass to parameter
////					SelectedStateID = StateCountyData.get("iStateId").toString();
//				}
//			});
//			StateCountyalertDialog = myDialog.show();
//			StateCountyalertDialog.getWindow().setLayout(400, 400);
//			WindowManager.LayoutParams wnlp = StateCountyalertDialog.getWindow().getAttributes();
//			wnlp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
//			StateCountyalertDialog.getWindow().setAttributes(wnlp);
//		}
	
	/**
	 * Calling the web-service of Category
	 */

	public void CallWsCategory() {

		String memberid = CommonVariable.getInstance().getMember_id();

		psd = new PostDataAndGetData(PostSettingActivity.this, null, null,"GET",CommonUtility.BaseUrl2 +"getCategories" + "&iMemberId=" + memberid,CatagoryRcode, 0, false, true);
		psd.execute();
	}


	/**
	 * Calling the web-service of Post Setting
	 */

	public void CallWsPostSetting() {

		strCounty = txt_county.getText().toString();
		strState = txt_state.getText().toString();
		strCity = txt_City.getText().toString();

		Parameter = new HashMap<String, String>();

		Parameter.put("vCategoryId", SelectedCategoryId);
		Parameter.put("iMemberId", CommonVariable.getInstance().getMember_id());
		Parameter.put("tPost", Headline);
		Parameter.put("tComments", Description);
		Parameter.put("iCountryId",strCounty);
		Parameter.put("iStateId", strState);
		Parameter.put("vCity", strCity);
		Parameter.put("vLattitude", Latitude);
		Parameter.put("vLongitude", Longitude);
		Parameter.put("isShareWithFacebook",mSharefecebook);
		Parameter.put("isShareWithTwitter",mShareTwitter);
		Parameter.put("eFileType", AppSharedPrefrence.getInstance(PostSettingActivity.this).getFileType());
		Parameter.put("vIP", ipaddress);

		psd = new PostDataAndGetData(PostSettingActivity.this,null,Parameter,"POST",CommonUtility.BaseUrl2 + "setPost",PostSettingRcode, 0, false, true);
//		if (upload != null) {
			psd.isFilePost(true);
			HashMap<String, String> fileArray = new HashMap<String, String>();
//			fileArray.put("userfile", upload.mCurrentPhotoPath);
			fileArray.put("vFile", AppSharedPrefrence.getInstance(PostSettingActivity.this).getFilePath());
			psd.setFile(fileArray);
//		}
		psd.execute();
	}

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
		Parameter.put("vDeviceid",android_id);
		Parameter.put("iMemberId", CommonVariable.getInstance().getMember_id());
		Parameter.put("vIP", ipaddress);

		psd = new PostDataAndGetData(PostSettingActivity.this, null, Parameter,
				"POST", CommonUtility.BaseUrl2 + "userLogin", FBResponseCode,0, false, true);
		psd.execute();
	}

	public void CallWsTwitterLogin() {
		Parameter = new HashMap<String, String>();

//		Parameter.put("vPassword", password);
		Parameter.put("vPicture", mTwitter.getUProPic());
//		Parameter.put("vEmail", mTwitter.getUsername());
		Parameter.put("vName", mTwitter.getUsername());
		Parameter.put("vUsername", mTwitter.getScreenname());
		Parameter.put("eLoginType", "TWITTER");
		Parameter.put("vDeviceid",android_id);
		//Parameter.put("vTwitterId", ""+mTwitter.getUID());
		Parameter.put("vTwitterId", mTwitter.getToken());
		Parameter.put("tDeviceToken", CommonVariable.getInstance().getGCM_id());
		Parameter.put("tTwitterToken", mTwitter.getTokenSecret());
		Parameter.put("iMemberId", CommonVariable.getInstance().getMember_id());
		Parameter.put("vIP", ipaddress);

		psd = new PostDataAndGetData(PostSettingActivity.this, null, Parameter,
				"POST", CommonUtility.BaseUrl2 + "userLogin", TwtResponseCode,
				0, false, true);
		psd.execute();

	}

	private int getImageOrientation() {
		final String[] imageColumns = { MediaStore.Images.Media._ID,MediaStore.Images.ImageColumns.ORIENTATION };
		final String imageOrderBy = MediaStore.Images.Media._ID + " DESC";
		Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageColumns,null, null, imageOrderBy);

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
			act = activity;
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
			Bitmap sourceBitmap = BitmapFactory.decodeFile(AppSharedPrefrence.getInstance(PostSettingActivity.this).getFilePath());
			bmp = Bitmap.createBitmap(sourceBitmap, 0, 0,sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix,true);

			// Bitmap bmp = createBitmap();
			OutputStream stream = null;
			try {
				stream = new FileOutputStream(AppSharedPrefrence.getInstance(PostSettingActivity.this).getFilePath());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			bmp.compress(CompressFormat.JPEG, 100, stream);
			//bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

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
				bar = new ProgressDialog(act,ProgressDialog.THEME_HOLO_LIGHT);
				bar.setIndeterminate(true);
				bar.setTitle(act.getString(R.string.rotating_));
				bar.setIcon(act.getResources().getDrawable(R.drawable.ic_launcher));
				bar.setCancelable(false);
				bar.show();
			} catch (Exception e) {
				e.printStackTrace();
			}

			super.onPreExecute();
		}

		@SuppressLint("NewApi")
		protected void onPostExecute(Void result) {

			// do UI work here
			img_PostImage.setBackground(null);
			img_PostImage.setImageBitmap(bmp);
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
			act = activity;
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
			Bitmap sourceBitmap = BitmapFactory.decodeFile(AppSharedPrefrence.getInstance(PostSettingActivity.this).getFilePath());
			bmp = Bitmap.createBitmap(sourceBitmap, 0, 0,sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix,true);

			// Bitmap bmp = createBitmap();
			OutputStream stream = null;
			try {
				stream = new FileOutputStream(AppSharedPrefrence.getInstance(PostSettingActivity.this).getFilePath());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			bmp.compress(CompressFormat.JPEG, 100, stream);
			//bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

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
				bar = new ProgressDialog(act,ProgressDialog.THEME_HOLO_LIGHT);
				bar.setIndeterminate(true);
				bar.setTitle(act.getString(R.string.rotating_));
				bar.setIcon(act.getResources().getDrawable(R.drawable.ic_launcher));
				bar.setCancelable(false);
				bar.show();
			} catch (Exception e) {
				e.printStackTrace();
			}

			super.onPreExecute();
		}

		@SuppressLint("NewApi")
		protected void onPostExecute(Void result) {

			// do UI work here
			img_PostImage.setBackground(null);
			img_PostImage.setImageBitmap(bmp);
			try {
				bar.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	

	@Override
	protected void onResume()
	{
		super.onResume();
		//Set instanced of facebook
		mSimpleFacebook = SimpleFacebook.getInstance(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

	//Adapter class for Category List.
	public class CatgeoryListAdapter extends BaseAdapter {

		Context ctx = null;
		private LayoutInflater mInflater = null;
		ArrayList<String> CategoryList = null;


		public CatgeoryListAdapter(Activity activty,ArrayList<String> categoryList) {
			this.ctx = activty;
			mInflater = LayoutInflater.from(ctx);
			this.CategoryList = categoryList;
		}

		@Override
		public int getCount() {
			return CategoryList.size();
		}

		@Override
		public Object getItem(int Position) {
			return CategoryList.get(Position);
		}

		@Override
		public long getItemId(int Position) {
			return Position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.dialogcat_list_item, null);

				holder.txt_Category =(TextView)convertView.findViewById(R.id.txt_Category_Name);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.txt_Category.setText(CategoryList.get(position));

			return convertView;
		}

		private class ViewHolder {
			TextView txt_Category;

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
						try {
//							strPicture=profile.getPicture();
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
				toggleButton_FB.setBackgroundDrawable(getResources().getDrawable(R.drawable.toggle_off));
				Toast.makeText(PostSettingActivity.this,R.string.you_didn_t_accept_read_permissions_, Toast.LENGTH_LONG).show();
			}
		};
	
		// Login with Twitter
		private final TwDialogListener twitterLoginDialogListener = new TwDialogListener() {

			@Override
			public void onError(String value) {
				toggleButton_Twitter.setBackgroundDrawable(getResources().getDrawable(R.drawable.toggle_off));
				Toast.makeText(PostSettingActivity.this, R.string.login_failed,Toast.LENGTH_LONG).show();
				mTwitter.resetAccessToken();
			}

			@Override
			public void onComplete(String value) {
				// showTwittDialog();
				
				CallWsTwitterLogin();
			}
		};

//		public void address(double lt, double lg) throws IOException {
//			Geocoder geocoder;
//			List<Address> addresses;
//			geocoder = new Geocoder(this, Locale.getDefault());
//			addresses = geocoder.getFromLocation(lt, lg, 1);
//
//			//String address = addresses.get(0).getAddressLine(0);
//			//String city = addresses.get(0).getAddressLine(1);
//			//String newstring = city.replaceAll(",","");
//			//String country = addresses.get(1).getAddressLine(2);
//
//			String city = addresses.get(0).getLocality();
//			String country = addresses.get(0).getCountryName();
//			String state = addresses.get(0).getAdminArea();
//		}


	@Override
	public void onBackPressed() {
		Intent intent = new Intent(PostSettingActivity.this,CustomGalleryActivity.class);
		startActivity(intent);
		finish();
		super.onBackPressed();
	}

	/**
	 * Web-service Response
	 */

	LinkedHashMap<String, Object> temp;
	LinkedHashMap<String, Object> tempCategory;
	ArrayList<LinkedHashMap<String, Object>> jsonData;
	ArrayList<Object> arrayList;
	LinkedHashMap<String, Object> data;
	ArrayList<Object> tempData;

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
				case PostSettingRcode:
					data=(LinkedHashMap<String, Object>) temp.get("data");
					Intent intent = new Intent(PostSettingActivity.this,NewsFeedScreen.class);
					startActivity(intent);
					finish();

					break;

				case CatagoryRcode:

					tempCategory = temp;
					tempData=new ArrayList<Object>();
					tempData=(ArrayList<Object>) tempCategory.get("data");

					for (int i = 0; i < tempData.size(); i++) {
						data = (LinkedHashMap<String, Object>) tempData.get(i);

						CategoryName.add(data.get("vCategory").toString());
					}

					break;

				case FBResponseCode:
					data=(LinkedHashMap<String, Object>) temp.get("data");
					toggleButton_FB.setBackgroundDrawable(getResources().getDrawable(R.drawable.toggle_on));
//					CommonVariable.getInstance().setMember_id(data.get("iMemberId").toString());
					break;

				case TwtResponseCode:
					data=(LinkedHashMap<String, Object>) temp.get("data");
					toggleButton_Twitter.setBackgroundDrawable(getResources().getDrawable(R.drawable.toggle_on));
//					CommonVariable.getInstance().setMember_id(data.get("iMemberId").toString());
					break;

				default:
					break;
				}
				
				  } else { CommonUtility.showAlert(data.get("msg").toString(),PostSettingActivity.this); }
			} else {
				CommonUtility.showAlert(getString(R.string.there_is_some_problem_in_getting_data_),PostSettingActivity.this);
			}
		} catch (Exception e) {
			CommonUtility.showAlert(getString(R.string.there_is_some_problem_in_getting_data_),PostSettingActivity.this);
		}
	}
}