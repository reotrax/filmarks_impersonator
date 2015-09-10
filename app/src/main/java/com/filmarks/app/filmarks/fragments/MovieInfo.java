package com.filmarks.app.filmarks.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.filmarks.app.filmarks.MainActivity;
import com.filmarks.app.filmarks.R;
import com.filmarks.app.filmarks.definition.LoaderForGetSQL;
import com.filmarks.app.filmarks.definition.LoaderImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieInfo extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks {

	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	private static final String ARG_PARAM3 = "param3";
	private static final String ARG_PARAM4 = "param4";
	private static final String ARG_PARAM5 = "param5";
	private String url = "http://160.16.127.123/android/filmarks/php/json_copy.php";
	private String movieNo,movieImg,movieTitle,userNo,userImg;
	private OnFragmentInteractionListener mListener;
	private TextView title,year,sub,duration,rating;
	private ImageView image;
	private RatingBar ratingBar;
	private LinearLayout directors,writers,casts;

	// TODO: Rename and change types and number of parameters
	public static MovieInfo newInstance(String movieNo, String imageURL, String title, String userImg, String userNo) {
		MovieInfo fragment = new MovieInfo();
		Bundle args = new Bundle();
		if (movieNo!=null) {
			args.putString(ARG_PARAM1, movieNo); // 映画番号
			args.putString(ARG_PARAM2, imageURL);
			args.putString(ARG_PARAM3, title);
			args.putString(ARG_PARAM4, userImg);
			args.putString(ARG_PARAM5, userNo);
		}
		fragment.setArguments(args);
		return fragment;
	}

	// TODO: Required empty public constructor
	public MovieInfo() {}

	@Override
	public void onAttach(Activity activity) {super.onAttach(activity);}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 受け取った映画番号を取得
		movieNo 	= getArguments().getString(ARG_PARAM1);
		movieImg 	= getArguments().getString(ARG_PARAM2);
		movieTitle 	= getArguments().getString(ARG_PARAM3);
		userImg 	= getArguments().getString(ARG_PARAM4);
		userNo 	= getArguments().getString(ARG_PARAM5);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.movie_info, container, false);
		LinearLayout watchedBtn = (LinearLayout) view.findViewById(R.id.watched_btnLayout);
		LinearLayout wantBtn = (LinearLayout) view.findViewById(R.id.want_btnLayout);
		watchedBtn.setOnClickListener(this);
		wantBtn.setOnClickListener(this);
		// レイアウトパーツ
		layoutViewAndListener(view);
		// 映画データ取得（非同期）
		Bundle args = new Bundle();
		args.putString("IMG", movieImg);
		getLoaderManager().initLoader(0, args, this);

		return view;
	}

	private void layoutViewAndListener(View view) {
		LinearLayout backBtn = (LinearLayout) view.findViewById(R.id.info_backBtn);
		ImageButton facebook = (ImageButton) view.findViewById(R.id.info_facebook);
		ImageButton twitter = (ImageButton) view.findViewById(R.id.info_twitter);
		title = (TextView) view.findViewById(R.id.info_title);
		year  = (TextView) view.findViewById(R.id.info_year);
		sub	  = (TextView) view.findViewById(R.id.info_sub_title);
		duration = (TextView) view.findViewById(R.id.info_duration);
		image = (ImageView) view.findViewById(R.id.info_image);
		ratingBar = (RatingBar) view.findViewById(R.id.ratingBar2);
		rating  = (TextView) view.findViewById(R.id.info_point);
		directors = (LinearLayout) view.findViewById(R.id.directors);
		writers = (LinearLayout) view.findViewById(R.id.writers);
		casts  = (LinearLayout) view.findViewById(R.id.casts);
		// リスナー登録
		backBtn.setOnClickListener(this);
		facebook.setOnClickListener(this);
		twitter.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.watched_btnLayout:
				MainActivity activity = (MainActivity)getActivity();
				activity.callReviewActivity(movieNo,movieImg,movieTitle,userNo,userImg);
				break;

			case R.id.want_btnLayout:
				break;

			case R.id.info_backBtn:
				// スタックしたフラグメントに戻る
				getFragmentManager().popBackStack();
				break;

			case R.id.info_facebook:
				break;

			case R.id.info_twitter:
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
		mListener = null;
	}

	@Override
	public Loader onCreateLoader(int id, Bundle args) {
		Log.v("test", "----onCreateLoader");
		// 非同期で処理を実行するLoaderを生成します.
		LoaderForGetSQL loader = null;

		// ここを切り替えてあげるだけで様々な非同期処理に対応できます.
		if(args != null) {
			loader = new LoaderForGetSQL(getActivity(), url, "info", movieNo);
			// 1.context, 2.url, 3.phpでの仕分け用, 4.映画id.
		}
		return loader;
	}

	@Override
	public void onLoadFinished(Loader loader, Object data) {
		Log.v("test", "----onLoadFinished");
		try {
			// 取得したデータをJSONオブジェクト化 -> Viewに表示
			JSONObject jsonObject = new JSONObject(data.toString());
			JSONArray jsonArray = jsonObject.getJSONArray("movie");

			JSONObject jObj = jsonArray.getJSONObject(0);
			title.setText(jObj.getString("title"));
			year.setText("(" + jObj.getString("year") + ")");
			duration.setText(jObj.getString("duration") + "分, " + jObj.getString("country"));
//			ratingBar.setRating(Integer.parseInt(jObj.getString("rating")));
//			rating.setText(jObj.getString("rating"));

			Log.v("test", jObj.getString("title_origin"));
			// サブタイトル
			if (!jObj.getString("title_origin").equals("null")) {
				sub.setText(jObj.getString("title_origin"));
			} else {
				sub.setVisibility(View.GONE);
			}

			// ディレクターの分割
			String directorJSON = jObj.getString("director");
			String[] director = directorJSON.split(",", 0);
			for (int i=0; i<director.length; i++)
			{
				Button button = new Button(getActivity());
				button.setGravity(Gravity.LEFT);
				button.setText(director[i]);
				button.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// 画面遷移
					}
				});
				int MP = ViewGroup.LayoutParams.MATCH_PARENT;
				int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
				directors.addView(button, i, new LinearLayout.LayoutParams(MP,80));

				if (i==4)
					break;
			}

			// 脚本家の分割
			String writerJSON = jObj.getString("writer");
			String[] writer = writerJSON.split(",", 0);
			for (int i=0; i<writer.length; i++)
			{
				Button button = new Button(getActivity());
				button.setGravity(Gravity.LEFT);
				button.setText(writer[i]);
				button.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// 画面遷移
					}
				});
				int MP = ViewGroup.LayoutParams.MATCH_PARENT;
				int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
				writers.addView(button, i, new LinearLayout.LayoutParams(MP,80));

				if (i==4)
					break;
			}

			// キャストの分割と表示
			String castJSON = jObj.getString("cast");
			Log.v("test", "castJSON " + castJSON);
			String[] cast = castJSON.split(",",0);
			for (int i=0; i<cast.length; i++)
			{
				Button button = new Button(getActivity());
				button.setGravity(Gravity.LEFT);
				button.setText(cast[i]);
				button.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// 画面遷移
					}
				});
				int MP = ViewGroup.LayoutParams.MATCH_PARENT;
				int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
				casts.addView(button, i, new LinearLayout.LayoutParams(MP,80));

				if (i==4)
					break;
			}

			// 画像の取得
			LoaderImage loaderImage = new LoaderImage(getActivity(), image, image.getLayoutParams().width, image.getLayoutParams().height);
			loaderImage.execute(movieImg);

		} catch (JSONException e) {
			Log.v("test", e.toString());
		}

	}

	@Override
	public void onLoaderReset(Loader loader) {

	}

	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onFragmentInteraction(Uri uri);
	}

}
