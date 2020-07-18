package org.openjfx.DirecT;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class App extends Application {

	
	private static Scene scene;

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
		FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/org/openjfx/DirecT/View/"+ fxml +".fxml"));
		return fxmlLoader.load();
	}

	
	
	
	public static void main(String[] args) {

		launch();
	}

}
