package com.filmarks.app.filmarks.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import com.filmarks.app.filmarks.MovieInfo;
import com.filmarks.app.filmarks.R;
import com.filmarks.app.filmarks.definition.GridCard;

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

	private enum Enum {GRID, LIST}
	private Enum vison = Enum.LIST;


	public static C5_M_WatchedList newInstance(String param1, String param2) {
		C5_M_WatchedList fragment = new C5_M_WatchedList();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public C5_M_WatchedList() {
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.c5_mypage_w_list, container, false);

		ImageButton list_change_btn = (ImageButton)view.findViewById(R.id.c5_layoutBtn_list);
		list_change_btn.setOnClickListener(this);

		//ソートの種類を切り替えるSpinner
		spinnerSort = (Spinner) view.findViewById(R.id.c5_spinner_list);
		ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, sortList);
		spinnerSort.setAdapter(adapter);
		spinnerSort.setOnItemSelectedListener(this);

		ArrayList<GridCard> arrayList = new ArrayList<GridCard>();
		for (int i=0; i<gridList.length; i++) {
			GridCard gc = gridList[i];
			arrayList.add(new GridCard(gc.getTitle(),gc.getBitmap(),gc.getCommentCount(),gc.getLikeCount(),gc.getRating(),gc.getDay()));
		}

		cardLinear = (LinearLayout) view.findViewById(R.id.c5_list_ll);
		cardLinear.removeAllViews();
		createCardView();

		return view;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		GridCard tempGrid;
		String item = (String) parent.getSelectedItem();
		switch (item){
			case "投稿日時：新しい順":
				for ( int i = 0; i < gridList.length - 1; i++ ) {
					for ( int j = gridList.length - 1; j > i; j-- ) {
						if ( gridList[j - 1].getDay() < gridList[j].getDay() ) {
							// 評価
							tempGrid = gridList[j - 1];
							gridList[j - 1] = gridList[j];
							gridList[j] = tempGrid;
						}
					}
				}
				break;
			case "投稿日時：古い順":
				for ( int i = 0; i < gridList.length - 1; i++ ) {
					for ( int j = gridList.length - 1; j > i; j-- ) {
						if ( gridList[j - 1].getDay() > gridList[j].getDay() ) {
							// 評価
							tempGrid = gridList[j - 1];
							gridList[j - 1] = gridList[j];
							gridList[j] = tempGrid;
						}
					}
				}
				break;
			case "SCORE：高い順":
				// 単純交換法による並べ替え
				for ( int i = 0; i < gridList.length - 1; i++ ) {
					for ( int j = gridList.length - 1; j > i; j-- ) {
						if ( gridList[j - 1].getRating() < gridList[j].getRating() ) {
							// 評価
							tempGrid = gridList[j - 1];
							gridList[j - 1] = gridList[j];
							gridList[j] = tempGrid;
						}
					}
				}
				break;
			case "SCORE：低い順":
				// 単純交換法による並べ替え
				for ( int i = 0; i < gridList.length - 1; i++ ) {
					for ( int j = gridList.length - 1; j > i; j-- ) {
						if ( gridList[j - 1].getRating() > gridList[j].getRating() ) {
							tempGrid = gridList[j - 1];
							gridList[j - 1] = gridList[j];
							gridList[j] = tempGrid;
						}
					}
				}
				break;
			case "映画タイトル：昇順":
				Comparator comparatorSyoujun = new strCompareAscending();
				Arrays.sort(gridList, comparatorSyoujun);
				break;
			case "映画タイトル：降順":
				Comparator comparatorKoujun = new strCompareDescending();
				Arrays.sort(gridList, comparatorKoujun);
				break;
		}
		// 配列をアレイリストに移す
		ArrayList<GridCard> arrayList = new ArrayList<GridCard>();
		for (int i=0; i<gridList.length; i++) {
			GridCard gc = gridList[i];
			arrayList.add(new GridCard(gc.getTitle(),gc.getBitmap(),gc.getCommentCount(),gc.getLikeCount(),gc.getRating(),gc.getDay()));
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
			ImageButton btnMovieJacket = (ImageButton) linearLayout.findViewById(R.id.btnMovieJaket);
			int width = btnMovieJacket.getLayoutParams().width;
			int height = btnMovieJacket.getLayoutParams().height;
			Bitmap bitmap = Bitmap.createScaledBitmap(gridList[i].getBitmap(), width, height, false);
			btnMovieJacket.setImageBitmap(bitmap);

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

			ImageView user_icon = (ImageView) linearLayout.findViewById(R.id.userImage);
			TextView title = (TextView) linearLayout.findViewById(R.id.userName);
			RatingBar ratingBar = (RatingBar) linearLayout.findViewById(R.id.ratingBar);
			TextView rating = (TextView) linearLayout.findViewById(R.id.rating);
			TextView comment = (TextView) linearLayout.findViewById(R.id.userComment);

			user_icon.setImageBitmap(gridList[i].getBitmap());
			title.setText(gridList[i].getTitle());
			ratingBar.setRating(gridList[i].getRating());
			rating.setText(Float.toString(gridList[i].getRating()));

			cardView.setTag(i);

			// カードビューとそのインデックス？を追加
			cardLinear.addView(linearLayout, i);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.c5_layoutBtn_list:

				Fragment fragment = new C5_M_WatchedGrid();
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				ft.replace(R.id.c5_container, fragment).commit();

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
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		imageBuilder();

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
		Resources res = getResources();
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		bitmaps[0] = BitmapFactory.decodeResource(res, R.drawable.movie_the_italian_jop, options);
		bitmaps[1] = BitmapFactory.decodeResource(res, R.drawable.movie_steve_jobs, options);
		bitmaps[2] = BitmapFactory.decodeResource(res, R.drawable.movie_shonen_merikensack, options);
		bitmaps[3] = BitmapFactory.decodeResource(res, R.drawable.movie_hatsukoi, options);
		bitmaps[4] = BitmapFactory.decodeResource(res, R.drawable.movie_himizu, options);
		bitmaps[5] = BitmapFactory.decodeResource(res, R.drawable.movie_merry_christmas_mr_lawrence, options);
		bitmaps[6] = BitmapFactory.decodeResource(res, R.drawable.movie_climers_high, options);
		bitmaps[7] = BitmapFactory.decodeResource(res, R.drawable.movie_almost_famous, options);
		bitmaps[8] = BitmapFactory.decodeResource(res, R.drawable.movie_robocon, options);
		bitmaps[9] = BitmapFactory.decodeResource(res, R.drawable.movie_the_fan, options);
		bitmaps[10] = BitmapFactory.decodeResource(res, R.drawable.movie_tantei_monogatari, options);
		bitmaps[11] = BitmapFactory.decodeResource(res, R.drawable.movie_yaju_shisubeshi, options);
		bitmaps[12] = BitmapFactory.decodeResource(res, R.drawable.movie_pai, options);
		bitmaps[13] = BitmapFactory.decodeResource(res, R.drawable.movie_doctor_parnassus, options);
		bitmaps[14] = BitmapFactory.decodeResource(res, R.drawable.movie_reality_bites, options);
		bitmaps[15] = BitmapFactory.decodeResource(res, R.drawable.movie_oh_brother, options);
		bitmaps[16] = BitmapFactory.decodeResource(res, R.drawable.movie_myrage_mylife, options);
		bitmaps[17] = BitmapFactory.decodeResource(res, R.drawable.movie_the_iron_mask, options);
		bitmaps[18] = BitmapFactory.decodeResource(res, R.drawable.movie_catch_me_if_you_can, options);
		bitmaps[19] = BitmapFactory.decodeResource(res, R.drawable.movie_blood_diamond, options);
		bitmaps[20] = BitmapFactory.decodeResource(res, R.drawable.movie_ghost_rider, options);
		bitmaps[21] = BitmapFactory.decodeResource(res, R.drawable.movie_biohazard3, options);
		bitmaps[22] = BitmapFactory.decodeResource(res, R.drawable.movie_john_q, options);
		bitmaps[23] = BitmapFactory.decodeResource(res, R.drawable.movie_wild_wild_west, options);
		gridList[0] = new GridCard("ミニミニ大作戦", bitmaps[0], 0, 0, 4.0f, 23);
		gridList[1] = new GridCard("スティーブ・ジョブズ", bitmaps[1], 0, 1, 1.5f, 22);
		gridList[2] = new GridCard("少年メリケンサック", bitmaps[2], 0, 0, 4.0f, 21);
		gridList[3] = new GridCard("初恋", bitmaps[3], 0, 0, 3.8f, 20);
		gridList[4] = new GridCard("ヒミズ", bitmaps[4], 0, 0, 3.8f, 19);
		gridList[5] = new GridCard("戦場のメリークリスマス", bitmaps[5], 0, 0, 4.5f, 18);
		gridList[6] = new GridCard("クライマーズ・ハイ", bitmaps[6], 0, 0, 4.2f, 17);
		gridList[7] = new GridCard("あの頃ペニーレインと", bitmaps[7], 0, 0, 0, 16);
		gridList[8] = new GridCard("ロボコン", bitmaps[8], 0, 0, 3.7f, 15);
		gridList[9] = new GridCard("The Fan", bitmaps[9], 0, 0, 3.9f, 14);
		gridList[10] = new GridCard("探偵物語", bitmaps[10], 0, 0, 0, 13);
		gridList[11] = new GridCard("野獣死すべし", bitmaps[11], 0, 0, 4.3f, 12);
		gridList[12] = new GridCard("π", bitmaps[12], 0, 0, 0, 11);
		gridList[13] = new GridCard("Dr.パルナサスの鏡", bitmaps[13], 0, 0, 0, 10);
		gridList[14] = new GridCard("Reality Bites", bitmaps[14], 0, 0, 3.3f, 9);
		gridList[15] = new GridCard("オー・ブラザー！", bitmaps[15], 0, 0, 3.8f, 8);
		gridList[16] = new GridCard("マイレージ、マイライフ", bitmaps[16], 0, 0, 3.5f, 7);
		gridList[17] = new GridCard("仮面の男", bitmaps[17], 0, 0, 0, 6);
		gridList[18] = new GridCard("Catch Me If You Can", bitmaps[18], 0, 0, 0, 5);
		gridList[19] = new GridCard("Blood Diamond", bitmaps[19], 0, 0, 0, 4);
		gridList[20] = new GridCard("Ghost Rider", bitmaps[20], 2, 0, 2.4f, 3);
		gridList[21] = new GridCard("バイオハザード３", bitmaps[21], 0, 0, 2.0f, 2);
		gridList[22] = new GridCard("ジョンQ", bitmaps[22], 0, 0, 4.0f, 1);
		gridList[23] = new GridCard("Wild Wild West", bitmaps[23], 0, 0, 2.5f, 0);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.v("test/C5_list", "- onDestroyView");

		// 画像をメモリから解放
		Log.v("test/C5", "recycle");
		for (int i=0; i<bitmaps.length; i++) {
			//if (bitmaps[i]!=null) {
			bitmaps[i].recycle();
			bitmaps[i] = null;
			//}
		}
	}

}
