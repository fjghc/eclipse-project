package com.example.dataobj;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 景点图片的类集合
 * @author fan
 * @version 0.1
 */
@SuppressLint("ParserError")
public class SpotCommentsList implements Iterable<HashMap<String, String>> {

	public SpotCommentsList() {
		super();
		comments = new ArrayList<HashMap<String, String>>();
	}

	private ArrayList<HashMap<String, String>> comments;
	
	public ArrayList<HashMap<String, String>> getComments() {
		return comments;
	}

	public void addTouristComments(String name,String time, String content) {
		HashMap<String, String> commentmaps = new HashMap<String, String>();
		commentmaps.put("name", name);
		commentmaps.put("time", time);
		commentmaps.put("content", content);
		this.comments.add(commentmaps);
	}
	
	public int getNumOfComments() {
		return comments.size();
	}
	
	@Override
	public Iterator<HashMap<String, String>> iterator() {
		return comments.iterator();
	}

}
