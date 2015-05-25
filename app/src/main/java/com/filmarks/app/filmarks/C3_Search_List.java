package com.filmarks.app.filmarks;

import android.app.Activity;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link C3_Search_List.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link C3_Search_List#newInstance} factory method to
 * create an instance of this fragment.
 */
public class C3_Search_List extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private OnFragmentInteractionListener mListener;


	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment C3_Search_List.
	 */
	// TODO: Rename and change types and number of parameters
	public static C3_Search_List newInstance(String param1, String param2) {
		C3_Search_List fragment = new C3_Search_List();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public C3_Search_List() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.c3_search_list, container, false);

		ExpandableHeightListView noScroll_ListVew1 = (ExpandableHeightListView) view.findViewById(R.id.noScroll_listview_1);
		ExpandableHeightListView noScroll_ListVew2 = (ExpandableHeightListView) view.findViewById(R.id.noScroll_listview_2);

		// 映画を探す
		ArrayList<String> list_movie = new ArrayList<>();
		list_movie.add("上映中の映画");
		list_movie.add("上映予定の映画");
		list_movie.add("映画賞");
		list_movie.add("制作年");
		list_movie.add("制作国");
		list_movie.add("ジャンル");
		list_movie.add("#タグ");

		ArrayAdapter<String> adapter_movie = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list_movie);

		noScroll_ListVew1.setAdapter(adapter_movie);
		noScroll_ListVew1.setExpanded(true);

		// ユーザーを探す
		ArrayList<String> list_user = new ArrayList<>();
		list_user.add("Facebook");
		list_user.add("Twitter");
		list_user.add("人気ユーザー");
		list_user.add("ID、ニックネーム");
		list_user.add("Facebook");
		list_user.add("Twitter");
		list_user.add("人気ユーザー");
		list_user.add("ID、ニックネーム");
		list_user.add("Facebook");
		list_user.add("Twitter");
		list_user.add("人気ユーザー");
		list_user.add("ID、ニックネーム");

		ArrayAdapter<String> adapter_user = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list_user);
		noScroll_ListVew2.setAdapter(adapter_user);
		noScroll_ListVew2.setExpanded(true);

		return view;
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 * <p/>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onFragmentInteraction(Uri uri);
	}

}
