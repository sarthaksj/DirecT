package org.openjfx.DirecT.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import org.openjfx.DirecT.FlowControl.BackHandler;
import animatefx.animation.FadeIn;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class About implements Initializable {
	

	@FXML
	public TextFlow about;


	@Override
	public void initialize(URL url, ResourceBundle rb) {
		new FadeIn(about).play();
		
		Text text_about = new Text(
				"This software can be used to share files and folders wirelessly i.e. in Local Area Network\n"
						+ "Sharing of files take place over wifi connection within nearby range of about 5 mtrs\n"
						+ "It also allows a complete user friendly interface which makes sharing of files more efficient, accurate and easy along with very high speed.\n");
		
		about.getChildren().add(text_about);
	}

	@FXML
	private void back() throws IOException {
		BackHandler.UsersSelection();
	}

}
