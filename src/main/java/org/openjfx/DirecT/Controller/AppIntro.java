package org.openjfx.DirecT.Controller;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import org.openjfx.DirecT.App;
import org.openjfx.DirecT.FlowControl.DetailsJsonHandler;

import com.jfoenix.controls.JFXButton;
import animatefx.animation.FadeIn;

public class AppIntro implements Initializable {

	@FXML
	private BorderPane mainPane;

	@FXML
	private StackPane pane1;

	@FXML
	private VBox pane2;

	@FXML
	private Label countLabel;

	@FXML
	private JFXButton prev;

	@FXML
	private JFXButton nxt;

	@FXML
	private TextField username;

	public static String name;

	public void translateAnimation(double duration, @SuppressWarnings("exports") Node node, double byX) {

		TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(duration), node);
		translateTransition.setByX(byX);
		translateTransition.play();

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		prev.setVisible(false);
		new FadeIn(pane1).play();
		translateAnimation(0.1, pane2, 1600);
	}

	int showSlide = 0;

	@FXML
	void nextAction(ActionEvent event) {
		prev.setVisible(true);

		if (prev.isVisible() == true) {
			new FadeIn(prev).play();
		}

		if (showSlide == 0) {
			translateAnimation(0.2, pane2, -1600);
			showSlide++; // showSlide=1
			countLabel.setText("2/2");
		} else {
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
				//increase count
				DetailsJsonHandler.increaseCount();
				
				App.setRoot("UsersSelection");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@FXML
	private void backAction(ActionEvent event) {
		prev.setVisible(false);

		if (showSlide == 0) {
		} else if (showSlide == 1) {
			translateAnimation(0.2, pane2, 1600);
			showSlide--; // showSlide=0
			countLabel.setText("1/2");
		}

	}

}
