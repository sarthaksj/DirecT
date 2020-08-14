package org.openjfx.DirecT.Controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map.Entry;
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
import javafx.scene.layout.VBox;

class SendThread {

	public void startSending() {

		try {

			// first send the total size of all the files for progress bar
			long total = 0;
			int count = 0;// to tell receiver the number of times of the loop
			for (File f : FileSelection.fileList) {
				total += f.length();
				count++;
			}

			// convert total to string
			String size = Long.toString(total);
			Connection.dos.writeUTF(size);// total size sent

			String c = Integer.toString(count);
			Connection.dos.writeUTF(c);

			send(total);// to send the file
			ProgressIndicatorHandler.setPorgress(SenderProgress.circularProgress, 100);

		} catch (Exception e) {
			e.printStackTrace();
		}

		SenderProgress.send2.setVisible(true);
		SenderProgress.disconnect2.setVisible(true);

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

	public static void send(long total) throws Exception {

		long sent = 0;
		// loop over all the files in fileListWithParent of FileSelection

		for (Entry<String, ArrayList<String>> entry : FileSelection.fileListWithParent.entrySet()) {

			String key = entry.getKey();// key contains parent

			for (String value : FileSelection.fileListWithParent.get(key)) {

				// value contains the absoulte path

				sent = SendingReceivingLogic.send(value, Connection.socket, total, sent, key);

				String name = value.substring(value.lastIndexOf("\\") + 1);// extract name of the file

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						SenderProgress.fileList2.getItems().addAll(name);

					}
				});

			}

		}
	}

}

public class SenderProgress implements Initializable {

	@FXML
	private VBox vBox;

	@FXML
	private JFXListView<String> filelList;

	public static JFXListView<String> fileList2;
	@FXML
	private Label username;

	@FXML
	private JFXButton send;
	@FXML
	private ProgressIndicator waitRing;

	@FXML
	private JFXButton disconnect;

	@SuppressWarnings("exports")
	public static JFXButton send2;
	@SuppressWarnings("exports")
	public static JFXButton disconnect2;

	@SuppressWarnings("exports")
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

		ProgressIndicatorHandler.setPorgress(circularProgress, 0);

		sendDirectoryStructure();

	}

	private void sendDirectoryStructure() {

		Service<Void> dir = new Service<Void>() {
			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {
					@Override
					protected Void call() throws Exception {

						circularProgress.setVisible(false);
						waitRing.setVisible(true);

						// first send directory structure to receiver to make similar directory
						// structure
						for (String s : FileSelection.dirList) {
							try {
								Connection.dos.writeUTF(s);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						// to tell receiver that directory structure is completed
						try {
							Connection.dos.writeUTF(" completed ");
						} catch (IOException e) {
							e.printStackTrace();
						}

						send();
						FlowControlVariables.sendReceive = true;
						waitRing.setVisible(false);

						circularProgress.setVisible(true);

						return null;
					}

				};
			}

		};
		dir.start();

	}

	private void send() {
		Service<Void> send = new Service<Void>() {
			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {
					@Override
					protected Void call() throws Exception {

						new SendThread().startSending();

						return null;
					}

				};
			}

		};
		send.start();

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
