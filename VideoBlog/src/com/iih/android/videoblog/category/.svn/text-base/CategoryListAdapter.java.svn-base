package com.iih.android.videoblog.category;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iih.android.videoblog.R;
import com.iih.loadwebimage.imageutils.ImageLoader;

public class CategoryListAdapter extends BaseAdapter {

	Activity activity = null;
	ArrayList<String> Categorylistarray = null;
	ArrayList<String> iconlistarray = null;
	private LayoutInflater mInflater = null;
	public ImageLoader imageLoader; 

	public CategoryListAdapter(Activity activty, ArrayList<String> list,ArrayList<String> iconlist) {

		this.activity = activty;
		mInflater = activty.getLayoutInflater();
		this.Categorylistarray = list;
		this.iconlistarray = iconlist;
		imageLoader=new ImageLoader(activity.getApplicationContext());
	}

	@Override
	public int getCount() {
		return Categorylistarray.size();
	}

	@Override
	public Object getItem(int position) {
		return Categorylistarray.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {

		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.search_cat_list_item, null);

			if(position % 2 == 0){
				convertView.setBackgroundResource(R.color.white);
			}else{
				convertView.setBackgroundResource(R.color.gray);
			}

			holder.categoryname = (TextView) convertView.findViewById(R.id.category_name);
			holder.categoryicons = (ImageView)convertView.findViewById(R.id.category_icon);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String datavalue = Categorylistarray.get(position);
		holder.categoryname.setText(datavalue);
//		CommonUtility.loadImage(new AQuery(activity), iconlistarray.get(position),  holder.categoryicons);
		imageLoader.DisplayImage(iconlistarray.get(position), holder.categoryicons);
		return convertView;
	}

	private static class ViewHolder {
		TextView categoryname;
		ImageView categoryicons;
	}
}