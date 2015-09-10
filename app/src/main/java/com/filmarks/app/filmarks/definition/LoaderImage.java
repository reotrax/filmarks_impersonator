package com.filmarks.app.filmarks.definition;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.filmarks.app.filmarks.urlimageview.ImageCache;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;

/**
 * Created by user on 15/06/28.
 */
public class LoaderImage extends AsyncTask<String,Integer,Bitmap> {
	// 1,呼び出すexecuteメソッドの引数 ＆ doInBackgroundの引数
	// 2,進捗度合を表示する時に利用したい型 - onProgressUpdateメソッドの引数の方
	// 3,doInBackgroundの返り値 ＆ onPostExecuteメソッドの引数

	private int width,height;
	private ImageView imageView;
	private Context context;

	public LoaderImage(Context context, ImageView imageView, int width, int height) {
		this.context = context;
		this.imageView = imageView;
		this.width = width;
		this.height = height;
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		// 受信
		Bitmap resizeBitmap = null;

		// 対象URLの画像がキャッシュされていなければ取得
		SoftReference<Bitmap> cacheBit = ImageCache.getImage(context.getCacheDir(), params[0]);
		if (cacheBit != null && cacheBit.get() != null) {
			resizeBitmap = cacheBit.get();
			Log.v("test", "cacheBit");

		} else {
			try {
				// 画像生成
				URL imgUrl = new URL(params[0]);
				InputStream inputStream = imgUrl.openStream();
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				resizeBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);

				// キャッシュに保存
				ImageCache.saveBitmap(context.getCacheDir(), params[0], resizeBitmap);

			} catch (IOException e) {
				Log.v("test", "OutputStream : " + e);
			}
		}

		return resizeBitmap;
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		super.onPostExecute(bitmap);
		imageView.setImageBitmap(bitmap);
	}


	@Override
	protected void onCancelled() {
		super.onCancelled();
	}
}
