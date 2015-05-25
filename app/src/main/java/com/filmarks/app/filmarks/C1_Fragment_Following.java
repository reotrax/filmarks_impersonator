package com.filmarks.app.filmarks;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.transition.Slide;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class C1_Fragment_Following extends Fragment {
	private CardList[] titleList;
	private ImageButton btnUserSearch;
	private LinearLayout cardLinear;
	private LinearLayout linearLayout;

	public static C1_Fragment_Following newInstance(String param1, String param2) {
		C1_Fragment_Following fragment = new C1_Fragment_Following();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public C1_Fragment_Following() {
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.v("test/Following", "onAttack");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("test/Following", "onCreate");
		if (getArguments() != null) {
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.v("test/Following", "onCreateView");

		View view = getActivity().getLayoutInflater().inflate(R.layout.c1_following, container, false);
		cardLinear = (LinearLayout) view.findViewById(R.id.cardLinear);
		CardView cardView = (CardView) view.findViewById(R.id.cardView);
		cardLinear.removeAllViews();
		// カードビュー生成
		createCardView();

		return view;
	}

	// カードビュー生成
	private void createCardView() {
		CardView cardView;
		for (int i=0; i<15; i++) {
			final int ii = i;
			LayoutInflater inflater1 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			linearLayout = (LinearLayout) inflater1.inflate(R.layout.test_card, null);
			cardView = (CardView) linearLayout.findViewById(R.id.cardView);
			ImageButton btnMovieJacket = (ImageButton) linearLayout.findViewById(R.id.btnMovieJaket);
			btnMovieJacket.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(getActivity(), "onClick / " + ii, Toast.LENGTH_SHORT).show();
					//
					Fragment fragment = new MovieInfo();
					// 遷移アニメーション（ver.21ロリポップ以上の場合）
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
						TransitionSet set = new TransitionSet();
						set.addTransition(new Slide(Gravity.RIGHT));
						fragment.setEnterTransition(set);
					}
					// 画面遷移
					FragmentTransaction ft = getFragmentManager().beginTransaction();
					ft.addToBackStack(null);
					ft.replace(R.id.c1_container, fragment).commit();
				}
			});
			ImageView imageView = (ImageView) linearLayout.findViewById(R.id.userImage);
			TextView title = (TextView) linearLayout.findViewById(R.id.userName);
			TextView genre = (TextView) linearLayout.findViewById(R.id.userComment);
//			imageView.setImageBitmap(titleList[i].getBitmap());
//			title.setText(titleList[i].getTitle());
//			genre.setText(titleList[i].getGenre());
//			comment.setText(titleList[i].getComment());
			cardView.setTag(i);
//			cardView.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//				}
//			});
			// カードビューとそのインデックス？を追加
			cardLinear.addView(linearLayout, i);
		}
	}


}
