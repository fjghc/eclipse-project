package org.jbit.news.dao;

import java.util.List;
import java.util.Map;

public interface CommentsDao{
	//通过nid查找评论
	public List getCommentsByNid(String nid);
	//通过cid删除评论
	public int deleteComment(String cid);
	//返回评论总数
	public int countComments(String nid);
	//添加评论
	public int addComment(Map comment);
}
