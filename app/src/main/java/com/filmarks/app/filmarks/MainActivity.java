package com.filmarks.app.filmarks;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.filmarks.app.filmarks.fragments.C1_M;
import com.filmarks.app.filmarks.fragments.C2_M;
import com.filmarks.app.filmarks.fragments.C3_M;
import com.filmarks.app.filmarks.fragments.C4_M;
import com.filmarks.app.filmarks.fragments.C5_M;
import com.filmarks.app.filmarks.fragments.C5_M_Watched_2;


public class MainActivity extends AppCompatActivity
							implements View.OnClickListener {

	private FrameLayout container;
	private Button btnFollowing,btnTrend,btnSearch,btnNews,btnMypage;
	private String fragmentTag = "following";

	private enum SELECT {FOLLOWING,TREND,SEARCH,NEWS,MYPAGE};
	private SELECT select = SELECT.FOLLOWING;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// アクションバーを隠す
		android.support.v7.app.ActionBar actionBar = getSupportActionBar();
		actionBar.hide();

		container = (FrameLayout) findViewById(R.id.container);
		btnFollowing = (Button) findViewById(R.id.btnFollowing);
		btnTrend = (Button) findViewById(R.id.btnTrend);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		btnNews = (Button) findViewById(R.id.btnNews);
		btnMypage = (Button) findViewById(R.id.btnMyPage);
		btnFollowing.setOnClickListener(this);
		btnTrend.setOnClickListener(this);
		btnSearch.setOnClickListener(this);
		btnNews.setOnClickListener(this);
		btnMypage.setOnClickListener(this);

		// フラグメントの初期画面を設定
		C1_M c1_m = new C1_M();
		FragmentManager fm = getSupportFragmentManager();
		fm.beginTransaction().add(R.id.container, c1_m).commit();

	}

	// 戻るボタンで、バックスタックのフラグメントを呼ぶ
	@Override
	public void onBackPressed() {
		int backStackCnt = getFragmentManager().getBackStackEntryCount();
		if (backStackCnt != 0) {
			getFragmentManager().popBackStack(); // BackStackに乗っているFragmentを戻す
		} else {
			super.onBackPressed(); // これをするとActivity終了
		}
	}

	// 画面下部のボタン
	@Override
	public void onClick(View v) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		switch (v.getId()){
			case R.id.btnFollowing:
				if (select!=SELECT.FOLLOWING) {
					Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(fragmentTag);
					String tag = "following";
					Fragment fragment = new C1_M();
					ft.remove(currentFragment).replace(R.id.container, fragment, tag).commit();
					select = SELECT.FOLLOWING;
					fragmentTag = tag;
				}
				break;
			case R.id.btnTrend:
				if (select!=SELECT.TREND) {
					Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(fragmentTag);
					String tag = "trend";
					Fragment fragment = new C2_M();
					ft.replace(R.id.container, fragment, tag).commit();
					select = SELECT.TREND;
					fragmentTag = tag;
				}
				break;
			case R.id.btnSearch:
				if (select!=SELECT.SEARCH) {
					Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(fragmentTag);
					String tag = "search";
					Fragment fragment = new C3_M();
					ft.replace(R.id.container, fragment, tag).commit();
					select = SELECT.SEARCH;
					fragmentTag = tag;
				}
				break;
			case R.id.btnNews:
				if (select!=SELECT.NEWS) {
					Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(fragmentTag);
					Fragment fragment = new C4_M();
					String tag = "news";
					ft.replace(R.id.container, fragment, tag).commit();
					select = SELECT.NEWS;
					fragmentTag = tag;
				}
				break;
			case R.id.btnMyPage:
				if (select!=SELECT.MYPAGE) {

					Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(fragmentTag);
					Fragment fragment = new C5_M();
					String tag = "mypage";
					ft.replace(R.id.container, fragment, tag).commit();

					select = SELECT.MYPAGE;
					fragmentTag = tag;

				}
				break;
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

}
