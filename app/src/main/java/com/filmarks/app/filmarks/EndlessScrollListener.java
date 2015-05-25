package com.filmarks.app.filmarks;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by user on 15/04/28.
 * （参考）http://qiita.com/konifar/items/e57c47bfb93d0d40ef59
 */
public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {
	// メンバー変数
	int firstVisibleItem, visibleItemCount, totalItemCount;
	private int previousTotal = 0;
	private boolean loading = true;
	private int current_page = 1;
	private LinearLayoutManager mLinearLayoutManager;

	// コンストラクタ
	public EndlessScrollListener(LinearLayoutManager linearLayoutManager) {
		this.mLinearLayoutManager = linearLayoutManager;
	}

	@Override
	public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
		super.onScrolled(recyclerView, dx, dy);

		visibleItemCount = recyclerView.getChildCount();
		totalItemCount = mLinearLayoutManager.getItemCount();
		firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

		if (loading) {
			if (totalItemCount > previousTotal) {
				loading = false;
				previousTotal = totalItemCount;
			}
		}

		if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleItemCount)) {
			current_page++;

			onLoadMore(current_page);

			loading = true;
		}
	}

	public abstract void onLoadMore(int current_page);
}
