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
				"This software holds two types of network connectivity i.e. Local Area Network and Wide Area Network\n"
						+ "In Local Area Network, Sharing of files take place over wifi connection within nearby range of about 5 mtrs, It doesn't require any internet connection\n"
						+ "Whereas in Wide Area Network, both devices require an active internet connection for pairing\n"
						+ "This Software helps to share file in both Local Area Network And Wide Area Network, And the files shared can be of any type like Videos, Photos, Music, Softwares etc.\n"
						+ "It also allows a complete user friendly interface which makes sharing of files more efficient, accurate and easy along with very high speed.\n");
		about.getChildren().add(text_about);
	}

	@FXML
	private void back() throws IOException {
		BackHandler.UsersSelection();
	}

}
