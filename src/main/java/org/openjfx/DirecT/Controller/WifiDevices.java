package org.openjfx.DirecT.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import org.openjfx.DirecT.App;
import org.openjfx.DirecT.Commands.WindowsCommands;
import org.openjfx.DirecT.FlowControl.BackHandler;
import org.openjfx.DirecT.FlowControl.DetailsJsonHandler;
import com.jfoenix.controls.JFXButton;
import animatefx.animation.FadeIn;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

class RunCommands implements Runnable {

	@Override
	public void run() {
		try {

			WindowsCommands.deleteProfile();
			WindowsCommands.connectProfile();
			WindowsCommands.connect();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}



public class WifiDevices implements Initializable {

	@FXML
	private PasswordField passField;
	@FXML
	private ScrollPane wifiScroll;
	@FXML
	private VBox vBoxCont;
	@FXML
	private JFXButton button;
	@FXML
	private Label username;
	private VBox wifiPane;

	public static boolean toRefresh;

	private static List<String> displayedNetworks;

	public static List<String> threadNetworks;

	private static HashMap<String, String> usersNetwork;

	Scene scene;

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		try {
			WindowsCommands.disconnect();//to ensure if the receiver is not connected to any netwrok double check
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		
		new FadeIn(vBoxCont).play();
		username.setText(DetailsJsonHandler.getName());
		usersNetwork = new HashMap<>();

		wifiPane = new VBox();

		toRefresh = true;
		displayedNetworks = new ArrayList<>();
		threadNetworks = new ArrayList<>();

		Runnable task = new Runnable() {
			public void run() {
				try {
					deviceInfo();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		// Run the task in a background thread
		Thread backgroundThread = new Thread(task);
		// Terminate the running thread if the application exits
		backgroundThread.setDaemon(true);
		// Start the thread
		backgroundThread.start();

	}

	private void deviceInfo() throws Exception {

		// for (String network : WindowsCommands.availableNetworks()) {

		while (toRefresh) {

			for (String n : WindowsCommands.availableNetworks()) {
				// first add to threadNetworks
				threadNetworks.add(n);
			}

			for (String network : threadNetworks) {

				if (!displayedNetworks.contains(network)) {

					displayedNetworks.add(network);
					String password = "";
					// network name has to be refined to get name of the user if the network
					// contains _dir
					if (network.contains("_DrT")) {
						int index = network.indexOf("_DrT");
						password = network.substring(index + 4);
						network = network.substring(0, index);
						usersNetwork.put(password, network);
						

					} else {
						continue;
					}

					String name = network;
					String pass = password;

					Platform.runLater(new Runnable() {
						@SuppressWarnings("static-access")
						@Override
						public void run() {
							button = new JFXButton(name);
							button.setId(pass);
							wifiPane.getChildren().addAll(button);

							EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
								public void handle(ActionEvent e) {

									
									toRefresh=false;
									WindowsCommands.name = name + "_DrT" + pass;
									WindowsCommands.password = pass;

									Thread t = new Thread(new RunCommands());
									t.start();
									try {
										Thread.sleep(500);
									} catch (InterruptedException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									try {
										App.setRoot("ReceiverProgress");
									} catch (IOException e2) {
										e2.printStackTrace();
									}

								}

							};

							wifiScroll.getStylesheets()
									.add(getClass().getResource("/org/openjfx/DirecT/View/style.css").toExternalForm());
							button.getStyleClass().add("wifi-button");
							wifiPane.setMargin(button, new Insets(5, 0, 0, 0));
							wifiPane.getStyleClass().add("wifi-panel");
							button.setMnemonicParsing(false);
							button.setOnAction(event);
							wifiScroll.setContent(wifiPane);

						}
					});

				}

			}
			Thread.sleep(2000);

		}

	}

	@FXML
	private void back() throws IOException {

		BackHandler.ThreadBreak();
	}
}
