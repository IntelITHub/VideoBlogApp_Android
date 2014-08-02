package com.iih.android.videoblog.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.iih.android.videoblog.R;
import com.iih.android.videoblog.Parsing.PostDataAndGetData;
import com.iih.android.videoblog.Parsing.parseListner;
import com.iih.android.videoblog.commonutill.CommonUtility;
import com.iih.android.videoblog.commonutill.CommonVariable;

public class UserPostListActivity extends Activity implements parseListner {

	private Button actionleft;
	private Button actionRight;

	private CommonUtility comonUti;
	private ListView UserpostListview;

	ProgressDialog pDialog;

	private final int getPostRCode = 0;

	private String IsMyProfile = "";
	Boolean IsEdit = true;

	/**
	 * Calling the web-service
	 */
	HashMap<String, String> Paramter;
	PostDataAndGetData psd;
	HashMap<String, String> Parameter = null;

	// Flag for current page
		int Current_page = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.users_postlist);

		initialization();

		// MyprofileListAdapter Adapter = new
		// MyprofileListAdapter(ProfileActivity.this);
		// mypostListview.setAdapter(Adapter);

		Button btnLoadMore = new Button(this);
		btnLoadMore.setBackgroundColor(getResources().getColor(R.color.themecolor));
		btnLoadMore.setTextColor(getResources().getColor(R.color.white));
		btnLoadMore.setText(R.string.load_more_);
		btnLoadMore.setHeight((int) getResources().getDimension(R.dimen.btnsize));
		btnLoadMore.setTextSize(getResources().getDimension(R.dimen.loadbtnsize));

		// Adding button to listview at footer
		UserpostListview.addFooterView(btnLoadMore);

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

		CallWsgetUserPost(Current_page);
	}

	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		CallWsgetUserPost(Current_page);
	}

	public void CallWsgetUserPost(int page) {

		String PostId = CommonVariable.getInstance().getPost_id();
		String Member_Id = CommonVariable.getInstance().getMember_id();

		psd = new PostDataAndGetData(UserPostListActivity.this, null, null,
				"GET",CommonUtility.BaseUrl2 +"getPostdetails&iPostId="+ PostId+"&iMemberId="+Member_Id +"&page=" + page,getPostRCode, 0, false, true);
		psd.execute();

	}

	private void initialization() {

		// action_title = (TextView) findViewById(R.id.action_title);
		// action_title.setText("Logo");

		actionleft = (Button) findViewById(R.id.action_Left);
		actionleft.setVisibility(View.INVISIBLE);

		actionRight = (Button) findViewById(R.id.action_right);

		UserpostListview = (ListView) findViewById(R.id.listview_newsfeed);

	}

	/**
	 * Web-service Response
	 */

	LinkedHashMap<String, Object> temp;
	ArrayList<LinkedHashMap<String, Object>> jsonData;
	ArrayList<Object> arrayList;
	LinkedHashMap<String, Object> data;
	LinkedHashMap<String, Object> UserFeeddata;
	ArrayList<Object> tempData = new ArrayList<Object>();
	ArrayList<Object> tempData2 = new ArrayList<Object>();;

	// LinkedHashMap<String, Object> tempdata;

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
					case getPostRCode:
						data = (LinkedHashMap<String, Object>) temp.get("data");
						// tempdata=data;

						try {
							UserFeeddata = (LinkedHashMap<String, Object>) data.get("newsFeeds");

							tempData = (ArrayList<Object>) UserFeeddata.get("data");
							tempData2.addAll(tempData);

							int index = UserpostListview.getFirstVisiblePosition();
							View v = UserpostListview.getChildAt(0);
							int top = (v == null) ? 0 : v.getTop();

							UserPosListAdapter Adapter = new UserPosListAdapter(UserPostListActivity.this, tempData2);
							Adapter.notifyDataSetChanged();
							UserpostListview.setAdapter(Adapter);

							UserpostListview.setSelectionFromTop(index, top);

						} catch (Exception e) {
							e.printStackTrace();
						}

						break;

					default:
						break;
					}
				} else {
					CommonUtility.showAlert(data.get("msg").toString(),UserPostListActivity.this);
				}
			} else {
				CommonUtility.showAlert(getString(R.string.there_is_some_problem_in_getting_data_),UserPostListActivity.this);
			}
		} catch (Exception e) {
			CommonUtility.showAlert(getString(R.string.there_is_some_problem_in_getting_data_),UserPostListActivity.this);
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
			pDialog = new ProgressDialog(UserPostListActivity.this,ProgressDialog.THEME_HOLO_LIGHT);
			pDialog.setIndeterminate(true);
			pDialog.setTitle(getString(R.string.loading_));
			pDialog.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
			pDialog.show();
		}

		protected Void doInBackground(Void... unused) {
			runOnUiThread(new Runnable() {
				public void run() {
					// increment current page

					
						Current_page += 1;
						CallWsgetUserPost(Current_page);

					// get listview current position - used to maintain scroll
					// position
//					int currentPosition = UserpostListview.getFirstVisiblePosition();
//
//					// Setting new scroll position
//					UserpostListview.setSelectionFromTop(currentPosition + 1, 0);
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
