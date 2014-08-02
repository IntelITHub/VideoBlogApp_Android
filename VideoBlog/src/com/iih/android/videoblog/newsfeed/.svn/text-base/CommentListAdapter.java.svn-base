package com.iih.android.videoblog.newsfeed;

import java.util.ArrayList;

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

public class CommentListAdapter extends BaseAdapter {

	Context ctx = null;
	private LayoutInflater mInflater = null;
	ArrayList<String> ComntList=new ArrayList<String>();
	ArrayList<String> userList=new ArrayList<String>();
	ArrayList<String> mComntimeList=new ArrayList<String>();
	ArrayList<String> picList=new ArrayList<String>();
	public ImageLoader imageLoader; 

	public CommentListAdapter(Activity activty, ArrayList<String> comntList, ArrayList<String> userList, ArrayList<String> mComntimeList, ArrayList<String> picList) {
		this.ctx = activty;
		
		this.ComntList=comntList;
		this.userList=userList;
		this.mComntimeList=mComntimeList;
		this.picList=picList;
		mInflater = activty.getLayoutInflater();
		imageLoader=new ImageLoader(ctx.getApplicationContext());
	}

	@Override
	public int getCount() {
		return ComntList.size();
	}

	
	@Override
	public Object getItem(int position) {
		return ComntList.get(position);
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
			convertView = mInflater.inflate(R.layout.comment_item, null);

			holder.txtComnt = (TextView) convertView.findViewById(R.id.comment);
			holder.txtUserName = (TextView) convertView.findViewById(R.id.username);
			holder.txtCommentTime = (TextView) convertView.findViewById(R.id.commenttime);
			holder.imgProfilePic = (ImageView) convertView.findViewById(R.id.profilepic);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.txtComnt.setText(this.ComntList.get(position).toString());
		holder.txtUserName.setText("@"+this.userList.get(position).toString());
		holder.txtCommentTime.setText(this.mComntimeList.get(position).toString());

		imageLoader.DisplayImage(this.picList.get(position).toString(), holder.imgProfilePic);

//		holder.txtComnt.setText(this.ComntList.get(position).toString());
		return convertView;
	}

	private static class ViewHolder {

		ImageView imgProfilePic;
		TextView txtComnt;
		TextView txtUserName;
		TextView txtCommentTime;

	}
}