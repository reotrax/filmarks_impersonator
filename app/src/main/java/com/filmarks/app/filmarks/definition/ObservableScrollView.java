package com.filmarks.app.filmarks.definition;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

/**
 * Created by user on 15/06/02.
 */
public class ObservableScrollView extends ScrollView {

	public ObservableScrollView(Context context) {
		super(context);
	}
	public ObservableScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public ObservableScrollView(Context context, AttributeSet attrs, int defs) {
		super(context, attrs, defs);
	}


	public interface HorizontalScrollViewListener {
		void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);
	}

	private HorizontalScrollViewListener scrollViewListener = null;

	public void setOnScrollViewListener(HorizontalScrollViewListener scrollViewListener) {
		this.scrollViewListener = scrollViewListener;
	}


	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		if (scrollViewListener != null) {
			scrollViewListener.onScrollChanged(this, x, y, oldx, oldx);
		}
	}
}

