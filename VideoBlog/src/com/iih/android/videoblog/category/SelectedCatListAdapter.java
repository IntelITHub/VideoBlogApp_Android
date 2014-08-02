package com.iih.android.videoblog.category;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iih.android.videoblog.R;
import com.iih.android.videoblog.commonutill.CommonVariable;
import com.iih.android.videoblog.newsfeed.CommentActivity;
import com.iih.android.videoblog.profile.ProfileActivity;
import com.iih.loadwebimage.imageutils.ImageLoader;

public class SelectedCatListAdapter extends BaseAdapter {

	// Context ctx = null;
	public Activity activity = null;
	public LayoutInflater mInflater = null;
	ArrayList<String> PostUsernameList = null;
	ArrayList<String> PostCategoryList = null;
	ArrayList<String> PostTitleList = null;
	ArrayList<String> PostImageList = null;
	ArrayList<String> PostLikesList = null;
	ArrayList<String> PostCommentList = null;
	ArrayList<String> PostDescription = null;
	ArrayList<Object> JsontempData = null;
	LinkedHashMap<String, Object> data;

	public ImageLoader imageLoader;

	public SelectedCatListAdapter(Activity activty, ArrayList<String> username,
			ArrayList<String> category, ArrayList<String> titlelist,
			ArrayList<String> imagelist, ArrayList<String> likelist,
			ArrayList<String> commentList, ArrayList<Object> tempData,ArrayList<String> descrption) {

		this.activity = activty;
		mInflater = activty.getLayoutInflater();
		this.PostUsernameList = username;
		this.PostCategoryList = category;
		this.PostTitleList = titlelist;
		this.PostImageList = imagelist;
		this.PostLikesList = likelist;
		this.PostCommentList = commentList;
		this.PostDescription = descrption;
		this.JsontempData = tempData;
		imageLoader = new ImageLoader(activity.getApplicationContext());
	}

	@Override
	public int getCount() {
		// return Categorylistarray.size();
		return PostTitleList.size();
	}

	@Override
	public Object getItem(int Position) {
		return PostTitleList.get(Position);
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
			convertView = mInflater.inflate(R.layout.selected_category_list_item, null);

//			int pos = position; 
//
//			if(pos % 2 == 0){
//				convertView.setBackgroundResource(R.color.white);
//			}else{
//				convertView.setBackgroundResource(R.color.gray);
//			}

			holder.posticon = (ImageView) convertView.findViewById(R.id.post_icons);
			holder.TitlePost = (TextView) convertView.findViewById(R.id.post_title);
			holder.PostUsername = (TextView) convertView.findViewById(R.id.post_username);
			holder.totalLikes = (TextView) convertView.findViewById(R.id.count_likes);
			holder.totalComments = (TextView) convertView.findViewById(R.id.Count_comment);
			holder.category_name = (TextView) convertView.findViewById(R.id.category_name);
			holder.ExpandComment = (ImageView) convertView.findViewById(R.id.expandDesc);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.TitlePost.setText(PostTitleList.get(position));
		holder.PostUsername.setText("@" + PostUsernameList.get(position));
		holder.category_name.setText(PostCategoryList.get(position));
		// Download Images from server and set into imageview

		imageLoader.DisplayImage(PostImageList.get(position), holder.posticon);
		holder.totalLikes.setText(PostLikesList.get(position));
		holder.totalComments.setText(PostCommentList.get(position));

		if (PostLikesList.get(position).equalsIgnoreCase("0")) {
			Drawable img = activity.getResources().getDrawable(R.drawable.countlike_white);
			img.setBounds(0, 0, 14, 14);
			// txtVw.setCompoundDrawables( img, null, null, null );
			holder.totalLikes.setCompoundDrawables(img, null, null, null);

		} else {
			Drawable img = activity.getResources().getDrawable(R.drawable.count_like_orange);
			img.setBounds(0, 0, 14, 14);
			// txtVw.setCompoundDrawables( img, null, null, null );
			holder.totalLikes.setCompoundDrawables(img, null, null, null);
		}

		if (PostCommentList.get(position).toString().equalsIgnoreCase("0")) {
			Drawable img = activity.getResources().getDrawable(R.drawable.countcomment_white);
			img.setBounds(0, 0, 14, 14);
			// txtVw.setCompoundDrawables( img, null, null, null );
			holder.totalComments.setCompoundDrawables(img, null, null, null);

		} else {
			Drawable img = activity.getResources().getDrawable(R.drawable.countcomment_orange);
			img.setBounds(0, 0, 14, 14);
			// txtVw.setCompoundDrawables( img, null, null, null );
			holder.totalComments.setCompoundDrawables(img, null, null, null);
		}

		holder.PostUsername.setTag(R.id.positionbtn, position);
		holder.PostUsername.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				// Profiler id for open a profile of particular user
				// This is Used when we click on username.

				int positionLayout = (Integer) view.getTag(R.id.positionbtn);

				data = (LinkedHashMap<String, Object>) JsontempData.get(positionLayout);
				//Passing this member id to Profile Activity
				String Profiler_id = data.get("iMemberId").toString(); // PostUsernameList.get(pos).toString();
				if (Profiler_id != null) {
					CommonVariable.getInstance().setProfiler_id(Profiler_id);
				}
				Intent intent = new Intent(activity,ProfileActivity.class);
				activity.startActivity(intent);
			}
		});

		holder.ExpandComment.setTag(R.id.positionbtn,position);
		holder.ExpandComment.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				int positionLayout = (Integer) view.getTag(R.id.positionbtn);

				data = (LinkedHashMap<String, Object>) JsontempData.get(positionLayout);
				CommonVariable.getInstance().setPost_id(data.get("iPostId").toString());
				Intent intent = new Intent(activity, CommentActivity.class);
				activity.startActivity(intent);
			}
		});
		

		return convertView;
	}

	public class ViewHolder {
		ImageView posticon;
		TextView TitlePost;
		TextView PostUsername;
		TextView totalLikes;
		TextView totalComments;
		TextView category_name;
		ImageView ExpandComment;
	}
}