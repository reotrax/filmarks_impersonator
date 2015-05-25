package com.filmarks.app.filmarks;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class C5_MyPage extends Fragment {

	public static C5_MyPage newInstance(String param1, String param2) {
		C5_MyPage fragment = new C5_MyPage();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public C5_MyPage() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.c5_mypage, container, false);

		// 最初のフラグメントを設定
		C5_Mypage_Watched mypage_watched = new C5_Mypage_Watched();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.add(R.id.c5_container, mypage_watched).commit();

		return view;
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

}
