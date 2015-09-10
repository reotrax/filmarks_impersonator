package com.filmarks.app.filmarks.definition;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.filmarks.app.filmarks.R;

import java.util.ArrayList;

/**
 * Created by user on 15/05/27.
 */
public class CustomListExAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater = null;
	private LayoutInflater mInflater;
	private InItem sLists;

	public CustomListExAdapter(Context context, InItem sLists) {
		this.mInflater = LayoutInflater.from(context);
		this.sLists = sLists;
		this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return sLists.getItem().length;
	}

	@Override
	public Object getItem(int position) {
		return sLists.getTitle();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView==null) {
			convertView = mInflater.inflate(R.layout.custom_ex_list, null);
		}

		InItem item = sLists;

		ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
		TextView title 		= (TextView) convertView.findViewById(R.id.title);
		TextView sub_title 	= (TextView) convertView.findViewById(R.id.sub);
		ImageView yajirushi = (ImageView) convertView.findViewById(R.id.yajirushi);

		// ビューの表示内容を設定
//		try {
//			imageView.setImageBitmap(item.getBitmaps()[position]);
//		} catch (ArrayIndexOutOfBoundsException e) {
			imageView.setVisibility(View.GONE);
//		}
		title.setText(item.getItem()[position]);
		sub_title.setText(item.getSub()[position]);
		//yajirushi.setImageBitmap();

		return convertView;
	}
}
