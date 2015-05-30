package com.filmarks.app.filmarks.fragments;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.filmarks.app.filmarks.R;
import com.filmarks.app.filmarks.definition.CustomGridAdapter;
import com.filmarks.app.filmarks.definition.ExpandableHeightGridView;
import com.filmarks.app.filmarks.definition.GridCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class C5_M_WatchedGrid extends Fragment
		implements AdapterView.OnItemSelectedListener, View.OnClickListener {

	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	private static final String ARG_PARAM9 = "param9";
	private ArrayList<GridCard> list = new ArrayList<>();
	private Bitmap[] bitmaps = new Bitmap[24];
	private GridCard[] gridList = new GridCard[24];
	private ArrayList<String> sortList = new ArrayList<>(Arrays.asList(
			"投稿日時：新しい順", "投稿日時：古い順", "SCORE：高い順", "SCORE：低い順",
			"制作年：新しい順", "制作年：古い順", "映画タイトル：昇順", "映画タイトル：降順"));

	private OnFragmentInteractionListener mListener;
	private Spinner spinnerSort;
	private ExpandableHeightGridView exGridView;
	private CustomGridAdapter grid_adapter;
	private int refreshNumber = 0;
	private Fragment fragment;

	private enum Enum {GRID, LIST}
	private Enum vison = Enum.GRID;


	public static C5_M_WatchedGrid newInstance(int param1) {
		C5_M_WatchedGrid fragment = new C5_M_WatchedGrid();
		Bundle args = new Bundle();
		args.putInt(ARG_PARAM1, param1);
		fragment.setArguments(args);
		return fragment;
	}


	public C5_M_WatchedGrid() {
		/** Required empty public constructor */
	}

	// 自作インターフェース
	private StateBridgeListener bridgeListener;

	public interface StateBridgeListener {
		public void Bridged(int refreshNumber);
	}

	// スワイプリフレッシュに対応する自作リスナー
	public void setListener (StateBridgeListener bridgeListener) {
		this.bridgeListener = bridgeListener;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// 画像準備
		imageBuilder();

		Fragment fragment = new C5_M_Watched_2();
		if (fragment instanceof StateBridgeListener == false) {
			throw new ClassCastException("fragment が StateBridgeListener を実装していません.");
		}
		bridgeListener = ((StateBridgeListener) fragment);

		// refreshNumber 用のバンドルが有ればセット
		Bundle args = getArguments();
		if (args != null) {
			refreshNumber = args.getInt(ARG_PARAM1);
			Log.d("test/Glid", "arg2 : " + refreshNumber);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view =  inflater.inflate(R.layout.c5_mypage_w_grid, container, false);

		ImageButton change_layout_btn = (ImageButton)view.findViewById(R.id.c5_layoutBtn_grid);
		change_layout_btn.setOnClickListener(this);

		//ソートの種類を切り替えるSpinner
		spinnerSort = (Spinner) view.findViewById(R.id.c5_spinner_grid);
		ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, sortList);
		spinnerSort.setAdapter(adapter);
		spinnerSort.setSelection(refreshNumber);
		spinnerSort.setOnItemSelectedListener(this);

		ArrayList<GridCard> arrayList = new ArrayList<GridCard>();
		for (int i=0; i<gridList.length; i++) {
			GridCard gc = gridList[i];
			arrayList.add(new GridCard(gc.getTitle(), gc.getBitmap(), gc.getCommentCount(), gc.getLikeCount(), gc.getRating(), gc.getDay()));
		}
		// GridLayoutを設定しなおす
		exGridView = (ExpandableHeightGridView) view.findViewById(R.id.c5_grid_gridview);
		grid_adapter = new CustomGridAdapter(getActivity(), R.layout.mypage_card_grid, arrayList);
		exGridView.setAdapter(grid_adapter);
		exGridView.setExpanded(true);
		exGridView.setNumColumns(5);



		return view;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		GridCard tempGrid;
		String item = (String) parent.getSelectedItem();
		switch (item){
			case "投稿日時：新しい順":
				sortNewPost();
				break;
			case "投稿日時：古い順":
				sortOldPost();
				break;
			case "SCORE：高い順":
				sortHighScore();
				break;
			case "SCORE：低い順":
				sortLowScore();
				break;
			case "映画タイトル：昇順":
				sortUpTitle();
				break;
			case "映画タイトル：降順":
				sortDownTitle();
				break;
		}
		// 配列をアレイリストに移す
		ArrayList<GridCard> arrayList = new ArrayList<GridCard>();
		for (int i=0; i<gridList.length; i++) {
			GridCard gc = gridList[i];
			arrayList.add(new GridCard(gc.getTitle(),gc.getBitmap(),gc.getCommentCount(),gc.getLikeCount(),gc.getRating(),gc.getDay()));
		}
		grid_adapter = new CustomGridAdapter(getActivity(), R.layout.mypage_card_grid, arrayList);
		exGridView.setAdapter(grid_adapter);
		exGridView.setExpanded(true);
		exGridView.setNumColumns(5);


	}

	private void sortDownTitle() {
		Comparator comparatorKoujun = new strCompareDescending();
		Arrays.sort(gridList, comparatorKoujun);
	}

	private void sortUpTitle() {
		Comparator comparatorSyoujun = new strCompareAscending();
		Arrays.sort(gridList, comparatorSyoujun);
	}

	private void sortLowScore() {
		GridCard tempGrid;
		for ( int i = 0; i < gridList.length - 1; i++ ) {
			for ( int j = gridList.length - 1; j > i; j-- ) {
				if ( gridList[j - 1].getRating() > gridList[j].getRating() ) {
					tempGrid = gridList[j - 1];
					gridList[j - 1] = gridList[j];
					gridList[j] = tempGrid;
				}
			}
		}
	}

	private void sortHighScore() {
		GridCard tempGrid;
		for ( int i = 0; i < gridList.length - 1; i++ ) {
			for ( int j = gridList.length - 1; j > i; j-- ) {
				if ( gridList[j - 1].getRating() < gridList[j].getRating() ) {
					tempGrid = gridList[j - 1];
					gridList[j - 1] = gridList[j];
					gridList[j] = tempGrid;
				}
			}
		}
	}

	private void sortOldPost() {
		GridCard tempGrid;
		for ( int i = 0; i < gridList.length - 1; i++ ) {
			for ( int j = gridList.length - 1; j > i; j-- ) {
				if ( gridList[j - 1].getDay() > gridList[j].getDay() ) {
					tempGrid = gridList[j - 1];
					gridList[j - 1] = gridList[j];
					gridList[j] = tempGrid;
				}
			}
		}
		bridgeListener = new StateBridgeListener() {
			@Override
			public void Bridged(int number) {
				if (bridgeListener != null) {
					refreshNumber = 1;
					Log.v("test/Grid/Brigde", "Bridged " + refreshNumber);
				}
			}
		};
		bridgeListener.Bridged(refreshNumber);
	}

	private void sortNewPost() {
		GridCard tempGrid;// 単純交換法による並べ替え
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
		bridgeListener = new StateBridgeListener() {
			@Override
			public void Bridged(int number) {
				if (bridgeListener != null) {
					refreshNumber = 0;
					Log.v("test/Grid/Brigde", "Bridged " + refreshNumber);
				}
			}
		};
		bridgeListener.Bridged(refreshNumber);
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

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.c5_layoutBtn_grid:

				Fragment fragment = new C5_M_WatchedList();
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				ft.replace(R.id.c5_container, fragment).commit();
				break;
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
	public void onDetach() {
		super.onDetach();
		mListener = null;
		bridgeListener = null;
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		// 画像をメモリから解放
		for (int i=0; i<bitmaps.length; i++) {
			//if (bitmaps[i]!=null) {
			bitmaps[i].recycle();
			bitmaps[i] = null;
		}
	}

	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onFragmentInteraction(Uri uri);
	}

}
