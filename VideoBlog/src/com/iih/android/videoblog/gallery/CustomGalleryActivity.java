package com.iih.android.videoblog.gallery;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.iih.android.videoblog.R;
import com.iih.android.videoblog.commonutill.AppSharedPrefrence;
import com.iih.android.videoblog.commonutill.CommonVariable;

@SuppressLint("NewApi")
public class CustomGalleryActivity extends Activity {

	private TextView action_title;
	private Button actionLeft;
	private Button actionRight;

	private Button Videosbtn;
	private Button PhotosBtn;

	public ImageAdapter imageAdapter;
	public VideoAdapter videoAdapter;

	private final static int TAKE_IMAGE = 1;
	private final static int UPLOAD_IMAGES = 2;
	private final static int VIEW_IMAGE = 3;
	private Uri imageUri;
	private MediaScannerConnection mScanner;
	public GridView imagegrid;
	private long lastId;
	private ImageView imgSelected;

	private Button btn_rotateLeft;
	private Button btn_rotateRight;

	private Bitmap bmp;

	int currentRotation = 0;

	private boolean flagleft = false;
	private boolean flagRight = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery);

		Initialization();

		actionLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CustomGalleryActivity.this,CameraActivity.class);
				startActivity(intent);
				finish();
			}
		});

		actionRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CustomGalleryActivity.this,PostSettingActivity.class);
				startActivity(intent);
				finish();
			}
		});

		try {
			//Chengs Made by jignesh Gallery not shown
			//videoAdapter = new VideoAdapter();
			//imageAdapter = new ImageAdapter();
			if (AppSharedPrefrence.getInstance(CustomGalleryActivity.this).getFileType().equalsIgnoreCase("Image")) {
				btn_rotateLeft.setVisibility(View.VISIBLE);
				btn_rotateRight.setVisibility(View.VISIBLE);

				Bitmap bmp ;//= BitmapFactory.decodeFile(CommonVariable.getInstance().getFilePath());
				bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(AppSharedPrefrence.getInstance(CustomGalleryActivity.this).getFilePath()),400, 600, true);
				imgSelected.setImageBitmap(bmp);
				Videosbtn.setBackgroundResource(R.drawable.video_btn_white);
				PhotosBtn.setBackgroundResource(R.drawable.photo_btn_orange);
			
//				imageAdapter = new ImageAdapter();
				//Load Images from Gallery  jignesh Changes
				//new LoadImages(this).execute();
			} else {
				//CommonVariable.getInstance().setFileType(("Video"));
				btn_rotateLeft.setVisibility(View.INVISIBLE);
				btn_rotateRight.setVisibility(View.INVISIBLE);
				Bitmap bitmap =ThumbnailUtils.createVideoThumbnail(AppSharedPrefrence.getInstance(CustomGalleryActivity.this).getFilePath(),MediaStore.Video.Thumbnails.MINI_KIND);
				imgSelected.setImageBitmap(bitmap);
				Videosbtn.setBackgroundResource(R.drawable.video_btn_orange);
				PhotosBtn.setBackgroundResource(R.drawable.photos_btn_white);
				Videosbtn.setTextColor(getResources().getColor(R.color.themecolor));
				PhotosBtn.setTextColor(getResources().getColor(R.color.white));
//				videoAdapter = new VideoAdapter();

				//Load Video from Gallery  jignesh Changes
				//new LoadVideos(this).execute();

			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	
	
		//Chnages Jignesh jain Hide Gallery
//		Videosbtn.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View v) {
//				btn_rotateLeft.setVisibility(View.INVISIBLE);
//				Videosbtn.setBackgroundResource(R.drawable.video_btn_orange);
//				PhotosBtn.setBackgroundResource(R.drawable.photos_btn_white);
//				Videosbtn.setTextColor(getResources().getColor(R.color.themecolor));
//				PhotosBtn.setTextColor(getResources().getColor(R.color.white));
//
////				try {
////					videoAdapter = new VideoAdapter();
////					imageAdapter = new ImageAdapter();
////					if (CommonVariable.getInstance().getFileType().equalsIgnoreCase("Image")) {
////
////						Bitmap bmp ;//= BitmapFactory.decodeFile(CommonVariable.getInstance().getFilePath());
////						bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(CommonVariable.getInstance().getFilePath()),400, 400, true);
////						imgSelected.setImageBitmap(bmp);
////						
////					
//////						imageAdapter = new ImageAdapter();
////						new LoadImages(CustomGalleryActivity.this).execute();
////					} else {
////
////						Bitmap bitmap =ThumbnailUtils.createVideoThumbnail(CommonVariable.getInstance().getFilePath(),MediaStore.Video.Thumbnails.MINI_KIND);
////						imgSelected.setImageBitmap(bitmap);
////	
//////						videoAdapter = new VideoAdapter();
////						new LoadVideos(CustomGalleryActivity.this).execute();
////
////					}
////				} catch (Exception e1) {
////					e1.printStackTrace();
////				}
//			
//
//				                                                                                                                                                                                   new LoadVideos(CustomGalleryActivity.this).execute();
//			}
//		});

		//Chnages Jignesh jain Hide Gallery
//		PhotosBtn.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View v) {
//				btn_rotateLeft.setVisibility(View.VISIBLE);
//				Videosbtn.setBackgroundResource(R.drawable.video_btn_white);
//				PhotosBtn.setBackgroundResource(R.drawable.photo_btn_orange);
//				Videosbtn.setTextColor(getResources().getColor(R.color.white));
//				PhotosBtn.setTextColor(getResources().getColor(R.color.themecolor));
//
////			
////				try {
////					videoAdapter = new VideoAdapter();
////					imageAdapter = new ImageAdapter();
////					if (CommonVariable.getInstance().getFileType().equalsIgnoreCase("Image")) {
////
////						Bitmap bmp ;//= BitmapFactory.decodeFile(CommonVariable.getInstance().getFilePath());
////						bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(CommonVariable.getInstance().getFilePath()),400, 400, true);
////						imgSelected.setImageBitmap(bmp);
////
//////						imageAdapter = new ImageAdapter();
////						new LoadImages(CustomGalleryActivity.this).execute();
////					} else {
////
////						Bitmap bitmap =ThumbnailUtils.createVideoThumbnail(CommonVariable.getInstance().getFilePath(),MediaStore.Video.Thumbnails.MINI_KIND);
////						imgSelected.setImageBitmap(bitmap);
////	
//////						videoAdapter = new VideoAdapter();
////						new LoadVideos(CustomGalleryActivity.this).execute();
////
////					}
////				} catch (Exception e1) {
////					e1.printStackTrace();
////				}
//
//				//Load images from gallery
//				//new LoadImages(CustomGalleryActivity.this).execute();
//			}
//		});
//		try {
////			Bitmap bmp ;//= BitmapFactory.decodeFile(CommonVariable.getInstance().getFilePath());
////			bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(CommonVariable.getInstance().getFilePath()),400, 400, true);
////			imgSelected.setImageBitmap(bmp);
////			Bitmap bm;
////			 bm = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(CommonVariable.getInstance().getFilePath()),150, 150, true);
////			 imgSelected.setImageBitmap(bm);
////			imgSelected.setBackground(Drawable.createFromPath(CommonVariable.getInstance().getFilePath()));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	
		btn_rotateLeft.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {

				imgSelected.setBackground(null);
				imgSelected.setImageBitmap(bmp);

				new RotateLeftAsyncTask(CustomGalleryActivity.this).execute();

			}
		});

		btn_rotateRight.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {

				imgSelected.setBackground(null);
				imgSelected.setImageBitmap(bmp);

				new RotateRightAsyncTask(CustomGalleryActivity.this).execute();

			}
		});

		
	}
	public static Bitmap rotate(Bitmap source, float angle) {
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		return Bitmap.createBitmap(source, 0, 0, source.getWidth(),
				source.getHeight(), matrix, false);
	}

	// public static Bitmap Shrink(String file, int width, int height) {
	// BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
	// int heightRatio =
	// (int)Math.ceil(bmpFactoryOptions.outHeight/(float)height);
	// int widthRatio = (int)Math.ceil(bmpFactoryOptions.outWidth/(float)width);
	// if (heightRatio > 1 || widthRatio > 1) {
	// bmpFactoryOptions.inSampleSize = (heightRatio > widthRatio) ? heightRatio
	// : widthRatio;
	// }
	// return BitmapFactory.decodeFile(file, bmpFactoryOptions);
	// }
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		lockScreenRotation(Configuration.ORIENTATION_PORTRAIT);
	}

	private void lockScreenRotation(int orientation) {
		// Stop the screen orientation changing during an event
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	private void Initialization() {
		action_title = (TextView) findViewById(R.id.action_title);
		action_title.setText(getResources().getString(R.string.photos_videos));

		actionLeft = (Button) findViewById(R.id.action_Left);
		actionLeft.setBackgroundResource(R.drawable.actionbar_back);

		actionRight = (Button) findViewById(R.id.action_right);
		actionRight.setBackgroundResource(R.drawable.actionbarnext);
		actionRight.setText(getResources().getString(R.string.next));

		Videosbtn = (Button) findViewById(R.id.Videobtn);
		PhotosBtn = (Button) findViewById(R.id.captureBtn);

		imagegrid = (GridView) findViewById(R.id.PhoneImageGrid);
		imgSelected = (ImageView) findViewById(R.id.imageselected);

		btn_rotateLeft = (Button)findViewById(R.id.btn_rotateLeft);
		btn_rotateRight = (Button)findViewById(R.id.btn_rotateRight);
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(CustomGalleryActivity.this,CameraActivity.class);
		startActivity(intent);
		finish();
	}

	private int getImageOrientation() {
		final String[] imageColumns = { MediaStore.Images.Media._ID,MediaStore.Images.ImageColumns.ORIENTATION };
		final String imageOrderBy = MediaStore.Images.Media._ID + " DESC";
		Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageColumns,null, null, imageOrderBy);

		if (cursor.moveToFirst()) {
			int orientation = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION));
			cursor.close();
			return orientation;
		} else {
			return 0;
		}
	}

	private class RotateLeftAsyncTask extends AsyncTask<Void, Void, Void> {
		private ProgressDialog bar;
		Activity act;

		public RotateLeftAsyncTask(Activity activity) {
			act = activity;
		}

		protected Void doInBackground(Void... args) {
			// do background work here

			// 1. figure out the amount of degrees
			// int rotation = getImageRotation();
			//int rotation = getImageOrientation();

			int rotation = getImageOrientation();

			// 2. rotate matrix by postconcatination
			Matrix matrix = new Matrix();

			if(currentRotation == -360){
				currentRotation = 0;
			}

			currentRotation = currentRotation - 90 ;
			matrix.postRotate(currentRotation);

//			if(flagleft == false){
//				matrix.postRotate(rotation - 90);
//				flagleft = true;
//			}else{
//				matrix.postRotate(rotation + 90);
//				flagleft = false;
//			}

			// 3. create Bitmap from rotated matrix
			Bitmap sourceBitmap = BitmapFactory.decodeFile(AppSharedPrefrence.getInstance(CustomGalleryActivity.this).getFilePath());
			bmp = Bitmap.createBitmap(sourceBitmap, 0, 0,sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix,true);

			// Bitmap bmp = createBitmap();
			OutputStream stream = null;
			try {
				stream = new FileOutputStream(AppSharedPrefrence.getInstance(CustomGalleryActivity.this).getFilePath());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
 			bmp.compress(CompressFormat.JPEG, 100, stream);

			try {
				stream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPreExecute() {


			act.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					bar = new ProgressDialog(act,ProgressDialog.THEME_HOLO_LIGHT);
					bar.setIndeterminate(true);
					bar.setTitle(act.getString(R.string.rotating_));
					bar.setIcon(act.getResources().getDrawable(R.drawable.ic_launcher));
					bar.show();
				}
			});

			super.onPreExecute();
		}

		@SuppressLint("NewApi")
		protected void onPostExecute(Void result) {

			// do UI work here
			imgSelected.setBackground(null);
			imgSelected.setImageBitmap(bmp);
			try {
				bar.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private class RotateRightAsyncTask extends AsyncTask<Void, Void, Void> {
		private ProgressDialog bar;
		Activity act;

		public RotateRightAsyncTask(Activity activity) {
			act = activity;
		}

		protected Void doInBackground(Void... args) {
			// do background work here

			// 1. figure out the amount of degrees
			// int rotation = getImageRotation();
			int rotation = getImageOrientation();

			// 2. rotate matrix by postconcatination
			Matrix matrix = new Matrix();

			if(currentRotation == 360){
				currentRotation = 0;
			}

			currentRotation = currentRotation + 90 ;
			matrix.postRotate(currentRotation);
//			if(flagRight == false){
//				matrix.postRotate(rotation + 90);
//				flagRight = true;
//			}else{
//				matrix.postRotate(rotation - 90);
//				flagRight = false;
//			}

			// 3. create Bitmap from rotated matrix
			Bitmap sourceBitmap = BitmapFactory.decodeFile(AppSharedPrefrence.getInstance(CustomGalleryActivity.this).getFilePath());
			bmp = Bitmap.createBitmap(sourceBitmap, 0, 0,sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix,true);

			//Bitmap bmp = createBitmap();

			OutputStream stream = null;
			try {
				stream = new FileOutputStream(AppSharedPrefrence.getInstance(CustomGalleryActivity.this).getFilePath());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			bmp.compress(CompressFormat.JPEG, 100, stream);

			try {
				stream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPreExecute() {

			try {
				act.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						bar = new ProgressDialog(CustomGalleryActivity.this,ProgressDialog.THEME_HOLO_LIGHT);
						bar.setIndeterminate(true);
						bar.setTitle(act.getString(R.string.rotating_));
						bar.setIcon(act.getResources().getDrawable(R.drawable.ic_launcher));
						bar.show();
					}
			});
			} catch (Exception e) {
				e.printStackTrace();
			}

			super.onPreExecute();
		}

		@SuppressLint("NewApi")
		protected void onPostExecute(Void result) {


					imgSelected.setBackground(null);
					imgSelected.setImageBitmap(bmp);
	
			// do UI work here
			
			try {
				bar.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public class LoadVideos extends AsyncTask<Void, Void, Void> {
		Context context;
		private ProgressDialog bar;

		public LoadVideos() {
			// TODO Auto-generated constructor stub
		}

		public LoadVideos(Context context, ListView listView) {
			this.context = context;
		}

		public LoadVideos(Context context) {
			this.context = context;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			try {
				imagegrid.setAdapter(videoAdapter);
				bar.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				bar = new ProgressDialog(context);
				bar.setIndeterminate(true);
				bar.setTitle(context.getString(R.string.loading_));
				bar.setIcon(context.getResources().getDrawable(R.drawable.ic_launcher));
				bar.setCancelable(false);
				bar.show();

//				bar = new ProgressDialog(context);
//				bar.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
//				bar.setIndeterminate(true);
//				bar.setCancelable(false);
//				bar.setTitle();
//				bar.show();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				videoAdapter.initializeVideo();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

	}

	public class LoadImages extends AsyncTask<Void, Void, Void> {
		Context context;
		private ProgressDialog bar;

		public LoadImages() {
			// TODO Auto-generated constructor stub
		}

		public LoadImages(Context context, ListView listView) {
			// TODO Auto-generated constructor stub
			this.context = context;
		}

		public LoadImages(Context context) {
			// TODO Auto-generated constructor stub
			this.context = context;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				imagegrid.setAdapter(imageAdapter);
				bar.dismiss();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			try {
//				bar = new ProgressDialog(context);
//				bar.setCancelable(false);
//				bar.setIndeterminate(true);
//				bar.setTitle(R.string.loading_);
//				bar.show();
				bar = new ProgressDialog(context);
				bar.setIndeterminate(true);
				bar.setTitle(context.getString(R.string.loading_));
				bar.setIcon(context.getResources().getDrawable(R.drawable.ic_launcher));
				bar.setCancelable(false);
				bar.show();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				imageAdapter.initializeImages();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

	}

	@Override
	protected void onRestart() {
//		Bitmap bmp = BitmapFactory.decodeFile(CommonVariable.getInstance().getFilePath());
//		imgSelected.setImageBitmap(bmp);
		
		try {
//			imageAdapter.initializeImages();
//			videoAdapter.initializeVideo();
//			new LoadImages(CustomGalleryActivity.this).execute();
//			new LoadVideos(CustomGalleryActivity.this).execute();

			//Load Image /Video From Gallery Jignesh Chnages
//			if (CommonVariable.getInstance().getFileType().equalsIgnoreCase("Image")) {
//				imageAdapter = new ImageAdapter();
//				new LoadImages(this).execute();
//			}else{
//				videoAdapter = new VideoAdapter();
//				new LoadVideos(this).execute();
//			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		 super.onRestart();
	}

	@Override
	protected void onResume() {
//		imageAdapter.initializeImages();
//		videoAdapter.initializeVideo();
//		new LoadImages(CustomGalleryActivity.this).execute();
//		new LoadVideos(CustomGalleryActivity.this).execute();
		super.onResume();
//		new LoadImages(this).execute();
//		new LoadVideos(this).execute();
		
//		try {
//			imageAdapter.initializeImages();
//			new LoadImages(CustomGalleryActivity.this).execute();
//			videoAdapter.initializeVideo();
//			new LoadVideos(CustomGalleryActivity.this).execute();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_IMAGE:
			try {
				if (resultCode == RESULT_OK) {

					// we need to update the gallery by starting MediaSanner
					// service.
					mScanner = new MediaScannerConnection(CustomGalleryActivity.this,
							new MediaScannerConnection.MediaScannerConnectionClient() {
								public void onMediaScannerConnected() {
									mScanner.scanFile(imageUri.getPath(), null /* mimeType */);
								}

								public void onScanCompleted(String path, Uri uri) {
									/**
									 * we can use the uri, to get the newly
									 * added image, but it will return path to
									 * full sized image e.g.
									 * content://media/external/images/media/7
									 * we can also update this path by replacing
									 * media by thumbnail to get the thumbnail
									 * because thumbnail path would be like
									 * content
									 * ://media/external/images/thumbnail/7 But
									 * the thumbnail is created after some delay
									 * by Android OS So you may not get the
									 * thumbnail. This is why I started new UI
									 * thread and it'll only run after the
									 * current thread completed.
									 */

									if (path.equals(imageUri.getPath())) {
										mScanner.disconnect();
										/**
										 * we need to create new UI thread
										 * because, we can't update our mail
										 * thread from here Both the thread will
										 * run one by one, see documentation of
										 * android
										 */

										CustomGalleryActivity.this.runOnUiThread(new Runnable() {
													public void run() {
														updateUI();
													}
												});
									}
								}
							});
					mScanner.connect();

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case UPLOAD_IMAGES:
			if (resultCode == RESULT_OK) {
				// do some code where you integrate this project
			}
			break;
		}
	}

	public void updateUI() {
		imageAdapter.checkForNewImages();
	}

	public class VideoAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		public ArrayList<ImageItem> images = new ArrayList<ImageItem>();

		public VideoAdapter() {
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public void initializeVideo() {

			images.clear();
			final String[] columns = { MediaStore.Video.Thumbnails._ID };
			final String orderBy = MediaStore.Video.Media._ID;
			Cursor imagecursor = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columns, null,null, orderBy);
			if (imagecursor != null) {
				int image_column_index = imagecursor.getColumnIndex(MediaStore.Video.Media._ID);
				int count = imagecursor.getCount();
				for (int i = 0; i < count; i++) {
					imagecursor.moveToPosition(i);
					int id = imagecursor.getInt(image_column_index);
					ImageItem imageItem = new ImageItem();
					imageItem.id = id;
					lastId = id;
					imageItem.img = MediaStore.Video.Thumbnails.getThumbnail(getApplicationContext().getContentResolver(), id,	MediaStore.Video.Thumbnails.MICRO_KIND, null);
					images.add(imageItem);
				}
				if (imagecursor != null && !imagecursor.isClosed()) {
					CustomGalleryActivity.this.stopManagingCursor(imagecursor);
					imagecursor.close();
				}
				//imagecursor.close();
			}
			// notifyDataSetChanged();
		}

		public int getCount() {
			return images.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.galleryitem, null);
				holder.imageview = (ImageView) convertView.findViewById(R.id.thumbImage);
				holder.checkbox = (CheckBox) convertView.findViewById(R.id.itemCheckBox);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			ImageItem item = images.get(position);
			holder.checkbox.setId(position);
			holder.imageview.setId(position);
			holder.checkbox.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					CheckBox cb = (CheckBox) v;
					int id = cb.getId();
					if (images.get(id).selection) {
						cb.setChecked(false);
						images.get(id).selection = false;
					} else {
						cb.setChecked(true);
						images.get(id).selection = true;
					}
				}
			});
			holder.imageview.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					int id = v.getId();

					AppSharedPrefrence.getInstance(CustomGalleryActivity.this).setFileType("Video");

					ImageItem item = images.get(id);
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);

					final String[] columns = { MediaStore.Video.Media.DATA };
					Cursor imagecursor = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,columns, MediaStore.Video.Media._ID + " = "+ item.id, null, MediaStore.Video.Media._ID);

					if (imagecursor != null && imagecursor.getCount() > 0) {
						imagecursor.moveToPosition(0);
						String path = imagecursor.getString(imagecursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));

						AppSharedPrefrence.getInstance(CustomGalleryActivity.this).setFilePath(path);
						File file = new File(path);
//						CommonVariable.getInstance().setFilePath(Uri.fromFile(file).toString());

						if (imagecursor != null && !imagecursor.isClosed()) {
							CustomGalleryActivity.this.stopManagingCursor(imagecursor);
							imagecursor.close();
						}
						//imagecursor.close();
						imgSelected.setImageBitmap(item.img);

//						 intent.setDataAndType(Uri.fromFile(file), "video/*");
//						 startActivityForResult(intent, VIEW_IMAGE);
					}
				}
			});
			holder.imageview.setImageBitmap(item.img);
			holder.checkbox.setChecked(item.selection);
			return convertView;
		}
	}

//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		imgSelected.setImageBitmap(null);
//		//bmp.recycle();
//	}

	public class ImageAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		public ArrayList<ImageItem> images = new ArrayList<ImageItem>();

		public ImageAdapter() {
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@SuppressWarnings("deprecation")
		public void initializeImages() {
			images.clear();
			final String[] columns = { MediaStore.Images.Thumbnails._ID };
			final String orderBy = MediaStore.Images.Media._ID;
			Cursor imagecursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,null, null, orderBy);
			if (imagecursor != null) {
				int image_column_index = imagecursor.getColumnIndex(MediaStore.Images.Media._ID);
				int count = imagecursor.getCount();
				for (int i = 0; i < count; i++) {
					imagecursor.moveToPosition(i);
					int id = imagecursor.getInt(image_column_index);
					ImageItem imageItem = new ImageItem();
					imageItem.id = id;
					lastId = id;
					imageItem.img = MediaStore.Images.Thumbnails.getThumbnail(getApplicationContext().getContentResolver(), id,MediaStore.Images.Thumbnails.MICRO_KIND, null);
					images.add(imageItem);
				}
				if (imagecursor != null && !imagecursor.isClosed()) {
					CustomGalleryActivity.this.stopManagingCursor(imagecursor);
					imagecursor.close();
				}
				//imagecursor.close();
			}
			// notifyDataSetChanged();
		}


		@SuppressWarnings("deprecation")
		public void checkForNewImages() {
			// Here we'll only check for newer images
			final String[] columns = { MediaStore.Images.Thumbnails._ID };
			final String orderBy = MediaStore.Images.Media._ID;
			Cursor imagecursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,MediaStore.Images.Media._ID + " > " + lastId, null, orderBy);
			int image_column_index = imagecursor.getColumnIndex(MediaStore.Images.Media._ID);
			int count = imagecursor.getCount();
			for (int i = 0; i < count; i++) {
				imagecursor.moveToPosition(i);
				int id = imagecursor.getInt(image_column_index);
				ImageItem imageItem = new ImageItem();
				imageItem.id = id;
				lastId = id;
				imageItem.img = MediaStore.Images.Thumbnails.getThumbnail(getApplicationContext().getContentResolver(), id,MediaStore.Images.Thumbnails.MICRO_KIND, null);

//				imageItem.img_big =MediaStore.Images.Thumbnails.getThumbnail(
//				 getApplicationContext().getContentResolver(), id,
//				 MediaStore.Images.Thumbnails.FULL_SCREEN_KIND, null);
				
				imageItem.selection = true; // newly added item will be selected by default
				images.add(imageItem);
			}
			if (imagecursor != null && !imagecursor.isClosed()) {
				CustomGalleryActivity.this.stopManagingCursor(imagecursor);
				imagecursor.close();
			}
			//imagecursor.close();
			notifyDataSetChanged();
		}

		public int getCount() {
			return images.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.galleryitem, null);
				holder.imageview = (ImageView) convertView.findViewById(R.id.thumbImage);
				holder.checkbox = (CheckBox) convertView.findViewById(R.id.itemCheckBox);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			ImageItem item = images.get(position);
			holder.checkbox.setId(position);
			holder.imageview.setId(position);
			holder.checkbox.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					CheckBox cb = (CheckBox) v;
					int id = cb.getId();
					if (images.get(id).selection) {
						cb.setChecked(false);
						images.get(id).selection = false;
					} else {
						cb.setChecked(true);
						images.get(id).selection = true;
					}
				}
			});

			holder.imageview.setOnClickListener(new OnClickListener() {

				@SuppressWarnings("deprecation")
				public void onClick(View v) {

					
					AppSharedPrefrence.getInstance(CustomGalleryActivity.this).setFileType("Image");

					int id = v.getId();
					ImageItem item = images.get(id);
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					final String[] columns = { MediaStore.Images.Media.DATA };
					@SuppressWarnings("deprecation")
					Cursor imagecursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,columns, MediaStore.Images.Media._ID + " = "+ item.id, null,MediaStore.Images.Media._ID);
					if (imagecursor != null && imagecursor.getCount() > 0) {
						imagecursor.moveToPosition(0);
						String path = imagecursor.getString(imagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
						AppSharedPrefrence.getInstance(CustomGalleryActivity.this).setFilePath(path);

						File file = new File(path);

						if (imagecursor != null && !imagecursor.isClosed()) {
							CustomGalleryActivity.this.stopManagingCursor(imagecursor);
							imagecursor.close();
						}
						//imagecursor.close();
						imgSelected.setImageBitmap(item.img);
	
//						 intent.setDataAndType( Uri.fromFile(file), "image/*");
//						 startActivityForResult(intent, VIEW_IMAGE);
					}
				}
			});
			////////////////////////////
			//holder.imageview.setBackground(null);
			holder.imageview.setImageBitmap(item.img);
			holder.checkbox.setChecked(item.selection);
			return convertView;
		}
		
	}

	class ViewHolder {
		ImageView imageview;
		CheckBox checkbox;
	}

	class ImageItem {
		boolean selection;
		int id;
		Bitmap img;
		Bitmap img_big;
	}
}