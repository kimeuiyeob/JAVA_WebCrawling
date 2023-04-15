package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnecter {

	public static Connection getConnection() {
		Connection connection = null;

		try {
			String userName = "root";
			String password = "1234";
			String url = "jdbc:mysql://localhost:3306/project";
			Class.forName("com.mysql.cj.jdbc.Driver");

			connection = DriverManager.getConnection(url, userName, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("연결 실패");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("알 수 없는 오류");
		}
		return connection;
	}
}
