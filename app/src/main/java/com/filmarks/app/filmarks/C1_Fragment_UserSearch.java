package com.filmarks.app.filmarks;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class C1_Fragment_UserSearch extends Fragment implements View.OnClickListener {

	private LinearLayout cardLinear;

	public static C1_Fragment_UserSearch newInstance(String param1, String param2) {
		C1_Fragment_UserSearch fragment = new C1_Fragment_UserSearch();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public C1_Fragment_UserSearch() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.c1_user_search, container, false);

		ImageButton btnBack = (ImageButton) view.findViewById(R.id.filmarks);
		cardLinear = (LinearLayout) view.findViewById(R.id.cardLinear);
		CardView cardView = (CardView) view.findViewById(R.id.cardView);
		cardLinear.removeAllViews();

		for (int i=0; i<5; i++) {
			final int ii = i;
			LayoutInflater inflater1 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout linearLayout = (LinearLayout) inflater1.inflate(R.layout.test_card_user, null);
			cardView = (android.support.v7.widget.CardView) linearLayout.findViewById(R.id.cardView);
			ImageButton btnFollow = (ImageButton) linearLayout.findViewById(R.id.btnFollow);
			btnFollow.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(getActivity(), "onClick / " + ii, Toast.LENGTH_SHORT).show();
				}
			});
			ImageView imageView = (ImageView) linearLayout.findViewById(R.id.userImage);
			TextView title = (TextView) linearLayout.findViewById(R.id.userName);
			TextView genre = (TextView) linearLayout.findViewById(R.id.userComment);
			cardView.setTag(i);
//			cardView.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//				}
//			});
			// カードビューとそのインデックス？を追加
			cardLinear.addView(linearLayout, i);
		}

		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.filmarks:

				break;
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}


}
