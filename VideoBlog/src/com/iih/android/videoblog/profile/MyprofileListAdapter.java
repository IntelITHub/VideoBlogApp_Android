package com.iih.android.videoblog.profile;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iih.android.videoblog.R;
import com.iih.loadwebimage.imageutils.ImageLoader;


public class MyprofileListAdapter extends BaseAdapter {

	Context ctx = null;
	private LayoutInflater mInflater = null;

	LinkedHashMap<String, Object> data;
	ImageLoader imageLoader;
	 ArrayList<Object> mData;
	public MyprofileListAdapter(Activity activty, ArrayList<Object> tempData) {
		this.ctx = activty;
		mInflater = LayoutInflater.from(ctx);
		imageLoader = new ImageLoader(ctx.getApplicationContext());
		mData=tempData;
	}

	@Override
	public int getCount() {
		// return Categorylistarray.size();
		return mData.size();
	}

	@Override
	public Object getItem(int pos) {
		return mData.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.profilelistitem, null);

			holder.txtUname=(TextView) convertView.findViewById(R.id.username);
			holder.txtPostTime=(TextView) convertView.findViewById(R.id.posttime);
			holder.txtDesc=(TextView) convertView.findViewById(R.id.posttext);
			holder.imgProfile=(ImageView) convertView.findViewById(R.id.profilepic);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		data=(LinkedHashMap<String, Object>) mData.get(position);
		holder.txtUname.setText("@" + data.get("vUsername").toString());
//		holder.txtPostTime.setText(data.get("vUsername").toString());
		holder.txtDesc.setText(data.get("tDescription").toString());
		imageLoader.DisplayImage(data.get("vFile").toString(), holder.imgProfile);
		holder.txtPostTime.setText(data.get("dCreatedDate").toString());
		
		/*
		 * String datavalue = Categorylistarray.get(position);
		 * holder.categoryname.setText(datavalue);
		 * holder.categoryicons.setImageResource
		 * (iconlistarray.get(position));
		 */

		return convertView;
	}

	private class ViewHolder {

		private TextView txtUname;
		private TextView txtPostTime;
		private TextView txtDesc;
		private ImageView imgProfile;
		
		
	}
}


