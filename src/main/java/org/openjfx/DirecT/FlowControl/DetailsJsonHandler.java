package org.openjfx.DirecT.FlowControl;

import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openjfx.DirecT.Database.DbConnection;

public class DetailsJsonHandler {
	Connection con;
	Statement st;

	public static void increaseCount() {

		JSONParser jsonParser = new JSONParser();
		try {

			FileReader fileReader = new FileReader("src//main//resources//org//openjfx//Details//details.json");

			Object obj = jsonParser.parse(fileReader);

			JSONObject object = (JSONObject) obj;

			int count = Integer.parseInt((String) object.get("count"));
			if (count != 0) {
			}
			count++;
			fileReader.close();
			FileWriter fileWriter = new FileWriter("src//main//resources//org//openjfx//Details//details.json");

			String c = Integer.toString(count);
			object.put("count", c);
			fileWriter.write(object.toString());
			fileWriter.close();
		} catch (Exception e) {
			System.out.println("Exception inside checkCount while accessing details.json file");
			e.printStackTrace();

		}

	}

	public static boolean ifFirstTime() {
		// return true if app opens first time

		JSONParser jsonParser = new JSONParser();
		boolean firstTime = true;
		try {

			FileReader fileReader = new FileReader("src//main//resources//org//openjfx//Details//details.json");

			Object obj = jsonParser.parse(fileReader);

			JSONObject object = (JSONObject) obj;

			int count = Integer.parseInt((String) object.get("count"));
			if (count != 0) {
				firstTime = false;
			}

			fileReader.close();
		} catch (Exception e) {
			System.out.println("Exception inside checkCount while accessing details.json file");
			e.printStackTrace();

		}

		return firstTime;

	}

	public static String getName() {

		JSONParser jsonParser = new JSONParser();
		String name = "";
		try {

			FileReader fileReader = new FileReader("src//main//resources//org//openjfx//Details//details.json");

			Object obj = jsonParser.parse(fileReader);

			JSONObject object = (JSONObject) obj;

			int count = Integer.parseInt((String) object.get("count"));
			name = (String) object.get("name");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return name;

	}

	public static void setName(String name) {

		// capitalize first alphabet of the name
		char ch = name.charAt(0);
		if ((int) ch > 96) {
			ch = (char) ((int) ch - 32);
		}

		name = ch + name.substring(1);
		JSONParser jsonParser = new JSONParser();
		boolean firstTime = true;
		try {

			FileReader fileReader = new FileReader("src//main//resources//org//openjfx//Details//details.json");

			Object obj = jsonParser.parse(fileReader);

			JSONObject object = (JSONObject) obj;

			fileReader.close();

			FileWriter fileWriter = new FileWriter("src//main//resources//org//openjfx//Details//details.json");

			object.put("name", name);
			fileWriter.write(object.toString());
			fileWriter.close();

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	public static void userCount() throws SQLException {
		Connection con;
		Statement st;
		String query = "update users set activeUserCount = activeUserCount+1";
		con = DbConnection.databaseConnectivity();
		st = con.createStatement();
		int rs = st.executeUpdate(query);
		
		con.close();
		st.close();
	}

	public static int versionCheck() throws SQLException {
		Connection con;
		Statement st;
		int version = 0;
		String query = "select version from users";
		con = DbConnection.databaseConnectivity();
		st = con.createStatement();
		ResultSet rs = st.executeQuery(query);
		while (rs.next()) {
			version = rs.getInt(1);
			System.out.println("Version " + version);
		}
		con.close();
		st.close();
		rs.close();
		return version;
		
	}

	public static void main(String[] args) throws SQLException {
		 //userCount();
		versionCheck();
	}

}
