package com.filmarks.app.filmarks.definition;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import com.filmarks.app.filmarks.R;

/**
 * Created by user on 15/06/23.
 * http://qiita.com/haratchatta/items/86aa8517a91fea1e772f
 */
public class MyScrollView extends ScrollView {

	// スクロールトゥボトム
	public interface ScrollToBottomListener {
		void onScrollToBottom(MyScrollView scrollView);
	}
	private ScrollToBottomListener scrollToBottomListener;
	private int scrollBottomMargin = 0;

	public void setScrollToBottomListener(ScrollToBottomListener listener) {
		this.scrollToBottomListener = listener;
	}
	public void setScrollBottomMargin(int value) {
		this.scrollBottomMargin = value;
	}


	public MyScrollView(Context context) {
		super(context);
	}
	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public MyScrollView(Context context, AttributeSet attrs, int defs) {
		super(context, attrs, defs);
	}


	// マイスクロール
	public interface ScrollViewListener {
		void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy);
	}
	private ScrollViewListener scrollViewListener = null;
	public void setScrollViewListener(ScrollViewListener viewListener) {
		this.scrollViewListener = viewListener;
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		View content = getChildAt(0);

//		switch () {
//			case R.id.:
				if (scrollToBottomListener == null) return;
				if (content == null) return;
				if (y + this.getHeight() >= content.getHeight() - scrollBottomMargin) {
					scrollToBottomListener.onScrollToBottom(this);
				}
//				break;
//			case R.id.:
				if (scrollViewListener != null) {
					scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
				}
//				break;
//		}
	}
}
