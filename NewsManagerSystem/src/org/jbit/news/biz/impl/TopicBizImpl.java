package org.jbit.news.biz.impl;

import java.util.List;
import java.util.Map;

import org.jbit.news.biz.TopicBiz;
import org.jbit.news.dao.TopicsDao;

public class TopicBizImpl implements TopicBiz {

	TopicsDao td;
	
	public TopicsDao getTd() {
		return td;
	}

	public void setTd(TopicsDao td) {
		this.td = td;
	}

	public int addTopic(Map topic) {
		return td.addTopic(topic);
	}

	public int deleteTopic(String tid) {
		return td.deleteTopic(tid);
	}

	public Map findTopicByTname(String tname) {
		return td.getTopicByTname(tname);
	}

	public List findTopicsByPage(int page_info_no, int page_no) {
		if(page_info_no == 0){
			return td.getAllTopics();
		}else
			return td.getAllTopicsByPage(page_info_no, page_no);
	}

	public int updateTopic(Map topic) {
		return td.updateTopic(topic);
	}

	public int countTopics() {
		return td.countTopics();
	}

}
