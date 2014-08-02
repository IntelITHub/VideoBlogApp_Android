package com.iih.android.videoblog.notification;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iih.android.videoblog.R;
import com.iih.android.videoblog.commonutill.CommonVariable;
import com.iih.android.videoblog.profile.ProfileActivity;
import com.iih.loadwebimage.imageutils.ImageLoader;

public class NotificationListAdapter extends BaseAdapter {

	Activity activity;
	private LayoutInflater mInflater = null;
	private ArrayList<String> userIconList = null;
	private ArrayList<String> usernameList = null;
	private ArrayList<String> notficationList = null;
	private ArrayList<String> postImageList = null;

	ArrayList<Object> JsontempData = null;
	LinkedHashMap<String, Object> data;

	public ImageLoader imageLoader;

	public NotificationListAdapter(Activity activty,
			ArrayList<String> userIcon, ArrayList<String> username,
			ArrayList<String> notfication, ArrayList<String> postImage, ArrayList<Object> tempData) {
		this.activity = activty;
		mInflater = activty.getLayoutInflater();
		this.userIconList = userIcon;
		this.usernameList = username;
		this.notficationList = notfication;
		this.postImageList = postImage;
		this.JsontempData = tempData;
		imageLoader=new ImageLoader(activity.getApplicationContext());
	}

	@Override
	public int getCount() {
		return usernameList.size();
	}

	@Override
	public Object getItem(int Position) {
		return usernameList.get(Position);
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
			convertView = mInflater.inflate(R.layout.notification_item, null);

			holder.img_UserIcon = (ImageView) convertView.findViewById(R.id.profilepic);
			holder.txt_Username = (TextView) convertView.findViewById(R.id.username);
			holder.txt_notification = (TextView) convertView.findViewById(R.id.notificationtext);
			holder.img_postImage = (ImageView) convertView.findViewById(R.id.posticon);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		imageLoader.DisplayImage( userIconList.get(position), holder.img_UserIcon);
		holder.txt_Username.setText("@"+usernameList.get(position));
		holder.txt_notification.setText(notficationList.get(position));
		imageLoader.DisplayImage( postImageList.get(position), holder.img_postImage);

		holder.txt_Username.setTag(R.id.position,position);
		holder.txt_Username.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int position = (Integer)v.getTag(R.id.position);

				data = (LinkedHashMap<String, Object>)JsontempData.get(position);
				//Passing this member id to Profile Activity
				String Profiler_id = data.get("iMemberId").toString(); // PostUsernameList.get(pos).toString();
				if (Profiler_id != null) {
					CommonVariable.getInstance().setProfiler_id(Profiler_id);
				}
				Intent intent = new Intent(activity,ProfileActivity.class);
				activity.startActivity(intent);
			}
		});
		return convertView;
	}

	private static class ViewHolder {
		ImageView img_UserIcon;
		TextView txt_Username;
		TextView txt_notification;
		ImageView img_postImage;

	}
}