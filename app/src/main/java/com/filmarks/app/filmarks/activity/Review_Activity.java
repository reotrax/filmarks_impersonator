package com.filmarks.app.filmarks.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.filmarks.app.filmarks.R;
import com.filmarks.app.filmarks.definition.LoaderForGetSQL;
import com.filmarks.app.filmarks.definition.LoaderPostSQL;

import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

public class Review_Activity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener,
		View.OnClickListener, CompoundButton.OnCheckedChangeListener,LoaderManager.LoaderCallbacks {

	private TextView score;
	private EditText review,editTag;
	private String url = "http://160.16.127.123/android/filmarks/php/add_sql.php";
	private String accountName = "filmarks";
	private ToggleButton netabareBtn;
	private boolean isChecked = false;
	private Intent intent;

	// エディットテキストの状態変化を監視
	private TextWatcher watcher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			Log.v("test", "before / char" + s);
		}
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			Log.v("test", "changed / char" + s);
		}
		@Override
		public void afterTextChanged(Editable s) {
			Log.v("test", "after / editable " + s);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_review);

		// インテント取得
		intent = getIntent();

		// ツールバー - styles.xml で Light.NoActionBarを設定
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("");
		setSupportActionBar(toolbar);
		ImageButton closeBtn = (ImageButton) findViewById(R.id.closeBtn);
		Button saveBtn = (Button) findViewById(R.id.saveBtn);
		closeBtn.setOnClickListener(this);
		saveBtn.setOnClickListener(this);

		// ビュー
		SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
		LinearLayout reviewLayout = (LinearLayout) findViewById(R.id.reviewLayout);
		netabareBtn = (ToggleButton) findViewById(R.id.netabareBtn);
		score = (TextView) findViewById(R.id.score);
		review = (EditText) findViewById(R.id.edit_review);
		editTag = (EditText) findViewById(R.id.edit_tag);

		netabareBtn.setOnCheckedChangeListener(this);
		editTag.addTextChangedListener(watcher);
		seekBar.setOnSeekBarChangeListener(this);
		reviewLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.closeBtn:
				finish();
				break;

			case R.id.saveBtn:
				if (!score.getText().equals("-")) {
					Bundle args = new Bundle();
					int id = 0;
					// onCreateLoaderが呼ばれます
					getSupportLoaderManager().initLoader(id, args, this);
				} else {
					Toast.makeText(this,"スコアを設定してください",Toast.LENGTH_SHORT).show();
				}
				break;

			case R.id.reviewLayout:
				review.requestFocus();
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(review, InputMethodManager.SHOW_FORCED);
				break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		this.isChecked = isChecked;
	}

	@Override
	public Loader<String> onCreateLoader(int id, Bundle args) {
		LoaderPostSQL loader = null;

		// ここを切り替えてあげるだけで様々な非同期処理に対応できます.
		if(args != null) {

			String title = intent.getStringExtra("title");
			String titleNo = intent.getStringExtra("title_no");
			String titleImg = intent.getStringExtra("title_img");
			String netabare = Boolean.toString(isChecked);
			String review = this.review.getText().toString();
			String score = this.score.getText().toString();

			String tag = this.editTag.getText().toString();
			if (tag.equals("")) {
				tag = "0";
			}

			Time time = new Time("Asia/Tokyo");
			time.setToNow();
			String reviewDate = time.year +"/"+ time.month +"/"+ time.monthDay +"/"+ time.hour +":"+ time.minute +":"+ time.second;

			try {

				JSONObject object = new JSONObject();
				object.put("user_name", accountName);
				object.put("user_img", "http://160.16.127.123/android/filmarks/icon/filmarks_logo.png"); //別に取得する必要が有る。
				object.put("user_no", "0"); //別に取得する必要が有る。
				object.put("title", title);
				object.put("title_no", titleNo);
				object.put("title_img", titleImg);
				object.put("netabare", netabare);
				object.put("score", score);
				object.put("tag", tag);
				object.put("review", review);
				object.put("review_date", reviewDate);
				String json = object.toString();
				Log.v("test",json);
				loader = new LoaderPostSQL(this,url,"addWatch",json,Integer.toString(id));

			} catch (JSONException e) {

				Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();

			}
		}
		return loader;
	}

	@Override
	public void onLoadFinished(Loader loader, Object data) {
		Log.v("test", "onLoadFinished // " + data.toString());
	}
	@Override
	public void onLoaderReset(Loader loader) {
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	// シークバー
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		// シークバーは0~50なので、/10fで小数点にする
		float f = progress/10f;
		if (f<1) {
			score.setText("-");
		} else {
			score.setText(Float.toString(f));
		}
	}
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}
}
