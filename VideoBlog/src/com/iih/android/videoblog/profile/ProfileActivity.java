package com.iih.android.videoblog.profile;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iih.android.videoblog.R;
import com.iih.android.videoblog.Parsing.PostDataAndGetData;
import com.iih.android.videoblog.Parsing.UploadPicture;
import com.iih.android.videoblog.Parsing.parseListner;
import com.iih.android.videoblog.category.PostDetailsActivity;
import com.iih.android.videoblog.category.SearchCategoryActivity;
import com.iih.android.videoblog.commonutill.CommonUtility;
import com.iih.android.videoblog.commonutill.CommonVariable;
import com.iih.android.videoblog.commonutill.CustomeAlertYesNo;
import com.iih.android.videoblog.gallery.CameraActivity;
import com.iih.android.videoblog.newsfeed.NewsFeedScreen;
import com.iih.android.videoblog.notification.NotificationActivity;
import com.iih.android.videoblog.setting.GeneralSetting;
import com.iih.loadwebimage.imageutils.ImageLoader;

public class ProfileActivity extends Activity implements OnClickListener, parseListner {

	private TextView action_title;
	private Button actionleft;
	private Button actionRight;

	private CommonUtility comonUti;
	private Intent intent;
	private Button btn_ProfileButton;
	private ListView mypostListview;

	private ImageView img_profile_pic;
	private TextView txt_firstname;
	private TextView txt_description;
	private TextView total_follower;
	private TextView total_following;
	private TextView total_posts;
	public ImageLoader imageLoader;

	private final int SetPublicProfileRCode = 0, SetFollowRcode = 1,SetCoverImageRcode=2;

	private String IsMyProfile = "";
	Boolean IsEdit=true;

	private ImageView img_cover;
	private Button btn_AddCover;

	private String[] cameraPrompt;

	private Uri fileUri; // file url to store image

	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	public static final int MEDIA_TYPE_IMAGE = 1;
	private int SELECT_PHOTO = 110;
	UploadPicture upload;

	private String CoverImagePath;

	private static final String IMAGE_DIRECTORY_NAME = "VideoBlog";

	/**
	 * Calling the web-service
	 */
	HashMap<String, String> Paramter;
	PostDataAndGetData psd;
	HashMap<String, String> Parameter = null;

	private String Following;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);

		cameraPrompt = new String[] { getResources().getString(R.string.from_camera), getResources().getString(R.string.from_gallery) };

		try {
			comonUti = new CommonUtility(this);
			comonUti.categorytype = "profile";
			comonUti.findViewById();
		} catch (Exception e) {
			e.printStackTrace();
		}

		initialization();

//		MyprofileListAdapter Adapter = new MyprofileListAdapter(ProfileActivity.this);
//		mypostListview.setAdapter(Adapter);

		imageLoader = new ImageLoader(ProfileActivity.this);
		btn_ProfileButton.setOnClickListener(this);
		actionRight.setOnClickListener(this);
		btn_AddCover.setOnClickListener(this);

		CallWsPublicprofile();

		mypostListview.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				data=(LinkedHashMap<String, Object>) tempData.get(position);
				CommonVariable.getInstance().setPost_id(data.get("iPostId").toString());
				Intent intent=new Intent(ProfileActivity.this, UserPostListActivity.class);
				startActivity(intent);
			}
		});
	}

	public void showImagePickerDialog(String[] str) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this,R.style.myBackgroundStyle);
		builder.setTitle(getResources().getString(R.string.change_cover));
		builder.setIcon(R.drawable.ic_launcher);
		builder.setSingleChoiceItems(str, -1,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
//							upload = new UploadPicture(CameraActivity.this);
//							upload.dispatchTakePictureIntent(CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
							dialog.dismiss();
							captureImage();
							break;
						case 1:
							dialog.dismiss();
							upload = new UploadPicture(ProfileActivity.this);
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

	/*
	 * Capturing Camera Image will lauch camera app requrest image capture
	 */
	private void captureImage() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

		// start the image capture Intent
		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	}

	/**
	 * Checking device has camera hardware or not
	 * */
	private boolean isDeviceSupportCamera() {
		if (getApplicationContext().getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			// this device has a camera
			return true;
		} else {
			// no camera on this device
			return false;
		}
	}

	public void CallwsCoverImage() {
		Parameter = new HashMap<String, String>();

		Parameter.put("iMemberId", CommonVariable.getInstance().getMember_id());

		psd = new PostDataAndGetData(ProfileActivity.this, null, Parameter,
				"POST", CommonUtility.BaseUrl2 + "setProfileCoverImage", SetCoverImageRcode,
				0, false, true);

		if (upload != null) {

			// psd.setFileUpload(true);
			psd.isFilePost(true);
			HashMap<String, String> fileArray = new HashMap<String, String>();
			fileArray.put("vCoverImage",CoverImagePath);
			psd.setFile(fileArray);
		}
		psd.execute();
	}

	public void CallwsSetFollow() {
		Parameter = new HashMap<String, String>();

		Parameter.put("iSessMemberId", CommonVariable.getInstance().getMember_id());
		Parameter.put("iProfilerId", CommonVariable.getInstance().getProfiler_id());

		psd = new PostDataAndGetData(ProfileActivity.this, null, Parameter,
				"POST", CommonUtility.BaseUrl2 + "setFollow", SetFollowRcode,
				0, false, true);
		psd.execute();
	}

	public void CallwsSetUnFollow() {
		Parameter = new HashMap<String, String>();

		Parameter.put("iSessMemberId", CommonVariable.getInstance().getMember_id());
		Parameter.put("iProfilerId", CommonVariable.getInstance().getProfiler_id());

		psd = new PostDataAndGetData(ProfileActivity.this, null, Parameter,
				"POST", CommonUtility.BaseUrl2 + "setUnFollow", SetFollowRcode,
				0, false, true);
		psd.execute();
	}

	public void CallWsPublicprofile() {

		Parameter = new HashMap<String, String>();

		String profiler_id = CommonVariable.getInstance().getProfiler_id();

		if ((profiler_id != null) && !profiler_id.equalsIgnoreCase("")) {
			Parameter.put("iSessMemberId", CommonVariable.getInstance().getMember_id());
			Parameter.put("iProfilerId", profiler_id);
			IsMyProfile = "no";
		} else {
			IsMyProfile = "yes";
			Parameter.put("iSessMemberId", CommonVariable.getInstance().getMember_id());
			Parameter.put("iProfilerId", CommonVariable.getInstance().getMember_id());
		}

		psd = new PostDataAndGetData(ProfileActivity.this, null, Parameter,"POST", CommonUtility.BaseUrl2 + "getPublicProfile",
				SetPublicProfileRCode, 0, false, true);

		psd.execute();
	}

	private void initialization() {

//		action_title = (TextView) findViewById(R.id.action_title);
//		action_title.setText("Logo");

		actionleft = (Button) findViewById(R.id.action_Left);
		actionleft.setVisibility(View.INVISIBLE);

		actionRight = (Button) findViewById(R.id.action_right);
		actionRight.setBackgroundResource(R.drawable.action_setitng);

		mypostListview = (ListView) findViewById(R.id.mystufflistview);
		btn_ProfileButton = (Button) findViewById(R.id.Editprofile);

		btn_AddCover = (Button)findViewById(R.id.btn_Addcover);
		img_cover = (ImageView)findViewById(R.id.CoverPhoto);

		img_profile_pic = (ImageView) findViewById(R.id.profile_pic);
		txt_firstname = (TextView) findViewById(R.id.Firstname);
		txt_description = (TextView) findViewById(R.id.User_Details);
		total_follower = (TextView) findViewById(R.id.countFollowers);
		total_following = (TextView) findViewById(R.id.countFollowing);
		total_posts = (TextView) findViewById(R.id.countPost);

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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		//super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {


			//Path of CoverImage
			CoverImagePath =  fileUri.getPath();
			Bitmap bmp ;
			bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(CoverImagePath),400, 400, true);
			img_cover.setImageBitmap(bmp);

			//Upload cover Image
			CallwsCoverImage();

		} else if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PHOTO) {
			try {
				upload.decodeUri(data.getData());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			//Path of CoverImage
			CoverImagePath = upload.mCurrentPhotoPath;
			//CommonVariable.getInstance().setFilePath(upload.mCurrentPhotoPath);

			Bitmap bmp ;
			bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(CoverImagePath),400, 400, true);
			img_cover.setImageBitmap(bmp);

			//Upload cover Image
			CallwsCoverImage();
		}
	}

	/**
	 * ------------ Helper Methods ---------------------- 
	 * */

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
		} /*else if (type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator+ "VID_" + timeStamp + ".mp4");
		}*/ else {
			return null;
		}

		return mediaFile;
	}

	@Override
	public void onBackPressed() {
		CommonVariable.getInstance().setProfiler_id("");
		if(IsMyProfile.equalsIgnoreCase("yes")){

			CustomeAlertYesNo customedialog = new CustomeAlertYesNo(ProfileActivity.this);
			customedialog.show();

		}else{
			super.onBackPressed();
		}
	}

	
	
	@Override
	public void onClick(View v) {
		int id = v.getId();

		switch (id) {

		case R.id.imgbtn_Home:
			CommonVariable.getInstance().setProfiler_id("");
			intent = new Intent(ProfileActivity.this, NewsFeedScreen.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); 

			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 

//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); 
//			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
			startActivity(intent);
			finish();

			break;

		case R.id.imgbtn_search_category:
			CommonVariable.getInstance().setProfiler_id("");
			intent = new Intent(ProfileActivity.this,SearchCategoryActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);   
			startActivity(intent);
			finish();
			break;

		case R.id.imgbtn_camera:
			CommonVariable.getInstance().setProfiler_id("");
			intent = new Intent(ProfileActivity.this, CameraActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
			startActivity(intent);
			finish();
			break;

		case R.id.imgbtn_notification:
			CommonVariable.getInstance().setProfiler_id("");

			intent = new Intent(ProfileActivity.this,NotificationActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
			startActivity(intent);
			finish();
			break;

		case R.id.Editprofile:

			if (data.get("isCurrentUserLoginMatch").toString().equalsIgnoreCase("NO")) {

				if (data.get("isFollowing").toString().equalsIgnoreCase("YES")) {
					CallwsSetUnFollow();
//					Following = "NO";
				} else {
					CallwsSetFollow();
//					Following = "YES";
				}
			} else {
				Intent intent = new Intent(ProfileActivity.this,EditProfileActivity.class);
				startActivity(intent);
				finish();
			}
			break;

		case R.id.action_right:

			Intent intent1 = new Intent(ProfileActivity.this,GeneralSetting.class);
			startActivity(intent1);
			break;

		case R.id.btn_Addcover:

			if (!isDeviceSupportCamera()) {
				Toast.makeText(getApplicationContext(),"Sorry! Your device doesn't support camera",Toast.LENGTH_LONG).show();
				// will close the app if the device does't have camera
				finish();
			}

			showImagePickerDialog(cameraPrompt);

			break;

		default:
			break;
		}
	}

	/**
	 * Web-service Response
	 */

	LinkedHashMap<String, Object> temp;
	ArrayList<LinkedHashMap<String, Object>> jsonData;
	ArrayList<Object> arrayList;
	LinkedHashMap<String, Object> data;
	LinkedHashMap<String, Object> mNewsFeeddata;
	ArrayList<Object> tempData;
//	LinkedHashMap<String, Object> tempdata;

	@SuppressWarnings("unchecked")
	@Override
	public void GetResult(Object jsonDataO, int responce) {
		try {
			arrayList = (ArrayList<Object>) jsonDataO;
			ArrayList<LinkedHashMap<String, Object>> jsonDataTemp = (ArrayList<LinkedHashMap<String, Object>>) jsonDataO;
			int size = arrayList.size();
			if (size != 0) {
				temp = (LinkedHashMap<String, Object>) arrayList.get(0);
				data = (LinkedHashMap<String, Object>) temp.get("message");
				if (data.get("success").toString().equalsIgnoreCase("1")) {
					jsonData = jsonDataTemp;
					switch (responce) {
					case SetPublicProfileRCode:
						data = (LinkedHashMap<String, Object>) temp.get("data");
//						tempdata=data;
						Following = data.get("isFollowing").toString();
						if (data.get("isCurrentUserLoginMatch").toString().equalsIgnoreCase("NO")) {

							if (Following.equalsIgnoreCase("YES")) {
								// btnFollowing
								btn_ProfileButton.setBackgroundResource(R.drawable.folliwing);
								btn_ProfileButton.setText(getResources().getString(R.string.following));
								btn_AddCover.setVisibility(View.INVISIBLE);

							} else {
								// btnFollow
								btn_ProfileButton.setBackgroundResource(R.drawable.follow);
								btn_ProfileButton.setText(getResources().getString(R.string.follow));
								btn_AddCover.setVisibility(View.INVISIBLE);
							}

						} else {
							// EDIt btn
							btn_ProfileButton.setBackgroundResource(R.drawable.edit_profile);
							btn_ProfileButton.setText(getResources().getString(R.string.editprofile));
							btn_AddCover.setVisibility(View.VISIBLE);
						}

						imageLoader.DisplayImage(data.get("vCoverImage").toString(), img_cover);
						imageLoader.DisplayImage(data.get("vPicture").toString(), img_profile_pic);
						txt_firstname.setText(data.get("vName").toString());
						txt_description.setText(data.get("tDescription").toString());
						total_follower.setText(data.get("totalFollowers").toString());
						total_following.setText(data.get("totalFollowings").toString());
						total_posts.setText(data.get("totalPostByProfiler").toString());

						try {
							mNewsFeeddata=(LinkedHashMap<String, Object>) data.get("newsFeeds");

							tempData=new ArrayList<Object>();
							tempData=(ArrayList<Object>) mNewsFeeddata.get("data");
							MyprofileListAdapter Adapter = new MyprofileListAdapter(ProfileActivity.this,tempData);
							mypostListview.setAdapter(Adapter);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						break;

					case SetFollowRcode:

//						btn_ProfileButton.setBackgroundResource(R.drawable.folliwing);
						Toast.makeText(ProfileActivity.this,data.get("msg").toString(), Toast.LENGTH_LONG).show();

						CallWsPublicprofile() ;
						break;

					case SetCoverImageRcode:
						break;

					default:
						break;
					}
				} else {
					CommonUtility.showAlert(data.get("msg").toString(),ProfileActivity.this);
				}
			} else {
				CommonUtility.showAlert(getString(R.string.there_is_some_problem_in_getting_data_), ProfileActivity.this);
			}
		} catch (Exception e) {
			CommonUtility.showAlert(getString(R.string.there_is_some_problem_in_getting_data_), ProfileActivity.this);
		}
	}

}
