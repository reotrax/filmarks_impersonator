package com.filmarks.app.filmarks.definition;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpException;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by user on 15/06/07.
 */
public class DownloadService extends IntentService {

	static String TAG = "Download1";

	public DownloadService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		try {

			Bundle bundle = intent.getExtras();
			if (bundle == null) {
				Log.d(TAG, "bundle == null");
				return;
			}
			String urlString = bundle.getString("url");

			// HTTP Connection
			URL url = new URL(urlString);
			String fileName = getFilenameFromURL(url);
			Log.d(TAG, fileName);
			URLConnection connection = url.openConnection();

			HttpURLConnection conn = (HttpURLConnection)connection;
			conn.setAllowUserInteraction(false);
			conn.setInstanceFollowRedirects(true);
			conn.setRequestMethod("GET");
			conn.connect();
			int response = conn.getResponseCode();

			// Check Response
			if (response != HttpURLConnection.HTTP_OK) {
				throw new HttpException();
			}
			int contentLength = conn.getContentLength();

			InputStream in = conn.getInputStream();

			FileOutputStream outputStream = openFileOutput(fileName, MODE_PRIVATE);

			DataInputStream dataInputStream = new DataInputStream(in);
			DataOutputStream dataOutputStream = new DataOutputStream(
					new BufferedOutputStream(outputStream));

			// Read Data
			byte[] b = new byte[4096];
			int readByte = 0, totalByte = 0;

			while (-1 != (readByte = dataInputStream.read(b))) {
				dataOutputStream.write(b, 0, readByte);
				totalByte += readByte;
				sendProgressBroadcast(
						contentLength,
						totalByte,
						fileName);
			}

			dataInputStream.close();
			dataOutputStream.close();

			if (contentLength < 0) {
				sendProgressBroadcast(totalByte, totalByte, fileName);
			}

		} catch (MalformedURLException e) {
			Log.d("TAG", e.toString());
		} catch (IOException e) {
			Log.d("TAG", e.toString());
		} catch (HttpException e) {
			Log.d("TAG", e.toString());
		}
	}


	protected void sendProgressBroadcast(int contentLength, int totalByte, String filename) {
		Intent broadcastIntent = new Intent();
		int completePercent = contentLength < 0 ?
				-1:((totalByte*1000)/(contentLength*10));
		Log.d(TAG, "completePercent = " + completePercent);
		Log.d(TAG, "totalByte = " + totalByte);
		Log.d(TAG, "fileName = " + filename);

		broadcastIntent.putExtra("completePercent", completePercent);
		broadcastIntent.putExtra("totalByte", totalByte);
		broadcastIntent.putExtra("filename", filename);
		broadcastIntent.setAction("DOWNLOAD_PROGRESS_ACTION");
		getBaseContext().sendBroadcast(broadcastIntent);
	}

	protected String getFilenameFromURL(URL url){
		String[] p = url.getFile().split("/");
		String s = p[p.length-1];
		if(s.indexOf("?") > -1){
			return s.substring(0, s.indexOf("?"));
		}
		return s;
	}
}
