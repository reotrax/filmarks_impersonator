package com.filmarks.app.filmarks;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

//LruCacheに画像のキャッシュをためる！
public class C5_Mypage_Watched extends Fragment
		implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemSelectedListener, View.OnClickListener {

	private RecyclerView mRecyclerView;
	private ArrayList<GridCard> list = new ArrayList<>();
	private Bitmap[] bitmaps = new Bitmap[24];
	private GridCard[] gridList = new GridCard[24];
	private ArrayList<String> sortList = new ArrayList<String>(Arrays.asList(
			"投稿日時：新しい順","投稿日時：古い順","SCORE：高い順","SCORE：低い順",
			"制作年：新しい順","制作年：古い順","映画タイトル：昇順","映画タイトル：降順"));
	private SwipeRefreshLayout swipeRefreshLayout;
	private Spinner spinnerSort;

	public static C5_Mypage_Watched newInstance(String param1, String param2) {
		C5_Mypage_Watched fragment = new C5_Mypage_Watched();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	// コンストラクタ
	public C5_Mypage_Watched() {
		Log.v("test/C5", "- Constructor");
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.v("test/C5", "- onAttach");
		//recycleBitmap(); //エラーになる
		imageBuilder();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.c5_b1_watched, container, false);

		Button listScrollBtn = (Button) view.findViewById(R.id.listScrollBtn);
		listScrollBtn.setOnClickListener(this);

		//ソートの種類を切り替えるSpinner
		spinnerSort = (Spinner) view.findViewById(R.id.spinnerSort);
		ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,sortList);
		spinnerSort.setAdapter(adapter);
		spinnerSort.setOnItemSelectedListener(this);

		// スワイプリフレッシュ
		swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipelayout);
		//swipeRefreshLayout.setColorSchemeResources(R.color.red,R.color,green...); //色設定
		swipeRefreshLayout.setOnRefreshListener(this);

		// リサイクラービューの設定（GridLayout）
		mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
		mRecyclerView.setAdapter(new ViewAdapter(getActivity(), gridList));
		mRecyclerView.setHasFixedSize(true);
		GridLayoutManager gridlm = new GridLayoutManager(getActivity(),5);
		mRecyclerView.setLayoutManager(gridlm);

		// アニメーション設定
		//mRecyclerView.setItemAnimator();

		// GridLayoutが最下部に到達したらロードする
		mRecyclerView.setOnScrollListener(new EndlessScrollListener((LinearLayoutManager)mRecyclerView.getLayoutManager())  {
			@Override
			public void onLoadMore(int current_page) {
				Log.v("test", "うまく動作しない。。。");
				spinnerSort.setSelection(3);
			}
		});
		//mAdapter = new CardViewDataAdapter(myDataset);
		//mRecyclerView.setAdapter(mAdapter);

		return view;
	}

	// OnRefreshListener - 画面スライドでリスト更新
	@Override
	public void onRefresh() {
		new Handler().post(new Runnable() {
			@Override
			public void run() {
				// スピナーの項目を指定する
				spinnerSort.setSelection(3);
				// GridLayoutを設定しなおす
				mRecyclerView.setAdapter(new ViewAdapter(getActivity(), gridList));
				// 更新が終了したらインジケーターを非表示に
				swipeRefreshLayout.setRefreshing(false);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.listScrollBtn:
				mRecyclerView.smoothScrollToPosition(0);
				break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		imageBuilder();
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
					//
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
		mRecyclerView.setAdapter(new ViewAdapter(getActivity(),gridList));
		mRecyclerView.setHasFixedSize(true);
		GridLayoutManager glm = new GridLayoutManager(getActivity(),5);
		mRecyclerView.setLayoutManager(glm);
	}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
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

	// リサイクラービューのアダプター
	public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> {
		private LayoutInflater layoutInflater;
		private GridCard[] gridList;

		// コンストラクタ
		public ViewAdapter(Context context, GridCard[] gridList) {
			super();
			layoutInflater = LayoutInflater.from(context);
			this.gridList = gridList;
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
			// リサイクラービューのグリッドにCardViewを設定
			View view = layoutInflater.from(viewGroup.getContext()).inflate(R.layout.mypage_card_grid, viewGroup, false);
			return new ViewHolder(view);
		}

		// グリッド数
		@Override
		public int getItemCount() {
			return gridList.length;
		}

		@Override
		public void onBindViewHolder(ViewHolder viewHolder, int position) {
			Bitmap bitmap = Bitmap.createScaledBitmap(gridList[position].getBitmap(),200,284,false);
			viewHolder.jacketPict.setImageBitmap(bitmap);
			viewHolder.movieTitle = gridList[position].getTitle();
			viewHolder.movieRating = gridList[position].getRating();
		}

		// 上記 onBindViewHolder で使用するViewを定義（inflater.inflate(~)に相当）
		class ViewHolder extends RecyclerView.ViewHolder {
			private ImageButton jacketPict;
			private String movieTitle;
			private float movieRating;
			// グリッドのレイアウト定義（今回はCardView）
			public ViewHolder(View view) {
				super(view);
				jacketPict = (ImageButton) view.findViewById(R.id.btnMovieJaket);
				jacketPict.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
					}
				});
				jacketPict.setOnLongClickListener(new View.OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {
						Toast.makeText(getActivity(), movieTitle+"/"+movieRating, Toast.LENGTH_SHORT).show();
						return false;
					}
				});
			}
		}


	}



	// グリッドレイアウトの設定
	public class CardLayoutManager extends GridLayoutManager {
		// コンストラクタ
		public CardLayoutManager(Context context, int spanCount) {
			super(context, spanCount); // spanCount = グリッドの列数
		}
		@Override
		public View findViewByPosition(int position) {
			return null;
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
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.v("test/C5", "- onPause");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.v("test/C5", "- onDestroyView");

		// 画像をメモリから解放
		Log.v("test/C5", "recycle");
		for (int i=0; i<bitmaps.length; i++) {
			//if (bitmaps[i]!=null) {
			bitmaps[i].recycle();
			bitmaps[i] = null;
			//}
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Log.v("test/C5", "- onDetach");
	}
}
