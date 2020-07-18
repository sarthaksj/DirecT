package org.openjfx.DirecT;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import org.openjfx.DirecT.Commands.WindowsCommands;
import org.openjfx.DirecT.Controller.QrCode;

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
		//WindowsCommands.deleteWifiProfile();
		QrCode.deleteQr();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	@Override
	public void start(Stage stage) throws IOException {
		scene = new Scene(loadFXML("SplashScreen"));
		stage.setTitle("DirecT");
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

	public static void main(String[] args) {

		launch();
	}
	


}
