package org.openjfx.DirecT.Controller;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import org.openjfx.DirecT.App;
import org.openjfx.DirecT.FlowControl.DetailsJsonHandler;

public class SplashScreen implements Initializable {

	@FXML
	private AnchorPane rootPane;

	@FXML
	private ImageView imgView;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		FileInputStream input = null;
		try {
			input = new FileInputStream("src/main/resources/org/openjfx/Icons/Icon.jpg");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		imgView.setImage(new Image(input));
		try {
			input.close();
		} catch (IOException e1) {

			e1.printStackTrace();
		}

		TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(.30), rootPane);
		translateTransition.setByX(0);
		translateTransition.play();

		translateTransition.setOnFinished(event -> {
			try {

				if (App.firstTime) {
					App.setRoot("AppIntro");
				} else {

					// increase count
					DetailsJsonHandler.increaseCount();
					App.setRoot("UsersSelection");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

	}

}
