package lesson.j2ee.ex3.model;

//用于使用JDBC访问Access数据库的工具类
import java.sql.*;

public class AccessJDBCUtil {
	private static final String accessDBURLPrefix = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
    private static final String accessDBURLSuffix = ";DriverID=22;READONLY=true}";
    static {
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        } catch(ClassNotFoundException e) {
            System.err.println("JdbcOdbc Bridge Driver not found!");
            
        }
    }
    
    /** 建立到Access数据库的连接对象 */
    public static java.sql.Connection getAccessDBConnection(String filename) throws SQLException {
        filename = filename.trim();
        String databaseURL = accessDBURLPrefix + filename + accessDBURLSuffix;
        return DriverManager.getConnection(databaseURL, "", "");
    }  
}