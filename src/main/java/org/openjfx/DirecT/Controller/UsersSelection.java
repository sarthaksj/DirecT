package org.openjfx.DirecT.Controller;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import org.openjfx.DirecT.App;
import org.openjfx.DirecT.Commands.WindowsCommands;
import org.openjfx.DirecT.Connection.Connection;
import org.openjfx.DirecT.FlowControl.DetailsJsonHandler;
import org.openjfx.DirecT.FlowControl.FlowControlVariables;
import animatefx.animation.FadeIn;
import animatefx.animation.FadeOut;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class UsersSelection implements Initializable {

	@FXML
	private AnchorPane drawerPane;
	@FXML
	private Button drawerIcon;
	@FXML
	private Button sender, receiver;
	@FXML
	private StackPane mainPane, opacityPane;
	@FXML
	private Label username;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		new FadeIn(mainPane).play();
		username.setText(DetailsJsonHandler.getName());
		opacityPane.setVisible(false);

		FlowControlVariables.sender = false;
		FlowControlVariables.receiver = false;
		FlowControlVariables.laptop = false;
		FlowControlVariables.mobile = false;
		FlowControlVariables.sendReceive = false;
		Connection.receiverConnected = false;
		Connection.senderConnected = false;
		FileSelection.connectionEstablished = false;

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

		try {
			Connection.socket.close();

		} catch (Exception e) {

		}

		try {
			Connection.serverSocket.close();

		} catch (Exception e) {

		}

	}

	@FXML
	private void sender() throws IOException {

		FlowControlVariables.sender = true;

		// check if sender is connected to a wifi
		boolean isConnected = true;
		try {
			isConnected = WindowsCommands.connectedToWifi();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!isConnected) {

			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("No Connection Available");
			alert.setHeaderText("");
			alert.setContentText("Please make sure you are connected to a wifi network");
			alert.showAndWait();
			return;

		}

		// has to open a hotspot here now
		try {
			WindowsCommands.openHotspot();
		} catch (Exception e) {

		}

		Service<Void> server = new Service<Void>() {
			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {
					@Override
					protected Void call() throws Exception {

						// open the serverSocket and accept any incoming connections
						try {
							if (!FlowControlVariables.sendReceive)
								Connection.openServer();
						} catch (Exception e) {
							e.printStackTrace();
						}

						return null;
					}

				};
			}

		};
		server.start();
		App.setRoot("FileSelection");
	}

	@FXML
	private void receiver() throws IOException {

		FlowControlVariables.receiver = true;
		new FadeOut(mainPane).play();
		try {

			// so that receiver can connect to sender's hotspot
			WindowsCommands.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		App.setRoot("WifiDevices");
	}

	@FXML
	public void feedback() throws IOException, URISyntaxException {
		Desktop d = Desktop.getDesktop();
		d.browse(new URI("https://forms.gle/3Zj1JEpKbSV8jMQa6"));

	}

	@FXML
	private void about() throws IOException {

		App.setRoot("About");
	}

	@FXML
	private void updates() throws Exception {
		// check if sender is connected to a wifi
		boolean isConnected = true;
		try {
			isConnected = WindowsCommands.connectedToWifi();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!isConnected) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("No Internet Connection Available");
			alert.setHeaderText("");
			alert.setContentText("Please Make Sure You Are Connected To A Internet Network");
			alert.showAndWait();
			return;

		}

		App.setRoot("Update");
	}
}
