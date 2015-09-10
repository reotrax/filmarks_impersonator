package com.filmarks.app.filmarks.definition;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by user on 15/06/19.
 */
public class LoaderForGetImage extends AsyncTaskLoader<String> {

	private Context context;
	private String url;
	private HttpResponse response;

	public LoaderForGetImage(Context context, String url) {
		super(context);

		this.context = context;
		this.url = url;
	}

	@Override
	public String loadInBackground() {

		// リクエストするクライアントの設定
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);

		/* Jsonで受け取りStringで返す */
		String jsonText = null;
		try {

			// HttpGet受信
			response = client.execute(httpGet);
			Log.v("test", response.getStatusLine().toString());
			jsonText = EntityUtils.toString(response.getEntity(), "UTF-8");
			Log.v("test", "loadInBackground, jsonText = " + jsonText);

		} catch (UnsupportedEncodingException e) {
			Log.v("test", e.toString());
		} catch (IOException e) {
			Log.v("test", e.toString());
		}

		return jsonText;
	}

	@Override
	protected void onStartLoading() {
		forceLoad();
	}
}
