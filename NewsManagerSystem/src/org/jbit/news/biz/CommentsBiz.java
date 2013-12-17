package org.jbit.news.biz;

import java.util.List;
import java.util.Map;

public interface CommentsBiz {
	public List findCommentsByNid(String nid);
	public int deleteComment(String cid);
	public int countComments(String nid);
	public int addComment(Map comment);
}
