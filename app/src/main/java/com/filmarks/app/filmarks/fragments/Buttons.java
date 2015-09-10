//package com.filmarks.app.filmarks.fragments;
//
//import android.app.Activity;
//import android.content.Context;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentTransaction;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
//
//import com.filmarks.app.filmarks.R;
//
///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link Buttons.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link Buttons#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class Buttons extends Fragment implements View.OnClickListener {
//	// TODO: Rename parameter arguments, choose names that match
//	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//	private static final String ARG_PARAM1 = "param1";
//	private static final String ARG_PARAM2 = "param2";
//
//	// TODO: Rename and change types of parameters
//	private String mParam1;
//	private String mParam2;
//
//	private String fragmentTag = "following";
//
//
//	private enum SELECT {FOLLOWING,TREND,SEARCH,NEWS,MYPAGE;};
//	private SELECT select = SELECT.FOLLOWING;
//	public static Buttons newInstance(String param1, String param2) {
//		Buttons fragment = new Buttons();
//		Bundle args = new Bundle();
//		args.putString(ARG_PARAM1, param1);
//		args.putString(ARG_PARAM2, param2);
//		fragment.setArguments(args);
//		return fragment;
//	}
//
//	public Buttons() {
//		// Required empty public constructor
//	}
//
//	private OnFragmentInteractionListener mListener;
//	public interface OnFragmentInteractionListener {
//		public void onFragmentInteraction(Uri uri);
//	}
//
//	private OnSwipeStateLintener swipeLintener;
//	public interface OnSwipeStateLintener {
//		public void SwipeOn  (String str);
//		public void SwipeOff (String str);
//
//	}
//
//	@Override
//	public void onAttach(Activity activity) {
//		super.onAttach(activity);
//
//		swipeLintener = (OnSwipeStateLintener) activity;
//
//	}
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		if (getArguments() != null) {
//			mParam1 = getArguments().getString(ARG_PARAM1);
//			mParam2 = getArguments().getString(ARG_PARAM2);
//		}
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//		View view = inflater.inflate(R.layout.f_buttons, container, false);
//
//		Button c1_btn = (Button)view.findViewById(R.id.c1_btn);
//		Button c2_btn = (Button)view.findViewById(R.id.c2_btn);
//		Button c3_btn = (Button)view.findViewById(R.id.c3_btn);
//		Button c4_btn = (Button)view.findViewById(R.id.c4_btn);
//		Button c5_btn = (Button)view.findViewById(R.id.c5_btn);
//		c1_btn.setOnClickListener(this);
//		c2_btn.setOnClickListener(this);
//		c3_btn.setOnClickListener(this);
//		c4_btn.setOnClickListener(this);
//		c5_btn.setOnClickListener(this);
//
//		return view;
//	}
//
//	// 画面下部のボタン
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()){
//			case R.id.c1_btn:
//				if (select!=SELECT.FOLLOWING) {
//
//					// Activity 側の SwipeRefreshListener をオン
//					String tag = "following";
//					swipeLintener.SwipeOn(tag);
//
//
//					Fragment fragment0 = new C1_F();
//					getFragmentManager().beginTransaction().replace(R.id.container0, fragment0).commit();
//
//					Fragment fragment1 = new C1_F_Following();
//					getFragmentManager().beginTransaction().replace(R.id.container1, fragment1).commit();
//
//					Fragment fragment2 = new Empty_Fragment();
//					getFragmentManager().beginTransaction().replace(R.id.container2, fragment2).commit();
//
//					select = SELECT.FOLLOWING;
//					fragmentTag = tag;
//
//				}
//				break;
//			case R.id.c2_btn:
//				if (select!=SELECT.TREND) {
//
//					String tag = "following";
//					swipeLintener.SwipeOn(tag);
//
//					Fragment fragment0 = new C2_T();
//					getFragmentManager().beginTransaction().replace(R.id.container0, fragment0).commit();
//
//					Fragment fragment1 = new Fragment();
//					getFragmentManager().beginTransaction().replace(R.id.container1, fragment1).commit();
//
//					Fragment fragment2 = new Empty_Fragment();
//					getFragmentManager().beginTransaction().replace(R.id.container2, fragment2).commit();
//
//					select = SELECT.TREND;
//
//				}
//				break;
//			case R.id.c3_btn:
//				if (select!=SELECT.SEARCH) {
//
//					// Activity 側の SwipeRefreshListener をオフ
//					String tag = "SEARCH";
//					swipeLintener.SwipeOff(tag);
//
//					Fragment fragment0 = new C3_S();
//					getFragmentManager().beginTransaction().replace(R.id.container0, fragment0).commit();
//
//					Fragment fragment1 = new C3_S_List();
//					getFragmentManager().beginTransaction().replace(R.id.container1, fragment1).commit();
//
//					Fragment fragment2 = new Empty_Fragment();
//					getFragmentManager().beginTransaction().replace(R.id.container2, fragment2).commit();
//
//					select = SELECT.SEARCH;
//
//				}
//				break;
//			case R.id.c4_btn:
//				if (select!=SELECT.NEWS) {
//
//					String tag = "NEWS";
//					swipeLintener.SwipeOn(tag);
//
//					Fragment fragment = new C4_N();
//					getFragmentManager().beginTransaction().replace(R.id.container0, fragment).commit();
//
//					Fragment fragment1 = new Empty_Fragment();
//					getFragmentManager().beginTransaction().replace(R.id.container1, fragment1).commit();
//
//					Fragment fragment2 = new Empty_Fragment();
//					getFragmentManager().beginTransaction().replace(R.id.container2, fragment2).commit();
//
//					select = SELECT.NEWS;
//
//				}
//				break;
//			case R.id.c5_btn:
//				if (select!=SELECT.MYPAGE) {
//
//					String tag = "MYPAGE";
//					swipeLintener.SwipeOn(tag);
//
//					Fragment fragment0 = new C5_M();
//					getFragmentManager().beginTransaction().replace(R.id.container0, fragment0).commit();
//
//					Fragment fragment = new C5_M_Watched_2();
//					getFragmentManager().beginTransaction().replace(R.id.container1, fragment).commit();
//
//					Fragment fragment2 = new C5_M_WatchedGrid();
//					getFragmentManager().beginTransaction().add(R.id.container2, fragment2).commit();
//
//					select = SELECT.MYPAGE;
//
//				}
//				break;
//		}
//		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//		imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//	}
//
//	public void onButtonPressed(Uri uri) {
//		if (mListener != null) {
//			mListener.onFragmentInteraction(uri);
//		}
//	}
//
//	@Override
//	public void onDetach() {
//		super.onDetach();
//		mListener = null;
//		swipeLintener = null;
//	}
//
//}
