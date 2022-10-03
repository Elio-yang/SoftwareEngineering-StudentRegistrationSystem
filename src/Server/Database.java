package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
	//TODO: modify the socket and passwd here

	// build a system based on mysql-8
	// create db called admininstration_system

	private static String dbOldUrl = "jdbc:mysql://xxxx/course_catalog";
	private static String dbNewUrl = "jdbc:mysql://xxxx/admininstration_system";
	private static String dbUser = "root";
	private static String dbPwd = "root";
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	// 获得一个旧数据库连接
	public static Connection getOldConnection() throws SQLException {
		System.out.println("连接数据库...");
		return DriverManager.getConnection(dbOldUrl, dbUser, dbPwd);
	}

	// 获得一个新数据库连接
	public static Connection getNewConnection() throws SQLException {
		System.out.println("连接数据库...");
		return DriverManager.getConnection(dbNewUrl, dbUser, dbPwd);
	}

	// 关闭数据库连接
	public static void freeDB(ResultSet rs, Statement st, Connection conn) throws SQLException {

		if (rs != null)
			rs.close();
		if (st != null)
			st.close();
		if (conn != null)
			conn.close();
		System.out.println("关闭数据库");
	}
}
