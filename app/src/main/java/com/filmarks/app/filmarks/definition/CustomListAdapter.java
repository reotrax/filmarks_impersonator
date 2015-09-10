package com.filmarks.app.filmarks.definition;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.filmarks.app.filmarks.MainActivity;
import com.filmarks.app.filmarks.R;
import com.filmarks.app.filmarks.fragments.C3_NowShowing;

import java.util.ArrayList;

/**
 * Created by user on 15/05/27.
 */
public class CustomListAdapter extends ArrayAdapter<CustomListAdapter.Item> implements AbsListView.OnScrollListener,
	MyScrollView.ScrollViewListener, AdapterView.OnItemClickListener {

	private AbsListView view;
	private int firstVisibleItem;
	private LayoutInflater mInflater;
	private InItem[] sLists;

	// スクロールリスナー
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		Log.v("test", "onScroll");
		this.view = view;
		this.firstVisibleItem = firstVisibleItem;
		// リストがスクロールしたらヘッダーの状態を更新
		performOverlayHeader(view, firstVisibleItem);
	}

	// マイスクロールビューリスナー
	@Override
	public void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy) {
		Log.v("test", "Adapter / onScrollChanged");
		performOverlayHeader(view, firstVisibleItem);
	}

	public static class Item {

		private InItem[] inItem;
		public Item(InItem[] inItem) {
			this.inItem = inItem;
		}
	}
	// コンストラクタ
	public CustomListAdapter(Context context, InItem[] sLists) {
		super(context, 0, new ArrayList<Item>());

		mInflater = LayoutInflater.from(context);
		this.sLists = sLists;

		for (int i=0; i<sLists.length; i++) {
			add(new Item(sLists));
		}
	}


	// オーバーレイヘッダー（＝スクロール中に上にとどまるヘッダー部分）のビュー
	private View mOverlayHeaderView;


	// オーバーレイヘッダーに表示するタイトル
	private String mCurrentTitleOfOverlayHeader;

	public void setOverlayHeaderView(View overlayHeaderView) {
		// オーバーレイヘッダー用のビューをセット
		this.mOverlayHeaderView = overlayHeaderView;
	}

	private View getOverlayHeader() {
		return mOverlayHeaderView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView==null) {
			convertView = mInflater.inflate(R.layout.row, null);
		}

		Item item = getItem(position);

		setHeaderTitle(convertView, item.inItem[position].getTitle());
		setListInList(convertView, sLists[position]);

		return convertView;
	}

	private void setHeaderTitle(View headerView, String title) {
		// ヘッダービューにタイトルをセット
		TextView header = (TextView) headerView.findViewById(R.id.title);
		header.setText(title);
	}

	private void setListInList(View view, InItem inItems) {
		// リストビューにアレイリストをセット
		CustomListExAdapter adapter_movie = new CustomListExAdapter(getContext(), inItems);
		ExpandableHeightListView ex_listView = (ExpandableHeightListView) view.findViewById(R.id.ex_listview1);
		ex_listView.setAdapter(adapter_movie);
		ex_listView.setExpanded(true);
		ex_listView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Log.v("test", "CustomAdapter, onItemClick, position = " + position);
		switch (view.getId()) {
			case R.id.ex_listview1:
				switch (position) {
					case 0:
						MainActivity activity = (MainActivity)getContext();
						activity.moveFragment(position);
						break;
					case 1:
						break;
					case 2:
						break;
					case 3:
						break;
					case 4:
						break;
					case 5:
						break;
					case 6:
						break;
					case 7:
						break;
				}
				break;
		}
	}

	public void performOverlayHeader(AbsListView view, int firstVisibleItem) {
		// リストの一番上にオーバーレイされるヘッダー
		View overlayHeader = getOverlayHeader();
		if (overlayHeader == null) {
			return;
		}

		// リストが空なら何もしない
		if (view.getChildCount() == 0) {
			return;
		}

		String title = getItem(firstVisibleItem).inItem.toString();

		// 現在表示されている一番上の行のView
		View row = view.getChildAt(0);
		// 行の中のヘッダー部分
		View header = row.findViewById(R.id.header);

		// 次の行の上部の位置＝次の行のヘッダーの上部の位置
		int nextRowPosition = row.getTop() + row.getHeight();

		if (nextRowPosition >= overlayHeader.getHeight()) {
			// 次のヘッダーがオーバーレイヘッダーよりも下にあるとき

			// オーバーレイヘッダーの位置を画面の一番上にする
			overlayHeader.setY(0);

			if (row.getTop() == 0) {
				// 行の上部が画面の上部と同じ場合はオーバーレイではなく
				// 行内のヘッダーを表示
				header.setVisibility(View.VISIBLE);
				overlayHeader.setVisibility(View.GONE);
			} else {
				// ヘッダーをオーバーレイ表示させたいので行内のヘッダーを非表示にして
				// オーバーレイヘッダーを表示
				header.setVisibility(View.INVISIBLE);
				overlayHeader.setVisibility(View.VISIBLE);
			}

			// オーバーレイヘッダーにタイトルをセット
			mCurrentTitleOfOverlayHeader = title;
			setHeaderTitle(overlayHeader, title);
			((View) overlayHeader.getParent()).postInvalidate();
			// ※ invalidate()でUI更新だが、別スレッドからの場合はpostInvalidate()でinvalidate()を呼ぶ形にするらしい

		} else {
			// 次のヘッダーでオーバーレイヘッダーが押し出されるとき

			// オーバーレイヘッダーを押し出される分だけ上にすらす
			float offset = nextRowPosition - overlayHeader.getHeight();
			overlayHeader.setY(offset);

			header.setVisibility(View.VISIBLE);
			overlayHeader.setVisibility(View.VISIBLE);

			if (mCurrentTitleOfOverlayHeader != title) {
				mCurrentTitleOfOverlayHeader = title;
				setHeaderTitle(overlayHeader, title);
				((View) overlayHeader.getParent()).postInvalidate();
			}
		}


		// 現在表示されている上から２番目の行のViewのヘッダーを表示
		setHeaderVisibility(1, view, View.VISIBLE);
		return;

	}


	private void setHeaderVisibility(int childPosition, AbsListView view, int visibility) {

		if (view.getChildCount() <= childPosition) {
			return;
		}

		View header = view.getChildAt(childPosition).findViewById(R.id.header);
		if (header != null) {
			header.setVisibility(visibility);
		}
	}
}
