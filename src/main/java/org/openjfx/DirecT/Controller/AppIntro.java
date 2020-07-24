package org.openjfx.DirecT.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import org.openjfx.DirecT.App;
import org.openjfx.DirecT.FlowControl.DetailsJsonHandler;

public class AppIntro implements Initializable {

	@FXML
	private BorderPane mainPane;

	@FXML
	private StackPane pane1;

	@FXML
	private TextField username;

	public static String name;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@FXML
	void nextAction(ActionEvent event) {
		name = username.getText();
		if (name.equals("")) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Username can't be empty");
			alert.setHeaderText("");
			alert.setContentText("Please type in the Username");
			alert.showAndWait();

			return;
		}
		try {

			DetailsJsonHandler.setName(name);
			// increase count
			DetailsJsonHandler.increaseCount();

			App.setRoot("UsersSelection");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
