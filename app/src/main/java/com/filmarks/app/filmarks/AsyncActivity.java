package com.filmarks.app.filmarks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.filmarks.app.filmarks.definition.AsyncSQL;
import com.filmarks.app.filmarks.definition.DownloadService;
import com.filmarks.app.filmarks.urlimageview.ImageCache;
import com.filmarks.app.filmarks.definition.MyAsyncTask;
import com.filmarks.app.filmarks.urlimageview.UrlImageView;
import com.filmarks.app.filmarks.urlimageview.UrlImageView.OnImageLoadListener;
import com.mysql.jdbc.ResultSetMetaData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;


public class AsyncActivity extends AppCompatActivity implements View.OnClickListener {

	static String TAG = "Download1";
	private TextView file_name, prog_textview;
	private EditText editText;
	private Button imgGet_btn, async_btn, url_get_btn, sqlGet_btn, jdbc_btn;
	private ImageView movie_image;
	private MyAsyncTask task;
	private UrlImageView url_imageView;
	private Random rnd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_async);

		file_name = (TextView)findViewById(R.id.file_name);
		editText  = (EditText)findViewById(R.id.editText);
		prog_textview = (TextView)findViewById(R.id.prog_textview);
		imgGet_btn = (Button)findViewById(R.id.httpGet_btn);
		movie_image = (ImageView)findViewById(R.id.movie_image);
		async_btn = (Button)findViewById(R.id.asyncGet_btn);
		jdbc_btn = (Button)findViewById(R.id.jdbc_btn);
		url_get_btn = (Button)findViewById(R.id.url_imageView_btn);
		url_imageView = (UrlImageView)findViewById(R.id.url_imageView);
		sqlGet_btn = (Button)findViewById(R.id.sqlGet_btn);

		imgGet_btn.setOnClickListener(this);
		async_btn.setOnClickListener(this);
		jdbc_btn.setOnClickListener(this);
		url_get_btn.setOnClickListener(this);
		sqlGet_btn.setOnClickListener(this);

		// BroadcastREcievier
		DownloadProgressBroadcastReceiver progressReceiver = new DownloadProgressBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("DOWNLOAD_PROGRESS_ACTION");
		//registerReceiver(progressReceiver, intentFilter);
	}


	private OnImageLoadListener imageLoadListener = new OnImageLoadListener() {
		@Override
		public void onStart(String url) {
			// 画像読み込み開始
			Toast.makeText(AsyncActivity.this, "start", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onComplete(String url) {
			// 画像読み込み終了
			Toast.makeText(getApplicationContext(), "end", Toast.LENGTH_SHORT).show();
		}
	};


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.httpGet_btn:

				/** 方法1,BroadcastReciever (HTTPでダウンロード) */
				Intent intent = new Intent(getBaseContext(), DownloadService.class);
				intent.putExtra("url", "http://160.16.127.123/android/filmarks/php/pdotest1_hfmn.php");
				startService(intent);
				break;

			case R.id.asyncGet_btn:

				/** 方法2,AsyncTask (PHPでダウンロード) */
				// URLを取得
				editText.setText("http://160.16.127.123/android/filmarks/php/json.php");
				SpannableStringBuilder builder = (SpannableStringBuilder)editText.getText();
				String text = editText.getText().toString();//builder.toString();

				// 非同期で画像を取得
				task = new MyAsyncTask(this);
				task.execute(text);
				break;

			case R.id.jdbc_btn:

				/** 方法3,JDBC */
				// MySQLの場合 - jdbc:mysql://(ホスト名orIP):(ポート)/(データベース名)
				final String db_url = "jdbc:mysql://160.16.127.123:3306/filmarks";
				final String db_usr = "root";
				final String db_pas = "reo1151s";

				// AsynkTaskの場合
				final Handler handler = new Handler();

				// 1,呼び出すexecuteメソッドの引数 ＆ doInBackgroundの引数
				// 2,進捗度合を表示する時に利用したい型 - onProgressUpdateメソッドの引数の方
				// 3,doInBackgroundの返り値 ＆ onPostExecuteメソッドの引数
				new AsyncTask<String,Integer,String>() {
					@Override
					protected String doInBackground(String... strings) {

						String result = null;

						try {

							// ドライバーの読み込み
							Class.forName("com.mysql.jdbc.Driver");
							Log.v("test", "Driver : OK");

							// MySQLサーバーへの接続
							Connection conn = DriverManager.getConnection(db_url, db_usr, db_pas);
							Log.v("test", "Connection : OK");

							result = "Database connection success\n";

							// JDBCでクエリー作成
							Statement statement = conn.createStatement();
							// .executeQuery() で抽出
							ResultSet resultSet = statement.executeQuery("select * from filmarks");
							ResultSetMetaData metaData = (ResultSetMetaData) resultSet.getMetaData();

							while (resultSet.next()) {
								result += metaData.getColumnName(1) + ":" + resultSet.getInt(1) + "\n";
								result += metaData.getColumnName(2) + ":" + resultSet.getString(2) + "\n";
								result += metaData.getColumnName(3) + ":" + resultSet.getString(3) + "\n";
							}

							resultSet.close();
							statement.close();
							conn.close();
							Log.v("test", "result " + result);

						} catch (final ClassNotFoundException e) {

							Log.v("test", "ClassNotFoundEx / " + e.toString());
							new Thread(new Runnable() {
								@Override
								public void run() {
									handler.post(new Runnable() {
										@Override
										public void run() {
											file_name.setText(e.toString());
										}
									});
								}
							}).start();

						} catch (final SQLException e) {

							e.printStackTrace();
							Log.v("test", "SQLEx / " + e.toString());
							new Thread(new Runnable() {
								@Override
								public void run() {
									handler.post(new Runnable() {
										@Override
										public void run() {
											file_name.setText(e.toString());
										}
									});
								}
							}).start();

						}

						return result;
					}

					@Override
					protected void onPostExecute(final String str) {
						// doInBackgroundの戻り値を引数にしている
						if (file_name.getText()!="しっぱい") {
							new Thread(new Runnable() {
								@Override
								public void run() {
									handler.post(new Runnable() {
										@Override
										public void run() {
											if (str != null) {
												file_name.setText(str);
											} else {
												Log.v("test", "onPostExecute / run : null");
											}
										}
									});
								}
							}).start();
						}
					}
				}.execute();

				break;

			case R.id.url_imageView_btn:

				rnd = new Random();
				int ran = rnd.nextInt(4);
				Log.v("test", "rnd = " + ran);
				String url = null;

				switch (ran) {
					case 0:
						url = "http://160.16.127.123/android/filmarks/icon/icon_afr.jpeg";
						break;
					case 1:
						url = "http://160.16.127.123/android/filmarks/icon/icon_hate.jpeg";
						break;
					case 2:
						url = "http://160.16.127.123/android/filmarks/icon/icon_mo.jpeg";
						break;
					case 3:
						url = "http://160.16.127.123/android/filmarks/icon/icon_tenki.jpeg";
						break;
				}
				file_name.setText(url);

				/** 方法3,UrlImageView を使用して取得 */
				url_imageView.setImageUrl(url, imageLoadListener);
				break;

			case R.id.sqlGet_btn:

				// サーバーからSQLを取得してリストに表示する
				AsyncSQL asyncSQL = new AsyncSQL(this);
				asyncSQL.execute();
				break;
		}
	}


	class DownloadProgressBroadcastReceiver extends BroadcastReceiver {
		@Override public void onReceive(Context context, Intent intent) {

			// Show Progress
			Bundle bundle = intent.getExtras();
			int completePercent = bundle.getInt("completePercent");
			int totalByte = bundle.getInt("totalByte");
			String progressString = totalByte + " byte read.";

			if(0 < completePercent){
				progressString += "[" + completePercent + "%]";
			}
			prog_textview.setText(progressString);

			// If completed, show the picture.
			if(completePercent == 100){
				String fileName = bundle.getString("filename");
				Bitmap bitmap = BitmapFactory.decodeFile("/data/data/com.keicode.android.test/files/" + fileName);
				if(bitmap != null){
					Log.v("test", "bitmap is not null");
					movie_image.setImageBitmap(bitmap);
				} else {
					Log.v("test", "bitmap is null");
				}
			}
		}
	}


	@Override
	protected void onDestroy() {
		// UrlImageViewでキャッシュした画像を削除
		ImageCache.deleteAll(getCacheDir());
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_async, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
