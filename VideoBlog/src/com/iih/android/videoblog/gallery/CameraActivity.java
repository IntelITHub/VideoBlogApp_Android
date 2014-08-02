package com.iih.android.videoblog.gallery;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.util.Common;
import com.iih.android.videoblog.R;
import com.iih.android.videoblog.Parsing.UploadPicture;
import com.iih.android.videoblog.commonutill.AppSharedPrefrence;
import com.iih.android.videoblog.commonutill.CommonVariable;
import com.iih.android.videoblog.newsfeed.NewsFeedScreen;

public class CameraActivity extends Activity implements OnClickListener{

	private FrameLayout CameraView;
	private Button rotateCamera;
	private Button backButton;
	private ImageView galleryBtn;
	private Button ImageCapturebtn;
	private Button VideoCapturebtn;
	private Button NextBtn;

	private Button cancelStartStop;

	private Camera myCamera;
	private MyCameraSurfaceView myCameraSurfaceView;
	private MediaRecorder mediaRecorder;
	boolean recording =  false;
	boolean capture =  false;

	private ImageView imgCapture;

	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;

	private int SELECT_PHOTO = 110;

	SurfaceHolder surfaceHolder;

	UploadPicture upload;

	private Boolean flash = false;

	private Boolean RotateCamera = false;
	
	private TextView txtOnOFF;
	
	private Boolean comefromStart = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.customcamera);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		initialization();
		


		// Get Camera for preview
		//return false in a
		myCamera = getCameraInstance(false);
		if (myCamera == null) {
			Toast.makeText(CameraActivity.this, "Fail to get Camera",Toast.LENGTH_LONG).show();
		}

		myCameraSurfaceView = new MyCameraSurfaceView(this, myCamera);
		CameraView = (FrameLayout) findViewById(R.id.cameraview);
		CameraView.addView(myCameraSurfaceView);

		// Checking camera availability
				if (!isDeviceSupportCamera()) {
					Toast.makeText(getApplicationContext(),"Sorry,Your Device dosen't support Camera.",Toast.LENGTH_LONG).show();
					// will close the app if the device does't have camera
					finish();
				}

				ImageCapturebtn.setOnClickListener(this);
				VideoCapturebtn.setOnClickListener(this);
				txtOnOFF.setOnClickListener(this);
				backButton.setOnClickListener(this);
				galleryBtn.setOnClickListener(this);
				NextBtn.setOnClickListener(this);
				rotateCamera.setOnClickListener(this);
				cancelStartStop.setOnClickListener(this);
				
				if (AppSharedPrefrence.getInstance(CameraActivity.this).getFilePath() == null) {
					
				}else{
					CameraView.setVisibility(View.GONE);
					imgCapture.setVisibility(View.VISIBLE);
					cancelStartStop.setVisibility(View.VISIBLE);
					ImageCapturebtn.setEnabled(false);
					VideoCapturebtn.setEnabled(false);
					
					rotateCamera.setVisibility(View.GONE);
					txtOnOFF.setVisibility(View.GONE);
					
					if(AppSharedPrefrence.getInstance(CameraActivity.this).getFileType().equals("Image")){
						Bitmap bmp;//= BitmapFactory.decodeFile(CommonVariable.getInstance().getFilePath());
						bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(AppSharedPrefrence.getInstance(CameraActivity.this).getFilePath()),600, 600, true);
						imgCapture.setImageBitmap(bmp);
						comefromStart = true;
					}else{
						Bitmap bitmap =ThumbnailUtils.createVideoThumbnail(AppSharedPrefrence.getInstance(CameraActivity.this).getFilePath(),MediaStore.Video.Thumbnails.MINI_KIND);
						imgCapture.setImageBitmap(bitmap);
						comefromStart = true;
					}
				}
		
			
		//set captured image to gallery imageview
		if (AppSharedPrefrence.getInstance(CameraActivity.this).getFileType() == null) {
		
		}else{
			if (AppSharedPrefrence.getInstance(CameraActivity.this).getFileType().equals("Image")){
		
			Bitmap bmp;
			bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(AppSharedPrefrence.getInstance(CameraActivity.this).getFilePath()),100, 100, true);
			galleryBtn.setImageBitmap(bmp);
		}else{
			Bitmap bitmap =ThumbnailUtils.createVideoThumbnail(AppSharedPrefrence.getInstance(CameraActivity.this).getFilePath(),MediaStore.Video.Thumbnails.MINI_KIND);
			galleryBtn.setImageBitmap(bitmap);
		}
		}
	}

	/**
	 * Checking device has camera hardware or not
	 * */
	private boolean isDeviceSupportCamera() {
		if (getApplicationContext().getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			// this device has a camera
			return true;
		} else {
			// no camera on this device
			return false;
		}
	}

	/**
	 * instantiate camera
	 * @return
	 */
	public Camera getCameraInstance(boolean rotate) {
		// TODO Auto-generated method stub
		Camera c = null;
		try {
			//c = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
			// attempt to get a Camera instance
			if(rotate == true){
				c = Camera.open(CameraInfo.CAMERA_FACING_FRONT);
			}else{
				c = Camera.open(CameraInfo.CAMERA_FACING_BACK);
			}
			c.setDisplayOrientation(90);
		} catch (Exception e) {
			// Camera is not available (in use or does not exist)
		}
		return c; // returns null if camera is unavailable
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	


	private boolean prepareMediaRecorder() {
		myCamera = getCameraInstance(RotateCamera);

		Parameters parameters = myCamera.getParameters();

		boolean hasFlash = CameraActivity.this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
		if(hasFlash == true){
			try {
				if(flash == true){
					parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
				}else {
					parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			Toast.makeText(getApplicationContext(),"Sorry,Your camera dosen't support flash.",Toast.LENGTH_LONG).show();
		}
		
		myCamera.setParameters(parameters);

		mediaRecorder = new MediaRecorder();

		myCamera.unlock();
		mediaRecorder.setCamera(myCamera);

		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
		mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

		mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

		mediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());
		mediaRecorder.setMaxDuration(6000000); // Set max duration 60 sec.
		mediaRecorder.setMaxFileSize(50000000); // Set max file size 5M
	
		mediaRecorder.setPreviewDisplay(myCameraSurfaceView.getHolder().getSurface());

		mediaRecorder.setOrientationHint(90);

		try {
			mediaRecorder.prepare();
		} catch (IllegalStateException e) {
			releaseMediaRecorder();
			return false;
		} catch (IOException e) {
			releaseMediaRecorder();
			return false;
		}
		return true;

	}


	@Override
	protected void onPause() {
		super.onPause();
		releaseMediaRecorder(); 
		// if you are using MediaRecorder, release it first
		releaseCamera(); // release the camera immediately on pause event
		
	}

	private void releaseMediaRecorder() {
		if (mediaRecorder != null) {
			mediaRecorder.reset(); // clear recorder configuration
			mediaRecorder.release(); // release the recorder object
			mediaRecorder = null;
			myCamera.lock(); // lock camera for later use
		}
	}

	private void releaseCamera() {
		if (myCamera != null) {
			myCamera.release();
			myCamera = null;
		}
	}

//	/** Create a file Uri for saving an image or video */
//	private static Uri getOutputMediaFileUri(int type) {
//		return Uri.fromFile(getOutputMediaFile(type));
//	}

	@SuppressLint("SimpleDateFormat")
	public  File getOutputMediaFile(int type) {

		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"Vrex");
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
			AppSharedPrefrence.getInstance(CameraActivity.this).setFilePath(mediaFile.getAbsolutePath());
		} else if (type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
			String path = mediaFile.getAbsolutePath();
			AppSharedPrefrence.getInstance(CameraActivity.this).setFilePath(path);
		} else {
			return null;
		}

		// mediaFile = Environment.getExternalStorageDirectory().getPath()+
		// "/default.mp4";
		return mediaFile;
	}


	private void initialization() {
		// TODO Auto-generated method stub
		CameraView = (FrameLayout) (findViewById(R.id.cameraview));
		rotateCamera = (Button)findViewById(R.id.RotateCamerabutton);
		backButton = (Button)findViewById(R.id.leftArrow);
		galleryBtn = (ImageView)findViewById(R.id.galleybutton);
		ImageCapturebtn = (Button)findViewById(R.id.PhotoCapture);
		VideoCapturebtn = (Button)findViewById(R.id.videoCapture);
		cancelStartStop = (Button)findViewById(R.id.cancelbtn);
		NextBtn = (Button)findViewById(R.id.nextbtn);
		
		txtOnOFF = (TextView)findViewById(R.id.onoff);
		imgCapture = (ImageView)findViewById(R.id.cameraImageView);
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();

		switch (id) {
		case R.id.PhotoCapture:
					
			myCamera.takePicture(null, null, mPicture);
			
			ImageCapturebtn.setEnabled(true);
			VideoCapturebtn.setEnabled(true);

			break;

		case R.id.videoCapture:
			
			AppSharedPrefrence.getInstance(CameraActivity.this).setFileType("Video");
			if (recording) {
				// stop recording and release camera
				mediaRecorder.stop(); // stop the recording
				releaseMediaRecorder(); // release the MediaRecorder object

				myCamera.lock(); 
				// take camera access back from
				recording = false;
		
				cancelStartStop.setBackground(getResources().getDrawable(R.drawable.close));
			
				CameraView.setVisibility(View.GONE);
				imgCapture.setVisibility(View.VISIBLE);
				cancelStartStop.setVisibility(View.VISIBLE);
				
				
				rotateCamera.setVisibility(View.GONE);
				txtOnOFF.setVisibility(View.GONE);
				
				ImageCapturebtn.setEnabled(false);
				VideoCapturebtn.setEnabled(false);
				
				Bitmap bitmap =ThumbnailUtils.createVideoThumbnail(AppSharedPrefrence.getInstance(CameraActivity.this).getFilePath(),MediaStore.Video.Thumbnails.MINI_KIND);
				imgCapture.setImageBitmap(bitmap);
		
				//VideoCapturebtn.setBackground(getResources().getDrawable(R.drawable.video_camera_grey));
				// MediaRecorder
				// inform the user that recording has stopped
				// Exit after saved
				//finish();
			} else {

				// Release Camera before MediaRecorder start
				releaseCamera();

				if (!prepareMediaRecorder()) {
					Toast.makeText(CameraActivity.this,"Fail in prepareMediaRecorder()!\n - Ended -",Toast.LENGTH_LONG).show();
					finish();
				}

				//VideoCapturebtn.setBackground(getResources().getDrawable(R.drawable.video_camera_orange));

				mediaRecorder.start();
				ImageCapturebtn.setEnabled(true);
				VideoCapturebtn.setEnabled(true);
				cancelStartStop.setVisibility(View.VISIBLE);
				cancelStartStop.setBackground(getResources().getDrawable(R.drawable.record));
				recording = true;
			}
			break;

		case  R.id.leftArrow:
			Intent intent =  new Intent(CameraActivity.this,NewsFeedScreen.class);
			startActivity(intent);
			finish();
			break;

		case  R.id.nextbtn:
			Intent intentnext =  new Intent(CameraActivity.this,CustomGalleryActivity.class);
			startActivity(intentnext);
			finish();
			break;

		case R.id.galleybutton:
			upload = new UploadPicture(CameraActivity.this);
			Intent photoPickerIntent; photoPickerIntent = new Intent(Intent.ACTION_PICK);
			photoPickerIntent.setType("image/*");
			startActivityForResult(photoPickerIntent,SELECT_PHOTO);
			AppSharedPrefrence.getInstance(CameraActivity.this).setFileType("Image");
			break;
	
		case R.id.cancelbtn:
			
			ImageCapturebtn.setEnabled(true);
			VideoCapturebtn.setEnabled(true);
			
			cancelStartStop.setVisibility(View.GONE);
			
			imgCapture.setVisibility(View.GONE);
			CameraView.setVisibility(View.VISIBLE);
			
			rotateCamera.setVisibility(View.VISIBLE);
			txtOnOFF.setVisibility(View.VISIBLE);
			
			
			if(comefromStart == true){
				comefromStart = false;
			}else{
			if(AppSharedPrefrence.getInstance(CameraActivity.this).getFileType().equals("Image")){
				CameraView.removeView(myCameraSurfaceView);
				
				myCamera = getCameraInstance(RotateCamera);
		
				myCameraSurfaceView = new MyCameraSurfaceView(this, myCamera);
				CameraView = (FrameLayout) findViewById(R.id.cameraview);
				CameraView.addView(myCameraSurfaceView);
					
				Parameters parameters = myCamera.getParameters();

				boolean ISFlash = CameraActivity.this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
				if(ISFlash == true){
					try {
						if(flash == true){
							parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
						}else {
							parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					//Toast.makeText(getApplicationContext(),"Sorry,Your camera dosen't support flash.",Toast.LENGTH_LONG).show();
				}
				myCamera.setParameters(parameters);
				
			}
			}
			
			break;
			

		case R.id.RotateCamerabutton:
		
			if (RotateCamera == false) {
				
				RotateCamera = true;
				
				CameraView.removeView(myCameraSurfaceView);
				
				myCamera = getCameraInstance(RotateCamera);
		
				myCameraSurfaceView = new MyCameraSurfaceView(this, myCamera);
				CameraView = (FrameLayout) findViewById(R.id.cameraview);
				CameraView.addView(myCameraSurfaceView);
					
				Parameters parameters = myCamera.getParameters();

				boolean hasFlash = CameraActivity.this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
				if(hasFlash == true){
					try {
						if(flash == true){
							parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
						}else {
							parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					//Toast.makeText(getApplicationContext(),"Sorry,Your camera dosen't support flash.",Toast.LENGTH_LONG).show();
				}
				
				myCamera.setParameters(parameters);
				
			} else {
				RotateCamera = false;
				
				CameraView.removeView(myCameraSurfaceView);
			
				myCamera = getCameraInstance(RotateCamera);
				
				myCameraSurfaceView = new MyCameraSurfaceView(this, myCamera);
				CameraView = (FrameLayout) findViewById(R.id.cameraview);
				CameraView.addView(myCameraSurfaceView);

				Parameters parameters = myCamera.getParameters();

				boolean hasFlash = CameraActivity.this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
				if(hasFlash == true){
					try {
						if(flash == true){
							parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
						}else {
							parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					//Toast.makeText(getApplicationContext(),"Sorry,Your camera dosen't support flash.",Toast.LENGTH_LONG).show();
				}
				
				myCamera.setParameters(parameters);
			}
			break;
		
		case R.id.onoff:
		
			if (flash == false) {
	
				txtOnOFF.setText("ON");
				
				flash = true;
					
				CameraView.removeView(myCameraSurfaceView);
				
				myCamera = getCameraInstance(RotateCamera);
		
				myCameraSurfaceView = new MyCameraSurfaceView(this, myCamera);
				CameraView = (FrameLayout) findViewById(R.id.cameraview);
				CameraView.addView(myCameraSurfaceView);
					
				Parameters parameters = myCamera.getParameters();

				boolean hasFlash = CameraActivity.this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
				if(hasFlash == true){
					try {
						if(flash == true){
							parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
						}else {
							parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					//Toast.makeText(getApplicationContext(),"Sorry,Your camera dosen't support flash.",Toast.LENGTH_LONG).show();
				}
				
				myCamera.setParameters(parameters);
	
			} else {
				flash = false;
				
				txtOnOFF.setText("OFF");
				
				CameraView.removeView(myCameraSurfaceView);
				
				myCamera = getCameraInstance(RotateCamera);
		
				myCameraSurfaceView = new MyCameraSurfaceView(this, myCamera);
				CameraView = (FrameLayout) findViewById(R.id.cameraview);
				CameraView.addView(myCameraSurfaceView);
					
				Parameters parameters = myCamera.getParameters();

				boolean hasFlash = CameraActivity.this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
				if(hasFlash == true){
					try {
						if(flash == true){
							parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
						}else {
							parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					//Toast.makeText(getApplicationContext(),"Sorry,Your camera dosen't support flash.",Toast.LENGTH_LONG).show();
				}
				myCamera.setParameters(parameters);
			}
			
			break;

		default:
			break;
		}
	}

	public class MyCameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

		private SurfaceHolder mHolder;
		private Camera mCamera;

		@SuppressWarnings("deprecation")
		public MyCameraSurfaceView(Context context, Camera camera) {
			super(context);
			mCamera = camera;

			// Install a SurfaceHolder.Callback so we get notified when the
			// underlying surface is created and destroyed.
			mHolder = getHolder();
			mHolder.addCallback(this);
			// deprecated setting, but required on Android versions prior to 3.0
			mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format,
				int weight, int height) {
			// If your preview can change or rotate, take care of those events
			// here.
			// Make sure to stop the preview before resizing or reformatting it.

			if (mHolder.getSurface() == null) {
				// preview surface does not exist
				return;
			}

			// stop preview before making changes
			try {
				mCamera.stopPreview();
			} catch (Exception e) {
				// ignore: tried to stop a non-existent preview
			}

			// make any resize, rotate or reformatting changes here

			// start preview with new settings
			try {
				mCamera.setPreviewDisplay(mHolder);
				mCamera.startPreview();

			} catch (Exception e) {
			}
		}
			


		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			// The Surface has been created, now tell the camera where to draw
			// the preview.
			try {
				mCamera.setPreviewDisplay(holder);
				mCamera.startPreview();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			 //stop the preview  
			mCamera.release();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PHOTO) {
			try {
				upload.decodeUri(data.getData());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			AppSharedPrefrence.getInstance(CameraActivity.this).setFilePath(upload.mCurrentPhotoPath);
			Intent intent = new Intent(CameraActivity.this,CustomGalleryActivity.class);
			startActivity(intent);
			finish();
		}
	}

	private PictureCallback mPicture = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// Log.v(TAG, "Getting output media file");
		
			//myCamera.startPreview();
			
			
			File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE); 
			if (pictureFile == null) {
				// Log.v(TAG, "Error creating output file");
				return;
			}
			try {
				
				//Camera image rotate setup 

				Bitmap realImage = BitmapFactory.decodeByteArray(data, 0,data.length);
				android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
				android.hardware.Camera.getCameraInfo(0, info);
				Bitmap bitmap = rotate(realImage, info.orientation);

				FileOutputStream fos = new FileOutputStream(pictureFile);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				fos.write(data);
				fos.close();
				
				AppSharedPrefrence.getInstance(CameraActivity.this).setFileType("Image");

			} catch (FileNotFoundException e) {
				// Log.v(TAG, e.getMessage());
			} catch (IOException e) {
				// Log.v(TAG, e.getMessage());
			}			
			

			Bitmap bmp ;//= BitmapFactory.decodeFile(CommonVariable.getInstance().getFilePath());
			bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(AppSharedPrefrence.getInstance(CameraActivity.this).getFilePath()),600, 600, true);
			imgCapture.setImageBitmap(bmp);

			//set image to galley icon
			Bitmap bmp1 ;//= BitmapFactory.decodeFile(CommonVariable.getInstance().getFilePath());
			bmp1 = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(AppSharedPrefrence.getInstance(CameraActivity.this).getFilePath()),70, 70, true);
			galleryBtn.setImageBitmap(bmp1);

			//added by jignesh
			if (myCamera != null) {
				// Call stopPreview() to stop updating the preview surface.
				myCamera.stopPreview();

				// Important: Call release() to release the camera for use by
				// other
				// applications. Applications should release the camera
				// immediately
				// during onPause() and re-open() it during onResume()).
				myCamera.release();

				myCamera = null;
			}
			
			
			CameraView.setVisibility(View.GONE);
			imgCapture.setVisibility(View.VISIBLE);
			cancelStartStop.setVisibility(View.VISIBLE);
			ImageCapturebtn.setEnabled(false);
			VideoCapturebtn.setEnabled(false);
			
			rotateCamera.setVisibility(View.GONE);
			txtOnOFF.setVisibility(View.GONE);
			
//	
		}
		
	};

	public static Bitmap rotate(Bitmap bitmap, int degree) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		Matrix mtx = new Matrix();
		mtx.postRotate(degree);

		return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent intent =  new Intent(CameraActivity.this,NewsFeedScreen.class);
		startActivity(intent);
		finish();
	}

}


