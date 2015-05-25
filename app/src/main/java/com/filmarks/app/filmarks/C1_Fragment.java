package com.filmarks.app.filmarks;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class C1_Fragment extends Fragment implements View.OnClickListener {

	private ImageButton filmarks,btnUserSearch;
	private ImageView btnBack;
	private Bitmap backImage;
	private TextView fragmentTitle;

	private enum Enum {follower, heavyUser, facebook, twitter}
	private Enum enumType = Enum.follower;

	public static C1_Fragment newInstance(String param1, String param2) {
		C1_Fragment fragment = new C1_Fragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public C1_Fragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.c1_fragment, container, false);

		btnBack = (ImageView) view.findViewById(R.id.btnBack);
		filmarks = (ImageButton) view.findViewById(R.id.filmarks);
		btnUserSearch = (ImageButton) view.findViewById(R.id.btnUserSearch);
		fragmentTitle = (TextView) view.findViewById(R.id.fragment_title);
		filmarks.setOnClickListener(this);
		btnUserSearch.setOnClickListener(this);

		Fragment fragment = new C1_Fragment_Following();
		// 遷移アニメーション（ver.21ロリポップ以上の場合）
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			TransitionSet set = new TransitionSet();
			set.addTransition(new Slide(Gravity.LEFT));
			//fragment.setEnterTransition(set);
		}
		// フラグメントの初期画面を設定
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.add(R.id.c1_container, fragment, "following").commit();

		// 画像の準備
		Resources res = getResources();
		backImage = BitmapFactory.decodeResource(res, R.drawable.ic_chevron_left_black_48dp);

		return view;
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.btnUserSearch:
				// ヘビーユーザー検索画面に画面遷移
				if (enumType== Enum.follower) {
					Fragment fragment = new C1_Fragment_UserSearch();
					FragmentTransaction ft = getFragmentManager().beginTransaction();
					// 現在のフラグメントをバックスタックに保存してから次のフラグメントにリプレイスする
					ft.addToBackStack(null)
					.replace(R.id.c1_container, fragment).commit();
					// ユーザー検索ボタンを隠す
					btnUserSearch.setVisibility(View.INVISIBLE);
					// 前画面ボタンの画像とタイトルを表示させる
					btnBack.setImageBitmap(backImage);
					fragmentTitle.setText("ヘビーユーザー");
					// enumを変更
					enumType = Enum.heavyUser;
				}
				break;
			case R.id.filmarks:
				// 戻るボタン
				int backStackCnt = getFragmentManager().getBackStackEntryCount();
				if (backStackCnt != 0) {
					// BackStackに乗っているFragmentを戻す
					getFragmentManager().popBackStack();
					// ユーザー検索ボタンを表示させる
					btnUserSearch.setVisibility(View.VISIBLE);
					// 前画面ボタンの画像とタイトルを隠す
					btnBack.setImageBitmap(null);
					fragmentTitle.setText("");
					// enumを変更
					enumType = Enum.follower;
				}
				break;
		}
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
}
