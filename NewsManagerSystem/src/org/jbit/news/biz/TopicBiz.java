package org.jbit.news.biz;

import java.util.List;
import java.util.Map;

import org.jbit.news.dao.TopicsDao;

public interface TopicBiz {
	public Map findTopicByTname(String tname);
	public List findTopicsByPage(int page_info_no, int page_no);
	public int addTopic(Map topic);
	public int updateTopic(Map topic);
	public int deleteTopic(String tid);
	public int countTopics();
	public TopicsDao getTd();
	public void setTd(TopicsDao td);
}
