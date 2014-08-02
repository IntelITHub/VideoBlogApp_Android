package com.iih.android.videoblog.newsfeed;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iih.android.videoblog.R;
import com.iih.android.videoblog.Parsing.PostDataAndGetData;
import com.iih.android.videoblog.Parsing.parseListner;
import com.iih.android.videoblog.category.SearchCategoryActivity;
import com.iih.android.videoblog.commonutill.AppSharedPrefrence;
import com.iih.android.videoblog.commonutill.CommonUtility;
import com.iih.android.videoblog.commonutill.CommonVariable;
import com.iih.android.videoblog.commonutill.CustomeAlertYesNo;
import com.iih.android.videoblog.gallery.CameraActivity;
import com.iih.android.videoblog.notification.NotificationActivity;
import com.iih.android.videoblog.profile.ProfileActivity;

public class NewsFeedScreen extends Activity implements OnClickListener,parseListner {

	private TextView action_title;
	private CommonUtility comonUti;
	private LinearLayout mLinearLayout;
	ValueAnimator mAnimator;

	private Button actionleft;
	private Button actionRight;
	private final int GetLatestPost = 0,GetMyLocationPost = 1,LikeRcode=2,UnlikeRcode =3,SetComment=4,GetLatestPostAfterLike = 5,GetMylocationAfterLike=6;
	private ListView mListView;

	ProgressDialog pDialog;

	/**
	 * Calling the web-service
	 */
	public PostDataAndGetData psd;

	private String[] LatLong;
	private String Latitude;
	private String Longitude;

	private RelativeLayout latestLayout;
	private RelativeLayout mylocationLayout;

	AdapterNewsFeedList newsfeedadapter;

	private String WsType = "";

	// Flag for current page
	int latest_page = 1;

	// Flag for current page
	int localization_page = 1;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newsfeed);

		intialization();

		try {
			comonUti = new CommonUtility(this);
			comonUti.categorytype = "home";
			comonUti.findViewById();

			actionRight.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (mLinearLayout.getVisibility() == View.GONE) {
						expand();
					} else {
						collapse();
					}
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
		} catch (Exception e) {
			e.printStackTrace();
		}

		latestLayout.setOnClickListener(this);
		mylocationLayout.setOnClickListener(this);

		//arrayList = (ArrayList<Object>) CommonVariable.getInstance().getcountry();

		
		Button btnLoadMore = new Button(this);

		btnLoadMore.setBackgroundColor(getResources().getColor(R.color.themecolor));
		btnLoadMore.setTextColor(getResources().getColor(R.color.white));
		btnLoadMore.setText(R.string.load_more_);
		btnLoadMore.setHeight((int) getResources().getDimension(R.dimen.btnsize));
		btnLoadMore.setTextSize(getResources().getDimension(R.dimen.loadbtnsize));

				// Adding button to listview at footer
				mListView.addFooterView(btnLoadMore);

		/**
		 * Listening to Load More button click event
		 * */
		btnLoadMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// Starting a new async task
				new loadMoreListView().execute();
			}
		});

		CallWsLatestFeed(latest_page);
		//CallWsMylocationFeed();
	}

	public void locationDialog(){

		final Dialog dialog = new Dialog(NewsFeedScreen.this);

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
					AppSharedPrefrence.getInstance(NewsFeedScreen.this).setIsLocation(true);
					CallWsMylocationFeed(localization_page);
					dialog.dismiss();
				}
			});

			btnNo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					AppSharedPrefrence.getInstance(NewsFeedScreen.this).setIsLocation(false);
					dialog.dismiss();
				}
			});
			dialog.show();
	}
	
	private void intialization() {
//		action_title = (TextView) findViewById(R.id.action_title);
//		action_title.setText("Logo");

		actionleft =(Button)findViewById(R.id.action_Left);
		actionleft.setVisibility(View.INVISIBLE);

		actionRight =(Button)findViewById(R.id.action_right);

		mLinearLayout = (LinearLayout) findViewById(R.id.expandable);
		mListView = (ListView) findViewById(R.id.listview_newsfeed);

		latestLayout = (RelativeLayout)findViewById(R.id.layout_latest);
		mylocationLayout =(RelativeLayout)findViewById(R.id.layout_mylocalization);
	}

	public void CallWsLatestFeed(int page) {

		WsType = "latest";

		String iMemberId=CommonVariable.getInstance().getMember_id();
		psd = new PostDataAndGetData(NewsFeedScreen.this, null, null,
				"GET", CommonUtility.BaseUrl2 + "homePageLatestPost&iMemberId=" + iMemberId + "&page=" + page , GetLatestPost ,0, false, true);
		psd.execute();

	}

	//update data After Like and Comment
	public void CallWsAfterLikeLatestFeed(int page) {

		WsType = "latest";

		String iMemberId=CommonVariable.getInstance().getMember_id();
		psd = new PostDataAndGetData(NewsFeedScreen.this, null, null,
				"GET", CommonUtility.BaseUrl2 + "homePageLatestPost&iMemberId=" + iMemberId + "&page=" + page , GetLatestPostAfterLike ,0, false, true);
		psd.execute();

	}

	public void CallWsMylocationFeed(int page) {

		WsType = "locale";
		//Getting current latitude and longitude
				LatLong = CommonUtility.getLatAndLong(NewsFeedScreen.this);
				Latitude = LatLong[0];
				Longitude = LatLong[1];

		String iMemberId = CommonVariable.getInstance().getMember_id();
		psd = new PostDataAndGetData(NewsFeedScreen.this, null, null, "GET",
				CommonUtility.BaseUrl2 + "homePageLatestPost&iMemberId="
						+ iMemberId + "&vLattitude=" + Latitude + "&vLongitude="+ Longitude +"&page=" + page, GetMyLocationPost, 0, false, true);

		psd.execute();

	}

	public void CallWsMylocationAfterLike(int page) {

		WsType = "locale";
		//Getting current latitude and longitude
				LatLong = CommonUtility.getLatAndLong(NewsFeedScreen.this);
				Latitude = LatLong[0];
				Longitude = LatLong[1];

		String iMemberId = CommonVariable.getInstance().getMember_id();
		psd = new PostDataAndGetData(NewsFeedScreen.this, null, null, "GET",
				CommonUtility.BaseUrl2 + "homePageLatestPost&iMemberId="+ iMemberId + "&vLattitude=" + Latitude + "&vLongitude="+ Longitude +"&page=" + page, GetMylocationAfterLike, 0, false, true);

		psd.execute();

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

	@Override
	public void onBackPressed() {
		CustomeAlertYesNo customedialog = new CustomeAlertYesNo(NewsFeedScreen.this);
		customedialog.show();
		//super.onBackPressed();
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


	Intent intent;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgbtn_search_category:
			intent = new Intent(NewsFeedScreen.this,SearchCategoryActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
			startActivity(intent);
			finish();
			break;

		case R.id.imgbtn_camera:

			intent = new Intent(NewsFeedScreen.this,CameraActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
			startActivity(intent);
			finish();
			break;

		case R.id.imgbtn_notification:
			intent = new Intent(NewsFeedScreen.this,NotificationActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
			startActivity(intent);
			finish();
			break;

		case R.id.imgbtn_profile:
			intent = new Intent(NewsFeedScreen.this, ProfileActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
			startActivity(intent);
			finish();
			break;

		case R.id.layout_latest:
			tempData.clear();
			tempData2.clear();
			CallWsLatestFeed(1);
			collapse();
			break;

		case R.id.layout_mylocalization:

			if(AppSharedPrefrence.getInstance(NewsFeedScreen.this).getIsLocation()){
				tempData.clear();
				tempData2.clear();
				CallWsMylocationFeed(1);
			}else{
				locationDialog();
			}
			collapse();
			break;
		default:
			break;
		}
	}
	
	
//	if(PostLike.equalsIgnoreCase("No")){
//		CallWsSetLike();
//	}else{
//		CallWsSetUnlike();
//	}
//	
	
	
	@Override
	protected void onRestart() {
		super.onRestart();
		//check for latest
		tempData.clear();
		tempData2.clear();
		if(WsType.equals("latest")){
			for(int i  = 1; i <= latest_page; i++){
				CallWsAfterLikeLatestFeed(i);
			}
			mListView.setSelection(AppSharedPrefrence.getInstance(NewsFeedScreen.this).Getindex());
		}else{
			for(int i  = 1; i <= localization_page; i++){
				CallWsMylocationAfterLike(i);
			}
			mListView.setSelection(AppSharedPrefrence.getInstance(NewsFeedScreen.this).Getindex());
		}
	}
		

	/**
	 * Web-service Response
	 */

	LinkedHashMap<String, Object> temp;
	ArrayList<LinkedHashMap<String, Object>> jsonData;
	ArrayList<Object> arrayList;
	LinkedHashMap<String, Object> data;
	ArrayList<Object> tempData = new ArrayList<Object>();
	ArrayList<Object> tempData2 = new ArrayList<Object>();

	

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
				case GetLatestPost:

					tempData=(ArrayList<Object>) temp.get("data");
					tempData2.addAll(tempData);

					//Maintain Scroll position
					int index = mListView.getFirstVisiblePosition();
					View v = mListView.getChildAt(0);
					int top = (v == null) ? 0 : v.getTop();

					newsfeedadapter = new AdapterNewsFeedList(NewsFeedScreen.this, jsonData,tempData2);
					newsfeedadapter.notifyDataSetChanged();
					mListView.setAdapter(newsfeedadapter);

					mListView.setSelectionFromTop(index, top);


//					tempData=new ArrayList<Object>();
//					tempData=(ArrayList<Object>) temp.get("data");
//					newsfeedadapter = new AdapterNewsFeedList(NewsFeedScreen.this, jsonData,tempData);
//					mListView.setAdapter(newsfeedadapter);

					break;

				case GetMyLocationPost:

					tempData=(ArrayList<Object>) temp.get("data");
					tempData2.addAll(tempData);

					//Maintain Scroll position
					int indexscroll = mListView.getFirstVisiblePosition();
					View view = mListView.getChildAt(0);
					int topScroll = (view == null) ? 0 : view.getTop();

					newsfeedadapter = new AdapterNewsFeedList(NewsFeedScreen.this, jsonData,tempData2);
					newsfeedadapter.notifyDataSetChanged();
					mListView.setAdapter(newsfeedadapter);

					mListView.setSelectionFromTop(indexscroll, topScroll);
	
//					tempData=new ArrayList<Object>();
//					tempData=(ArrayList<Object>) temp.get("data");
//					newsfeedadapter = new AdapterNewsFeedList(NewsFeedScreen.this, jsonData,tempData);
//					mListView.setAdapter(newsfeedadapter);

					break;

				case LikeRcode:

					tempData.clear();
					tempData2.clear();

					if(WsType.equals("latest")){
						for(int i  = 1; i <= latest_page; i++){
							CallWsAfterLikeLatestFeed(i);
						}
					}else{
						for(int i  = 1; i <= localization_page; i++){
							CallWsMylocationAfterLike(i);
							}
					}

					break;

				case UnlikeRcode:

					tempData.clear();
					tempData2.clear();

					if(WsType.equals("latest")){
						for(int i  = 1; i <= latest_page; i++){
							CallWsAfterLikeLatestFeed(i);
						}
						}else{
							for(int i  = 1; i <= localization_page; i++){
							CallWsMylocationAfterLike(i);
							}
						}

					break;

				case SetComment:

					tempData.clear();
					tempData2.clear();

					if(WsType.equals("latest")){

						for(int i  = 1; i <= latest_page; i++){
							CallWsAfterLikeLatestFeed(i);
						}
						}else{
							for(int i  = 1; i <= localization_page; i++){
								CallWsMylocationAfterLike(i);
								}
						}
					break;

				case GetLatestPostAfterLike:

					tempData=(ArrayList<Object>) temp.get("data");
					tempData2.addAll(tempData);

//					index = mListView.getFirstVisiblePosition();
//					v = mListView.getChildAt(0);
//					top = (v == null) ? 0 : v.getTop();

					newsfeedadapter = new AdapterNewsFeedList(NewsFeedScreen.this, jsonData,tempData2);
					//newsfeedadapter.notifyDataSetChanged();
					//mListView.invalidate();
					mListView.setAdapter(newsfeedadapter);

					//set the position after like post
					mListView.setSelection(AppSharedPrefrence.getInstance(NewsFeedScreen.this).Getindex());

					break;

				case GetMylocationAfterLike:

					tempData=(ArrayList<Object>) temp.get("data");
					tempData2.addAll(tempData);

					newsfeedadapter = new AdapterNewsFeedList(NewsFeedScreen.this, jsonData,tempData2);
					mListView.setAdapter(newsfeedadapter);

					//set the position after like post
					mListView.setSelection(AppSharedPrefrence.getInstance(NewsFeedScreen.this).Getindex());

				default:
					break;
				}
				 } else {

					mListView.setAdapter(null);
					//CommonUtility.showAlert(data.get("msg").toString(),NewsFeedScreen.this);
				 }
			} else {
				CommonUtility.showAlert(getString(R.string.there_is_some_problem_in_getting_data_), NewsFeedScreen.this);
			}
		} catch (Exception e) {
			CommonUtility.showAlert(getString(R.string.there_is_some_problem_in_getting_data_), NewsFeedScreen.this);
		}

	}

	/**
	 * Async Task that send a request to url Gets new list view data Appends to
	 * list view
	 * */
	private class loadMoreListView extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// Showing progress dialog before sending http request

			pDialog = new ProgressDialog(NewsFeedScreen.this,ProgressDialog.THEME_HOLO_LIGHT);
			pDialog.setIndeterminate(true);
			pDialog.setTitle(getString(R.string.loading_));
			pDialog.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
			pDialog.show();
		}

		protected Void doInBackground(Void... unused) {
			runOnUiThread(new Runnable() {
				public void run() {
					// increment current page

					if(WsType.equals("latest")){
						latest_page += 1;
						CallWsLatestFeed(latest_page);
					}else{
						localization_page +=1;
						CallWsMylocationFeed(localization_page);
					}

					// get listview current position - used to maintain scroll
					// position
					//int currentPosition = mListView.getFirstVisiblePosition();

					// Setting new scroll position
					//mListView.setSelectionFromTop(currentPosition + 1, 0);
				}
			});
			return (null);
		}

		protected void onPostExecute(Void unused) {
			// closing progress dialog
			pDialog.dismiss();
		}
	}
}
