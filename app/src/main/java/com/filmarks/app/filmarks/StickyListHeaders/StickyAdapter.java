package com.filmarks.app.filmarks.StickyListHeaders;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.filmarks.app.filmarks.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by user on 15/07/29.
 */
public class StickyAdapter extends BaseAdapter implements StickyListHeadersAdapter {

	private final LayoutInflater inflater;
	private String[] headerList = {"映画を探す","ユーザーを探す"};
	private String[] itemList = {"0,上映中の映画","0,上映予定の映画","0,映画賞","0,制作年","0,制作国","0,ジャンル","0,#タグ"
								,"1,Facebook","1,Twitter","1,人気のユーザー","1,ID、ニックネーム"}; // ２文字目まではABC順に記述する事

	public StickyAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		arrayWordReplace();
	}

	private void arrayWordReplace() {
		// ---- "0,上映中の映画" -> "映画を探す,上映中の映画" -> 最終的な表示は "上映中の映画" にする
		// itemListから順に照合して行き、止まった要素数を保持。
		int countStopNo = 0;
		Log.v("test", "headerList.length = " + headerList.length + ", itemList.length = " + itemList.length);

		for (int i=0; i<headerList.length; i++) {
			String replaceText = headerList[i] + ",";
			//int countNo = 0;

			switch (i) {
				case 0:
					// itemList[] を順に "0," と照合していく
					for (int j = 0; j < itemList.length; j++) {
						String item = itemList[j];
						String item0 = Character.toString(item.charAt(0));
						String item1 = Character.toString(item.charAt(1));
						item = item0 + item1; // "0,"

						// itemList[] の 頭文字(item) がもしも "0,"なら、その部分を "replaceText" と差し替える
						if (item.equals(0 + ",")) {
							Pattern p = Pattern.compile(0 + ",");
							Matcher m = p.matcher(itemList[j]);
							itemList[j] = m.replaceFirst(replaceText);
							Log.v("test", "itemList[" + 0 + "] = " + itemList[j]);
						}
					}
					break;

				case 1:
					// itemList[] を順に "1," と照合していく
					for (int j = 0; j < itemList.length; j++) {
						String item = itemList[j];
						String item0 = Character.toString(item.charAt(0));
						String item1 = Character.toString(item.charAt(1));
						item = item0 + item1; // "0,"

						// itemList[] の 頭文字(item) がもしも "0,"なら、その部分を "replaceText" と差し替える
						if (item.equals("1,")) {
							Pattern p = Pattern.compile(1 + ",");
							Matcher m = p.matcher(itemList[j]);
							itemList[j] = m.replaceFirst(replaceText);
							Log.v("test", "itemList[" + 1 + "] = " + itemList[j]);
						}
					}
					break;
			}
		}
	}

	@Override
	public int getCount() {
		//必ず実装
		// --- アイテム（header + listView）数
		Log.v("test", "getCount()");
		return itemList.length;
	}

	@Override
	public Object getItem(int pos) {
		//必ず実装
		// -> ヘッダーで分割する基準もここで作られる様子
		return itemList[pos];
	}

	@Override
	public long getItemId(int pos) {
		//必要に応じて実装
		return pos;
	}

	@Override
	public int getItemViewType(int pos) {
		//必要に応じて実装
		return 0;
	}

	class ViewHolder {
		TextView textView,headerText;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		Log.v("test", "getView, pos = " + pos);

		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.sticky_listview, parent, false);
			holder.textView = (TextView) convertView.findViewById(R.id.stickyText);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// ヘッダー分割の要素 "0," などは必要無いので外す -> ListViewの子ビューに表示
		String[] text = itemList[pos].split(",", 0) ;
		holder.textView.setText(text[1]);

		//普通に実装
		return convertView;
	}

	@Override
	public long getHeaderId(int pos) {
		//必ず実装
		//posはgetItemと同じ値
		// --- 各ヘッダーの位置などを決める？
		Log.v("test", "getHeaderId(), " + pos);
		return itemList[pos].subSequence(0,1).charAt(0);
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		Log.v("test", "getHeaderView(), " + position);

		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater infalInflater = (LayoutInflater) parent.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.sticky_header, null);
			holder.headerText = (TextView) convertView.findViewById(R.id.headerTitle);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		//必ず実装
		String[] text = itemList[position].split(",", 0);//.subSequence(0,1).charAt(0);
		holder.headerText.setText(text[0]);

		return convertView;
	}

	@Override
	public View getDropDownView(int pos, View convertView, ViewGroup parent) {
		Log.v("test", "getDropDownView, pos = " + pos);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) parent.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(android.R.layout.simple_spinner_dropdown_item, null);
		}
		//普通に実装
		return convertView;
	}

}