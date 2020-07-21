package org.openjfx.DirecT.Update;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.net.ssl.HttpsURLConnection;

import org.openjfx.DirecT.Database.DbConnection;

public class Update implements Runnable {

	private static String link;
	private static File out;

	private static File defaultdirectory = new File("");
	private static String pathname = (defaultdirectory.getAbsolutePath() + "\\temp.zip");

	public static void fetchUpdate() throws SQLException {

		link = Link();
		out = new File(pathname);
		new Thread(new Update(link, out)).start();

	}

	public Update(String link, File out) {
		this.link = link;
		this.out = out;
	}

	@Override
	public void run() {

		try {
			URL url = new URL(link);
			HttpsURLConnection http = (HttpsURLConnection) url.openConnection();
			double fileSize = (double) http.getContentLengthLong();
			BufferedInputStream in = new BufferedInputStream(http.getInputStream());
			FileOutputStream fos = new FileOutputStream(out);
			BufferedOutputStream bos = new BufferedOutputStream(fos, 1024);
			byte[] buffer = new byte[1024];
			double download = 0.00;
			int read = 0;
			double percentDownloaded = 0.00;
			while ((read = in.read(buffer, 0, 1024)) >= 0) {
				bos.write(buffer, 0, read);
				download += read;
				percentDownloaded = (download * 100) / fileSize;
				String percent = String.format("%.4f", percentDownloaded);
				System.out.println("Downloaded " + percent + "% of file");

			}
			bos.close();
			in.close();
			fos.close();
			System.out.println("Download Complete");
			
            //out.delete();
			//Unzip(pathname, (defaultdirectory.getAbsolutePath()));

			System.out.println("Update Complete");

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void Unzip(String zipFile, String extractFolder) {
		try {
			int BUFFER = 2048;
			File file = new File(zipFile);

			ZipFile zip = new ZipFile(file);
			String newPath = extractFolder;

			new File(newPath).mkdir();
			Enumeration zipFileEntries = zip.entries();

			// Process each entry
			while (zipFileEntries.hasMoreElements()) {
				// grab a zip file entry
				ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
				String currentEntry = entry.getName();

				File destFile = new File(newPath, currentEntry);
				// destFile = new File(newPath, destFile.getName());
				File destinationParent = destFile.getParentFile();

				// create the parent directory structure if needed
				destinationParent.mkdirs();

				if (!entry.isDirectory()) {
					BufferedInputStream is = new BufferedInputStream(zip.getInputStream(entry));
					int currentByte;
					// establish buffer for writing file
					byte data[] = new byte[BUFFER];

					// write the current file to disk
					FileOutputStream fos = new FileOutputStream(destFile);
					BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);

					// read and write until last byte is encountered
					while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
						dest.write(data, 0, currentByte);
					}
					dest.flush();
					dest.close();
					is.close();
					fos.close();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String Link() throws SQLException {
		Connection con;
		Statement st;
		String link = null;
		String query = "select link from users";
		con = DbConnection.databaseConnectivity();
		st = con.createStatement();
		ResultSet rs = st.executeQuery(query);
		while (rs.next()) {
			link = rs.getString(1);
			System.out.println("link " + link);
		}
		con.close();
		st.close();
		rs.close();
		return link;
	}

	public static void main(String[] args) throws SQLException {
		fetchUpdate();
	}

}
