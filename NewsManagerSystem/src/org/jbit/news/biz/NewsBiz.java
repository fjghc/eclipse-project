package org.jbit.news.biz;

import java.util.List;
import java.util.Map;

import org.jbit.news.dao.NewsDao;

public interface NewsBiz {
	public void setNd(NewsDao nd);
	public NewsDao getNd();
	public List findNewsByPage(int page_info_no, int page_no, String tid);
	public Map findNewsByNid(String nid);
	public int addNews(Map news);
	public int updateNews(Map news);
	public int deleteNews(String nid);
	public int countNews();
}
