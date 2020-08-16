package org.openjfx.DirecT;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import org.openjfx.DirecT.Commands.WindowsCommands;
import org.openjfx.DirecT.Connection.Connection;
import org.openjfx.DirecT.Controller.QrCode;
import org.openjfx.DirecT.FlowControl.DetailsJsonHandler;
import org.openjfx.DirecT.Update.Update;

class CheckUpdate {

	public void update() {

		try {
			// DetailsJsonHandler.userCount();// increase the count to the update
		} catch (Exception e) {

		}


	}

}

public class App extends Application {

	private static Scene scene;
	public static boolean toUpdate = false;
	public static boolean firstTime = false;
	public static FileInputStream input;

	@Override
	public void stop() throws IOException {
		
		try {
			super.stop();
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		try {

			Connection.socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {

			Connection.serverSocket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		WindowsCommands.deleteWifiProfile();
		QrCode.deleteQr();
		
		// update if update is available

		if (toUpdate) {
			try {
				Update.fetchUpdate();
				DetailsJsonHandler.setNewVersion();

			} catch (Exception e) {
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

	private static void updater() {

		Service<Void> emailService = new Service<Void>() {
			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						CheckUpdate updt = new CheckUpdate();
						updt.update();
						return null;
					}

				};
			}

		};
		emailService.start();
	}

	public static void main(String[] args) throws Exception {
		

		firstTime = DetailsJsonHandler.ifFirstTime();// to check if the app is opened first time and to increase
		//count every time
		
	//	updater();
		launch();

	}
}
