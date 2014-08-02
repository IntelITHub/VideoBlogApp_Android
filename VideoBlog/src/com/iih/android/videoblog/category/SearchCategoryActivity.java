package com.iih.android.videoblog.category;

import java.text.BreakIterator;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.iih.android.videoblog.R;
import com.iih.android.videoblog.Parsing.PostDataAndGetData;
import com.iih.android.videoblog.Parsing.parseListner;
import com.iih.android.videoblog.commonutill.AppSharedPrefrence;
import com.iih.android.videoblog.commonutill.CommonUtility;
import com.iih.android.videoblog.commonutill.CommonVariable;
import com.iih.android.videoblog.commonutill.CustomeAlertYesNo;
import com.iih.android.videoblog.gallery.CameraActivity;
import com.iih.android.videoblog.newsfeed.NewsFeedScreen;
import com.iih.android.videoblog.notification.NotificationActivity;
import com.iih.android.videoblog.profile.ProfileActivity;

public class SearchCategoryActivity extends Activity implements OnClickListener, parseListner {

	private String Tag = "SearchCategoryActivity";
	private TextView action_title;
	private Button actionleft;
	private Button actionRight;
	private CommonUtility comonUti;

	private ListView categorylist;
	private EditText SearchEdittext;
	CategoryListAdapter categorylistAdapter;
	private Intent intent;

	ArrayList<String> Username;
	ArrayList<String> PostCategory;
	ArrayList<String> PostTitle;
	ArrayList<String> PostImage;
	ArrayList<String> PostLikes;
	ArrayList<String> PostComments;
	ArrayList<String> PostDescription;
	

	AllPostAdapter allpostAdapter;

	private String PostId;

	private final int ResponseCode = 0, RcodeAllpost = 1;
	PostDataAndGetData psd;
	ArrayList<String> CategoryName = new ArrayList<String>();
	ArrayList<String> CategoryIcon = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchcategory);

		try {
			comonUti = new CommonUtility(this);
			comonUti.categorytype = "searchcategory";
			comonUti.findViewById();
		} catch (Exception e) {
			e.printStackTrace();
		}

		initialization();

		CallWsCategory();

		CallWsAllPost();

		categorylist.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3) {

				if(AppSharedPrefrence.getInstance(SearchCategoryActivity.this).getPostOrCategory().equals("category")){
					categorylistAdapter = (CategoryListAdapter) arg0.getAdapter();

					//tempData=new ArrayList<Object>();
					//tempData=(ArrayList<Object>) temp.get("data");

					//data=(LinkedHashMap<String, Object>) tempData.get(position);

					ArrayList<Object> tempData1 = (ArrayList<Object>) CommonVariable.getInstance().getAllpostTemp();
				
					LinkedHashMap<String, Object> data1 = (LinkedHashMap<String, Object>) tempData1.get(position);

					String Category_id = data1.get("iCategoryId").toString();
					Intent intent = new Intent(SearchCategoryActivity.this,SelectedcategoryActivity.class);
					if(Category_id != null){
						CommonVariable.getInstance().setCategoryId(Category_id);
					}
					startActivity(intent);
				}else{

					allpostAdapter = (AllPostAdapter)arg0.getAdapter();

					tempData=new ArrayList<Object>();
					tempData=(ArrayList<Object>) temp.get("data");

					data=(LinkedHashMap<String, Object>) tempData.get(position);

					PostId = data.get("iPostId").toString();

					if(PostId != null){
						CommonVariable.getInstance().setPost_id(PostId);
					}

					Intent intent = new Intent(SearchCategoryActivity.this,PostDetailsActivity.class);
					startActivity(intent);
				}
				
//				finish();
			}
		});
	}

	private void initialization() {
//		action_title = (TextView) findViewById(R.id.action_title);
//		action_title.setText("Logo");

		actionleft = (Button) findViewById(R.id.action_Left);
		actionleft.setBackgroundResource(R.drawable.actionbar_back);
		actionleft.setVisibility(View.INVISIBLE);

		actionRight = (Button) findViewById(R.id.action_right);
		actionRight.setVisibility(View.INVISIBLE);

		categorylist = (ListView) findViewById(R.id.categoryList);
		SearchEdittext = (EditText) findViewById(R.id.edt_searchbox);
	}

	/**
	 * Calling the web-service
	 */

	public void CallWsCategory() {

		String memberid = CommonVariable.getInstance().getMember_id();

		psd = new PostDataAndGetData(SearchCategoryActivity.this, null, null,
				"GET",CommonUtility.BaseUrl2 +"getCategories"+"&iMemberId=" + memberid,ResponseCode, 0, false, true);
		psd.execute();
	}

	/**
	 * Calling the web-service
	 */

	public void CallWsAllPost() {

		psd = new PostDataAndGetData(SearchCategoryActivity.this, null, null,
				"GET",CommonUtility.BaseUrl2 +"getAllPost",RcodeAllpost, 0, false, true);

		try {
			psd.execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		CustomeAlertYesNo customedialog = new CustomeAlertYesNo(SearchCategoryActivity.this);
		customedialog.show();
		//super.onBackPressed();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();

		switch (id) {

		case R.id.imgbtn_Home:
			intent = new Intent(SearchCategoryActivity.this,NewsFeedScreen.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
			startActivity(intent);
			finish();
			break;

		case R.id.imgbtn_camera:
			intent = new Intent(SearchCategoryActivity.this,CameraActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
			startActivity(intent);
			finish();
			break;
		case R.id.imgbtn_notification:
			intent = new Intent(SearchCategoryActivity.this,NotificationActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
			startActivity(intent);
			finish();
			break;
		case R.id.imgbtn_profile:

			intent = new Intent(SearchCategoryActivity.this,ProfileActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}
	}

	/**
	 * Web-service Response
	 */

	LinkedHashMap<String, Object> temp;
	LinkedHashMap<String, Object> data;
	ArrayList<LinkedHashMap<String, Object>> jsonData;
	ArrayList<Object> arrayList;
	ArrayList<Object> tempData;
	ArrayList<Object> tempDataCopy;

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
				case ResponseCode:

					AppSharedPrefrence.getInstance(SearchCategoryActivity.this).setPostOrCategory("category");

					tempData=new ArrayList<Object>();
					tempData=(ArrayList<Object>) temp.get("data");

					//tempDataCopy.add(tempData);
					CommonVariable.getInstance().setAllPostTemp(tempData);

					for (int i = 0; i < tempData.size(); i++) {
						data=(LinkedHashMap<String, Object>) tempData.get(i);
						CategoryName.add(data.get("vCategory").toString());
						CategoryIcon.add(data.get("vImage").toString());
					}

					CommonVariable.getInstance().setAllPostData(data);

					categorylistAdapter = new CategoryListAdapter(SearchCategoryActivity.this, CategoryName,CategoryIcon);
					categorylist.setAdapter(categorylistAdapter);

					break;

				case RcodeAllpost:

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

						//seraching post by hashtag

						data=(LinkedHashMap<String, Object>) tempData.get(i);
						Username.add(data.get("vUsername").toString());
						PostCategory.add(data.get("vCategoriesName").toString());
						PostTitle.add(data.get("tPost").toString());
						PostImage.add(data.get("vFile").toString());
						PostLikes.add(data.get("totalPostLikes").toString());
						PostComments.add(data.get("totalPostComments").toString());
						PostDescription.add(data.get("tDescription").toString());
						
					}

					//allpostAdapter = new AllPostAdapter(SearchCategoryActivity.this,Username,PostCategory,PostTitle,PostImage,PostLikes,PostComments,tempData);
					//categorylist.setAdapter(allpostAdapter);

					TextWatcherCategorylist myTextWatcher = new TextWatcherCategorylist(SearchEdittext, categorylist,SearchCategoryActivity.this, Username,PostCategory,PostTitle,PostImage,PostLikes,PostComments,tempData,PostDescription);
					SearchEdittext.addTextChangedListener(myTextWatcher);

					break;

				default:
					break;
				}
				 } else {
					 CommonUtility.showAlert(data.get("msg").toString(),SearchCategoryActivity.this); 
				}
			} else {
				CommonUtility.showAlert(getString(R.string.there_is_some_problem_in_getting_data_),SearchCategoryActivity.this);
			}
		} catch (Exception e) {
			CommonUtility.showAlert(getString(R.string.there_is_some_problem_in_getting_data_),SearchCategoryActivity.this);
		}
	}
}
