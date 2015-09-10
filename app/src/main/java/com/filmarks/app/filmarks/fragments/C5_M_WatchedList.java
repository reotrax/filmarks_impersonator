package com.filmarks.app.filmarks.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.transition.Slide;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.filmarks.app.filmarks.R;
import com.filmarks.app.filmarks.definition.GridCard;
import com.filmarks.app.filmarks.urlimageview.UrlImageButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link C5_M_WatchedList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link C5_M_WatchedList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class C5_M_WatchedList extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	private ArrayList<GridCard> list = new ArrayList<>();
	private Bitmap[] bitmaps = new Bitmap[24];
	private GridCard[] gridList = new GridCard[24];
	private ArrayList<String> sortList = new ArrayList<String>(Arrays.asList(
			"投稿日時：新しい順", "投稿日時：古い順", "SCORE：高い順", "SCORE：低い順",
			"制作年：新しい順", "制作年：古い順", "映画タイトル：昇順", "映画タイトル：降順"));

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private OnFragmentInteractionListener mListener;
	private LinearLayout linearLayout;
	private LinearLayout cardLinear;
	private Spinner spinnerSort;
	private static int refreshNumber = 0;
	private int gridCount = 3;

	private enum Enum {GRID, LIST}
	private Enum vison = Enum.LIST;


	public static C5_M_WatchedList newInstance(int param1, int refreshNumber) {
		C5_M_WatchedList fragment = new C5_M_WatchedList();
		Bundle args = new Bundle();
		args.putInt(ARG_PARAM1, param1);
		args.putInt(ARG_PARAM2, refreshNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public C5_M_WatchedList() {
		/** Required empty public constructor */
	}

	// 自作インターフェース
	// 1,interface---2,引数---3,引数=(interface)activity---4,interface.method()で実行
	private FragmentListListener stateLisntener;
	public interface FragmentListListener {
		public void FragmentListListener(String layout, int refreshNumber);
	}

	// 自作インターフェース
	private StateBridgeListener bridgeListener;
	public interface StateBridgeListener {
		public void BridgedForList(String str, int number);
	}


	// スワイプリフレッシュに対応する自作リスナー
	public void setListener (StateBridgeListener bridgeListener) {
		this.bridgeListener = bridgeListener;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		imageBuilder();

		bridgeListener = (StateBridgeListener) activity;
		stateLisntener = (FragmentListListener) activity;

		// グリッド数用の引数を取得
		Bundle arg = getArguments();
		if (arg != null) {
			gridCount = arg.getInt(ARG_PARAM1);
			refreshNumber = arg.getInt(ARG_PARAM2);
		}
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.c5_mypage_w_list, container, false);

		ImageButton list_change_btn = (ImageButton)view.findViewById(R.id.c5_layoutBtn_list);
		list_change_btn.setOnClickListener(this);

		// ListView
//		ArrayList<GridCard> arrayList = new ArrayList<GridCard>();
//		for (int i=0; i<gridList.length; i++) {
//			GridCard gc = gridList[i];
//			arrayList.add(new GridCard(gc.getTitle(),gc.getBitmap(),gc.getCommentCount(),gc.getLikeCount(),gc.getRating(),gc.getDay()));
//		}

		//ソートの種類を切り替えるSpinner
		spinnerSort = (Spinner) view.findViewById(R.id.c5_spinner_list);
		ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, sortList);
		spinnerSort.setAdapter(adapter);
		spinnerSort.setSelection(refreshNumber);
		spinnerSort.setOnItemSelectedListener(this);

		cardLinear = (LinearLayout) view.findViewById(R.id.c5_list_ll);
		cardLinear.removeAllViews();
//		createCardView();

		return view;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		GridCard tempGrid;
		String item = (String) parent.getSelectedItem();
		switch (item){
			case "投稿日時：新しい順":
				refreshNumber = 0;
				break;
			case "投稿日時：古い順":
				refreshNumber = 1;
				break;
			case "SCORE：高い順":
				refreshNumber = 2;
				break;
			case "SCORE：低い順":
				refreshNumber = 3;
				break;
			case "制作年：新しい順":
				refreshNumber = 4;
				break;
			case "制作年：古い順":
				refreshNumber = 5;
				break;
			case "映画タイトル：昇順":
				refreshNumber = 6;
				break;
			case "映画タイトル：降順":
				refreshNumber = 7;
				break;
		}
		// 配列をアレイリストに移す
		ArrayList<GridCard> arrayList = new ArrayList<GridCard>();
		for (int i=0; i<gridList.length; i++) {
			GridCard gc = gridList[i];
//			arrayList.add(new GridCard(gc.getTitle(),gc.getBitmap(),gc.getCommentCount(),gc.getLikeCount(),gc.getRating(),gc.getDay()));
		}

		// カードビュー再構成
		createCardView();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	// カードビュー生成
	private void createCardView() {
		// カードビュー再構成
		cardLinear.removeAllViews();

		CardView cardView;
		for (int i=0; i<gridList.length; i++) {
			final int ii = i;

			LayoutInflater inflater1 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			linearLayout = (LinearLayout) inflater1.inflate(R.layout.test_card, null);

			cardView = (CardView) linearLayout.findViewById(R.id.cardView);
			UrlImageButton btnMovieJacket = (UrlImageButton) linearLayout.findViewById(R.id.btnUrlImage);
			int width = btnMovieJacket.getLayoutParams().width;
			int height = btnMovieJacket.getLayoutParams().height;

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
				}
			});

			ImageView user_icon = (ImageView) linearLayout.findViewById(R.id.userImage);

			// 画像取得の非同期処理
//			Bitmap bitmap = Bitmap.createScaledBitmap(gridList[i].getBitmap(), width, height, false);
//			btnMovieJacket.setImageBitmap(bitmap);
//			user_icon.setImageBitmap(gridList[i].getBitmap());

			TextView title = (TextView) linearLayout.findViewById(R.id.userName);
			RatingBar ratingBar = (RatingBar) linearLayout.findViewById(R.id.ratingBar);
			TextView rating = (TextView) linearLayout.findViewById(R.id.rating);
			TextView comment = (TextView) linearLayout.findViewById(R.id.userComment);

//			title.setText(gridList[i].getTitle());
//			ratingBar.setRating(gridList[i].getRating());
//			rating.setText(Float.toString(gridList[i].getRating()));

			cardView.setTag(i);

			// カードビューとそのインデックス？を追加
			cardLinear.addView(linearLayout, i);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.c5_layoutBtn_list:

				stateLisntener.FragmentListListener("LIST", refreshNumber);

//				bridgeListener.BridgedForList("GRID", refreshNumber);
//				Fragment fragment = new C5_M_WatchedGrid().newInstance(gridCount,null);
//				getFragmentManager().beginTransaction().replace(R.id.container2, fragment).commit();
				break;

		}
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		bridgeListener = null;
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

	// 映画タイトルで並び替えるための定義（昇順）
	class strCompareAscending implements Comparator {
		public int compare(Object obj1, Object obj2) {
			int answer = 0;
			String str1 = ((GridCard)obj1).getTitle();
			String str2 = ((GridCard)obj2).getTitle();
			String str3 = str1;
			// １、文字列の長さを比較（比較対象の長さが違っても比べるのは短い方の文字までだから）
			// ２、長さの短い方をループ用にする
			if (str1.length() > str2.length()) {
				str3 = str2;
			}
			// 短い文字列の長さ分だけループすれば良い
			for (int i = 0; i < str3.length(); i++) {
				// １、一文字づつに分けたcharをStringに変換して代入して、、、
				String str11 = Character.toString(str1.charAt(i));
				String str22 = Character.toString(str2.charAt(i));
				// ２、どちらが辞書的に大きいか差を数値にする
				answer = str11.compareToIgnoreCase(str22);
				// ３、同じじゃない場合はそこで順番が決められるのでループを抜ける
				if (answer != 0) {
					break;
				}
				//比較してが同じ場合は答えが決められないのでループを続ける
			}
			return answer;
		}
	}
	// 映画タイトルで並び替えるための定義（降順）
	class strCompareDescending implements Comparator {
		public int compare(Object obj1, Object obj2) {
			int answer = 0;
			String str1 = ((GridCard)obj1).getTitle();
			String str2 = ((GridCard)obj2).getTitle();
			String str3 = str1;
			if (str1.length() > str2.length()) {
				str3 = str2;
			}
			for (int i = 0; i < str3.length(); i++) {
				String str11 = Character.toString(str1.charAt(i));
				String str22 = Character.toString(str2.charAt(i));
				answer = str22.compareToIgnoreCase(str11);
				if (answer != 0) {
					break;
				}
			}
			return answer;
		}
	}

	public void imageBuilder() {
		// 画像をRGB_565に落としてから読み込んで軽くする
//		Resources res = getResources();
//		BitmapFactory.Options options = new BitmapFactory.Options();
//		options.inPreferredConfig = Bitmap.Config.RGB_565;
//		bitmaps[0] = BitmapFactory.decodeResource(res, R.drawable.movie_the_italian_job, options);
//		gridList[0] = new GridCard("ミニミニ大作戦", bitmaps[0], 0, 0, 4.0f, 23);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.v("test/C5_list", "- onDestroyView");

		// 画像をメモリから解放
		Log.v("test/C5", "recycle");
		for (int i=0; i<bitmaps.length; i++) {
			//if (bitmaps[i]!=null) {
//			bitmaps[i].recycle();
//			bitmaps[i] = null;
			//}
		}
	}

}
