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
			System.out.println("����̹� �ε� ����");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("���� ����");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("�� �� ���� ����");
		}
		return connection;
	}
}
