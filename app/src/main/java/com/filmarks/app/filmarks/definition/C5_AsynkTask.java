package com.filmarks.app.filmarks.definition;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by user on 15/06/19.
 */
public class C5_AsynkTask extends AsyncTaskLoader<String> {

	private Context context;
	private String url;
	private HttpResponse response;
	private int id;

	public C5_AsynkTask(Context context, GridCard[] gridCards, int id) {
		super(context);

		this.context = context;
		this.url = url;
		this.id = id;
	}

	@Override
	public String loadInBackground() {

		// リクエストするクライアントの設定
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);

		/* POST送信する場合はデータを格納 */
		// マニフェスト
		// <uses-permission android:name="android.permission.INTERNET"/>
		// <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>

		// PHPに渡す文字列
		ArrayList<NameValuePair> arrayList = new ArrayList<>();
		arrayList.add(new BasicNameValuePair("moji", Integer.toString(id)));
		/* Jsonで受け取りStringで返す */
		String jsonText = null;
		// レスポンス受信
		try {
			post.setEntity(new UrlEncodedFormEntity(arrayList, "UTF-8"));
			response = client.execute(post);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			response.getEntity().writeTo(byteArrayOutputStream);
			jsonText = byteArrayOutputStream.toString();
			Log.v("test", "Async / response.getStatusLine : " + response.getStatusLine().toString());
			Log.v("test", "Async / byteArrayOutputStream : " + byteArrayOutputStream);
		} catch (IOException e) {
			Log.v("test", "OutputStream : " + e);
		}

		return jsonText;
	}

	@Override
	protected void onStartLoading() {
		Log.v("test", "Async / onStartLoading");
		forceLoad();
	}
}
