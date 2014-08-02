package com.iih.android.videoblog.gallery;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iih.android.videoblog.R;

public class CountyListAdapter extends BaseAdapter {

	Context ctx = null;
	private LayoutInflater mInflater = null;
	ArrayList<String> CountyList = null;
	ArrayList<String> CountyID = null;

	public CountyListAdapter(Activity activty,ArrayList<String> countylist, ArrayList<String> countryID) {
		this.ctx = activty;
		mInflater = LayoutInflater.from(ctx);
		this.CountyList = countylist;
		this.CountyID = countryID;
	}

	@Override
	public int getCount() {
		return CountyList.size();
	}

	@Override
	public Object getItem(int Position) {
		return CountyList.get(Position);
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
			convertView = mInflater.inflate(R.layout.statecounty_list_dialog, null);

			holder.txt_Country =(TextView)convertView.findViewById(R.id.txt_statecounty_Name);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.txt_Country.setText(CountyList.get(position));

		return convertView;
	}

	private class ViewHolder {
		TextView txt_Country;

	}

}
