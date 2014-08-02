package com.iih.android.videoblog.category;

import java.util.ArrayList;

import android.R.string;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;


public class TextWatcherSelectedCategorylist implements TextWatcher {

	private int textlength = 0;
	private EditText editText;
	private Activity activity;
	private ListView myListview;
	private String checktype;
	
	private ArrayList<String> UsernameList = new ArrayList<String>();
	private ArrayList<String> PostCategoryList = new ArrayList<String>();
	private ArrayList<String> PostTitleList = new ArrayList<String>();
	private ArrayList<String> PostImageList = new ArrayList<String>();
	private ArrayList<String> PostLikes = new ArrayList<String>();
	private ArrayList<String> PostComments = new ArrayList<String>();
	private ArrayList<Object> JsontempData =  new ArrayList<Object>();
	private ArrayList<String> postDescription =  new ArrayList<String>();

	private ArrayList<String> UsernameListCopy;
	private ArrayList<String> PostCategoryListCopy;
	private ArrayList<String> PostTitleListCopy;
	private ArrayList<String> PostImageListcopy;
	private ArrayList<String> PostLikesCopy;
	private ArrayList<String> PostCommentsCopy;
	private ArrayList<String> PostDescriptionCopy;
	private ArrayList<Object> JsontempDataListCopy;

	public TextWatcherSelectedCategorylist(EditText et, ListView listview,Activity a,ArrayList<String> username,ArrayList<String> category,
			ArrayList<String> postTitle,ArrayList<String> postImage,ArrayList<String> postlikes,ArrayList<String> postComments, ArrayList<Object> tempData,ArrayList<String> postdesc) {

		editText = et;
		activity = a;
		myListview = listview;
		UsernameList = username;
		PostCategoryList = category;
		PostTitleList = postTitle;
		PostImageList = postImage;
		PostLikes = postlikes;
		PostComments = postComments;
		JsontempData = tempData;
		postDescription= postdesc;

		UsernameListCopy = new ArrayList<String>(UsernameList);
		PostCategoryListCopy = new ArrayList<String>(PostCategoryList);
		PostTitleListCopy = new ArrayList<String>(PostTitleList);
		PostImageListcopy = new ArrayList<String>(PostImageList);
		PostLikesCopy = new ArrayList<String>(PostLikes);
		PostCommentsCopy = new ArrayList<String>(PostComments);
		PostDescriptionCopy = new ArrayList<String>(postDescription);
		JsontempDataListCopy = new ArrayList<Object>(JsontempData);
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,int after) {
		// TODO Auto-generated method stub

	}

	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

		textlength = editText.getText().length();

		checktype=editText.getText().toString().trim();

		if (textlength == 0) {
			//listView.setAdapter(new SelectedCatListAdapter(activity,UsernameListCopy,PostCategoryListCopy,PostTitleListCopy,PostImageListcopy,PostLikesCopy,PostCommentsCopy));
			myListview.setAdapter(new SelectedCatListAdapter(activity,UsernameListCopy,PostCategoryListCopy,PostTitleListCopy,PostImageListcopy,PostLikesCopy,PostCommentsCopy,JsontempDataListCopy,PostDescriptionCopy));
		} else {
			
			//String keyToSearch = editText.getText().toString();
			if(checktype.startsWith("@", 0)){

				checktype = checktype.replace("@", "");
				textlength=checktype.length();
				int len = UsernameListCopy.size();

				//clear old arraylist
				UsernameList.clear();
				PostCategoryList.clear();
				PostTitleList.clear();
				PostImageList.clear();
				PostLikes.clear();
				PostComments.clear();
				postDescription.clear();
				JsontempData.clear();

			for (int i = 0; i < len; i++) {
				if (textlength <= UsernameListCopy.get(i).length()) {

					// only at start of the string
					if (checktype.equalsIgnoreCase((String) UsernameListCopy.get(i).subSequence(0, textlength)))

					// contains character
					// if(serachListCopy.get(i).toString().contains(editText.getText().toString()))
					{
						UsernameList.add(UsernameListCopy.get(i));
						PostCategoryList.add(PostCategoryListCopy.get(i));
						PostTitleList.add(PostTitleListCopy.get(i));
						PostImageList.add(PostImageListcopy.get(i));
						//Set images based on position and copy new searched array
						PostLikes.add(PostLikesCopy.get(i));
						PostComments.add(PostCommentsCopy.get(i));
						postDescription.add(PostCommentsCopy.get(i));
						JsontempData.add(JsontempDataListCopy.get(i));
					}
				}
			}
			}else if(checktype.startsWith("#", 0)){

				checktype = checktype.replace("#", "");
				textlength=checktype.length();
				int len1 = PostDescriptionCopy.size();

				//clear old arraylist
				UsernameList.clear();
				PostCategoryList.clear();
				PostTitleList.clear();
				PostImageList.clear();
				PostLikes.clear();
				PostComments.clear();
				postDescription.clear();
				JsontempData.clear();

			for (int i = 0; i < len1; i++) {
				if (textlength <= PostDescriptionCopy.get(i).length()) {

					// only at start of the string
					if (checktype.equalsIgnoreCase((String) PostDescriptionCopy.get(i).subSequence(0, textlength)))

					// contains character
					// if(serachListCopy.get(i).toString().contains(editText.getText().toString()))
					{
						UsernameList.add(UsernameListCopy.get(i));
						PostCategoryList.add(PostCategoryListCopy.get(i));
						PostTitleList.add(PostTitleListCopy.get(i));
						PostImageList.add(PostImageListcopy.get(i));
						//Set images based on position and copy new searched array
						PostLikes.add(PostLikesCopy.get(i));
						PostComments.add(PostCommentsCopy.get(i));
						postDescription.add(PostCommentsCopy.get(i));
						JsontempData.add(JsontempDataListCopy.get(i));
					}
				}
			}
			}else{
				int len = PostTitleListCopy.size();

				//clear old arraylist
				UsernameList.clear();
				PostCategoryList.clear();
				PostTitleList.clear();
				PostImageList.clear();
				PostLikes.clear();
				PostComments.clear();
				postDescription.clear();
				JsontempData.clear();

				for (int i = 0; i < len; i++) {

					if (textlength <= PostTitleListCopy.get(i).length()) {

						// only at start of the string
						if (editText.getText().toString().equalsIgnoreCase((String) PostTitleListCopy.get(i).subSequence(0, textlength)))

						// contains character
						// if(serachListCopy.get(i).toString().contains(editText.getText().toString()))
						{
							UsernameList.add(UsernameListCopy.get(i));
							PostCategoryList.add(PostCategoryListCopy.get(i));
							PostTitleList.add(PostTitleListCopy.get(i));
							PostImageList.add(PostImageListcopy.get(i));
							//Set images based on position and copy new searched array
							PostLikes.add(PostLikesCopy.get(i));
							PostComments.add(PostCommentsCopy.get(i));
							postDescription.add(PostCommentsCopy.get(i));
							JsontempData.add(JsontempDataListCopy.get(i));
						}
					}
				}
			}
			/*
			 * Collections.binarySearch(serachListCopy,keyToSearch,new
			 * Comparator<String>() {
			 * 
			 * @Override public int compare(String lhs, String rhs) { return 0;
			 * } });
			 */
//			listView.setAdapter(new CustomAlertAdapterPlace(activity, serachList,CommonUtil.Iconlist()));
			myListview.setAdapter(new SelectedCatListAdapter(activity,UsernameList,PostCategoryList,PostTitleList,PostImageList,PostLikes,PostComments,JsontempData,postDescription));
		}
	}

	
}
