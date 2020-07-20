package org.openjfx.DirecT.Database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnection {
	static String jdbcUrl = "jdbc:mysql://localhost:3306/test";
	static String user = "root";
	static String pass = "sarthak1234";
	static Connection myConn = null;

	public static Connection databaseConnectivity() {
		String driverName = "com.mysql.cj.jdbc.Driver";
		try {
			Class.forName(driverName);
			System.out.println("Connecting To Database: " + jdbcUrl);

			myConn = DriverManager.getConnection(jdbcUrl, user, pass);

			System.out.println("Connection Successful!: " + myConn);

		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return myConn;
	}
}
