package org.jbit.news.dao.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbit.news.dao.BaseDao;
import org.jbit.news.dao.NewsDao;

public class NewsDaoImpl extends BaseDao implements NewsDao {

	public List getAllnews(){
		openConnection();
		List list = new ArrayList();		
		String sql = "select *"
			+ " from news,topic"
			+ " where news.ntid = topic.tid"
			+ " order by ntid,ncreatedate desc";
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm");
		try{			
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();	
			while(rs.next()){
				Map news = new HashMap();
				news.put("ncontent", rs.getString("ncontent"));				
				news.put("nsummary", rs.getString("nsummary"));	
				news.put("nid", String.valueOf(rs.getObject("nid")));
				news.put("ntid", String.valueOf(rs.getObject("ntid")));
				news.put("ntitle", rs.getString("ntitle"));
				news.put("nauthor", rs.getString("nauthor"));
				news.put("tname", rs.getString("tname"));				
				news.put("ncreatedate", sdf.format(rs.getObject("ncreatedate")));
				news.put("npicpath", rs.getString("npicpath"));				
				list.add(news);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeResource();
		}
		return list;
	}

	public List getNewsByType(int page_info_no, int page_no){
		openConnection();
		List list = new ArrayList();
		String sql = "select * "
			+ "from (select b.*, rownum r"
			+ " from (select * from news,topic where news.ntid = topic.tid order by news.ncreatedate desc) b "
			+ " where rownum < " + (page_info_no*(page_no)) + ") t "
			+ " where t.r >" +  (page_info_no*(page_no-1));		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm");
		try{			
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();	
			while(rs.next()){
				Map news = new HashMap();
				news.put("ncontent", rs.getString("ncontent"));			
				news.put("nsummary", rs.getString("nsummary"));
				news.put("nid", String.valueOf(rs.getObject("nid")));
				news.put("ntid", String.valueOf(rs.getObject("ntid")));
				news.put("ntitle", rs.getString("ntitle"));
				news.put("nauthor", rs.getString("nauthor"));
				news.put("tname", rs.getString("tname"));
				news.put("ncreatedate", sdf.format(rs.getObject("ncreatedate")));
				news.put("npicpath", rs.getString("npicpath"));					
				list.add(news);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeResource();
		}
		return list;
	}
	
	public List getAllnewsByPage(int page_info_no, int page_no){
		openConnection();
		List list = new ArrayList();
		String sql = "select * "
					+ "from (select b.*, rownum r"
					+ " from (select * from news,topic where news.ntid = topic.tid order by news.ncreatedate desc) b "
					+ " where rownum < " + (page_info_no*(page_no)) + ") t "
					+ " where t.r >" +  (page_info_no*(page_no-1));		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm");
		try{			
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();	
			while(rs.next()){
				Map news = new HashMap();
				news.put("ncontent", rs.getString("ncontent"));
				news.put("nsummary", rs.getString("nsummary"));	
				news.put("nid", String.valueOf(rs.getObject("nid")));
				news.put("ntid", String.valueOf(rs.getObject("ntid")));
				news.put("ntitle", rs.getString("ntitle"));
				news.put("nauthor", rs.getString("nauthor"));
				news.put("tname", rs.getString("tname"));				
				news.put("ncreatedate", rs.getObject("ncreatedate"));
				news.put("npicpath", rs.getString("npicpath"));	
							
				list.add(news);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeResource();
		}
		return list;
	}

	public List getAllnewsByPageAndTid(int page_info_no, int page_no, String tid){
		openConnection();
		List list = new ArrayList();
		
		String sql = "select * "
			+ "from (select b.*, rownum r"
			+ " from (select * from news,topic where news.ntid = topic.tid and tid = " + tid + " order by news.ncreatedate desc) b "
			+ " where rownum < " + (page_info_no*(page_no)) + ") t "
			+ " where t.r >" +  (page_info_no*(page_no-1));
		
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm");
		try{			
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();	
			while(rs.next()){
				Map news = new HashMap();
				news.put("ncontent", rs.getString("ncontent"));				
				news.put("nsummary", rs.getString("nsummary"));
				news.put("nid", String.valueOf(rs.getObject("nid")));
				news.put("ntid", String.valueOf(rs.getObject("ntid")));
				news.put("ntitle", rs.getString("ntitle"));
				news.put("nauthor", rs.getString("nauthor"));
				news.put("tname", rs.getString("tname"));
				news.put("ncreatedate", sdf.format(rs.getObject("ncreatedate")));
				news.put("npicpath", rs.getString("npicpath"));				
				list.add(news);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeResource();
		}
		return list;
	}

	public int countNews(){
		openConnection();
		int count = 0;
		String sql = "select count(*)"
			+ " from news";
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

	public Map getNewsByNid(String nid){
		openConnection();
		Map news = null;
		String sql = "select *"
			+ " from news,topic"
			+ " where news.ntid = topic.tid and nid = "
			+ nid;
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm");
		try{			
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();			
			while(rs.next()){
				news = new HashMap();
				news.put("ncontent", rs.getString("ncontent"));
				news.put("nsummary", rs.getString("nsummary"));
				news.put("nid", String.valueOf(rs.getObject("nid")));
				news.put("ntid", String.valueOf(rs.getObject("ntid")));
				news.put("ntitle", rs.getString("ntitle"));
				news.put("nauthor", rs.getString("nauthor"));
				news.put("tname", rs.getString("tname"));
				news.put("ncreatedate", sdf.format(rs.getObject("ncreatedate")));
				news.put("npicpath", rs.getString("npicpath"));				
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeResource();
		}
		return news;
	}

	public List getNewByTid(String tid){
		openConnection();
		List list = new ArrayList();
		String sql = "select *"
			+ " from news,topic"
			+ " where news.ntid = topic.tid and topic.tid = "
			+ tid
			+ " order by ncreatedate desc";
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm");
		try{			
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();	
			while(rs.next()){
				Map news = new HashMap();
				news.put("ncontent", rs.getString("ncontent"));
				news.put("nsummary", rs.getString("nsummary"));		
				news.put("nid", String.valueOf(rs.getObject("nid")));
				news.put("ntid", String.valueOf(rs.getObject("ntid")));
				news.put("ntitle", rs.getString("ntitle"));
				news.put("nauthor", rs.getString("nauthor"));
				news.put("tname", rs.getString("tname"));				
				news.put("ncreatedate", sdf.format(rs.getObject("ncreatedate")));
				news.put("npicpath", rs.getString("npicpath"));		
				list.add(news);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeResource();
		}
		return list;
	}
	
	public int updateNews(Map news) {
		openConnection();
		String sql = "update news set ntid=?, ntitle=?, nauthor=?, npicpath=?, ncontent=?, nmodifydate=?, nsummary=? "
					+ "where nid = " + news.get("nid");
		int update_rows = 0;
		try {
			java.util.Date date = new java.util.Date();
			Timestamp ts = new Timestamp(date.getTime());
			ps = con.prepareStatement(sql);			
			ps.setObject(1, news.get("ntid"));
			ps.setObject(2, news.get("ntitle"));
			ps.setObject(3, news.get("nauthor"));
			if(news.get("filePath") == null)
				ps.setObject(4, "");
			else
				ps.setObject(4, news.get("filePath"));
			String ncontent = (String) news.get("ncontent");
			ps.setCharacterStream(5, new InputStreamReader(new ByteArrayInputStream(ncontent.getBytes())),ncontent.length());
			ps.setObject(6, ts);
			ps.setObject(7, news.get("nsummary"));	
			
			update_rows = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeResource();
		}
		return update_rows;
	}

	public int addNews(Map news) {
		openConnection();
		java.util.Date date = new java.util.Date();
		Timestamp ts = new Timestamp(date.getTime());
		int add_rows = 0;
		String sql = "insert into news values(nid_seq.nextval,?,?,?,?,?,?,?,?)";
		try {
			ps = con.prepareStatement(sql);
			ps.setObject(1,news.get("ntid"));
			ps.setObject(2,news.get("ntitle"));
			ps.setObject(3,news.get("nauthor"));
			ps.setObject(4,ts);
			if(news.get("filePath") == null)
				ps.setObject(5, "");
			else
				ps.setObject(5, news.get("filePath"));
			String ncontent = (String) news.get("ncontent");
			ps.setCharacterStream(6, new InputStreamReader(new ByteArrayInputStream(ncontent.getBytes())),ncontent.length());
			ps.setObject(7,"");
			ps.setObject(8,news.get("nsummary"));
			add_rows = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeResource();
		}
		return add_rows;
	}

	public int deleteNews(String nid) {
		openConnection();
		String sql = "delete from news where nid = " + nid;		
		return executDelete(sql);
	}
}
