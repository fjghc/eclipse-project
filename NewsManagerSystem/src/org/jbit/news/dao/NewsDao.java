package org.jbit.news.dao;

import java.util.List;
import java.util.Map;

public interface NewsDao{
	//获取所有新闻
	public List getAllnews();
	//通过分页获取所有新闻
	public List getAllnewsByPage(int page_info_no, int page_no);
	//通过分页获取选择主题下的所有新闻
	public List getAllnewsByPageAndTid(int page_info_no, int page_no, String tid);
	//返回新闻数目
	public int countNews();
	//通过新闻id获取新闻
	public Map getNewsByNid(String nid);
	//获取选择主题下所有新闻
	public List getNewByTid(String tid);
	//更新选择的新闻
	public int updateNews(Map news);
	//添加新闻
	public int addNews(Map news);
	//删除新闻
	public int deleteNews(String nid);
	//按照主题显示新闻
	public List getNewsByType(int page_info_no, int page_no);
}
