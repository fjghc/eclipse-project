package com.example.dataobj;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 景点图片的类集合
 * 
 * @author fan
 * @version 0.1
 */
@SuppressLint("ParserError")
public class SpotPhotos implements Iterable<HashMap<String, String>> {

	public SpotPhotos() {
		super();
		photos = new ArrayList<HashMap<String, String>>();
	}

	private ArrayList<HashMap<String, String>> photos;

	public void addPhoto(String picpath, String picname) {
		HashMap<String, String> photomaps = new HashMap<String, String>();
		photomaps.put("picpath", picpath);
		photomaps.put("picname", picname);
		this.photos.add(photomaps);
	}

	public String getPhotoName(String picpath) {
		for (HashMap<String, String> photomap : photos) {
			if (photomap.get("picpath").equals(picpath)) {
				return photomap.get("picname");
			}
		}
		return null;
	}

	/**
	 * 重载方法
	 * 
	 * @param picpath
	 * @return
	 */
	public String getPhotoName(int picposition) {
		if (picposition < photos.size() && picposition >= 0)
			return photos.get(picposition).get("picname");
		return null;
	}

	public int getNumOfPhotos() {
		return photos.size();
	}

	@Override
	public Iterator<HashMap<String, String>> iterator() {
		return photos.iterator();
	}

}
