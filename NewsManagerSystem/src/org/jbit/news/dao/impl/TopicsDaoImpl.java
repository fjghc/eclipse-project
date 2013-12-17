package org.jbit.news.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbit.news.dao.BaseDao;
import org.jbit.news.dao.TopicsDao;

public class TopicsDaoImpl extends BaseDao implements TopicsDao {

	public int countTopics() {
		openConnection();
		int count = 0;
		String sql = "select count(*)"
			+ " from topic";
		try{			
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();	
			while(rs.next()){
				count = rs.getInt(1);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeResource();
		}
		return count;
	}
	
	public List getAllTopics(){
		openConnection();
		List list = new ArrayList();
		String sql = "select * from topic";
		try{			
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();	
			while(rs.next()){
				Map topic = new HashMap();
				topic.put("tid", rs.getString("tid"));
				topic.put("tname", rs.getString("tname"));								
				list.add(topic);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeResource();
		}
		return list;
	}

	public int deleteTopic(String tid) {
		String sql = "delete from news where ntid = " + tid;
		executDelete(sql);
		sql = "delete from topic where tid=" + tid;		
		return executDelete(sql);
	}

	public List getAllTopicsByPage(int page_info_no, int page_no) {
		openConnection();
		List list = new ArrayList();		
		/*String sql = "select top " + page_info_no +" *"
					+ " from topic "
					+ " where tid not in"
					+ " (select top " + ((page_no-1) * page_info_no)
						+ " tid from topic)";*/
		String sql = "select * from "
					+ "(select topic.*,rownum r from topic where rownum < " + page_info_no*page_no +") t "
					+ "where t.r > " + page_info_no*(page_no-1);
		try{			
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();	
			while(rs.next()){
				Map topic = new HashMap();
				topic.put("tid", String.valueOf(rs.getObject("tid")));
				topic.put("tname", String.valueOf(rs.getObject("tname")));
				list.add(topic);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeResource();
		}
		return list;
	}

	
	public int addTopic(Map topic) {
		openConnection();
		int add_rows = 0;
		String sql = "insert into topic values(?,?)";
		try {
			ps = con.prepareStatement(sql);
			ps.setObject(1, "");
			ps.setObject(2, topic.get("tname"));
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeResource();
		}
		return add_rows;
	}

	public int updateTopic(Map topic) {
		openConnection();
		String sql = "update topic set tname=? "
			+ "where tid = " + topic.get("tid");
		int update_rows = 0;
		try {
			ps = con.prepareStatement(sql);
			ps.setObject(1, topic.get("tname"));
			update_rows = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeResource();
		}
		return update_rows;
	}

	public Map getTopicByTname(String tname) {
		openConnection();
		Map topic = null;		
		String sql = "select * from topic"
					+ " where tname = '"
					+ tname + "'";
		try{			
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();			
			while(rs.next()){
				topic = new HashMap();
				topic.put("tid", String.valueOf(rs.getObject("tid")));		
				topic.put("tname", String.valueOf(rs.getObject("tname")));
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeResource();
		}
		return topic;
	}
}
