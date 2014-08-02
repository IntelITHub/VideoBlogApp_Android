package com.iih.android.videoblog.notification;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.iih.android.videoblog.R;
import com.iih.android.videoblog.Parsing.PostDataAndGetData;
import com.iih.android.videoblog.Parsing.parseListner;
import com.iih.android.videoblog.category.PostDetailsActivity;
import com.iih.android.videoblog.category.SearchCategoryActivity;
import com.iih.android.videoblog.category.SelectedCatListAdapter;
import com.iih.android.videoblog.category.SelectedcategoryActivity;
import com.iih.android.videoblog.commonutill.CommonUtility;
import com.iih.android.videoblog.commonutill.CommonVariable;
import com.iih.android.videoblog.commonutill.CustomeAlertYesNo;
import com.iih.android.videoblog.gallery.CameraActivity;
import com.iih.android.videoblog.newsfeed.NewsFeedScreen;
import com.iih.android.videoblog.profile.ProfileActivity;

public class NotificationActivity extends Activity implements OnClickListener,
		parseListner {

	private TextView action_title;

	private Button actionleft;

	private ListView notifictn_Listview;
	private CommonUtility comonUti;
	private Intent intent;

	private Button btn_Me;
	private Button btn_Following;

	public PostDataAndGetData psd;
	private final int ResponseCodeMe = 0;
	private final int ResponseCodeFollowing = 1;

	ArrayList<String> UserIcon;
	ArrayList<String> Username;
	ArrayList<String> notfication;
	ArrayList<String> PostImage;

	private String PostId;

	NotificationListAdapter notificationAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notifiacation);

		try {
			comonUti = new CommonUtility(this);
			comonUti.categorytype = "Notification";
			comonUti.findViewById();
		} catch (Exception e) {
			e.printStackTrace();
		}

		initialization();

		btn_Me.setOnClickListener(this);
		btn_Following.setOnClickListener(this);

		notifictn_Listview.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				// TODO Auto-generated method stub
				
				notificationAdapter = (NotificationListAdapter)arg0.getAdapter();
				tempData=new ArrayList<Object>();

				tempData=(ArrayList<Object>) temp.get("data");
				data=(LinkedHashMap<String, Object>) tempData.get(position);

				PostId = data.get("iPostId").toString();

				if(PostId != null){
					CommonVariable.getInstance().setPost_id(PostId);
				}

				Intent intent = new Intent(NotificationActivity.this,PostDetailsActivity.class);
				startActivity(intent);
			}
		});

		CallWsMe();
	}

	/**
	 * Calling the web-service
	 */

	public void CallWsMe() {

		String Member_id = CommonVariable.getInstance().getMember_id();

		psd = new PostDataAndGetData(NotificationActivity.this, null, null,
				"GET", CommonUtility.BaseUrl2 + "newsFeed" + "&iProfilerId=" + Member_id + "&eNewsFeedType=" + "ME", ResponseCodeMe,0, false, true);
		psd.execute();
	
	}

	public void CallWsFollowing() {
		String Member_id = CommonVariable.getInstance().getMember_id();

		psd = new PostDataAndGetData(NotificationActivity.this, null, null,
				"GET", CommonUtility.BaseUrl2 + "newsFeed" + "&iProfilerId="+ Member_id + "&eNewsFeedType=" + "FOLLOWING",ResponseCodeFollowing, 0, false, true);
		psd.execute();

	}


	@Override
	public void onBackPressed() {
		CustomeAlertYesNo customedialog = new CustomeAlertYesNo(NotificationActivity.this);
		customedialog.show();
		// super.onBackPressed();
	}

	private void initialization() {

		btn_Me = (Button) findViewById(R.id.btn_me);
		btn_Following = (Button) findViewById(R.id.btn_following);

		notifictn_Listview = (ListView) findViewById(R.id.notificationlistview);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

		switch (id) {

		case R.id.imgbtn_Home:
			intent = new Intent(NotificationActivity.this, NewsFeedScreen.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
			startActivity(intent);
			finish();
			break;

		case R.id.imgbtn_search_category:
			intent = new Intent(NotificationActivity.this,SearchCategoryActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
			startActivity(intent);
			finish();
			break;

		case R.id.imgbtn_camera:
			intent = new Intent(NotificationActivity.this, CameraActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
			startActivity(intent);
			finish();
			break;

		case R.id.imgbtn_profile:

			intent = new Intent(NotificationActivity.this,ProfileActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
			startActivity(intent);
			finish();
			break;

		case R.id.btn_me:
			btn_Me.setBackgroundResource(R.drawable.ntf_me_white);
			btn_Following.setBackgroundResource(R.drawable.ntf_following_orange);
			btn_Me.setTextColor(getResources().getColor(R.color.themecolor));
			btn_Following.setTextColor(getResources().getColor(R.color.white));
			CallWsMe();
			break;

		case R.id.btn_following:
			btn_Me.setBackgroundResource(R.drawable.ntf_me_orange);
			btn_Following.setBackgroundResource(R.drawable.ntf_following_white);
			btn_Me.setTextColor(getResources().getColor(R.color.white));
			btn_Following.setTextColor(getResources().getColor(R.color.themecolor));
			CallWsFollowing();
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
	ArrayList<Object> tempData;
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

					switch (responce) {
					case ResponseCodeMe:

						UserIcon = new ArrayList<String>();
						Username = new ArrayList<String>();
						notfication = new ArrayList<String>();
						PostImage = new ArrayList<String>();

						tempData = new ArrayList<Object>();
						tempData = (ArrayList<Object>) temp.get("data");

						for (int i = 0; i < tempData.size(); i++) {
							data = (LinkedHashMap<String, Object>) tempData.get(i);

							UserIcon.add(data.get("vPicture").toString());
							Username.add(data.get("vUsername").toString());
							notfication.add(data.get("tDescription").toString());
							PostImage.add(data.get("vFile").toString());
						}

						notificationAdapter = new NotificationListAdapter(NotificationActivity.this,UserIcon,Username,notfication,PostImage,tempData);
						notifictn_Listview.setAdapter(notificationAdapter);
						notificationAdapter.notifyDataSetChanged();

						break;

					case ResponseCodeFollowing:

						UserIcon = new ArrayList<String>();
						Username = new ArrayList<String>();
						notfication = new ArrayList<String>();
						PostImage = new ArrayList<String>();

						tempData = new ArrayList<Object>();
						tempData = (ArrayList<Object>) temp.get("data");

						for (int i = 0; i < tempData.size(); i++) {
							data = (LinkedHashMap<String, Object>) tempData.get(i);

							UserIcon.add(data.get("vPicture").toString());
							Username.add(data.get("vUsername").toString());
							notfication.add(data.get("tDescription").toString());
							PostImage.add(data.get("vFile").toString());
						}

						notificationAdapter = new NotificationListAdapter(NotificationActivity.this,UserIcon,Username,notfication,PostImage,tempData);
						notifictn_Listview.setAdapter(notificationAdapter);
						notificationAdapter.notifyDataSetChanged();

						break;
					default:
						break;
					}
				} else {
					notifictn_Listview.setAdapter(null);
					CommonUtility.showAlert(data.get("msg").toString(),NotificationActivity.this);
				}
			} else {
				CommonUtility.showAlert(getString(R.string.there_is_some_problem_in_getting_data_), NotificationActivity.this);
			}
		} catch (Exception e) {
			CommonUtility.showAlert(getString(R.string.there_is_some_problem_in_getting_data_), NotificationActivity.this);
		}
	}
}
