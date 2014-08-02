package com.iih.android.videoblog.profile;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.iih.android.videoblog.R;
import com.iih.android.videoblog.commonutill.AppSharedPrefrence;
import com.iih.android.videoblog.commonutill.CommonVariable;
import com.iih.android.videoblog.newsfeed.CommentActivity;
import com.iih.android.videoblog.newsfeed.VideoPlayer;
import com.iih.loadwebimage.imageutils.ImageLoader;

public class UserPosListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	Activity activity;
	public ArrayList<LinkedHashMap<String, Object>> jsonLocal;
	public ImageLoader imageLoader;
	LinkedHashMap<String, Object> data;
	ArrayList<Object> tempData;
	ArrayList<Object> mAllData;
	private String strComment;
	private Boolean flag = false;

	int count = 0;

	public UserPosListAdapter(Activity act, ArrayList<Object> AllData) {
		activity = act;
		// arrayList = jsonData;
		// arrayList = (ArrayList<Object>) jsonData;
		mInflater = LayoutInflater.from(activity);
		imageLoader = new ImageLoader(activity.getApplicationContext());
		mAllData=AllData;
	}

	public int getCount() {
		return mAllData.size();
	}

	public Object getItem(int position) {
		return mAllData.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("unchecked")
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.userpost_item, null);
			holder = new ViewHolder();

			holder.imgProfilePic = (ImageView) convertView.findViewById(R.id.imgProfilePic);
			holder.imgFeed = (ImageView) convertView.findViewById(R.id.imgfeed);

			holder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
			holder.txtTitle = (TextView) convertView.findViewById(R.id.txt_title);
			holder.txtDesc = (TextView) convertView.findViewById(R.id.txt_desc);
			holder.txtLike = (TextView) convertView.findViewById(R.id.txt_like);
			holder.txtComment = (TextView) convertView.findViewById(R.id.txt_comment);
			holder.txtTime =(TextView)convertView.findViewById(R.id.posttime);
			holder.imgVicon = (ImageView) convertView.findViewById(R.id.v_icon); 

			holder.ExpandComment =(Button)convertView.findViewById(R.id.post_expand);
			//holder.CommentLayout = (RelativeLayout)convertView.findViewById(R.id.commentLayout);

			//holder.edt_Comment = (EditText)convertView.findViewById(R.id.commentedittttext);
			//holder.btnsend = (Button)convertView.findViewById(R.id.sendbtn);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.ExpandComment.setTag(R.id.position, position);

		//holder.Comment_btn.setTag(R.id.position, position);
		//holder.like_btn.setTag(R.id.position, position);
		holder.imgVicon.setTag(R.id.position, position);

//		holder.like_btn.setOnClickListener(new OnClickListener() {
//
//			@SuppressWarnings("unchecked")
//			@Override
//			public void onClick(View view) {
//				int position = (Integer) view.getTag(R.id.position);
////				LinkedHashMap<String, Object> likeData = jsonLocal.get(position);
//
////				tempData=new ArrayList<Object>();
////				tempData=(ArrayList<Object>) likeData.get("data");
////				data=(LinkedHashMap<String, Object>) tempData.get(position);
//
//				data=(LinkedHashMap<String, Object>) mAllData.get(position);
//				String PostLike = data.get("isPostLike").toString();
//				CommonVariable.getInstance().setPost_id(data.get("iPostId").toString());
//				//this is wrong 
//				//CommonVariable.getInstance().setMember_id(data.get("iMemberId").toString());
//
//
//			}
//		});

//		holder.Comment_btn.setOnClickListener(new OnClickListener() {
//
//			@SuppressWarnings("unchecked")
//			@Override
//			public void onClick(View view) {
//				//holder.Comment_btn.setTextColor(activity.getResources().getColorStateList(R.color.white));
//
//				int position = (Integer) view.getTag(R.id.position); 
//
//				data=(LinkedHashMap<String, Object>) mAllData.get(position);
//				CommonVariable.getInstance().setPost_id(data.get("iPostId").toString());
//				Intent intent = new Intent(activity, CommentActivity.class);
//				activity.startActivity(intent);
//			}
//		});

		holder.ExpandComment.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View view) {
				//holder.Comment_btn.setTextColor(activity.getResources().getColorStateList(R.color.white));

				int position = (Integer) view.getTag(R.id.position); 

				data=(LinkedHashMap<String, Object>) mAllData.get(position);
				CommonVariable.getInstance().setPost_id(data.get("iPostId").toString());
				Intent intent = new Intent(activity, CommentActivity.class);
				activity.startActivity(intent);
			}
		});

//		LinkedHashMap<String, Object> temp = jsonLocal.get(position);
		

		try {
			data=(LinkedHashMap<String, Object>) mAllData.get(position);
//			data= (LinkedHashMap<String, Object>) temp.get("data");
		} catch (Exception e) {
			e.printStackTrace();
		}

		imageLoader.DisplayImage(data.get("vPicture").toString(),holder.imgProfilePic);

		holder.txtName.setText("@" + data.get("vUsername").toString());
		holder.txtTitle.setText(data.get("tPost").toString());
		holder.txtDesc.setText(data.get("tDescription").toString());
		holder.txtLike.setText(data.get("totalPostLikes").toString());// tLikes
		holder.txtComment.setText(data.get("totalPostComments").toString());// tComments
		holder.txtTime.setText(data.get("dCreatedDate").toString());
//		 "eFileType": "Video",

		//Checking num of likes
		if (data.get("totalPostLikes").toString().equalsIgnoreCase("0")) {
			Drawable img = activity.getResources().getDrawable(R.drawable.countlike_white);
			img.setBounds(0, 0, 14, 14);
			// txtVw.setCompoundDrawables( img, null, null, null );
			holder.txtLike.setCompoundDrawables(img, null, null, null);
		} else {
			Drawable img = activity.getResources().getDrawable(R.drawable.count_like_orange);
			img.setBounds(0, 0, 14, 14);
			// txtVw.setCompoundDrawables( img, null, null, null );
			holder.txtLike.setCompoundDrawables(img, null, null, null);
		}

		//Checking num of comments
		if (data.get("totalPostComments").toString().equalsIgnoreCase("0")) {
			Drawable img = activity.getResources().getDrawable(R.drawable.countcomment_white);
			img.setBounds(0, 0, 14, 14);
			// txtVw.setCompoundDrawables( img, null, null, null );
			holder.txtComment.setCompoundDrawables(img, null, null, null);
		} else {
			Drawable img = activity.getResources().getDrawable(R.drawable.countcomment_orange);
			img.setBounds(0, 0, 14, 14);
			// txtVw.setCompoundDrawables( img, null, null, null );
			holder.txtComment.setCompoundDrawables(img, null, null, null);
		}

		if (data.get("eFileType").toString().equalsIgnoreCase("Video")) {
			holder.imgVicon.setVisibility(View.VISIBLE);
			imageLoader.DisplayImage(data.get("vVideothumbnail").toString(),holder.imgFeed);
		}else{
			imageLoader.DisplayImage(data.get("vFile").toString(), holder.imgFeed);
			holder.imgVicon.setVisibility(View.GONE);

		}
		 holder.imgVicon.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

//					holder.imgFeed.setVisibility(View.GONE);
//					holder.videoViewPlay.setVisibility(View.VISIBLE);

				int vPosotion = (Integer) view.getTag(R.id.position);

				data=(LinkedHashMap<String, Object>) mAllData.get(vPosotion);

//				//Play Video in browser 
//				String FileUrl =data.get("vFile").toString();
//
//				Intent i = new Intent(Intent.ACTION_VIEW);
//				i.setData(Uri.parse(FileUrl));
//				activity.startActivity(i);

//				File file = new File(data.get("vFile").toString());
//				Intent i = new Intent();
//				i.setAction(android.content.Intent.ACTION_VIEW);
//				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
//				String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
//				i.setDataAndType(Uri.fromFile(file), mimetype);
//
//				activity.startActivity(i);

				//Old Jignesh data
				/**
				 * Play Video in default player
				 */
				String videourl = data.get("vFile").toString();
				AppSharedPrefrence.getInstance(activity).SetVideoPath(videourl);
//				Uri uri = Uri.parse(videourl);
//				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//				intent.setDataAndType(uri, "video/*");
//				activity.startActivity(intent);

				Intent intent  = new Intent(activity,VideoPlayer.class);
				activity.startActivity(intent);

				// create a Dialog component
			/*	final Dialog dialog = new Dialog(activity);

				dialog.getWindow();

				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				// tell the Dialog to use the dialog.xml as it's layout
				// description
				dialog.setContentView(R.layout.custom_dialog);
				// dialog.setTitle(null);
					

					TextView txt = (TextView) dialog.findViewById(R.id.txt_dia);

					txt.setText("Do you Want to download this video!");

					Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
					Button btnNo = (Button) dialog.findViewById(R.id.btn_no);

					btnYes.setOnClickListener(new OnClickListener() {
						@SuppressWarnings("unchecked")
						@Override
						public void onClick(View view) {

							data=(LinkedHashMap<String, Object>) mAllData.get(vPosotion);
							String FileUrl =data.get("vFile").toString();
							String fileName = FileUrl.substring(FileUrl.lastIndexOf('/') + 1);
							new DownloadVideo(activity,FileUrl,fileName).execute();
							dialog.dismiss();
						}
					});

					btnNo.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							dialog.dismiss();
						}
					});

					dialog.show();*/

//					data=(LinkedHashMap<String, Object>) mAllData.get(position);
//					String FileUrl =data.get("vFile").toString();
//					new DownloadVideo(activity,FileUrl).execute();
				}
			});

		 holder.imgFeed.setTag(R.id.position,position);
		 holder.imgFeed.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				int vPosotion = (Integer) view.getTag(R.id.position);

				data=(LinkedHashMap<String, Object>) mAllData.get(vPosotion);

				if (data.get("eFileType").toString().equalsIgnoreCase("Video")){
//					String videourl = data.get("vFile").toString();
//					Uri uri = Uri.parse(videourl);
//					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//					intent.setDataAndType(uri, "video/*");
//					activity.startActivity(intent);
					String videourl = data.get("vFile").toString();
					AppSharedPrefrence.getInstance(activity).SetVideoPath(videourl);
					Intent intent  = new Intent(activity,VideoPlayer.class);
					activity.startActivity(intent);

				}else{
					
					String imageUrl = data.get("vFile").toString();
					Uri uri = Uri.parse(imageUrl);
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					intent.setDataAndType(uri, "image/*");
					activity.startActivity(intent);
				}
			}
		});

//		 holder.Comment_btn.setOnClickListener(new OnClickListener() {
//			
//			@SuppressWarnings("unchecked")
//			@Override
//			public void onClick(View view) {
//				int position = (Integer) view.getTag(R.id.position);
//
//				if(flag == false){
//					holder.CommentLayout.setVisibility(View.VISIBLE);
//					flag = true;
//				}else{
//					holder.CommentLayout.setVisibility(View.GONE);
//					flag = false;
//				}
//
//				data=(LinkedHashMap<String, Object>) mAllData.get(position);
//				CommonVariable.getInstance().setPost_id(data.get("iPostId").toString());
//
//				
//				//holder.CommentLayout.setVisibility(View.VISIBLE);
//			}
//		});
//
//		 holder.btnsend.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//
//				strComment =holder. edt_Comment.getText().toString().trim();
//				holder.edt_Comment.setText("");
//				if (!isEmpty(strComment)) {
//					CallWsSetComment();
//				} else {
//
//				}
//				holder.CommentLayout.setVisibility(View.GONE);
//			}
//		});
		

		return convertView;
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str.trim())) {
			return true;
		}
		return false;
	}

	static class ViewHolder {
		TextView txt_category_name;
		TextView txtName;// txt_name
		TextView txtCity;
		TextView txtTitle;
		TextView txtDesc;
		TextView txtLike;
		TextView txtComment;
		TextView txtTime;

		//Button like_btn;
		//Button Comment_btn;
		ImageView imgProfilePic;
		ImageView imgFeed;
		ImageView imgVicon;
		Button ExpandComment;

		//EditText edt_Comment;
		//Button btnsend;

		//RelativeLayout CommentLayout;
	}
}