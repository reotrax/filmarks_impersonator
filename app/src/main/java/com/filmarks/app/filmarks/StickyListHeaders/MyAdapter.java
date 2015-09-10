//package com.filmarks.app.filmarks.StickyListHeaders;
//
//import android.content.Context;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//
//import com.filmarks.app.filmarks.R;
//
///**
// * Created by user on 15/07/30.
// */
//public class MyAdapter extends ArrayAdapter<String> implements StickyListHeadersAdapter {
//
//	private final LayoutInflater inflater;
//	private String[] headerList = {"映画を探す","ユーザーを探す"};
//
//	public MyAdapter(Context context) {
//		super(context,  android.R.layout.simple_list_item_1);
//		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//	}
//
//	@Override
//	public long getHeaderId(int position) {
//		// position = リストビューで表示されているTopのposition
//		// ---> つまりスクロールする毎に呼ばれる
//
//		// return には、指定された位置にあるヘッダIDを返すらしい
//		return 0;//itemCount[position];
//	}
//
//	@Override
//	public View getHeaderView(int position, View convertView, ViewGroup parent) {
//		if (convertView==null) {
//			convertView = inflater.inflate(R.layout.sticky_header, null);
//		}
//
//		TextView header = (TextView) convertView.findViewById(R.id.headerTitle);
//		header.setText(headerList[position]);
//
//		return convertView;
//	}
//}
