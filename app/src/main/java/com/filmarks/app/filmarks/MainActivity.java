package com.filmarks.app.filmarks;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;

import com.filmarks.app.filmarks.activity.Review_Activity;
import com.filmarks.app.filmarks.definition.DetectableKeyboardEventLayout;
import com.filmarks.app.filmarks.definition.MyScrollView;
import com.filmarks.app.filmarks.fragments.C1_F;
import com.filmarks.app.filmarks.fragments.C1_F_Following;
import com.filmarks.app.filmarks.fragments.C2_T;
import com.filmarks.app.filmarks.fragments.C3_NowShowing;
import com.filmarks.app.filmarks.fragments.C3_S;
import com.filmarks.app.filmarks.fragments.C4_N;
import com.filmarks.app.filmarks.fragments.C5_M;
import com.filmarks.app.filmarks.fragments.C5_M_WatchedGrid;
import com.filmarks.app.filmarks.fragments.C5_M_WatchedList;
import com.filmarks.app.filmarks.fragments.Empty_Fragment;


public class MainActivity extends AppCompatActivity
		implements C5_M_WatchedGrid.StateBridgeListener, C5_M_WatchedGrid.FragmentGridListener, C5_M_WatchedList.FragmentListListener,
		SwipeRefreshLayout.OnRefreshListener, C5_M_WatchedList.StateBridgeListener, DetectableKeyboardEventLayout.KeyboardListener,
		View.OnClickListener, MyScrollView.ScrollToBottomListener {

	private String fragmentTag = "following";
	private int refreshNumber;
	private int gridCount = 3;
	public SwipeRefreshLayout swipeRefreshLayout;
	private Boolean nowLoading = false;
	private FrameLayout container1,container2;
	private Button c1btn,c2btn,c3btn,c4btn,c5btn;
	private String accountName = "filmarks";

	@Override
	public void onScrollToBottom(MyScrollView scrollView) {
		Log.v("test", "onScrollToBottom");
	}

	@Override
	public void FragmentGridListener(String layout, int refreshNumber) {
		Fragment fragment = new C5_M_WatchedList().newInstance(0, refreshNumber);
		getSupportFragmentManager().beginTransaction().replace(R.id.container1, fragment).commit();
	}

	@Override
	public void FragmentListListener(String layout, int refreshNumber) {
		Fragment fragment = new C5_M_WatchedGrid().newInstance(gridCount, refreshNumber);
		getSupportFragmentManager().beginTransaction().replace(R.id.container1, fragment).commit();
	}


	// マイページ、ステイトブリッジリスナー
	private enum SELECT {FOLLOWING,TREND,SEARCH,NEWS,MYPAGE;};
	private SELECT select = SELECT.FOLLOWING;
	private enum Enum {GRID, LIST;}
	private Enum vison = Enum.GRID;
	@Override
	public void BridgedForGrid(String str, int number) {
		vison = Enum.LIST;
		refreshNumber = number;
	}
	@Override
	public void BridgedForList(String str, int number) {
		vison = Enum.GRID;
		refreshNumber = number;
	}

	public void moveFragment(int position) {
		Log.v("test", "moveFragment = " + position);
		String stackWord = "C3_1";
		Fragment fragment = new C3_NowShowing().newInstance(stackWord, null);
		getSupportFragmentManager().beginTransaction().addToBackStack(stackWord).replace(R.id.container1,fragment).commit();
	}

	// ソフトウェアキーボードの状態変化で呼ばれる - DetectableKeyboardEventLayout
	@Override
	public void onKeyboardShown() {
		findViewById(R.id.btn_layout).setVisibility(View.GONE);
	}
	@Override
	public void onKeyboardHidden() {
		findViewById(R.id.btn_layout).setVisibility(View.VISIBLE);
	}

	// レビュー画面を呼ぶ
	public void callReviewActivity(String title_no, String title_img, String title, String user_no, String user_img) {
		Intent intent = new Intent(this, Review_Activity.class);
		intent.putExtra("user_name",accountName	);
		intent.putExtra("user_img",	user_img);
		intent.putExtra("title",	title	);
		intent.putExtra("title_no",	title_no);
		intent.putExtra("title_img",title_img);
		intent.putExtra("user_no",	user_no	);
		startActivity(intent);
	}

	//	// ボタンズ、スワイプステイトリスナー
//	@Override
//	public void SwipeOn(String page) {
//		swipeRefreshLayout.setEnabled(true);
//
//		switch (page) {
//			case "FOLLOWING":
//				select = SELECT.FOLLOWING;
//				break;
//			case "TREND":
//				select = SELECT.TREND;
//				break;
//			case "NEWS":
//				select = SELECT.NEWS;
//				break;
//			case "MYPAGE":
//				select = SELECT.MYPAGE;
//				break;
//		}
//	}
//	@Override
//	public void SwipeOff(String page) {
//		swipeRefreshLayout.setEnabled(false);
//
//		switch (page) {
//			case "SEARCH":
//				select = SELECT.SEARCH;
//				break;
//		}
//	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 画面サイズ取得 > グリッド数の判断材料に
		WindowManager wm = getWindowManager();
		Display display = wm.getDefaultDisplay();
		Point point = new Point();
		display.getSize(point);
		int width = point.x;
		Log.v("test", "width = " + width);
		if (width >= 850 && width < 1100) {
			gridCount = 4;
		} else if (width >= 1100) {
			gridCount = 5;
		}

		// カスタマイズしたスクロールビュー
//		MyScrollView scrollView = (MyScrollView) findViewById(R.id.scrollView);
//		scrollView.setScrollBottomMargin();
//		scrollView.setScrollToBottomListener(this);
//		scrollView.setEnabled(false);
		// スワイプリフレッシュ
//		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipelayout);
//		swipeRefreshLayout.setColorSchemeResources(R.color.yellow, R.color.yellow, R.color.yellow, R.color.yellow); //４色の設定
//		swipeRefreshLayout.setOnRefreshListener(this);
//		swipeRefreshLayout.setEnabled(false);

		DetectableKeyboardEventLayout root = (DetectableKeyboardEventLayout)findViewById(R.id.keyboardEvent);
		root.setKeyboardListener(this);

		container1 = (FrameLayout)findViewById(R.id.container1);

		// フラグメントの初期画面を設定
		C1_F c1_f = new C1_F();
		getSupportFragmentManager().beginTransaction().add(R.id.container0, c1_f).commit();
		C1_F_Following c1_following = new C1_F_Following();
		getSupportFragmentManager().beginTransaction().add(R.id.container1, c1_following).commit();

		// 画面遷移ボタン
		//Button actvt_btn = (Button)findViewById(R.id.activity_btn);
		//actvt_btn.setOnClickListener(this);
		c1btn = (Button)findViewById(R.id.c1btn);
		c2btn = (Button)findViewById(R.id.c2btn);
		c3btn = (Button)findViewById(R.id.c3btn);
		c4btn = (Button)findViewById(R.id.c4btn);
		c5btn = (Button)findViewById(R.id.c5btn);
		c1btn.setOnClickListener(this);
		c2btn.setOnClickListener(this);
		c3btn.setOnClickListener(this);
		c4btn.setOnClickListener(this);
		c5btn.setOnClickListener(this);
		c1btn.setTextColor(Color.rgb(254, 225, 1)); // #FEE101
	}

	// スワイプリフレッシュ - 画面スライドでリスト更新
	@Override
	public void onRefresh() {
		nowLoading = true;
		new Handler().post(new Runnable() {
			@Override
			public void run() {
				switch (select) {
					case FOLLOWING:
						Fragment fragment1 = new C1_F_Following();
						getSupportFragmentManager().beginTransaction().replace(R.id.container1, fragment1).commit();
						break;
					case TREND:
						break;
					case SEARCH:
						// Activity からのスワイプはオフ
						break;
					case NEWS:
						break;
					case MYPAGE:
						switch (vison) {
							case GRID:
								Fragment fragment_g = new C5_M_WatchedGrid().newInstance(gridCount,0);
								getSupportFragmentManager().beginTransaction().replace(R.id.container1, fragment_g).commit();
								break;
							case LIST:
								Fragment fragment_l = new C5_M_WatchedList();
								getSupportFragmentManager().beginTransaction().replace(R.id.container1, fragment_l).commit();
								break;
						}
						break;
				}
				// 更新が終了したらインジケーターを非表示に
				swipeRefreshLayout.setRefreshing(false);
			}
		});
	}

	// 画面下部のボタン
	@Override
	public void onClick(View v) {
		if (!nowLoading) {
			switch (v.getId()) {
				case R.id.c1btn:
					if (select != SELECT.FOLLOWING) {

						Fragment fragment0 = new C1_F();
						Fragment fragment1 = new C1_F_Following();
						getSupportFragmentManager().beginTransaction().replace(R.id.container0, fragment0).commit();
						getSupportFragmentManager().beginTransaction().replace(R.id.container1, fragment1).commit();
						container1.setVisibility(View.VISIBLE);
						c1btn.setTextColor(Color.rgb(254, 225, 1)); // #FEE101
						c2btn.setTextColor(Color.rgb(255, 255, 255));
						c3btn.setTextColor(Color.rgb(255, 255, 255));
						c4btn.setTextColor(Color.rgb(255, 255, 255));
						c5btn.setTextColor(Color.rgb(254, 225, 255));
						select = SELECT.FOLLOWING;
					}
					break;
				case R.id.c2btn:
					if (select != SELECT.TREND) {

						Fragment fragment0 = new C2_T();
						Fragment fragment1 = new Fragment();
						getSupportFragmentManager().beginTransaction().replace(R.id.container0, fragment0).commit();
						getSupportFragmentManager().beginTransaction().replace(R.id.container1, fragment1).commit();
						container1.setVisibility(View.VISIBLE);
						c1btn.setTextColor(Color.rgb(255, 255, 255));
						c2btn.setTextColor(Color.rgb(254, 225, 1)); // #FEE101
						c3btn.setTextColor(Color.rgb(255, 255, 255));
						c4btn.setTextColor(Color.rgb(255, 255, 255));
						c5btn.setTextColor(Color.rgb(254, 225, 255));
						select = SELECT.TREND;
					}
					break;
				case R.id.c3btn:
					if (select != SELECT.SEARCH) {

						Fragment fragment0 = new Empty_Fragment();
						Fragment fragment1 = new C3_S(); //C3_S_List();
						getSupportFragmentManager().beginTransaction().replace(R.id.container0, fragment0).commit();
						getSupportFragmentManager().beginTransaction().replace(R.id.container1, fragment1).commit();
						container1.setVisibility(View.VISIBLE);
						c1btn.setTextColor(Color.rgb(255, 255, 255));
						c2btn.setTextColor(Color.rgb(255, 255, 255));
						c3btn.setTextColor(Color.rgb(254, 225, 1)); // #FEE101
						c4btn.setTextColor(Color.rgb(255, 255, 255));
						c5btn.setTextColor(Color.rgb(254, 225, 255));
						select = SELECT.SEARCH;
					}
					break;
				case R.id.c4btn:
					if (select != SELECT.NEWS) {

						Fragment fragment = new C4_N();
						Fragment fragment1 = new Empty_Fragment();
						getSupportFragmentManager().beginTransaction().replace(R.id.container0, fragment).commit();
						getSupportFragmentManager().beginTransaction().replace(R.id.container1, fragment1).commit();
						container1.setVisibility(View.VISIBLE);
						c1btn.setTextColor(Color.rgb(255, 255, 255));
						c2btn.setTextColor(Color.rgb(255, 255, 255));
						c3btn.setTextColor(Color.rgb(255, 255, 255));
						c4btn.setTextColor(Color.rgb(254, 225, 1)); // #FEE101
						c5btn.setTextColor(Color.rgb(255, 255, 255));
						select = SELECT.NEWS;
					}
					break;
				case R.id.c5btn:
					if (select != SELECT.MYPAGE) {

						Fragment fragment0 = new C5_M();
						Fragment fragment1 = new C5_M_WatchedGrid().newInstance(gridCount,0);
						getSupportFragmentManager().beginTransaction().replace(R.id.container0, fragment0).commit();
						getSupportFragmentManager().beginTransaction().replace(R.id.container1, fragment1).commit();
						container1.setVisibility(View.VISIBLE);
						c1btn.setTextColor(Color.rgb(255, 255, 255));
						c2btn.setTextColor(Color.rgb(255,255,255));
						c3btn.setTextColor(Color.rgb(255,255,255));
						c4btn.setTextColor(Color.rgb(255,255,255));
						c5btn.setTextColor(Color.rgb(254,225,1)); // #FEE101
						select = SELECT.MYPAGE;
					}
					break;
			}
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
		nowLoading = false;
	}


	// 戻るボタンで、バックスタックのフラグメントを呼ぶ
	@Override
	public void onBackPressed() {
		int backStackCnt = getFragmentManager().getBackStackEntryCount();
		if (backStackCnt != 0) {
			switch (select) {
				case FOLLOWING:
					getFragmentManager().popBackStack(); // BackStackに乗っているFragmentを戻す
					break;
				case TREND:
					break;
				case SEARCH:
					break;
				case NEWS:
					break;
				case MYPAGE:
					break;
			}
		} else {
			super.onBackPressed(); // これをするとActivity終了
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
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
