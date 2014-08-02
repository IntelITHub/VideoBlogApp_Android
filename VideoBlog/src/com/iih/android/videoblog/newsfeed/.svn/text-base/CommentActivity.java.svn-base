package com.iih.android.videoblog.newsfeed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.iih.android.videoblog.R;
import com.iih.android.videoblog.Parsing.PostDataAndGetData;
import com.iih.android.videoblog.Parsing.parseListner;
import com.iih.android.videoblog.commonutill.CommonUtility;
import com.iih.android.videoblog.commonutill.CommonVariable;

public class CommentActivity extends Activity implements parseListner {

	private TextView action_title;
	private EditText edt_comment;
	private Button actionleft;
	private Button actionRight;
	private Button btnSendCommt;

	private ListView commnetlistview;
	private final int GetComment = 0;
	private final int SetComment = 1;
	private final int DeleteComment =2;
	private String strCmnt;

	CommentListAdapter commentAdapter;

	private String commented_id;
	private String postId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment);

		initialization();

		actionleft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent intent = new Intent(CommentActivity.this,
				// NewsFeedScreen.class);
				// startActivity(intent);
				finish();
			}
		});
		btnSendCommt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				strCmnt = edt_comment.getText().toString().trim();
				edt_comment.setText("");
				if (!isEmpty(strCmnt)) {
					CallWsSetCmt();
				} else {

				}
			}
		});

		commnetlistview.setOnItemLongClickListener(new OnItemLongClickListener() {

					@SuppressWarnings("unchecked")
					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,View arg1, int position, long arg3) {
						// TODO Auto-generated method stub

						commentAdapter = (CommentListAdapter) arg0.getAdapter();
						tempData = new ArrayList<Object>();

						tempData = (ArrayList<Object>) temp.get("data");
						data = (LinkedHashMap<String, Object>) tempData.get(position);

						commented_id = data.get("iCommentId").toString();
						String MemberId= data.get("iMemberId").toString();
						postId= data.get("iPostId").toString();

						if(MemberId.equalsIgnoreCase(CommonVariable.getInstance().getMember_id())){

							final Dialog dialog = new Dialog(CommentActivity.this);

							dialog.getWindow();

							dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
							// tell the Dialog to use the dialog.xml as it's layout
							// description
							dialog.setContentView(R.layout.custom_dialog);
							// dialog.setTitle(null);
								

								TextView txt = (TextView) dialog.findViewById(R.id.txt_dia);

								txt.setText(R.string.delete_comment);

								Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
								Button btnNo = (Button) dialog.findViewById(R.id.btn_no);

								btnYes.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View view) {
										
										dialog.dismiss();
										CallWsDelete();
									}
								});

								btnNo.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View view) {
				
										dialog.dismiss();
									}
								});

								dialog.show();

//						AlertDialog.Builder builder = new AlertDialog.Builder(CommentActivity.this);
//
//						builder.setTitle(R.string.delete_comment);
//						builder.setMessage(R.string.are_you_sure_);
//
//						builder.setPositiveButton(R.string.yes,new DialogInterface.OnClickListener() {
//
//									public void onClick(DialogInterface dialog,int which) {
//										// Do nothing but close the dialog
//
//										dialog.dismiss();
//
//										CallWsDelete();
//										// CallWsGetCmt();
//									}
//								});
//
//						builder.setNegativeButton(R.string.no,new DialogInterface.OnClickListener() {
//
//									@Override
//									public void onClick(DialogInterface dialog,
//											int which) {
//										// Do nothing
//										dialog.dismiss();
//									}
//								});
//
//						AlertDialog alert = builder.create();
//						alert.show();
						}else{
							CommonUtility.showAlert(getString(R.string.you_are_not_authorized_to_delete_other_users_comment), CommentActivity.this);
						}
						return false;
					}
				});

		CallWsGetCmt();
	}

	private void initialization() {
		action_title = (TextView) findViewById(R.id.action_title);
		action_title.setText(getResources().getString(R.string.comments));

		actionleft = (Button) findViewById(R.id.action_Left);
		actionleft.setBackgroundResource(R.drawable.actionbar_back);

		actionRight = (Button) findViewById(R.id.action_right);
		actionRight.setVisibility(View.INVISIBLE);

		commnetlistview = (ListView) findViewById(R.id.Commentlistview);
		btnSendCommt = (Button) findViewById(R.id.sendbtn);
		edt_comment = (EditText) findViewById(R.id.commentedittttext);
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
	HashMap<String, String> Paramter;
	HashMap<String, String> Parameter = null;
	public PostDataAndGetData psd;

	public void CallWsGetCmt() {
		Parameter = new HashMap<String, String>();

		Parameter.put("iPostId", CommonVariable.getInstance().getPost_id());
		Parameter.put("iMemberId", CommonVariable.getInstance().getMember_id());

		psd = new PostDataAndGetData(CommentActivity.this, null, Parameter,
				"POST", CommonUtility.BaseUrl2 + "getComments", GetComment, 0,
				false, true);
		psd.execute();

	}

	
	public void CallWsSetCmt() {
		Parameter = new HashMap<String, String>();

		Parameter.put("iPostId", CommonVariable.getInstance().getPost_id());
		Parameter.put("iMemberId", CommonVariable.getInstance().getMember_id());
		Parameter.put("tComments", strCmnt);

		psd = new PostDataAndGetData(CommentActivity.this, null, Parameter,
				"POST", CommonUtility.BaseUrl2 + "setComments", SetComment, 0,
				false, true);
		psd.execute();
	}

	public void CallWsDelete() {
		Parameter = new HashMap<String, String>();

		Parameter.put("iPostId",postId);
		Parameter.put("iCommentId", commented_id);
		Parameter.put("iMemberId", CommonVariable.getInstance().getMember_id());

		psd = new PostDataAndGetData(CommentActivity.this, null, Parameter,
				"POST", CommonUtility.BaseUrl2 + "deleteComment", DeleteComment, 0,
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
	ArrayList<Object> tempData;

	@SuppressWarnings("unchecked")
	@Override
	public void GetResult(Object jsonDataO, int responce) {
		try {
			arrayList = (ArrayList<Object>) jsonDataO;
			ArrayList<LinkedHashMap<String, Object>> jsonDataTemp = (ArrayList<LinkedHashMap<String, Object>>) jsonDataO;
			int size = arrayList.size();
			if (size != 0) {

				temp = (LinkedHashMap<String, Object>) arrayList.get(0);
				data = (LinkedHashMap<String, Object>) temp.get("message");
				if (data.get("success").toString().equalsIgnoreCase("1")) {
					jsonData = jsonDataTemp;
//					data=(LinkedHashMap<String, Object>) temp.get("data");
					switch (responce) {
					case GetComment:
						ArrayList<String> ComntList = new ArrayList<String>();
						ArrayList<String> userList = new ArrayList<String>();
						ArrayList<String> mComntimeList = new ArrayList<String>();
						ArrayList<String> picList = new ArrayList<String>();
						try {

							tempData=new ArrayList<Object>();
							tempData=(ArrayList<Object>) temp.get("data");
							for (int i = 0; i < tempData.size(); i++) {
								data=(LinkedHashMap<String, Object>) tempData.get(i);
								ComntList.add(data.get("tComments").toString());
								userList.add(data.get("vUsername").toString());
								mComntimeList.add(data.get("dCreatedDate").toString());
								picList.add(data.get("vPicture").toString());
							}

							commentAdapter = new CommentListAdapter(CommentActivity.this, ComntList, userList,mComntimeList, picList);
							commnetlistview.setAdapter(commentAdapter);

						} catch (Exception e) {

							CommonUtility.showAlert(getString(R.string.no_post_comment_available_),CommentActivity.this);
							e.printStackTrace();
						}

						break;
					case SetComment:  

						try {
							CallWsGetCmt();
						} catch (Exception e) {
							e.printStackTrace();
						}

					case DeleteComment:
						try {
							CallWsGetCmt();
						} catch (Exception e) {
							e.printStackTrace();
						}

						break;
					default:
						break;
					}
				} else {
					commnetlistview.setAdapter(null);
					//CommonUtility.showAlert(data.get("msg").toString(),CommentActivity.this);
				}
			} else {
				CommonUtility.showAlert(getString(R.string.there_is_some_problem_in_getting_data_),CommentActivity.this);
			}
		} catch (Exception e) {
			CommonUtility.showAlert(getString(R.string.there_is_some_problem_in_getting_data_),CommentActivity.this);
		}

	}
}
