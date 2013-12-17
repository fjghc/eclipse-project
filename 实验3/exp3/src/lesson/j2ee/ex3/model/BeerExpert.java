package lesson.j2ee.ex3.model;

import java.util.*;
import java.sql.*;

public class BeerExpert {
	public List getBrands(String rootpath, String color) {
		Connection conn = null;
		List brands = new ArrayList();
		try {
			conn = AccessJDBCUtil.getAccessDBConnection(rootpath+"db/ex3.mdb");
			System.out.println(rootpath);
			Statement stmt = conn.createStatement();
			
			if (color.equals("light")) {
				String queryString = "select name from beer where color = 'light'";
				ResultSet rs = stmt.executeQuery(queryString);
				while (rs.next()) {
					brands.add(rs.getString(1));
				}
			} else if (color.equals("amber")) {
				String queryString = "select name from beer where color = 'amber'";
				ResultSet rs = stmt.executeQuery(queryString);
				while (rs.next()) {
					brands.add(rs.getString(1));
				}
			} else if (color.equals("brown")) {
				String queryString = "select name from beer where color = 'brown'";
				ResultSet rs = stmt.executeQuery(queryString);
				while (rs.next()) {
					brands.add(rs.getString(1));
				}
			} else {
				String queryString = "select name from beer where color = 'dark'";
				ResultSet rs = stmt.executeQuery(queryString);
				while (rs.next()) {
					brands.add(rs.getString(1));
				}
			}
			return (brands);
		} catch (SQLException s) {
			System.out.println(s);	
			return null;
		} finally {
			if (conn != null) {
				try {					
					conn.close();					
				} catch (SQLException ignore) {					
				}
			}
		}
	}
}
