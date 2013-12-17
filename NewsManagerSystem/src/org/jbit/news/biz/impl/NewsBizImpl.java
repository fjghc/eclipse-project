package org.jbit.news.biz.impl;

import java.util.List;
import java.util.Map;

import org.jbit.news.biz.NewsBiz;
import org.jbit.news.dao.NewsDao;


public class NewsBizImpl implements NewsBiz {

	private NewsDao nd;
	public NewsDao getNd() {
		return nd;
	}

	public void setNd(NewsDao nd) {
		this.nd = nd;
	}

	public int addNews(Map news) {
		return nd.addNews(news);
	}

	public int deleteNews(String nid) {
		return nd.deleteNews(nid);
	}

	public Map findNewsByNid(String nid) {
		return nd.getNewsByNid(nid);
	}

	public List findNewsByPage(int page_info_no, int page_no, String tid) {
		if(page_info_no == 0){
			if(tid == null || tid.equals(""))
				return nd.getAllnews();
			else
				return nd.getNewByTid(tid);
		}else{
			if(tid == null || tid.equals(""))
				return nd.getAllnewsByPage(page_info_no, page_no);
			else if(tid.equals("-getTypes"))
				return nd.getNewsByType(page_info_no, page_no);
			else
				return nd.getAllnewsByPageAndTid(page_info_no, page_no, tid);
		}
	}

	public int updateNews(Map news) {
		return nd.updateNews(news);
	}

	public int countNews() {
		return nd.countNews();
	}

}
