package com.iih.android.videoblog.gallery;

import java.util.ArrayList;

import com.iih.android.videoblog.category.CategoryListAdapter;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

public class TextWatcherCountry implements TextWatcher {

	private int textlength = 0;
	private EditText editText;
	private ListView listView;
	private Activity activity;
	private ArrayList<String> serachList = new ArrayList<String>();
	private ArrayList<String> imgserachList = new ArrayList<String>();
	private ArrayList<String> serachListCopy;
	private ArrayList<String> imgserachListcopy;

	public TextWatcherCountry(EditText et, ListView listview, Activity a,ArrayList<String> searchArrayList,ArrayList<String> imgArraylist) {
		editText = et;
		listView = listview;
		activity = a;
		serachList = searchArrayList;
		imgserachList = imgArraylist;
		serachListCopy = new ArrayList<String>(serachList);
		imgserachListcopy = new ArrayList<String>(imgserachList);
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

		if (textlength == 0) {
			listView.setAdapter(new CategoryListAdapter(activity, serachListCopy,imgserachListcopy));
		} else {
			int len = serachListCopy.size();

			serachList.clear();

			imgserachList.clear();//clear old arraylist
			//String keyToSearch = editText.getText().toString();

			for (int i = 0; i < len; i++) {
				if (textlength <= serachListCopy.get(i).length()) {

					// only at start of the string
					if (editText.getText().toString().equalsIgnoreCase((String) serachListCopy.get(i).subSequence(0, textlength)))

					// contains character
					// if(serachListCopy.get(i).toString().contains(editText.getText().toString()))
					{
						serachList.add(serachListCopy.get(i));
						//Set images based on position and copy new searched array
						imgserachList.add(imgserachListcopy.get(i));
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

			listView.setAdapter(new CategoryListAdapter(activity, serachList,imgserachList));
		}
	}

}