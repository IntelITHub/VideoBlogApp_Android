package com.iih.android.videoblog.newsfeed;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class DownloadVideo extends AsyncTask<Void, Void, Void> {
	Context context;
	private ProgressDialog bar;
	String Url;
	String fileName;

	public DownloadVideo() {
	}

	public DownloadVideo(Context context, String fileURL,String name) {
		this.context = context;
		Url = fileURL;
		fileName=name;
	}

	public DownloadVideo(Context context) {
		this.context = context;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		try {
			// imagegrid.setAdapter(videoAdapter);
			bar.dismiss();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		try {
			bar = new ProgressDialog(context);
			bar.setIndeterminate(true);
			bar.setCancelable(false);
			bar.setTitle("Downloading...");
			bar.show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected Void doInBackground(Void... arg0) {
		// videoAdapter.initializeVideo();
		DownloadFile(Url, fileName);
		return null;
	}

	public void DownloadFile(String fileURL, String fileName) {
		try {
			String RootDir = Environment.getExternalStorageDirectory()+ File.separator + "Video";
			File RootFile = new File(RootDir);
			RootFile.mkdir();
			// File root = Environment.getExternalStorageDirectory();
			URL u = new URL(fileURL);
			HttpURLConnection c = (HttpURLConnection) u.openConnection();
			c.setRequestMethod("GET");

			c.setDoOutput(true);
			c.connect();

			c.setConnectTimeout(30000);
			FileOutputStream f = new FileOutputStream(new File(RootFile,fileName));
			InputStream in = c.getInputStream();
			// byte[] buffer = new byte[1024];
			byte[] buffer = new byte[33000];// 8192
			int len1 = 0;

			while ((len1 = in.read(buffer)) > 0) {
				f.write(buffer, 0, len1);
			}
			f.close();

		} catch (Exception e) {

			Log.d("Error....", e.toString());

		}

	}
}