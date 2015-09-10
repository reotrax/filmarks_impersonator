package com.filmarks.app.filmarks.urlimageview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.filmarks.app.filmarks.R;

import java.lang.ref.SoftReference;

public final class UrlImageButton extends ImageButton {
	private final Context context;

	private Request request;
	private String url;
	private boolean isLoading = false;
	private final Handler handler = new Handler();
	private OnImageLoadListener listener = new OnImageLoadListener() {
		public void onStart(String url) {
		}

		public void onComplete(String url) {
		}
	};

	public static interface OnImageLoadListener {
		public void onStart(String url);

		public void onComplete(String url);
	}

	private final Runnable threadRunnable = new Runnable() {
		public void run() {
			handler.post(imageLoadRunnable);
		}
	};

	private final Runnable imageLoadRunnable = new Runnable() {
		public void run() {
			setImageLocalCache();
		}
	};

	private boolean setImageLocalCache() {
		SoftReference<Bitmap> image = ImageCache.getImage(
				context.getCacheDir(), url);
		if (image != null && image.get() != null) {
			// 画像を効果的な状態にして読み込む
//			Resources res = getResources();
//			BitmapFactory.Options options = new BitmapFactory.Options();
//			options.inPreferredConfig = Bitmap.Config.RGB_565;
//			Bitmap bitmap = BitmapFactory.decodeResource(res, image.get(), options);

			// リサイズ
			Matrix matrix = new Matrix();
			matrix.postScale(1.15f, 1.15f); // 拡大縮小の倍率をfloatで決める
			Bitmap preBitmap = image.get();
			int width = preBitmap.getWidth();
			int height= preBitmap.getHeight();
			Bitmap bitmap = Bitmap.createBitmap(preBitmap,0,0, width, height, matrix, true);

			// イメージを貼る
			setImageBitmap(bitmap);
			listener.onComplete(url);
			isLoading = false;
			return true;
		}
		return false;
	}

	public UrlImageButton(Context context) {
		super(context);
		this.context = context;
	}

	public UrlImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public UrlImageButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public void setOnImageLoadListener(OnImageLoadListener listener) {
		this.listener = listener;
	}

	public void setImageUrl(String url, OnImageLoadListener listener) {
		setOnImageLoadListener(listener);
		setImageUrl(url);
	}

	public void setImageUrl(String url) {
		this.url = url;
		isLoading = true;
		request = new Request(url, context.getCacheDir(), threadRunnable);
		if (setImageLocalCache()) {
			return;
		}
		listener.onStart(url);
		Channel.getInstance().putRequest(request, Channel.Priority.HIGH);
	}

	public Boolean isLoading() {
		return isLoading;
	}

}