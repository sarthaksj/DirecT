package org.openjfx.DirecT.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import org.openjfx.DirecT.App;
import org.openjfx.DirecT.Commands.WindowsCommands;
import org.openjfx.DirecT.FlowControl.BackHandler;
import org.openjfx.DirecT.FlowControl.DetailsJsonHandler;
import org.openjfx.DirecT.FlowControl.FlowControlVariables;
import animatefx.animation.FadeIn;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class DeviceSelection implements Initializable {

	@FXML
	private AnchorPane opacityPane, drawerPane;
	@FXML
	private Button drawerIcon;
	@FXML
	private Button mobile, laptop;
	@FXML
	private StackPane mainPane;
	@FXML
	private Label username;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		new FadeIn(mainPane).play();

		username.setText(DetailsJsonHandler.getName());
		opacityPane.setVisible(false);

		FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.2), opacityPane);
		fadeTransition.setFromValue(1);
		fadeTransition.setToValue(0);
		fadeTransition.play();

		TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.2), drawerPane);
		translateTransition.setByX(-700);
		translateTransition.play();

		drawerIcon.setOnMouseClicked(event -> {
			drawerPane.setVisible(true);
			if (opacityPane.isVisible() == false) {
				opacityPane.setVisible(true);

				FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(0.2), opacityPane);
				fadeTransition1.setFromValue(0);
				fadeTransition1.setToValue(0.15);
				fadeTransition1.play();

				TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.2), drawerPane);
				translateTransition1.setByX(+700);
				translateTransition1.play();
			}
		});

		opacityPane.setOnMouseClicked(event -> {

			if (opacityPane.isVisible() == true) {

				FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(0.2), opacityPane);
				fadeTransition1.setFromValue(0.15);
				fadeTransition1.setToValue(0);
				fadeTransition1.play();

				fadeTransition1.setOnFinished(event1 -> {
					opacityPane.setVisible(false);
				});

				TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.2), drawerPane);
				translateTransition1.setByX(-700);
				translateTransition1.play();
			}
		});

	}

	@FXML
	private void mobile() throws IOException {

		App.setRoot("QrCode");
	}

	@FXML
	private void laptop() throws IOException {

		FlowControlVariables.laptop = true;

		if (FlowControlVariables.sender) {

			// opens a hotspot
			try {
				WindowsCommands.openHotspot();
			} catch (Exception e) {

			}

			App.setRoot("FileSelection");

		} else if (FlowControlVariables.receiver) {

			App.setRoot("WifiDevices");
		}

	}

	@FXML
	private void back() throws IOException {

		BackHandler.UsersSelection();
	}

	@FXML
	private void about() throws IOException {

		App.setRoot("About");
	}
}
