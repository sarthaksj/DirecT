package org.openjfx.DirecT.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import org.openjfx.DirecT.App;
import org.openjfx.DirecT.Connection.Connection;
import org.openjfx.DirecT.FlowControl.FlowControlVariables;
import org.openjfx.DirecT.FolderStructure.MakeStructure;
import com.jfoenix.controls.JFXScrollPane;
import animatefx.animation.FadeIn;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;

public class FileSelection implements Initializable {

	@FXML
	private JFXScrollPane scrollPane;

	@FXML
	private Label labelmid;

	@FXML
	private Button selection, send;

	@FXML
	public ProgressIndicator waitRing;

	public static ProgressIndicator waitRing2;

	private static FlowPane tile2;

	private static VBox box2;

	public static List<String> fileChecker;

	private static int count = 0;

	public static List<String> dirList;

	public static List<File> fileList;

	public static HashMap<String, ArrayList<String>> fileListWithParent;// parent,file paths for that parent folder

	public static boolean connectionEstablished;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		new FadeIn(scrollPane).play();

		waitRing.setVisible(false);
		waitRing2 = waitRing;

		fileChecker = new ArrayList<>();
		fileList = new ArrayList<>();
		dirList = new ArrayList<>();
		fileListWithParent = new HashMap<>();

		FlowPane tile = new FlowPane();
		VBox box = null;
		tile.setPadding(new Insets(25, 0, 5, 50));
		tile.setVgap(5);
		tile.setHgap(10);
		tile2 = tile;
		box2 = box;

		scrollPane.setContent(tile);

	}

	private ImageView createImageView(final File imageFile) {

		ImageView imageView = null;
		try {
			final Image image = new Image(new FileInputStream(imageFile), 70, 0, true, true);
			imageView = new ImageView(image);
			imageView.setFitWidth(70);

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		return imageView;
	}

	@FXML
	private void fileChooser() throws Exception {

		// To select all the files
		try {
			// Set extension filter
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("All files", "*.*");

			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select Files To Send");
			fileChooser.getExtensionFilters().add(extFilter);
			/* Show open file dialog to select multiple files. */
			List<File> temp;

			temp = fileChooser.showOpenMultipleDialog(null);
			if (temp == null) {
				return;
			}

			labelmid.setText("");

			for (File f : temp) {

				if (!fileChecker.contains(f.getAbsolutePath())) {

					addFiles(f);
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}

	}

	@FXML
	private void dragDroppedhandle(DragEvent event) throws Exception {

		boolean success = false;
		if (event.getDragboard().hasFiles()) {
			success = true;
			List<File> temp;
			temp = event.getDragboard().getFiles();
			labelmid.setText("");

			for (File f : temp) {

				if (!fileChecker.contains(f.getAbsolutePath())) {

					addFiles(f);
				}
			}

			event.setDropCompleted(success);
			event.consume();
		}
	}

	// set the thumbnail for the given file
	public void setThumbnail(File f, String name, String what) {

		if (what.equals("directory")) {

			String n = name;
			if (n.length() > 28) {
				n = n.substring(0, 26) + "...";
			}
			Text text = new Text(n);
			TextFlow lab = new TextFlow(text);
			lab.setPrefWidth(120);

			box2 = new VBox();
			ImageView imageView;
			String path = "src\\main\\resources\\org\\openjfx\\Icons\\foldericon.png";
			File folder = new File(path);
			imageView = createImageView(folder);
			box2.getChildren().addAll(imageView, lab);
			tile2.getChildren().add(box2);
			VBox.setMargin(lab, new Insets(2, 0, 0, 0));

			return;

		}
		// extract extension from name
		String ext = "";
		{
			for (int i = name.length() - 1;; i--) {
				if (name.charAt(i) == '.') {
					break;
				}
				ext = name.charAt(i) + ext;
			}
		}

		String n = name;
		if (n.length() > 28) {
			n = n.substring(0, 26) + "...";
		}
		Text text = new Text(n);
		TextFlow lab = new TextFlow(text);
		lab.setPrefWidth(120);

		if (ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("jpeg") || ext.equalsIgnoreCase("png")
				|| ext.equalsIgnoreCase("gif") || ext.equalsIgnoreCase("svg") || ext.equalsIgnoreCase("bmp")) {
			box2 = new VBox();
			ImageView imageView;
			String path = "src\\main\\resources\\org\\openjfx\\Icons\\pictureicon.png";
			File folder = new File(path);
			imageView = createImageView(folder);
			box2.getChildren().addAll(imageView, lab);
			tile2.getChildren().add(box2);
		} else if (ext.equalsIgnoreCase("mp4") || ext.equalsIgnoreCase("avi") || ext.equalsIgnoreCase("mkv")
				|| ext.equalsIgnoreCase("vob") || ext.equalsIgnoreCase("jpg")) {
			box2 = new VBox();
			ImageView imageView;
			String path = "src\\main\\resources\\org\\openjfx\\Icons\\videoicon.png";
			File folder = new File(path);
			imageView = createImageView(folder);
			box2.getChildren().addAll(imageView, lab);
			tile2.getChildren().add(box2);
		} else if (ext.equalsIgnoreCase("rar") || ext.equalsIgnoreCase("zip")) {
			box2 = new VBox();
			ImageView imageView;
			String path = "src\\main\\resources\\org\\openjfx\\Icons\\zipicon.png";
			File folder = new File(path);
			imageView = createImageView(folder);
			box2.getChildren().addAll(imageView, lab);
			tile2.getChildren().add(box2);
		} else if (ext.equalsIgnoreCase("pdf")) {
			box2 = new VBox();
			ImageView imageView;

			String path = "src\\main\\resources\\org\\openjfx\\Icons\\pdficon.png";
			File folder = new File(path);
			imageView = createImageView(folder);
			box2.getChildren().addAll(imageView, lab);
			tile2.getChildren().add(box2);
		} else if (ext.equalsIgnoreCase("mp3") || ext.equalsIgnoreCase("wav") || ext.equalsIgnoreCase("wma")
				|| ext.equalsIgnoreCase("amr") || ext.equalsIgnoreCase("aac") || ext.equalsIgnoreCase("3gp")) {
			box2 = new VBox();
			ImageView imageView;
			String path = "src\\main\\resources\\org\\openjfx\\Icons\\musicicon.png";
			File folder = new File(path);
			imageView = createImageView(folder);
			box2.getChildren().addAll(imageView, lab);
			tile2.getChildren().add(box2);
		} else {
			box2 = new VBox();
			ImageView imageView;
			String path = "src\\main\\resources\\org\\openjfx\\Icons\\docicon.png";
			File folder = new File(path);
			imageView = createImageView(folder);
			box2.getChildren().addAll(imageView, lab);
			tile2.getChildren().add(box2);
		}
		VBox.setMargin(lab, new Insets(2, 0, 0, 0));
	}

	public void addFiles(File f) {

		if (f.isDirectory()) {

			fileChecker.add(f.getAbsolutePath());

			String n = f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf("\\") + 1);
			dirList.add(n);
			MakeStructure.senderDirHandler(f.getAbsolutePath(), n);

			// thumbnail has to be set
			count++;

			String name = (Integer.toString(count) + ". "
					+ f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf("\\") + 1));

			setThumbnail(f, name, "directory");
		} else {

			fileChecker.add(f.getAbsolutePath());

			fileList.add(f);

			Object ob = fileListWithParent.get("no parent");
			if (ob != null) {
				// add in the existing array list
				@SuppressWarnings("unchecked")
				ArrayList<String> ar = (ArrayList<String>) ob;
				ar.add(f.getAbsolutePath());
				fileListWithParent.put("no parent", ar);
			} else {
				// create new arraylist
				ArrayList<String> ar = new ArrayList<>();
				ar.add(f.getAbsolutePath());
				fileListWithParent.put("no parent", ar);
			}

			count++;
			String name = (Integer.toString(count) + ". "
					+ f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf("\\") + 1));

			setThumbnail(f, name, "file");
		}

	}

	@FXML
	private void dragOverhandle(DragEvent event) throws Exception {

		if (event.getDragboard().hasFiles()) {
			event.acceptTransferModes(TransferMode.ANY);
		} else {
			event.consume();
		}
	}

	// Sending Logic Here

	@FXML
	private void sendFiles() throws Exception {

		if (fileList.isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("No Files Selected");
			alert.setHeaderText("");
			alert.setContentText("Select Atleast One File To Send");
			alert.showAndWait();
			return;
		}

		// establish a conection to the sender and delete from sender progress;
		if (connectionEstablished == false) {

			waitRing2.setVisible(true);
			// call a thread to establish a connection which will set connectionEstablished
			// to true to proceed
			waitRing();

		} else {
			App.setRoot("SenderProgress");

		}
	}

	private void waitRing() {
		Service<Void> waitRing = new Service<Void>() {
			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {
					@Override
					protected Void call() throws Exception {

						
						while(!Connection.senderConnected) {
							Thread.sleep(100);
						}
										
						connectionEstablished = true;
						waitRing2.setVisible(false);
						try {
							System.out.println("Program reched sender progress0");
							App.setRoot("SenderProgress");
						} catch (Exception e) {
							e.printStackTrace();
						}

						return null;
					}

				};
			}

		};
		waitRing.start();
	}

}
