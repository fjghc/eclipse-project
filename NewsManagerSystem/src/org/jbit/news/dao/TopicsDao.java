package org.jbit.news.dao;

import java.util.List;
import java.util.Map;

public interface TopicsDao{
	//获取所有主题
	public List getAllTopics();
	//通过tid获取主题
	public Map getTopicByTname(String tname);
	//通过tid删除主题
	public int deleteTopic(String tid);
	//通过分页获取所有主题
	public List getAllTopicsByPage(int page_info_no, int page_no);
	//返回主题数目
	public int countTopics();
	//更新主题
	public int updateTopic(Map topic);
	//添加主题
	public int addTopic(Map topic);
}