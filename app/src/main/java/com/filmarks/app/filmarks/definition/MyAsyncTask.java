package com.filmarks.app.filmarks.definition;

import android.app.Activity;
import android.app.SearchManager;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.filmarks.app.filmarks.R;
import com.filmarks.app.filmarks.fragments.C1_F_Following;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by user on 15/06/06.
 */
public class MyAsyncTask extends AsyncTask<String, Integer, String> implements SearchManager.OnCancelListener {
	// 1,呼び出すexecuteメソッドの引数 ＆ doInBackgroundの引数
	// 2,進捗度合を表示する時に利用したい型 - onProgressUpdateメソッドの引数の方
	// 3,doInBackgroundの返り値 ＆ onPostExecuteメソッドの引数

	// クライアント設定
	private Activity activity;
	private String url = null;
	HttpResponse response = null;
	ByteArrayOutputStream byteArrayOutputStream;
	private JSONObject jsonObject;


	public MyAsyncTask(Activity activity) {
		super();
		this.activity = activity;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... contents) {

		url = contents[0].toString();
		Log.v("test", "url = " + url);

		// リクエストするクライアントの設定
		HttpClient client = new DefaultHttpClient();
		//HttpPost post = new HttpPost(url);
		HttpGet httpGet = new HttpGet(url);

		// POST送信する場合はデータを格納
//		ArrayList<NameValuePair> arrayList = new ArrayList<>();
//		arrayList.add(new BasicNameValuePair("filmarks", url));
//		post.setEntity(new UrlEncodedFormEntity(arrayList, "UTF-8"));
//		response = client.execute(post);
//		// レスポンス受信
//		try {
//			byteArrayOutputStream = new ByteArrayOutputStream();
//			response.getEntity().writeTo(byteArrayOutputStream);
//			response.getEntity();
//		} catch (IOException e) {
//			Log.v("test", "OutputStream : " + e);
//		}

		String entityString = null;
		try {

			// HttpGet受信
			response = client.execute(httpGet);
			Log.v("test", response.getStatusLine().toString());
			entityString = EntityUtils.toString(response.getEntity(), "UTF-8");
			// JSONを取り出しやすい様にタイトルを付ける
			entityString = entityString;
			Log.v("test", entityString);

		} catch (UnsupportedEncodingException e) {
			Log.v("test", e.toString());
		} catch (IOException e) {
			Log.v("test", e.toString());
		}

		return entityString;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values[0]);
		Log.v("test", "onProgressUpdate - " + values[0]);
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		Log.v("test", "onCancelled");
	}

	@Override
	protected void onPostExecute(String entityString) {
		Log.v("test", "JSON / entityString = " + entityString);

		// サーバーからの応答を取得
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

			// JSONオブジェクトの生成
			try {
				Log.v("test", "JSON / OK");

				// JSONデータから一つ一つ取得する場合
				jsonObject = new JSONObject(entityString);
				//String id = jsonObject.getString("id");

				// JSONデータが配列の場合
				String name = null;

				/* ----- entityString に下記のJSONデータができている。
				{"filmarks" :
				[{
				"id":"0",
				"user_name":"??",
				"user_img":"http:\/\/160.16.127.123\/android\/filmarks\/icon\/icon_tenki.jpeg",
				"title":"???????","title_img":"http:\/\/160.16.127.123\/android\/filmarks\/icon\/movie_the_italian_job.jpg",
				"comment":"????\n?????"},
				{
				"id":"1",
				"user_name":"hate",
				"user_img":"http:\/\/160.16.127.123\/android\/filmarks\/icon\/icon_hate.jpeg",
				"title":"??????????","title_img":"http:\/\/160.16.127.123\/android\/filmarks\/icon\/movie_steve_jobs.jpg",
				"comment":"??????\n???????????"
				}]}
				*/

//				// JSONデータのタイトル　"filmarks"　を指定
//				JSONArray jsonArray = jsonObject.getJSONArray("filmarks");
//				for (int i=0; i<jsonArray.length(); i++) {
//					JSONObject object = jsonArray.getJSONObject(i);
//					name = object.getString("user_name");
//				}
//				Log.v("test", "name = " + name);



				// Snackbar を表示
//				LinearLayout layout = (LinearLayout)activity.findViewById(R.id.snackbar);
//				Snackbar.make(layout, "name = " + name, Snackbar.LENGTH_LONG)
//						.setAction("UNDO", new View.OnClickListener() {
//							@Override
//							public void onClick(View v) {
//								// "UNDO"ボタンを押下した際のアクションを記述
//							}
//						})
//								// .setActionTextColor(Color.rgb(255,255,255))
//						.show();

			} catch (JSONException e) {
				Log.v("test", "jsonObject = " + e.toString());
			}

			// サーバーから受け取った文字列の処理
//			if (byteArrayOutputStream.toString().equals("1")) {
//				Toast.makeText(this.activity, "[ここには処理１] ", Toast.LENGTH_LONG).show();
//			} else {
//				Toast.makeText(this.activity, "[ここには処理２] ", Toast.LENGTH_LONG).show();
//			}

		} else {

			// Snackbar を表示
			LinearLayout layout = (LinearLayout)activity.findViewById(R.id.snackbar);
			Snackbar.make(layout, "[error]: "+response.getStatusLine(), Snackbar.LENGTH_LONG)
					.setAction("UNDO", new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							// "UNDO"ボタンを押下した際のアクションを記述
						}
					})
					// .setActionTextColor(Color.rgb(255,255,255))
					.show();
		}
	}

	// OnCancelListener
	@Override
	public void onCancel() {

	}
}
