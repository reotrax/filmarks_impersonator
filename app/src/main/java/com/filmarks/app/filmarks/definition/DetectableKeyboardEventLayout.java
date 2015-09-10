package com.filmarks.app.filmarks.definition;

/**
 * Created by user on 15/07/23.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/*
* 参考
* http://setohide.blogspot.jp/2012/08/android-imeonoff.html
* http://d.hatena.ne.jp/glass-_-onion/20110813/1313166206
*/
public class DetectableKeyboardEventLayout extends LinearLayout {
	public interface KeyboardListener {
		void onKeyboardShown();
		void onKeyboardHidden();
	}
	private static final int MIN_KEYBOARD_HEIGHT = 100;
	private KeyboardListener keyboardListener;
	public DetectableKeyboardEventLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public DetectableKeyboardEventLayout(Context context) {
		super(context);
	}
	public void setKeyboardListener(KeyboardListener keyboardListener) {
		this.keyboardListener = keyboardListener;
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (keyboardListener == null) {
			return;
		}
		int proposedheight = MeasureSpec.getSize(heightMeasureSpec);
		int actualHeight = getHeight();
		final int difference = actualHeight - proposedheight;
		if (difference == 0) {
			return;
		}
		// UI スレッド以外からも onMeasure メソッドが呼ばれるので Handler を使う
		getHandler().post(new Runnable() {
			@Override
			public void run() {
				if (difference > MIN_KEYBOARD_HEIGHT){
					keyboardListener.onKeyboardShown();
				} else if (difference < -MIN_KEYBOARD_HEIGHT) {
					keyboardListener.onKeyboardHidden();
				}
			}
		});
	}
}