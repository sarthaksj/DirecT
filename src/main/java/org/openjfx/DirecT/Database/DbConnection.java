package org.openjfx.DirecT.Database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnection {

	public static Connection databaseConnectivity(){

		String jdbcUrl = "jdbc:mysql://localhost:3306/test";
		String user = "root";
		String pass = "sarthak1234";
        String driverName="com.mysql.cj.jdbc.Driver";
        Connection myConn = null;
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
