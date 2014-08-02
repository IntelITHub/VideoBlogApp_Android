package com.iih.android.videoblog.Parsing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Stack;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.iih.android.videoblog.R;


public class ExpImageLoader {

	// the simplest in-memory cache implementation. This should be replaced with
	// something like SoftReference or BitmapOptions.inPurgeable(since 1.6)
	private HashMap<String, WeakReference<Bitmap>> cache = new HashMap<String, WeakReference<Bitmap>>();

	private File cacheDir;
	int REQUIRED_Height = 70;
	int REQUIRED_Width = 70;

	public ExpImageLoader(Context context) {
		photoLoaderThread.setPriority(Thread.NORM_PRIORITY - 1);
		// Find the dir to save cached images
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			cacheDir = new File(android.os.Environment.getExternalStorageDirectory(),"VideoBlog");
			if (!cacheDir.exists())
				cacheDir.mkdirs();
		} else {
			showCommonAlert(
					"But your sdcard is not mounted.Please mount your sdcard and enjoy",
					(Activity) context);

		}

	}

	public void DisplayImage(String url, Activity activity,ImageView imageView, ProgressBar progress) {
		try {
			if (url == null)
				url = "";
			String rl = url.replaceAll(" ", "%20");
			url = rl;
//			Log.w("URL", url);
			if (cache.containsKey(url)) {
				imageView.setVisibility(View.VISIBLE);
				progress.setVisibility(View.GONE);
				Bitmap bitmap = cache.get(url).get();
				if (bitmap != null)
					imageView.setImageBitmap(bitmap);
				else {
					queuePhoto(url, activity, imageView, progress);
				}
			} else {
				queuePhoto(url, activity, imageView, progress);
			}
		} catch (Exception e) {
			e.getMessage();
		}
	}

	private void queuePhoto(String url, Activity activity, ImageView imageView,
			ProgressBar progress) {
		photosQueue.Clean(imageView, progress);
		try {
			PhotoToLoad p = new PhotoToLoad(url, imageView, progress);
			synchronized (photosQueue.photosToLoad) {
				photosQueue.photosToLoad.push(p);
				photosQueue.photosToLoad.notifyAll();
			}

			// start thread if it's not started yet
			if (photoLoaderThread.getState() == Thread.State.NEW)
				photoLoaderThread.start();
		} catch (Exception E) {
			E.getStackTrace();
		}
	}

	private WeakReference<Bitmap> getBitmap(String url) {
		if (url == null)
			url = "http://184.164.156.56/projects/combiz/public/images/1_big-noimage.gif";

		String filename = md5(url);
		File f = new File(cacheDir, filename);
		WeakReference<Bitmap> b = decodeFile(f, url);
		if (b != null)
			return b;
		else {
			try {
				WeakReference<Bitmap> bitmap = null;
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(url);
				HttpResponse response = httpclient.execute(httppost);
				InputStream is = response.getEntity().getContent();
				OutputStream os = new FileOutputStream(f);
				Utils.CopyStream(is, os);
				os.close();
				bitmap = decodeFile(f, url);
				return bitmap;
			} catch (Exception ex) {
				ex.printStackTrace(); // Unknown Host Exception
				return null;
			}
		}
	}

	private WeakReference<Bitmap> decodeFile(File f, String url) {
		try {

			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_Width
						|| height_tmp / 2 < REQUIRED_Height)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale++;
			}
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			o2.inJustDecodeBounds = false;
			return new WeakReference<Bitmap>(BitmapFactory.decodeStream(new FileInputStream(f), null, o2));
		} catch (FileNotFoundException e) {
			try {
				WeakReference<Bitmap> bitmap = null;
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(url);
				HttpResponse response = httpclient.execute(httppost);
				InputStream is = response.getEntity().getContent();
				OutputStream os = new FileOutputStream(f);
				Utils.CopyStream(is, os);
				os.close();
				bitmap = decodeFile(f, url);
				return bitmap;
			} catch (Exception ex) {
				ex.printStackTrace(); // Unknown Host Exception
				return null;
			}
		} catch (Exception e) {
		}
		return null;
	}

	private class PhotoToLoad {
		public String url;
		public ImageView imageView;
		public ProgressBar bar;

		public PhotoToLoad(String u, ImageView i, ProgressBar bar) {
			url = u;
			imageView = i;
			this.bar = bar;
		}
	}

	PhotosQueue photosQueue = new PhotosQueue();

	public void stopThread() {
		photoLoaderThread.interrupt();
	}

	class PhotosQueue {
		private Stack<PhotoToLoad> photosToLoad = new Stack<PhotoToLoad>();

		public void Clean(ImageView image, ProgressBar progress) {
			for (int j = 0; j < photosToLoad.size();) {
				if (photosToLoad.get(j).imageView == image) {
					photosToLoad.remove(j);
					photosToLoad.remove(progress);
				} else
					++j;
			}
		}
	}

	class PhotosLoader extends Thread {
		public void run() {
			try {
				while (true) {
					if (photosQueue.photosToLoad.size() == 0)
						synchronized (photosQueue.photosToLoad) {
							photosQueue.photosToLoad.wait();
						}
					if (photosQueue.photosToLoad.size() != 0) {
						PhotoToLoad photoToLoad;
						synchronized (photosQueue.photosToLoad) {
							photoToLoad = photosQueue.photosToLoad.pop();
						}
						WeakReference<Bitmap> bmp = getBitmap(photoToLoad.url);
						cache.put(photoToLoad.url, bmp);
						try {
							if (((String) photoToLoad.imageView.getTag())
									.equals(photoToLoad.url)) {
								/* photoToLoad.bar.setVisibility(View.GONE); */
								BitmapDisplayer bd = new BitmapDisplayer(
										bmp.get(), photoToLoad.imageView,
										photoToLoad.bar);
								Activity a = (Activity) photoToLoad.imageView
										.getContext();
								a.runOnUiThread(bd);
							}
						} catch (Exception e) {
							e.getMessage();
						}
					}
					if (Thread.interrupted())
						break;
				}
			} catch (InterruptedException e) {
				// allow thread to exit
			}
		}
	}

	PhotosLoader photoLoaderThread = new PhotosLoader();

	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		ImageView imageView;
		ProgressBar bar;

		public BitmapDisplayer(Bitmap b, ImageView i, ProgressBar bar) {
			bitmap = b;
			imageView = i;
			this.bar = bar;
		}

		public void run() {
			if (bitmap != null) {
				imageView.setVisibility(View.VISIBLE);
				imageView.setImageBitmap(bitmap);
				bar.setVisibility(View.GONE);
			} else {
				imageView.setVisibility(View.VISIBLE);
				bar.setVisibility(View.GONE);
			}
		}
	}

	public void clearCache() {
		cache.clear();
		File[] files = cacheDir.listFiles();
		for (File f : files)
			f.delete();
	}

	private void showCommonAlert(String message, final Activity _aActivity) {
		Builder builder = new AlertDialog.Builder(_aActivity);
		builder.setMessage(message);
		builder.setIcon(R.drawable.ic_launcher);
		TextView titleText = new TextView(_aActivity);
		titleText.setTypeface(null, Typeface.BOLD);
		titleText.setTextSize(20);
		titleText.setPadding(0, 10, 0, 0);
		titleText.setGravity(Gravity.CENTER_HORIZONTAL
				| Gravity.CENTER_VERTICAL);
		titleText.setText("We'd love to tell you more..");
		builder.setCustomTitle(titleText);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface d, int which) {
				_aActivity.finish();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public String md5(String s) {
		try {
			StringBuilder string = new StringBuilder(s);
			StringBuilder reverse = string.reverse();
			String extension = reverse.substring(0, reverse.indexOf("."));
			reverse = new StringBuilder(extension);
			extension = reverse.reverse().toString();
			MessageDigest digest = java.security.MessageDigest
					.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < messageDigest.length; i++)
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));

			return hexString.toString() + "." + extension;

		} catch (Exception e) {
			if (e != null)
				e.printStackTrace();
		}
		return "";
	}
}
