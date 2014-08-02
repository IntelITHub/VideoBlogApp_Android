package com.iih.android.videoblog.category;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iih.android.videoblog.R;
import com.iih.android.videoblog.Parsing.PostDataAndGetData;
import com.iih.android.videoblog.Parsing.parseListner;
import com.iih.android.videoblog.commonutill.AppSharedPrefrence;
import com.iih.android.videoblog.commonutill.CommonUtility;
import com.iih.android.videoblog.commonutill.CommonVariable;
import com.iih.android.videoblog.gallery.CameraActivity;
import com.iih.android.videoblog.newsfeed.NewsFeedScreen;
import com.iih.android.videoblog.notification.NotificationActivity;
import com.iih.android.videoblog.profile.ProfileActivity;

public class SelectedcategoryActivity extends Activity implements OnClickListener,parseListner{

	private TextView action_title;
	private Button actionleft;
	private Button actionRight;

	private LinearLayout mLinearLayout;
	ValueAnimator mAnimator;

	private ListView SelectedCatList;
	private CommonUtility comonUti;
	private Intent intent;

	public PostDataAndGetData psd;
	private final int LatestResponseCode = 0;
	private final int ResponseCodeDeletePost = 1;
	private final int LocalizationResponseCode = 2;

	SelectedCatListAdapter SelectedCatAdapter;

	ArrayList<String> Username;
	ArrayList<String> PostCategory;
	ArrayList<String> PostTitle;
	ArrayList<String> PostImage;
	ArrayList<String> PostLikes;
	ArrayList<String> PostComments;
	ArrayList<String> PostDescription;

	private EditText SearchEdittext;

	private String PostId;

	private RelativeLayout latestLayout;
	private RelativeLayout mylocationLayout;

	private String[] LatLong;
	private String Latitude;
	private String Longitude;

	private String WsType = "";

	TextWatcherSelectedCategorylist myTextWatcherCategory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selectedsearchcategory);

		try {
			comonUti = new CommonUtility(this);
			comonUti.categorytype = "searchcategory";
			comonUti.findViewById();
		} catch (Exception e) {
			e.printStackTrace();
		}

		initialization();

		//Post details on item click
		SelectedCatList.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				// TODO Auto-generated method stub

				SelectedCatAdapter = (SelectedCatListAdapter)arg0.getAdapter();
				tempData=new ArrayList<Object>();

				tempData=(ArrayList<Object>) temp.get("data");
				data=(LinkedHashMap<String, Object>) tempData.get(position);

				PostId = data.get("iPostId").toString();

				if(PostId != null){
					CommonVariable.getInstance().setPost_id(PostId);
				}

				Intent intent = new Intent(SelectedcategoryActivity.this,PostDetailsActivity.class);
				startActivity(intent);
			}
		});

		//Delete post on item long press
		SelectedCatList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,int position, long arg3) {
				SelectedCatAdapter = (SelectedCatListAdapter) arg0.getAdapter();
				tempData = new ArrayList<Object>();

				PostId = data.get("iPostId").toString();
				String MemberId= data.get("iMemberId").toString();

				tempData = (ArrayList<Object>) temp.get("data");
				data = (LinkedHashMap<String, Object>) tempData.get(position);

				if(MemberId.equals(CommonVariable.getInstance().getMember_id())){

					final Dialog dialog = new Dialog(SelectedcategoryActivity.this);

					dialog.getWindow();

					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					// tell the Dialog to use the dialog.xml as it's layout
					// description
					dialog.setContentView(R.layout.custom_dialog);
					// dialog.setTitle(null);
						

						TextView txt = (TextView) dialog.findViewById(R.id.txt_dia);

						txt.setText(R.string.delete_post);

						Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
						Button btnNo = (Button) dialog.findViewById(R.id.btn_no);

						btnYes.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View view) {
								CallWsDeletePost();
								dialog.dismiss();
							}
						});

						btnNo.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View view) {
		
								dialog.dismiss();
							}
						});

						dialog.show();

//				AlertDialog.Builder builder = new AlertDialog.Builder(SelectedcategoryActivity.this);
//
//				builder.setTitle(R.string.delete_post);
//				builder.setMessage(R.string.are_you_sure_);
//
//				builder.setPositiveButton(R.string.yes,new DialogInterface.OnClickListener() {
//
//							public void onClick(DialogInterface dialog,int which) {
//								// Do nothing but close the dialog
//								dialog.dismiss();
//								CallWsDeletePost();
//							}
//						});
//
//				builder.setNegativeButton(R.string.no,
//						new DialogInterface.OnClickListener() {
//
//							@Override
//							public void onClick(DialogInterface dialog,int which) {
//								// Do nothing
//								dialog.dismiss();
//							}
//						});
//
//				AlertDialog alert = builder.create();
//				alert.show();
				}else{
					CommonUtility.showAlert(getString(R.string.you_are_not_authorized_to_delete_other_users_post_), SelectedcategoryActivity.this);
				}
				return true;
			}
		});

		// Add onPreDrawListener
					mLinearLayout.getViewTreeObserver().addOnPreDrawListener(
							new ViewTreeObserver.OnPreDrawListener() {

								@Override
								public boolean onPreDraw() {
									mLinearLayout.getViewTreeObserver().removeOnPreDrawListener(this);
									mLinearLayout.setVisibility(View.GONE);

									final int widthSpec = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
									final int heightSpec = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
									mLinearLayout.measure(widthSpec, heightSpec);

									mAnimator = slideAnimator(0,mLinearLayout.getMeasuredHeight());
									return true;
								}
							});

		actionleft.setOnClickListener(this);
		actionRight.setOnClickListener(this);
		latestLayout.setOnClickListener(this);
		mylocationLayout.setOnClickListener(this);

		CallWsSelectedCatLatest();

	}

	public void locationDialog(){

		final Dialog dialog = new Dialog(SelectedcategoryActivity.this);

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
					AppSharedPrefrence.getInstance(SelectedcategoryActivity.this).setIsLocation(true);
					CallWsSelectedCatLocalization();
					dialog.dismiss();
				}
			});

			btnNo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					AppSharedPrefrence.getInstance(SelectedcategoryActivity.this).setIsLocation(false);
					dialog.dismiss();
				}
			});

			dialog.show();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		//Update category when restart 
		CallWsSelectedCatLatest();
	}

	private void initialization() {
//		action_title =(TextView)findViewById(R.id.action_title);
//		action_title.setText("Logo");

		actionleft =(Button)findViewById(R.id.action_Left);
		actionleft.setBackgroundResource(R.drawable.actionbar_back);

		actionRight =(Button)findViewById(R.id.action_right);
		actionRight.setBackgroundResource(R.drawable.actionbar_plus);

		SearchEdittext = (EditText)findViewById(R.id.edt_searchbox);
		SelectedCatList = (ListView)findViewById(R.id.selectedcategoryList);

		mLinearLayout = (LinearLayout) findViewById(R.id.expandable);
		latestLayout = (RelativeLayout)findViewById(R.id.layout_latest);
		mylocationLayout =(RelativeLayout)findViewById(R.id.layout_mylocalization);
	}

	/**
	 * Calling the web-service
	 */

	public void CallWsSelectedCatLatest() {

		WsType = "latest";

		String Selected_Cat_id = CommonVariable.getInstance().getCategoryId();
		String memberid = CommonVariable.getInstance().getMember_id();

		psd = new PostDataAndGetData(SelectedcategoryActivity.this, null, null, "GET",
				CommonUtility.BaseUrl2 + "getPosts"+ "&iCategoryId=" + Selected_Cat_id + "&iMemberId=" + memberid ,LatestResponseCode, 0, false, true);
		psd.execute();
	}

	/**
	 * Calling the web-service
	 */

	public void CallWsSelectedCatLocalization() {

		WsType = "locale";

		String Selected_Cat_id = CommonVariable.getInstance().getCategoryId();
		String memberid = CommonVariable.getInstance().getMember_id();
		//Getting current latitude and longitude
		LatLong = CommonUtility.getLatAndLong(SelectedcategoryActivity.this);
		Latitude = LatLong[0];
		Longitude = LatLong[1];

		psd = new PostDataAndGetData(SelectedcategoryActivity.this, null, null,
				"GET", CommonUtility.BaseUrl2 + "getPosts" + "&iCategoryId="
						+ Selected_Cat_id + "&vLattitude=" + Latitude
						+ "&vLongitude" + Longitude + "&iMemberId=" + memberid, LocalizationResponseCode, 0, false,true);
		psd.execute();
	}


	public void CallWsDeletePost() {

		String memberid = CommonVariable.getInstance().getMember_id();

		psd = new PostDataAndGetData(SelectedcategoryActivity.this, null, null, "GET",
				CommonUtility.BaseUrl2 + "deletePost"+ "&iPostId=" + PostId + "&iMemberId=" + memberid ,ResponseCodeDeletePost, 0, false, true);
		psd.execute();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		CommonUtility.isBackPress = true;
//		Intent intent = new Intent(SelectedcategoryActivity.this,SearchCategoryActivity.class);
//		intent.putExtra("onback", "yes");
//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//		startActivity(intent);
//		finish();
		super.onBackPressed();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();

		switch (id) {

		case R.id.imgbtn_Home:
			intent = new Intent(SelectedcategoryActivity.this,NewsFeedScreen.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
			startActivity(intent);
			finish();
			break;

		case R.id.imgbtn_camera:
			intent = new Intent(SelectedcategoryActivity.this,CameraActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
			startActivity(intent);
			finish();
			break;

		case R.id.imgbtn_notification:
			intent = new Intent(SelectedcategoryActivity.this,NotificationActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
			startActivity(intent);
			finish();
			break;

		case R.id.imgbtn_profile:

			 intent = new Intent(SelectedcategoryActivity.this,ProfileActivity.class);
			 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
			 startActivity(intent);
			 finish();
			break;

		case R.id.action_Left:
			 finish();
			break;

		case R.id.action_right:

			if (mLinearLayout.getVisibility() == View.GONE) {
				expand();
			} else {
				collapse();
			}
			break;

		case R.id.layout_latest:
			CallWsSelectedCatLatest();
			collapse();
			break;

		case R.id.layout_mylocalization:
			
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					collapse();

					if(AppSharedPrefrence.getInstance(SelectedcategoryActivity.this).getIsLocation()){
						CallWsSelectedCatLocalization();
					}else{
						locationDialog();
					}
				}
			});
			
			break;
		default:
			break;
		}
	}

	/**
	 * for Hide and layout with animation
	 */

	private void expand() {
		// set Visible
		mLinearLayout.setVisibility(View.VISIBLE);

		/*
		 * Remove and used in preDrawListener final int widthSpec =
		 * View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		 * final int heightSpec = View.MeasureSpec.makeMeasureSpec(0,
		 * View.MeasureSpec.UNSPECIFIED); mLinearLayout.measure(widthSpec,
		 * heightSpec);
		 * 
		 * mAnimator = slideAnimator(0, mLinearLayout.getMeasuredHeight());
		 */

		mAnimator.start();
	}

	private void collapse() {
		int finalHeight = mLinearLayout.getHeight();

		ValueAnimator mAnimator = slideAnimator(finalHeight, 0);

		mAnimator.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationEnd(Animator animator) {
				// Height=0, but it set visibility to GONE
				mLinearLayout.setVisibility(View.GONE);
			}

			@Override
			public void onAnimationStart(Animator animator) {
			}

			@Override
			public void onAnimationCancel(Animator animator) {
			}

			@Override
			public void onAnimationRepeat(Animator animator) {
			}
		});
		mAnimator.start();
	}

	private ValueAnimator slideAnimator(int start, int end) {

		ValueAnimator animator = ValueAnimator.ofInt(start, end);

		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				// Update Height
				int value = (Integer) valueAnimator.getAnimatedValue();

				ViewGroup.LayoutParams layoutParams = mLinearLayout.getLayoutParams();
				layoutParams.height = value;
				mLinearLayout.setLayoutParams(layoutParams);
			}
		});
		return animator;
	}

	/**
	 * Web-service Response
	 */

	LinkedHashMap<String, Object> temp;
	LinkedHashMap<String, Object> data;
	ArrayList<LinkedHashMap<String, Object>> jsonData;
	ArrayList<Object> arrayList;
	ArrayList<Object> tempData;

	@SuppressWarnings("unchecked")
	@Override
	public void GetResult(Object jsonDataO, int responce) {
		// TODO Auto-generated method stub
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

				case LatestResponseCode:

					Username = new ArrayList<String>();
					PostCategory = new ArrayList<String>();
					PostTitle = new ArrayList<String>();
					PostImage = new ArrayList<String>();
					PostLikes = new ArrayList<String>();
					PostComments = new ArrayList<String>();
					PostDescription = new ArrayList<String>();

					tempData=new ArrayList<Object>();
					tempData=(ArrayList<Object>) temp.get("data");

					for (int i = 0; i < tempData.size(); i++) {

						data=(LinkedHashMap<String, Object>) tempData.get(i);
						Username.add(data.get("vUsername").toString());
						PostCategory.add(data.get("vCategoriesName").toString());
						PostTitle.add(data.get("tPost").toString());
						PostImage.add(data.get("vFile").toString());
						PostLikes.add(data.get("totalPostLikes").toString());
						PostComments.add(data.get("totalPostComments").toString());
						PostDescription.add(data.get("tDescription").toString());
					}

					SelectedCatAdapter = new SelectedCatListAdapter(SelectedcategoryActivity.this,Username,PostCategory,PostTitle,PostImage,PostLikes,PostComments,tempData,PostDescription);
					SelectedCatList.setAdapter(SelectedCatAdapter);

					myTextWatcherCategory = new TextWatcherSelectedCategorylist(SearchEdittext, SelectedCatList,SelectedcategoryActivity.this,Username,PostCategory,PostTitle,PostImage,PostLikes,PostComments,tempData,PostDescription);
					SearchEdittext.addTextChangedListener(myTextWatcherCategory);

					break;

				case ResponseCodeDeletePost:
					try {
						if(WsType.equals("latest")){
							CallWsSelectedCatLatest();
						}else{
							CallWsSelectedCatLocalization();
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					break;

				case LocalizationResponseCode:

					Username = new ArrayList<String>();
					PostCategory = new ArrayList<String>();
					PostTitle = new ArrayList<String>();
					PostImage = new ArrayList<String>();
					PostLikes = new ArrayList<String>();
					PostComments = new ArrayList<String>();
					PostDescription = new ArrayList<String>();

					tempData=new ArrayList<Object>();
					tempData=(ArrayList<Object>) temp.get("data");

					for (int i = 0; i < tempData.size(); i++) {

						data=(LinkedHashMap<String, Object>) tempData.get(i);
						Username.add(data.get("vUsername").toString());
						PostCategory.add(data.get("vCategoriesName").toString());
						PostTitle.add(data.get("tPost").toString());
						PostImage.add(data.get("vFile").toString());
						PostLikes.add(data.get("totalPostLikes").toString());
						PostComments.add(data.get("totalPostComments").toString());
						PostDescription.add(data.get("tDescription").toString());
					}

					SelectedCatAdapter = new SelectedCatListAdapter(SelectedcategoryActivity.this,Username,PostCategory,PostTitle,PostImage,PostLikes,PostComments,tempData,PostDescription);
					SelectedCatList.setAdapter(SelectedCatAdapter);

					myTextWatcherCategory = new TextWatcherSelectedCategorylist(SearchEdittext, SelectedCatList,SelectedcategoryActivity.this,Username,PostCategory,PostTitle,PostImage,PostLikes,PostComments,tempData,PostDescription);
					SearchEdittext.addTextChangedListener(myTextWatcherCategory);
				default:
					break;
				}
				} else {
					SelectedCatList.setAdapter(null);
					CommonUtility.showAlert(data.get("msg").toString(),SelectedcategoryActivity.this);
				}
			} else {
				CommonUtility.showAlert(getString(R.string.there_is_some_problem_in_getting_data_),SelectedcategoryActivity.this);
			}
		} catch (Exception e) {
			CommonUtility.showAlert(getString(R.string.there_is_some_problem_in_getting_data_),SelectedcategoryActivity.this);
		}
	}
}
