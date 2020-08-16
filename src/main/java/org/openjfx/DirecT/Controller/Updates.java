package org.openjfx.DirecT.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import org.openjfx.DirecT.App;
import org.openjfx.DirecT.Database.DbConnection;
import org.openjfx.DirecT.FlowControl.BackHandler;
import org.openjfx.DirecT.FlowControl.DetailsJsonHandler;
import org.openjfx.DirecT.Update.Update;
import org.pdfsam.ui.RingProgressIndicator;
import animatefx.animation.FadeIn;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class Updates implements Initializable {
	@FXML
	private StackPane mainPane;
	@FXML
	private Label msg;

	public static RingProgressIndicator ringProgress;
	public static StackPane mainPane2;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		new FadeIn(mainPane).play();
		RingProgressIndicator rpi = new RingProgressIndicator();
		ringProgress = rpi;
		mainPane2 = mainPane;

		if (DetailsJsonHandler.con == null) {
			DetailsJsonHandler.con = DbConnection.databaseConnectivity();
		}
		updateApp();

	}

	private void updateApp() {
		Service<Void> updateService = new Service<Void>() {
			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						mainPane.getChildren().add(ringProgress);
						ringProgress.makeIndeterminate();

						msg.setText("Checking For Updates...");

						App.toUpdate = DetailsJsonHandler.checkUpdateAvaialable();
						System.out.println(App.toUpdate);
						if (App.toUpdate) {

							ringProgress.setProgress(0);

							msg.setText("Fetching Updates From the Server\n" + "        Please Wait...");

							Platform.runLater(new Runnable() {

								@Override
								public void run() {
									msg.setText("Fetching Updates From the Server\n" + "        Please Wait...");

								}
							});

							Update.fetchUpdate();
							
						} else {
							System.out.println("inside checker");
							ringProgress.setVisible(false);

							Platform.runLater(new Runnable() {

								@Override
								public void run() {
									msg.setText("\t\t\t\t\tHurrah!!!\n"
											+ "You're Updated With The Latest Version Of The Application");

								}
							});

							Thread.sleep(1000);
							App.setRoot("UsersSelection");
						}

						return null;
					}
				};
			}

		};
		updateService.start();
		updateService.setOnSucceeded(null);
	}

	@FXML
	private void back() throws IOException {
		BackHandler.UsersSelection();
	}

}
