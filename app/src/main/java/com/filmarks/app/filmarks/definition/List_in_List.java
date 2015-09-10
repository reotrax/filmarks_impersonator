package com.filmarks.app.filmarks.definition;

import java.util.ArrayList;

/**
 * Created by user on 15/06/01.
 */
public class List_in_List {

	private String header;
	private ArrayList<String> list;

	public List_in_List(String header, ArrayList<String> list) {
		this.header = header;
		this.list = list;
	}

	public String getHeader() {
		return header;
	}
	public List_in_List setHeader(String header) {
		this.header = header;
		return this;
	}
	public ArrayList<String> getList() {
		return list;
	}
	public List_in_List setList(ArrayList<String> list) {
		this.list = list;
		return this;
	}
}
