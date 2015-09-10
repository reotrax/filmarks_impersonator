package com.filmarks.app.filmarks.definition;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Created by user on 15/06/10.
 */
public class AsyncSQL extends AsyncTask<Void,Void,Void> {
	// AsyncTask< 1, 2, 3>
	// 1,呼び出し元でメソッドが実行される時に渡される引数の型
	// 2,進捗を表現する時に使用する型
	// 3,スレッド終了時に返す値の型

	Activity activity = null;

	public AsyncSQL(Activity activity) {
		this.activity = activity;
	}

	@Override
	protected Void doInBackground(Void... params) {



		return null;
	}

	@Override
	protected void onPostExecute(Void aVoid) {
		super.onPostExecute(aVoid);

		Toast.makeText(activity,"登録を終了しました", Toast.LENGTH_LONG).show();
	}
}
