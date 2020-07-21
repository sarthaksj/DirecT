package org.openjfx.DirecT;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
<<<<<<< HEAD

import org.openjfx.DirecT.Commands.WindowsCommands;
=======
>>>>>>> db615d97ab4f3ea68278842cffd3528765cb25ac
import org.openjfx.DirecT.Controller.QrCode;
import org.openjfx.DirecT.FlowControl.DetailsJsonHandler;

   
public class App extends Application {

	private static Scene scene;

	@Override
	public void stop() {
		try {
			super.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Hello");
		// WindowsCommands.deleteWifiProfile();
		QrCode.deleteQr();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void start(Stage stage) throws SQLException, IOException {
		scene = new Scene(loadFXML("SplashScreen"));
		stage.setTitle("DirecT");
		stage.setScene(scene);
		stage.show();
		/*stage.setOnCloseRequest(e -> {
			try {
				Update.fetchUpdate();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		});*/

	}

	public static void setRoot(String fxml) throws IOException {
		scene.setRoot(loadFXML(fxml));
	}

	private static Parent loadFXML(String fxml) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/org/openjfx/DirecT/View/" + fxml + ".fxml"));
		return fxmlLoader.load();
	}

	public static void main(String[] args) throws SQLException {

		
		try {
			DetailsJsonHandler.userCount();//increase the count to the update
		} catch (Exception e) {
			
			System.out.println("Hello");
		}
		
		
		boolean shouldUpdate=false;
		//check if we should check for update
		try {
			 shouldUpdate=DetailsJsonHandler.shouldCheckForUpdate();
			 System.out.println("shouldUpdaete"+shouldUpdate);
		}catch(Exception e) {
			
		}
		
		//if to check
		if(shouldUpdate) {
			
			boolean updateAvailable=false;//returns true if update available 
			try {
				System.out.println("Helslsls");
				updateAvailable=DetailsJsonHandler.checkUpdateAvaialable();
				System.out.println("Update ava"+updateAvailable);
			} catch (Exception e) {
				
			}
		}
		//TODO write update login if update is available
		File f=new File("");
		System.out.println(f.getAbsolutePath());
		
		
		launch();
	}

}
