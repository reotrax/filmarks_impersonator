package com.filmarks.app.filmarks.fragments;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.transition.Slide;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.filmarks.app.filmarks.R;
import com.filmarks.app.filmarks.definition.CustomGridAdapter;
import com.filmarks.app.filmarks.definition.ExpandableHeightGridView;
import com.filmarks.app.filmarks.definition.GridCard;
import com.filmarks.app.filmarks.definition.LoaderForGetSQL;
import com.filmarks.app.filmarks.definition.ServerGridSQL;
import com.filmarks.app.filmarks.urlimageview.ImageCache;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class C5_M_WatchedGrid extends Fragment
		implements AdapterView.OnItemSelectedListener, View.OnClickListener, AdapterView.OnItemClickListener,
		LoaderManager.LoaderCallbacks {

	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	private ExpandableHeightGridView exGridView;
	private CustomGridAdapter grid_adapter;
	private static int refreshNumber = 0;
	private int gridCount = 3;
	private int stuckCount = 0;
	private int startNum = 0;
	private String url = "http://160.16.127.123/android/filmarks/php/json_copy.php";
	private ArrayList<GridCard> arrayList = new ArrayList<>(); // -1-
	private ArrayList<ServerGridSQL> arraySQL = new ArrayList<>(); // -2-
	private ArrayList<ServerGridSQL> arraySQLTotal = new ArrayList<>(); // -3-
	private ArrayList<String> sortList = new ArrayList<>(Arrays.asList(
			"投稿日時：新しい順", "投稿日時：古い順", "SCORE：高い順", "SCORE：低い順",
			"制作年：新しい順", "制作年：古い順", "映画タイトル：昇順", "映画タイトル：降順"));


	public static C5_M_WatchedGrid newInstance(int gridCount, int refreshNumber) {
		C5_M_WatchedGrid fragment = new C5_M_WatchedGrid();
		Bundle args = new Bundle();
		if (gridCount!=0) {
			args.putInt(ARG_PARAM1, gridCount);
		}
		if (refreshNumber != 0) {
			args.putInt(ARG_PARAM2, refreshNumber);
		}
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		bridgeListener = ((StateBridgeListener) activity);
		stateLisntener = ((FragmentGridListener) activity);

		// グリッド数用の引数を取得
		Bundle arg = getArguments();
		if (arg != null) {
			Log.v("test", "bundle!=null");
			gridCount = arg.getInt(ARG_PARAM1);
			refreshNumber = arg.getInt(ARG_PARAM2);
		} else {
			Log.v("test", "bundle==null");
		}

		// ソートの種類
	}

	public C5_M_WatchedGrid() {
		/** Required empty public constructor */
	}

	// 自作インターフェース
	// 1,interface---2,引数---3,引数=(interface)activity---4,interface.method()で実行
	private FragmentGridListener stateLisntener;

	public interface FragmentGridListener {

		public void FragmentGridListener(String layout, int refreshNumber);
	}
	// 自作インターフェース
	private StateBridgeListener bridgeListener;

	public interface StateBridgeListener {
		public void BridgedForGrid(String str, int number);
	}
	// スワイプリフレッシュに対応する自作リスナー
	public void setListener (StateBridgeListener bridgeListener) {
		this.bridgeListener = bridgeListener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view =  inflater.inflate(R.layout.c5_mypage_w_grid, container, false);

		ImageButton change_layout_btn = (ImageButton)view.findViewById(R.id.c5_layoutBtn_grid);
		Button load_btn = (Button)view.findViewById(R.id.c5_loadBtn);
		change_layout_btn.setOnClickListener(this);
		load_btn.setOnClickListener(this);

		// GridLayoutを設定しなおす
		exGridView = (ExpandableHeightGridView) view.findViewById(R.id.c5_grid_gridview);
		exGridView.setExpanded(true);
		exGridView.setNumColumns(gridCount);
		exGridView.setOnItemClickListener(this);
//		grid_adapter = new CustomGridAdapter(getActivity(), R.layout.mypage_card_grid);

		// ソートの種類を切り替えるSpinner
		Spinner spinnerSort = (Spinner) view.findViewById(R.id.c5_spinner_grid);
		ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, sortList);
		spinnerSort.setAdapter(adapter);
		spinnerSort.setSelection(refreshNumber);
		spinnerSort.setOnItemSelectedListener(this);

		// バックスタックから戻った場合はキャッシュ済みの画像などを読み込む
		if (stuckCount == 1) {
			grid_adapter = new CustomGridAdapter(getActivity(), R.layout.mypage_card_grid);
			for (int i=0; i<arrayList.size(); i++) {
				GridCard gc = arrayList.get(i);
				grid_adapter.add(new GridCard(gc.getUserNo(),gc.getUserImg(), gc.getTitle(), gc.getTitleNo(),gc.getTitleUrl(),
						gc.getComCount(), gc.getFavCount(), gc.getRating(), gc.getDate()));
			}
			exGridView.setAdapter(grid_adapter);
			startNum = grid_adapter.getCount();
		}

			return view;
	}

	// グリッドビューのクリックリスナー
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// Toast.makeText(getActivity(), "ItemClick : " + position, Toast.LENGTH_SHORT).show();

		GridCard gc = (GridCard) parent.getAdapter().getItem(position);
		Fragment fragment = new MovieInfo().newInstance(gc.getTitleNo(),gc.getTitleUrl(),gc.getTitle(),gc.getUserImg(),gc.getUserNo());
		// 遷移アニメーション（ver.21ロリポップ以上の場合）
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			TransitionSet set = new TransitionSet();
			set.addTransition(new Slide(Gravity.RIGHT));
			fragment.setEnterTransition(set);
		}
		getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.container1, fragment).commit();

		Toast.makeText(getActivity(), "no : " + gc.getTitleNo(), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		String item = (String) parent.getSelectedItem();
		switch (item) {
			case "投稿日時：新しい順":
				// 1.自分のWatched映画を読み込む
				// 2.グリッドを一斉に生成
				// 3.グリッド毎に画像を読み込む
				startAsyncLoad(url,startNum);
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
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.c5_layoutBtn_grid:
				// アクティビティ側でフラグメントをリプレイスする
				stateLisntener.FragmentGridListener("GRID", refreshNumber);
				break;
			case R.id.c5_loadBtn:
				stuckCount = 0;
				grid_adapter = null;
				startAsyncLoad(url,startNum);
				Toast.makeText(getActivity(),"loadBtn",Toast.LENGTH_SHORT).show();
				break;
		}
	}

	// ----非同期で画像取得
	public void startAsyncLoad(String url, int id) {
		Log.v("test", "----startAsyncLoad");
		Bundle args = new Bundle();
		args.putString("url", url);

		//childCount = cardLinear.getChildCount();
		// onCreateLoaderが呼ばれます
		getLoaderManager().initLoader(id, args, this);

		// TODO: 複数のLoaderを同時に動かす場合は、第一引数を一意のIDにしてやる必要があります。
		// TODO: GridViewなどに表示する画像を非同期で一気に取得する場合とか
	}

	@Override
	public Loader onCreateLoader(int id, Bundle args) {
		Log.v("test", "----onCreateLoader");
		// 非同期で処理を実行するLoaderを生成します.
		LoaderForGetSQL loader = null;

		// ここを切り替えてあげるだけで様々な非同期処理に対応できます.
		if(args != null) {
			loader = new LoaderForGetSQL(getActivity(), url, "grid"+gridCount, Integer.toString(id));
			// 1.context, 2.url, 3.phpでの仕分け用, 4.SQL取得開始レコードNo.
		}

		return loader;
	}

	@Override
	public void onLoadFinished(Loader loader, Object object) {
		Log.v("test", "----onLoadFinished");
		// 非同期処理が終了したら呼ばれる

		if (stuckCount == 0) {
			Log.v("test", "stuckCount = " + stuckCount);
			makeTheGrid(object);
		}
		// バックスタックで戻った時にカードビューを再読み込みしないようにする
		stuckCount = 1;
		//loading = false;
	}

	@Override
	public void onLoaderReset(Loader loader) {
	}

	// グリッド生成
	private void makeTheGrid(Object object) {
		Log.v("test", "object " + object);
		if (object != null) {
			try {
				JSONObject jsonObject = new JSONObject(object.toString());
				JSONArray jsonArray = jsonObject.getJSONArray("filmarks");
				arraySQL.clear();

				// 配列をアレイリストに移す
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jObj 	= jsonArray.getJSONObject(i);
					String user_img 	= jObj.getString("user_img");
					String user_no 		= jObj.getString("user_no");
					String review_date 	= jObj.getString("review_date");
					String rating 		= jObj.getString("rating");
					String c_fav 		= jObj.getString("c_fav");
					String fav 			= jObj.getString("fav");
					String title_img 	= jObj.getString("title_img");
					String title 		= jObj.getString("title");
					String title_no 	= jObj.getString("title_no");
					// 1.グリッドの要素追加
					arrayList.add(new GridCard(user_no, user_img, title, title_no, title_img, c_fav, fav, rating, review_date));
					// 2.追加で取得するグリッド用DB
					arraySQL.add(  i, new ServerGridSQL(review_date, rating, c_fav, fav, title_img, title, title_no));
					// 3.表示されている全てのグリッド用DB
					arraySQLTotal.add(new ServerGridSQL(review_date, rating, c_fav, fav, title_img, title, title_no));
				}

				// グリッドの更新対象のViewを取得
				switch (startNum) {
					case 0:
						// グリッドの生成
						grid_adapter = new CustomGridAdapter(getActivity(), R.layout.mypage_card_grid);
						for (int i=0; i<arrayList.size(); i++) {
							GridCard gc = arrayList.get(i);
							grid_adapter.add(new GridCard(gc.getUserNo(), gc.getUserImg(), gc.getTitle(), gc.getTitleNo(),gc.getTitleUrl(),
									gc.getComCount(), gc.getFavCount(), gc.getRating(), gc.getDate()));
						}
						exGridView.setAdapter(grid_adapter);
						break;

					default:
//						for (int i=0/*startNum*/; i<exGridView.getChildCount()/*arrayList.size()*/; i++) {
							// 更新対象のViewを取得
							//View targetView = exGridView.getChildAt(i);
							// getViewで対象のViewを更新
							//exGridView.getAdapter().getView(i, targetView, exGridView);

//							GridCard gc = arrayList.get(i);
//							arrayList.add(new GridCard(gc.getTitle(), gc.getTitleUrl(),
//									gc.getComCount(), gc.getFavCount(), gc.getRating(), gc.getDate()));
							grid_adapter = new CustomGridAdapter(getActivity(), R.layout.mypage_card_grid, arrayList);
							//grid_adapter.notifyDataSetChanged();
							exGridView.setAdapter(grid_adapter);
//						}
						break;
				}
				// 呼び出したグリッド数を次回呼び出しの開始値に足す。
				startNum += gridCount;

			} catch (JSONException e) {
				Log.v("test", e.toString());
			}

			// スナックバー
//			LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.snackbar_main);
//			Snackbar.make(layout, "[JSON]: OK", Snackbar.LENGTH_LONG)
//					.setAction("UNDO", new View.OnClickListener() {
//						@Override
//						public void onClick(View v) {
//						}
//					})
//					//.setActionTextColor(Color.rgb(255,255,255))
//					.show();
		} else {
			Log.v("test", "onLoadFinished - object = null");
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Log.v("test", "onDetach");
		bridgeListener = null;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.v("test", "onDestroyView");
		startNum = 0;
		ImageCache.deleteAll(getActivity().getCacheDir());
	}


}
