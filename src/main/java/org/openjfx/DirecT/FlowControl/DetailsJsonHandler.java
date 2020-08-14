package org.openjfx.DirecT.FlowControl;

import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openjfx.DirecT.Database.DbConnection;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class DetailsJsonHandler {
	private static Connection con = DbConnection.databaseConnectivity();
	private static Statement st;
	
	public static int updateVersion;
	public static void increaseCount() {

		JSONParser jsonParser = new JSONParser();
		try {

			FileReader fileReader = new FileReader("Details//details.json");

			Object obj = jsonParser.parse(fileReader);

			JSONObject object = (JSONObject) obj;

			int count = Integer.parseInt((String) object.get("count"));
			if (count != 0) {
			}
			count++;
			fileReader.close();
			FileWriter fileWriter = new FileWriter("Details//details.json");

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

			FileReader fileReader = new FileReader("Details//details.json");

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

			FileReader fileReader = new FileReader("Details//details.json");

			Object obj = jsonParser.parse(fileReader);

			JSONObject object = (JSONObject) obj;

			int count = Integer.parseInt((String) object.get("count"));
			name = (String) object.get("name");
			fileReader.close();

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

			FileReader fileReader = new FileReader("Details//details.json");

			Object obj = jsonParser.parse(fileReader);

			JSONObject object = (JSONObject) obj;

			fileReader.close();

			FileWriter fileWriter = new FileWriter("Details//details.json");

			object.put("name", name);
			fileWriter.write(object.toString());
			fileWriter.close();

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	public static void userCount() throws SQLException {
		String query = "update users set activeusers = activeusers+1";
		st = con.createStatement();
		int rs = st.executeUpdate(query);
		st.close();
	}

	public static int versionCheck() throws SQLException {

		int version = 0;
		String query = "select version from users";
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

	public static boolean shouldCheckForUpdate() {// checks for update every 7th day
		// return true if to be checked

		JSONParser jsonParser = new JSONParser();
		String name = "";
		boolean check = false;
		try {

			FileReader fileReader = new FileReader("Details//details.json");

			Object obj = jsonParser.parse(fileReader);

			JSONObject object = (JSONObject) obj;

			String date = (String) object.get("lastChecked");

			int t1 = Integer.parseInt(date);
			Date d = new Date();
			int t2 = d.getDate();
			if (t1 - t2 > 6 || t1 - t2 < -6) {
				check = true;
			} else {
				fileReader.close();

				return false;
			}

			fileReader.close();

			FileWriter fileWriter = new FileWriter("Details//details.json");

			object.put("lastChecked", Integer.toString(t2));
			fileWriter.write(object.toString());
			fileWriter.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return check;

	}

	public static int getCurrentVesion() {
		JSONParser jsonParser = new JSONParser();
		String version = "";
		boolean check = false;
		try {

			FileReader fileReader = new FileReader("Details//details.json");

			Object obj = jsonParser.parse(fileReader);

			JSONObject object = (JSONObject) obj;

			version = (String) object.get("version");
			fileReader.close();

		} catch (Exception e) {

		}

		return Integer.parseInt(version);
	}

	public static void setUpdateAvailableTrue() {

		JSONParser jsonParser = new JSONParser();
		String name = "";
		boolean check = false;
		try {

			FileReader fileReader = new FileReader("Details//details.json");

			Object obj = jsonParser.parse(fileReader);

			JSONObject object = (JSONObject) obj;

			String date = (String) object.get("updateAvailable");

			fileReader.close();

			FileWriter fileWriter = new FileWriter("Details//details.json");

			object.put("updateAvailable", "true");
			fileWriter.write(object.toString());
			fileWriter.close();

		} catch (Exception e) {
		}

	}

	public static void setNewVersion() {
		
		JSONParser jsonParser = new JSONParser();
		try {

			FileReader fileReader = new FileReader("Details//details.json");

			Object obj = jsonParser.parse(fileReader);

			JSONObject object = (JSONObject) obj;

			fileReader.close();

			FileWriter fileWriter = new FileWriter("Details//details.json");

			object.put("version", Integer.toString(updateVersion));
			fileWriter.write(object.toString());
			fileWriter.close();

		} catch (Exception e) {
			e.printStackTrace();

		}
		
	}
	public static boolean checkUpdateAvaialable() throws Exception {

		int v1 = versionCheck();
		int v2 = getCurrentVesion();
		updateVersion=v1;
		System.out.println("isnide check update");
		if (v1 != v2) {
			System.out.println("true");

			return true;
		}
		System.out.println("false");

		return false;

	}

	public static void main(String[] args) throws SQLException {

	}

}