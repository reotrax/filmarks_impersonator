package com.filmarks.app.filmarks.definition;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
