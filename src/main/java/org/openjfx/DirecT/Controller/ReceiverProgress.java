package org.openjfx.DirecT.Controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import org.openjfx.DirecT.App;
import org.openjfx.DirecT.Connection.Connection;
import org.openjfx.DirecT.FlowControl.DetailsJsonHandler;
import org.openjfx.DirecT.FlowControl.FlowControlVariables;
import org.openjfx.DirecT.SenderReceivingPackage.ProgressIndicatorHandler;
import org.openjfx.DirecT.SenderReceivingPackage.SendingReceivingLogic;
import org.pdfsam.ui.FillProgressIndicator;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import animatefx.animation.FadeIn;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

class ReceiveThread  {

	public void startReceving() {

		try {

			// receive the total size
			String size = Connection.dis.readUTF();

			String count = Connection.dis.readUTF();

			int c = Integer.parseInt(count);

			// dis.close();

			long total = Long.parseLong(size);

			receive(c, total);

			ProgressIndicatorHandler.setPorgress(ReceiverProgress.circularProgress, 100);

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		ReceiverProgress.send2.setVisible(true);
		ReceiverProgress.disconnect2.setVisible(true);
		// now listen for send button
		String s = "";
		try {
			s = Connection.dis.readUTF();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (s.equals("send")) {
		
			Platform.runLater(new Runnable() {
				@Override
				public void run() {

					try {
						Connection.dos.writeUTF("OK");

						App.setRoot("ReceiverProgress");
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			});
			

		} else if (s.equals("disconnect")) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					FlowControlVariables.sendReceive = false;

					try {
						Connection.dos.writeUTF("OK");

						App.setRoot("UsersSelection");
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			});
		}
	}

	public static void receive(int count, long total) {

		long prev = 0;
		for (int i = 1; i <= count; i++) {

			try {

				prev = SendingReceivingLogic.receive(ReceiverProgress.folderPath, Connection.socket, total, prev);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}
}

public class ReceiverProgress implements Initializable {

	@FXML
	private VBox vBox;

	@FXML
	private JFXListView<String> filelList;

	public static JFXListView<String> fileList2;

	@FXML
	private ProgressIndicator waitRing;
	
	@FXML
	private Label username;

	@FXML
	private JFXButton send;

	@FXML
	private JFXButton disconnect;

	public static JFXButton send2;
	public static JFXButton disconnect2;

	@FXML
	private StackPane progressPane;

	public static String folderPath;

	public static FillProgressIndicator circularProgress;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		username.setText(DetailsJsonHandler.getName());
		fileList2 = filelList;

		send2 = send;
		disconnect2 = disconnect;
		send.setVisible(false);
		disconnect.setVisible(false);
		new FadeIn(vBox).play();

		FillProgressIndicator fpi = new FillProgressIndicator();

		vBox.getChildren().add(fpi);

		circularProgress = fpi;

		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Select Location To Save Files");
		File selectedDirectory = chooser.showDialog(null);
		folderPath = selectedDirectory.getAbsolutePath();
		try {
			if (!FlowControlVariables.sendReceive) {
				Connection.connectToServer();

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		ProgressIndicatorHandler.setPorgress(circularProgress, 0);
		getDirectoryStructure();

	}
	
	private void getDirectoryStructure() {
		
		Service<Void> dir = new Service<Void>() {
			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						circularProgress.setVisible(false);
						waitRing.setVisible(true);
						// get directory structure
						try {

							while (true) {

								String str = Connection.dis.readUTF();
								if (str.equals(" completed ")) {

									break;
								} else {

									String path = folderPath + "//" + str;
									File f = new File(path);
									f.mkdir();
								}

							}
						} catch (Exception e) {
							e.printStackTrace();
						}

						receive();
						waitRing.setVisible(false);
						circularProgress.setVisible(true);


						FlowControlVariables.sendReceive = true;
						
						return null;
					}

				};
			}

		};
		dir.start();

		
	}

	private void receive() {

		Service<Void> receive = new Service<Void>() {
			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						ReceiveThread obj=new ReceiveThread();
						obj.startReceving();
						return null;
					}

				};
			}

		};
		receive.start();

	}

	@FXML
	private void sendFiles() {

		try {

			Connection.dos.writeUTF("send");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			App.setRoot("FileSelection");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	private void disconnect() {
		FlowControlVariables.sendReceive = false;
		try {
			Connection.dos.writeUTF("disconnect");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {

			Connection.socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {

			Connection.serverSocket.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			App.setRoot("UsersSelection");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
