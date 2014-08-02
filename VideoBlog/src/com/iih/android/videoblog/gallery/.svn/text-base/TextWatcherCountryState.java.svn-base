package com.iih.android.videoblog.gallery;

import java.util.ArrayList;

import com.iih.android.videoblog.category.CategoryListAdapter;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

public class TextWatcherCountryState implements TextWatcher {

	private int textlength = 0;
	private EditText editText;
	private ListView listView;
	private Activity activity;
	private ArrayList<String> serachCountyList = new ArrayList<String>();
	private ArrayList<String> serachCountyIDList = new ArrayList<String>();
	private ArrayList<String> serachCountryListCopy;
	private ArrayList<String> serachCountryIDCopy;

	public TextWatcherCountryState(EditText et, ListView listview, Activity a,ArrayList<String> searchCountryArrayList,ArrayList<String> CountryIdArrayList) {
		editText = et;
		listView = listview;
		activity = a;
		serachCountyList = searchCountryArrayList;
		serachCountyIDList = CountryIdArrayList;
		serachCountryListCopy = new ArrayList<String>(serachCountyList);
		serachCountryIDCopy = new ArrayList<String>(serachCountyIDList);
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

		textlength = editText.getText().length();

		if (textlength == 0) {
			listView.setAdapter(new CountyListAdapter(activity,serachCountryListCopy,serachCountryIDCopy));
		} else {
			int len = serachCountryListCopy.size();

			serachCountyList.clear();
			serachCountyIDList.clear();

			// clear old arraylist
			// String keyToSearch = editText.getText().toString();

			for (int i = 0; i < len; i++) {
				if (textlength <= serachCountryListCopy.get(i).length()) {

					// only at start of the string
					if (editText.getText().toString().equalsIgnoreCase((String) serachCountryListCopy.get(i).subSequence(0, textlength)))

					// contains character
					// if(serachListCopy.get(i).toString().contains(editText.getText().toString()))
					{
						serachCountyList.add(serachCountryListCopy.get(i));
						serachCountyIDList.add(serachCountryIDCopy.get(i));
						// Set images based on position and copy new searched
						// array
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

			listView.setAdapter(new CountyListAdapter(activity,serachCountyList,serachCountyIDList));
		}
	}

}