package com.iih.android.videoblog.category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iih.android.videoblog.R;
import com.iih.android.videoblog.Parsing.PostDataAndGetData;
import com.iih.android.videoblog.Parsing.parseListner;
import com.iih.android.videoblog.commonutill.AppSharedPrefrence;
import com.iih.android.videoblog.commonutill.CommonUtility;
import com.iih.android.videoblog.commonutill.CommonVariable;
import com.iih.android.videoblog.newsfeed.CommentActivity;
import com.iih.android.videoblog.newsfeed.VideoPlayer;
import com.iih.android.videoblog.profile.ProfileActivity;
import com.iih.loadwebimage.imageutils.ImageLoader;

public class PostDetailsActivity extends Activity implements parseListner,OnClickListener {

	private TextView action_title;
	private Button actionleft;
	private Button actionRight;

	private ImageView img_profilepic;
	private TextView txt_username;
	private TextView txt_postTitle;
	private TextView txt_post_city;
	private ImageView img_postfeedImage;
	private ImageView imgVicon;
	private TextView txt_postDescription;
	private TextView txt_totallikes;
	private TextView txt_totalComments;
	private TextView txt_categoryname;
	private Button btn_like;
	private Button btn_comments;
	private Button btn_expandComment;

	private RelativeLayout CommnetLayout;

	public ImageLoader imageLoader; 
	private final int PostdetailsRcode = 0,LikeRcode=1,UnlikeRcode =2,SetComment=3;
	PostDataAndGetData psd;

	private EditText edt_Comment;
	private Button btnsend;
	private String strComment;

	private Boolean flag = false;

	/**
	 * Calling the web-service
	 */
	HashMap<String, String> Paramter;
	HashMap<String, String> Parameter = null;

	private String PostLike;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.postdetails);

		initialization();

		btn_expandComment.setOnClickListener(this);
		btn_like.setOnClickListener(this);
		txt_username.setOnClickListener(this);
		btn_comments.setOnClickListener(this);
		btnsend.setOnClickListener(this);
		imgVicon.setOnClickListener(this);
		img_postfeedImage.setOnClickListener(this);

		CallWsPostDetails();
	}

	private void initialization() {
//		action_title = (TextView) findViewById(R.id.action_title);
//		action_title.setText("Logo");

		actionleft = (Button) findViewById(R.id.action_Left);
		actionleft.setVisibility(View.INVISIBLE);

		actionRight = (Button) findViewById(R.id.action_right);
		actionRight.setVisibility(View.INVISIBLE);

		img_profilepic = (ImageView)findViewById(R.id.postUserProPic);
		txt_username =(TextView)findViewById(R.id.username);
		txt_postTitle =(TextView)findViewById(R.id.post_title);
		txt_post_city =(TextView)findViewById(R.id.Post_city);
		img_postfeedImage =(ImageView)findViewById(R.id.feedimage);
		txt_postDescription =(TextView)findViewById(R.id.Postdescription);
		txt_totallikes = (TextView)findViewById(R.id.txt_like);
		txt_totalComments = (TextView)findViewById(R.id.txt_comment);
		btn_like = (Button)findViewById(R.id.post_like);
		btn_comments = (Button)findViewById(R.id.post_comment);
		btn_expandComment =(Button)findViewById(R.id.post_expand);
		txt_categoryname =(TextView)findViewById(R.id.category_name);
		imageLoader=new ImageLoader(getApplicationContext());
		imgVicon=(ImageView) findViewById(R.id.v_icon);

		CommnetLayout = (RelativeLayout)findViewById(R.id.commentLayout);
		edt_Comment = (EditText)findViewById(R.id.commentedittttext);
		btnsend = (Button)findViewById(R.id.sendbtn);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		CallWsPostDetails();
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
	/**
	 * Calling the web-service
	 */

	public void CallWsPostDetails() {

		String PostId = CommonVariable.getInstance().getPost_id();
		String Member_Id = CommonVariable.getInstance().getMember_id();

		psd = new PostDataAndGetData(PostDetailsActivity.this, null, null,
				"GET",CommonUtility.BaseUrl2 +"getPostdetails&iPostId="+ PostId+"&iMemberId="+Member_Id ,PostdetailsRcode, 0, false, true);
		psd.execute();
	}

	public void CallWsSetLike() {

		Parameter = new HashMap<String, String>();

		Parameter.put("iPostId", CommonVariable.getInstance().getPost_id());
		Parameter.put("iMemberId", CommonVariable.getInstance().getMember_id());

		psd = new PostDataAndGetData(PostDetailsActivity.this,null,Parameter,"POST",CommonUtility.BaseUrl2 + "setPostLike",LikeRcode, 0, false, true);
		psd.execute();
	}

	public void CallWsSetUnlike() {

		Parameter = new HashMap<String, String>();

		Parameter.put("iPostId", CommonVariable.getInstance().getPost_id());
		Parameter.put("iMemberId", CommonVariable.getInstance().getMember_id());

		psd = new PostDataAndGetData(PostDetailsActivity.this,null,Parameter,"POST",CommonUtility.BaseUrl2 + "setPostUnLike",UnlikeRcode, 0, false, true);
		psd.execute();
	}

	public void CallWsSetComment() {

		Parameter = new HashMap<String, String>();

		Parameter.put("iPostId", CommonVariable.getInstance().getPost_id());
		Parameter.put("iMemberId", CommonVariable.getInstance().getMember_id());
		Parameter.put("tComments", strComment);

		psd = new PostDataAndGetData(PostDetailsActivity.this, null, Parameter,
				"POST", CommonUtility.BaseUrl2 + "setComments", SetComment, 0,
				false, true);

		psd.execute();
	}


	/**
	 * Web-service Response
	 */
	LinkedHashMap<String, Object> temp;
	ArrayList<LinkedHashMap<String, Object>> jsonData;
	ArrayList<Object> arrayList;
	LinkedHashMap<String, Object> data;

	@SuppressWarnings("unchecked")
	@Override
	public void GetResult(Object jsonDataO, int responce) {
		try{
			arrayList = (ArrayList<Object>) jsonDataO;
			ArrayList<LinkedHashMap<String, Object>> jsonDataTemp =new ArrayList<LinkedHashMap<String, Object>>();
			int size = arrayList.size();
			if (size != 0) {
				temp = (LinkedHashMap<String, Object>) arrayList.get(0);
				data= (LinkedHashMap<String, Object>) temp.get("message");

			if (data.get("success").toString().equalsIgnoreCase("1")) {
			jsonData = jsonDataTemp;

			data=(LinkedHashMap<String, Object>) temp.get("data");
			switch (responce) {
			case PostdetailsRcode:

				imageLoader.DisplayImage( data.get("vPicture").toString(), img_profilepic);
				txt_username.setText("@"+data.get("vName").toString());
				txt_postTitle.setText(data.get("tPost").toString());
				txt_post_city.setText(data.get("vCity").toString());

				txt_postDescription.setText(data.get("tDescription").toString());
				txt_totallikes.setText(data.get("totalPostLikes").toString());
				txt_totalComments.setText(data.get("totalPostComments").toString());
				txt_categoryname.setText(data.get("vCategoriesName").toString());

				PostLike = data.get("isPostLike").toString();
//				eFiletype
				if (data.get("totalPostLikes").toString().equalsIgnoreCase("0")) {
					Drawable img = PostDetailsActivity.this.getResources().getDrawable(R.drawable.countlike_white);
					img.setBounds(0, 0, 14, 14);
					// txtVw.setCompoundDrawables( img, null, null, null );
					txt_totallikes.setCompoundDrawables(img, null, null, null);

				} else {
					Drawable img = PostDetailsActivity.this.getResources().getDrawable(R.drawable.count_like_orange);
					img.setBounds(0, 0, 14, 14);
					// txtVw.setCompoundDrawables( img, null, null, null );
					txt_totallikes.setCompoundDrawables(img, null, null, null);
				}

				if (data.get("totalPostComments").toString().equalsIgnoreCase("0")) {
					Drawable img = PostDetailsActivity.this.getResources().getDrawable(R.drawable.countcomment_white);
					img.setBounds(0, 0, 14, 14);
					// txtVw.setCompoundDrawables( img, null, null, null );
					txt_totalComments.setCompoundDrawables(img, null, null, null);

				} else {
					Drawable img = PostDetailsActivity.this.getResources().getDrawable(R.drawable.countcomment_orange);
					img.setBounds(0, 0, 14, 14);
					// txtVw.setCompoundDrawables( img, null, null, null );
					txt_totalComments.setCompoundDrawables(img, null, null, null);
				}
				if (data.get("eFiletype").toString().equalsIgnoreCase("Video")) {
					imgVicon.setVisibility(View.VISIBLE);
					imageLoader.DisplayImage( data.get("vVideothumbnail").toString(), img_postfeedImage);
				}else{
					imageLoader.DisplayImage( data.get("vFile").toString(), img_postfeedImage);
					imgVicon.setVisibility(View.GONE);
				}

				imgVicon.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View view) {

//							String videoUrl =data.get("vFile").toString();
//
//							Intent i = new Intent(Intent.ACTION_VIEW);
//							i.setData(Uri.parse(videoUrl));
//							startActivity(i);
						/**
						 * Play video directly to default media player on device
						 */

							String videourl = data.get("vFile").toString();
							AppSharedPrefrence.getInstance(PostDetailsActivity.this).SetVideoPath(videourl);

							Intent intent = new Intent(PostDetailsActivity.this,VideoPlayer.class);
							startActivity(intent);
//							Uri uri = Uri.parse(videourl);
//							Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//							intent.setDataAndType(uri, "video/*");
//							startActivity(intent);

						// create a Dialog component
						/*final Dialog dialog = new Dialog(PostDetailsActivity.this);

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

//									data=(LinkedHashMap<String, Object>) mAllData.get(vPosotion);
									String FileUrl =data.get("vFile").toString();
									String fileName = FileUrl.substring(FileUrl.lastIndexOf('/') + 1);
									new DownloadVideo(PostDetailsActivity.this,FileUrl,fileName).execute();
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
//							data=(LinkedHashMap<String, Object>) mAllData.get(position);
//							String FileUrl =data.get("vFile").toString();
//							new DownloadVideo(activity,FileUrl).execute();
						}
					});
	
//				img_postfeedImage.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						
//					}
//				});
				break;

			case LikeRcode:

				btn_like.setBackgroundResource(R.drawable.liked_btn);
				btn_like.setText(R.string.liked);
				btn_like.setTextColor(getResources().getColorStateList(R.color.white));
				CallWsPostDetails();
				break;

			case UnlikeRcode:
				btn_like.setBackgroundResource(R.drawable.likebtn);
				btn_like.setText(R.string.like);
				btn_like.setTextColor(getResources().getColorStateList(android.R.color.darker_gray));
				CallWsPostDetails();
				break;

			case SetComment:
				CallWsPostDetails();
				break;

			default:
				break;
			}
			} else {
				CommonUtility.showAlert(data.get("msg").toString(),PostDetailsActivity.this);
			}
		} else {
			CommonUtility.showAlert(getString(R.string.there_is_some_problem_in_getting_data_), PostDetailsActivity.this);
		}
	} catch (Exception e) {
		CommonUtility.showAlert(getString(R.string.there_is_some_problem_in_getting_data_), PostDetailsActivity.this);
	}
}

	@Override
	public void onClick(View v) {
		int id=v.getId();
		switch (id) {
		case R.id.post_expand:

			try {
				CommonVariable.getInstance().setPost_id(data.get("iPostId").toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			Intent intent = new Intent(PostDetailsActivity.this,CommentActivity.class);
			startActivity(intent);
			break;

		case R.id.post_like:

			if(PostLike.equalsIgnoreCase("No")){
				CallWsSetLike();
			}else{
				CallWsSetUnlike();
			}

			break;

		case R.id.username:
			String Profiler_id = data.get("iMemberId").toString();
			if(Profiler_id != null){
				CommonVariable.getInstance().setProfiler_id(Profiler_id);
			}
			Intent intent1 = new Intent(PostDetailsActivity.this,ProfileActivity.class);
			startActivity(intent1);
			break;

		case R.id.v_icon:
			//String FileUrl =data.get("vFile").toString();
//			String fileName = FileUrl.substring(FileUrl.lastIndexOf('/') + 1);
//			new DownloadVideo(PostDetailsActivity.this, FileUrl, fileName).execute();

			String Playvideourl = data.get("vFile").toString();
			AppSharedPrefrence.getInstance(PostDetailsActivity.this).SetVideoPath(Playvideourl);

			Intent intentPlayer = new Intent(PostDetailsActivity.this,VideoPlayer.class);
			startActivity(intentPlayer);

			//Jignesh Device default player
			
//			Uri videouri = Uri.parse(Playvideourl);
//			Intent intent2 = new Intent(Intent.ACTION_VIEW, videouri);
//			intent2.setDataAndType(videouri, "video/*");
//			startActivity(intent2);
			break;

		case R.id.post_comment:
			//btn_comments.setTextColor(getResources().getColorStateList(R.color.white));
			if(flag == false){
				CommnetLayout.setVisibility(View.VISIBLE);
				flag = true;
			}else{
				CommnetLayout.setVisibility(View.GONE);
				flag = false;
			}
			CommonVariable.getInstance().setPost_id(data.get("iPostId").toString());

			break;

		case R.id.sendbtn:

			strComment =edt_Comment.getText().toString().trim();
			edt_Comment.setText("");
			if (!isEmpty(strComment)) {
				CallWsSetComment();
			} else {
			}
			//CommnetLayout.setVisibility(View.GONE);
			break;

		case R.id.feedimage:
			if (data.get("eFiletype").toString().equalsIgnoreCase("Video")){
				String videourl = data.get("vFile").toString();
				Uri uri = Uri.parse(videourl);
				Intent imageintent = new Intent(Intent.ACTION_VIEW, uri);
				imageintent.setDataAndType(uri, "video/*");
				startActivity(imageintent);
			}else{
				String Imageurl = data.get("vFile").toString();
				Uri uri = Uri.parse(Imageurl);
				Intent videointent = new Intent(Intent.ACTION_VIEW, uri);
				videointent.setDataAndType(uri, "image/*");
				startActivity(videointent);
			}
			break;


		default:
			break;
		}
	}
}
