package com.filmarks.app.filmarks.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.filmarks.app.filmarks.R;

public class C3_NowShowing extends Fragment implements View.OnClickListener {

	private static final String BACK_STACK = "backStack";
	private static final String ARG_PARAM2 = "param2";
	private String backStackKeyword;
	private String mParam2;

	// TODO: Rename and change types and number of parameters
	public static C3_NowShowing newInstance(String backStackKeyword, String param2) {
		C3_NowShowing fragment = new C3_NowShowing();
		Bundle args = new Bundle();
		args.putString(BACK_STACK, backStackKeyword);
		fragment.setArguments(args);
		return fragment;
	}

	public C3_NowShowing() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			backStackKeyword = getArguments().getString(BACK_STACK);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.c3_now_showing, container, false);

		TextView backBtn = (TextView)view.findViewById(R.id.backBtn);
		ImageView viewChangeBtn = (ImageView)view.findViewById(R.id.viewChangeBtn);
		backBtn.setOnClickListener(this);
		viewChangeBtn.setOnClickListener(this);

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.backBtn:
				getFragmentManager().popBackStack();//backStackKeyword,0);
				break;

			case R.id.viewChangeBtn:
				break;
		}
	}


	@Override
	public void onDetach() {
		super.onDetach();
	}
}
