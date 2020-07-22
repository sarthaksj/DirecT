package org.openjfx.DirecT;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.openjfx.DirecT.Commands.WindowsCommands;

import org.openjfx.DirecT.Controller.QrCode;
import org.openjfx.DirecT.FlowControl.DetailsJsonHandler;
import org.openjfx.DirecT.Update.Update;


class checkUpdate implements Runnable{

	@Override
	public void run() {
		// TODO Auto-generated method stub

		try {
			DetailsJsonHandler.userCount();//increase the count to the update
		} catch (Exception e) {
			
			System.out.println("Hello");
		}
		
		
		try{
			App.toUpate=DetailsJsonHandler.checkUpdateAvaialable();
		}catch(Exception e) {
			e.printStackTrace();
		}

		
	}
	
}

public class App extends Application {

	private static Scene scene;
	public static boolean toUpate=false;

	@Override
	public void stop() {
		try {
			super.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Hello");
		WindowsCommands.deleteWifiProfile();
		QrCode.deleteQr();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//update if update is available
		if(toUpate) {
			try {
				Update.fetchUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void start(Stage stage) throws SQLException, IOException {
		scene = new Scene(loadFXML("SplashScreen"));
		stage.setTitle("DirecT");
		stage.getIcons().add(new Image("file:///..\\src/main/resources/org/openjfx/Icons/Icon.png"));
		stage.setScene(scene);
		stage.show();

	}

	public static void setRoot(String fxml) throws IOException {
		scene.setRoot(loadFXML(fxml));
	}

	private static Parent loadFXML(String fxml) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/org/openjfx/DirecT/View/" + fxml + ".fxml"));
		return fxmlLoader.load();
	}

	public static void main(String[] args) throws SQLException {

		
		new Thread(new checkUpdate()).start();
		
		launch();
	}

}