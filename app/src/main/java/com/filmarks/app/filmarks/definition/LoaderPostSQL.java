package com.filmarks.app.filmarks.definition;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by user on 15/06/19.
 */
public class LoaderPostSQL extends AsyncTaskLoader<String> {

	private String urlText,key,id;
	private String json;

	public LoaderPostSQL(Context context, String url, String key, String json, String id) {
		super(context);
		this.urlText = url;
		this.key = key;
		this.json = json;
		this.id = id;
	}

	@Override
	public String loadInBackground() {

		String callback = "";
		try {
			URL url = new URL(urlText);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);

			/* POST送信する場合はデータを格納 */
			// PHPに渡す文字列
			PrintStream printStream = new PrintStream(conn.getOutputStream());
			printStream.print("POST=" + json);
			printStream.close();

			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			callback = reader.readLine();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


		return callback;
	}

	@Override
	protected void onStartLoading() {
		forceLoad();
	}
}
